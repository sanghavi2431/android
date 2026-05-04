package `in`.woloo.www.application_kotlin.interfaces

interface OtpReceivedInterface {
    fun onOtpReceived(otp: String?)
    fun onOtpTimeout()
}
