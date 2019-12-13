package com.neki.comedoresperanza

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.neki.comedoresperanza.utils.Http
import com.neki.comedoresperanza.utils.MainAdapter
import com.neki.comedoresperanza.utils.Products
import com.neki.comedoresperanza.utils.Utils
import kotlinx.android.synthetic.main.activity_by_category.*
import org.json.JSONArray

class ByCategoryActivity : AppCompatActivity() {

    private val productsByCategory: MutableList<Products> = mutableListOf()
    var userId:String? = null
    var userName:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_by_category)

        val category = intent.getStringExtra("category")

        by_category_toolbar.title = category

        by_category_toolbar.setNavigationIcon(R.drawable.back_button)

        by_category_toolbar.setNavigationOnClickListener {
            finish()
        }

        val http = Http()
        val utils = Utils()

        val prefs = this.getSharedPreferences("storage", Context.MODE_PRIVATE)

        userId = prefs.getString("seller_id", "")
        userName = prefs.getString("seller_name", "")

        http
            .getData(
                this
                ,this
                , "https://open-backend.herokuapp.com/getProductsByCategory?category=${utils.categoryTranslater(category)}"
                , ::getProducts
            )


        /**Updating products*/
        category_swipe_refresh.setOnRefreshListener {

            productsByCategory.clear()

            http
                .getData(
                    this
                    ,this
                    , "https://open-backend.herokuapp.com/getProductsByCategory?category=${utils.categoryTranslater(category)}"
                    , ::getProducts
                )

        }
    }

    private fun getProducts(jsonArr: JSONArray){

        for (i in 0 until jsonArr.length()){

            val p = jsonArr.getJSONObject(i)
            productsByCategory.add(
                Products(
                     userId!!
                    ,userName!!
                    ,p.getString("id")
                    ,p.getString( "product_name")
                    ,p.getString("description").replace("+", " ")
                    ,"$ " + p.getString("price")
                    ,"https://res.cloudinary.com/djlzeapiz/image/upload/v1573315852/" + p.getJSONArray ("image_array")[0].toString())
            )

            category_swipe_refresh.isRefreshing = false

        }

        category_recycler_view.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        category_recycler_view.adapter = MainAdapter(this, productsByCategory)

    }

}
