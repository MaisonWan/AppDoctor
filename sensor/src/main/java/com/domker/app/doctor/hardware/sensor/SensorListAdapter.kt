package com.domker.app.doctor.hardware.sensor

import android.content.Context
import android.hardware.Sensor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.hardware.R

/**
 * 传感器展示的适配器
 */
class SensorListAdapter(context: Context, private val sensorList: List<Sensor>) : RecyclerView.Adapter<SensorListAdapter.SensorDetailViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorDetailViewHolder {
        val root = inflater.inflate(R.layout.sensor_item, parent, false)
        return SensorDetailViewHolder(root)
    }

    override fun onBindViewHolder(holder: SensorDetailViewHolder, position: Int) {
        val sensor = sensorList[position]
        holder.subject.text = sensor.name
        holder.label.text = parserTypeName(sensor)
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("sensor_name", sensor.name)
            bundle.putInt("sensor_type", sensor.type)
            Navigation.findNavController(it).navigate(R.id.navigation_sensor_detail, bundle)
        }
    }

    override fun getItemCount(): Int = sensorList.size

    /**
     * 根据类型翻转出类型名字
     */
    private fun parserTypeName(sensor: Sensor): String {
        return when (sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> "加速度传感器"
            Sensor.TYPE_MAGNETIC_FIELD -> "地磁传感器"
            Sensor.TYPE_ORIENTATION -> "方向传感器"
            Sensor.TYPE_GYROSCOPE -> "陀螺仪传感器"
            Sensor.TYPE_LIGHT -> "光线传感器"
            Sensor.TYPE_PRESSURE -> "压力传感器"
            Sensor.TYPE_TEMPERATURE, Sensor.TYPE_AMBIENT_TEMPERATURE -> "温度传感器"
            Sensor.TYPE_PROXIMITY -> "近程传感器"
            Sensor.TYPE_GRAVITY -> "重力传感器"
            Sensor.TYPE_LINEAR_ACCELERATION -> "线性加速度传感器"
            Sensor.TYPE_ROTATION_VECTOR -> "旋转矢量传感器"
            Sensor.TYPE_STEP_DETECTOR -> "计步检测器"
            Sensor.TYPE_STEP_COUNTER -> "计步器"
            else -> "${sensor.stringType} ${sensor.type}"
        }
    }

    /**
     * 展示详细内容的ViewHolder
     */
    class SensorDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subject: TextView = itemView.findViewById(R.id.textViewSubject)
        val label: TextView = itemView.findViewById(R.id.textViewFile)
    }
}