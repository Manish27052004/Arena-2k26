package com.pmec.arena2k26

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pmec.arena2k26.ui.organiserHomeScreen.CreateMatchScreen
import com.pmec.arena2k26.ui.organiserHomeScreen.OrgHomeScreen
import com.pmec.arena2k26.ui.theme.Arena2k26Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Arena2k26Theme {
                CreateMatchScreen()
            }
        }
    }
}

