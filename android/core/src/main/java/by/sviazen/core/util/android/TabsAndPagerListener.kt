package by.sviazen.core.util.android

import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout


/** Helper class to bind [TabLayout] and [ViewPager2] with each other. **/
open class TabsAndPagerListener(
    private val tabs: TabLayout,
    private val pager: ViewPager2
): ViewPager2.OnPageChangeCallback(), TabLayout.OnTabSelectedListener {

    override fun onTabUnselected(tab: TabLayout.Tab) {   }
    override fun onTabReselected(tab: TabLayout.Tab) {   }


    override fun onTabSelected(tab: TabLayout.Tab) {
        if(pager.currentItem != tab.position) {
            pager.setCurrentItem(tab.position, true)
        }
    }

    override fun onPageSelected(position: Int) {
        if(tabs.selectedTabPosition != position) {
            tabs.selectTab( tabs.getTabAt(position) )
        }
    }


    fun attach() {
        tabs.addOnTabSelectedListener(this)
        pager.registerOnPageChangeCallback(this)
    }

    fun detach() {
        tabs.removeOnTabSelectedListener(this)
        pager.unregisterOnPageChangeCallback(this)
    }
}