package com.example.notesapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private var note:List<Note>, context: Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>()//brackets?
{

    private val db: NotesDatabaseHelper = NotesDatabaseHelper(context)

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateBtn)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = note.size


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {   //holder set data, position which item to get
        val notes = note[position]
        holder.titleTextView.text = notes.title
        holder.contentTextView.text = notes.content

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNoteActivity:: class.java).apply {
                putExtra("note_id", notes.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            db.deleteNote(notes.id)
            refreshData(db.getAllNotes())
            Toast.makeText(holder.itemView.context, "Note Deleted", Toast.LENGTH_SHORT).show()
        }
    }


    //else have to keep refreshing
    fun refreshData(newNotes: List<Note>){
        note = newNotes
        notifyDataSetChanged()
    }

}