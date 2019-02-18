package com.aashishtamsya.android.mynotes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*
import java.lang.Exception

class AddNoteActivity : AppCompatActivity() {

    var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val bundle = intent.extras
        try {
            id = bundle.getString("noteId")
            Note.getNote(id)?.let {
                bind(it)
            }

        } catch (exception: Exception) { }

    }

    protected fun addNote(view: View) {
        val title = titleEditText.text.toString()
        val content = contentEditText.text.toString()
        if (!title.isEmpty() && !content.isEmpty()) {
            if (id == null) {
                val note = Note(System.currentTimeMillis().toString(), title, content)
                note.saveOrUpdate()
            } else {
                id?.let {
                    Note.updateNote(it, title, content)
                }
            }
            finish()
        } else {
            Toast.makeText(this, "Please enter details of notes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bind(note: Note?) {
        note?.title?.let {
            titleEditText.setText(it)
        }
        note?.content?.let {
            contentEditText.setText(it)
        }
        addNoteButton.text = "Update Note"
    }
}
