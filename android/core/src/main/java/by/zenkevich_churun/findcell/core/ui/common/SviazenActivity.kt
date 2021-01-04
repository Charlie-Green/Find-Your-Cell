package by.zenkevich_churun.findcell.core.ui.common

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


/** Same purpose as [SviazenFragment], but derived from [AppCompatActivity]. **/
abstract class SviazenActivity<
    ViewBindingType: ViewBinding
>: AppCompatActivity() {

    protected lateinit var vb: ViewBindingType


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = inflateViewBinding()
        customizeView(vb.root)
        setContentView(vb.root)
    }


    protected abstract fun inflateViewBinding(): ViewBindingType

    /** The place to customize [AppCompatActivity]'s [View]
      * before setting it as content, customize [Window], etc. **/
    protected open fun customizeView(v: View) {
        // Default implementation is empty.
    }
}