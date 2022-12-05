package com.otto.sdk.shared

import com.otto.sdk.shared.localData.UserAuth

object Constants {

 object staging {
    var Base_URL : String = "testStaging"
    fun Menu_URL(phone:String) : String = "https://phoenix-imkas.ottodigital.id/ppob?phoneNumber=${phone}"
  }

  object production{
    var Base_URL : String = "testProduction"
    fun Menu_URL(phone:String) : String = "https://phoenix-imkas.ottodigital.id/ppob?phoneNumber=${phone}"
  }

  var environment = staging

}