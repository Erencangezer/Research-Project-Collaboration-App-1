package com.example.individualproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response





class PrivateChatActivity : AppCompatActivity() {

    private lateinit var messageAdapter: ChatMessagesAdapter
    private lateinit var messageList: MutableList<MessageItem>
    private lateinit var recyclerView: RecyclerView
    private lateinit var editMessage: EditText
    private lateinit var btnSend: Button

    private var researchId: Int = -1
    private lateinit var senderEmail: String
    private lateinit var receiverEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_chat)

        // Parametreleri al (Intent'ten)
        researchId = intent.getIntExtra("research_id", -1)
        senderEmail = intent.getStringExtra("sender_email") ?: ""
        receiverEmail = intent.getStringExtra("receiver_email") ?: ""

        supportActionBar?.title = receiverEmail // üstte hedef kullanıcıyı göster

        recyclerView = findViewById(R.id.recyclerView)
        editMessage = findViewById(R.id.editMessage)
        btnSend = findViewById(R.id.btnSend)

        messageList = mutableListOf()
        messageAdapter = ChatMessagesAdapter(this, messageList, senderEmail)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter

        btnSend.setOnClickListener {
            val content = editMessage.text.toString().trim()
            if (content.isNotEmpty()) {
                sendPrivateMessage(content)
            }
        }

        // Mesajları yükle
        getPrivateMessages()
    }

    private fun sendPrivateMessage(content: String) {
        val request = MessageRequest(
            research_id = researchId,
            sender_email = senderEmail,
            receiver_email = receiverEmail,
            content = content
        )

        RetrofitClient.api.sendMessage(request).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    editMessage.text.clear()
                    getPrivateMessages()
                } else {
                    Toast.makeText(this@PrivateChatActivity, "Mesaj gönderilemedi", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Toast.makeText(this@PrivateChatActivity, "Hata: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getPrivateMessages() {
        RetrofitClient.api.getPrivateMessages(researchId, senderEmail, receiverEmail)
            .enqueue(object : Callback<List<MessageItem>> {
                override fun onResponse(call: Call<List<MessageItem>>, response: Response<List<MessageItem>>) {
                    if (response.isSuccessful) {
                        messageList.clear()
                        messageList.addAll(response.body() ?: emptyList())
                        messageAdapter.notifyDataSetChanged()
                        recyclerView.scrollToPosition(messageList.size - 1)
                    }
                }

                override fun onFailure(call: Call<List<MessageItem>>, t: Throwable) {
                    Toast.makeText(this@PrivateChatActivity, "Mesajlar alınamadı: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
