package com.stopkaaaa.swtec_practice.threadpool

import java.util.concurrent.BlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

class PausableExecutor(
    val _corePoolSize: Int,
    val _maximumPoolSize: Int,
    val keepAliveTime: Long,
    val timeUnit: TimeUnit,
    val workQueue: BlockingQueue<Runnable>
)
    : ThreadPoolExecutor(_corePoolSize, _maximumPoolSize, keepAliveTime, timeUnit, workQueue) {

    private var isPaused = false
    private val pauseLock: ReentrantLock = ReentrantLock()
    private val unpaused: Condition = pauseLock.newCondition()

    override fun beforeExecute(t: Thread?, r: Runnable?) {
        super.beforeExecute(t, r)
        pauseLock.lock()
        try {
            while (isPaused) unpaused.await()
        } catch (ie: InterruptedException) {
            t!!.interrupt()
        } finally {
            pauseLock.unlock()
        }
    }

    fun pause() {
        pauseLock.lock()
        isPaused = try {
            true
        } finally {
            pauseLock.unlock()
        }
    }

    fun resume() {
        pauseLock.lock()
        try {
            isPaused = false
            unpaused.signalAll()
        } finally {
            pauseLock.unlock()
        }
    }
}

