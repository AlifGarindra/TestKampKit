package com.otto.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kotlin.jvm.functions.Function1;
import otto.com.sdk.MainFragment;
import otto.com.sdk.ui.data.MenuItem;

public class TheActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.the_activity_main);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.container, MainFragment.Companion.newInstance(new Function1<List<MenuItem>, List<MenuItem>>() {
            @Override
            public List<MenuItem> invoke(List<MenuItem> menuItems) {
              List<MenuItem> newItems = new ArrayList<>();
              newItems.addAll(Collections.singleton(menuItems.get(2)));
              return newItems;
            }
          }))
          .commitNow();
    }
  }
}