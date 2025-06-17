package com.francisco.glucosmart.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.francisco.glucosmart.ui.theme.screens.TablesPage.TablesPage
import com.francisco.glucosmart.ui.theme.screens.home.HomePage
import com.francisco.glucosmart.ui.theme.screens.search.SearchPage
import com.francisco.glucosmart.ui.theme.screens.calculator.CalculatorPage
import com.francisco.glucosmart.ui.theme.screens.meals.MealsPage
import com.francisco.glucosmart.ui.theme.screens.addfood.AddFoodPage
import com.francisco.glucosmart.ui.theme.screens.history.HistoryPage
import com.francisco.glucosmart.ui.theme.screens.login.LoginScreen
import com.francisco.glucosmart.ui.theme.screens.receitatual.ReceitaAtualPage
import com.francisco.glucosmart.ui.theme.screens.settings.SettingsScreen
import com.francisco.glucosmart.ui.theme.screens.singup.SignUpScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomePage(navController) }
        composable(Routes.SEARCH) { SearchPage(navController) }
        composable(Routes.CALCULATOR) { CalculatorPage(navController) }
        composable(Routes.MEALS) { MealsPage(navController) }
        composable(Routes.TABLES) { TablesPage(navController) }
        composable(Routes.ADD_FOOD) { AddFoodPage(navController) }
        composable(Routes.HISTORY) { HistoryPage(navController) }
        composable(Routes.RECEITA_ATUAL) { ReceitaAtualPage(navController, null) }
        composable(
            "${Routes.RECEITA_ATUAL}/{mealId}",
            arguments = listOf(navArgument("mealId") { nullable = true })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId")
            ReceitaAtualPage(navController, mealId)
        }
        composable(Routes.LOGIN) { LoginScreen(navController) }
        composable(Routes.SIGNUP) { SignUpScreen(navController) }
        composable(Routes.SETTINGS) { SettingsScreen(navController) }
    }
}
