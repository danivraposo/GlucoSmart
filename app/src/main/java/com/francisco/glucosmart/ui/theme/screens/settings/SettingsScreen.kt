@file:OptIn(ExperimentalMaterial3Api::class)
package com.francisco.glucosmart.ui.theme.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.francisco.glucosmart.data.ColorBlindModeManager
import com.francisco.glucosmart.data.TextSizeManager
import com.francisco.glucosmart.ui.theme.screens.components.MenuDrawer
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Observar o estado do modo daltônico
    val isColorBlindModeEnabled by ColorBlindModeManager.isColorBlindModeEnabled
    
    // Observar o tamanho do texto atual
    val currentTextSize by TextSizeManager.currentTextSize
    
    // Cores baseadas no modo daltônico
    val primaryColor = ColorBlindModeManager.getPrimaryColor()
    val textColor = ColorBlindModeManager.getTextColor()
    
    MenuDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Definições",
                            fontSize = TextSizeManager.getTitleTextSize(),
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Seção de Aparência
                Text(
                    text = "Aparência",
                    fontSize = TextSizeManager.getSubtitleTextSize(),
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Modo daltônico
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ColorLens,
                                    contentDescription = "Modo daltônico",
                                    tint = if (isColorBlindModeEnabled) ColorBlindModeManager.colorBlindHighlightColor else textColor,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "Modo daltônico",
                                        fontSize = TextSizeManager.getBodyTextSize(),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Cores adaptadas para daltônicos",
                                        fontSize = TextSizeManager.getSmallTextSize(),
                                        color = Color.Gray
                                    )
                                }
                            }
                            Switch(
                                checked = isColorBlindModeEnabled,
                                onCheckedChange = { ColorBlindModeManager.toggleColorBlindMode() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = primaryColor,
                                    checkedTrackColor = primaryColor.copy(alpha = 0.5f)
                                )
                            )
                        }
                        
                        Divider()
                        
                        // Tamanho do texto
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatSize,
                                    contentDescription = "Tamanho do texto",
                                    tint = textColor,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "Tamanho do texto",
                                    fontSize = TextSizeManager.getBodyTextSize(),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                TextSizeManager.TextSize.values().forEach { size ->
                                    Button(
                                        onClick = { TextSizeManager.setTextSize(size) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (currentTextSize == size) primaryColor else Color.LightGray
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = when (size) {
                                                TextSizeManager.TextSize.SMALL -> "A"
                                                TextSizeManager.TextSize.MEDIUM -> "A"
                                                TextSizeManager.TextSize.LARGE -> "A"
                                            },
                                            fontSize = when (size) {
                                                TextSizeManager.TextSize.SMALL -> 14.sp
                                                TextSizeManager.TextSize.MEDIUM -> 18.sp
                                                TextSizeManager.TextSize.LARGE -> 22.sp
                                            },
                                            color = if (currentTextSize == size) Color.White else Color.Black
                                        )
                                    }
                                    
                                    if (size != TextSizeManager.TextSize.LARGE) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = when (currentTextSize) {
                                    TextSizeManager.TextSize.SMALL -> "Pequeno"
                                    TextSizeManager.TextSize.MEDIUM -> "Médio"
                                    TextSizeManager.TextSize.LARGE -> "Grande"
                                },
                                fontSize = TextSizeManager.getSmallTextSize(),
                                color = Color.Gray,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
                
                // Seção Sobre o Aplicativo
                Text(
                    text = "Sobre o Aplicativo",
                    fontSize = TextSizeManager.getSubtitleTextSize(),
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ListItem(
                            headlineContent = { 
                                Text(
                                    "GlucoSmart",
                                    fontSize = TextSizeManager.getBodyTextSize(),
                                    fontWeight = FontWeight.Medium
                                ) 
                            },
                            supportingContent = { 
                                Text(
                                    "Versão 1.0.0",
                                    fontSize = TextSizeManager.getSmallTextSize()
                                ) 
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Informação",
                                    tint = primaryColor
                                )
                            }
                        )
                        
                        Divider()
                        
                        ListItem(
                            headlineContent = { 
                                Text(
                                    "Desenvolvido por",
                                    fontSize = TextSizeManager.getBodyTextSize(),
                                    fontWeight = FontWeight.Medium
                                ) 
                            },
                            supportingContent = { 
                                Column {
                                    Text(
                                        "Daniel Raposo",
                                        fontSize = TextSizeManager.getSmallTextSize()
                                    )
                                    Text(
                                        "Francisco Sequeira",
                                        fontSize = TextSizeManager.getSmallTextSize()
                                    )
                                    Text(
                                        "Paulo Neves",
                                        fontSize = TextSizeManager.getSmallTextSize()
                                    )
                                }
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Desenvolvedores",
                                    tint = primaryColor
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
