package by.sviazen.core.util.behave.snackbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import com.google.android.material.snackbar.Snackbar


/** Behavior that makes the dependent [View] pop its content up and down
  * when a [Snackbar] shows up. **/
class RespectSnackbarBehavior: CoordinatorLayout.Behavior<View> {

    constructor():
        super()
    constructor(context: Context, attrs: AttributeSet?):
        super(context, attrs)


    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }


    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        child.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            bottomMargin = dependency.height - dependency.translationY.toInt()
        }

        return true  // Child's size was changed.
    }
}