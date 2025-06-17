package com.francisco.glucosmart.data

object FoodDatabase {
    // Lista com todos os alimentos
    private val _allFoods: MutableList<FoodItem> = generateFoodItems().toMutableList()
    val allFoods: List<FoodItem> get() = _allFoods.toList()
    
    // Lista com alimentos adicionados pelo usuário
    private val _userFoods: MutableList<FoodItem> = mutableListOf()
    val userFoods: List<FoodItem> get() = _userFoods.toList()
    
    // Alimentos agrupados por categoria
    val foodCategories: List<FoodCategoryGroup> = allFoods
        .groupBy { it.category }
        .map { (category, items) ->
            FoodCategoryGroup(
                category = category,
                displayName = getCategoryDisplayName(category),
                items = items.sortedBy { it.name }
            )
        }
        .sortedBy { it.displayName }

    // Função para buscar alimentos por nome
    fun searchFoodsByName(query: String): List<FoodItem> {
        if (query.isBlank()) return emptyList()
        
        return allFoods.filter {
            it.name.contains(query, ignoreCase = true)
        }.sortedBy { it.name }
    }
    
    // Função para obter alimentos por categoria
    fun getFoodsByCategory(category: FoodCategory): List<FoodItem> {
        return allFoods.filter { it.category == category }
                      .sortedBy { it.name }
    }
    
    // Função para adicionar um novo alimento à base de dados
    fun addFood(name: String, carbs: Double, category: FoodCategory): FoodItem {
        val newFood = FoodItem(name, carbs, category)
        _allFoods.add(newFood)
        _userFoods.add(newFood)
        return newFood
    }
    
    // Função para obter o nome de exibição da categoria
    fun getCategoryDisplayName(category: FoodCategory): String {
        return when (category) {
            FoodCategory.PRATOS_MISTOS -> "Pratos Mistos"
            FoodCategory.PASTELARIA -> "Pastelaria"
            FoodCategory.SALGADOS_CHARCUTARIA -> "Salgados e Charcutaria"
            FoodCategory.SOBREMESAS -> "Sobremesas"
            FoodCategory.PAO_PRODUTOS_AFINS -> "Pão e Produtos Afins"
            FoodCategory.CEREAIS_BISCOITOS -> "Cereais e Biscoitos"
            FoodCategory.LACTICINIOS_BEBIDAS -> "Lacticínios e Bebidas"
            FoodCategory.SOPAS -> "Sopas"
            FoodCategory.ARROZ -> "Arroz"
            FoodCategory.MASSA -> "Massa"
            FoodCategory.BATATA -> "Batata"
            FoodCategory.LEGUMINOSAS -> "Leguminosas"
            FoodCategory.FARINHAS -> "Farinhas"
            FoodCategory.FRUTA_FRESCA -> "Fruta Fresca"
            FoodCategory.FRUTA_CALDA -> "Fruta em Calda"
            FoodCategory.FRUTA_DESIDRATADA -> "Fruta Desidratada"
            FoodCategory.FRUTOS_AMILACEOS -> "Frutos Amiláceos e Oleaginosos"
            FoodCategory.FRUTOS_SECOS -> "Frutos Secos"
            FoodCategory.SEMENTES -> "Sementes"
            FoodCategory.SUSHI -> "Sushi"
            FoodCategory.OTHER -> "Outros"
        }
    }

