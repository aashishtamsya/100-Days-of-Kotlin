package com.aashishtamsya.android.mynotes

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.util.ArrayList

@RealmClass
open class Note() : RealmObject(), Savable {
    @PrimaryKey
    var id: String? = null
    var title: String? = null
    var content: String? = null

    companion object {
        @JvmStatic
        fun getNote(byId: String? = null): Note? {
            val id = byId ?: (return null)
            val config = RealmConfiguration.Builder().name("notes.realm").build()
            val realm = Realm.getInstance(config)
            return realm.where<Note>().equalTo("id", id).findFirst()
        }

        @JvmStatic
        fun getNotes(): ArrayList<Note> {
            val config = RealmConfiguration.Builder().name("notes.realm").build()
            val realm = Realm.getInstance(config)
            val results = realm.where<Note>().findAll()
            val notes = ArrayList<Note>()
            notes.addAll(realm.copyFromRealm(results).reversed())
            return notes
        }

        @JvmStatic
        fun updateNote(byId: String?, title: String? = null, content: String? = null) {
            val config = RealmConfiguration.Builder().name("notes.realm").build()
            val realm = Realm.getInstance(config)
            realm.executeTransaction {
                val note = getNote(byId)
                note?.let { note ->
                    title?.let {
                        note.title = it
                    }
                    content?.let {
                        note.content = it
                    }
                }
            }
        }

        @JvmStatic
        fun deleteNote(byId: String?) {
            val config = RealmConfiguration.Builder().name("notes.realm").build()
            val realm = Realm.getInstance(config)
            realm.executeTransaction {
                val note = getNote(byId)
                note?.let {
                    it.deleteFromRealm()
                }
            }
        }
    }

    constructor(id: String? = null, title: String? = null, content: String? = null) : this() {
        this.id = id
        this.title = title
        this.content = content
    }

    override fun saveOrUpdate() {
        val config = RealmConfiguration.Builder().name("notes.realm").build()
        val realm = Realm.getInstance(config)
        saveOrUpdate(realm)
    }

    override fun saveOrUpdate(realm: Realm) {
        realm.executeTransaction {
            val note = it.createObject<Note>(this.id)
            note.content = this.content
            note.title = this.title
        }
    }





}