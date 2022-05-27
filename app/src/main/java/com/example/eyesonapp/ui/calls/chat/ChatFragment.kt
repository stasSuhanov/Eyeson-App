package com.example.eyesonapp.ui.calls.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eyesonapp.databinding.FragmentChatBinding
import com.example.eyesonapp.ui.calls.CallsViewModel

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val viewModel: CallsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.sendMessageButton.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        viewModel.sendMessage(binding.messageInputField.text.toString())
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatFragment()
    }
}