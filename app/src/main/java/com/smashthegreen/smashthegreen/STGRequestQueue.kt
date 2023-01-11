package com.smashthegreen.smashthegreen

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class STGRequestQueue(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: STGRequestQueue? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: STGRequestQueue(context).also {
                    INSTANCE = it
                }
            }
    }


    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
    private fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }

    fun postReq(url:String, body:String, onResponse: (response:String) -> Unit,
                  onError: (error: VolleyError) -> Unit){
        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                onResponse.invoke(response)
            },
            Response.ErrorListener { error ->
                onError.invoke(error)
            }){
            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded"
            }
            override fun getBody(): ByteArray {
                return body.toByteArray()
            }
        }
        addToRequestQueue(stringRequest)
    }

    fun putReq(url:String, body:MutableMap<String, String>?, onResponse: (response:String) -> Unit,
                onError: (error: VolleyError) -> Unit){
        val stringRequest = object : StringRequest(Method.PUT, url,
            Response.Listener { response ->
                onResponse.invoke(response)
            },
            Response.ErrorListener { error ->
                onError.invoke(error)
            }){
            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded"
            }

            override fun getParams(): MutableMap<String, String>? {
                return body
            }
        }
        addToRequestQueue(stringRequest)
    }

}