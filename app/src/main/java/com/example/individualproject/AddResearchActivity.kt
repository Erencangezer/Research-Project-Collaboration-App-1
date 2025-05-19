package com.example.individualproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddResearchActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSave: Button
    private lateinit var etParticipantEmails: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_research)

        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        btnSave = findViewById(R.id.btnSaveResearch)
        etParticipantEmails = findViewById(R.id.etParticipantEmails)

        val userEmail = getSharedPreferences("user_session", MODE_PRIVATE)
            .getString("email", "") ?: ""

        val btnGoToResearchList = findViewById<Button>(R.id.btnGoToResearchList)
        btnGoToResearchList.setOnClickListener {
            startActivity(Intent(this, ResearchListActivity::class.java))
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val desc = etDescription.text.toString().trim()
            val participantInput = etParticipantEmails.text.toString()
            val participantEmails = participantInput.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            if (title.isEmpty()) {
                Toast.makeText(this, "Başlık boş olamaz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // POST isteği için data modeli
            val request = AddResearchRequest(
                title = title,
                description = desc,
                owner_email = userEmail,
                invited_emails = participantEmails
            )

            RetrofitClient.api.addResearch(request).enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        Log.d("AddResearch", "Success: ${response.body()?.message}")
                        Toast.makeText(this@AddResearchActivity, "Araştırma başarıyla eklendi", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("AddResearch", "Error response: $errorBody")
                        Toast.makeText(this@AddResearchActivity, "Ekleme başarısız", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Log.e("AddResearch", "Request failed: ${t.message}")
                    Toast.makeText(this@AddResearchActivity, "Sunucu hatası: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
