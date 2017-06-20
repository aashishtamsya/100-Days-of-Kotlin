package com.aashishtamsya.simplecalculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        plusButton.setOnClickListener(View.OnClickListener { view ->
            perform(Operations.ADD)
        })

        minusButton.setOnClickListener(View.OnClickListener { view ->
            perform(Operations.SUBTRACT)
        })

        multiplyButton.setOnClickListener(View.OnClickListener { view ->
            perform(Operations.MULTIPLY)
        })

        divideButton.setOnClickListener(View.OnClickListener { view ->
            perform(Operations.DIVIDE)
        })
    }

    fun getFirstNumber(): Int {
        return Integer.parseInt(firstNumberEditText.text.toString())
    }

    fun getSecondNumber(): Int {
        return Integer.parseInt(secondNumberEditText.text.toString())
    }

    fun perform(operation: Operations) {
        when (operation) {
            Operations.ADD -> add()
            Operations.SUBTRACT -> subtract()
            Operations.MULTIPLY -> multiply()
            Operations.DIVIDE -> divide()
        }
    }

    fun add() {
        val numberOne = getFirstNumber()
        val numberTwo = getSecondNumber()
        val result = numberOne + numberTwo
        resultTextView.text = result.toString()
        actionTextView.text = "+"
    }

    fun subtract() {
        val numberOne = getFirstNumber()
        val numberTwo = getSecondNumber()
        val result = numberOne - numberTwo
        resultTextView.text = result.toString()
        actionTextView.text = "-"
    }

    fun multiply() {
        val numberOne = getFirstNumber()
        val numberTwo = getSecondNumber()
        val result = numberOne * numberTwo
        resultTextView.text = result.toString()
        actionTextView.text = "-"
    }

    fun divide() {
        val numberOne = getFirstNumber()
        val numberTwo = getSecondNumber()
        val result = numberOne / numberTwo
        resultTextView.text = result.toString()
        actionTextView.text = "/"
    }

}
