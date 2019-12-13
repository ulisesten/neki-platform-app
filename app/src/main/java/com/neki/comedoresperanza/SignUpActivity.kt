package com.neki.comedoresperanza

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.neki.comedoresperanza.utils.HttpAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        sign_up_button.setOnClickListener {

            if(     user_name.text.toString().isEmpty()
                &&  user_email_su.text.toString().isEmpty()
                &&  password_su.text.toString().isEmpty()) {

                Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sign_up_button.isEnabled = false

            val userName = user_name.text.toString()
            val email = user_email_su.text.toString()
            val pass = password_su.text.toString()

            val http = HttpAuth()

            val json = JSONObject()

            json.put("username",userName)
            json.put("email", email)
            json.put("password",pass)

            http.auth(this
                ,this
                ,"https://open-backend.herokuapp.com/mobileRegister"
                , json
                , ::signUpResponse)
        }

        login_tv.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun signUpResponse(res: Boolean){
        if(res){

            val newIntent = Intent(this, HomeActivity::class.java)
            newIntent.putExtra("isAuth",true)
            startActivity(newIntent)
            finish()

        } else {

            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            sign_up_button.isEnabled = true

        }
    }
}
