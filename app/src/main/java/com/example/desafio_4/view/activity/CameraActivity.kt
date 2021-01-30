package com.example.desafio_4.view.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.desafio_4.R
import com.example.desafio_4.databinding.ActivityAddAndEditGameBinding
import com.example.desafio_4.databinding.ActivityCameraBinding
import com.example.desafio_4.model.Game
import com.example.desafio_4.utils.Constants
import com.example.desafio_4.utils.Constants.AdapterFields.ID
import com.example.desafio_4.utils.Constants.AdapterFields.PHOTO
import com.example.desafio_4.utils.Constants.CameraX.FILENAME_FORMAT
import com.example.desafio_4.utils.Constants.CameraX.REQUEST_CODE_PERMISSIONS
import com.example.desafio_4.utils.Constants.CameraX.REQUIRED_PERMISSIONS
import com.example.desafio_4.utils.Constants.CameraX.TAG_CAMERA
import com.example.desafio_4.utils.Constants.Firebase.DATABASE_GAMES
import com.example.desafio_4.utils.Constants.Firebase.ID_GAME
import com.example.desafio_4.utils.Constants.Firebase.ORIGIN_INTENT
import com.example.desafio_4.viewModel.CameraViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.HashMap

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var viewModelCamera: CameraViewModel
    var getId = ""
    var getOrigin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelCamera = ViewModelProvider(this).get(CameraViewModel::class.java)
        getId = intent.getStringExtra(ID_GAME).toString()
        getOrigin = intent.getIntExtra(ORIGIN_INTENT, 0)

        if(getOrigin == 1) {
            viewModelCamera.getCollectionSize()
            viewModelCamera.collectionSize.observe(this) {
                getId = it.toString()
            }
        }

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        // Set up the listener for take photo button
        binding.cameraCaptureButton.setOnClickListener {
            takePhoto()
        }

        binding.btGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 0)
        }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val targetUri = data?.data
        if(resultCode == Activity.RESULT_OK) {
            try {
                targetUri?.let {
                    val msg = "Foto salva com Sucesso!! Aguarde..."

                    viewModelCamera.putFileToStorage(it, getId.toInt())
                    viewModelCamera.getUri.observe(this) {uri ->
                        when(getOrigin) {
                            1 -> { setUpByOrigin(true, uri) }
                            2 -> { setUpByOrigin(false, uri) }
                        }
                    }
                    Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
                }
            } catch (exception: FileNotFoundException) {
                exception.printStackTrace()
            }
        }
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            "gamePhoto.jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()


        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG_CAMERA, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Foto tirada com Sucesso!! Aguarde..."

                    viewModelCamera.putFileToStorage(savedUri, getId.toInt())
                    viewModelCamera.getUri.observe(this@CameraActivity) {uri ->
                        when(getOrigin) {
                            1 -> { setUpByOrigin(true, uri) }
                            2 -> { setUpByOrigin(false, uri) }
                        }
                    }
                    Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun setUpByOrigin(game: Boolean, uri: Uri) {
        if(game) {
            val gameSet = Game(getId.toInt(), uri.toString(), "", "", "")
            viewModelCamera.setGameValue(gameSet, getId.toInt())
            viewModelCamera.onSucess.observe(this) {
                if(it) {
                    startAddGameActivity(3)
                }
            }
        }else {
            val photoSet = hashMapOf(PHOTO to uri.toString())
            viewModelCamera.setPhotoValue(photoSet, getId.toInt())
            viewModelCamera.onSucess.observe(this) {
                if(it) {
                    startAddGameActivity(2)
                }
            }
        }
    }

    private fun startAddGameActivity(origin: Int) {
        val intent = Intent(this@CameraActivity, AddAndEditGameActivity::class.java)
        intent.putExtra(ORIGIN_INTENT, origin)
        intent.putExtra(ID_GAME, getId)
        startActivity(intent)
        finish()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }
            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG_CAMERA, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = Constants.CameraX.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

}