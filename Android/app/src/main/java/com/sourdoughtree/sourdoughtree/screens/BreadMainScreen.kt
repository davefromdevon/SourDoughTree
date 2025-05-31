package com.sourdoughtree.sourdoughtree.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sourdoughtree.sourdoughtree.R

@Composable
fun BreadMainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.app_background))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.breadslice),
            contentDescription = "Bread Icon",
            modifier = Modifier
                .size(220.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Under Construction...",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF5E3A1C) // Rich brown to match your theme
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Our bakers are still kneading this page!",
            fontSize = 16.sp,
            color = Color(0xFF6E4B2C)
        )
    }
}