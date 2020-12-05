package com.country.information.uiscreens

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.country.information.R
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.apply {
            title = getString(R.string.deafult_toolbar_text)
        }
        initializeViews()
    }

    private fun initializeViews() {
        val button = findViewById<Button>(R.id.button)
        val frameLayout = findViewById<FrameLayout>(R.id.mframeLayout)
        button.apply {
            setOnClickListener {
                it.visibility = View.GONE
                frameLayout.visibility = View.VISIBLE
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mframeLayout, CountryListFragment()).commit()
            }
        }
    }
}


