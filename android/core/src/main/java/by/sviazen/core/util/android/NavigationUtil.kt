package by.sviazen.core.util.android

import android.os.Bundle
import androidx.navigation.NavController


object NavigationUtil {

    inline fun safeNavigate(
        controller: NavController,
        currentNav: Int,
        targetNav: Int,
        createArgs: () -> Bundle? ) {

        if(controller.currentDestination?.id == currentNav) {
            controller.navigate(targetNav, createArgs())
        }
    }


    inline fun navigateIfNotYet(
        controller: NavController,
        desiredNav: Int,
        actionRes: Int,
        createArgs: () -> Bundle? ) {

        if(controller.currentDestination?.id != desiredNav) {
            controller.navigate(actionRes, createArgs())
        }
    }

    inline fun navigateIfNotYet(
        controller: NavController,
        desiredNav: Int,
        createArgs: () -> Bundle?
    ) = navigateIfNotYet(controller, desiredNav, desiredNav, createArgs)
}