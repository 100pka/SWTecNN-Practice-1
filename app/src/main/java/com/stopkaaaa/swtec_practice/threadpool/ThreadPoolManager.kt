package com.stopkaaaa.swtec_practice.threadpool

import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast
import java.lang.ref.WeakReference
import java.util.concurrent.*


object ThreadPoolManager {

    private const val DEFAULT_THREAD_POOL_SIZE = 4
    private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()
    private const val KEEP_ALIVE_TIME = 1L
    private val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS

    lateinit var  executorService: PausableExecutor
    private val taskQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
    private val runningTaskList: MutableList<Future<Unit>> = mutableListOf()

    private var callback: WeakReference<UiThreadCallback>? = null

    var currentWeatherCash: CurrentWeatherForecast? = null
    val dailyWeatherCash = mutableListOf<DailyForecast>()

    init {
        executorService = PausableExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES*2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, taskQueue)
    }

    fun addCallable(callable: Callable<Unit>){
        val future = executorService.submit(callable)
        runningTaskList.add(future)
    }

    fun cancelAllTasks() {
        synchronized (this) {
            taskQueue.clear()
            for (task in runningTaskList) {
            if (!task.isDone) {
                task.cancel(true)
            }
        }
            runningTaskList.clear()
        }
    }

    fun setBindResultCallback(callback: UiThreadCallback){
        this.callback = WeakReference<UiThreadCallback>(callback)
    }

    fun postCurrentWeatherToUi(currentWeather: CurrentWeatherForecast){
        if(callback != null && callback?.get() != null) {
            currentWeatherCash = currentWeather
            callback?.get()?.bindCurrentWeatherToUi(currentWeather)
        }
    }

    fun postDailyWeatherToUi(dailyWeather: List<DailyForecast>){
        if(callback != null && callback?.get() != null) {
            dailyWeatherCash.clear()
            dailyWeatherCash.addAll(dailyWeather)
            callback?.get()?.bindDailyWeatherToUi(dailyWeather)
        }
    }

    fun activityReCreated() {
        if(callback != null && callback?.get() != null) {
            if (dailyWeatherCash.isNotEmpty()) {
                callback?.get()?.bindDailyWeatherToUi(dailyWeatherCash)
            }
            if (currentWeatherCash != null) {
                callback?.get()?.bindCurrentWeatherToUi(currentWeatherCash!!)
            }
        }
    }
}
