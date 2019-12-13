package com.neki.comedoresperanza

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.neki.comedoresperanza.utils.Products

class MainActivity : AppCompatActivity() {

    val products: MutableList<Products> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

        /*products.add(Products("Enchiladas", "Enchiladas con chile y queso", "$8.00","https://i.ytimg.com/vi/-P5pdc9DBLg/maxresdefault.jpg"))
        products.add(Products("Tamales", "Enchiladas con chile y queso", "$8.00","https://res.cloudinary.com/djlzeapiz/image/upload/v1563739654/comedor_esperanza/tamales.jpg"))
        products.add(Products("Entomatadas", "Enchiladas con chile y queso", "$8.00","http://2.bp.blogspot.com/-YSz_DVzdbN0/TwRiOvsUAoI/AAAAAAAAAfI/rzhOe_V-N2c/s1600/Entomatadas.jpg"))
        products.add(Products("Tacos", "Enchiladas con chile y queso", "$8.00","https://res.cloudinary.com/djlzeapiz/image/upload/v1563736711/comedor_esperanza/Shredded_Beef_Tacos.jpg"))
        products.add(Products("Bocoles", "Enchiladas con chile y queso", "$8.00","https://i.pinimg.com/736x/3f/10/d8/3f10d8e271187ebded66e650dd06efaa.jpg"))
        products.add(Products("Migadas", "Enchiladas con chile y queso", "$8.00","http://cdn2.cocinaycomparte.com/public/files/production/recipes/images/2956/thumb/r_2956_1475610679.jpg"))
        products.add(Products("Gorditas", "Enchiladas con chile y queso", "$8.00","https://images1.westword.com/imager/u/original/11204414/sabor-de-mi-tierra-gorditas3.jpg"))
        products.add(Products("Menudo", "Enchiladas con chile y queso", "$8.00","https://elmercadodelpueblo.files.wordpress.com/2011/11/menudo.jpg"))

        recycler_view.layoutManager = GridLayoutManager(this,2)
        recycler_view.adapter = MainAdapter(this, products)
        */


        //val adapter = MainAdapter(this,products)

        //(adapter.adapter as PubsAdapter).notifyDataSetChanged()
    }
}
