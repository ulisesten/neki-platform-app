package com.neki.comedoresperanza

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.neki.comedoresperanza.utils.HttpAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button.setOnClickListener {

            if(     user_email.text.toString().isEmpty()
                &&  password.text.toString().isEmpty()) {

                Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login_button.isEnabled = false

            val email = user_email.text.toString()
            val pass = password.text.toString()

            val http = HttpAuth()

            val json = JSONObject()

            json.put("email", email)
            json.put("password", pass)

            http.auth(this,this,"https://open-backend.herokuapp.com/mobileLogin", json, ::loginResponse)

        }

        register_tv.setOnClickListener {
            val i = Intent(this, SignUpActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun loginResponse(res: Boolean){
        if(res){

            val newIntent = Intent(this, SellActivity::class.java)
            newIntent.putExtra("isAuth",true)
            startActivity(newIntent)
            finish()

        } else {

            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            login_button.isEnabled = true

        }
    }

}
