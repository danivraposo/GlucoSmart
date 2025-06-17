@file:OptIn(ExperimentalMaterial3Api::class)
package com.francisco.glucosmart.ui.theme.screens.singup

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
fun SignUpScreen(navController: NavHostController) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Logo
        Icon(
            painter = painterResource(id = R.drawable.simplification1),
            contentDescription = "Logo",
            tint = Color(0xFF00A99D),
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))
        
        // App Name
        Text(
            "GlucoSmart",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3050)
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        // Sign Up Title
        Text(
            "Sign Up",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3050)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Name Field
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            placeholder = { Text("Name") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF00A99D)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF00A99D)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            placeholder = { Text("Password") },
            singleLine = true,
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

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        OutlinedTextField(
            value = confirmar,
            onValueChange = { confirmar = it },
            placeholder = { Text("Confirm password") },
            singleLine = true,
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

        // Create Account Button
        Button(
            onClick = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.SIGNUP) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CA1D5))
        ) {
            Text(
                "Create Account",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Login Link
        Row(
            modifier = Modifier.padding(bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Have an account? ",
                fontSize = 18.sp,
                color = Color(0xFF1E3050)
            )
            
            TextButton(
                onClick = { navController.navigate(Routes.LOGIN) },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "Log in",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4CA1D5)
                )
            }
        }
    }
}
