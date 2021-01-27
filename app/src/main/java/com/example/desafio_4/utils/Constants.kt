package com.example.desafio_4.utils

import android.Manifest

class Constants {
    object SharedPreferences{
        const val EMAIL_SHAREDPREFERENCE = "email"
        const val PASSWORD_SHAREDPREFERENCE = "password"
        const val CHECKED_SHAREDPREFERENCE = "checked"
        const val NAME_SHAREDPREFERENCE = "sharedPreferencesLogIn"
    }
    object AdapterFields {
        const val RELEASE = "release"
        const val TITLE = "title"
        const val PHOTO = "photo"
        const val DESCRIPTION = "description"
        const val ID = "id"
    }
    object Firebase {
        const val DATABASE_USERS = "users"
        const val DATABASE_GAMES = "gamesList"
        const val ID_GAME = "id"
        const val ORIGIN_INTENT = "origin"
    }
    object CameraX  {
        const val TAG_CAMERA = "CameraXBasic"
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    object VoiceRecognation {
        const val REQUEST_CODE = 999
    }
}