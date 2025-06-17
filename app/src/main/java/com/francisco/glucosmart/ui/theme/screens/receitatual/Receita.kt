@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.francisco.glucosmart.ui.theme.screens.receitatual

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.francisco.glucosmart.data.*
import com.francisco.glucosmart.ui.theme.navigation.Routes
import com.francisco.glucosmart.ui.theme.screens.components.ColorBlindModeIconButton
import com.francisco.glucosmart.ui.theme.screens.components.MenuDrawer
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class Ingrediente(val nome: String, val quantidade: String, val hc: String)

@Composable
fun ReceitaAtualPage(navController: NavHostController, mealId: String? = null) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val mealTypes = MealType.values()
    
    // Carregar dados da refeição se mealId for fornecido
    val refeicaoParaReutilizar = remember(mealId) {
        if (mealId != null) {
            MealDatabase.meals.find { it.id == mealId }
        } else null
    }
    
    // Inicializar estados com dados da refeição se disponível
    var selectedMealType by remember { 
        mutableStateOf(
            if (refeicaoParaReutilizar != null) {
                MealType.values().find { it.displayName == refeicaoParaReutilizar.name } ?: MealType.OTHER
            } else MealType.OTHER
        ) 
    }
    
    var nomeReceita by remember { 
        mutableStateOf(
            if (refeicaoParaReutilizar != null) refeicaoParaReutilizar.name else ""
        ) 
    }
    
    var ingredientes by remember { 
        mutableStateOf<List<Ingrediente>>(
            if (refeicaoParaReutilizar != null) {
                refeicaoParaReutilizar.items.map { item ->
                    Ingrediente(
                        nome = item.foodItem.name,
                        quantidade = "${item.quantity}g",
                        hc = "${String.format("%.1f", item.carbs)} hc"
                    )
                }
            } else emptyList()
        ) 
    }
    
    // Estado para o diálogo de seleção de alimentos
    var showFoodSelector by remember { mutableStateOf(false) }
    
    // Mensagens para feedback ao usuário
    var showSuccessMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // Título da tela baseado na operação (adicionar ou reutilizar)
    val tituloPagina = if (mealId != null) "Reutilizar Refeição" else "Adicionar Refeição"

    // Diálogo de seleção de alimentos
    if (showFoodSelector) {
        FoodSelectorDialog(
            onDismiss = { showFoodSelector = false },
            onFoodSelected = { food, quantity ->
                val carbsValue = food.carbs * quantity / 100
                val novoIngrediente = Ingrediente(
                    nome = food.name,
                    quantidade = "${quantity}g",
                    hc = "${String.format("%.1f", carbsValue)} hc"
                )
                ingredientes = ingredientes + novoIngrediente
            }
        )
    }

    MenuDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text(tituloPagina) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Close, contentDescription = "Fechar")
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFFE0E0E0))
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Selecione o tipo de refeição", fontSize = 16.sp, fontWeight = FontWeight.Medium)

                // Dropdown para selecionar o tipo de refeição
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(selectedMealType.displayName)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        mealTypes.forEach { mealType ->
                            DropdownMenuItem(
                                text = { Text(mealType.displayName) },
                                onClick = {
                                    selectedMealType = mealType
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                Text("Escreve o nome da Receita", fontSize = 16.sp, fontWeight = FontWeight.Medium)

                TextField(
                    value = nomeReceita,
                    onValueChange = { nomeReceita = it },
                    placeholder = { Text("Escreva o nome da Receita...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
                )

                Text("Ingredientes:", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                // Botão para adicionar ingredientes
                Button(
                    onClick = { showFoodSelector = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF58C271))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar Alimento")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Adicionar Alimento")
                }

                // Lista de ingredientes
                if (ingredientes.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Text(
                            "Nenhum alimento adicionado",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ingredientes.forEachIndexed { index, ing ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFEDEDED))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("${ing.quantidade} - ${ing.nome}", fontSize = 14.sp, color = ColorBlindModeManager.getPrimaryColor())
                                        Text(
                                            ing.hc,
                                            fontSize = 13.sp,
                                            color = Color.White,
                                            modifier = Modifier
                                                .padding(top = 4.dp)
                                                .background(Color(0xFF58C271), shape = RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    IconButton(onClick = {
                                        ingredientes = ingredientes.filterIndexed { i, _ -> i != index }
                                    }) {
                                        Icon(Icons.Default.Close, contentDescription = "Remover", tint = Color(0xFFD9534F))
                                    }
                                }
                            }
                        }
                        
                        // Total de HC
                        val totalCarbs = ingredientes.sumOf { 
                            val carbsStr = it.hc.replace(Regex("[^0-9.]"), "")
                            carbsStr.toDoubleOrNull() ?: 0.0
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = ColorBlindModeManager.getPrimaryColor().copy(alpha = 0.2f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total de Hidratos de Carbono:", fontWeight = FontWeight.Medium)
                                Text(
                                    "${String.format("%.1f", totalCarbs)} HC",
                                    fontWeight = FontWeight.Bold,
                                    color = ColorBlindModeManager.getPrimaryColor()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Mostrar mensagem de sucesso ou erro
                if (showSuccessMessage) {
                    Text(
                        if (mealId != null) "Refeição reutilizada com sucesso!" else "Refeição adicionada com sucesso!",
                        color = Color(0xFF58C271),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                
                errorMessage?.let {
                    Text(
                        it,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { navController.navigateUp() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9534F))
                    ) {
                        Text("✘ Cancelar")
                    }
                    Button(
                        onClick = {
                            try {
                                // Verifica se há pelo menos um ingrediente
                                if (ingredientes.isEmpty()) {
                                    errorMessage = "Adicione pelo menos um alimento à refeição"
                                    return@Button
                                }
                                
                                // Converte ingredientes para MealItems
                                val mealItems = convertIngredientesToMealItems(ingredientes)
                                
                                // Verifica se o nome da receita foi preenchido
                                if (nomeReceita.isBlank()) {
                                    nomeReceita = selectedMealType.displayName
                                }
                                
                                // Cria e adiciona a refeição
                                val meal = Meal(
                                    name = nomeReceita,
                                    dateTime = LocalDateTime.now(),
                                    items = mealItems
                                )
                                
                                // Adiciona ao banco de dados
                                MealDatabase.addMeal(meal)
                                
                                // Feedback e navegação
                                showSuccessMessage = true
                                errorMessage = null
                                
                                // Navega de volta após um curto delay
                                scope.launch {
                                    kotlinx.coroutines.delay(1500)
                                    navController.navigate(Routes.MEALS) {
                                        // Limpar a pilha de navegação até MEALS
                                        popUpTo(Routes.MEALS) { inclusive = true }
                                        // Garantir que a tela será recriada
                                        launchSingleTop = true
                                    }
                                }
                            } catch (e: Exception) {
                                // Em caso de erro, mostra a mensagem
                                errorMessage = "Erro ao salvar: ${e.message}"
                                showSuccessMessage = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorBlindModeManager.getPrimaryColor())
                    ) {
                        Text(if (mealId != null) "✓ Confirmar" else "➕ Adicionar")
                    }
                }
            }
        }
    }
}

@Composable
fun FoodSelectorDialog(
    onDismiss: () -> Unit,
    onFoodSelected: (FoodItem, Double) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<FoodCategory?>(null) }
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var quantity by remember { mutableStateOf("") }
    
    val foodCategories = FoodDatabase.foodCategories
    
    val filteredFoods = remember(searchQuery, selectedCategory) {
        if (searchQuery.isNotBlank()) {
            FoodDatabase.allFoods.filter { it.name.contains(searchQuery, ignoreCase = true) }
        } else if (selectedCategory != null) {
            FoodDatabase.allFoods.filter { it.category == selectedCategory }
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
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    "Selecionar Alimento",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Campo de pesquisa
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { 
                        searchQuery = it
                        selectedCategory = null 
                    },
                    placeholder = { Text("Pesquisar alimento...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Pesquisar") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Se um alimento for selecionado, mostrar o campo de quantidade
                if (selectedFood != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            "Alimento selecionado: ${selectedFood!!.name}",
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Hidratos de Carbono: ${selectedFood!!.carbs}g por 100g",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Quantidade (g)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                selectedFood = null
                                quantity = ""
                            }) {
                                Text("Cancelar")
                            }
                            
                            Button(
                                onClick = {
                                    val qtyValue = quantity.toDoubleOrNull() ?: 0.0
                                    if (qtyValue > 0) {
                                        onFoodSelected(selectedFood!!, qtyValue)
                                        onDismiss()
                                    }
                                },
                                enabled = quantity.isNotBlank() && quantity.toDoubleOrNull() ?: 0.0 > 0
                            ) {
                                Text("Adicionar")
                            }
                        }
                    }
                } else {
                    // Lista de categorias ou alimentos
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
                                        .clickable { selectedFood = food }
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
                        TextButton(onClick = onDismiss) {
                            Text("Cancelar")
                        }
                    }
                }
            }
        }
    }
}

// Função para converter os ingredientes da interface para MealItems
private fun convertIngredientesToMealItems(ingredientes: List<Ingrediente>): List<MealItem> {
    return ingredientes.map { ingrediente ->
        // Extrair a quantidade numérica do formato "100g"
        val quantityStr = ingrediente.quantidade.replace(Regex("[^0-9.]"), "")
        val quantity = quantityStr.toDoubleOrNull() ?: 0.0
        
        // Tenta encontrar o alimento no banco de dados
        val foodItem = FoodDatabase.allFoods.find { it.name == ingrediente.nome }
            ?: FoodItem(ingrediente.nome, 0.0, FoodCategory.OTHER)
        
        // Extrair o valor de HC do formato "1 hc"
        val carbsStr = ingrediente.hc.replace(Regex("[^0-9.]"), "")
        val carbs = carbsStr.toDoubleOrNull() ?: 0.0
        
        MealItem(foodItem, quantity, carbs)
    }
}
