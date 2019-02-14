package com.aashishtamsya.android.tictactoe

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var activePlayer = PlayerType.NOUGHT
    private var state = ArrayList<Int>()
    private var winningCombinations = arrayListOf(arrayListOf(0,1,2), arrayListOf(3,4,5), arrayListOf(6,7,8), arrayListOf(0,3,6), arrayListOf(1,4,7), arrayListOf(2,5,8), arrayListOf(0,4,8), arrayListOf(2,4,6))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initGame()
    }

    fun click(view: View) {
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
                playGame(cellId, it)
            }
        }
    }

    private fun playGame(id: Int, button: Button) {
        button.isEnabled = false
        button.text = activePlayer.symbol()
        button.setBackgroundColor(activePlayer.getBackgroundColor())
        state.set(id, activePlayer.value())
        if (activePlayer == PlayerType.NOUGHT) {
            activePlayer = PlayerType.CROSS
        } else if (activePlayer == PlayerType.CROSS) {
            activePlayer = PlayerType.NOUGHT
        }
        Log.i("STATE", "$state")
        if (state.filter { it != 0 }.count() > 4) {
            findWinner()
        }
    }

    private fun findWinner() {
        var winner = -1
        loop@ for (combinantion in winningCombinations) {
            val first = state[combinantion[0]]
            val second = state[combinantion[1]]
            val third = state[combinantion[2]]
            if ( first != 0 && second != 0 && third != 0 && first == second && second == third) {
                winner = first
                break@loop
            }
        }
        if (winner != -1) {
            val player = if (winner == 1) "CROSS" else "NOUGHT"
            Toast.makeText(this, "Winner is Player $player", Toast.LENGTH_SHORT).show()
            initGame()
        } else if (state.filter { it == 0 }.isEmpty()) {
            Toast.makeText(this, "Game is Drawn", Toast.LENGTH_SHORT).show()
            initGame()
        }
    }

    private fun initGame() {
        state = arrayListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
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
        activePlayer = PlayerType.CROSS
    }
}
