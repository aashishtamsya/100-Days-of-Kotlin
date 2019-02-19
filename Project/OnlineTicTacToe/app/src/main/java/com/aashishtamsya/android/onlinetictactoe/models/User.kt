package com.aashishtamsya.android.onlinetictactoe.models

import com.aashishtamsya.android.onlinetictactoe.interfaces.Encoder
import com.google.firebase.auth.FirebaseUser
import java.io.Serializable

class User: Serializable, Encoder {
    companion object {
        val TAG = "User.class"
    }
    var uid: String? = null
    var email: String? = null

    constructor(uid: String? = null, email: String? = null) {
        this.uid = uid
        this.email = email
    }

    constructor(user: FirebaseUser? = null) {
        if (user == null) return
        this.uid = user.uid
        this.email = user.email
    }

    override fun encode(): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        if (uid != null) {
            hashMap["uid"] = uid!!
        }
        if (email != null) {
            hashMap["email"] = email!!
        }
        return hashMap
    }
}