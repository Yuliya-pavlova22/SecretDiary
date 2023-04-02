package org.hyperskill.secretdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val pin = "1234"

        val edPin: EditText = findViewById(R.id.etPin)
        val btnLogin: Button = findViewById(R.id.btnLogin)


        btnLogin.setOnClickListener {
            if (edPin.text.toString() == pin) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                // Toast.makeText(this, "Wrong PIN!", Toast.LENGTH_SHORT).show()
                edPin.error  = "Wrong PIN!"
            }
        }



    }
}