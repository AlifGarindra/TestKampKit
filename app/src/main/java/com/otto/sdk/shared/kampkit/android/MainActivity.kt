package com.otto.sdk.shared.kampkit.android

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.otto.sdk.shared.interfaces.GeneralListener
import com.otto.sdk.shared.response.GeneralStatus
import com.otto.sdk.shared.response.MasterMenu
import com.otto.sdk.shared.response.Posts
import otto.com.sdk.MainFragment
import otto.com.sdk.SDKManager
import otto.com.sdk.ui.data.MenuItem

class MainActivity : AppCompatActivity() {


  fun fx(items: List<MenuItem>): List<MenuItem> {
    return arrayListOf()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val testImportAja: MasterMenu.Item
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(
          R.id.container, MainFragment.newInstance(
            itemsModifier = {
              arrayListOf(it.get(0))
            }
          )
        )
        .commitNow()
    }
    Log.e("NNN", SDKManager.getInstance(this).x)

    SDKManager.getInstance(this).getPosts{
      Log.d("test123", "$it")
    }

    SDKManager.getInstance(this).setGeneralListener(object : GeneralListener {
     override fun onOpenPPOB(status: GeneralStatus) {
      Log.d("test1234", "onPageStarted: ${status.state}")
    }

      override fun onClosePPOB(status: GeneralStatus) {
        var balance : String = SDKManager.getInstance(this@MainActivity).getBalancePPOB()
        // TODO rewrite SDK BALANCE
        Log.d("test1234", "onPageStarted: $balance")
      }

      override fun onError(status: GeneralStatus) {
        TODO("Not yet implemented")
      }
    })
    onPressButton();
  }

  fun onPressButton(){
    var button : Button = findViewById(R.id.button1)
    button.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).openPPOB("www.google.com",this@MainActivity)
        SDKManager.getInstance(this@MainActivity).testingHOC { Log.d("hoc", "$it") }
//                SDKManager.getInstance(this@MainActivity).trySentry();
//               var hello =  SDKManager.getInstance(this@MainActivity).getBalancePPOB()
//                Log.e("error", "onClick: $hello", )
      }
  })
  }
}
