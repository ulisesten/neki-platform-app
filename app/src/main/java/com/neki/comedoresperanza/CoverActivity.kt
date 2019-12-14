package com.neki.comedoresperanza

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_cover.*

class CoverActivity : AppCompatActivity() {

    val PIC_CROP_REQUEST = 1
    val PICK_IMAGE_REQUEST = 2
    var imagePreview: ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cover)

        cover_toolbar.setNavigationIcon(R.drawable.back_button)

        cover_toolbar.setNavigationOnClickListener {
            finish()
        }

        imagePreview = findViewById(R.id.image_preview)

        choose_image_button.setOnClickListener {
            imagePicker()
        }
    }

    private fun imagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra("crop", "true")
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            CropImage.activity(data.data!!).start(this)

        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                imagePreview?.setImageURI(result.uri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }
        }

    }
}
