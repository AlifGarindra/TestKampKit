package com.otto.sdk.shared

import com.otto.sdk.shared.localData.UserAuth

object Constants {

  var isSandbox = true

 // object staging {
 //    var Base_URL : String = "testStaging"
 //    var Ppob_Domain : String = "https://phoenix-imkas.ottodigital.id"
 //   var Ppob_Menu_Slug : String = "/ppob"
 //    // fun Menu_URL(phone:String) : String = "https://phoenix-imkas.ottodigital.id/ppob?phoneNumber=${phone}"
 //  }
 //
 //  object production{
 //    var Base_URL : String = "testProduction"
 //    var Ppob_Domain : String = "https://phoenix-imkas.ottodigital.id"
 //    var Ppob_Menu_Slug : String = "/ppob"
 //    // fun Menu_URL(phone:String) : String = "https://phoenix-imkas.ottodigital.id/ppob?phoneNumber=${phone}"
 //  }

  object environtment {
    var Base_URL : String = if (isSandbox == true) "testStaging" else "testProduction"
    var Ppob_Domain : String = if (isSandbox == true) "https://phoenix-imkas.ottodigital.id" else "https://phoenix-imkas.ottodigital.id"
    var Ppob_Menu_Slug : String = if (isSandbox == true) "/ppob" else "/ppob"
  }

  // var environment = "production"

}