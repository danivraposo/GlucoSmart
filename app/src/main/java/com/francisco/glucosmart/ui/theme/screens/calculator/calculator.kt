@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.francisco.glucosmart.ui.theme.screens.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.francisco.glucosmart.R
import com.francisco.glucosmart.data.ColorBlindModeManager
import com.francisco.glucosmart.data.FoodCategory
import com.francisco.glucosmart.data.FoodDatabase
import com.francisco.glucosmart.data.FoodItem
import com.francisco.glucosmart.ui.theme.navigation.Routes
import com.francisco.glucosmart.ui.theme.screens.components.MenuDrawer
import kotlinx.coroutines.launch
import kotlin.math.max

data class Ingredient(
    val name: String,
    val quantity: Double,
    val carbsPer100g: Double = 0.0
)

@Composable
fun CalculatorPage(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var ingredientText by remember { mutableStateOf("") }
    var pesoPorcao by remember { mutableStateOf("") }
    var showFoodSelector by remember { mutableStateOf(false) }
    var selectedQuantity by remember { mutableStateOf("") }
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    
    var ingredients by remember { mutableStateOf<List<Ingredient>>(emptyList()) }
    
    // Calcular os totais
    val totalCarbs = ingredients.sumOf { it.carbsPer100g * it.quantity / 100 }
    val totalWeight = ingredients.sumOf { it.quantity }
    
    // Calcular valores da porção
    val porcaoWeight = pesoPorcao.toDoubleOrNull() ?: 0.0
    val porcaoCarbs = if (totalWeight > 0 && porcaoWeight > 0) {
        totalCarbs * (porcaoWeight / totalWeight)
    } else 0.0

    // Diálogo de seleção de alimentos
    if (showFoodSelector) {
        FoodSelectorDialog(
            onDismiss = { showFoodSelector = false },
            onFoodSelected = { food ->
                selectedFood = food
                showFoodSelector = false
            }
        )
    }

    MenuDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Text(
                            text = "Calculadora",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {


                // Seção 1: Lista de ingredientes
                item {
                    CalculatorSection(number = "1", title = "Liste os ingredientes") {
                        // Seleção de alimento
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Substituir o OutlinedTextField por um Surface clicável que parece um campo de texto
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .clickable { showFoodSelector = true },
                                shape = MaterialTheme.shapes.small
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = selectedFood?.name ?: "Selecione um alimento",
                                        color = if (selectedFood == null) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                               else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Selecionar"
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Quantidade
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = selectedQuantity,
                                onValueChange = { selectedQuantity = it },
                                placeholder = { Text("Quantidade (g)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Button(
                                onClick = {
                                    val quantity = selectedQuantity.toDoubleOrNull() ?: 0.0
                                    if (selectedFood != null && quantity > 0) {
                                        ingredients = ingredients + Ingredient(
                                            name = selectedFood!!.name,
                                            quantity = quantity,
                                            carbsPer100g = selectedFood!!.carbs
                                        )
                                        selectedFood = null
                                        selectedQuantity = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ColorBlindModeManager.getPrimaryColor()),
                                enabled = selectedFood != null && selectedQuantity.isNotEmpty()
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Adicionar")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Adicionar", color = Color.White)
                            }
                        }
                        
                        if (ingredients.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                "Ingredientes adicionados:",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            ingredients.forEachIndexed { index, ingredient ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "${ingredient.name} (${ingredient.quantity}g)",
                                        fontSize = 16.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    
                                    IconButton(
                                        onClick = {
                                            ingredients = ingredients.toMutableList().apply {
                                                removeAt(index)
                                            }
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Remover",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }



                // Seção 2: Porção e cálculos
                item {
                    CalculatorSection(number = "2", title = "Pese a sua porção") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = pesoPorcao,
                                onValueChange = { pesoPorcao = it },
                                placeholder = { Text("Ex: 150") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = MaterialTheme.shapes.medium,
                                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("g", fontSize = 16.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8E8E8))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "Porção: ${pesoPorcao.ifEmpty { "0" }}g", 
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "= ${String.format("%.1f", porcaoCarbs)}hc", 
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorBlindModeManager.getPrimaryColor()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodSelectorDialog(
    onDismiss: () -> Unit,
    onFoodSelected: (FoodItem) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<FoodCategory?>(null) }
    
    val foodCategories = FoodDatabase.foodCategories
    
    val filteredFoods = remember(searchQuery, selectedCategory) {
        if (searchQuery.isNotBlank()) {
            FoodDatabase.searchFoodsByName(searchQuery)
        } else if (selectedCategory != null) {
            FoodDatabase.getFoodsByCategory(selectedCategory!!)
        } else {
            emptyList()
        }
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "Selecione um alimento",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Campo de pesquisa
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Pesquisar alimentos") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Categorias
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (searchQuery.isBlank() && selectedCategory == null) {
                        // Mostrar categorias
                        item {
                            Text(
                                "Categorias:",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        
                        items(foodCategories.size) { index ->
                            val category = foodCategories[index]
                            Text(
                                text = category.displayName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedCategory = category.category }
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                fontSize = 16.sp
                            )
                            if (index < foodCategories.size - 1) {
                                Divider()
                            }
                        }
                    } else {
                        // Mostrar alimentos filtrados
                        items(filteredFoods.size) { index ->
                            val food = filteredFoods[index]
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onFoodSelected(food) }
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = food.name,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "${food.carbs} HC",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = ColorBlindModeManager.getPrimaryColor()
                                )
                            }
                            if (index < filteredFoods.size - 1) {
                                Divider()
                            }
                        }
                    }
                }
                
                // Botões
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        if (selectedCategory != null) {
                            selectedCategory = null
                        } else {
                            searchQuery = ""
                        }
                    }) {
                        Text("Voltar")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorSection(number: String, title: String, content: @Composable () -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(ColorBlindModeManager.getPrimaryColor(), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    number,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ColorBlindModeManager.getPrimaryColor()
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}
