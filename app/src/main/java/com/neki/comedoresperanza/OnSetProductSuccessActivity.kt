package com.neki.comedoresperanza

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_on_set_product_success.*

class OnSetProductSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_set_product_success)

        add_more_products.setOnClickListener {
            val intent = Intent(this, SellActivity::class.java)
            startActivity(intent)
            finish()
        }

        go_home.setOnClickListener {
            val hIntent = Intent(this, HomeActivity::class.java)
            startActivity(hIntent)
            finish()
        }
    }
}
