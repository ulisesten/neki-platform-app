package com.neki.comedoresperanza.utils

import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONException
import org.json.JSONObject

class WebSocketActions {

    fun sendMessage( socket: Socket, userId:String, name: String, message: String, sellerId:String, productId:String ) {

        val obj = JSONObject()
        //val name = user

        try {

            obj.put("fromId",    userId)
            obj.put("name",      name)
            obj.put("message",   message)
            obj.put("toId",      sellerId)
            obj.put("productId", productId)

        } catch (e: JSONException) {

            e.printStackTrace()

        }

        socket.emit("message", obj)

    }

    fun onNewMessage(cb:(name:String, message:String) -> Unit): Emitter.Listener {
        return Emitter.Listener { args ->

            //createNotificationChannel(activity)

            val data = args[0] as JSONObject
            val mName: String
            var mType = "incoming"
            val mMessage: String
            val mImage: String

            //mName = data.getString("name")
            //mMessage = data.getString("message")
            //mImage = data.getString("imageUrl")

            cb("", "")

        }
    }

}