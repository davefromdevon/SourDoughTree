package com.sourdoughtree.sourdoughtree.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler
import com.amazonaws.regions.Regions
import com.sourdoughtree.sourdoughtree.SessionManager


class CognitoAuthManager(context: Context) {

    private val poolID = "eu-west-2_qLiYCgWBU"
    private val clientID = "6nttrcgep5u23cp6cvok3g6j6f"
    private val clientSecret = "cseqoi36cffnrod016chb8km1d600l8fvrt09apadgoo4lacfrp"
    private val awsRegion = Regions.EU_WEST_2  // Change to your actual region

    private val appContext = context.applicationContext
    private val userPool =
        CognitoUserPool(appContext, poolID, clientID, clientSecret, awsRegion)
    private lateinit var userPassword: String

    fun loginUser(username: String, password: String,
                  onLoginSuccess: () -> Unit) {
        val user = userPool.getUser(username)
        userPassword = password
        SessionManager.username=username

        val authHandler = object : AuthenticationHandler {
            override fun onSuccess(session: CognitoUserSession?, newDevice: CognitoDevice?) {
                SessionManager.loggedIn = true
                onLoginSuccess()  // ✅ This now works
            }

            override fun getAuthenticationDetails(continuation: AuthenticationContinuation?, userId: String?) {
                val authDetails = AuthenticationDetails(userId, userPassword, null)
                continuation?.setAuthenticationDetails(authDetails)
                continuation?.continueTask()
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                // Implement if needed
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                Toast.makeText(appContext, "Challenge required", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(exception: Exception?) {
                Toast.makeText(appContext, "Login failed: ${exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                Log.e("CognitoAuth", "Login failed", exception)
            }
        }

        user.getSessionInBackground(authHandler)

    }

    fun logoutUser(username: String) {
        SessionManager.loggedIn=false
        val user = userPool.getUser(username)
        user.signOut()
    }

    fun checkSessionIfLoggedIn(callback: () -> Unit) {
        val user = userPool.currentUser
        user.getSessionInBackground(object : AuthenticationHandler {
            override fun onSuccess(session: CognitoUserSession?, newDevice: CognitoDevice?) {
                if (session?.isValid == true)
                {
                    val user = userPool.currentUser

                    user.getDetailsInBackground(object : GetDetailsHandler {
                        override fun onSuccess(cognitoUserDetails: CognitoUserDetails?) {
                            val attributes = cognitoUserDetails?.attributes
                            var email = attributes?.attributes?.get("email")
                            SessionManager.username=email

                            callback()
                        }

                        override fun onFailure(exception: Exception?) {
                            // Handle error
                        }
                    })




                    callback()
                }

            }

            override fun getAuthenticationDetails(continuation: AuthenticationContinuation?, userId: String?) {
                // No saved password, so cannot continue auth.
                //callback(false)
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                //callback(false)
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                //callback(false)
            }

            override fun onFailure(exception: Exception?) {
                //callback(false)
            }
        })
    }

    }