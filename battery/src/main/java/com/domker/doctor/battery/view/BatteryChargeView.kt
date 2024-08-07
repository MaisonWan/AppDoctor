package com.domker.doctor.battery.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.domker.doctor.battery.R


/**
 * 电池展示View
 */
class BatteryChargeView : View {
    // 画笔
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 文字相关
    private lateinit var textPaint: TextPaint
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f


    // 绘制曲线矩形区位置，提前创建好，避免绘制的时候耗时
    private val oval = RectF()

    // 属性颜色
    private var cycleBackgroundColor: Int = Color.GRAY
    private var cycleColor: Int = Color.GREEN
    private var lowChargeColor: Int = Color.RED
    private var strokeWidth = 30f
    private var textSize = 20f

    // 范例文本
    private val demoText = "00%"

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BatteryChargeView, defStyle, 0)
        cycleBackgroundColor = a.getColor(R.styleable.BatteryChargeView_backgroundColor, Color.GRAY)
        cycleColor = a.getColor(R.styleable.BatteryChargeView_normalColor, Color.GREEN)
        lowChargeColor = a.getColor(R.styleable.BatteryChargeView_warningColor, Color.RED)
        strokeWidth = a.getDimension(R.styleable.BatteryChargeView_strokeWidth, 30f)
        textSize = a.getDimension(R.styleable.BatteryChargeView_textSize, 20f)

        a.recycle()

        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }

        invalidateTextPaintAndMeasurements()
    }

    /**
     * 计算文本占用空间
     */
    private fun invalidateTextPaintAndMeasurements() {
        textPaint.let {
            it.textSize = textSize
            it.color = Color.GRAY
            textWidth = it.measureText(demoText)
            textHeight = it.fontMetrics.bottom
        }
    }

    init {
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.STROKE
    }

    /**
     * 当前需要显示的电量
     */
    var charge: Int = 60
        set(value) {
            field = when {
                value < 0 -> 0
                value > 100 -> 100
                else -> value
            }
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        // 圆心位置
        val x = contentWidth / 2.0f
        val y = contentHeight / 2.0f

        // 绘制半径
        val radius = contentWidth / 2.0f - 50

        paint.strokeWidth = strokeWidth

        // 绘制背景
        drawBackground(canvas, x, y, radius)

        // 绘制圆环百分比
        drawPercent(x, radius, y, canvas)

        // 绘制中间显示文字
        drawText(canvas, contentWidth, contentHeight)
    }

    /**
     * 绘制文字
     */
    private fun drawText(canvas: Canvas, contentWidth: Int, contentHeight: Int) {
        val chargeText = String.format("%d%%", charge)
        textWidth = textPaint.measureText(demoText)
        textHeight = textPaint.fontMetrics.bottom
        canvas.drawText(
            chargeText,
            paddingLeft + (contentWidth - textWidth) / 2,
            paddingTop + (contentHeight / 2 + textHeight),
            textPaint
        )
    }

    /**
     * 绘制百分比
     */
    private fun drawPercent(x: Float, radius: Float, y: Float, canvas: Canvas) {
        oval.left = x - radius
        oval.top = y - radius
        oval.right = x + radius
        oval.bottom = y + radius
        if (charge > 20) {
            paint.color = cycleColor
        } else {
            paint.color = lowChargeColor
        }
        canvas.drawArc(oval, 270f, -360 * charge / 100.0f, false, paint)
    }

    /**
     * 绘制背景
     */
    private fun drawBackground(canvas: Canvas, x: Float, y: Float, radius: Float) {
        paint.color = cycleBackgroundColor
        canvas.drawCircle(x, y, radius, paint)
    }

}