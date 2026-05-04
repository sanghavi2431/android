package `in`.woloo.www.application_kotlin.model.lists_models

class ResultVtionSdkModel {
    var mobileNumber: String? = null
    @kotlin.jvm.JvmField
    public var result: String? = null
    var status: String? = null
    var deleteRequest: Boolean? = null

    constructor()
    constructor(mobileNumber: String?, result: String?, deleteRequest: Boolean?) {
        this.mobileNumber = mobileNumber
        this.result = result
        this.deleteRequest = deleteRequest
    }
}
