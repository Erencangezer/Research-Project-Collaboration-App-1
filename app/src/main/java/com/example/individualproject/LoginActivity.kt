package com.example.individualproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val savedEmail = sharedPref.getString("email", null)

        if (savedEmail != null) {
            // Kullanıcı zaten giriş yapmış, direkt ana menüye gönder
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val tvGoToRegister = findViewById<TextView>(R.id.tvGoToRegister)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email ve şifre boş olamaz", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        val user = User(email = email, password = password)
        RetrofitClient.api.login(user).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@LoginActivity, "Giriş başarılı", Toast.LENGTH_SHORT).show()

                    val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
                    sharedPref.edit().putString("email", email).apply()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMsg = response.body()?.message ?: "Giriş başarısız: ${response.code()}"
                    Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Sunucuya bağlanılamıyor", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
