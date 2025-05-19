package com.example.individualproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MessagesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        recyclerView = findViewById(R.id.recyclerMessages)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // API'den grup sohbetlerini çek
        RetrofitClient.api.getGroupChats().enqueue(object : Callback<List<GroupChat>> {
            override fun onResponse(call: Call<List<GroupChat>>, response: Response<List<GroupChat>>) {
                if (response.isSuccessful) {
                    val groupChats = response.body() ?: emptyList()

                    val messages = mutableListOf<MessageListItem>()

                    // Grup sohbetlerini ekle
                    groupChats.forEach {
                        messages.add(
                            MessageListItem(
                                title = "${it.research_title} - Grup",
                                isGroup = true,
                                researchId = it.research_id,
                                receiverEmail = null,
                                groupChatId = it.group_chat_id // burası önemli!

                            )
                        )
                    }

                    messages.add(
                        MessageListItem("bob@example.com", false, null, "bob@example.com", null)
                    )
                    messages.add(
                        MessageListItem("charlie@example.com", false, null, "charlie@example.com", null)
                    )

                    adapter = MessagesAdapter(messages) { selected ->
                        if (selected.isGroup) {
                            val intent = Intent(this@MessagesActivity, GroupChatActivity::class.java)
                            intent.putExtra("group_chat_id", selected.groupChatId)
                            intent.putExtra("research_title", selected.title)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@MessagesActivity, PrivateChatActivity::class.java)
                            intent.putExtra("receiver_email", selected.receiverEmail)
                            startActivity(intent)
                        }
                    }
                    recyclerView.adapter = adapter
                } else {
                    // Hata durumunda kullanıcıya mesaj verebilirsin
                }
            }

            override fun onFailure(call: Call<List<GroupChat>>, t: Throwable) {
                // Hata durumunda log ve kullanıcı bilgilendirme
            }
        })
    }
}

