package com.otto.sdk.shared.kampkit.android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.otto.sdk.shared.interfaces.GeneralListener
import com.otto.sdk.shared.response.GeneralStatus
import com.otto.sdk.shared.response.MasterMenu
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
  }
}
