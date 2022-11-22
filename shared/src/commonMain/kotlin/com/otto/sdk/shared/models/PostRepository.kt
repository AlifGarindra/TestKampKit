package com.otto.sdk.shared.models

import co.touchlab.stately.ensureNeverFrozen
import com.otto.sdk.shared.ktor.PostApi
import com.otto.sdk.shared.response.Posts
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class PostRepository(
  private val postApi : PostApi
) {

  init {
    ensureNeverFrozen()
  }

// suspend fun fetchFirstPost() : Posts {
//   val singlePost : Posts =  postApi.getSinglePost()
//   return singlePost
// }

 fun fetchFirstPost() : Posts {
  val asyncPosts : Posts =  runBlocking {
    var post = async {
      postApi.getSinglePost()
    }
    post.await()
  }
  return asyncPosts
}

}