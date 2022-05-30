package com.example.eyesonapp.ui.calls

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.eyesonapp.ARG_ROOM_ACCESS_KEY
import com.example.eyesonapp.R
import com.example.eyesonapp.databinding.ActivityCallsBinding
import com.example.eyesonapp.ui.calls.chat.ChatFragment
import dagger.hilt.android.AndroidEntryPoint
import org.webrtc.RendererCommon
import org.webrtc.SurfaceViewRenderer

@AndroidEntryPoint
class CallsActivity : AppCompatActivity() {

    private val viewModel by viewModels<CallsViewModel>()
    private lateinit var binding: ActivityCallsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestCallPermissions()
        connectToRoom()
        setClickListeners()
        observeViewModel()
        attachChatFragment()
    }

    override fun onBackPressed() {
        if (getChatFragmentIfAttached()?.isVisible == true) {
            showChat(false)
        } else {
            super.onBackPressed()
            disconnect()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearTargets()
    }

    private fun setClickListeners() {
        binding.endCallButton.setOnClickListener { disconnect() }
        binding.openChatButton.setOnClickListener { showChat(true) }
    }

    private fun attachChatFragment() {
        if (getChatFragmentIfAttached() != null) return
        supportFragmentManager.beginTransaction()
            .replace(R.id.chat_fragment_container, ChatFragment(), ChatFragment::class.simpleName)
            .commitNow()
        showChat(false)
    }

    private fun showChat(show: Boolean) {
        getChatFragmentIfAttached()?.let {
            supportFragmentManager.beginTransaction()
                .apply { if (show) show(it) else hide(it) }
                .commit()
        }
    }

    private fun getChatFragmentIfAttached(): ChatFragment? {
        return supportFragmentManager.findFragmentByTag(ChatFragment::class.simpleName) as? ChatFragment
    }

    private fun requestCallPermissions() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 7)
    }

    private fun connectToRoom() {
        clearTargets()
        val roomAccessKey = intent.extras?.getString(ARG_ROOM_ACCESS_KEY, "")
        val guestToken = intent.data.toString().split("=").last()
        viewModel.connectToRoom(roomAccessKey, guestToken, binding.localVideo, binding.remoteVideo)
    }

    private fun observeViewModel() {
        viewModel.meetingStateLiveData.observe(this) { state ->
            state ?: return@observe
            when (state) {
                MeetingState.CREATED -> {
                    binding.joiningCallGroup.visibility = View.VISIBLE
                    binding.callGroup.visibility = View.INVISIBLE
                }
                MeetingState.JOINED -> {
                    binding.joiningCallGroup.visibility = View.INVISIBLE
                    binding.callGroup.visibility = View.VISIBLE
                    bindVideoViews()
                    viewModel.setTargets(binding.localVideo, binding.remoteVideo)
                }
                is MeetingState.TERMINATED -> {
                    viewModel.onMeetingTerminated(state.reason)
                }
            }
        }

        viewModel.isMicrophoneEnable.observe(this) { isMicrophoneEnable ->
            binding.muteMicrophoneButton.setOnClickListener {
                binding.muteMicrophoneButton.setImageResource(
                    if (isMicrophoneEnable) R.drawable.ic_microphone_off else R.drawable.ic_microphone_on
                )
                viewModel.muteMicrophone(!isMicrophoneEnable)
            }
        }

        viewModel.isCameraEnable.observe(this) { isCameraEnable ->
            binding.cameraButton.setOnClickListener {
                binding.cameraButton.setImageResource(
                    if (isCameraEnable) R.drawable.ic_video_camera_off else R.drawable.ic_video_camera_on
                )
                viewModel.muteCamera(!isCameraEnable)
            }
        }
    }

    private fun bindVideoViews() {
        listOf(binding.localVideo, binding.remoteVideo).forEach { bindViewView(it) }
    }

    private fun bindViewView(surfaceViewRenderer: SurfaceViewRenderer) {
        surfaceViewRenderer.init(viewModel.getEglContext(), null)
        surfaceViewRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_BALANCED)
        surfaceViewRenderer.setEnableHardwareScaler(true)
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
}