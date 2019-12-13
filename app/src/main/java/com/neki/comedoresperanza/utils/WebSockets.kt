package com.neki.comedoresperanza.utils

import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

class WebSocket{

    var mSocket: Socket? = null
    private var userId:String = String()

    companion object {
        @Volatile
        private var INSTANCE: WebSocket? = null
        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: WebSocket().also {
                    INSTANCE = it
                }
            }
    }

    fun createSocket(id:String) {

        val mUrl = "https://open-backend.herokuapp.com"

        try {
            val opts = IO.Options()
            opts.forceNew = true
            opts.reconnection = true
            opts.secure = true
            opts.query = "id=${id}"
            mSocket = IO.socket(mUrl,opts)
            mSocket!!.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

    }

    fun onNewMessage(cb:(fromId:String,
                         toId:String,
                         senderName:String,
                         receiverName:String,
                         message:String,
                         productId:String,
                         timestamp:String ) -> Unit): Emitter.Listener {

        return Emitter.Listener { args ->

            //createNotificationChannel(activity)

            val data = args[0] as JSONObject

            val senderName = data.getString("sender_name")
            val receiverName = data.getString("receiver_name")
            val message = data.getString("message")
            val fromId = data.getString("from_id")
            val toId = data.getString("to_id")
            val productId = data.getString("product_id")
            val timestamp = data.getString("timestamp")


            cb( fromId,
                toId,
                senderName,
                receiverName,
                message,
                productId,
                timestamp )

        }
    }

    fun sendMessage( socket: Socket, fromId:String, toId:String, senderName: String, receiverName:String, message: String, productId:String ) {

        val obj = JSONObject()

        try {

            obj.put("from_id",          fromId)
            obj.put("to_id",            toId)
            obj.put("sender_name",      senderName)
            obj.put("receiver_name",    receiverName)
            obj.put("message",          message)
            obj.put("product_id",       productId)

        } catch (e: JSONException) {

            e.printStackTrace()

        }

        socket.emit("message", obj)

    }


    fun getSocket():Socket? {
        return mSocket
    }

    fun setId(id:String){
        userId = id
    }

}