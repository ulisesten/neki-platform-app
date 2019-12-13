package com.neki.comedoresperanza

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.github.nkzawa.socketio.client.Socket
import com.neki.comedoresperanza.utils.*
import kotlinx.android.synthetic.main.activity_contacting_seller.*
import org.json.JSONArray

class ContactingSellerActivity : AppCompatActivity() {

    private val messages: MutableList<Message> = mutableListOf()
    private var userId:String? = null
    private var mProductId:String? = null

    var adapter:MessageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacting_seller)

        val prefs = this.getSharedPreferences("storage", Context.MODE_PRIVATE)
        userId = prefs.getString("seller_id",null)



        if( userId != null ) {

            val userName = intent.getStringExtra("userName")
            val sellerName = intent.getStringExtra("seller_name")
            val sellerId = intent.getStringExtra("seller_id")
            mProductId = intent.getStringExtra("productId")

            contacting_seller_toolbar.title = sellerName

            contacting_seller_toolbar.setNavigationIcon(R.drawable.back_button)

            contacting_seller_toolbar.setNavigationOnClickListener { finish() }

            /**WebSockets*/
            val sio = initSocket()!!


            /**Getting messages*/
            val http = Http()
            http
                .getData(
                    this
                    ,
                    this
                    ,
                    "https://open-backend.herokuapp.com/getMessages?from_id=${userId!!}&to_id=${sellerId}&product_id=${mProductId!!}"
                    ,
                    ::getMessages
                )

            contacting_seller_button.setOnClickListener {
                val message = contacting_seller_message.text.toString()

                if (message.isEmpty()) {
                    Toast.makeText(this, "El mensaje está vacío", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                WebSocket.getInstance().sendMessage(
                    sio
                    , userId!!
                    , sellerId
                    , userName!!
                    , sellerName
                    , message
                    , mProductId!!
                )

                contacting_seller_message.text.clear()

            }

            sio.on("message", WebSocket.getInstance().onNewMessage(::getNewMessage))

            if (adapter == null)
                adapter = MessageAdapter(userId!!, messages)

            message_recycler_view.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            message_recycler_view.adapter = adapter


        } else {

            val authIntent = Intent(this, LoginActivity::class.java)
            startActivity(authIntent)
            finish()

        }

    }

    private fun getNewMessage(fromId:String,
                              toId:String,
                              senderName:String,
                              receiverName:String,
                              message:String,
                              productId:String,
                              timestamp:String ){

        runOnUiThread {

            if(mProductId == productId)
                messages.add(Message(
                    fromId,
                    toId,
                    senderName,
                    receiverName,
                    message,
                    productId,
                    timestamp ))

            if(adapter == null)
                adapter = MessageAdapter(userId!!, messages)
            adapter!!.notifyDataSetChanged()

        }

    }

    private fun getMessages(jsonArr: JSONArray){
        runOnUiThread {

            for (i in 0 until jsonArr.length()) {

                val p = jsonArr.getJSONObject(i)
                messages.add(
                    Message(
                          p.getString("from_id")
                        , p.getString("to_id")
                        , p.getString("sender_name")
                        , p.getString("receiver_name")
                        , p.getString("message")
                        , p.getString("product_id")
                        , p.getString("timestamp")
                    )
                )

                //swipe_refresh.isRefreshing = false

            }

            if (adapter == null)
                adapter = MessageAdapter(userId!!, messages)

            adapter!!.notifyDataSetChanged()
        }
    }

    private fun initSocket(): Socket?{
        var sio = WebSocket.getInstance().getSocket()


        if(sio == null){

            userId = "unknown id"
            //userName = "unknown name"
            WebSocket.getInstance().createSocket(userId!!)
            sio = WebSocket.getInstance().getSocket()

        }

        return sio
    }
}
