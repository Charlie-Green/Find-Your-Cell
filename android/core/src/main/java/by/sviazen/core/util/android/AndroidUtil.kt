package by.sviazen.core.util.android

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Size
import android.util.TypedValue
import android.view.*
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


object AndroidUtil {
    private val typval by lazy { TypedValue() }


    fun activitySize(activity: Activity): Size {
        if(Build.VERSION.SDK_INT < 30) {
            val metrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            return Size(metrics.widthPixels, metrics.heightPixels)
        }

        val metrics = activity.windowManager.currentWindowMetrics
        val insets = metrics.windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout() )
        val iw = insets.left + insets.right
        val ih = insets.top + insets.bottom

        val bounds = metrics.bounds
        return Size(bounds.width() - iw, bounds.height() - ih)
    }


    fun themeColor(appContext: Context, attrRes: Int): Int {
        appContext.theme.resolveAttribute(attrRes, typval, true)
        return typval.data
    }


    fun mergeStrings(
        appContext: Context,
        vararg stringRess: Int
    ): String {

        val sb = StringBuilder(64*stringRess.size)

        for(stringRes in stringRess) {
            val string = appContext.getString(stringRes)

            if(!sb.isEmpty()) {
                sb.append(" ")
            }
            sb.append(string)
        }

        return sb.toString()
    }

    fun stringByResourceName(
        context: Context,
        resourceName: String
    ): String? {

        val res = context.resources.getIdentifier(
            resourceName,
            "string",
            context.packageName
        )

        return if(res == 0) null else context.getString(res)
    }


    /** Sets a special mode to the given [Window] so that it's keyboard
      * is hidden unless the user explicitly makes it show up (touches an [EditText]) **/
    fun defaultHideKeyboard(target: Window) {
        target.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }


    val isThreadMain: Boolean
        get() {
            return Looper.myLooper() == Looper.getMainLooper()
        }


    fun <T> setOrPost(ld: MutableLiveData<T>, newValue: T) {
        if(isThreadMain) {
            ld.value = newValue
        } else {
            ld.postValue(newValue)
        }
    }


    fun setRefreshThemeColors(refreshLayout: SwipeRefreshLayout, vararg attrs: Int) {
        val colors = IntArray(attrs.size) { index ->
            themeColor(refreshLayout.context, attrs[index])
        }
        refreshLayout.setColorSchemeColors(*colors)
    }
}