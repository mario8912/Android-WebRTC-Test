package com.example.wcmobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = MyPastelRed,
    secondary = MyPastelRed,
    tertiary = MyPastelRed,
    background = MyPastelRed,
    surface = MyYellow,
    onPrimary = MyYellow,
    onSecondary = MyYellow,
    onTertiary = MyYellow,
    onBackground = MyYellow,
    onSurface = MyYellow
)

private val LightColorScheme = lightColorScheme(
    primary = MyPastelRedTranslucid,
    secondary = MyPastelRed,
    tertiary = MyPastelRed,
    background = MyPastelRedDark, //botones de navegacion del sistema y fondo
    //surface = MyGray,

    onPrimary = MyYellow,
    onSecondary = MyYellow,
    onTertiary = MyYellow,
    onBackground = MyGray,
    onSurface = MyGray,

)

@Composable
fun WCMobileTheme(
    //darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    //dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    /*val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }*/

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}