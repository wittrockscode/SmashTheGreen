package com.smashthegreen.smashthegreen

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LifecycleOwner
import com.google.gson.GsonBuilder
import org.json.JSONObject
import java.io.File
import java.lang.reflect.Modifier
import kotlin.math.roundToInt

class STGTools {


    companion object{
        var CURR_WIDTH: Int = 0
        var CURR_HEIGHT: Int = 0
        var IS_ONLINE: Boolean = false
        val STG_GREEN = Color.argb(255, 0, 255, 126)
        val STG_YELLOW = Color.argb(255, 248, 255, 59)
        val STG_BLUE = Color.argb(255, 0, 180, 255)
        val STG_RED = Color.argb(255, 255, 0, 126)
        private var backButtonTime = System.currentTimeMillis()

        val UNLOCK_MODE_TIMES: Array<Int> = arrayOf(1300, 1600, 2000, 2300, 3000, 3500, 4000, 4500, 5000, 5500)

        fun hideSystemBars(window: Window, view: View) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, view).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

        }

        fun fInt(i: Float): Int{
            return i.roundToInt()
        }

        fun toReqBody(vals: Array<String>): String{
            return if(vals.size%2 != 0) ""
            else{
                var finalString = ""
                for ((i, v) in vals.withIndex()){
                    finalString += if(i%2 == 1){
                        "=${v}"
                    }else{
                        "&${v}"
                    }
                }
                finalString
            }
        }


        fun assertJsonKeyTrue(jsonString: String, key: String, onMissing: (error: Unit?) -> Unit): Boolean{
            val jsonObject = JSONObject(jsonString)
            return if(jsonObject.has(key)){
                jsonObject.getBoolean(key)
            }else{
                onMissing.invoke(null)
                false
            }
        }

        fun getValueFromJsonString(jsonString: String, key: String): String{
            val jsonObject = JSONObject(jsonString)
            return jsonObject.getString(key)
        }


        fun loadSaveState(context: Context){
            val save = File(context.filesDir, "stgSave.sv")
            if(!save.exists()) return
            val saveString: String = save.readText()
            val gson = GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create()
            val saveNew: SaveState = gson.fromJson(saveString, SaveState::class.java)
            SaveState.name = saveNew.name
            SaveState.times = saveNew.times
            SaveState.modes = saveNew.modes
            SaveState.friends = saveNew.friends
            SaveState.settings = saveNew.settings
            SaveState.uuid = saveNew.uuid
            SaveState.selectedMode = saveNew.selectedMode
            SaveState.endless = saveNew.endless

        }

        fun suppressBackButton(lifecycleOwner: LifecycleOwner, componentActivity: ComponentActivity, onBackPressedDispatcher: OnBackPressedDispatcher){
            onBackPressedDispatcher.addCallback(
                lifecycleOwner ,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if(System.currentTimeMillis() - backButtonTime < 1800){
                            val intent = Intent(Intent.ACTION_MAIN)
                            intent.addCategory(Intent.CATEGORY_HOME)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            componentActivity.startActivity(intent)
                        }else{
                            backButtonTime = System.currentTimeMillis()
                        }
                    }
                }
            )
        }

        fun suppressBackButton(lifecycleOwner: LifecycleOwner, componentActivity: ComponentActivity, onBackPressedDispatcher: OnBackPressedDispatcher, context: Context){
            onBackPressedDispatcher.addCallback(
                lifecycleOwner ,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        val intent = Intent(context, MainMenuActivity::class.java)
                        componentActivity.startActivity(intent)
                    }
                }
            )
        }

        fun saveSaveState(context: Context){
            val save = File(context.filesDir, "stgSave.sv")
            val gson = GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create()
            save.writeText(gson.toJson(SaveState))
        }

        fun sendSaveState(context: Context, modes: Array<Int>, endless: Boolean){
            val url: String = context.getString(R.string.server_ip) + context.getString(R.string.update_times)
            STGRequestQueue.getInstance(context).putReq(url, SaveState.getTimesMap(modes, endless), {
                    response ->
                Log.d("RSPONSE", response)
            }, {
                    error ->
                Log.d("ERROR", error.toString())
            })

        }


    }

}