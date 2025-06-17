package com.francisco.glucosmart.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

/**
 * Classe para gerenciar o modo daltônico em toda a aplicação
 */
object ColorBlindModeManager {
    // Estado que indica se o modo daltônico está ativado
    val isColorBlindModeEnabled = mutableStateOf(false)
    
    // Função para alternar o modo daltônico
    fun toggleColorBlindMode() {
        isColorBlindModeEnabled.value = !isColorBlindModeEnabled.value
    }
    
    // Cores padrão do aplicativo
    private val defaultPrimaryColor = Color(0xFF00A99D)
    private val defaultBackgroundColor = Color.White
    private val defaultTextColor = Color.Black
    private val defaultCardColor = Color(0xFFE8E8E8)
    
    // Cores para o modo daltônico (alto contraste, amigável para todos os tipos de daltonismo)
    val colorBlindPrimaryColor = Color(0xFF0072B2) // Azul escuro, visível para a maioria dos tipos de daltonismo
    val colorBlindHighlightColor = Color(0xFFFFD700) // Amarelo, visível para a maioria dos tipos de daltonismo
    private val colorBlindBackgroundColor = Color.White
    private val colorBlindTextColor = Color.Black
    private val colorBlindCardColor = Color(0xFFE0E0E0) // Cinza claro para maior contraste
    
    // Função para obter a cor primária atual com base no modo
    fun getPrimaryColor(): Color {
        return if (isColorBlindModeEnabled.value) colorBlindPrimaryColor else defaultPrimaryColor
    }
    
    // Função para obter a cor de destaque com base no modo
    fun getHighlightColor(): Color {
        return if (isColorBlindModeEnabled.value) colorBlindHighlightColor else defaultPrimaryColor
    }
    
    // Função para obter a cor de fundo com base no modo
    fun getBackgroundColor(): Color {
        return if (isColorBlindModeEnabled.value) colorBlindBackgroundColor else defaultBackgroundColor
    }
    
    // Função para obter a cor do texto com base no modo
    fun getTextColor(): Color {
        return if (isColorBlindModeEnabled.value) colorBlindTextColor else defaultTextColor
    }
    
    // Função para obter a cor do card com base no modo
    fun getCardColor(): Color {
        return if (isColorBlindModeEnabled.value) colorBlindCardColor else defaultCardColor
    }
} 