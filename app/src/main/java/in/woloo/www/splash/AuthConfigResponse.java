package in.woloo.www.splash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthConfigResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{

        @SerializedName("URLS")
        @Expose
        private URLS uRLS;
        @SerializedName("CUSTOM_MESSAGE")
        @Expose
        private CUSTOMMESSAGE cUSTOMMESSAGE;
        @SerializedName("APP_VERSION")
        @Expose
        private APPVERSION aPPVERSION;
        @SerializedName("MAINTENANCE_SETTINGS")
        @Expose
        private MAINTENANCESETTINGS mAINTENANCESETTINGS;
        @SerializedName("BLOCK_APP")
        @Expose
        private BLOCKAPP bLOCKAPP;

        @SerializedName("RZ_CRED")
        @Expose
        private RZCRED rZCRED;

        @SerializedName("GOOGLE_MAPS")
        @Expose
        private GOOGLE_MAPS google_maps;

        @SerializedName("free_trial_period_days")
        @Expose
        private String free_trial_period_days = "0";

        @SerializedName("free_trial_text")
        @Expose
        private String free_trial_text = "0";

        @SerializedName("SUPPORT_EMAIL")
        @Expose
        private SUPPORT_EMAIL supportEmail;

        public URLS getuRLS() {
            return uRLS;
        }

        public void setuRLS(URLS uRLS) {
            this.uRLS = uRLS;
        }

        public CUSTOMMESSAGE getcUSTOMMESSAGE() {
            return cUSTOMMESSAGE;
        }

        public void setcUSTOMMESSAGE(CUSTOMMESSAGE cUSTOMMESSAGE) {
            this.cUSTOMMESSAGE = cUSTOMMESSAGE;
        }

        public APPVERSION getaPPVERSION() {
            return aPPVERSION;
        }

        public void setaPPVERSION(APPVERSION aPPVERSION) {
            this.aPPVERSION = aPPVERSION;
        }

        public MAINTENANCESETTINGS getmAINTENANCESETTINGS() {
            return mAINTENANCESETTINGS;
        }

        public void setmAINTENANCESETTINGS(MAINTENANCESETTINGS mAINTENANCESETTINGS) {
            this.mAINTENANCESETTINGS = mAINTENANCESETTINGS;
        }

        public BLOCKAPP getbLOCKAPP() {
            return bLOCKAPP;
        }

        public void setbLOCKAPP(BLOCKAPP bLOCKAPP) {
            this.bLOCKAPP = bLOCKAPP;
        }

        public RZCRED getrZCRED() {
            return rZCRED;
        }

        public void setrZCRED(RZCRED rZCRED) {
            this.rZCRED = rZCRED;
        }

        public GOOGLE_MAPS getGoogle_maps() {
            return google_maps;
        }

        public void setGoogle_maps(GOOGLE_MAPS google_maps) {
            this.google_maps = google_maps;
        }

        public String getFreeTrialPeriodDays() {
            return free_trial_period_days;
        }

        public void setFreeTrialPeriodDays(String free_trial_period_days) {
            this.free_trial_period_days = free_trial_period_days;
        }

        public String getFreeTrialText() {
            return free_trial_text;
        }

        public void setFreeTrialText(String free_trial_text) {
            this.free_trial_text = free_trial_text;
        }

        public SUPPORT_EMAIL getSupportEmail() {
            return supportEmail;
        }

        public void setSupportEmail(SUPPORT_EMAIL supportEmail) {
            this.supportEmail = supportEmail;
        }
    }

    public class URLS{
        @SerializedName("about_url")
        @Expose
        private String aboutUrl;
        @SerializedName("terms_url")
        @Expose
        private String terms_url;
        @SerializedName("app_share_url")
        @Expose
        private String app_share_url;
        @SerializedName("free_trial_image_url")
        @Expose
        private String free_trial_image_url;
        @SerializedName("shop_bg_image_url")
        @Expose
        private String shop_bg_image_url;
        @SerializedName("powered_by_image_url")
        @Expose
        private String powered_by_image_url;

        public String getAboutUrl() {
            return aboutUrl;
        }

        public void setAboutUrl(String aboutUrl) {
            this.aboutUrl = aboutUrl;
        }

        public String getTerms_url() {
            return terms_url;
        }

        public void setTerms_url(String terms_url) {
            this.terms_url = terms_url;
        }

        public String getApp_share_url() {
            return app_share_url;
        }

        public void setApp_share_url(String app_share_url) {
            this.app_share_url = app_share_url;
        }

        public String getFree_trial_image_url() {
            return free_trial_image_url;
        }

        public void setFree_trial_image_url(String free_trial_image_url) {
            this.free_trial_image_url = free_trial_image_url;
        }

        public String getShop_bg_image_url() {
            return shop_bg_image_url;
        }

        public void setShop_bg_image_url(String shop_bg_image_url) {
            this.shop_bg_image_url = shop_bg_image_url;
        }

        public String getPowered_by_image_url() {
            return powered_by_image_url;
        }

        public void setPowered_by_image_url(String powered_by_image_url) {
            this.powered_by_image_url = powered_by_image_url;
        }
    }

    public class CUSTOMMESSAGE{

        @SerializedName("hello")
        @Expose
        private String hello;
        @SerializedName("logoutDialog")
        @Expose
        private String logoutDialog;
        @SerializedName("isSocialLoginEnable")
        @Expose
        private String isSocialLoginEnable;
        @SerializedName("freeTrialDialogText")
        @Expose
        private String freeTrialDialogText;
        @SerializedName("addReviewSuccessDialogText")
        @Expose
        private String addReviewSuccessDialogText;
        @SerializedName("arrivedDestinationDialogText")
        @Expose
        private String arrivedDestinationDialogText;

        @SerializedName("arrivedDestinationText")
        @Expose
        private String arrivedDestinationText;

        @SerializedName("arrivedDestinationPoints")
        @Expose
        private String arrivedDestinationPoints;

        @SerializedName("subscribeNowDialogText")
        @Expose
        private String subscribeNowDialogText;
        @SerializedName("paymentSuccessDialogText")
        @Expose
        private String paymentSuccessDialogText;
        @SerializedName("QRCodeScanningSuccessDialog")
        @Expose
        private String qRCodeScanningSuccessDialog;
        @SerializedName("referralRewardMessage")
        @Expose
        private String referralRewardMessage;
        @SerializedName("inviteFriendText")
        @Expose
        private String inviteFriendText;
        @SerializedName("wolooReferHostText")
        @Expose
        private String wolooReferHostText;
        @SerializedName("cancelSubscriptionReasons")
        @Expose
        private String cancelSubscriptionReasons;

        public String getInviteFriendText() {
            return inviteFriendText;
        }

        public void setInviteFriendText(String inviteFriendText) {
            this.inviteFriendText = inviteFriendText;
        }

        public String getWolooReferHostText() {
            return wolooReferHostText;
        }

        public void setWolooReferHostText(String wolooReferHostText) {
            this.wolooReferHostText = wolooReferHostText;
        }

        public String getCancelSubscriptionReasons() {
            return cancelSubscriptionReasons;
        }

        public void setCancelSubscriptionReasons(String cancelSubscriptionReasons) {
            this.cancelSubscriptionReasons = cancelSubscriptionReasons;
        }

        public String getArrivedDestinationText() {
            return arrivedDestinationText;
        }

        public void setArrivedDestinationText(String arrivedDestinationText) {
            this.arrivedDestinationText = arrivedDestinationText;
        }

        public String getArrivedDestinationPoints() {
            return arrivedDestinationPoints;
        }

        public void setArrivedDestinationPoints(String arrivedDestinationPoints) {
            this.arrivedDestinationPoints = arrivedDestinationPoints;
        }

        public String getHello() {
            return hello;
        }

        public void setHello(String hello) {
            this.hello = hello;
        }

        public String getLogoutDialog() {
            return logoutDialog;
        }

        public void setLogoutDialog(String logoutDialog) {
            this.logoutDialog = logoutDialog;
        }

        public String getIsSocialLoginEnable() {
            return isSocialLoginEnable;
        }

        public void setIsSocialLoginEnable(String isSocialLoginEnable) {
            this.isSocialLoginEnable = isSocialLoginEnable;
        }

        public String getFreeTrialDialogText() {
            return freeTrialDialogText;
        }

        public void setFreeTrialDialogText(String freeTrialDialogText) {
            this.freeTrialDialogText = freeTrialDialogText;
        }

        public String getAddReviewSuccessDialogText() {
            return addReviewSuccessDialogText;
        }

        public void setAddReviewSuccessDialogText(String addReviewSuccessDialogText) {
            this.addReviewSuccessDialogText = addReviewSuccessDialogText;
        }

        public String getArrivedDestinationDialogText() {
            return arrivedDestinationDialogText;
        }

        public void setArrivedDestinationDialogText(String arrivedDestinationDialogText) {
            this.arrivedDestinationDialogText = arrivedDestinationDialogText;
        }

        public String getSubscribeNowDialogText() {
            return subscribeNowDialogText;
        }

        public void setSubscribeNowDialogText(String subscribeNowDialogText) {
            this.subscribeNowDialogText = subscribeNowDialogText;
        }

        public String getPaymentSuccessDialogText() {
            return paymentSuccessDialogText;
        }

        public void setPaymentSuccessDialogText(String paymentSuccessDialogText) {
            this.paymentSuccessDialogText = paymentSuccessDialogText;
        }

        public String getqRCodeScanningSuccessDialog() {
            return qRCodeScanningSuccessDialog;
        }

        public void setqRCodeScanningSuccessDialog(String qRCodeScanningSuccessDialog) {
            this.qRCodeScanningSuccessDialog = qRCodeScanningSuccessDialog;
        }

        public String getReferralRewardMessage() {
            return referralRewardMessage;
        }

        public void setReferralRewardMessage(String referralRewardMessage) {
            this.referralRewardMessage = referralRewardMessage;
        }
    }

    public class APPVERSION{
        @SerializedName("version_code")
        @Expose
        private String versionCode;
        @SerializedName("force_update")
        @Expose
        private String forceUpdate;
        @SerializedName("update_text")
        @Expose
        private String updateText;

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getForceUpdate() {
            return forceUpdate;
        }

        public void setForceUpdate(String forceUpdate) {
            this.forceUpdate = forceUpdate;
        }

        public String getUpdateText() {
            return updateText;
        }

        public void setUpdateText(String updateText) {
            this.updateText = updateText;
        }
    }

    public class MAINTENANCESETTINGS{

        @SerializedName("MaintenanceFlag")
        @Expose
        private String maintenanceFlag;
        @SerializedName("MaintenanceMessage")
        @Expose
        private String maintenanceMessage;

        public String getMaintenanceFlag() {
            return maintenanceFlag;
        }

        public void setMaintenanceFlag(String maintenanceFlag) {
            this.maintenanceFlag = maintenanceFlag;
        }

        public String getMaintenanceMessage() {
            return maintenanceMessage;
        }

        public void setMaintenanceMessage(String maintenanceMessage) {
            this.maintenanceMessage = maintenanceMessage;
        }

    }

    public class BLOCKAPP{

        @SerializedName("1")
        @Expose
        private String _1;
        @SerializedName("2")
        @Expose
        private String _2;
        @SerializedName("3")
        @Expose
        private String _3;
        @SerializedName("4")
        @Expose
        private String _4;
        @SerializedName("5")
        @Expose
        private String _5;

        public String get1() {
            return _1;
        }

        public void set1(String _1) {
            this._1 = _1;
        }

        public String get2() {
            return _2;
        }

        public void set2(String _2) {
            this._2 = _2;
        }

        public String get3() {
            return _3;
        }

        public void set3(String _3) {
            this._3 = _3;
        }

        public String get4() {
            return _4;
        }

        public void set4(String _4) {
            this._4 = _4;
        }

        public String get5() {
            return _5;
        }

        public void set5(String _5) {
            this._5 = _5;
        }

    }

    public class RZCRED{

        @SerializedName("key")
        @Expose
        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

    }


    public class GOOGLE_MAPS{

        @SerializedName("key")
        @Expose
        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

    }

    public class SUPPORT_EMAIL {
        @SerializedName("id")
        @Expose
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
