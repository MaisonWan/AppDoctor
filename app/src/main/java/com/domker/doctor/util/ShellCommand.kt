package com.domker.doctor.util

import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * 执行命令的类
 */
class ShellCommand {
    //shell进程
    private var process: Process? = null

    //对应进程的3个流
    private var successResult: BufferedReader? = null
    private var errorResult: BufferedReader? = null
    private var os: DataOutputStream? = null

    //是否同步，true：run会一直阻塞至完成或超时。false：run会立刻返回
    private var bSynchronous: Boolean

    /**
     * 还没开始执行，和已经执行完成 这两种情况都返回false
     *
     * @return 是否正在执行
     */
    //表示shell进程是否还在运行
    var isRunning = false
        private set

    //同步锁
    var lock: ReadWriteLock = ReentrantReadWriteLock()

    //保存执行结果
    private val result = mutableListOf<String>()

    /**
     * 构造函数
     *
     * @param synchronous true：同步，false：异步
     */
    constructor(synchronous: Boolean) {
        bSynchronous = synchronous
    }

    /**
     * 默认构造函数，默认是同步执行
     */
    constructor() {
        bSynchronous = true
    }

    /**
     * @return 返回执行结果
     */
    fun getResult(): List<String> {
        val readLock = lock.readLock()
        readLock.lock()
        return try {
            Log.i("auto", "getResult")
            result
        } finally {
            readLock.unlock()
        }
    }

    /**
     * 执行命令
     *
     * @param command eg: cat /sdcard/test.txt
     * 路径最好不要是自己拼写的路径，最好是通过方法获取的路径
     * example：Environment.getExternalStorageDirectory()
     * @param maxTime 最大等待时间 (ms)
     * @return this
     */
    fun run(command: String?, maxTime: Int): ShellCommand {
        Log.i("auto", "run command:$command,maxtime:$maxTime")
        if (command == null || command.isEmpty()) {
            return this
        }
        process = try {
            Runtime.getRuntime().exec("sh") //看情况可能是su
        } catch (e: Exception) {
            return this
        }
        isRunning = true
        successResult = BufferedReader(InputStreamReader(process!!.inputStream))
        errorResult = BufferedReader(InputStreamReader(process!!.errorStream))
        os = DataOutputStream(process!!.outputStream)
        try {
            //向sh写入要执行的命令
            os!!.write(command.toByteArray())
            os!!.writeBytes("\n")
            os!!.flush()
            os!!.writeBytes("exit\n")
            os!!.flush()
            os!!.close()
            //如果等待时间设置为非正，就不开启超时关闭功能
            if (maxTime > 0) {
                //超时就关闭进程
                Thread {
                    try {
                        Thread.sleep(maxTime.toLong())
                    } catch (e: Exception) {
                    }
                    try {
                        val ret = process!!.exitValue()
                        Log.i("auto", "exitValue Stream over$ret")
                    } catch (e: IllegalThreadStateException) {
                        Log.i("auto", "take maxTime,forced to destroy process")
                        process!!.destroy()
                    }
                }.start()
            }

            //开一个线程来处理input流
            val t1 = Thread {
                var line: String?
                val writeLock = lock.writeLock()
                try {
                    while (successResult!!.readLine().also { line = it } != null) {
//                        line += "\n"
                        writeLock.lock()
                        result.add(line!!)
                        writeLock.unlock()
                    }
                } catch (e: Exception) {
                    Log.i("auto", "read InputStream exception:$e")
                } finally {
                    try {
                        successResult!!.close()
                        Log.i("auto", "read InputStream over")
                    } catch (e: Exception) {
                        Log.i("auto", "close InputStream exception:$e")
                    }
                }
            }
            t1.start()

            //开一个线程来处理error流
            val t2 = Thread {
                var line: String?
                val writeLock = lock.writeLock()
                try {
                    while (errorResult!!.readLine().also { line = it } != null) {
//                        line += "\n"
                        writeLock.lock()
                        result.add(line!!)
                        writeLock.unlock()
                    }
                } catch (e: Exception) {
                    Log.i("auto", "read ErrorStream exception:$e")
                } finally {
                    try {
                        errorResult!!.close()
                        Log.i("auto", "read ErrorStream over")
                    } catch (e: Exception) {
                        Log.i("auto", "read ErrorStream exception:$e")
                    }
                }
            }
            t2.start()
            val t3 = Thread {
                try {
                    //等待执行完毕
                    t1.join()
                    t2.join()
                    process!!.waitFor()
                } catch (e: Exception) {
                } finally {
                    isRunning = false
                    Log.i("auto", "run command process end")
                }
            }
            t3.start()
            if (bSynchronous) {
                Log.i("auto", "run is go to end")
                t3.join()
                Log.i("auto", "run is end")
            }
        } catch (e: Exception) {
            Log.i("auto", "run command process exception:$e")
        }
        return this
    }
}