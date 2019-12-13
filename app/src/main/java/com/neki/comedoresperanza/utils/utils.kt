package com.neki.comedoresperanza.utils

class Utils {
    fun categoryTranslater(category:String):String {
        var res = ""
        when (category) {
            "computación" -> {
                res = "computation"
            }
            "música" -> {
                res = "music"
            }
            "libros" -> {
                res = "books"
            }
            "deportes" -> {
                res = "sports"
            }
            "ofertas" -> {
                res = "offers"
            }
            "mascotas" -> {
                res = "pets"
            }
            "más" -> {
                res = "more"
            }
        }

        return res

    }
}