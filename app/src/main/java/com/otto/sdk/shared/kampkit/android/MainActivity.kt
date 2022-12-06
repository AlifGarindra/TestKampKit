package com.otto.sdk.shared.kampkit.android

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.otto.sdk.shared.interfaces.GeneralListener
import com.otto.sdk.shared.localData.GeneralStatus
import com.otto.sdk.shared.localData.UserAuth
import otto.com.sdk.SDKManager

class MainActivity : AppCompatActivity() {

  lateinit var userAccessTokenLabel : TextView
  lateinit var clientTokenLabel : TextView
  lateinit var phoneNumberLabel : TextView
  lateinit var outletNameLabel : TextView
  lateinit var phoneNumberInput : EditText
  lateinit var outletNameInput : EditText

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
        Log.e("test1234", "onError:${status.state} ", )
        val showError = Toast.makeText(this@MainActivity,"${status.state}",Toast.LENGTH_SHORT)
        showError.show()
      }

      override fun onUserAccessTokenExpired() {
        SDKManager.getInstance(this@MainActivity).setOutletName("helo")
        Log.d("expireduat", "onUserAccessTokenExpired: ${UserAuth.outletName} ")
      }
    })

    onLabelSet()

    onPressButton()

  }

  fun onPressButton(){
    var openPPOBButton : Button = findViewById(R.id.button_open_PPOB)
    var phoneNumberButton : Button = findViewById(R.id.button_set_phone_number)
    var outletNameButton : Button = findViewById(R.id.button_set_outlet_name)
    var clientTokenButton : Button = findViewById(R.id.button_set_client_token)
    var userAccessTokenButton : Button = findViewById(R.id.button_set_user_access_token)

    // var clientKeyButton : Button = findViewById(R.id.button_set_client_key)

    openPPOBButton.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).openPpob(this@MainActivity)
        // SDKManager.getInstance(this@MainActivity).testingHOC { Log.d("hoc", "$it") }
//                SDKManager.getInstance(this@MainActivity).trySentry();
//               var hello =  SDKManager.getInstance(this@MainActivity).getBalancePPOB()
//                Log.e("error", "onClick: $hello", )
      }
  })

    phoneNumberButton.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).setPhoneNumber(phoneNumberInput.text.toString())
        phoneNumberLabel.text = UserAuth.phoneNumber
      }
    })

    outletNameButton.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).setOutletName(outletNameInput.text.toString())
        outletNameLabel.text = UserAuth.outletName
      }
    })

    clientTokenButton.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).setClientToken("abcdeefajsdbfjabsifasasfasf")
        clientTokenLabel.text = UserAuth.clientToken
      }
    })

    userAccessTokenButton.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).setUserAccessToken("osdgfiuasdtbgts8 8wdg f8asgdf9 asdfsa")
        userAccessTokenLabel.text = UserAuth.userAccessToken
      }
    })

  }

  fun onLabelSet(){
    userAccessTokenLabel = findViewById(R.id.text_user_access_token_sdk)
    clientTokenLabel = findViewById(R.id.text_client_token_sdk)
    phoneNumberLabel = findViewById(R.id.text_phone_number)
    outletNameLabel = findViewById(R.id.text_outlet_name)
    phoneNumberInput = findViewById(R.id.input_phone_number)
    outletNameInput = findViewById(R.id.input_outlet_name)
  }

}
