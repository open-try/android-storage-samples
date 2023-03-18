package com.opentry.android_storage_samples.safdemos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.opentry.android_storage_samples.databinding.ActivityMainSafDemosBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainSafDemosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }
}
