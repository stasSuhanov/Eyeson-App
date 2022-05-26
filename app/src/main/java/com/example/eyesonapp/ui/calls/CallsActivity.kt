package com.example.eyesonapp.ui.calls

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.eyesonapp.databinding.ActivityCallsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallsActivity : AppCompatActivity() {

    private val viewModel by viewModels<CallsViewModel>()
    private lateinit var binding: ActivityCallsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.endCallButton.setOnClickListener {
            //TODO
        }

        binding.muteMicrophoneButton.setOnClickListener {
            //TODO
        }

        binding.turnOffCameraButton.setOnClickListener {
            //TODO
        }
    }
}