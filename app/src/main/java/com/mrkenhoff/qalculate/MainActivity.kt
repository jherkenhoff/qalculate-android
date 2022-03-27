package com.mrkenhoff.qalculate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mrkenhoff.qalculate.databinding.ActivityMainBinding
import com.mrkenhoff.libqalculate.Calculator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        init {
            System.loadLibrary("libqalculate_swig")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .add(R.id.main_fragment, MainFragment())
            .commit()

        var c = Calculator()
        var s = c.calculateAndPrint("1 + 1 + pi", 2000)
    }
}