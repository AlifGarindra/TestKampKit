package otto.com.sdk.ui

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import otto.com.sdk.ui.data.PpobMenuModel
import otto.com.sdk.R
import otto.com.sdk.ui.screen.WebViewKt

@Composable
fun MenuItemView(
  item: PpobMenuModel,
  showIcon: Boolean = true
) {
  val mContext = LocalContext.current
  Card(
    modifier = Modifier
      // .wrapContentSize(Alignment.Center, false)
      .clickable {
        var intent = Intent(mContext, WebViewKt::class.java)
        // intent.putExtra("openURL", PpobDataObject.url)
        mContext.startActivity(intent);
      },
    backgroundColor = Color.Transparent,
    elevation = 0.dp,
  ) {
    Column(
      modifier = Modifier
        .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
      if (showIcon) {
        // Icon(
        //   painter = painterResource(id = item.imageRes),
        //   contentDescription = "iconRes",
        //   tint = Color.,
        //   modifier = Modifier
        //     .padding(top = 8.dp)
        //     .size(height = 48.dp, width = 48.dp)
        //     .align(Alignment.CenterHorizontally)
        // )
        Image(
          painter = painterResource(id = item.imageRes),
          contentDescription = "",
          modifier = Modifier
            .padding(top = 8.dp)
            .size(height = 48.dp, width = 48.dp)
            .align(Alignment.CenterHorizontally)
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = item.title,
        textAlign = TextAlign.Center,
        maxLines = 2,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      )
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridMenuView(
  listOfMenu: List<PpobMenuModel> = arrayListOf(),
  showIcon: Boolean = true
) {
  LazyVerticalGrid(
    cells = GridCells.Fixed(4),
    contentPadding = PaddingValues(top = 25.dp),
    modifier = Modifier.padding(horizontal = 10.dp)
  ) {
    items(listOfMenu.size) { number ->
      val menu = listOfMenu[number]
      MenuItemView(menu, showIcon)
    }
  }
}

@Preview(showBackground = true)
@Composable
fun MenuItemViewPreview(showIcon: Boolean = true) {
  MenuItemView(
    item = PpobMenuModel(
      title = "Pendanaan",
      imageRes = R.drawable.ic_isimple_pinjaman,
      rank = 1,
      url = ""
    ),
    showIcon
  )
}

@Preview(showBackground = true)
@Composable
fun MenuItemViewNoIconPreview() {
  MenuItemViewPreview(false)
}

@Preview(showBackground = true)
@Composable
fun GridPreview(showIcon: Boolean = true) {
  val listOfMenu = arrayListOf(
    PpobMenuModel("Asuransi", R.drawable.ic_wysiwyg, 0, ""),
    PpobMenuModel("BPJS", R.drawable.ic_isimple_bpjs, 0, ""),
    PpobMenuModel("Voucher Game", R.drawable.ic_isimple_game, 0, ""),
    PpobMenuModel("Wallet", R.drawable.ic_isimple_wallet, 0, ""),
    PpobMenuModel("Pulsa", R.drawable.ic_isimple_telepon, 0, ""),
    PpobMenuModel("Pinjaman", R.drawable.ic_isimple_pinjaman, 0, ""),
  )
  GridMenuView(listOfMenu, showIcon)
}

@Preview(showBackground = true)
@Composable
fun GridNoTitlePreview() {
  GridPreview(false)
}