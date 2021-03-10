package ru.anisimov_d.da_androidcourse_2021

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : BaseActivity() {
    companion object {
        const val BUNDLE_COUNTER_KEY = "COUNTER"
    }

    private lateinit var counterView: TextView
    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        counterView = findViewById(R.id.main_tv_counter)
        findViewById<Button>(R.id.main_bt_openSquared)
            .setOnClickListener {
                Intent(this, SquaredActivity::class.java)
                    .apply { putExtra(SquaredActivity.INTENT_EXTRA_COUNTER_KEY, counter) }
                    .let { startActivity(it) }
            }
    }

    override fun onResume() {
        super.onResume()
        counterView.text = getString(R.string.main_tv_counterText, counter)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BUNDLE_COUNTER_KEY, ++counter)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        counter = savedInstanceState.getInt(BUNDLE_COUNTER_KEY)
    }
}
