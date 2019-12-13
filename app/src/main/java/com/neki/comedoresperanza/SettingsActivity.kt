package com.neki.comedoresperanza

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.neki.comedoresperanza.utils.Settings
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settings_toolbar.setNavigationIcon(R.drawable.back_button)

        settings_toolbar.setNavigationOnClickListener {
            finish()
        }

        close_session_settings.setOnClickListener {
            val settings = Settings()

            settings.closeSession(this)
        }
    }
}
