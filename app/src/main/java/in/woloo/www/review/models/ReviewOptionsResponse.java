package in.woloo.www.review.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewOptionsResponse {

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

    public class Data {

        @SerializedName("rating_option")
        @Expose
        private List<RatingOption> ratingOption = null;
        @SerializedName("rating_review")
        @Expose
        private List<RatingReview> ratingReview = null;
        @SerializedName("rating_improvement")
        @Expose
        private List<RatingImprovement> ratingImprovement = null;

        public List<RatingOption> getRatingOption() {
            return ratingOption;
        }

        public void setRatingOption(List<RatingOption> ratingOption) {
            this.ratingOption = ratingOption;
        }

        public List<RatingReview> getRatingReview() {
            return ratingReview;
        }

        public void setRatingReview(List<RatingReview> ratingReview) {
            this.ratingReview = ratingReview;
        }

        public List<RatingImprovement> getRatingImprovement() {
            return ratingImprovement;
        }

        public void setRatingImprovement(List<RatingImprovement> ratingImprovement) {
            this.ratingImprovement = ratingImprovement;
        }

    }

    public class RatingImprovement {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("display_name")
        @Expose
        private String displayName;
        @SerializedName("value")
        @Expose
        private String value;
        @SerializedName("details")
        @Expose
        private Object details;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("order")
        @Expose
        private Integer order;
        @SerializedName("group")
        @Expose
        private String group;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Object getDetails() {
            return details;
        }

        public void setDetails(Object details) {
            this.details = details;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

    }

    public class RatingOption {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("display_name")
        @Expose
        private String displayName;
        @SerializedName("value")
        @Expose
        private String value;
        @SerializedName("details")
        @Expose
        private Object details;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("order")
        @Expose
        private Integer order;
        @SerializedName("group")
        @Expose
        private String group;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Object getDetails() {
            return details;
        }

        public void setDetails(Object details) {
            this.details = details;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }
    }

    public class RatingReview {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("display_name")
        @Expose
        private String displayName;
        @SerializedName("value")
        @Expose
        private String value;
        @SerializedName("details")
        @Expose
        private Object details;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("order")
        @Expose
        private Integer order;
        @SerializedName("group")
        @Expose
        private String group;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Object getDetails() {
            return details;
        }

        public void setDetails(Object details) {
            this.details = details;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

    }

}
