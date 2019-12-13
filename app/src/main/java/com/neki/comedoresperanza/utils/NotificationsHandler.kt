package com.neki.comedoresperanza.utils

class NotificationsHandler {

    private val messages: MutableList<Message> = mutableListOf()

    companion object {
        @Volatile
        private var INSTANCE: NotificationsHandler? = null
        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NotificationsHandler().also {
                    INSTANCE = it
                }
            }
    }

    fun setMessage(message:Message){
        messages.add(message)
    }

    fun getMessageList(): MutableList<Message> {
        return messages
    }

}