package com.aashishtamsya.android.onlinetictactoe.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.aashishtamsya.android.onlinetictactoe.R
import com.aashishtamsya.android.onlinetictactoe.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.Serializable

class SignUpActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
    }

    protected fun signUpButtonOnClick(view: View) {
        val email = emailTextEdit.text.toString()
        val verifyEmail = verifyEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show()
            return
        }
        if (verifyEmail.isEmpty()) {
            Toast.makeText(this, "Please enter verify email address.", Toast.LENGTH_SHORT).show()
            return
        }
        if (!email.equals(verifyEmail)) {
            Toast.makeText(this, "Verify email should match.", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show()
            return
        }
        signUp(email, password)
    }

    private fun signUp(email: String, password: String) {
        if (auth == null) return
        auth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LoginActivity.TAG, "createUserWithEmail:success")
                    val user = auth!!.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LoginActivity.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            Toast.makeText(this, "Unable to sign up at this time", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "SignUp successfull", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun showGame(firebaseUser: FirebaseUser?) {
        val user = User(firebaseUser)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(User.TAG, user as Serializable)
        startActivity(intent)
    }
}
