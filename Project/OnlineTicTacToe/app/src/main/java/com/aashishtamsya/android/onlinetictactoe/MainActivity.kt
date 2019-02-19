package com.aashishtamsya.android.onlinetictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.aashishtamsya.android.onlinetictactoe.models.User
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "MainActivity.class"
    }

    private var auth: FirebaseAuth? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var user: User? = null
    private var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        user = intent.extras.get(User.TAG) as User
        if (user != null) {
            registerUser(user!!)
        }
    }

    protected fun signOutButtonOnClick(view: View) {
        signOut()
    }

    private fun signOut() {
        if (auth != null) {
            auth!!.signOut()
            finish()
        }
    }

    private fun registerUser(user: User) {
        if (db != null && user.uid != null) {
            val doc = db!!.collection("users").document(user.uid!!)
            doc.set(user!!.encode())
        }
    }
}
