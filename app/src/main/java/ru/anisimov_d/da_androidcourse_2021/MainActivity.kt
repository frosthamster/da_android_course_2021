package ru.anisimov_d.da_androidcourse_2021

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    companion object {
        const val COUNTER_KEY = "COUNTER"
        const val SHOW_BUTTON_KEY = "SHOW_BUTTON"
    }

    init {
        lifecycle.addObserver(LifecycleLoggerObserver(this.javaClass.name))
    }

    private lateinit var counterView: TextView
    private var counter: Int = 0
    private var showButton: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        counter = intent.getIntExtra(COUNTER_KEY, 0)
        showButton = intent.getBooleanExtra(SHOW_BUTTON_KEY, true)

        counterView = findViewById(R.id.counter)
        findViewById<Button>(R.id.openSquaredActivityButton)
            .apply { visibility = if (showButton) View.VISIBLE else View.INVISIBLE }
            .setOnClickListener {
                Intent(this, this.javaClass)
                    .apply {
                        putExtra(COUNTER_KEY, counter * counter)
                        putExtra(SHOW_BUTTON_KEY, false)
                    }
                    .let { startActivity(it) }
            }
    }

    override fun onResume() {
        super.onResume()
        counterView.text = counter.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (showButton)
            counter++
        outState.putInt(COUNTER_KEY, counter)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        counter = savedInstanceState.getInt(COUNTER_KEY)
    }
}
