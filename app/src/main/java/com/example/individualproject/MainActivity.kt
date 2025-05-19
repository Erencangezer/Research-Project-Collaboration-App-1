package com.example.individualproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.individualproject.AddResearchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var userNameTextView: TextView
    private lateinit var profileButton: Button
    private lateinit var btnLogout: Button


    private lateinit var btnMenu1: Button
    private lateinit var btnMenu2: Button
    private lateinit var btnMenu3: Button
    private lateinit var btnMenu4: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userNameTextView = findViewById(R.id.tvUserName)
        profileButton = findViewById(R.id.btnProfile)
        btnLogout = findViewById(R.id.btnLogout)

        btnMenu1 = findViewById(R.id.btnMenu1)
        btnMenu2 = findViewById(R.id.btnMenu2)
        btnMenu3 = findViewById(R.id.btnMenu3)
        btnMenu4 = findViewById(R.id.btnMenu4)

        val email = intent.getStringExtra("user_email") ?: ""

        if (email.isNotEmpty()) {
            fetchUserName(email)
        } else {
            userNameTextView.text = "Kullanıcı"
        }

        profileButton.setOnClickListener {
            Toast.makeText(this, "Profil butonuna tıklandı", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
            sharedPref.edit().clear().apply()  // Tüm oturum verisini temizle

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


        btnMenu1.setOnClickListener {
            val intent = Intent(this, AddResearchActivity::class.java)
            startActivity(intent)
        }
        btnMenu2.setOnClickListener {
            val intent = Intent(this, MessagesActivity::class.java)
            intent.putExtra("user_email", email) // Giriş yapan kullanıcının emailini aktar
            startActivity(intent)
        }
        btnMenu3.setOnClickListener { Toast.makeText(this, "Menü 3 tıklandı", Toast.LENGTH_SHORT).show() }
        btnMenu4.setOnClickListener { Toast.makeText(this, "Menü 4 tıklandı", Toast.LENGTH_SHORT).show() }
    }

    private fun fetchUserName(email: String) {
        RetrofitClient.api.getUserByEmail(email).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    userNameTextView.text = response.body()?.name ?: "Kullanıcı"
                } else {
                    userNameTextView.text = "Kullanıcı"
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                userNameTextView.text = "Kullanıcı"
            }
        })
    }
}
