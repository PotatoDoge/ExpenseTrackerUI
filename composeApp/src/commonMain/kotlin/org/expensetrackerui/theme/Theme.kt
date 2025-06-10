package org.expensetrackerui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


val CustomLightColorScheme = lightColorScheme(
    primary = PrimaryCustom,
    onPrimary = OnPrimaryCustom,
    secondary = SecondaryCustom,
    tertiary = TertiaryCustom,
    background = BackgroundCustom,
    surface = SurfaceCustom,
    onBackground = OnSurfaceCustom,
    onSurface = OnSurfaceCustom,
    error = ErrorCustom,
    onError = OnErrorCustom,
)

 val CustomDarkColorScheme = darkColorScheme(
    primary = PrimaryCustom,
    onPrimary = OnPrimaryCustom,
    secondary = SecondaryCustom,
    tertiary = TertiaryCustom,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color.White,
    onSurface = Color.White,
    error = ErrorCustom,
    onError = OnErrorCustom,
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
fun ExpenseTrackerUITheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) CustomDarkColorScheme else CustomLightColorScheme

    val typography = Typography

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}