package by.zenkevich_churun.findcell.core.util.ld

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData


/** Modification of [MediatorLiveData]
  * allowing to ignore an upcoming emission of an item. **/
class EmissionIgnoringMediatorLiveData<T>(
    private val source: LiveData<out T>
): MediatorLiveData<T>() {

    private var ignoredValue: T? = null
    private var valueIgnored = false

    var ignoreNext = false


    init {
        addSource(source) { t ->
            setOrIgnore(t)
        }
    }


    fun setIgnoredValue() {
        if(valueIgnored) {
            value = ignoredValue
            valueIgnored = false
            ignoredValue = null
        }
    }


    private fun setOrIgnore(t: T) {
        if(ignoreNext) {
            ignoredValue = t
            valueIgnored = true
            ignoreNext = false
        } else {
            value = t
        }
    }
}