package in.woloo.www.login;

import in.woloo.www.login.models.LoginResponse;
import in.woloo.www.login.models.OTPResponse;
import in.woloo.www.login.models.UpdateTokenResponse;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.splash.PendingReviewStatusResponse;

interface LoginView {
    void sendOtpSuccessFlow(OTPResponse otpResponse);
    void loginSuccessFlow(LoginResponse loginResponse);
    void tokenUpdateSuccess(UpdateTokenResponse updateTokenResponse);
    void authConfigSuccess(AuthConfigResponse authConfigResponse);
    void pendingReviewStatusResponse(PendingReviewStatusResponse pendingReviewStatusResponse);
    void onInvalidOTP();
}
