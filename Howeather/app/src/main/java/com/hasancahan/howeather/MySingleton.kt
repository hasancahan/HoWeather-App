package com.hasancahan.howeather

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley

/**
 * Created by thegh on 5.12.2020.
 */

class MySingleton private constructor(context: Context) {
    private var requestQueue: RequestQueue? = null


    init {
        ctx = context
        requestQueue = getRequestQueue()


    }

    fun getRequestQueue(): RequestQueue? {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx?.applicationContext)
        }
        return requestQueue
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        getRequestQueue()?.add(req)
    }

    companion object {
        private var instance: MySingleton? = null
        private var ctx: Context? =null

        @Synchronized
        fun getInstance(context: Context?): MySingleton? {
            if (instance == null) {
                instance = MySingleton(context!!)
            }
            return instance
        }
    }
}