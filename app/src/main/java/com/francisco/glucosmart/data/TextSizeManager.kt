package com.francisco.glucosmart.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.sp

/**
 * Classe para gerenciar o tamanho do texto em toda a aplicação
 */
object TextSizeManager {
    // Tamanhos de texto disponíveis
    enum class TextSize(val label: String) {
        SMALL("Pequeno"),
        MEDIUM("Médio"),
        LARGE("Grande")
    }
    
    // Estado que indica o tamanho do texto atual
    val currentTextSize = mutableStateOf(TextSize.MEDIUM)
    
    // Função para definir o tamanho do texto
    fun setTextSize(size: TextSize) {
        currentTextSize.value = size
    }
    
    // Função para obter o tamanho do texto para títulos
    fun getTitleTextSize() = when (currentTextSize.value) {
        TextSize.SMALL -> 18.sp
        TextSize.MEDIUM -> 20.sp
        TextSize.LARGE -> 24.sp
    }
    
    // Função para obter o tamanho do texto para subtítulos
    fun getSubtitleTextSize() = when (currentTextSize.value) {
        TextSize.SMALL -> 16.sp
        TextSize.MEDIUM -> 18.sp
        TextSize.LARGE -> 20.sp
    }
    
    // Função para obter o tamanho do texto para corpo de texto
    fun getBodyTextSize() = when (currentTextSize.value) {
        TextSize.SMALL -> 14.sp
        TextSize.MEDIUM -> 16.sp
        TextSize.LARGE -> 18.sp
    }
    
    // Função para obter o tamanho do texto para texto pequeno
    fun getSmallTextSize() = when (currentTextSize.value) {
        TextSize.SMALL -> 12.sp
        TextSize.MEDIUM -> 14.sp
        TextSize.LARGE -> 16.sp
    }
} 