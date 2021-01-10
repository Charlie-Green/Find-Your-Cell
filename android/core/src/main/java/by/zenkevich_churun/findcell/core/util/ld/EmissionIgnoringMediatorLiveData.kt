package by.zenkevich_churun.findcell.core.util.ld

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import by.zenkevich_churun.findcell.core.util.android.AndroidUtil


/** Modification of [MediatorLiveData]
  * allowing to ignore an upcoming emission of an item. **/
class EmissionIgnoringMediatorLiveData<T>(
    private val source: LiveData<out T>
): MediatorLiveData<T>() {

    private var lastValue: T? = null
    private var valueIgnored = false

    var ignoreNext = false
        get() {
            synchronized(this) {
                return field
            }
        }
        set(value) {
            synchronized(this) {
                field = value
            }
        }


    init {
        addSource(source) { t ->
            setOrIgnore(t)
        }
    }


    fun setIgnoredValue() {
        if(valueIgnored) {
            AndroidUtil.setOrPost(this, lastValue)
            valueIgnored = false
            lastValue = null
        }
    }


    private fun setOrIgnore(t: T) {
        Log.v("CharlieDebug", "newValue. ignoreNext = $ignoreNext")
        lastValue = t

        synchronized(this) {
            if(ignoreNext) {
                valueIgnored = true
                ignoreNext = false
            } else {
                AndroidUtil.setOrPost(this, t)
                valueIgnored = false
            }
        }

    }
}