package com.neki.comedoresperanza

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.PopupMenu
import android.text.TextUtils.isEmpty
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.neki.comedoresperanza.utils.Http
import com.neki.comedoresperanza.utils.Utils
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_sell.*

data class Info(val uri:Uri)

class SellActivity : AppCompatActivity() {

    private val imgResult = 1
    private var selector = 0
    private val imageInfoArray = ArrayList<Info>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)

        sell_toolbar.setNavigationIcon(R.drawable.back_button)

        sell_toolbar.setNavigationOnClickListener {
            finish()
        }

        val prefs = this.getSharedPreferences("storage", Context.MODE_PRIVATE)
        val utils = Utils()

        //val sessionToken = prefs.getString("session_token", null)
        val sellerId = prefs.getString("seller_id", null)
        val sellerName = prefs.getString("seller_name", null)

        if( sellerId != null) {


            sell_image1.setOnClickListener {

                selector = 1
                val intent1 = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }

                startActivityForResult(intent1, imgResult).apply {}
            }

            sell_image2.setOnClickListener {
                selector = 2
                val intent2 = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }

                startActivityForResult(intent2, imgResult).apply {}
            }

            sell_image3.setOnClickListener {
                selector = 3
                val intent3 = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }

                startActivityForResult(intent3, imgResult).apply {}
            }

            sell_image4.setOnClickListener {
                selector = 4
                val intent4 = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }

                startActivityForResult(intent4, imgResult).apply {}
            }

            sell_image5.setOnClickListener {
                selector = 5
                val intent5 = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }

                startActivityForResult(intent5, imgResult).apply {}

            }

            sell_image6.setOnClickListener {
                selector = 6
                val intent6 = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }

                startActivityForResult(intent6, imgResult).apply {}
            }

            sell_product_category.setOnClickListener {
                showPopup(sell_product_category)
            }

            /**Send request*/
            set_product_btn.setOnClickListener {

                if(     sell_product_name.text.toString().isEmpty()
                    &&  sell_product_description.text.toString().isEmpty()
                    &&  sell_product_price.text.toString().isEmpty()) {

                    Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


                set_product_btn.isEnabled = false

                val http = Http()
                val url = "https://open-backend.herokuapp.com/setProduct"

                http.setProduct(
                    this
                        , url
                        , imageInfoArray
                        , sellerId
                        , sellerName!!
                        , sell_product_name.text.toString()
                        , utils.categoryTranslater(sell_product_category.text.toString())
                        , sell_product_description.text.toString()
                        , sell_product_price.text.toString()
                        , ::setProductCallBack
                    )


            }

        } else {
            val actIntent = Intent(this, LoginActivity::class.java)
            startActivity(actIntent)
            finish()
        }
    }

    private fun setProductCallBack(message: String, st: Boolean){


        if(st){
            val newIntent = Intent(this, OnSetProductSuccessActivity::class.java)
            startActivity(newIntent)
            finish()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            set_product_btn.isEnabled = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == imgResult && resultCode == RESULT_OK
            && null != data){

            data.data?.also { uri ->

                Glide.with(this).load(uri).into(selectView(selector))

                imageInfoArray.add(Info(uri))
            }

            setResult(Activity.RESULT_OK)

        }


    }


    private fun selectView(id: Int?): ImageView {

        var imageView: ImageView? = null
        when(id) {

            1 -> imageView = sell_image1

            2 -> imageView = sell_image2

            3 -> imageView = sell_image3

            4 -> imageView = sell_image4

            5 -> imageView = sell_image5

            6 -> imageView = sell_image6

        }

        return imageView!!
    }


    private fun showPopup(v: View) {
        val popup = PopupMenu(this, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.category_menu, popup.menu)
        popup.setOnMenuItemClickListener {item ->
            onMenuItemClick(item)
        }
        popup.show()
    }


    private fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.computation -> {
                sell_product_category.text = getString(R.string.computation)
                true
            }
            R.id.music -> {
                sell_product_category.text = getString(R.string.music)
                true
            }
            R.id.books -> {
                sell_product_category.text = getString(R.string.books)
                true
            }
            R.id.sports -> {
                sell_product_category.text = getString(R.string.sports)
                true
            }
            R.id.offers -> {
                sell_product_category.text = getString(R.string.offers)
                true
            }
            R.id.pets -> {
                sell_product_category.text = getString(R.string.pets)
                true
            }
            else -> onMenuItemClick(item)
        }
    }

}


