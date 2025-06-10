package org.expensetrackerui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import org.expensetrackerui.presentation.MainViewModel
import org.expensetrackerui.presentation.addexpense.AddExpenseViewModel
import org.expensetrackerui.presentation.home.HomeViewModel
import org.expensetrackerui.presentation.showexpenses.ShowExpensesViewModel
import org.expensetrackerui.theme.CustomDarkColorScheme
import org.expensetrackerui.theme.CustomLightColorScheme
import org.expensetrackerui.theme.ExpenseTrackerUITheme
import org.expensetrackerui.theme.Typography
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = get()
            val homeViewModel: HomeViewModel = get()
            val addExpenseViewModel: AddExpenseViewModel = get()
            val showExpensesViewModel: ShowExpensesViewModel = get()

            val darkTheme = isSystemInDarkTheme()

            val context = LocalContext.current
            val dynamicColor = true

            val colorScheme = when {
                dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
                }
                darkTheme -> CustomDarkColorScheme
                else -> CustomLightColorScheme
            }

            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    window.statusBarColor = colorScheme.primary.toArgb()
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                }
            }

            ExpenseTrackerUITheme(
                darkTheme = darkTheme
            ) {
                MaterialTheme(
                    colorScheme = colorScheme,
                    typography = Typography,
                    content = {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            App(
                                mainViewModel = mainViewModel,
                                homeViewModel = homeViewModel,
                                addExpenseViewModel = addExpenseViewModel,
                                showExpensesViewModel = showExpensesViewModel
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}