package com.example.notesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.databinding.ActivityAddNotesBinding

class AddNotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNotesBinding
    private lateinit var db: NotesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        binding.saveBtn.setOnClickListener {
            val  title = binding.titleEditText.text.toString()  //input from the user bu edittext.text
            val content = binding.contentEditText.text.toString()
            val note = Note(0, title, content)
            db.insertNote(note)
            finish() //redirect to the mainActivity
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
        }

    }
}