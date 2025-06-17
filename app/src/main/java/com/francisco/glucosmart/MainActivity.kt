package com.francisco.glucosmart

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.francisco.glucosmart.ui.theme.GlucoSmartTheme
import com.francisco.glucosmart.ui.theme.navigation.NavGraph
import com.francisco.glucosmart.ui.theme.navigation.Routes
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Instalar a tela de splash antes de chamar o onCreate
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Manter a tela de splash visível até que estejamos prontos para mostrar o conteúdo principal
        var keepSplashOnScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        
        setContent {
            GlucoSmartTheme {
                val navController = rememberNavController()
                
                // Tela de splash animada
                Box(modifier = Modifier.fillMaxSize()) {
                    SplashScreen(onSplashFinished = {
                        keepSplashOnScreen = false
                    })
                    
                    Surface(modifier = Modifier.fillMaxSize()) {
                        NavGraph(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val scale = remember { Animatable(0f) }
    
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(1000L)
        onSplashFinished()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.mipmap.glucologo_foreground),
            contentDescription = "Logo",
            modifier = Modifier.scale(scale.value)
        )
    }
}
