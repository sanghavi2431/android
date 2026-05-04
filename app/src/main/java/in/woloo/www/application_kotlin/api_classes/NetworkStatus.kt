package `in`.woloo.www.application_kotlin.api_classes

interface NetworkStatus {
    companion object {
        const val SUCCESS = 200
        const val JET_ENCRYPTOR_ERROR = 403
        const val CONTENT_EXPIRED = 402
        const val CONTENT_NOT_PURCHASED = 603
        const val API_FAILED = 400
        const val CARD_NOT_FOUND = 606
        const val SUCCESS_STR = "success"
    }
}
