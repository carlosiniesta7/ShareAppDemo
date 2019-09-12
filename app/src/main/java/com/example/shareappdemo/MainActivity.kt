package com.example.shareappdemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetWorldReadable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shareTextButton.setOnClickListener {
            val shareText = editText.text.toString()

            val intentShareText = Intent()
            intentShareText.action = Intent.ACTION_SEND
            intentShareText.type = "text/plain"
            intentShareText.putExtra(Intent.EXTRA_TEXT, shareText)
            intentShareText.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT HERE")
            startActivity(Intent.createChooser(intentShareText,"Share Text via" ))
        }

        shareImageButton.setOnClickListener{
            //val myDrawable = imageView.drawable
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.tetrix)

            // Save the bitmap to internal storage and get uri
            //val uri = bitmap.saveToInternalStorage(this)

            // Finally, share the internal storage saved bitmap
            //this.shareCacheDirBitmap(uri)


            // getExternalFilesDir() + "/Pictures" should match the declaration in fileprovider.xml paths
            // val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "splash.png")
            val file = File(externalCacheDir, "splash.png")

            // wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
            val bmpUri = FileProvider.getUriForFile(this, "com.example.shareappdemo.fileprovider", file)

            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
            file.setReadable(true, false)
            val shareImageIntent = Intent(Intent.ACTION_SEND)
            shareImageIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            shareImageIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
            shareImageIntent.type = "image/png"
            shareImageIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
            startActivity(Intent.createChooser(shareImageIntent,"Share Image via" ))
        }
    }
}

private fun MainActivity.shareCacheDirBitmap(uri: Uri) {
    val fis = FileInputStream(uri.path)  // 2nd line
    val bitmap = BitmapFactory.decodeStream(fis)
    fis.close()

    try {
        val file = File("${this.cacheDir}/drawing.png")
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
        val contentUri = FileProvider.getUriForFile(this, this.packageName + ".provider", file)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.type = "image/*"
        this.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
}

val View.bitmap: Bitmap
    get() {
        // Screenshot taken for the specified root view and its child elements.
        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.draw(canvas)
        return bitmap
    }

private fun Bitmap.saveToInternalStorage(context: Context): Uri {
    // Get the context wrapper instance
    val wrapper = ContextWrapper(context)

    // Initializing a new file
    // The bellow line return a directory in internal storage
    var file = wrapper.getDir("images", Context.MODE_PRIVATE)


    // Create a file to save the image, random file name
    //file = File(file, "${UUID.randomUUID()}.png")

    file = File(file, "image.png")

    try {
        // Get the file output stream
        val stream: OutputStream = FileOutputStream(file)

        // Compress bitmap
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)

        // Flush the stream
        stream.flush()

        // Close stream
        stream.close()
    } catch (e: IOException){ // Catch the exception
        e.printStackTrace()
    }

    // Return the saved image uri
    return Uri.parse(file.absolutePath)
}
