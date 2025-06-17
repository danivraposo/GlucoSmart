@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.francisco.glucosmart.ui.theme.screens.addfood

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import com.francisco.glucosmart.R
import com.francisco.glucosmart.data.ColorBlindModeManager
import com.francisco.glucosmart.data.FoodCategory
import com.francisco.glucosmart.data.FoodDatabase
import com.francisco.glucosmart.ui.theme.navigation.Routes
import com.francisco.glucosmart.ui.theme.screens.components.MenuDrawer
import kotlinx.coroutines.launch

@Composable
fun AddFoodPage(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var foodName by remember { mutableStateOf("") }
    var portionSize by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<FoodCategory?>(null) }
    
    // Estado para mostrar mensagens de sucesso/erro
    var showSuccessMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    MenuDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Text(
                            text = "Adicionar Alimento",
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
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
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Nome do alimento
                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = { Text("Nome do Alimento") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Porção e Hidratos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = portionSize,
                        onValueChange = { portionSize = it },
                        label = { Text("Porção (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it },
                        label = { Text("Hidratos (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                // Dropdown de categorias
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedCategory?.let { FoodDatabase.getCategoryDisplayName(it) } ?: "",
                        onValueChange = { },
                        label = { Text("Categoria") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showCategoryDropdown = true }) {
                                Icon(Icons.Default.ArrowDropDown, "Expandir")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    DropdownMenu(
                        expanded = showCategoryDropdown,
                        onDismissRequest = { showCategoryDropdown = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        FoodCategory.values().forEach { category ->
                            DropdownMenuItem(
                                text = { Text(FoodDatabase.getCategoryDisplayName(category)) },
                                onClick = {
                                    selectedCategory = category
                                    showCategoryDropdown = false
                                }
                            )
                        }
                    }
                }

                // Mensagem de sucesso
                if (showSuccessMessage) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5E1))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Sucesso",
                                tint = ColorBlindModeManager.getPrimaryColor()
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Alimento adicionado com sucesso!",
                                color = ColorBlindModeManager.getPrimaryColor()
                            )
                        }
                    }
                }
                
                // Mensagem de erro
                errorMessage?.let {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5E1E1))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_addfood),
                                contentDescription = "Erro",
                                tint = Color.Red
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                it,
                                color = Color.Red
                            )
                        }
                    }
                }

                // Botões
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            // Validar campos
                            when {
                                foodName.isBlank() -> {
                                    errorMessage = "Insira o nome do alimento"
                                    showSuccessMessage = false
                                }
                                portionSize.isBlank() -> {
                                    errorMessage = "Insira o tamanho da porção"
                                    showSuccessMessage = false
                                }
                                carbs.isBlank() -> {
                                    errorMessage = "Insira a quantidade de hidratos"
                                    showSuccessMessage = false
                                }
                                selectedCategory == null -> {
                                    errorMessage = "Selecione uma categoria"
                                    showSuccessMessage = false
                                }
                                else -> {
                                    try {
                                        // Calcular HC por 100g
                                        val portion = portionSize.toDoubleOrNull() ?: 0.0
                                        val carbsValue = carbs.toDoubleOrNull() ?: 0.0
                                        val carbsPer100g = if (portion > 0) (carbsValue * 100) / portion else 0.0
                                        
                                        // Adicionar alimento à base de dados
                                        FoodDatabase.addFood(
                                            name = foodName,
                                            carbs = carbsPer100g,
                                            category = selectedCategory!!
                                        )
                                        
                                        // Limpar campos e mostrar mensagem de sucesso
                                        foodName = ""
                                        portionSize = ""
                                        carbs = ""
                                        selectedCategory = null
                                        errorMessage = null
                                        showSuccessMessage = true
                                    } catch (e: Exception) {
                                        errorMessage = "Erro ao adicionar alimento: ${e.message}"
                                        showSuccessMessage = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A99D))
                    ) {
                        Text("Adicionar", fontSize = 16.sp, color = Color.White)
                    }

                    Button(
                        onClick = { 
                            // Limpar campos
                            foodName = ""
                            portionSize = ""
                            carbs = ""
                            selectedCategory = null
                            errorMessage = null
                            showSuccessMessage = false
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Text("Limpar", fontSize = 16.sp, color = Color.Black)
                    }
                }
                
                // Botão para ir para a tabela de alimentos
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate(Routes.SEARCH) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CA1D5))
                ) {
                    Text("Ver Tabela de Alimentos", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}
