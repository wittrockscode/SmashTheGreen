package com.smashthegreen.smashthegreen

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

object SaveState {

    @SerializedName("name") var name: String = ""
    @SerializedName("uuid") var uuid: String = "00000000-0000-0000-0000-000000000000"
    @SerializedName("times") var times: Array<Long> = arrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    @SerializedName("times_endless") var times_endless: Array<Long> = arrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)
    @SerializedName("modes") var modes: Array<Boolean> = arrayOf(true,true,true,true,true,true,true,true,true,true,true)
    //@SerializedName("modes") var modes: Array<Boolean> = arrayOf(true,false,false,false,false,false,false,false,false,false,false)
    @SerializedName("friends") var friends: ArrayList<String> = ArrayList(0)
    @SerializedName("settings") var settings: Array<Boolean> = arrayOf(true, true)
    @SerializedName("selectedMode") var selectedMode: Int = 0
    @SerializedName("endless") var endless: Boolean = false



    override fun toString(): String {
        return "name: $name uuid: $uuid"
    }

    fun getTimesMap(modes: Array<Int>, isEndless: Boolean): MutableMap<String, String>{
        if(isEndless){
            val timesMap = HashMap<String, String>(modes.size)
            for(mode in modes){
                timesMap["tme${mode+1}"] = times_endless[mode].toString()
            }
            val finalMap = HashMap<String, String>(2)
            finalMap["times"] = Gson().toJson(timesMap)
            finalMap["endless"] = "true"
            finalMap["uuid"] = uuid
            return finalMap
        }else{
            Log.d("MODE_ORDER: ", modes.contentToString())
            val timesMap = HashMap<String, String>(modes.size)
            for(mode in modes){
                Log.d("MODE: ", mode.toString())
                timesMap["tm${mode+1}"] = times[mode].toString()
            }
            val finalMap = HashMap<String, String>(2)
            Log.d("JSON: ", Gson().toJson(timesMap))
            finalMap["times"] = Gson().toJson(timesMap)
            finalMap["endless"] = "false"
            finalMap["uuid"] = uuid
            return finalMap
        }
    }

}