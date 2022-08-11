package com.gaur.flowoperator

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gaur.flowoperator.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var _binding:ActivityMainBinding?=null
    private val binding:ActivityMainBinding
    get() = _binding!!


    private val createFile = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        it.data?.data?.let {
            createFile(it)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            createFile()
        }


    }

    fun createFile(){
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TITLE,binding.edFileName.text.toString())
        createFile.launch(intent)
    }

    fun createFile(uri:Uri){
        try {
            val parcelFileDescriptor = this.contentResolver.openFileDescriptor(uri,"w")
            val fileOutputStream = FileOutputStream(parcelFileDescriptor?.fileDescriptor)
            fileOutputStream.write(binding.edFileContent.text.toString().toByteArray())
            fileOutputStream.close()
            parcelFileDescriptor?.close()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }


}
