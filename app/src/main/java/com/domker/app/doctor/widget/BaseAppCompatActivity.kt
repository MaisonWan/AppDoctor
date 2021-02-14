package com.domker.app.doctor.widget

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.domker.app.doctor.R

/**
 * 基类
 * Created by wanlipeng on 2/15/21 12:12 AM
 */
abstract class BaseAppCompatActivity : AppCompatActivity() {

    /**
     * 初始化工具栏
     */
    protected fun initToolbar() {
        val toolbar: Toolbar? = findViewById(R.id.toolbar)
        toolbar?.let {
            setSupportActionBar(it)
            it.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24)
            it.setNavigationOnClickListener {
                finish()
            }
        }
    }
}