package com.example.jetareader.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetareader.screens.ReaderSplashScreen
import com.example.jetareader.screens.details.ReaderBookDetailsScreen
import com.example.jetareader.screens.home.HomeScreenViewModel
import com.example.jetareader.screens.home.ReaderHomeScreen
import com.example.jetareader.screens.login.ReaderLoginScreen
import com.example.jetareader.screens.search.BookSearchViewModel
import com.example.jetareader.screens.search.ReaderSearchScreen
import com.example.jetareader.screens.stats.ReaderStatsScreen
import com.example.jetareader.screens.update.ReaderBookUpdateScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReaderNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = ReaderScreens.SplashScreen.name){

        composable(ReaderScreens.SplashScreen.name){
            ReaderSplashScreen(navController = navController)
        }

        composable(ReaderScreens.LoginScreen.name){
            ReaderLoginScreen(navController = navController)
        }

        composable(ReaderScreens.ReaderStatsScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderStatsScreen(navController = navController, viewModel = homeViewModel)
        }

        composable(ReaderScreens.ReaderHomeScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderHomeScreen(navController = navController, viewModel = homeViewModel)
        }

        composable(ReaderScreens.SearchScreen.name){
            val searchViewModel= hiltViewModel<BookSearchViewModel>()
            ReaderSearchScreen(navController = navController, viewModel=searchViewModel)
        }

        val detailName = ReaderScreens.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })){ backStackEntry->
            backStackEntry.arguments?.getString("bookId").let {
                ReaderBookDetailsScreen(navController = navController, bookId = it.toString())
            }
        }

        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}",
            arguments = listOf(navArgument("bookItemId"){
                type=NavType.StringType
            })){ navBackStackEntry ->
            navBackStackEntry.arguments?.getString("bookItemId").let {
                ReaderBookUpdateScreen(navController=navController, bookItemId = it.toString())
            }
            
        }
    }
}