package com.domker.doctor.explorer.menu

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.domker.doctor.explorer.R


/**
 *
 * Created by wanlipeng on 12/30/20 3:22 PM
 */
class ExplorerMenu(context: Context, anchor: View) : PopupMenu(context, anchor) {

    init {
        menuInflater.inflate(R.menu.apk_explorer_item_menu, menu)
    }

}