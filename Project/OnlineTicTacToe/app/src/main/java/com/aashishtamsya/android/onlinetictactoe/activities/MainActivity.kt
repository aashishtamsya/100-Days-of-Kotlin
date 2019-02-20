package com.aashishtamsya.android.onlinetictactoe.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.aashishtamsya.android.onlinetictactoe.R
import com.aashishtamsya.android.onlinetictactoe.models.Challenge
import com.aashishtamsya.android.onlinetictactoe.models.User
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "MainActivity.class"
    }

    private var auth: FirebaseAuth? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var user: User? = null
    private var challenge: Challenge? = null
    private var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        user = intent.extras.get(User.TAG) as User
        if (user != null) {
            registerUser(user!!)
            listenIncommingRequest(user!!)
        }
    }

    protected fun signOutButtonOnClick(view: View) {
        signOut()
    }

    protected fun requestButtonOnClick(view: View) {
        val email = emailEditText.text.toString()
        if (email.isEmpty() && user != null) {
            Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show()
            return
        }
        request(email, user!!)
    }

    private fun signOut() {
        if (auth != null) {
            auth!!.signOut()
            finish()
        }
    }

    private fun registerUser(user: User) {
        if (user.uid == null) return
        if (db != null) {
            val doc = db!!.collection("users").document(user.uid!!)
            doc.set(user!!.encode())
        }
    }

    private fun request(email: String, user: User) {
        if (user.uid == null || user.email == null) return
        if (email.equals(user!!.email)) {
            Toast.makeText(this, "Please do not enter your email address.", Toast.LENGTH_SHORT).show()
            return
        }
        if (db != null) {
            db!!.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    val doc = documents.filter { it["email"]!!.equals(email) }.first()
                    db!!.collection("users").document(doc.id)
                        .set(Challenge(email, user!!.uid, user!!.email).encode(), SetOptions.merge())
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
    }

    private fun listenIncommingRequest(user: User) {
        if (db == null) return
        db!!.collection("users").document(user.uid!!)
            .addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@EventListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val user = User.decode(snapshot.data)
                    user?.challenge.let {
                        this.challenge = it
                    }
                    user?.challenge?.challengerEmail.let {
                        emailEditText.setText(it)
                    }
                } else {
                    Log.d(TAG, "Current data: null")
                }
            })
    }

    private fun playOnline(sessionId: String) {

    }
}
