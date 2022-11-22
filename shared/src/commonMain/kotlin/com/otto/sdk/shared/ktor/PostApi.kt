package com.otto.sdk.shared.ktor

import com.otto.sdk.shared.response.Posts

interface PostApi {
  suspend fun getSinglePost() : Posts
}