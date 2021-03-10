package ru.anisimov_d.da_androidcourse_2021

import android.os.Bundle
import android.widget.TextView

class SquaredActivity : BaseActivity() {
    companion object {
        const val INTENT_EXTRA_COUNTER_KEY = "COUNTER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_squared)

        val counter = intent.getIntExtra(INTENT_EXTRA_COUNTER_KEY, 0)

        findViewById<TextView>(R.id.squared_tv_counter).apply {
            text = getString(R.string.squared_tv_counterText, counter * counter)
        }
    }
}
