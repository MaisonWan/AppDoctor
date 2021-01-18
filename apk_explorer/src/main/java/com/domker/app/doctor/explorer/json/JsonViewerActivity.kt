package com.domker.app.doctor.explorer.json

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.domker.base.file.FileUtils
import com.domker.app.doctor.explorer.databinding.ActivityJsonViewerBinding


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
            val content = FileUtils.readFile(jsonFilePath)
            binding.jsonRecyclerView.bindJson(content)
        }
    }
}