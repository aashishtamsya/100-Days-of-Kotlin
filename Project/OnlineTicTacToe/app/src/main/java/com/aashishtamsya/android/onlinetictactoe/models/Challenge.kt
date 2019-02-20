package com.aashishtamsya.android.onlinetictactoe.models

import com.aashishtamsya.android.onlinetictactoe.interfaces.Encoder


class Challenge: Encoder {

    companion object {
        val KEY_EMAIL = "email"
        val KEY_CHALLENGER_ID = "challenger_id"
        val KEY_CHALLENGER_EMAIL = "challenger_email"

        fun decode(data: Map<String, Any>?): Challenge? {
            if (data == null) return null
            val email = data[KEY_EMAIL] as? String
            val challengerId = data[KEY_CHALLENGER_ID] as? String
            val challengerEmail = data[KEY_CHALLENGER_EMAIL] as? String

            if (email == null || challengerId == null || challengerEmail == null) return null
            return Challenge(email, challengerId, challengerEmail)
        }
    }
    var email: String? = null
    var challengerId: String? = null
    var challengerEmail: String? = null

    constructor(email: String? = null, challengerId: String? = null, challengerEmail: String? = null) {
        this.email = email
        this.challengerId = challengerId
        this.challengerEmail = challengerEmail
    }

    override fun encode(): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        val challengeMap = HashMap<String, Any>()
        if (email != null) {
            challengeMap[KEY_EMAIL] = email!!
        }
        if (challengerId != null) {
            challengeMap[KEY_CHALLENGER_ID] = challengerId!!
        }
        if (challengerEmail != null) {
            challengeMap[KEY_CHALLENGER_EMAIL] = challengerEmail!!
        }
        hashMap["challenge"] = challengeMap
        return hashMap
    }
}