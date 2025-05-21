package com.sourdoughtree.android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import com.amplifyframework.core.Amplify


import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.result.step.AuthSignInStep

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        Amplify.Auth.fetchAuthSession(
            { result ->
                if (result.isSignedIn) {
                    Log.i("AuthCheck", "User is already signed in.")
                    // You could optionally sign them out or skip login

                    Amplify.Auth.signOut {
                        Log.i("AuthCheck", "User signed out")
                    }


                } else {
                    Log.i("AuthCheck", "User not signed in. Proceeding with login.")


                    Amplify.Auth.signIn(
                        "newuser",
                        "NewPassword456!",
                        { result ->
                            if (result.isSignedIn) {
                                Log.i("AuthQuickStart", "Sign in succeeded")
                            } else {
                                val step = result.nextStep.signInStep
                                if (step == AuthSignInStep.CONFIRM_SIGN_IN_WITH_NEW_PASSWORD) {
                                    Log.i("Auth", "Must set new password")

                                    Amplify.Auth.confirmSignIn(
                                        "NewPassword456!",
                                        { result ->
                                            if (result.isSignedIn) {
                                                Log.i("Auth", "Password changed and sign-in completed.")
                                            } else {
                                                Log.i("Auth", "Next step: ${result.nextStep.signInStep}")
                                            }
                                        },
                                        { error ->
                                            Log.e("Auth", "Failed to confirm sign-in with new password", error)
                                        }
                                    )



                                }
                                else {
                                    Log.i("AuthQuickStart", "Sign in not complete")
                                    Log.i("AuthQuickStart", result.toString())
                                }
                            }

                        },
                        { error ->
                            Log.e("AuthQuickStart", "Sign in failed", error)
                        }
                    )


                }
            },
            { error -> Log.e("AuthCheck", "Failed to fetch auth session", error) }
        )






    }
}