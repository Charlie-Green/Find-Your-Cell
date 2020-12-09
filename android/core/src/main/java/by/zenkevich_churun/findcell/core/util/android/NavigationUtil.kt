package by.zenkevich_churun.findcell.core.util.android

import android.os.Bundle
import androidx.navigation.NavController


object NavigationUtil {

    fun safeNavigate(
        controller: NavController,
        currentNav: Int,
        targetNav: Int,
        args: Bundle? = null ) {

        if(controller.currentDestination?.id == currentNav) {
            controller.navigate(targetNav, args)
        }
    }
}