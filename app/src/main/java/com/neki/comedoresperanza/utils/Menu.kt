package com.neki.comedoresperanza.utils

import android.content.Context
import android.content.Intent
import com.neki.comedoresperanza.LoginActivity
import com.neki.comedoresperanza.NotificationsActivity
import com.neki.comedoresperanza.SellActivity

class MenuOptions {

    fun onShareOption(context: Context){
        val shareIntent = Intent(Intent.ACTION_SEND)

        shareIntent.type = "text/plain"

        val shareBody = "https://play.google.com/store/apps/details?id=com.neki.mlvalles"

        shareIntent.putExtra(Intent.EXTRA_TITLE, "ML Ciudad Valles")
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)

        context.startActivity( Intent.createChooser(shareIntent,"Compartir mediante"))
    }

    fun onSellProductOption(context: Context){

        val newIntent = Intent(context, SellActivity::class.java)
        context.startActivity(newIntent)

    }

    fun onLoginOption(context: Context){

        val newIntent = Intent(context, LoginActivity::class.java)
        context.startActivity(newIntent)

    }

    fun onNotificationsOption(context:Context){
        val newIntent = Intent(context, NotificationsActivity::class.java)
        context.startActivity(newIntent)
    }
}