package com.aashishtamsya.android.onlinetictactoe.activities

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.aashishtamsya.android.onlinetictactoe.R
import com.aashishtamsya.android.onlinetictactoe.R.drawable.ic_cross
import com.aashishtamsya.android.onlinetictactoe.R.drawable.ic_nought
import com.aashishtamsya.android.onlinetictactoe.models.*
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
    private var sessionId: String? = null
    private var playerSymbol:String? = null

    // game settings
    private var player1 = ArrayList<Int>()
    private var player2 = ArrayList<Int>()
    private var activePlayer = 1

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
        initGame()
    }

    protected fun onTileButtonClick(view: View) {
        var cellId = 0
        (view as? Button)?.let {
            when (it.id) {
                R.id.button0 -> cellId = 0
                R.id.button1 -> cellId = 1
                R.id.button2 -> cellId = 2
                R.id.button3 -> cellId = 3
                R.id.button4 -> cellId = 4
                R.id.button5 -> cellId = 5
                R.id.button6 -> cellId = 6
                R.id.button7 -> cellId = 7
                R.id.button8 -> cellId = 8
            }
            if (it.isEnabled) {
                it.isEnabled = false
//                playGame(cellId, it)
                val data = HashMap<String, Any>()
                user?.email?.let {
                    data[cellId.toString()] = it
                }
                db!!.collection("sessions").document(sessionId!!).set(data, SetOptions.merge())
            }
        }
    }

    private fun playGame(cellId: Int, buttonSelected: Button) {
        if (activePlayer == 1) {
//            buttonSelected.setText(PlayerType.NOUGHT.symbol())
            buttonSelected.background = ContextCompat.getDrawable(this, ic_nought)
            player1.add(cellId)
            activePlayer = 2
        } else if (activePlayer == 2) {
//            buttonSelected.setText(PlayerType.CROSS.symbol())
            buttonSelected.background = ContextCompat.getDrawable(this, ic_cross)
            player2.add(cellId)
            activePlayer = 1
        }
        buttonSelected.isEnabled = false
        findWinner()
    }


    private fun findWinner() {

    }

    private fun autoPlay(cellId: Int) {
        var buttonSelected: Button?
        when (cellId) {
            0 -> buttonSelected = button0
            1 -> buttonSelected = button1
            2 -> buttonSelected = button2
            3 -> buttonSelected = button3
            4 -> buttonSelected = button4
            5 -> buttonSelected = button5
            6 -> buttonSelected = button6
            7 -> buttonSelected = button7
            8 -> buttonSelected = button8
            else -> return
        }
        if (buttonSelected == null) return
        playGame(cellId, buttonSelected!!)
    }

    private fun initGame() {
        user?.email?.let {
            currentPlayerTextView.setText(it)
        }
        player1 = ArrayList<Int>()
        player2 = ArrayList<Int>()
        button0.text = ""
        button1.text = ""
        button2.text = ""
        button3.text = ""
        button4.text = ""
        button5.text = ""
        button6.text = ""
        button7.text = ""
        button8.text = ""
        button0.setBackgroundColor(Color.LTGRAY)
        button1.setBackgroundColor(Color.LTGRAY)
        button2.setBackgroundColor(Color.LTGRAY)
        button3.setBackgroundColor(Color.LTGRAY)
        button4.setBackgroundColor(Color.LTGRAY)
        button5.setBackgroundColor(Color.LTGRAY)
        button6.setBackgroundColor(Color.LTGRAY)
        button7.setBackgroundColor(Color.LTGRAY)
        button8.setBackgroundColor(Color.LTGRAY)
        button0.isEnabled = true
        button1.isEnabled = true
        button2.isEnabled = true
        button3.isEnabled = true
        button4.isEnabled = true
        button5.isEnabled = true
        button6.isEnabled = true
        button7.isEnabled = true
        button8.isEnabled = true
        activePlayer = 1
    }

    protected fun signOutButtonOnClick(view: View) {
        signOut()
    }

    protected fun requestButtonOnClick(view: View) {
        val email = emailEditText.text.toString()
        if (user == null) return
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show()
            return
        }
        request(email, user!!)
        playerSymbol = "X"
        playOnline(email + user!!.email!!, Status.REQUESTED)
    }

    protected fun acceptButtonOnClick(view: View) {
        val email = emailEditText.text.toString()
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show()
            return
        }
        playerSymbol = "0"
        playOnline( user!!.email!! + email, Status.ACCEPTED)
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

    private fun playOnline(sessionId: String, status: Status) {
        this.sessionId = sessionId
        if (db == null && user!!.email == null) return
        val gameMap = HashMap<String, Any>()
        gameMap["status"] = status.value()
        db!!.collection("sessions").document(sessionId).delete()
        db!!.collection("sessions").document(sessionId).set(gameMap, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "Status successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        db!!.collection("sessions").document(sessionId)
            .addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@EventListener
                }
                if (snapshot != null && snapshot.exists()) {
                    player1.clear()
                    player2.clear()
                    val game = Game.decode(snapshot.data)
                    game?.status?.let {
                        when (it) {
                            Status.REQUESTED -> return@EventListener
                            Status.ACCEPTED -> Toast.makeText(this, "Challenge Accepted", Toast.LENGTH_SHORT).show()
                        }
                    }
                    game?.button0?.let {
                        play(0, it)
                    }
                    game?.button1?.let {
                        play(1, it)
                    }
                    game?.button2?.let {
                        play(2, it)
                    }
                    game?.button3?.let {
                        play(3, it)
                    }
                    game?.button4?.let {
                        play(4, it)
                    }
                    game?.button5?.let {
                        play(5, it)
                    }
                    game?.button6?.let {
                        play(6, it)
                    }
                    game?.button7?.let {
                        play(7, it)
                    }
                    game?.button8?.let {
                        play(8, it)
                    }
                } else {
                    Log.d(TAG, "Current data: null")
                }
            })
    }

    private fun play(cellId: Int, email: String) {
        if (email != user!!.email) {
            activePlayer = if (playerSymbol == "X") 1 else 2
        } else {
            activePlayer = if (playerSymbol == "X") 2 else 1
        }
        autoPlay(cellId)
    }
}
