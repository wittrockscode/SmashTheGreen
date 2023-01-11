package com.smashthegreen.smashthegreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        STGTools.hideSystemBars(window, findViewById(android.R.id.content))
        STGTools.suppressBackButton(this, this, onBackPressedDispatcher)

        findViewById<EditText>(R.id.etUsername).apply {
            setOnEditorActionListener { textView, i, _ ->
                return@setOnEditorActionListener when (i) {
                    EditorInfo.IME_ACTION_DONE -> {
                        checkNameAvailability(textView.text.toString().trim())
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun checkNameAvailability(name: String) {
        val url = getString(R.string.server_ip) + getString(R.string.login)
        STGRequestQueue.getInstance(this).postReq(url, STGTools.toReqBody(arrayOf("name", name)),
            {response ->
                val result = STGTools.assertJsonKeyTrue(response, "created") {
                    //TODO:
                    //Handle server offline, start in offline mode
                }

                if(result){
                    SaveState.name = name
                    SaveState.uuid = STGTools.getValueFromJsonString(response, "uuid")
                    STGTools.saveSaveState(this)
                    startActivity(Intent(this, MainMenuActivity::class.java))

                }else{
                    findViewById<EditText>(R.id.etUsername).setBackgroundResource(R.drawable.edittextbackgrounderror)
                    findViewById<TextView>(R.id.tvNameTaken).visibility = View.VISIBLE
                }

        }, {error ->
                Log.d("Error", error.toString())
                //TODO:
                //Handle server offline, start in offline mode
        })


    }
}