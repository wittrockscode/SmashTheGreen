package com.smashthegreen.smashthegreen

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.reflect.Modifier

class MainActivity : AppCompatActivity() {




    private lateinit var startupAnim: AnimationDrawable
    private lateinit var soupAnimImage: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        STGTools.hideSystemBars(window, findViewById(android.R.id.content))
        STGTools.suppressBackButton(this, this, onBackPressedDispatcher)
        startAnimation()
        load()
    }

    private fun startAnimation(){
        soupAnimImage = findViewById<ImageView>(R.id.iwStartAnim).apply {
            setBackgroundResource(R.drawable.soup_anim)
            startupAnim = background as AnimationDrawable
        }
    }


    override fun onStart() {
        super.onStart()
        startupAnim.start()
    }


    private fun load(){
        val queue = Volley.newRequestQueue(this)
        val url = getString(R.string.server_ip) + getString(R.string.server_connectivity)

        val stringRequest = StringRequest(Request.Method.GET, url, {
            response -> if (response == "online") STGTools.IS_ONLINE = true
            checkExistingUser()
        }, {
            checkExistingUser()
        })
        queue.add(stringRequest)
    }


    private fun checkExistingUser(){

        val save = File(baseContext.filesDir, "stgSave.sv")

        ////////// Remove on production!
        if(save.exists()) save.delete()
        //////////

        if(save.exists()){
            STGTools.loadSaveState(this)
            if(SaveState.name == "" && SaveState.uuid == "00000000-0000-0000-0000-000000000000"){
                if(STGTools.IS_ONLINE) switchView(true)
                else switchView(false)
            }else switchView(false)
        }else{
            save.createNewFile()
            val gson = GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create()
            val newUserSaveString: String = gson.toJson(SaveState)
            save.writeText(newUserSaveString)
            if(STGTools.IS_ONLINE) switchView(true)
            else switchView(false)
        }

    }


    private fun switchView(newUser: Boolean){

        STGTools.sendSaveState(this, arrayOf(0,1,2,3), true)

        if(newUser){
            startActivity(Intent(this, SignUpActivity::class.java))
        }else{
            startActivity(Intent(this, MainMenuActivity::class.java))
        }
    }





}


