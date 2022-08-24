package co.touchlab.kampkit.android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SortButtonRow(
  modifier: Modifier = Modifier,
) {
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = PaddingValues(horizontal = 16.dp),
    modifier = Modifier
  ) {
    // items(items) { item ->
    //   SortButton(drawable = item., text =)
    //
    // }
  }
}