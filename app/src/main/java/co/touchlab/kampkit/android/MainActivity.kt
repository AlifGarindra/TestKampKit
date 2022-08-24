package co.touchlab.kampkit.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import otto.com.sdk.MainActivity

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    startActivity(Intent(this, MainActivity::class.java))
  }
}
