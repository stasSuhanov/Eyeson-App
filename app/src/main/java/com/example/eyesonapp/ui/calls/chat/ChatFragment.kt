package com.example.eyesonapp.ui.calls.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.eyesonapp.databinding.FragmentChatBinding
import com.example.eyesonapp.ui.calls.CallsViewModel

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val viewModel: CallsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
        observeViewModel()
    }

    private fun setClickListeners() {
        binding.sendMessageButton.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        viewModel.sendMessage(binding.messageInputField.text.toString())
    }

    private fun observeViewModel() {
        viewModel.chatMessage.observe(viewLifecycleOwner) { message ->
            displayMessage(message.userName, message.message)
        }
    }

    private fun displayMessage(userName: String, message: String) {
        binding.chat.append("\n")
        binding.chat.append("$userName: $message")
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatFragment()
    }
}