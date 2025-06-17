package com.francisco.glucosmart.ui.theme.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francisco.glucosmart.data.ColorBlindModeManager

@Composable
fun ColorBlindModeButton(
    modifier: Modifier = Modifier,
    onToggle: () -> Unit
) {
    val isColorBlindModeEnabled by ColorBlindModeManager.isColorBlindModeEnabled
    
    val buttonColor = if (isColorBlindModeEnabled) {
        ColorBlindModeManager.colorBlindHighlightColor
    } else {
        ColorBlindModeManager.getPrimaryColor()
    }
    
    val textColor = if (isColorBlindModeEnabled) {
        Color.Black
    } else {
        Color.White
    }
    
    Button(
        onClick = onToggle,
        modifier = modifier
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ColorLens,
            contentDescription = "Alternar modo daltônico",
            tint = textColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isColorBlindModeEnabled) "Desativar Modo Daltônico" else "Ativar Modo Daltônico",
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
} 