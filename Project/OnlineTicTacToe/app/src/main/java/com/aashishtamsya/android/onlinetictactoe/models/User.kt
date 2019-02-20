package com.aashishtamsya.android.onlinetictactoe.models

import com.aashishtamsya.android.onlinetictactoe.interfaces.Encoder
import com.google.firebase.auth.FirebaseUser
import java.io.Serializable

class User: Serializable, Encoder {
    companion object {
        val TAG = "User.class"

        fun decode(data: Map<String, Any>?): User? {
            if (data == null) return null
            val uid = data["uid"] as? String
            val email = data["email"] as? String
            val challenge = Challenge.decode(data["challenge"] as? Map<String, Any>)

            if (uid == null) return null
            return User(uid, email, challenge)
        }
    }
    var uid: String? = null
    var email: String? = null
    var challenge: Challenge? = null

    constructor(uid: String? = null, email: String? = null, challenge: Challenge? = null) {
        this.uid = uid
        this.email = email
        this.challenge = challenge
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