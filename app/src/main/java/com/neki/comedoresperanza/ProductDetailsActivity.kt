package com.neki.comedoresperanza

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.neki.comedoresperanza.utils.*
import kotlinx.android.synthetic.main.activity_product_details.*
import org.json.JSONObject

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    var userId: String = String()
    var userName: String = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        product_details_toolbar.setNavigationIcon(R.drawable.back_button)

        product_details_toolbar.setNavigationOnClickListener {
            finish()
        }

        userId =  intent.getStringExtra("userId")
        userName =  intent.getStringExtra("userName")
        val productId = intent.getStringExtra("productId")

        //Log.d("id", mId)
        val http = Http()

        http.getProductDetails(this
            ,this
            ,"https://open-backend.herokuapp.com/getProductDetails"
            , productId
            , ::callBack)


    }

    private fun callBack(j: JSONObject){

        val images = j.getJSONArray("image_array")
        val imageList: ArrayList<String> = arrayListOf()

        for (i in 0 until images.length() ){
            imageList.add(images[i].toString())
        }

        viewPager = findViewById(R.id.slider_view_pager)
        val adapter = ViewPagerAdapter(this, imageList)
        viewPager.adapter = adapter

        product_name.text = j.getString("product_name")
        product_price.text = "$ ${j.getString("price")}"
        product_description.text = j.getString("description")

        contact_seller_button.setOnClickListener {
            val intent = Intent(this, ContactingSellerActivity::class.java)
            intent.putExtra("userId", userId )
            intent.putExtra("userName", userName )
            intent.putExtra("seller_id", j.getString("seller_id"))
            intent.putExtra("seller_name", j.getString("seller_name"))
            intent.putExtra("productId", j.getString("id"))
            startActivity(intent)
        }
    }
}
