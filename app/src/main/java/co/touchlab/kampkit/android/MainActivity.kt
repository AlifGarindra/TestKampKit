package co.touchlab.kampkit.android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
  }
}
