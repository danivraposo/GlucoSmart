package com.francisco.glucosmart.ui.theme.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.francisco.glucosmart.R
import com.francisco.glucosmart.data.ColorBlindModeManager
import com.francisco.glucosmart.data.TextSizeManager
import com.francisco.glucosmart.ui.theme.navigation.Routes

data class DrawerItem(val label: String, val icon: ImageVector, val route: String)

val drawerItems = listOf(
    DrawerItem("Início", Icons.Default.Home, Routes.HOME),
    DrawerItem("Pesquisa", Icons.Default.Search, Routes.SEARCH),
    DrawerItem("Tabelas", Icons.Default.TableChart, Routes.TABLES),
    DrawerItem("Histórico", Icons.Default.History, Routes.HISTORY),
    DrawerItem("Refeições Diárias", Icons.Default.Restaurant, Routes.MEALS),
    DrawerItem("Adicionar Alimentos", Icons.Default.Add, Routes.ADD_FOOD),
    DrawerItem("Calculadora", Icons.Default.Calculate, Routes.CALCULATOR)
)

@Composable
fun MenuDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    var selectedRoute by remember { mutableStateOf(navController.currentBackStackEntry?.destination?.route) }
    
    // Observar o estado do modo daltônico
    val isColorBlindModeEnabled by ColorBlindModeManager.isColorBlindModeEnabled
    
    // Observar o tamanho do texto atual
    val currentTextSize by TextSizeManager.currentTextSize
    
    // Determinar as cores com base no modo daltônico
    val primaryColor = ColorBlindModeManager.getPrimaryColor()
    val textColor = ColorBlindModeManager.getTextColor()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 24.dp, bottom = 24.dp)
            ) {
                // Logotipo GlucoSmart
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.simplification1),
                        contentDescription = "Logo GlucoSmart",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "GlucoSmart",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = primaryColor
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Itens de navegação
                drawerItems.forEachIndexed { index, item ->
                    if (index == 3) {
                        Divider(modifier = Modifier.padding(vertical = 10.dp))
                    }

                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = item.label,
                                fontSize = TextSizeManager.getBodyTextSize(),
                                fontWeight = FontWeight.Medium,
                                color = if (selectedRoute == item.route) primaryColor else textColor
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = if (selectedRoute == item.route) primaryColor else textColor,
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        selected = selectedRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                            selectedRoute = item.route
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
                
                // Seção de configurações
                Divider(modifier = Modifier.padding(vertical = 10.dp))
                
                // Item de definições
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Definições",
                            fontSize = TextSizeManager.getBodyTextSize(),
                            fontWeight = FontWeight.Medium,
                            color = if (selectedRoute == Routes.SETTINGS) primaryColor else textColor
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Definições",
                            tint = if (selectedRoute == Routes.SETTINGS) primaryColor else textColor,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    selected = selectedRoute == Routes.SETTINGS,
                    onClick = {
                        navController.navigate(Routes.SETTINGS) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        selectedRoute = Routes.SETTINGS
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        },
        content = content
    )
}
