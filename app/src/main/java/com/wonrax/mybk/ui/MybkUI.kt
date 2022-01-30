package com.wonrax.mybk.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wonrax.mybk.Greeting
import com.wonrax.mybk.ui.component.BottomNavigation
import com.wonrax.mybk.ui.component.Screen
import com.wonrax.mybk.ui.theme.MybkTheme

@Composable
fun MybkUI() {
    MybkTheme(false) {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigation { navController.navigate(it) } }
        ) {
            Surface(
                modifier = Modifier.background(Color.LightGray).padding(16.dp).fillMaxSize(),
                color = Color.LightGray
            ) {
                Navigation(navController)
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.id) {

        val home = Screen.Home
        val schedules = Screen.Schedules
        val transcript = Screen.Transcript
        val profile = Screen.Profile

        composable(home.id) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        composable(schedules.id) {
            Greeting(name = schedules.title)
        }
        composable(transcript.id) {
            Greeting(name = transcript.title)
        }
        composable(profile.id) {
            Greeting(name = profile.title)
        }
    }
}
