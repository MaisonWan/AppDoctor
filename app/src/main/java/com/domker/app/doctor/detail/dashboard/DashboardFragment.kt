package com.domker.app.doctor.detail.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.domker.app.doctor.R
import com.domker.app.doctor.detail.AppDetailActivity
import com.domker.app.doctor.view.TypeFacePool
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import java.util.*
import kotlin.collections.HashMap

/**
 * app详情里面的统计
 */
class DashboardFragment : Fragment() {
    private val tabs = arrayOf("Activity", "Service", "Provider", "Receiver", "Permission")
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var chart: RadarChart
    private val data = HashMap<String, Int>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("DashboardFragment onActivityCreated")
        // 设置观察者模式，获取数量
        initObserve()
    }

    private fun initObserve() {
        AppDetailActivity.componentViewModel?.let { vm ->
            println("initObserve")
            val info = arrayOf(vm.activityInfo, vm.serviceInfo, vm.providerInfo, vm.receiverInfo,
                    vm.permissionInfo)
            // 观察者
            tabs.forEachIndexed { index, name ->
                info[index].observe(viewLifecycleOwner, {
                    println("$name = ${it.size}")
                    data[name] = it.size

                    // 获取够了5种类型的数据，则刷新图标
                    if (data.size == tabs.size) {
                        setData()
                    }
                })
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        return inflater.inflate(R.layout.fragment_detail_bashboard, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChart(view)
    }

    private fun initChart(view: View) {
        chart = view.findViewById(R.id.radarChartComponent)

        chart.description.isEnabled = false
        chart.webLineWidth = 1f
        chart.webColor = Color.LTGRAY
        chart.webLineWidthInner = 1f
        chart.webColorInner = Color.LTGRAY
        chart.webAlpha = 100

        val mv: MarkerView = RadarMarkerView(requireContext(), R.layout.radar_markerview)
        mv.chartView = chart // For bounds control
        chart.marker = mv // Set the marker to the chart

        chart.animateXY(1400, 1400, Easing.EaseInOutQuad)

        val xAxis: XAxis = chart.xAxis
        xAxis.typeface = TypeFacePool.openSansLight
        xAxis.textSize = 9f
        xAxis.yOffset = 0f
        xAxis.xOffset = 0f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return tabs[value.toInt() % tabs.size]
            }
        }
        xAxis.textColor = Color.BLACK

        val l: Legend = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(true)
        l.typeface = TypeFacePool.openSansLight
        l.xEntrySpace = 7f
        l.yEntrySpace = 5f
        l.textColor = Color.BLACK
    }

    private fun setData() {
        val list = ArrayList<RadarEntry>()
        tabs.forEach {
            list.add(RadarEntry(data[it]!!.toFloat()))
        }
        val yAxis: YAxis = chart.yAxis
        yAxis.typeface = TypeFacePool.openSansLight
        yAxis.setLabelCount(5, false)
        yAxis.textSize = 9f
        yAxis.axisMinimum = 0f
        yAxis.resetAxisMaximum()
        yAxis.axisMaximum = list.maxOfOrNull { it.value } ?: 100f
        yAxis.setDrawLabels(false)


        val dataSet = RadarDataSet(list, "AppComponent")
        dataSet.color = Color.rgb(121, 162, 175)
        dataSet.fillColor = Color.rgb(121, 162, 175)
        dataSet.setDrawFilled(false)
        dataSet.fillAlpha = 180
        dataSet.lineWidth = 2f
        dataSet.isDrawHighlightCircleEnabled = false
//        dataSet.setDrawValues(true)
        dataSet.setDrawHighlightIndicators(false)

        val sets = ArrayList<IRadarDataSet>()
        sets.add(dataSet)

        val radarData = RadarData(sets)
        radarData.setValueTypeface(TypeFacePool.openSansLight)
        radarData.setValueTextSize(8f)
        radarData.setDrawValues(true)
        radarData.setValueTextColor(Color.BLACK)
        chart.data = radarData
        chart.invalidate()
    }
}
