package com.example.individualproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.individualproject.RetrofitClient
import com.example.individualproject.Research
import com.example.individualproject.ResearchListResponse
import com.example.individualproject.ResearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResearchListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResearchAdapter
    private lateinit var researches: MutableList<Research>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_research_list)

        recyclerView = findViewById(R.id.recyclerViewResearches)
        recyclerView.layoutManager = LinearLayoutManager(this)

        researches = mutableListOf()
        adapter = ResearchAdapter(researches) { selectedResearch ->
            val intent = Intent(this, ResearchDetailActivity::class.java)
            intent.putExtra("research_id", selectedResearch.id)
            intent.putExtra("research_title", selectedResearch.title)
            startActivity(intent)
        }
        recyclerView.adapter = adapter


        val email = getSharedPreferences("user_session", MODE_PRIVATE).getString("email", "") ?: ""

        fetchResearches(email)
    }

    private fun fetchResearches(email: String) {
        RetrofitClient.api.getResearches(email).enqueue(object : Callback<ResearchListResponse> {
            override fun onResponse(call: Call<ResearchListResponse>, response: Response<ResearchListResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    researches.clear()
                    researches.addAll(response.body()!!.researches)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@ResearchListActivity, "Araştırmalar getirilemedi", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResearchListResponse>, t: Throwable) {
                Toast.makeText(this@ResearchListActivity, "Sunucu hatası", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

