package com.francisco.glucosmart.data

import java.time.LocalDateTime

data class Meal(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val items: List<MealItem> = emptyList(),
    val totalCarbs: Double = items.sumOf { it.carbs }
)

data class MealItem(
    val foodItem: FoodItem,
    val quantity: Double, // em gramas ou ml
    val carbs: Double = foodItem.carbs * quantity / 100 // HC por 100g/ml * quantidade / 100
)

enum class MealType(val displayName: String) {
    BREAKFAST("Pequeno Almoço"),
    MORNING_SNACK("Lanche da Manhã"),
    LUNCH("Almoço"),
    AFTERNOON_SNACK("Lanche da Tarde"),
    DINNER("Jantar"),
    SUPPER("Ceia"),
    OTHER("Outra")
}

// Classe para gerenciar as refeições
object MealDatabase {
    private val _meals = mutableListOf<Meal>()
    val meals: List<Meal> get() = _meals.toList()
    
    // Exemplo de refeições pré-definidas
    init {
        // Adicionar algumas refeições de exemplo
        val breakfastItems = listOf(
            MealItem(
                FoodDatabase.allFoods.find { it.name == "Pão de trigo branco" }!!,
                50.0
            ),
            MealItem(
                FoodDatabase.allFoods.find { it.name == "Leite de vaca" }!!,
                200.0
            )
        )
        
        val lunchItems = listOf(
            MealItem(
                FoodDatabase.allFoods.find { it.name == "Arroz \"solto\" cozido" }!!,
                150.0
            ),
            MealItem(
                FoodDatabase.allFoods.find { it.name == "Frango" }
                    ?: FoodItem("Frango", 0.0, FoodCategory.PRATOS_MISTOS),
                100.0
            ),
            MealItem(
                FoodDatabase.allFoods.find { it.name == "Salada de fruta sem calda" }!!,
                100.0
            )
        )
        
        val snackItems = listOf(
            MealItem(
                FoodDatabase.allFoods.find { it.name == "Iogurte natural ou 0% açúcar" }!!,
                125.0
            ),
            MealItem(
                FoodDatabase.allFoods.find { it.name == "Banana" }!!,
                100.0
            )
        )
        
        val dinnerItems = listOf(
            MealItem(
                FoodDatabase.allFoods.find { it.name == "Sopa de legumes (média)" }!!,
                250.0
            ),
            MealItem(
                FoodDatabase.allFoods.find { it.name == "Pão de centeio" }!!,
                30.0
            ),
            MealItem(
                FoodDatabase.allFoods.find { it.name == "Queijo" }
                    ?: FoodItem("Queijo", 0.0, FoodCategory.LACTICINIOS_BEBIDAS),
                20.0
            )
        )
        
        _meals.add(Meal(
            name = MealType.BREAKFAST.displayName,
            items = breakfastItems
        ))
        
        _meals.add(Meal(
            name = MealType.LUNCH.displayName,
            items = lunchItems
        ))
        
        _meals.add(Meal(
            name = MealType.AFTERNOON_SNACK.displayName,
            items = snackItems
        ))
        
        _meals.add(Meal(
            name = MealType.DINNER.displayName,
            items = dinnerItems
        ))
    }
    
    fun addMeal(meal: Meal) {
        _meals.add(meal)
    }
    
    fun updateMeal(meal: Meal) {
        val index = _meals.indexOfFirst { it.id == meal.id }
        if (index >= 0) {
            _meals[index] = meal
        }
    }
    
    fun deleteMeal(mealId: String) {
        _meals.removeIf { it.id == mealId }
    }
    
    fun getMealsByDate(date: LocalDateTime): List<Meal> {
        return _meals.filter { 
            it.dateTime.year == date.year && 
            it.dateTime.month == date.month && 
            it.dateTime.dayOfMonth == date.dayOfMonth 
        }
    }
} 