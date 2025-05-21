package com.sourdoughtree.android

import android.app.Application
import android.util.Log
import com.amplifyframework.core.Amplify
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin

class SourdoughTreeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i("SourdoughApp", "initializing Amplify")
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.i("SourdoughApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("SourdoughApp", "Could not initialize Amplify", error)
        }
    }
}