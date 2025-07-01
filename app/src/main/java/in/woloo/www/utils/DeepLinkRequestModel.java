package in.woloo.www.utils;

import com.google.gson.annotations.SerializedName;

public class DeepLinkRequestModel {
    @SerializedName("dynamicLinkInfo")
    private final DynamicLinkInfo dynamicLinkInfo;

    @SerializedName("suffix")
    private final Suffix suffix;


    public DeepLinkRequestModel(DynamicLinkInfo dynamicLinkInfo, Suffix suffix) {
        this.dynamicLinkInfo = dynamicLinkInfo;
        this.suffix = suffix;
    }

    public DynamicLinkInfo getDynamicLinkInfo() {
        return dynamicLinkInfo;
    }

    public Suffix getSuffix() {
        return suffix;
    }

    public static class DynamicLinkInfo {
        @SerializedName("iosInfo")
        private final IosInfo iosInfo;

        @SerializedName("androidInfo")
        private final AndroidInfo androidInfo;

        @SerializedName("link")
        private final String link;

        @SerializedName("domainUriPrefix")
        private final String domainUriPrefix;

        @SerializedName("socialMetaTagInfo")
        private final SocialMetaTagInfo socialMetaTagInfo;

        public DynamicLinkInfo(IosInfo iosInfo, AndroidInfo androidInfo, String link,
                               String domainUriPrefix,SocialMetaTagInfo socialMetaTagInfo) {
            this.iosInfo = iosInfo;
            this.androidInfo = androidInfo;
            this.link = link;
            this.domainUriPrefix = domainUriPrefix;
            this.socialMetaTagInfo = socialMetaTagInfo;
        }

        public IosInfo getIosInfo() {
            return iosInfo;
        }

        public AndroidInfo getAndroidInfo() {
            return androidInfo;
        }

        public String getLink() {
            return link;
        }

        public String getDomainUriPrefix() {
            return domainUriPrefix;
        }

        public static class IosInfo {
            @SerializedName("iosBundleId")
            private final String iosBundleId;

            public IosInfo(String iosBundleId) {
                this.iosBundleId = iosBundleId;
            }

            public String getIosBundleId() {
                return iosBundleId;
            }
        }

        public static class AndroidInfo {
            @SerializedName("androidPackageName")
            private final String androidPackageName;

            public AndroidInfo(String androidPackageName) {
                this.androidPackageName = androidPackageName;
            }

            public String getAndroidPackageName() {
                return androidPackageName;
            }
        }

        public static class SocialMetaTagInfo {

            @SerializedName("socialImageLink")
            private String socialImageLink;

            @SerializedName("socialTitle")
            private String socialTitle;

            @SerializedName("socialDescription")
            private String socialDescription;

            public SocialMetaTagInfo(String socialTitle, String socialDescription, String socialImageLink) {
                this.socialTitle = socialTitle;
                this.socialDescription = socialDescription;
                this.socialImageLink = socialImageLink;
            }

            public String getSocialTitle() {
                return socialTitle;
            }

            public String getSocialDescription() {
                return socialDescription;
            }

            public String getSocialImageLink() {
                return socialImageLink;
            }
        }
    }

    public static class Suffix {
        @SerializedName("option")
        private final String option;

        public Suffix(String option) {
            this.option = option;
        }

        public String getOption() {
            return option;
        }
    }

}
