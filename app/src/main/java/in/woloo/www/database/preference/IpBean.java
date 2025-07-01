package in.woloo.www.database.preference;

import com.google.gson.annotations.SerializedName;

public class IpBean {
    @SerializedName("code")
    private final int code;

    @SerializedName("status")
    private final String status;

    @SerializedName("message")
    private final String message;

    @SerializedName("data")
    private final Data data;

    public IpBean(int code, String status, String message, Data data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        @SerializedName("Versions")
        private final Versions versions;

        public Data(Versions versions) {
            this.versions = versions;
        }

        public Versions getVersions() {
            return versions;
        }

        public static class Versions {
            @SerializedName("ipAddress")
            private final String ipAddress;

            @SerializedName("countryCode")
            private final String countryCode;

            @SerializedName("cityName")
            private final String cityName;

            @SerializedName("regionName")
            private final String regionName;

            @SerializedName("isp")
            private final String isp;

            public Versions(String ipAddress, String countryCode, String cityName,
                            String regionName, String isp) {
                this.ipAddress = ipAddress;
                this.countryCode = countryCode;
                this.cityName = cityName;
                this.regionName = regionName;
                this.isp = isp;
            }

            public String getIpAddress() {
                return ipAddress;
            }

            public String getCountryCode() {
                return countryCode;
            }

            public String getCityName() {
                return cityName;
            }

            public String getRegionName() {
                return regionName;
            }

            public String getIsp() {
                return isp;
            }
        }
    }

    @Override
    public String toString() {
        return "IpBean{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
