package ru.anisimov_d.da_androidcourse_2021

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class LifecycleLoggerObserver(private val tag: String) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() = Log.i(tag, "onCreate called")

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() = Log.i(tag, "onStart called")

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() = Log.i(tag, "onResume called")

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() = Log.i(tag, "onPause called")

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() = Log.i(tag, "onStop called")

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() = Log.i(tag, "onDestroy called")
}
