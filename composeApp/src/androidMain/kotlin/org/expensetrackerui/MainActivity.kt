package org.expensetrackerui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.expensetrackerui.presentation.MainViewModel
import org.expensetrackerui.presentation.home.HomeViewModel
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = get()
            val homeViewModel: HomeViewModel = get()
            App(mainViewModel = mainViewModel, homeViewModel = homeViewModel)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}