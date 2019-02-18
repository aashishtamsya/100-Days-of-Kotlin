package com.aashishtamsya.android.mynotes

import io.realm.Realm

open interface Savable {
    fun saveOrUpdate()
    fun saveOrUpdate(realm: Realm)
}