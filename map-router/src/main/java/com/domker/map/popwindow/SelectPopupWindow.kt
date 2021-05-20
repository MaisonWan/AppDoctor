package com.domker.map.popwindow

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import com.domker.map.R

/**
 * Created by cretin on 16/3/4.
 */
class SelectPopupWindow(
    context: Activity,
    listener: OnPopWindowClickListener,
    showGaode: Boolean,
    showBaidu: Boolean
) : PopupWindow(), View.OnClickListener {
    private var mMenuView: View? = null
    private var listener: OnPopWindowClickListener? = null

    @SuppressLint("ClickableViewAccessibility")
    private fun initView(
        context: Activity,
        listener: OnPopWindowClickListener,
        showGaode: Boolean,
        showBaidu: Boolean
    ) {
        this.listener = listener
        //设置按钮监听
        initViewSelectedPic(context, showGaode, showBaidu)

        //设置SelectPicPopupWindow的View
        this.contentView = mMenuView
        //设置SelectPicPopupWindow弹出窗体的宽
        this.width = ViewGroup.LayoutParams.MATCH_PARENT
        //设置SelectPicPopupWindow弹出窗体的高
        this.height = ViewGroup.LayoutParams.WRAP_CONTENT
        //设置SelectPicPopupWindow弹出窗体可点击
        this.isFocusable = true
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.dialog_style);
        //实例化一个ColorDrawable颜色为半透明
        val dw = ColorDrawable(-0x50000000)
        //设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw)
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView?.setOnTouchListener { v, event ->
            val height = mMenuView!!.findViewById<View>(R.id.pop_layout).top
            val y = event.y.toInt()
            if (event.action == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss()
                }
            }
            true
        }
    }

    private fun initViewSelectedPic(context: Activity, showGaode: Boolean, showBaidu: Boolean) {
        val btnGaode: Button
        val btnBaidu: Button
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mMenuView = inflater.inflate(R.layout.layout_popwindow_dialog_select_type, null)
        if (showGaode) {
            btnGaode = mMenuView?.findViewById(R.id.btn_select_gaode) as Button
            mMenuView?.findViewById<View>(R.id.ll_gaode)?.visibility = View.VISIBLE
            btnGaode.setOnClickListener(this)
        }
        if (showBaidu) {
            btnBaidu = mMenuView?.findViewById<View>(R.id.btn_select_baidu) as Button
            mMenuView?.findViewById<View>(R.id.ll_baidu)?.visibility = View.VISIBLE
            btnBaidu.setOnClickListener(this)
        }
        val btnCancel: Button = mMenuView?.findViewById<View>(R.id.btn_select_pic_cancel) as Button
        btnCancel.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        listener?.onPopWindowClickListener(v)
        dismiss()
    }

    interface OnPopWindowClickListener {
        fun onPopWindowClickListener(view: View)
    }

    interface OnPswClickListener {
        fun onPswClickListener(psw: String?, complete: Boolean)
    }

    init {
        initView(context, listener, showGaode, showBaidu)
    }
}