package com.francisco.glucosmart.ui.theme.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.size
import com.francisco.glucosmart.data.ColorBlindModeManager

@Composable
fun ColorBlindModeIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isColorBlindModeEnabled by ColorBlindModeManager.isColorBlindModeEnabled
    
    // Usar cor diferente quando o modo daltônico estiver ativado
    val iconTint = if (isColorBlindModeEnabled) {
        ColorBlindModeManager.colorBlindHighlightColor
    } else {
        ColorBlindModeManager.getPrimaryColor()
    }
    
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.ColorLens,
            contentDescription = if (isColorBlindModeEnabled) "Desativar modo daltônico" else "Ativar modo daltônico",
            tint = iconTint,
            modifier = Modifier.size(28.dp)
        )
    }
} 