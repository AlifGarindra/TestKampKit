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
import com.otto.sdk.shared.interfaces.UserInfoListener
import com.otto.sdk.shared.kampkit.android.data.PpobUser
import com.otto.sdk.shared.kampkit.android.http.PpobApi
import com.otto.sdk.shared.localData.ErrorStatus
import com.otto.sdk.shared.localData.GeneralStatus
import com.otto.sdk.shared.localData.UserAuth
import com.otto.sdk.shared.localData.UserInfoStatus
import otto.com.sdk.SDKManager

class MainActivity : AppCompatActivity() {

  val api: PpobApi = PpobApi()

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
    setGeneralListener()
    onLabelSet()
    onPressButton()
    getUserInfo()
  }

  fun getUserInfo() {
    SDKManager.getInstance(this@MainActivity).userInfoListener(object : UserInfoListener {
      override fun onUserInfo(userInfo: UserInfoStatus) {
        Log.d("userInfoList", "onUserInfo:${userInfo.balance} ")
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
        api.getClientToken()
        PpobUser.clientToken = "aea709c5-90e8-3bcd-a3e3-e952f3f31558"
        refreshStateApp("ct")
      }
    })

    userAccessTokenApp.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        PpobUser.userAccessToken = "x-user-access-token"
        refreshStateApp("uat")
      }
    })
    userAccessTokenExpiredApp.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        PpobUser.userAccessToken = "expired-user-access-token"
        refreshStateApp("uat")
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
        SDKManager.getInstance(this@MainActivity).openActivation(this@MainActivity)
      }
    })

    openPPOBButtonSdk.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).openPpob(this@MainActivity)
        // SDKManager.getInstance(this@MainActivity).testingHOC { Log.d("hoc", "$it") }
//                SDKManager.getInstance(this@MainActivity).trySentry();
//               var hello =  SDKManager.getInstance(this@MainActivity).getBalancePPOB()
//                Log.e("error", "onClick: $hello", )
      }
    })


    phoneNumberButtonSdk.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        SDKManager.getInstance(this@MainActivity).setPhoneNumber(phoneNumberInput.text.toString())
        refreshStateSDK("phone")
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

  fun refreshStateApp(state: String? = null) {
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
      }

      override fun onError(status: ErrorStatus) {
        val showError = Toast.makeText(this@MainActivity, status.message, Toast.LENGTH_SHORT)
        showError.show()
      }

      override fun onClientTokenExpired() {
      }

      override fun onUserAccessTokenExpired() {
        val showError = Toast.makeText(this@MainActivity, "token expired!", Toast.LENGTH_SHORT)
        showError.show()
        PpobUser.userAccessToken = "x-user-access-token"
        SDKManager.getInstance(this@MainActivity).setUserAccessToken(PpobUser.userAccessToken)
        refreshStateApp("uat")
        refreshStateSDK("uat")
      }

      override fun onAuthCode(authCode: String) {
        PpobUser.userAccessToken = "x-user-access-token"
        SDKManager.getInstance(this@MainActivity).setUserAccessToken(PpobUser.userAccessToken)
        refreshStateApp("uat")
        refreshStateSDK("uat")
      }

      override fun onUserProfile(userInfo: UserInfoStatus) {
        //isi
      }
    }
  }
}
