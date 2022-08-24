package co.touchlab.kampkit.android.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.touchlab.kampkit.android.R
import co.touchlab.kampkit.android.ui.theme.KaMPKitTheme

@Composable
fun SortButton(
  @DrawableRes drawable: Int,
  @StringRes text: Int,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  )
  {
    Image(
      painter = painterResource(drawable),
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .size(88.dp)
        .clip(CircleShape)
    )
    Text(
      text = stringResource(text),
      style = MaterialTheme.typography.h3,
      modifier = Modifier.paddingFromBaseline(
        top = 24.dp, bottom = 8.dp
      )
    )
  }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun SortButtonPreview() {
  KaMPKitTheme {
    SortButton(
      text = R.string.ab1,
      drawable = R.drawable.ic_favorite_24px,
      modifier = Modifier.padding(8.dp)
    )
  }
}
