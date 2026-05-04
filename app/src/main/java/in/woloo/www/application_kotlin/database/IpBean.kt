package `in`.woloo.www.application_kotlin.database

import com.google.gson.annotations.SerializedName

class IpBean(
    @field:SerializedName("code") val code: Int,
    @field:SerializedName(
        "status"
    ) val status: String,
    @field:SerializedName("message") val message: String,
    @JvmField @field:SerializedName(
        "data"
    ) val data: Data
) {

    class Data(@JvmField @field:SerializedName("Versions") val versions: Versions) {

        class Versions(
            @field:SerializedName("ipAddress") val ipAddress: String, @JvmField @field:SerializedName(
                "countryCode"
            ) val countryCode: String, @field:SerializedName("cityName") val cityName: String,
            @field:SerializedName("regionName") val regionName: String, @field:SerializedName(
                "isp"
            ) val isp: String
        )
    }

    override fun toString(): String {
        return "IpBean{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}'
    }
}
