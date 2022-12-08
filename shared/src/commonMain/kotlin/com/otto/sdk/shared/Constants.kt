package com.otto.sdk.shared

import com.otto.sdk.shared.localData.UserAuth

object Constants {

 object staging {
    var Base_URL : String = "testStaging"
    var Ppob_Domain : String = "https://phoenix-imkas.ottodigital.id"
   var Ppob_Menu_Slug : String = "/ppob"
    // fun Menu_URL(phone:String) : String = "https://phoenix-imkas.ottodigital.id/ppob?phoneNumber=${phone}"
  }

  object production{
    var Base_URL : String = "testProduction"
    var Ppob_Domain : String = "https://phoenix-imkas.ottodigital.id"
    var Ppob_Menu_Slug : String = "/ppob"
    // fun Menu_URL(phone:String) : String = "https://phoenix-imkas.ottodigital.id/ppob?phoneNumber=${phone}"
  }

  var environment = staging

}