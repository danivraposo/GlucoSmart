@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.francisco.glucosmart.ui.theme.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.francisco.glucosmart.R
import com.francisco.glucosmart.data.ColorBlindModeManager
import com.francisco.glucosmart.data.FoodCategory
import com.francisco.glucosmart.data.FoodCategoryGroup
import com.francisco.glucosmart.data.FoodDatabase
import com.francisco.glucosmart.data.FoodItem
import com.francisco.glucosmart.ui.theme.navigation.Routes
import com.francisco.glucosmart.ui.theme.screens.components.MenuDrawer
import kotlinx.coroutines.launch

@Composable
fun SearchPage(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    
    // Lista de resultados da pesquisa
    val searchResults = remember(searchText.text) {
        if (searchText.text.isNotBlank()) {
            FoodDatabase.searchFoodsByName(searchText.text)
        } else {
            emptyList()
        }
    }

    MenuDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Text(
                            text = "Pesquisa de Alimentos",
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        // Botão de perfil
                        IconButton(onClick = {
                            navController.navigate(Routes.LOGIN) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_user),
                                contentDescription = "Perfil do usuário",
                                tint = ColorBlindModeManager.getPrimaryColor(),
                                modifier = Modifier.size(28.dp)
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
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // Campo de busca estilizado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Pesquisar",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        BasicTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            singleLine = true,
                            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                            cursorBrush = SolidColor(Color.Black),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (searchText.text.isEmpty()) {
                                    Text("Digite um alimento...", color = Color.Gray)
                                }
                                innerTextField()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                // Resultados da pesquisa
                if (searchText.text.isNotBlank()) {
                    if (searchResults.isNotEmpty()) {
                        Text(
                            "Resultados da pesquisa",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = ColorBlindModeManager.getPrimaryColor(),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(searchResults) { food ->
                                FoodItemRow(food)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Nenhum alimento encontrado",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 32.dp)
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Use a pesquisa acima para localizar o alimento",
                            fontSize = 14.sp,
                            color = ColorBlindModeManager.getPrimaryColor(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FoodItemRow(food: FoodItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { /* Ação ao clicar no alimento */ },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = food.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = getCategoryDisplayName(food.category),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            Text(
                text = "${food.carbs} HC",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00A99D)
            )
        }
    }
}

// Função auxiliar para obter o nome de exibição da categoria
private fun getCategoryDisplayName(category: com.francisco.glucosmart.data.FoodCategory): String {
    return when (category) {
        com.francisco.glucosmart.data.FoodCategory.PRATOS_MISTOS -> "Pratos Mistos"
        com.francisco.glucosmart.data.FoodCategory.PASTELARIA -> "Pastelaria"
        com.francisco.glucosmart.data.FoodCategory.SALGADOS_CHARCUTARIA -> "Salgados e Charcutaria"
        com.francisco.glucosmart.data.FoodCategory.SOBREMESAS -> "Sobremesas"
        com.francisco.glucosmart.data.FoodCategory.PAO_PRODUTOS_AFINS -> "Pão e Produtos Afins"
        com.francisco.glucosmart.data.FoodCategory.CEREAIS_BISCOITOS -> "Cereais e Biscoitos"
        com.francisco.glucosmart.data.FoodCategory.LACTICINIOS_BEBIDAS -> "Lacticínios e Bebidas"
        com.francisco.glucosmart.data.FoodCategory.SOPAS -> "Sopas"
        com.francisco.glucosmart.data.FoodCategory.ARROZ -> "Arroz"
        com.francisco.glucosmart.data.FoodCategory.MASSA -> "Massa"
        com.francisco.glucosmart.data.FoodCategory.BATATA -> "Batata"
        com.francisco.glucosmart.data.FoodCategory.LEGUMINOSAS -> "Leguminosas"
        com.francisco.glucosmart.data.FoodCategory.FARINHAS -> "Farinhas"
        com.francisco.glucosmart.data.FoodCategory.FRUTA_FRESCA -> "Fruta Fresca"
        com.francisco.glucosmart.data.FoodCategory.FRUTA_CALDA -> "Fruta em Calda"
        com.francisco.glucosmart.data.FoodCategory.FRUTA_DESIDRATADA -> "Fruta Desidratada"
        com.francisco.glucosmart.data.FoodCategory.FRUTOS_AMILACEOS -> "Frutos Amiláceos e Oleaginosos"
        com.francisco.glucosmart.data.FoodCategory.FRUTOS_SECOS -> "Frutos Secos"
        com.francisco.glucosmart.data.FoodCategory.SEMENTES -> "Sementes"
        com.francisco.glucosmart.data.FoodCategory.SUSHI -> "Sushi"
        com.francisco.glucosmart.data.FoodCategory.OTHER -> "Outros"
    }
}
