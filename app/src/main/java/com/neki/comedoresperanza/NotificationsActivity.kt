package com.neki.comedoresperanza

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.neki.comedoresperanza.utils.*
import kotlinx.android.synthetic.main.activity_notifications.*
import org.json.JSONArray


class NotificationsActivity : AppCompatActivity() {

    private var notifications: MutableList<Message> = mutableListOf()
    var adapter:NotificationsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        notifications_toolbar.setNavigationIcon(R.drawable.back_button)

        notifications_toolbar.setNavigationOnClickListener {
            finish()
        }

        val prefs = this.getSharedPreferences("storage", Context.MODE_PRIVATE)
        val userId = prefs.getString("seller_id", "")
        val userName = prefs.getString("seller_name", "")

        val http = Http()
        //val notifications = NotificationsHandler()
        http.getData(this,
                     this,
                         "https://open-backend.herokuapp.com/getNotifications?to_id=${userId}",
                             ::getNotifications)

        if(adapter == null)
            adapter = NotificationsAdapter(this, notifications)

        notification_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        notification_recycler.adapter = adapter

        //notificationsList = NotificationsHandler.getInstance().getMessageList()
        adapter!!.notifyDataSetChanged()

        refresh_notifications.setOnRefreshListener {

            notifications.clear()

            http.getData(this,
                this,
                "https://open-backend.herokuapp.com/getNotifications?to_id=${userId}",
                ::getNotifications)


        }
    }

    private fun getNotifications(jsonArr:JSONArray){

        var auxId = ""

        for (i in 0 until jsonArr.length()){

            val p = jsonArr.getJSONObject(i)

            if(auxId != p.getString("from_id")) {

                auxId = p.getString("from_id")

                notifications.add(
                    Message(
                          auxId
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

        }

        adapter!!.notifyDataSetChanged()
        refresh_notifications.isRefreshing = false

    }
}
