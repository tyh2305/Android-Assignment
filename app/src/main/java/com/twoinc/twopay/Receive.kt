package com.twoinc.twopay

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.google.common.io.Resources.getResource
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException


@Suppress("DEPRECATION")
class Receive : AppCompatActivity() {
    val TAG = "FragmentActivity"
    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive)

        val bitmap = generateQRCode("twopay--userid=uXCBY6uZNRxiueY1fk3m")
        val qrcodeLocator:ImageView = findViewById<ImageView>(R.id.qrcodeHolder)
//        qrcodeLocator.setImageBitmap(bitmap)
        val logoDraw = resources.getDrawable(R.drawable.ic_logo_subtract,theme)
        val logoBit = logoDraw.toBitmap(width = 50,height = 50,config = null)
        val qrcode_bitmap: Bitmap = generateQRCode("twopay>userid=uXCBY6uZNRxiueY1fk3m")
        val merged :Bitmap = mergeBitmaps(logoBit, qrcode_bitmap);
        qrcodeLocator.setImageBitmap(merged)
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    @SuppressLint("ResourceAsColor")
    fun generateQRCode(text: String): Bitmap{
        val width =  1500
        val height = 1500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()

        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until height){
                for(y in 0 until width){
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }   catch (e: WriterException){
            Log.d(TAG, "generateQRCOde : ${e.message}")
        }
        return bitmap
    }

    fun mergeBitmaps(logo: Bitmap?, qrcode: Bitmap): Bitmap {
        val combined = Bitmap.createBitmap(qrcode.width, qrcode.height, qrcode.config)
        val canvas = Canvas(combined)
        val canvasWidth: Int = canvas.getWidth()
        val canvasHeight: Int = canvas.getHeight()

        canvas.drawBitmap(qrcode, Matrix(), null)
        val resizeLogo = Bitmap.createScaledBitmap(logo!!, canvasWidth / 8, canvasHeight / 8, true)
        val centreX:Float = ((canvasWidth - resizeLogo.width) / 2).toFloat()
        val centreY:Float  = ((canvasHeight - resizeLogo.height) / 2).toFloat()
        canvas.drawBitmap(resizeLogo, centreX, centreY, null)
        return combined
    }




}


