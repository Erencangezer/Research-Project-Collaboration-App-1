package com.example.individualproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MessagesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        recyclerView = findViewById(R.id.recyclerMessages)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Bu listeyi sunucudan da alabilirsin
        val messages = listOf(
            MessageListItem("Yapay Zeka Projesi", true, researchId = 1, receiverEmail = null),
            MessageListItem("Sağlık Teknolojileri", true, researchId = 2, receiverEmail = null),
            MessageListItem("bob@example.com", false, researchId = null, receiverEmail = "bob@example.com"),
            MessageListItem("charlie@example.com", false, researchId = null, receiverEmail = "charlie@example.com")
        )

        adapter = MessagesAdapter(messages) { selected ->
            if (selected.isGroup) {
                val intent = Intent(this, GroupChatActivity::class.java)
                intent.putExtra("research_id", selected.researchId)
                intent.putExtra("research_title", selected.title)
                startActivity(intent)
            } else {
                val intent = Intent(this, PrivateChatActivity::class.java)
                intent.putExtra("receiver_email", selected.receiverEmail)
                startActivity(intent)
            }
        }

        recyclerView.adapter = adapter
    }
}
