package com.example.eyesonapp.ui.home

import android.content.Intent
import android.os.Bundle
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

        binding.createRoomButton.setOnClickListener {
            viewModel.createMeetingRoom()
        }

        binding.roomLink.setOnClickListener {
            sendInvite(binding.roomLink.text.toString())
        }
    }

    private fun sendInvite(link: String) {
        if (link.contains(BASE_URL)) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.room_invite, link))
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    companion object {
        private const val BASE_URL = "app.eyeson.team"
    }
}