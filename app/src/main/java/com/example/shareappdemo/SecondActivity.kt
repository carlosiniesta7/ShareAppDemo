package com.example.shareappdemo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_second.*
import java.io.File
import java.io.FileOutputStream

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        shareTextButton.setOnClickListener {
            val shareText = editText.text.toString()
            val shareTextIntent = Intent()
            shareTextFun(shareTextIntent, shareText)
        }

        shareImageButton.setOnClickListener{
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.tetrix)
            val file = File(externalCacheDir, "splash.png")
            val shareImageIntent = Intent()
            shareImageFun(file, bitmap, shareImageIntent)
        }
    }

    // ---------------- SHARE FUNCTIONS ---------------

    private fun shareTextFun(intentShareText: Intent, shareText: String) {
        intentShareText.action = Intent.ACTION_SEND
        intentShareText.type = "text/plain"
        intentShareText.putExtra(Intent.EXTRA_TEXT, shareText)
        intentShareText.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT HERE")
        startActivity(Intent.createChooser(intentShareText, "Share Text via"))
    }

    private fun shareImageFun(
        file: File,
        bitmap: Bitmap,
        shareImageIntent: Intent
    ) {
        val bmpUri = FileProvider.getUriForFile(this, "com.example.shareappdemo.fileprovider", file)
        val fOut = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
        fOut.flush()
        fOut.close()

        shareImageIntent.action = Intent.ACTION_SEND
        shareImageIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareImageIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
        shareImageIntent.type = "image/png"
        shareImageIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        startActivity(Intent.createChooser(shareImageIntent, "Share Image via"))
    }
    // END ---------------- SHARE FUNCTIONS ---------------
}