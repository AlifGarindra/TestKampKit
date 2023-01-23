package com.otto.sdk.shared.kampkit.android.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPref(val mContext:Context){

  private val sharedPreferences: SharedPreferences = mContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
  fun storeValue(key: String, value: String) {
    val editor = sharedPreferences.edit()
    editor.putString("access_token_"+key, value)
    editor.apply()
  }

  fun retrieveValue(key: String): String {
    var value = sharedPreferences.getString("access_token_"+ key, "")!!
    Log.d("test1234", "retrieveValue:$value ")
    return value
  }

  fun clearValue(key:String){
    val editor = sharedPreferences.edit()
    editor.remove("access_token_"+key)
    editor.apply()
  }

  fun clearSharedPref(){
    val editor = sharedPreferences.edit()
    editor.clear()
    editor.apply()
  }
}