    // Função para gerar a lista de alimentos a partir das tabelas
    private fun generateFoodItems(): List<FoodItem> {
        val foods = mutableListOf<FoodItem>()
        
        // PRATOS MISTOS
        foods.addAll(listOf(
            FoodItem("Bacalhau à brás", 8.3, FoodCategory.PRATOS_MISTOS),
            FoodItem("Bacalhau com natas", 9.2, FoodCategory.PRATOS_MISTOS),
            FoodItem("Douradinhos", 17.5, FoodCategory.PRATOS_MISTOS),
            FoodItem("Empadão de atum", 10.7, FoodCategory.PRATOS_MISTOS),
            FoodItem("Feijoada c/ carne porco", 13.3, FoodCategory.PRATOS_MISTOS),
            FoodItem("Panados", 8.5, FoodCategory.PRATOS_MISTOS),
            FoodItem("Rancho", 6.5, FoodCategory.PRATOS_MISTOS),
            FoodItem("Lasanha bolonhesa", 9.2, FoodCategory.PRATOS_MISTOS),
            FoodItem("Pizza", 29.9, FoodCategory.PRATOS_MISTOS),
            FoodItem("Quiche lorraine c/ leite", 23.5, FoodCategory.PRATOS_MISTOS),
            FoodItem("Açorda de marisco", 5.4, FoodCategory.PRATOS_MISTOS),
            FoodItem("Arroz cabidela", 12.2, FoodCategory.PRATOS_MISTOS),
            FoodItem("Arroz frango", 25.2, FoodCategory.PRATOS_MISTOS),
            FoodItem("Arroz marisco", 8.3, FoodCategory.PRATOS_MISTOS),
            FoodItem("Arroz pato", 11.0, FoodCategory.PRATOS_MISTOS),
            FoodItem("Arroz peixe", 15.5, FoodCategory.PRATOS_MISTOS),
            FoodItem("Arroz polvo", 10.9, FoodCategory.PRATOS_MISTOS),
            FoodItem("Arroz valenciana", 7.7, FoodCategory.PRATOS_MISTOS),
            FoodItem("Bacalhau gomes de sá", 13.1, FoodCategory.PRATOS_MISTOS),
            FoodItem("Caldeirada de Bacalhau", 7.1, FoodCategory.PRATOS_MISTOS),
            FoodItem("Carne estufada c/ ervilhas e bat", 5.4, FoodCategory.PRATOS_MISTOS),
            FoodItem("Empadão de carne (puré)", 10.7, FoodCategory.PRATOS_MISTOS),
            FoodItem("Massada de peixe", 14.0, FoodCategory.PRATOS_MISTOS),
            FoodItem("Salada de bacalhau com grão", 7.6, FoodCategory.PRATOS_MISTOS),
            FoodItem("Salada russa", 5.6, FoodCategory.PRATOS_MISTOS),
            FoodItem("Quiche vegetais", 12.9, FoodCategory.PRATOS_MISTOS)
        ))
        
        // PASTELARIA
        foods.addAll(listOf(
            FoodItem("Bolo de arroz", 62.7, FoodCategory.PASTELARIA),
            FoodItem("Bolo de Berlim s/ creme", 43.7, FoodCategory.PASTELARIA),
            FoodItem("Bolo de Berlim c/ creme", 68.9, FoodCategory.PASTELARIA),
            FoodItem("Bolo de bolacha", 46.3, FoodCategory.PASTELARIA),
            FoodItem("Bolo de chocolate", 47.0, FoodCategory.PASTELARIA),
            FoodItem("Croissant simples", 42.2, FoodCategory.PASTELARIA),
            FoodItem("Pão de leite", 51.4, FoodCategory.PASTELARIA),
            FoodItem("Pão de ló", 62.2, FoodCategory.PASTELARIA),
            FoodItem("Pastel de feijão", 52.0, FoodCategory.PASTELARIA),
            FoodItem("Pastel de nata", 48.5, FoodCategory.PASTELARIA),
            FoodItem("Queijada de requeijão", 40.8, FoodCategory.PASTELARIA),
            FoodItem("Brioche", 53.8, FoodCategory.PASTELARIA),
            FoodItem("Pão de deus", 42.0, FoodCategory.PASTELARIA),
            FoodItem("Salame", 53.6, FoodCategory.PASTELARIA),
            FoodItem("Palmiers", 57.0, FoodCategory.PASTELARIA),
            FoodItem("Bolo Rei", 57.4, FoodCategory.PASTELARIA),
            FoodItem("Tarte de maçã", 29.3, FoodCategory.PASTELARIA),
            FoodItem("Pipocas doces (médias)", 64.0, FoodCategory.PASTELARIA),
            FoodItem("Pipocas salgadas (médias)", 51.4, FoodCategory.PASTELARIA)
        ))
        
        // SOBREMESAS
        foods.addAll(listOf(
            FoodItem("Arroz doce", 45.2, FoodCategory.SOBREMESAS),
            FoodItem("Gelado leite/nata", 53.1, FoodCategory.SOBREMESAS),
            FoodItem("Gelatina", 15.4, FoodCategory.SOBREMESAS),
            FoodItem("Leite creme", 19.5, FoodCategory.SOBREMESAS),
            FoodItem("Mousse chocolate", 31.0, FoodCategory.SOBREMESAS),
            FoodItem("Pudim de ovos e leite", 44.8, FoodCategory.SOBREMESAS),
            FoodItem("Rabanadas", 31.3, FoodCategory.SOBREMESAS),
            FoodItem("Sonhos", 29.2, FoodCategory.SOBREMESAS),
            FoodItem("Açúcar", 99.3, FoodCategory.SOBREMESAS),
            FoodItem("Mel", 78.0, FoodCategory.SOBREMESAS)
        ))
        
        // SALGADOS E CHARCUTARIA
        foods.addAll(listOf(
            FoodItem("Batata frita caseira palitos", 27.6, FoodCategory.SALGADOS_CHARCUTARIA),
            FoodItem("Chamuça", 39.3, FoodCategory.SALGADOS_CHARCUTARIA),
            FoodItem("Croquete", 23.1, FoodCategory.SALGADOS_CHARCUTARIA),
            FoodItem("Empada de frango", 33.9, FoodCategory.SALGADOS_CHARCUTARIA),
            FoodItem("Rissol", 28.0, FoodCategory.SALGADOS_CHARCUTARIA),
            FoodItem("Pastel de bacalhau", 12.3, FoodCategory.SALGADOS_CHARCUTARIA),
            FoodItem("Pastel folhado carne", 37.4, FoodCategory.SALGADOS_CHARCUTARIA),
            FoodItem("Alheira/Farinheira crua (média)", 27.0, FoodCategory.SALGADOS_CHARCUTARIA),
            FoodItem("Pão com chouriço", 45.6, FoodCategory.SALGADOS_CHARCUTARIA)
        ))
        
        // PÃO E PRODUTOS AFINS
        foods.addAll(listOf(
            FoodItem("Pão de trigo branco", 57.3, FoodCategory.PAO_PRODUTOS_AFINS),
            FoodItem("Pão trigo integral", 39.9, FoodCategory.PAO_PRODUTOS_AFINS),
            FoodItem("Pão trigo integral c/ sementes", 43.2, FoodCategory.PAO_PRODUTOS_AFINS),
            FoodItem("Pão de centeio", 56.4, FoodCategory.PAO_PRODUTOS_AFINS),
            FoodItem("Pão centeio integral", 41.3, FoodCategory.PAO_PRODUTOS_AFINS),
            FoodItem("Pão mistura", 53.8, FoodCategory.PAO_PRODUTOS_AFINS),
            FoodItem("Pão milho", 37.2, FoodCategory.PAO_PRODUTOS_AFINS),
            FoodItem("Wrap simples", 53.0, FoodCategory.PAO_PRODUTOS_AFINS)
        ))
        
        // CEREAIS E BISCOITOS
        foods.addAll(listOf(
            FoodItem("Biscoito caseiro simples", 75.0, FoodCategory.CEREAIS_BISCOITOS),
            FoodItem("Croutons", 74.0, FoodCategory.CEREAIS_BISCOITOS),
            FoodItem("Flocos de aveia", 61.7, FoodCategory.CEREAIS_BISCOITOS),
            FoodItem("Muesli", 69.1, FoodCategory.CEREAIS_BISCOITOS),
            FoodItem("Granola/Granola sem açúcar", 72.3, FoodCategory.CEREAIS_BISCOITOS),
            FoodItem("Tapioca", 87.5, FoodCategory.CEREAIS_BISCOITOS),
            FoodItem("Tosta de trigo", 69.7, FoodCategory.CEREAIS_BISCOITOS),
            FoodItem("Gresinho", 67.0, FoodCategory.CEREAIS_BISCOITOS)
        ))
        
        // LACTICÍNIOS E BEBIDAS VEGETAIS
        foods.addAll(listOf(
            FoodItem("Leite de vaca", 4.9, FoodCategory.LACTICINIOS_BEBIDAS),
            FoodItem("Iogurte natural ou 0% açúcar", 5.0, FoodCategory.LACTICINIOS_BEBIDAS),
            FoodItem("Iogurte líquido 0%açúcar", 4.4, FoodCategory.LACTICINIOS_BEBIDAS),
            FoodItem("Bebida amêndoa", 5.3, FoodCategory.LACTICINIOS_BEBIDAS),
            FoodItem("Bebida arroz", 11.8, FoodCategory.LACTICINIOS_BEBIDAS),
            FoodItem("Bebida aveia", 8.2, FoodCategory.LACTICINIOS_BEBIDAS),
            FoodItem("Bebida soja natural 0%açúcar", 0.4, FoodCategory.LACTICINIOS_BEBIDAS)
        ))
        
        // SOPAS
        foods.addAll(listOf(
            FoodItem("Sopa de legumes (média)", 4.5, FoodCategory.SOPAS),
            FoodItem("Caldo verde", 5.3, FoodCategory.SOPAS),
            FoodItem("Sopa de grão/feijão (média)", 6.0, FoodCategory.SOPAS),
            FoodItem("Creme de ervilhas/favas (média)", 6.0, FoodCategory.SOPAS),
            FoodItem("Canja de galinha c/ arroz", 5.8, FoodCategory.SOPAS),
            FoodItem("Sopa de peixe c/ massa", 5.8, FoodCategory.SOPAS),
            FoodItem("Canja c/ massa", 2.9, FoodCategory.SOPAS)
        ))
        
        // ARROZ
        foods.addAll(listOf(
            FoodItem("Arroz integral cozido", 33.3, FoodCategory.ARROZ),
            FoodItem("Arroz \"solto\" cozido", 28.0, FoodCategory.ARROZ),
            FoodItem("Arroz de cenoura", 20.0, FoodCategory.ARROZ),
            FoodItem("Arroz ervilhas", 18.0, FoodCategory.ARROZ),
            FoodItem("Arroz de feijão", 20.8, FoodCategory.ARROZ),
            FoodItem("Arroz malandrinho", 16.8, FoodCategory.ARROZ),
            FoodItem("Arroz agulha cru", 78.0, FoodCategory.ARROZ),
            FoodItem("Arroz integral cru", 71.6, FoodCategory.ARROZ)
        ))
        
        // MASSA
        foods.addAll(listOf(
            FoodItem("Massa cozida \"al dente\"", 34.3, FoodCategory.MASSA),
            FoodItem("Massa bem cozida", 19.9, FoodCategory.MASSA),
            FoodItem("Massa crua", 71.1, FoodCategory.MASSA),
            FoodItem("Cuscus cozido", 22.2, FoodCategory.MASSA),
            FoodItem("Bulgur/Couscous cru", 67.5, FoodCategory.MASSA),
            FoodItem("Quinoa cozida", 26.4, FoodCategory.MASSA),
            FoodItem("Quinoa/Trigo Sarraceno crus", 62.0, FoodCategory.MASSA),
            FoodItem("Millet cozido", 23.6, FoodCategory.MASSA),
            FoodItem("Millet cru", 72.9, FoodCategory.MASSA)
        ))
        
        // BATATA
        foods.addAll(listOf(
            FoodItem("Batata cozida", 18.5, FoodCategory.BATATA),
            FoodItem("Batata assada", 24.8, FoodCategory.BATATA),
            FoodItem("Batata doce assada", 28.3, FoodCategory.BATATA),
            FoodItem("Puré batata", 16.8, FoodCategory.BATATA),
            FoodItem("Batata crua", 19.2, FoodCategory.BATATA),
            FoodItem("Batata crua com pele", 16.7, FoodCategory.BATATA),
            FoodItem("Batata doce crua com pele", 21.8, FoodCategory.BATATA),
            FoodItem("Batata frita (cav.)", 24.6, FoodCategory.BATATA),
            FoodItem("Batata frita (congelada)", 29.5, FoodCategory.BATATA),
            FoodItem("Boniato/batata", 19.5, FoodCategory.BATATA)
        ))
        
        // LEGUMINOSAS
        foods.addAll(listOf(
            FoodItem("Feijão manteiga cozido", 14.0, FoodCategory.LEGUMINOSAS),
            FoodItem("Feijão branco cozido", 14.6, FoodCategory.LEGUMINOSAS),
            FoodItem("Feijão frade cozido", 18.1, FoodCategory.LEGUMINOSAS),
            FoodItem("Grão de bico cozido", 16.7, FoodCategory.LEGUMINOSAS),
            FoodItem("Ervilhas congeladas cozidas", 7.5, FoodCategory.LEGUMINOSAS),
            FoodItem("Ervilhas estufadas", 9.6, FoodCategory.LEGUMINOSAS),
            FoodItem("Favas cozidas", 7.4, FoodCategory.LEGUMINOSAS),
            FoodItem("Lentilhas secas cozidas", 16.7, FoodCategory.LEGUMINOSAS),
            FoodItem("Tremoço cozido", 7.2, FoodCategory.LEGUMINOSAS),
            FoodItem("Feijão Preto", 13.0, FoodCategory.LEGUMINOSAS)
        ))
        
        // FARINHAS
        foods.addAll(listOf(
            FoodItem("Farinha de trigo integral", 65.2, FoodCategory.FARINHAS),
            FoodItem("Farinha de trigo", 74.0, FoodCategory.FARINHAS),
            FoodItem("Farinha tipo tipo 55", 74.3, FoodCategory.FARINHAS),
            FoodItem("Farinha espelta", 69.8, FoodCategory.FARINHAS),
            FoodItem("Farinha mandioca", 84.6, FoodCategory.FARINHAS),
            FoodItem("Farinha de milho", 75.3, FoodCategory.FARINHAS),
            FoodItem("Farinha alfarroba", 85.6, FoodCategory.FARINHAS),
            FoodItem("Farinha de amêndoa", 6.9, FoodCategory.FARINHAS),
            FoodItem("Farinha de aveia", 57.0, FoodCategory.FARINHAS),
            FoodItem("Amido de milho", 90.2, FoodCategory.FARINHAS)
        ))
        
        // FRUTA FRESCA
        foods.addAll(listOf(
            FoodItem("Abacate", 1.6, FoodCategory.FRUTA_FRESCA),
            FoodItem("Abricote/Damasco", 8.0, FoodCategory.FRUTA_FRESCA),
            FoodItem("Ameixa branca", 7.4, FoodCategory.FRUTA_FRESCA),
            FoodItem("Ameixa vermelha", 7.0, FoodCategory.FRUTA_FRESCA),
            FoodItem("Ameixa rainha cláudia", 11.2, FoodCategory.FRUTA_FRESCA),
            FoodItem("Ananás", 6.5, FoodCategory.FRUTA_FRESCA),
            FoodItem("Anona", 10.4, FoodCategory.FRUTA_FRESCA),
            FoodItem("Banana", 12.9, FoodCategory.FRUTA_FRESCA),
            FoodItem("Carambola", 6.8, FoodCategory.FRUTA_FRESCA),
            FoodItem("Cereja", 11.2, FoodCategory.FRUTA_FRESCA),
            FoodItem("Clementina", 8.3, FoodCategory.FRUTA_FRESCA),
            FoodItem("Diospiro", 12.4, FoodCategory.FRUTA_FRESCA),
            FoodItem("Figo", 11.9, FoodCategory.FRUTA_FRESCA),
            FoodItem("Framboesa", 4.8, FoodCategory.FRUTA_FRESCA),
            FoodItem("Kiwi", 8.8, FoodCategory.FRUTA_FRESCA),
            FoodItem("Laranja", 6.1, FoodCategory.FRUTA_FRESCA),
            FoodItem("Lichia fresca", 14.8, FoodCategory.FRUTA_FRESCA),
            FoodItem("Maçã", 10.2, FoodCategory.FRUTA_FRESCA),
            FoodItem("Maçã cozida", 10.5, FoodCategory.FRUTA_FRESCA),
            FoodItem("Maçã assada", 15.7, FoodCategory.FRUTA_FRESCA),
            FoodItem("Manga", 8.0, FoodCategory.FRUTA_FRESCA),
            FoodItem("Marmelo", 7.3, FoodCategory.FRUTA_FRESCA),
            FoodItem("Melancia", 2.9, FoodCategory.FRUTA_FRESCA),
            FoodItem("Melão", 3.0, FoodCategory.FRUTA_FRESCA),
            FoodItem("Meloa", 2.5, FoodCategory.FRUTA_FRESCA),
            FoodItem("Mirtilo", 6.4, FoodCategory.FRUTA_FRESCA),
            FoodItem("Morango", 5.1, FoodCategory.FRUTA_FRESCA),
            FoodItem("Nectarina", 7.7, FoodCategory.FRUTA_FRESCA),
            FoodItem("Nêspera", 6.2, FoodCategory.FRUTA_FRESCA),
            FoodItem("Papaia", 5.7, FoodCategory.FRUTA_FRESCA),
            FoodItem("Pêra cozida", 7.8, FoodCategory.FRUTA_FRESCA),
            FoodItem("Pêssego", 6.2, FoodCategory.FRUTA_FRESCA),
            FoodItem("Romã", 6.0, FoodCategory.FRUTA_FRESCA),
            FoodItem("Tangerina", 6.3, FoodCategory.FRUTA_FRESCA),
            FoodItem("Uva branca", 14.9, FoodCategory.FRUTA_FRESCA),
            FoodItem("Uva tinta", 15.2, FoodCategory.FRUTA_FRESCA),
            FoodItem("Salada de fruta sem calda", 9.2, FoodCategory.FRUTA_FRESCA)
        ))
        
        // FRUTA EM CALDA
        foods.addAll(listOf(
            FoodItem("Pêssego em calda", 20.6, FoodCategory.FRUTA_CALDA),
            FoodItem("Ananás em calda", 23.2, FoodCategory.FRUTA_CALDA),
            FoodItem("Cereja em calda", 28.7, FoodCategory.FRUTA_CALDA),
            FoodItem("Pêra em calda", 28.9, FoodCategory.FRUTA_CALDA)
        ))
        
        // FRUTA DESIDRATADA
        foods.addAll(listOf(
            FoodItem("Cereja desidratada", 72.6, FoodCategory.FRUTA_DESIDRATADA),
            FoodItem("Framboesa desidratada", 30.9, FoodCategory.FRUTA_DESIDRATADA),
            FoodItem("Maçã desidratada", 74.5, FoodCategory.FRUTA_DESIDRATADA),
            FoodItem("Manga desidratada", 68.7, FoodCategory.FRUTA_DESIDRATADA),
            FoodItem("Papaia desidratada", 73.3, FoodCategory.FRUTA_DESIDRATADA),
            FoodItem("Pêra desidratada", 59.9, FoodCategory.FRUTA_DESIDRATADA)
        ))
        
        // FRUTOS AMILÁCEOS E OLEAGINOSOS
        foods.addAll(listOf(
            FoodItem("Castanha cozida", 35.9, FoodCategory.FRUTOS_AMILACEOS),
            FoodItem("Castanha assada", 45.5, FoodCategory.FRUTOS_AMILACEOS),
            FoodItem("Amêndoa torrada s/ pele", 7.1, FoodCategory.FRUTOS_AMILACEOS),
            FoodItem("Amendoim natural", 10.1, FoodCategory.FRUTOS_AMILACEOS),
            FoodItem("Avelã", 6.0, FoodCategory.FRUTOS_AMILACEOS),
            FoodItem("Noz", 3.6, FoodCategory.FRUTOS_AMILACEOS),
            FoodItem("Pinhão", 5.0, FoodCategory.FRUTOS_AMILACEOS),
            FoodItem("Pistáchio torrado", 12.6, FoodCategory.FRUTOS_AMILACEOS),
            FoodItem("Caju torrado", 19.4, FoodCategory.FRUTOS_AMILACEOS)
        ))
        
        // FRUTOS SECOS
        foods.addAll(listOf(
            FoodItem("Ameixa", 31.0, FoodCategory.FRUTOS_SECOS),
            FoodItem("Figo", 57.2, FoodCategory.FRUTOS_SECOS),
            FoodItem("Tâmara", 60.6, FoodCategory.FRUTOS_SECOS),
            FoodItem("Tâmara s/ caroço", 67.3, FoodCategory.FRUTOS_SECOS),
            FoodItem("Uva passa", 66.3, FoodCategory.FRUTOS_SECOS)
        ))
        
        // SEMENTES
        foods.addAll(listOf(
            FoodItem("Abóbora", 13.9, FoodCategory.SEMENTES),
            FoodItem("Cânhamo", 2.4, FoodCategory.SEMENTES),
            FoodItem("Chia", 7.7, FoodCategory.SEMENTES),
            FoodItem("Girassol", 17.0, FoodCategory.SEMENTES),
            FoodItem("Linhaça", 18.1, FoodCategory.SEMENTES),
            FoodItem("Papoila", 13.7, FoodCategory.SEMENTES),
            FoodItem("Sésamo", 6.4, FoodCategory.SEMENTES)
        ))
        
        // SUSHI
        foods.addAll(listOf(
            FoodItem("Uramaki/Rolos california (média)", 25.0, FoodCategory.SUSHI),
            FoodItem("Temaki (média)", 17.5, FoodCategory.SUSHI),
            FoodItem("Maki", 29.0, FoodCategory.SUSHI),
            FoodItem("Nigiri (média)", 30.0, FoodCategory.SUSHI),
            FoodItem("Tempura camarão", 10.4, FoodCategory.SUSHI),
            FoodItem("Tempura de vegetais", 13.8, FoodCategory.SUSHI)
        ))
        
        return foods
    }
} 