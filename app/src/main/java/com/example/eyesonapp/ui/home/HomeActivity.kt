package com.example.eyesonapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.eyesonapp.R
import com.example.eyesonapp.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClickListeners()
        observeUiState()
    }

    override fun onBackPressed() {
        if (viewModel.state.value != State.Initial) {
            viewModel.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    private fun setClickListeners() {
        binding.roomLink.setOnClickListener { viewModel.sendInviteLink() }
    }

    private fun observeUiState() {
        viewModel.state.observe(this) { applyUiState(it) }
    }

    private fun applyUiState(state: State) {
        when (state) {
            is State.Initial -> {
                binding.initialGroup.visibility = View.VISIBLE
                binding.progressGroup.visibility = View.INVISIBLE
                binding.roomCreatedGroup.visibility = View.INVISIBLE

                binding.nextButton.text = getString(R.string.home_screen_create_room)
                binding.nextButton.setOnClickListener { viewModel.createMeetingRoom() }
            }
            is State.Progress -> {
                binding.initialGroup.visibility = View.INVISIBLE
                binding.progressGroup.visibility = View.VISIBLE
                binding.roomCreatedGroup.visibility = View.INVISIBLE

            }
            is State.RoomCreated -> {
                binding.initialGroup.visibility = View.INVISIBLE
                binding.progressGroup.visibility = View.INVISIBLE
                binding.roomCreatedGroup.visibility = View.VISIBLE

                binding.nextButton.text = getString(R.string.home_screen_join_room)
                binding.roomLink.text = state.guestJoinLink
                binding.nextButton.setOnClickListener { viewModel.joinMeetingRoomAsHost() }
            }
        }
    }
}