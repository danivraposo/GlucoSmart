@file:OptIn(ExperimentalMaterial3Api::class)
package com.francisco.glucosmart.ui.theme.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.francisco.glucosmart.R
import com.francisco.glucosmart.ui.theme.navigation.Routes

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // Logo
        Icon(
            painter = painterResource(id = R.drawable.simplification1),
            contentDescription = "Logo",
            tint = Color(0xFF00A99D),
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        // App Name
        Text(
            "GlucoSmart", 
            fontSize = 36.sp, 
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3050)
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Email Field
        Text(
            "Email",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1E3050),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("") },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF00A99D)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Password Field
        Text(
            "Password",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1E3050),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )
        
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            placeholder = { Text("") },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF00A99D)
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Login Button
        Button(
            onClick = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CA1D5))
        ) {
            Text(
                "Log In", 
                color = Color.White, 
                fontSize = 20.sp, 
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Forgot Password
        TextButton(
            onClick = { /* Recuperar senha futuramente */ },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                "Forgot password?", 
                fontSize = 18.sp,
                color = Color(0xFF1E3050)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Sign Up Button
        TextButton(
            onClick = {
                navController.navigate(Routes.SIGNUP)
            },
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                "Sign Up", 
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1E3050)
            )
        }
    }
}
