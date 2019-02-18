package com.aashishtamsya.android.mynotes

import android.app.SearchManager
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {

    var notes = ArrayList<Note>()

    inner class NotesListener: LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun renderData() {
            notes.clear()
            notes.addAll(Note.getNotes())
            var noteAdapter = NoteAdapter(notes)
            listView.adapter = noteAdapter
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)
        mainSwipeRefreshLayout.setOnRefreshListener {
            fetchData()
            mainSwipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData() {
        notes.clear()
        notes.addAll(Note.getNotes())
        var noteAdapter = NoteAdapter(notes)
        listView.adapter = noteAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var searchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        var searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when(item.itemId) {
                R.id.addNote -> {
                    var intent = Intent(this, AddNoteActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    inner class NoteAdapter: BaseAdapter {
        var notes = ArrayList<Note>()
        constructor(notes: ArrayList<Note>): super() {
            this.notes = notes
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val cell = layoutInflater.inflate(R.layout.ticket, null)
            var note = notes[p0]
            cell.titleTextView.text = note.title
            cell.contentTextView.text = note.content
            cell.deleteImageView.setOnClickListener {
                Note.deleteNote(note.id)
                fetchData()
            }
            cell.editImageView.setOnClickListener {
                note.id?.let {
                    updateNoteWith(it)
                }
            }
            return cell
        }

        override fun getItem(p0: Int): Any {
            return notes[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return notes.count()
        }
    }

    private fun updateNoteWith(id: String) {
        var intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("noteId", id)
        startActivity(intent)
    }
}
