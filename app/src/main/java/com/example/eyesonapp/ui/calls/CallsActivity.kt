package com.example.eyesonapp.ui.calls

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.eyesonapp.R
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

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isMicrophoneEnable.observe(this) { isMicrophoneEnable ->
            binding.muteMicrophoneButton.setOnClickListener {
                if (isMicrophoneEnable == true) {
                    binding.muteMicrophoneButton.setImageResource(R.drawable.ic_microphone_off)
                    viewModel.muteMicrophone()
                } else {
                    binding.muteMicrophoneButton.setImageResource(R.drawable.ic_microphone_on)
                    viewModel.unMuteMicrophone()
                }
            }
        }

        viewModel.isCameraEnable.observe(this) { isCameraEnable ->
            binding.cameraButton.setOnClickListener {
                if (isCameraEnable == true) {
                    binding.cameraButton.setImageResource(R.drawable.ic_video_camera_off)
                    viewModel.turnOffCamera()
                } else {
                    binding.cameraButton.setImageResource(R.drawable.ic_video_camera_on)
                    viewModel.turnOnCamera()
                }
            }
        }
    }
}