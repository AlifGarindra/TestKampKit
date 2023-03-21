package com.otto.sdk.shared.kampkit.android

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.otto.sdk.shared.Constants
import com.otto.sdk.shared.interfaces.GeneralListener
import com.otto.sdk.shared.interfaces.UserInfoListener
import com.otto.sdk.shared.kampkit.android.data.LocalUserToken
import com.otto.sdk.shared.kampkit.android.data.PpobUser
import com.otto.sdk.shared.kampkit.android.data.SharedPref
import com.otto.sdk.shared.kampkit.android.http.PpobApi
import com.otto.sdk.shared.localData.ErrorStatus
import com.otto.sdk.shared.localData.GeneralStatus
import com.otto.sdk.shared.localData.UserAuth
import com.otto.sdk.shared.localData.UserInfoStatus
import otto.com.sdk.SDKManager

class MainActivity : AppCompatActivity() {

  lateinit var api: PpobApi
  val gson = Gson()
  var sharedPref: SharedPref? = null
  lateinit var userAccessTokenLabelSDK: TextView
  lateinit var clientTokenLabelSDK: TextView
  lateinit var userAccessTokenLabelApp: TextView
  lateinit var clientTokenLabelApp: TextView
  lateinit var phoneNumberLabel: TextView
  lateinit var outletNameLabel: TextView
  lateinit var phoneNumberInput: EditText
  lateinit var outletNameInput: EditText

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    Log.d("testsynchronous", "onCreate: ")
    if(Build.VERSION.SDK_INT > 19 ){
      api = PpobApi()
      setGeneralListener()
      onLabelSet()
      onPressButton()
      sharedPref = SharedPref(this@MainActivity)
    }
    // getUserInfo()
  }

  override fun onStart() {
    super.onStart()
    Log.d("testsynchronous", "onStart: ")
    if(Build.VERSION.SDK_INT > 19 ){
      api = PpobApi()
      setGeneralListener()
      onLabelSet()
      onPressButton()
      sharedPref = SharedPref(this@MainActivity)
    }
  }

  override fun onResume() {
    super.onResume()
    Log.d("testsynchronous", "onResume: ")
    if(Build.VERSION.SDK_INT > 19 ){
      api = PpobApi()
      setGeneralListener()
      onLabelSet()
      onPressButton()
      sharedPref = SharedPref(this@MainActivity)
    }
  }

  fun getUserInfo() {
    SDKManager.getInstance(this@MainActivity).userInfoListener(object : UserInfoListener {
      override fun onUserInfo(userInfo: UserInfoStatus) {
        runOnUiThread{
          val showError = Toast.makeText(this@MainActivity, userInfo.balance, Toast.LENGTH_SHORT)
          showError.show()
        }
      }
    })
  }

  fun onPressButton() {

    val clientTokenButtonApp: Button = findViewById(R.id.button_get_client_token)
    val userAccessTokenApp: Button = findViewById(R.id.button_get_user_access_token)
    val userAccessTokenExpiredApp: Button = findViewById(R.id.button_get_user_access_token_expired)
    val openPPOBButtonSdk: Button = findViewById(R.id.button_open_PPOB)
    val openActivationButton: Button = findViewById(R.id.button_open_activation)
    val phoneNumberButtonSdk: Button = findViewById(R.id.button_set_phone_number)
    val outletNameButtonSdk: Button = findViewById(R.id.button_set_outlet_name)
    val clientTokenButtonSdk: Button = findViewById(R.id.button_set_client_token)
    val userAccessTokenButtonSdk: Button = findViewById(R.id.button_set_user_access_token)
    val resetSessionButtonSdk: Button = findViewById(R.id.button_reset_session_sdk)
    val resetsessionButtonApp: Button = findViewById(R.id.button_reset_session_app)

    clientTokenButtonApp.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        api!!.getClientToken{
          PpobUser.clientToken = it
          refreshStateApp("ct")
        }
      }
    })

    userAccessTokenApp.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        var savedUserToken = sharedPref!!.retrieveValue(phoneNumberInput.text.toString())
        var localUserToken : LocalUserToken
        if(savedUserToken != ""){
          localUserToken = gson.fromJson(savedUserToken,LocalUserToken::class.java)
          PpobUser.userAccessToken = localUserToken.userAccessToken
          PpobUser.refreshToken = localUserToken.refreshToken
        }else{
          PpobUser.userAccessToken = ""
          PpobUser.refreshToken = ""
        }
        refreshStateApp("uat")
      }
    })

    userAccessTokenExpiredApp.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        api!!.refreshUserAccessToken(PpobUser.clientToken,PpobUser.refreshToken,{
          userToken, refreshToken ->
          var localUserToken = LocalUserToken(userToken,refreshToken)
          val jsonString = gson.toJson(localUserToken)
          sharedPref!!.storeValue(phoneNumberInput.text.toString(),jsonString)
          PpobUser.userAccessToken = userToken
          PpobUser.refreshToken = refreshToken
          refreshStateApp("uat")
          refreshStateSDK("uat")
        },{
          sharedPref!!.clearValue(phoneNumberInput.text.toString())
          PpobUser.userAccessToken = ""
          PpobUser.refreshToken = ""
          SDKManager.getInstance(this@MainActivity).setUserAccessToken()
          refreshStateApp("uat")
          refreshStateSDK("uat")
        })

      }
    })

    resetsessionButtonApp.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        PpobUser.reset()
        refreshStateApp()
      }
    })

    openActivationButton.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).setClientToken(clientTokenLabelSDK.text.toString()).openActivation(this@MainActivity)
        refreshStateSDK("uat")
        refreshStateApp("uat")
      }
    })

    openPPOBButtonSdk.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).setClientToken(clientTokenLabelSDK.text.toString()).setUserAccessToken(userAccessTokenLabelSDK.text.toString()).openPpob(this@MainActivity)
      }
    })


    phoneNumberButtonSdk.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).setPhoneNumber(phoneNumberInput.text.toString())
        refreshStateSDK("phone")
        refreshStateSDK("uat")
      }
    })

    outletNameButtonSdk.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).setOutletName(outletNameInput.text.toString())
        refreshStateSDK("outlet")
      }
    })

    clientTokenButtonSdk.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).setClientToken(PpobUser.clientToken)
        refreshStateSDK("ct")
      }
    })

    userAccessTokenButtonSdk.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).setUserAccessToken(PpobUser.userAccessToken)
        refreshStateSDK("uat")
      }
    })

    resetSessionButtonSdk.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).clearSDKSession()
        refreshStateSDK()
      }
    })
  }

  fun refreshStateSDK(state: String? = null) {
    runOnUiThread {
    if (state != null) {
      if (state == "phone") {
        phoneNumberLabel.text = UserAuth.phoneNumber
      }
      if (state == "outlet") {
        outletNameLabel.text = UserAuth.outletName
      }
      if (state == "ct") {
        clientTokenLabelSDK.text = UserAuth.clientToken
      }
      if (state == "uat") {
        userAccessTokenLabelSDK.text = UserAuth.userAccessToken
      }
    } else {
      phoneNumberLabel.text = UserAuth.phoneNumber
      outletNameLabel.text = UserAuth.outletName
      clientTokenLabelSDK.text = UserAuth.clientToken
      userAccessTokenLabelSDK.text = UserAuth.userAccessToken
    }
    }
  }

  fun refreshStateApp(state: String? = null) {
    runOnUiThread {
      if (state != null) {
        if (state == "ct") {
          clientTokenLabelApp.text = PpobUser.clientToken
        }
        if (state == "uat") {
          userAccessTokenLabelApp.text = PpobUser.userAccessToken
        }
      } else {
        clientTokenLabelApp.text = PpobUser.clientToken
        userAccessTokenLabelApp.text = PpobUser.userAccessToken
        phoneNumberInput.text.clear()
        outletNameInput.text.clear()
      }
    }
  }

  fun onLabelSet() {
    userAccessTokenLabelSDK = findViewById(R.id.text_user_access_token_sdk)
    clientTokenLabelSDK = findViewById(R.id.text_client_token_sdk)
    userAccessTokenLabelApp = findViewById(R.id.text_user_access_token_app)
    clientTokenLabelApp = findViewById(R.id.text_client_token_app)
    phoneNumberLabel = findViewById(R.id.text_phone_number)
    outletNameLabel = findViewById(R.id.text_outlet_name)
    phoneNumberInput = findViewById(R.id.input_phone_number)
    outletNameInput = findViewById(R.id.input_outlet_name)
  }

  fun setGeneralListener() {
    SDKManager.getInstance(this).generalListener = object : GeneralListener {
      override fun onOpenPPOB(status: GeneralStatus) {
        Log.d("test1234", "isopen?: ${status.state}")
      }

      override fun onClosePPOB(status: GeneralStatus) {
        Log.d("testsynchronous", "oncloseppob - getuserinfo 1")
        getUserInfo()
      }

      override fun onError(status: ErrorStatus) {
        Log.d("testsynchronous", "${status.message} ${status.code}")

      }

      override fun onClientTokenExpired() {
        Log.d("testsynchronous", "onclienttokenexpired")
        api!!.getClientToken{
          PpobUser.clientToken = it
          SDKManager.getInstance(this@MainActivity).setClientToken(PpobUser.clientToken)
          refreshStateApp("ct")
          refreshStateSDK("ct")
          if(PpobUser.userAccessToken != ""){
            Log.d("testsynchronous", "onclienttokenexpired - buka ppob")
              SDKManager.getInstance(this@MainActivity).openPpob(this@MainActivity)
          }else{
            Log.d("testsynchronous", "onclienttokenexpired - open aktivasi")
            SDKManager.getInstance(this@MainActivity).openActivation(this@MainActivity)
          }
        }
      }

      override fun onUserAccessTokenExpired() {
        val showError = Toast.makeText(this@MainActivity, "token expired!", Toast.LENGTH_SHORT)
        showError.show()
        Log.d("testsynchronous", "onuseraccesstokenexpired")
        api!!.refreshUserAccessToken(PpobUser.clientToken,PpobUser.refreshToken,{
            userToken, refreshToken ->
          var localUserToken = LocalUserToken(userToken,refreshToken)
          val jsonString = gson.toJson(localUserToken)
          sharedPref!!.storeValue(phoneNumberInput.text.toString(),jsonString)
          PpobUser.userAccessToken = userToken
          PpobUser.refreshToken = refreshToken
          SDKManager.getInstance(this@MainActivity).setUserAccessToken(PpobUser.userAccessToken)
          refreshStateApp("uat")
          refreshStateSDK("uat")
          SDKManager.getInstance(this@MainActivity).openPpob(this@MainActivity)
        },{
          sharedPref!!.clearValue(phoneNumberInput.text.toString())
          PpobUser.userAccessToken = ""
          PpobUser.refreshToken = ""
          SDKManager.getInstance(this@MainActivity).openActivation(this@MainActivity)
          refreshStateApp("uat")
          refreshStateSDK("uat")
        })
      }

      override fun onAuthCode(authCode: String) {
        api!!.generateUserAccessToken(clientToken = PpobUser.clientToken, phoneNumber = phoneNumberInput.text.toString(),authCode= authCode){
          userToken, refreshToken ->
          var localUserToken = LocalUserToken(userToken,refreshToken)
          val jsonString = gson.toJson(localUserToken)
          sharedPref!!.storeValue(phoneNumberInput.text.toString(),jsonString)
          PpobUser.userAccessToken = userToken
          PpobUser.refreshToken = refreshToken
          SDKManager.getInstance(this@MainActivity).setUserAccessToken(PpobUser.userAccessToken)
          refreshStateApp("uat")
          refreshStateSDK("uat")
          SDKManager.getInstance(this@MainActivity).openPpob(this@MainActivity)
        }
      }

      override fun onUserProfile(userInfo: UserInfoStatus) {
        Log.d("testsynchronous", "onuserprofile")
        runOnUiThread{
          val showError = Toast.makeText(this@MainActivity, UserInfoStatus.balance, Toast.LENGTH_SHORT)
          showError.show()
        }
        Log.d("test1234", "onUserProfile: ${userInfo.balance}")
      }

    }
  }
}
