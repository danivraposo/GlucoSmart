@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.francisco.glucosmart.ui.theme.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.francisco.glucosmart.R
import com.francisco.glucosmart.data.ColorBlindModeManager
import com.francisco.glucosmart.ui.theme.navigation.Routes
import com.francisco.glucosmart.ui.theme.screens.components.MenuDrawer
import kotlinx.coroutines.launch

@Composable
fun HomePage(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Observar o estado do modo daltônico
    val isColorBlindModeEnabled by ColorBlindModeManager.isColorBlindModeEnabled
    
    // Determinar a cor primária com base no modo daltônico
    val primaryColor = ColorBlindModeManager.getPrimaryColor()

    MenuDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menu")
                        }
                    },
                    actions = {
                        // Botão de perfil
                        IconButton(onClick = {
                            navController.navigate(Routes.LOGIN) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_user),
                                contentDescription = "Perfil do usuário",
                                tint = primaryColor,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logotipo e texto
                Image(
                    painter = painterResource(id = R.drawable.simplification1),
                    contentDescription = "Logo GlucoSmart",
                    modifier = Modifier
                        .height(180.dp)
                        .padding(top = 16.dp)
                )


                Text(
                    text = "Escolha uma opção:",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconWithLabel(R.drawable.ic_search, "Pesquisa") {
                            navController.navigate(Routes.SEARCH)
                        }
                        IconWithLabel(R.drawable.ic_tables, "Tabelas") {
                            navController.navigate(Routes.TABLES)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconWithLabel(R.drawable.ic_history, "Histórico") {
                            navController.navigate(Routes.HISTORY)
                        }
                        IconWithLabel(R.drawable.ic_meals, "Refeições\nDiárias") {
                            navController.navigate(Routes.MEALS)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconWithLabel(R.drawable.ic_addfood, "Adicionar\nAlimentos") {
                            navController.navigate(Routes.ADD_FOOD)
                        }
                        IconWithLabel(R.drawable.ic_calculator, "Calculadora") {
                            navController.navigate(Routes.CALCULATOR)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IconWithLabel(iconResId: Int, label: String, onClick: () -> Unit) {
    // Obter a cor primária com base no modo daltônico
    val primaryColor = ColorBlindModeManager.getPrimaryColor()
    val textColor = ColorBlindModeManager.getTextColor()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .width(110.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = label,
            modifier = Modifier.size(52.dp),
            tint = primaryColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}
