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
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private var _binding:ActivityMainBinding?=null
    private val binding:ActivityMainBinding
    get() = _binding!!


    private val createFile = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        it.data?.data?.let {
            createFile(it)
        }
    }

    private val readFile = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        it.data?.data?.let {
           binding.tvFileContent.text =  readFile(it)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            createFile()
        }

        binding.btnRead.setOnClickListener {
            readFile()
        }


    }


    fun readFile(){
        val intent =Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type="text/*"
        readFile.launch(intent)
    }
    fun readFile(uri:Uri):String{
        return try {
            val inputStream = this.contentResolver.openInputStream(uri)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val line = StringBuilder()
            do {
                val l = bufferedReader.readLine()
                l?.let {
                    line.append(l)
                }
            }while (l!=null)
            line.toString()
        }catch (e:Exception){
            e.printStackTrace()
            ""
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
