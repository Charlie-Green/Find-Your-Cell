package by.sviazen.core.util.recycler.autogrid

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import by.sviazen.core.util.android.AndroidUtil


class AutomaticGridLayoutManager(
    context: Context,
    containerWidth: Int,
    minItemWidth: Int
): GridLayoutManager(context, containerWidth/minItemWidth) {

    constructor(activity: Activity, minItemWidth: Int):
        this( activity, AndroidUtil.activitySize(activity).width, minItemWidth )
}