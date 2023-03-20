package otto.com.sdk.static

object userTokenTask {
  var inProgress = false
  var userInfoRunning = false
  var failCounter = 4
  var failTimeStamp : Long? = null
}