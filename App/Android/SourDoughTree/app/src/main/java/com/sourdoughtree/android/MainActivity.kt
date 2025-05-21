package com.sourdoughtree.android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            handleLogin()
        }


        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            signOut()
        }


//        Amplify.Auth.fetchAuthSession(
//            { result ->
//                if (result.isSignedIn) {
//                    Log.i("AuthCheck", "User is already signed in.")
//                    // You could optionally sign them out or skip login
//
//                    Amplify.Auth.signOut {
//                        Log.i("AuthCheck", "User signed out")
//                    }
//
//
//                } else {
//                    Log.i("AuthCheck", "User not signed in. Proceeding with login.")
//
//
//                    Amplify.Auth.signIn(
//                        "davedixson@yahoo.com",
//                        "NewPassword456!",
//                        { result ->
//                            if (result.isSignedIn) {
//                                Log.i("AuthQuickStart", "Sign in succeeded")
//                            } else {
//                                val step = result.nextStep.signInStep
//                                if (step == AuthSignInStep.CONFIRM_SIGN_IN_WITH_NEW_PASSWORD) {
//                                    Log.i("Auth", "Must set new password")
//
//                                    Amplify.Auth.confirmSignIn(
//                                        "NewPassword456!",
//                                        { result ->
//                                            if (result.isSignedIn) {
//                                                Log.i("Auth", "Password changed and sign-in completed.")
//                                            } else {
//                                                Log.i("Auth", "Next step: ${result.nextStep.signInStep}")
//                                            }
//                                        },
//                                        { error ->
//                                            Log.e("Auth", "Failed to confirm sign-in with new password", error)
//                                        }
//                                    )
//
//
//
//                                }
//                                else {
//                                    Log.i("AuthQuickStart", "Sign in not complete")
//                                    Log.i("AuthQuickStart", result.toString())
//                                }
//                            }
//
//                        },
//                        { error ->
//                            Log.e("AuthQuickStart", "Sign in failed", error)
//                        }
//                    )
//
//
//                }
//            },
//            { error -> Log.e("AuthCheck", "Failed to fetch auth session", error) }
//        )






    }

    private fun handleLogin() {
        val usernameField = findViewById<EditText>(R.id.usernameField)
        val passwordField = findViewById<EditText>(R.id.passwordField)

        val username = usernameField.text.toString().trim()
        val password = passwordField.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            return
        }

        Amplify.Auth.signIn(
            username,
            password,
            { result ->
                if (result.isSignedIn) {
                    Log.i("AuthLogin", "Sign in succeeded")
                    runOnUiThread {
                        Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()
                        // TODO: Navigate to the next screen
                    }
                } else {
                    Log.i("AuthLogin", "Sign in not complete: ${result.nextStep.signInStep}")

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
                    runOnUiThread {
                        Toast.makeText(this, "Next step: ${result.nextStep.signInStep}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            { error ->
                Log.e("AuthLogin", "Sign in failed", error)
                runOnUiThread {
                    Toast.makeText(this, "Login failed: ${error.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    private fun signOut() {

            Toast.makeText(this, "sign out pressed!", Toast.LENGTH_SHORT).show()

            Amplify.Auth.signOut { result ->
                Log.i("AuthSignOut", "Sign out result: $result")
            }

    }

}