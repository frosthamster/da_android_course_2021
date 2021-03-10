package ru.anisimov_d.da_androidcourse_2021

import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    init {
        lifecycle.addObserver(LifecycleLoggerObserver(this.javaClass.name))
    }
}
