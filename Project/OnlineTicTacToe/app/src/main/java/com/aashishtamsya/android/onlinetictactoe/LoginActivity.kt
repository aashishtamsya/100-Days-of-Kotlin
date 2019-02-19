package com.aashishtamsya.android.onlinetictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.aashishtamsya.android.onlinetictactoe.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        val TAG = "LoginActivity.class"
    }

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        if (auth != null) {
            val currentUser = auth!!.currentUser
            updateUI(currentUser)
        }
    }

    protected fun loginButtonOnClick(view: View) {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            return
        }
        login(email, password)

    }

    protected fun signUpButtonOnClick(view: View) {
        showSignUp()
    }

    private fun login(email: String, password: String) {
        if (auth == null) return
        auth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth!!.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null, true)
                }
            }

    }

    private fun updateUI(user: FirebaseUser?, showToast: Boolean = false) {
        if (user != null) {
            // TODO Show game activity
            showGame(user)
        } else {
            if (showToast) {
                Toast.makeText(this, "Unable to login at this time", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun showGame(firebaseUser: FirebaseUser?) {
        val user = User(firebaseUser)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(User.TAG, user)
        startActivity(intent)
    }
}
