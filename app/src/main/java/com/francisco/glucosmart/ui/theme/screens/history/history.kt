@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.francisco.glucosmart.ui.theme.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
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
import com.francisco.glucosmart.data.MealDatabase
import com.francisco.glucosmart.ui.theme.navigation.Routes
import com.francisco.glucosmart.ui.theme.screens.components.MenuDrawer
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryPage(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Obter refeições do banco de dados
    val allMeals by remember { mutableStateOf(MealDatabase.meals) }
    
    // Efeito para atualizar a lista quando a tela se tornar ativa
    DisposableEffect(Unit) {
        onDispose { }
    }

    var expandedItem by remember { mutableStateOf<String?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Date()) }

    val dateFormatter = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "PT"))
    val formattedDate = dateFormatter.format(selectedDate)
    
    // Converter selectedDate para LocalDateTime para comparação
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH é baseado em zero
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    
    // Filtrar refeições pela data selecionada
    val refeicoesSelecionadas = allMeals.filter { meal ->
        meal.dateTime.year == year &&
        meal.dateTime.monthValue == month &&
        meal.dateTime.dayOfMonth == day
    }

    MenuDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Text("Histórico", fontSize = 18.sp, color = Color.Black)
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        // Botão de calendário
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Selecionar Data",
                                tint = ColorBlindModeManager.getPrimaryColor(),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White)
                )
            }
        ) { innerPadding ->

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = selectedDate.time,
                        yearRange = IntRange(2020, 2030),
                        initialDisplayMode = DisplayMode.Picker
                    )
                    DatePicker(
                        state = datePickerState,
                        title = { Text("Selecione a data", fontSize = 16.sp, fontWeight = FontWeight.Medium) },
                        headline = { Text("Selecione a data para ver o histórico", fontSize = 14.sp) }
                    )
                    LaunchedEffect(datePickerState.selectedDateMillis) {
                        datePickerState.selectedDateMillis?.let {
                            selectedDate = Date(it)
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5F4))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { showDatePicker = true },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Data selecionada: $formattedDate",
                            fontSize = 16.sp,
                            color = ColorBlindModeManager.getPrimaryColor(),
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Alterar data",
                            tint = ColorBlindModeManager.getPrimaryColor()
                        )
                    }
                }

                if (refeicoesSelecionadas.isEmpty()) {
                    Text("Sem registos para esta data.", fontSize = 14.sp)
                } else {
                    refeicoesSelecionadas.forEach { meal ->
                        val isExpanded = expandedItem == meal.id

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    expandedItem = if (isExpanded) null else meal.id
                                },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8E8E8))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = meal.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                
                                Text(
                                    text = "Total HC: ${String.format("%.1f", meal.totalCarbs)}g",
                                    fontSize = 14.sp,
                                    color = ColorBlindModeManager.getPrimaryColor(),
                                    fontWeight = FontWeight.Medium
                                )

                                if (isExpanded && meal.items.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Alimentos:",
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp
                                    )
                                    meal.items.forEach { item ->
                                        Text(
                                            text = "• ${item.foodItem.name} (${String.format("%.1f", item.quantity)}g) - ${String.format("%.1f", item.carbs)}g HC",
                                            fontSize = 14.sp
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    // Botão Reutilizar
                                    Button(
                                        onClick = {
                                            // Navegar para a tela de adicionar refeição com os dados preenchidos
                                            navController.navigate("${Routes.RECEITA_ATUAL}/${meal.id}")
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = ColorBlindModeManager.getPrimaryColor()
                                        ),
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = "Reutilizar",
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Reutilizar", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

