package com.example.eyesonapp.ui.calls

import android.Manifest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.example.eyesonapp.R
import com.example.eyesonapp.databinding.ActivityCallsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.webrtc.RendererCommon

@AndroidEntryPoint
class CallsActivity : AppCompatActivity() {

    private val viewModel by viewModels<CallsViewModel>()
    private lateinit var binding: ActivityCallsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 7)

//        arguments?.let {
//            accessKey = it.getString(ACCESS_KEY)
//            guestToken = it.getString(GUEST_TOKEN)
//            guestName = it.getString(GUEST_NAME)
//        }
        connectToRoom()

        binding.endCallButton.setOnClickListener {
            disconnect()
        }

        if (viewModel.inCall) {
            bindVideoViews()

            viewModel.setTargets(binding.localVideo, binding.remoteVideo)
        }

//        this.lifecycleScope.launch {
//            this@CallsActivity.repeatOnLifecycle(Lifecycle.State.STARTED) {
//
//                launch {
//                    viewModel.callTerminated.collect {
//                        if (it) {
//                            clearTargets()
//                        }
//                    }
//                }
//            }
//        }

        observeViewModel()
    }

    private fun connectToRoom() {
        viewModel.disconnect()
        clearTargets()
        when {
            !"K5nN3iQEvErOVJohmHo7eLDi".isNullOrBlank() -> {
                viewModel.connect(
                    "K5nN3iQEvErOVJohmHo7eLDi",
                    binding.localVideo,
                    binding.remoteVideo
                )
            }
            !"K5nN3iQEvErOVJohmHo7eLDi".isNullOrBlank() -> {
                viewModel.connectAsGuest(
                    "K5nN3iQEvErOVJohmHo7eLDi",
                    "guestName" ?: "I'm a guest name!",
                    binding.localVideo,
                    binding.remoteVideo
                )
            }
        }
        bindVideoViews()
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
                    viewModel.muteCamera()
                } else {
                    binding.cameraButton.setImageResource(R.drawable.ic_video_camera_on)
                    viewModel.unMuteCamera()
                }
            }
        }
    }

    private fun bindVideoViews() {
        binding.localVideo.init(viewModel.getEglContext(), null)
        binding.localVideo.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_BALANCED)
        binding.localVideo.setEnableHardwareScaler(true)

        binding.remoteVideo.init(viewModel.getEglContext(), null)
        binding.remoteVideo.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_BALANCED)
        binding.remoteVideo.setEnableHardwareScaler(true)
    }

    private fun disconnect() {
        viewModel.disconnect()
        binding.localVideo.release()
        binding.remoteVideo.release()
    }

    private fun clearTargets() {
        binding.localVideo.release()
        binding.remoteVideo.release()

        viewModel.clearTarget()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearTargets()
    }

    companion object {
        private const val ACCESS_KEY = "access_key"
        private const val GUEST_TOKEN = "guest_token"
        private const val GUEST_NAME = "guest_name"

    }
}