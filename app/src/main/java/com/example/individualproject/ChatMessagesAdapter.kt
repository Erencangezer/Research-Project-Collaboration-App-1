package com.example.individualproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatMessagesAdapter(
    private val context: Context,
    private val messages: List<MessageItem>,
    private val senderEmail: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class SentMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtMessage: TextView = view.findViewById(R.id.txtSentMessage)
    }

    inner class ReceivedMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtMessage: TextView = view.findViewById(R.id.txtReceivedMessage)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender_email == senderEmail) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_sent_message, parent, false)
            SentMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_received_message, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentMessageViewHolder) {
            holder.txtMessage.text = message.content
        } else if (holder is ReceivedMessageViewHolder) {
            holder.txtMessage.text = message.content
        }
    }
}
