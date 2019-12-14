package com.neki.comedoresperanza

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.neki.comedoresperanza.utils.*
import kotlinx.android.synthetic.main.activity_main.recycler_view
import kotlinx.android.synthetic.main.content_home.*
import org.json.JSONArray
import java.util.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var pagerAdapter:ViewPagerAdapter? = null
    var coverViewPager:ViewPager? = null
    private val menuOptions = MenuOptions()
    private val products: MutableList<Products> = mutableListOf()
    private val categories: MutableList<Category> = mutableListOf()
    private val covers:ArrayList<String> = arrayListOf()
    private var userId:String? = null
    private var userName:String? = null
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        coverViewPager  = findViewById<ViewPager>(R.id.cover_view_pager)
        pagerAdapter = ViewPagerAdapter(this, covers)

        /**Covers/Portadas*/
        covers.add("background_image")
        covers.add("snquvnoh2klxorjl9og4")

        handleCovers()/**Scroll covers by time*/

        coverViewPager!!.adapter = pagerAdapter


        /**Server: https://open-backend.herokuapp.com/ */
        val http = Http()
        val prefs = this.getSharedPreferences("storage", Context.MODE_PRIVATE)

        userId = prefs.getString("seller_id", "")
        userName = prefs.getString("seller_name", "")

        if(userId != null) {

            Log.d("id", userId)

            /**Init web socket instance*/
            WebSocket.getInstance().createSocket(userId!!)
            SocketIOService.startService(this, "neki notifications")

        }

        categories.add(Category("","computación"))
        categories.add(Category("","música"))
        categories.add(Category("","libros"))
        categories.add(Category("","deportes"))
        categories.add(Category("","ofertas"))
        categories.add(Category("","mascotas"))
        categories.add(Category("","más"))

        /**Place here the adapter*/
        category_layout.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        category_layout.adapter = CategoryAdapter(this, categories)

        /**@param ::getProducts, parse a jsonArray and adds it to products recycler view adapter*/
        http
            .getData(
                 this
                ,this
                , "https://open-backend.herokuapp.com/getProducts"
                , ::getProducts
            )

        swipe_refresh.setOnRefreshListener {

            products.clear()

            http
                .getData(
                    this
                    ,this
                    , "https://open-backend.herokuapp.com/getProducts"
                    , ::getProducts
                )

        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            /*Snack bar.make(view, "Replace with your own action", Snack bar.LENGTH_LONG)
                .setAction("Action", null).show()*/
            menuOptions.onSellProductOption(this)
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    private fun getProducts(jsonArr: JSONArray){

        for (i in 0 until jsonArr.length()){

            val p = jsonArr.getJSONObject(i)
            products.add(Products(
                 userId!!
                ,userName!!
                ,p.getString("id")
                ,p.getString( "product_name")
                ,p.getString("description").replace("+", " ")
                , "$ " + p.getString("price")
                ,"https://res.cloudinary.com/djlzeapiz/image/upload/v1573315852/" + p.getJSONArray ("image_array")[0].toString()))

            //Log.d("homeActivity", p.toString())
            swipe_refresh.isRefreshing = false

        }

        recycler_view.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recycler_view.adapter = MainAdapter(this, products)

    }


    /**private fun getCategories(jsonArr: JSONArray){

        for (i in 0 until jsonArr.length()){

            val item = jsonArr.getJSONObject(i)
            categories.add(Category(item.getString("image url"), item.getString("category")))

        }

        /**Place here the adapter*/
        category_layout.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        category_layout.adapter = CategoryAdapter(this, categories)

    }*/


    private fun handleCovers(){
        var currentIndex = 0
        val timeCounter: Runnable = object : Runnable {
            override fun run() {
                if(!coverViewPager!!.isFocused) {
                    if (currentIndex + 1 > covers.size) {
                        currentIndex = 0
                    } else {
                        currentIndex++
                    }
                    coverViewPager!!.currentItem = currentIndex
                    handler.postDelayed(this, 4 * 1000.toLong())
                }
            }
        }
        handler.postDelayed(timeCounter, 4 * 1000.toLong())
    }



    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.nav_home -> {

                menuOptions.onLoginOption(this)

            }

            R.id.sell_product -> {

                menuOptions.onSellProductOption(this)

            }

            R.id.get_at_cover -> {
                menuOptions.onSetCoverOption(this)
            }

            R.id.nav_share -> {

                menuOptions.onShareOption(this)

            }

            R.id.nav_notifications -> {

                menuOptions.onNotificationsOption(this)

            }

            R.id.nav_send -> {

            }

            R.id.nav_dev -> {

            }
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
