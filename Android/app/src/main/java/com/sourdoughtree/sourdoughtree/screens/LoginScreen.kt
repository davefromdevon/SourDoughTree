package com.sourdoughtree.sourdoughtree.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sourdoughtree.sourdoughtree.auth.CognitoAuthManager
import com.sourdoughtree.sourdoughtree.R

@Composable
fun LoginScreenHost( onLoginSuccess: () -> Unit) {
    var context = LocalContext.current
    var username by remember { mutableStateOf("daveiandixson@gmail.com") }
    var password by remember { mutableStateOf("Password345!") }

    LoginScreen(
        username = username,
        password = password,
        onUsernameChange = { username = it },
        onPasswordChange = { password = it },
        onLoginClick = {
            CognitoAuthManager(context).loginUser(username, password, onLoginSuccess)
        },
        onLogoutClick = {
            Toast.makeText(context, "Logout clicked", Toast.LENGTH_SHORT).show()
            CognitoAuthManager(context).logoutUser(username)
        },
        onSignUpClick = {
            Toast.makeText(context, "Sign up clicked", Toast.LENGTH_SHORT).show()
        }
    )
}

@Composable
fun LoginScreen(
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var passwordVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.app_background))
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.app_background)),
            contentAlignment = Alignment.Center
        ) {

        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.app_background))
                .verticalScroll(scrollState)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Spacer(modifier = Modifier.height(80.dp))

            Image(
                painter = painterResource(id = R.drawable.sourdoughtreelogolarge),
                contentDescription = "App Logo",
                modifier = Modifier.size(160.dp)
            )
            Spacer(modifier = Modifier.height (16.dp))

            Text(
                text = "SourdoughTree",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5E3A1C)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Every starter has a story.",
                fontSize = 16.sp,
                color = Color(0xFF6E4B2C)
            )

            Text(
                text = "Every loaf has a lineage.",
                fontSize = 16.sp,
                color = Color(0xFF6E4B2C),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF6E4B2C),
                    unfocusedIndicatorColor = Color(0xFF6E4B2C),
                    focusedLabelColor = Color(0xFF6E4B2C),
                    unfocusedLabelColor = Color(0xFF6E4B2C),
                    cursorColor = Color(0xFF6E4B2C),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF6E4B2C),
                    unfocusedIndicatorColor = Color(0xFF6E4B2C),
                    focusedLabelColor = Color(0xFF6E4B2C),
                    unfocusedLabelColor = Color(0xFF6E4B2C),
                    cursorColor = Color(0xFF6E4B2C),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                trailingIcon = {
                    val image = if (passwordVisible)
                        painterResource(id = R.drawable.ic_visibility_off)
                    else
                        painterResource(id = R.drawable.ic_visibility)

                    Image(
                        painter = image,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        modifier = Modifier
                            .clickable { passwordVisible = !passwordVisible }
                            .size(24.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6E4B2C))
            ) {
                Text("Login", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "Don't have an account? Sign up",
                color = Color(0xFF5E3A1C),
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .clickable { onSignUpClick() }
                    .padding(top = 8.dp)
            )
        }
        }
    }
}
