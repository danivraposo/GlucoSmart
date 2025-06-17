@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.francisco.glucosmart.ui.theme.screens.TablesPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
fun TablesPage(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var expandedCategory by remember { mutableStateOf<FoodCategory?>(null) }
    
    // Agrupar alimentos por categoria
    val foodCategories = remember { FoodDatabase.foodCategories }
    
    // Filtrar alimentos com base na pesquisa
    val filteredFoods = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            emptyList()
        } else {
            FoodDatabase.searchFoodsByName(searchQuery)
        }
    }

    MenuDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Text("Tabelas", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.Black)
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
                                contentDescription = "Perfil",
                                tint = ColorBlindModeManager.getPrimaryColor(),
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
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Tabela de Hidratos de Carbono",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Campo de pesquisa
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Pesquisar alimentos") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Pesquisar") },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = ColorBlindModeManager.getPrimaryColor(),
                        unfocusedBorderColor = Color(0xFFCCCCCC)
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "HC = Hidratos de Carbono por 100g/ml",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                LazyColumn {
                    // Mostrar resultados da pesquisa
                    if (searchQuery.isNotBlank()) {
                        if (filteredFoods.isEmpty()) {
                            item {
                                Text(
                                    "Nenhum alimento encontrado para \"$searchQuery\"",
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        } else {
                            item {
                                Text(
                                    "Resultados da pesquisa:",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(filteredFoods) { food ->
                                FoodItemRow(food)
                            }
                        }
                    } else {
                        // Mostrar categorias
                        items(foodCategories) { category ->
                            CategoryCard(
                                category = category,
                                isExpanded = expandedCategory == category.category,
                                onToggleExpand = {
                                    expandedCategory = if (expandedCategory == category.category) null else category.category
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: FoodCategoryGroup,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onToggleExpand),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8E8E8))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = category.displayName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = if (isExpanded) "Recolher" else "Expandir",
                    tint = ColorBlindModeManager.getPrimaryColor()
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                category.items.forEach { food ->
                    FoodItemRow(food)
                }
            }
        }
    }
}

@Composable
fun FoodItemRow(food: FoodItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = food.name,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${food.carbs}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00A99D)
        )
    }
}
