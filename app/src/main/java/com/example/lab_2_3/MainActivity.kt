package com.example.lab_2_3

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

     fun login(view: View) {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
    }

    fun reg(view: View) {
        val intent = Intent(this, Reg::class.java)
        startActivity(intent)
    }
}

