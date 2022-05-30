package com.example.eyesonapp.ui.calls.chat

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eyesonapp.databinding.FragmentChatBinding
import com.example.eyesonapp.ui.calls.CallsViewModel

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val viewModel: CallsViewModel by activityViewModels()

    private val adapter: MessageAdapter by lazy { MessageAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        setClickListeners()
        observeViewModel()
    }

    private fun initRecyclerView() {
        binding.messageRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
            adapter = this@ChatFragment.adapter
        }
    }

    private fun setClickListeners() {
        binding.sendMessageButton.setOnClickListener { sendMessage() }
        binding.backButton.setOnClickListener { hide() }
    }

    private fun sendMessage() {
        val text = binding.messageInputField.text.toString()
        if (text.isEmpty()) return
        viewModel.sendMessage(text)
        binding.messageInputField.setText("")
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun observeViewModel() {
        viewModel.chatMessage.observe(viewLifecycleOwner) { message ->
            adapter.showItems(listOf(message))
        }
    }

    private fun hide() {
        activity?.supportFragmentManager?.beginTransaction()?.hide(this)?.commit()
    }
}