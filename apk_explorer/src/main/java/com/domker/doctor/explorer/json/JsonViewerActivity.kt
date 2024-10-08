package com.domker.doctor.explorer.json

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.domker.doctor.base.file.AppFileUtils
import com.domker.doctor.explorer.databinding.ActivityJsonViewerBinding
import java.io.File


@Route(path = "/json_viewer/activity")
class JsonViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJsonViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJsonViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initScale()

        initJsonFile()
    }

    private fun initScale() {
        binding.jsonRecyclerView.setScaleEnable(true)
        binding.jsonRecyclerView.addOnItemTouchListener(object : OnItemTouchListener {
            // 避免双指缩放与上下左右滑动冲突
            override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
                when (event.action and event.actionMasked) {
                    MotionEvent.ACTION_POINTER_UP -> binding.horizontalScrollView.requestDisallowInterceptTouchEvent(false)
                    MotionEvent.ACTION_POINTER_DOWN -> binding.horizontalScrollView.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

    }

    private fun initJsonFile() {
        val jsonFilePath = intent.getStringExtra("json_file_path")
        jsonFilePath?.apply {
            // 显示文件名到标题
            title = File(jsonFilePath).name
            val content = AppFileUtils.readFile(jsonFilePath)
            try {
                binding.jsonRecyclerView.bindJson(content)
            } catch (e: IllegalArgumentException) {
                Toast.makeText(applicationContext, "json文件格式不正确，不能解析", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}