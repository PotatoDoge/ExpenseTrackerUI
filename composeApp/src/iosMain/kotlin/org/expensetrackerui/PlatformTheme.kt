
package org.expensetrackerui // <-- MUST MATCH THE EXPECT PACKAGE

import androidx.compose.runtime.Composable
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIScreen

@Composable
actual fun isSystemInDarkThemeKmp(): Boolean {
    return UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
}