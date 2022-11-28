package com.otto.sdk.shared.kampkit.android

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.otto.sdk.shared.interfaces.GeneralListener
import com.otto.sdk.shared.response.GeneralStatus
import com.otto.sdk.shared.response.MasterMenu
import com.otto.sdk.shared.response.Posts
import com.otto.sdk.shared.response.UserAuth
import otto.com.sdk.MainFragment
import otto.com.sdk.SDKManager
import otto.com.sdk.ui.data.MenuItem

class MainActivity : AppCompatActivity() {


  // fun fx(items: List<MenuItem>): List<MenuItem> {
  //   return arrayListOf()
  // }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    // val testImportAja: MasterMenu.Item
    // if (savedInstanceState == null) {
    //   supportFragmentManager.beginTransaction()
    //     .replace(
    //       R.id.container, MainFragment.newInstance(
    //         itemsModifier = {
    //           arrayListOf(it.get(0))
    //         }
    //       )
    //     )
    //     .commitNow()
    // }


    SDKManager.getInstance(this).getPosts{
      Log.d("test123", "$it")
    }

    SDKManager.getInstance(this).setGeneralListener(object : GeneralListener {
     override fun onOpenPPOB(status: GeneralStatus) {
      Log.d("test1234", "onPageStarted: ${status.state}")
    }

      override fun onClosePPOB(status: GeneralStatus) {
        // TODO rewrite SDK BALANCE
      }

      override fun onError(status: GeneralStatus) {
        TODO("Not yet implemented")
      }
    })
    onPressButton()
    onSetLabel()
  }

  fun onPressButton(){
    var openPPOBButton : Button = findViewById(R.id.button_open_PPOB)
    var clientTokenButton : Button = findViewById(R.id.button_set_client_token)
    var userAccessTokenButton : Button = findViewById(R.id.button_set_user_access_token)
    openPPOBButton.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).openPPOB(this@MainActivity)
        // SDKManager.getInstance(this@MainActivity).testingHOC { Log.d("hoc", "$it") }
//                SDKManager.getInstance(this@MainActivity).trySentry();
//               var hello =  SDKManager.getInstance(this@MainActivity).getBalancePPOB()
//                Log.e("error", "onClick: $hello", )
      }
  })
    clientTokenButton.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).setClientToken("abcdeefajsdbfjabsifasasfasf")
        onSetLabel()
      }
    })
    userAccessTokenButton.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).setUserAccessToken("osdgfiuasdtbgts8 8wdg f8asgdf9 asdfsa")
        onSetLabel()
      }
    })
  }

  fun onSetLabel(){
    var clientTokenLabel : TextView = findViewById(R.id.text_client_token)
    var useraccessTokenLabel : TextView = findViewById(R.id.text_user_access_token)
    clientTokenLabel.setText(UserAuth.clientToken)
    useraccessTokenLabel.setText(UserAuth.userAccessToken)

  }
}
