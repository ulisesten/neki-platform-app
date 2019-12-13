package com.neki.comedoresperanza.utils

import android.app.Activity
import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class HttpAuth {

    fun auth(context: Context, activity: Activity, url: String, json: JSONObject, cb:(response: Boolean) -> Unit ){

        val jsonObjectRequest = JsonObjectRequest( Request.Method.POST, url, json,
            Response.Listener { response ->

                //Log.d("res",response.toString())

                storeCredentials(activity,
                    response.getString("id"),
                    response.getString("username"),
                    response.getString("credentials"))

                cb(true)

            },

            Response.ErrorListener { error ->

                val networkResponse =  error.networkResponse

                if (networkResponse != null && networkResponse.statusCode != 200 ) {
                    // HTTP Status Code: 401 Unauthorized
                    cb(false)
                }

            })

        val queue = Volley.newRequestQueue(context)
        queue!!.add(jsonObjectRequest)

    }



    /**========================*/
    private fun storeCredentials(activity: Activity, seller_id:String, seller_name: String, session_token: String ){

        val prefs = activity.getSharedPreferences("storage", Context.MODE_PRIVATE ).edit()

        prefs.putString("seller_id", seller_id)
        prefs.putString("seller_name", seller_name)
        prefs.putString("session_token",session_token)
        prefs.apply()

    }
}