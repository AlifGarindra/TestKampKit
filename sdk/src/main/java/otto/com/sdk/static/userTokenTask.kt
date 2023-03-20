package otto.com.sdk.static

object userTokenTask {
  var inProgress = false
  var userInfoRunning = false
  var failCounter = 5
  var failTimeStamp : Long? = null
}