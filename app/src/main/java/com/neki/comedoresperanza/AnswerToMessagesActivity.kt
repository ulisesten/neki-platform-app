package com.neki.comedoresperanza

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.github.nkzawa.socketio.client.Socket
import com.neki.comedoresperanza.utils.Http
import com.neki.comedoresperanza.utils.Message
import com.neki.comedoresperanza.utils.MessageAdapter
import com.neki.comedoresperanza.utils.WebSocket
import kotlinx.android.synthetic.main.activity_answer_to_messages.*
import org.json.JSONArray

class AnswerToMessagesActivity : AppCompatActivity() {

    var adapter:MessageAdapter? = null
    var userId:String? = null
    var mProductId:String? = null
    private val messages:MutableList<Message> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_to_messages)

        val fromId = intent.getStringExtra("fromId")
        val toId = intent.getStringExtra("toId")
        val senderName = intent.getStringExtra("senderName")
        val receiverName = intent.getStringExtra("receiverName")
        val productId = intent.getStringExtra("productId")

        val prefs = this.getSharedPreferences("storage", Context.MODE_PRIVATE)
        userId = prefs.getString("seller_id", "")


        answering_to_message_toolbar.title = senderName
        answering_to_message_toolbar.setNavigationIcon(R.drawable.back_button)
        answering_to_message_toolbar.setNavigationOnClickListener {
            finish()
        }

        val sio = initSocket()!!
        val http = Http()

        mProductId = productId

        http.getData(
            this,
            this,
            "https://open-backend.herokuapp.com/getMessages?from_id=${fromId}&to_id=${toId}&product_id=${productId}",
            ::getMessages
        )

        answer_to_message_button.setOnClickListener {
            val message = answer_to_message_et.text.toString()

            if(message.isEmpty()){
                Toast.makeText(this,"El mensaje está vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /**The fields has been inverted*/
            WebSocket.getInstance().sendMessage(
                      sio
                    , userId!!
                    , fromId
                    , receiverName
                    , senderName
                    , message
                    , productId )

            answer_to_message_et.text.clear()

        }

        sio.on("message", WebSocket.getInstance().onNewMessage(::getNewMessage))

        if(adapter == null)
            adapter = MessageAdapter(userId!!, messages)

        answer_message_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        answer_message_recycler_view.adapter = adapter

        adapter!!.notifyDataSetChanged()

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

            /**Testing notification handler*/
            //NotificationsHandler.getInstance().setMessage(Notif("",name, message))

            if(adapter == null)
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
