package com.domker.map.utils

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.domker.map.R

/**************************************************************************************
 * [Project]
 * MyProgressDialog
 * [Package]
 * com.lxd.widgets
 * [FileName]
 * CustomProgressDialog.java
 * [Copyright]
 * Copyright 2012 LXD All Rights Reserved.
 * [History]
 * Version          Date              Author                        Record
 * --------------------------------------------------------------------------------------
 * 1.0.0           2012-4-27         lxd (rohsuton@gmail.com)        Create
 */
class CustomProgressDialog : Dialog {

    constructor(context: Context?) : super(context!!) {
    }

    constructor(context: Context?, theme: Int) : super(context!!, theme) {}

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        val imageView = findViewById<View>(R.id.loading_image) as ImageView
        val animationDrawable = imageView.background as AnimationDrawable
        animationDrawable.start()
    }

    /**
     * [Summary] setTitle 标题
     *
     * @param title
     * @return
     */
    fun setTitle(title: String?): CustomProgressDialog {
        return this.setTitle(title)
    }

    /**
     * [Summary] setMessage 提示内容
     *
     * @param strMessage
     * @return
     */
    fun setMessage(strMessage: String?): CustomProgressDialog {
        val tvMsg = findViewById<View>(R.id.loading_msg) as TextView?
        if (tvMsg != null) {
            tvMsg.text = strMessage
        }
        return this
    }

    companion object {
        @JvmStatic
        fun createDialog(context: Context?): CustomProgressDialog {
            val customProgressDialog = CustomProgressDialog(
                context,
                R.style.CustomProgressDialog
            )
            customProgressDialog.setContentView(R.layout.custom_progressdialog)
            customProgressDialog.window!!.attributes.gravity = Gravity.CENTER
            return customProgressDialog
        }
    }
}