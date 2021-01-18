package com.domker.app.doctor

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.databinding.SplashLayoutBinding
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.util.Router
import com.domker.app.doctor.util.log

/**
 * Created by wanlipeng on 2018/3/5.
 */
class SplashActivity : AppCompatActivity() {
    private val ACTION_ANIMATION_BEGIN = 0
    private val ACTION_ANIMATION_END = 1

    private lateinit var mHandler: Handler
    private lateinit var handlerThread: HandlerThread
    private lateinit var binding: SplashLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handlerThread = HandlerThread("initThread")
        handlerThread.start()
        mHandler = Handler(handlerThread.looper, Handler.Callback { msg ->
            when (msg.what) {
                ACTION_ANIMATION_BEGIN -> {
                    log("SplashActivityTag", "before updateAppList")
                    val checker = AppCheckFactory.getInstance(applicationContext)
                    if (checker.updateAppListFromDatabase().isEmpty()) {
                        checker.updateAppListFromSystem()
                    }
                    log("SplashActivityTag", "after updateAppList")
                    mHandler.removeMessages(ACTION_ANIMATION_END)
                    mHandler.sendEmptyMessage(ACTION_ANIMATION_END)
                    true
                }
                ACTION_ANIMATION_END -> {
                    runOnUiThread {
                        binding.animationView.cancelAnimation()
                    }
                    openMainActivity()
                    true
                }
                else -> {
                    log("SplashActivityTag", "when else")
                    false
                }
            }
        })
        binding.animationView.speed = 1f
        log("SplashActivityTag", "onCreate...")
    }

    override fun onResume() {
        super.onResume()
        log("SplashActivityTag", "onAnimationStart sendEmptyMessageDelayed")
        binding.animationView.playAnimation()
        mHandler.sendEmptyMessage(ACTION_ANIMATION_BEGIN)
        mHandler.sendEmptyMessageDelayed(ACTION_ANIMATION_END, 3000)
    }

    private fun openMainActivity() {
        ARouter.getInstance()
                .build(Router.MAIN_ACTIVITY)
                .navigation()
        finish()
        log("SplashActivityTag", "openMainActivity")
    }
}