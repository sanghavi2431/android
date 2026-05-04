package `in`.woloo.www.application_kotlin


object LoginFlowController {
    var otpVerified: Boolean = false
    var accessibilityTriggered: Boolean = false

    fun reset() {
        otpVerified = false
        accessibilityTriggered = false
    }
}