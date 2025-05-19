package com.example.individualproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var editMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var txtGroupTitle: TextView
    private val senderEmail = "alice@example.com" // oturum açan kullanıcıdan alınmalı

    private var groupChatId: Int = -1
    private var groupTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)

        recyclerView = findViewById(R.id.recyclerMessages)
        txtGroupTitle = findViewById(R.id.txtGroupTitle)
        editMessage = findViewById(R.id.editMessage)
        btnSend = findViewById(R.id.btnSend)

        adapter = ChatAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        groupChatId = intent.getIntExtra("group_chat_id", -1)
        groupTitle = intent.getStringExtra("research_title")
        txtGroupTitle.text = "Grup: $groupTitle"

        if (groupChatId == -1) {
            Toast.makeText(this, "Grup sohbeti bulunamadı", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        getGroupMessages()

        btnSend.setOnClickListener {
            val content = editMessage.text.toString().trim()
            if (content.isNotEmpty()) {
                sendGroupMessage(content)
            }
        }
    }

    private fun getGroupMessages() {
        RetrofitClient.api.getGroupMessages(groupChatId).enqueue(object : Callback<MessageListResponse> {
            override fun onResponse(call: Call<MessageListResponse>, response: Response<MessageListResponse>) {
                if (response.isSuccessful) {
                    val messageList = response.body()?.messages ?: emptyList()
                    adapter.setMessages(messageList)
                }
            }

            override fun onFailure(call: Call<MessageListResponse>, t: Throwable) {
                Toast.makeText(this@GroupChatActivity, "Hata: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendGroupMessage(content: String) {
        val request = MessageRequest(groupChatId, senderEmail, null, content)
        RetrofitClient.api.sendMessage(request).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    editMessage.text.clear()
                    getGroupMessages()
                } else {
                    Toast.makeText(this@GroupChatActivity, "Mesaj gönderilemedi", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Toast.makeText(this@GroupChatActivity, "Hata: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
