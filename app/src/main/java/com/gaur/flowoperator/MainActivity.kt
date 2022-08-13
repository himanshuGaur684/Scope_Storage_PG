package com.gaur.flowoperator

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.BitmapCompat
import com.gaur.flowoperator.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!


    private val cameraResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val bitmap = it.data?.extras?.get("data") as Bitmap
        storeBitmap(bitmap)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnCapture.setOnClickListener {
            openCamera()
        }


    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResult.launch(intent)
    }

    private fun storeBitmap(bitmap: Bitmap){
        val contentResolver = this.contentResolver
        val imageCollection = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }else{
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME,"${Date().time}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                put(MediaStore.Images.Media.IS_PENDING,1)
                // save your file to a specific location ( use this below comment line )
//                put(MediaStore.Images.Media.RELATIVE_PATH,"DCIM/new capture")
            }
        }

        val imageUri = contentResolver.insert(imageCollection,contentValues)
        imageUri?.let {
            val outputStream = contentResolver.openOutputStream(it)
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream)
            outputStream?.close()
            contentValues.clear()
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                contentValues.put(MediaStore.Images.Media.IS_PENDING,0)
            }
            contentResolver.update(it,contentValues,null,null)
            outputStream?.close()
        }




    }


}
