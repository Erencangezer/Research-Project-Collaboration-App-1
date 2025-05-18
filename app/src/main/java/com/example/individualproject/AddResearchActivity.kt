package com.example.individualproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.individualproject.RetrofitClient
import com.example.individualproject.AddResearchRequest
import com.example.individualproject.DefaultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddResearchActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_research)

        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        btnSave = findViewById(R.id.btnSaveResearch)

        val userEmail = getSharedPreferences("user_session", MODE_PRIVATE).getString("email", "") ?: ""

        val btnGoToResearchList = findViewById<Button>(R.id.btnGoToResearchList)

        btnGoToResearchList.setOnClickListener {
            val intent = Intent(this, ResearchListActivity::class.java)
            startActivity(intent)
        }


        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val desc = etDescription.text.toString()

            if (title.isNotEmpty()) {
                val request = AddResearchRequest(title, desc, userEmail)
                RetrofitClient.api.addResearch(request).enqueue(object : Callback<DefaultResponse> {
                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {

                        if (response.isSuccessful && response.body()?.success == true) {
                            Toast.makeText(this@AddResearchActivity, "Araştırma eklendi", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@AddResearchActivity, "Ekleme başarısız", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(this@AddResearchActivity, "Sunucu hatası: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this, "Başlık boş olamaz", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
