package com.aashishtamsya.android.findmyage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun calculateAge(view: View) {
    if (editText.text.toString().isEmpty()) {
      Toast.makeText(this, "Please enter Year of Birth.", Toast.LENGTH_SHORT).show()
      return
    }
    val year = editText.text.toString().toInt()
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    textView.setText(if (currentYear > year) "Your age is ${currentYear - year}." else "Please enter valid Year of Birth.")
    editText.setText("")
  }
}
