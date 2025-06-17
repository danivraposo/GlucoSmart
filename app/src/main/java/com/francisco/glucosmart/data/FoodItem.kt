package com.francisco.glucosmart.data

data class FoodItem(
    val name: String,
    val carbs: Double,
    val category: FoodCategory
)

enum class FoodCategory {
    PRATOS_MISTOS,
    PASTELARIA,
    SALGADOS_CHARCUTARIA,
    SOBREMESAS,
    PAO_PRODUTOS_AFINS,
    CEREAIS_BISCOITOS,
    LACTICINIOS_BEBIDAS,
    SOPAS,
    ARROZ,
    MASSA,
    BATATA,
    LEGUMINOSAS,
    FARINHAS,
    FRUTA_FRESCA,
    FRUTA_CALDA,
    FRUTA_DESIDRATADA,
    FRUTOS_AMILACEOS,
    FRUTOS_SECOS,
    SEMENTES,
    SUSHI,
    OTHER
}

// Classe para agrupar alimentos por categoria
data class FoodCategoryGroup(
    val category: FoodCategory,
    val displayName: String,
    val items: List<FoodItem>
) 