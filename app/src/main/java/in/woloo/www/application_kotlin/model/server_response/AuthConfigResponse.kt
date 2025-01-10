package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthConfigResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("URLS")
        @Expose
        private var uRLS: URLS? = null

        @SerializedName("CUSTOM_MESSAGE")
        @Expose
        private var cUSTOMMESSAGE: CUSTOMMESSAGE? = null

        @SerializedName("APP_VERSION")
        @Expose
        private var aPPVERSION: APPVERSION? = null

        @SerializedName("MAINTENANCE_SETTINGS")
        @Expose
        private var mAINTENANCESETTINGS: MAINTENANCESETTINGS? = null

        @SerializedName("BLOCK_APP")
        @Expose
        private var bLOCKAPP: BLOCKAPP? = null

        @SerializedName("RZ_CRED")
        @Expose
        private var rZCRED: RZCRED? = null

        @SerializedName("GOOGLE_MAPS")
        @Expose
        var google_maps: GOOGLE_MAPS? = null

        @SerializedName("free_trial_period_days")
        @Expose
        var freeTrialPeriodDays = "0"

        @SerializedName("free_trial_text")
        @Expose
        var freeTrialText = "0"

        @SerializedName("SUPPORT_EMAIL")
        @Expose
        var supportEmail: SUPPORT_EMAIL? = null
        fun getuRLS(): URLS? {
            return uRLS
        }

        fun setuRLS(uRLS: URLS?) {
            this.uRLS = uRLS
        }

        fun getcUSTOMMESSAGE(): CUSTOMMESSAGE? {
            return cUSTOMMESSAGE
        }

        fun setcUSTOMMESSAGE(cUSTOMMESSAGE: CUSTOMMESSAGE?) {
            this.cUSTOMMESSAGE = cUSTOMMESSAGE
        }

        fun getaPPVERSION(): APPVERSION? {
            return aPPVERSION
        }

        fun setaPPVERSION(aPPVERSION: APPVERSION?) {
            this.aPPVERSION = aPPVERSION
        }

        fun getmAINTENANCESETTINGS(): MAINTENANCESETTINGS? {
            return mAINTENANCESETTINGS
        }

        fun setmAINTENANCESETTINGS(mAINTENANCESETTINGS: MAINTENANCESETTINGS?) {
            this.mAINTENANCESETTINGS = mAINTENANCESETTINGS
        }

        fun getbLOCKAPP(): BLOCKAPP? {
            return bLOCKAPP
        }

        fun setbLOCKAPP(bLOCKAPP: BLOCKAPP?) {
            this.bLOCKAPP = bLOCKAPP
        }

        fun getrZCRED(): RZCRED? {
            return rZCRED
        }

        fun setrZCRED(rZCRED: RZCRED?) {
            this.rZCRED = rZCRED
        }
    }

    inner class URLS {
        @SerializedName("about_url")
        @Expose
        var aboutUrl: String? = null

        @SerializedName("terms_url")
        @Expose
        var terms_url: String? = null

        @SerializedName("app_share_url")
        @Expose
        var app_share_url: String? = null

        @SerializedName("free_trial_image_url")
        @Expose
        var free_trial_image_url: String? = null

        @SerializedName("shop_bg_image_url")
        @Expose
        var shop_bg_image_url: String? = null

        @SerializedName("powered_by_image_url")
        @Expose
        var powered_by_image_url: String? = null
    }

    inner class CUSTOMMESSAGE {
        @SerializedName("hello")
        @Expose
        var hello: String? = null

        @SerializedName("logoutDialog")
        @Expose
        var logoutDialog: String? = null

        @SerializedName("isSocialLoginEnable")
        @Expose
        var isSocialLoginEnable: String? = null

        @SerializedName("freeTrialDialogText")
        @Expose
        var freeTrialDialogText: String? = null

        @SerializedName("addReviewSuccessDialogText")
        @Expose
        var addReviewSuccessDialogText: String? = null

        @SerializedName("arrivedDestinationDialogText")
        @Expose
        var arrivedDestinationDialogText: String? = null

        @SerializedName("arrivedDestinationText")
        @Expose
        var arrivedDestinationText: String? = null

        @SerializedName("arrivedDestinationPoints")
        @Expose
        var arrivedDestinationPoints: String? = null

        @SerializedName("subscribeNowDialogText")
        @Expose
        var subscribeNowDialogText: String? = null

        @SerializedName("paymentSuccessDialogText")
        @Expose
        var paymentSuccessDialogText: String? = null

        @SerializedName("QRCodeScanningSuccessDialog")
        @Expose
        private var qRCodeScanningSuccessDialog: String? = null

        @SerializedName("referralRewardMessage")
        @Expose
        var referralRewardMessage: String? = null

        @SerializedName("inviteFriendText")
        @Expose
        var inviteFriendText: String? = null

        @SerializedName("wolooReferHostText")
        @Expose
        var wolooReferHostText: String? = null

        @SerializedName("cancelSubscriptionReasons")
        @Expose
        var cancelSubscriptionReasons: String? = null
        fun getqRCodeScanningSuccessDialog(): String? {
            return qRCodeScanningSuccessDialog
        }

        fun setqRCodeScanningSuccessDialog(qRCodeScanningSuccessDialog: String?) {
            this.qRCodeScanningSuccessDialog = qRCodeScanningSuccessDialog
        }
    }

    inner class APPVERSION {
        @SerializedName("version_code")
        @Expose
        var versionCode: String? = null

        @SerializedName("force_update")
        @Expose
        var forceUpdate: String? = null

        @SerializedName("update_text")
        @Expose
        var updateText: String? = null
    }

    inner class MAINTENANCESETTINGS {
        @SerializedName("MaintenanceFlag")
        @Expose
        var maintenanceFlag: String? = null

        @SerializedName("MaintenanceMessage")
        @Expose
        var maintenanceMessage: String? = null
    }

    inner class BLOCKAPP {
        @SerializedName("1")
        @Expose
        private var _1: String? = null

        @SerializedName("2")
        @Expose
        private var _2: String? = null

        @SerializedName("3")
        @Expose
        private var _3: String? = null

        @SerializedName("4")
        @Expose
        private var _4: String? = null

        @SerializedName("5")
        @Expose
        private var _5: String? = null
        fun get1(): String? {
            return _1
        }

        fun set1(_1: String?) {
            this._1 = _1
        }

        fun get2(): String? {
            return _2
        }

        fun set2(_2: String?) {
            this._2 = _2
        }

        fun get3(): String? {
            return _3
        }

        fun set3(_3: String?) {
            this._3 = _3
        }

        fun get4(): String? {
            return _4
        }

        fun set4(_4: String?) {
            this._4 = _4
        }

        fun get5(): String? {
            return _5
        }

        fun set5(_5: String?) {
            this._5 = _5
        }
    }

    inner class RZCRED {
        @SerializedName("key")
        @Expose
        var key: String? = null
    }

    inner class GOOGLE_MAPS {
        @SerializedName("key")
        @Expose
        var key: String? = null
    }

    inner class SUPPORT_EMAIL {
        @SerializedName("id")
        @Expose
        var id: String? = null
    }
}
