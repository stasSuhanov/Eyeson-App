package com.example.eyesonapp.ui.calls.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eyesonapp.databinding.ItemMessageBinding
import com.example.eyesonapp.databinding.ItemMessageIncomingBinding
import com.example.eyesonapp.databinding.ItemMessageOutgoingBinding

const val TYPE_INCOMING_MESSAGE = 0
const val TYPE_OUTGOING_MESSAGE = 1

class MessageAdapter : RecyclerView.Adapter<MessageViewHolder>() {

    private val data by lazy { mutableListOf<Message>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == TYPE_INCOMING_MESSAGE) {
            val binding = ItemMessageIncomingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
            MessageIncomingViewHolder(binding)
        } else {
            val binding = ItemMessageOutgoingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
            MessageOutgoingViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data.getOrNull(itemCount - position - 1)?.isMine == true) {
            TYPE_OUTGOING_MESSAGE
        } else {
            TYPE_INCOMING_MESSAGE
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        data.getOrNull(itemCount - position - 1)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showItems(items: List<Message>) {
        data.addAll(items)
        notifyDataSetChanged()
    }
}

open class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val messageBinding: ItemMessageBinding

    init {
        messageBinding = ItemMessageBinding.bind(itemView)
    }

    fun bind(message: Message) {
        messageBinding.message.text = message.message
        messageBinding.username.text = message.userName
        messageBinding.date.text = message.formattedDate
    }
}

class MessageIncomingViewHolder(binding: ItemMessageIncomingBinding) : MessageViewHolder(binding.root)

class MessageOutgoingViewHolder(binding: ItemMessageOutgoingBinding) : MessageViewHolder(binding.root)
