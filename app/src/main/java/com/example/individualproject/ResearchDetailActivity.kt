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
import com.example.individualproject.Note
import com.example.individualproject.NoteAdapter
import com.example.individualproject.NoteListResponse
import com.example.individualproject.RetrofitClient
import com.example.individualproject.GenericResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResearchDetailActivity : AppCompatActivity() {

    private lateinit var tvResearchTitle: TextView
    private lateinit var etNoteContent: EditText
    private lateinit var btnSaveNote: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private var noteList: MutableList<Note> = mutableListOf()
    private var researchId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_research_detail)

        tvResearchTitle = findViewById(R.id.tvResearchDetailTitle)
        etNoteContent = findViewById(R.id.etNoteContent)
        btnSaveNote = findViewById(R.id.btnSaveNote)
        recyclerView = findViewById(R.id.recyclerViewNotes)

        researchId = intent.getIntExtra("research_id", -1)
        val researchTitle = intent.getStringExtra("research_title")
        tvResearchTitle.text = researchTitle

        recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(noteList)
        recyclerView.adapter = noteAdapter

        fetchNotes()

        btnSaveNote.setOnClickListener {
            val content = etNoteContent.text.toString()
            if (content.isNotEmpty()) {
                val request = AddNoteRequest(researchId, content)

                RetrofitClient.api.addNote(request).enqueue(object : Callback<GenericResponse> {
                    override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            Toast.makeText(this@ResearchDetailActivity, "Not eklendi", Toast.LENGTH_SHORT).show()
                            fetchNotes() // notları yenile
                        } else {
                            Toast.makeText(this@ResearchDetailActivity, "Not eklenemedi", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                        Toast.makeText(this@ResearchDetailActivity, "Sunucu hatası", Toast.LENGTH_SHORT).show()
                    }
                })

            }
        }

    }

    private fun fetchNotes() {
        RetrofitClient.api.getNotes(researchId).enqueue(object : Callback<NoteListResponse> {
            override fun onResponse(call: Call<NoteListResponse>, response: Response<NoteListResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    noteAdapter.updateNotes(response.body()!!.notes)  // <- BURASI
                }
            }

            override fun onFailure(call: Call<NoteListResponse>, t: Throwable) {
                Toast.makeText(this@ResearchDetailActivity, "Notlar alınamadı", Toast.LENGTH_SHORT).show()
            }
        })
    }

}

