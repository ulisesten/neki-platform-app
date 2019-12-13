package com.neki.comedoresperanza.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import org.json.JSONArray
import org.json.JSONObject
import com.cloudinary.android.callback.ErrorInfo
import com.neki.comedoresperanza.Info


class Http {




    fun setProduct(context:Context
                   , url:String
                   , images: ArrayList<Info>
                   , sellerId:String
                   , sellerName:String
                   , productName:String
                   , productCategory:String
                   , productDescription:String
                   , productPrice:String
                   , cb:(res: String, st: Boolean) -> Unit ){
        val queue = Volley.newRequestQueue(context)

        MediaManager.init(context)


        val body = JSONObject()
        val imageArray = JSONArray()

        for((i, el) in images.withIndex()){

            MediaManager.get().upload(el.uri)
                .unsigned("di0dbpqd")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        // your code here
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        // example code starts here
                        val progress = bytes.toDouble() / totalBytes
                        // post progress to app UI (e.g. progress bar, notification)
                        // example code ends here
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        // your code here
                        imageArray.put(i, resultData.get("public_id").toString())
                        //Log.d("result", resultData.get("public_id").toString())
                        if(i == images.size - 1){

                            body.put("seller_id", sellerId)
                            body.put("seller_name", sellerName)
                            body.put("product_name", productName)
                            body.put("image_array", imageArray)
                            body.put("category", productCategory)
                            body.put("description", productDescription)
                            body.put("price",productPrice)

                            val jsonObjectRequest = JsonObjectRequest(
                                Request.Method.POST
                                , url
                                , body
                                , Response.Listener{

                                    cb("Producto guardado corréctamente", true)

                                }

                                , Response.ErrorListener { error ->
                                    val networkResponse =  error.networkResponse

                                    if (networkResponse != null && networkResponse.statusCode != 200 ) {

                                        cb("Error al guardar el producto", false)

                                    }

                                })


                            queue.add(jsonObjectRequest)

                        }
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        // your code here
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        // your code here
                    }
                }).dispatch()

        }

    }




    fun getData(context: Context, activity: Activity, url: String, cb: (arr: JSONArray) -> Unit ){

        //val queue = Volley.newRequestQueue(context)
        val queue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonArrayRequest(
              Request.Method.GET
            , url
            , null
            , Response.Listener{ response ->

                cb(response)
                //Log.d("getProducts", response.toString() )

            }

            , Response.ErrorListener {

                //Log.d(TAG, it.toString())
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

            })


        queue.add(jsonObjectRequest)
    }




    fun getProductDetails(context: Context, activity: Activity, url: String, productId:String, cb: (arr: JSONObject) -> Unit ){

        val queue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET
            , url +"?id="+ productId
            , null
            , Response.Listener{ response ->

                cb(response)

            }

            , Response.ErrorListener {

                //Log.d(TAG, it.toString())
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

            })

        queue.add(jsonObjectRequest)
    }

}