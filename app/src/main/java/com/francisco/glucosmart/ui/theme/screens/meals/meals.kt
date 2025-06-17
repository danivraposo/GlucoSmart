@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.francisco.glucosmart.ui.theme.screens.meals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Menu
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
import com.francisco.glucosmart.data.Meal
import com.francisco.glucosmart.data.MealDatabase
import com.francisco.glucosmart.data.MealType
import com.francisco.glucosmart.ui.theme.navigation.Routes
import com.francisco.glucosmart.ui.theme.screens.components.ColorBlindModeIconButton
import com.francisco.glucosmart.ui.theme.screens.components.MenuDrawer
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun MealsPage(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var expandedMealId by remember { mutableStateOf<String?>(null) }
    
    // Obter refeições do banco de dados e recompor quando mudar
    val meals by remember { mutableStateOf(MealDatabase.meals) }
    
    // Efeito para atualizar a lista quando a tela se tornar ativa
    DisposableEffect(Unit) {
        onDispose {
            // Este código será executado quando a composição for descartada
            // Não precisamos fazer nada aqui, mas isso força a recomposição
        }
    }

    MenuDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Text(
                            text = "Refeições Diárias",
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
            },
            floatingActionButton = {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Botão para adicionar alimentos a uma refeição
                    ExtendedFloatingActionButton(
                        text = { Text("Adicionar alimentos", fontSize = 16.sp) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Adicionar alimentos",
                                tint = Color.White
                            )
                        },
                        onClick = {
                            navController.navigate(Routes.RECEITA_ATUAL)
                        },
                        containerColor = Color(0xFF58C271),
                        contentColor = Color.White
                    )
                    
                    // Botão para adicionar refeição
                    ExtendedFloatingActionButton(
                        text = { Text("Adicionar refeição", fontSize = 16.sp) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Adicionar",
                                tint = Color.White
                            )
                        },
                        onClick = {
                            navController.navigate(Routes.RECEITA_ATUAL)
                        },
                        containerColor = ColorBlindModeManager.getPrimaryColor(),
                        contentColor = Color.White
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Minhas Refeições Diárias",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "HC = Hidratos de Carbono",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn {
                    // Agrupar refeições por tipo
                    val mealsByType = MealType.values().associateWith { mealType ->
                        meals.filter { it.name == mealType.displayName }
                    }
                    
                    mealsByType.forEach { (mealType, mealsOfType) ->
                        // Só mostrar tipos de refeição que têm pelo menos uma refeição
                        if (mealsOfType.isNotEmpty()) {
                            item {
                                MealTypeCard(
                                    mealType = mealType,
                                    meals = mealsOfType,
                                    expandedMealId = expandedMealId,
                                    onToggleExpand = { mealId ->
                                        expandedMealId = if (expandedMealId == mealId) null else mealId
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MealTypeCard(
    mealType: MealType,
    meals: List<Meal>,
    expandedMealId: String?,
    onToggleExpand: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8E8E8))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = mealType.displayName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            meals.forEach { meal ->
                MealCard(
                    meal = meal,
                    isExpanded = expandedMealId == meal.id,
                    onToggleExpand = { onToggleExpand(meal.id) }
                )
            }
        }
    }
}

@Composable
fun MealCard(
    meal: Meal,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onToggleExpand),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = meal.dateTime.format(dateFormatter),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Total HC: ${String.format("%.1f", meal.totalCarbs)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF00A99D)
                    )
                }
                
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = if (isExpanded) "Recolher" else "Expandir",
                    tint = Color(0xFF00A99D)
                )
            }
            
            if (isExpanded && meal.items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                
                Column {
                    meal.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.foodItem.name} (${item.quantity}g)",
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = String.format("%.1f", item.carbs),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
