package in.woloo.www.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SafetyNetResponse {
    @SerializedName("ctsProfileMatch")
    private final boolean ctsProfileMatch;

    @SerializedName("apkPackageName")
    private final String apkPackageName;

    @SerializedName("apkDigestSha256")
    private final String apkDigestSha256;

    @SerializedName("nonce")
    private final String nonce;

    @SerializedName("apkCertificateDigestSha256")
    private final List<String> apkCertificateDigestSha256;

    @SerializedName("timestampMs")
    private final long timestampMs;

    @SerializedName("basicIntegrity")
    private final boolean basicIntegrity;

    public SafetyNetResponse(boolean ctsProfileMatch, String apkPackageName, String apkDigestSha256,
                             String nonce, List<String> apkCertificateDigestSha256, long timestampMs,
                             boolean basicIntegrity) {
        this.ctsProfileMatch = ctsProfileMatch;
        this.apkPackageName = apkPackageName;
        this.apkDigestSha256 = apkDigestSha256;
        this.nonce = nonce;
        this.apkCertificateDigestSha256 = apkCertificateDigestSha256;
        this.timestampMs = timestampMs;
        this.basicIntegrity = basicIntegrity;
    }

    public boolean isCtsProfileMatch() {
        return ctsProfileMatch;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public String getApkDigestSha256() {
        return apkDigestSha256;
    }

    public String getNonce() {
        return nonce;
    }

    public List<String> getApkCertificateDigestSha256() {
        return apkCertificateDigestSha256;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public boolean isBasicIntegrity() {
        return basicIntegrity;
    }
}
