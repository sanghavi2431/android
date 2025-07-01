package in.woloo.www.my_history.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyHistoryResponse {

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

        @SerializedName("total_count")
        @Expose
        private Integer totalCount = 0;
        @SerializedName("history_count")
        @Expose
        private Integer historyCount = 0;
        @SerializedName("history")
        @Expose
        private List<History> history = null;
        @SerializedName("next")
        @Expose
        private Integer next = 0;

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public Integer getHistoryCount() {
            return historyCount;
        }

        public void setHistoryCount(Integer historyCount) {
            this.historyCount = historyCount;
        }

        public List<History> getHistory() {
            return history;
        }

        public void setHistory(List<History> history) {
            this.history = history;
        }

        public Integer getNext() {
            return next;
        }

        public void setNext(Integer next) {
            this.next = next;
        }
    }

    public class History{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("woloo_id")
        @Expose
        private Integer wolooId;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("is_review_pending")
        @Expose
        private Integer isReviewPending;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("woloo_details")
        @Expose
        private WolooDetails wolooDetails;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getWolooId() {
            return wolooId;
        }

        public void setWolooId(Integer wolooId) {
            this.wolooId = wolooId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public WolooDetails getWolooDetails() {
            return wolooDetails;
        }

        public void setWolooDetails(WolooDetails wolooDetails) {
            this.wolooDetails = wolooDetails;
        }

        public Integer getIsReviewPending() {
            return isReviewPending;
        }

        public void setIsReviewPending(Integer isReviewPending) {
            this.isReviewPending = isReviewPending;
        }


    }

    public class WolooDetails{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("image")
        @Expose
        private List<String> image;
        @SerializedName("opening_hours")
        @Expose
        private Object openingHours;
        @SerializedName("restaurant")
        @Expose
        private Object restaurant;
        @SerializedName("segregated")
        @Expose
        private Object segregated;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lng")
        @Expose
        private String lng;
        @SerializedName("user_id")
        @Expose
        private Integer userId = 0;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("description")
        @Expose
        private Object description;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("is_covid_free")
        @Expose
        private Integer isCovidFree;
        @SerializedName("is_safe_space")
        @Expose
        private Integer isSafeSpace;
        @SerializedName("is_clean_and_hygiene")
        @Expose
        private Integer isCleanAndHygiene;
        @SerializedName("is_sanitary_pads_available")
        @Expose
        private Integer isSanitaryPadsAvailable;
        @SerializedName("is_makeup_room_available")
        @Expose
        private Integer isMakeupRoomAvailable;
        @SerializedName("is_coffee_available")
        @Expose
        private Integer isCoffeeAvailable;
        @SerializedName("is_sanitizer_available")
        @Expose
        private Integer isSanitizerAvailable;
        @SerializedName("is_feeding_room")
        @Expose
        private Integer isFeedingRoom;
        @SerializedName("is_wheelchair_accessible")
        @Expose
        private Integer isWheelchairAccessible;
        @SerializedName("is_washroom")
        @Expose
        private Integer isWashroom;
        @SerializedName("is_premium")
        @Expose
        private Integer isPremium;
        @SerializedName("user_rating")
        @Expose
        private String userRating;
        @SerializedName("user_review_count")
        @Expose
        private Integer userReviewCount;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImage() {
            return image;
        }

        public void setImage(List<String> image) {
            this.image = image;
        }

        public Object getOpeningHours() {
            return openingHours;
        }

        public void setOpeningHours(Object openingHours) {
            this.openingHours = openingHours;
        }

        public Object getRestaurant() {
            return restaurant;
        }

        public void setRestaurant(Object restaurant) {
            this.restaurant = restaurant;
        }

        public Object getSegregated() {
            return segregated;
        }

        public void setSegregated(Object segregated) {
            this.segregated = segregated;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
        }

        public Integer getIsCovidFree() {
            return isCovidFree;
        }

        public void setIsCovidFree(Integer isCovidFree) {
            this.isCovidFree = isCovidFree;
        }

        public Integer getIsSafeSpace() {
            return isSafeSpace;
        }

        public void setIsSafeSpace(Integer isSafeSpace) {
            this.isSafeSpace = isSafeSpace;
        }

        public Integer getIsCleanAndHygiene() {
            return isCleanAndHygiene;
        }

        public void setIsCleanAndHygiene(Integer isCleanAndHygiene) {
            this.isCleanAndHygiene = isCleanAndHygiene;
        }

        public Integer getIsSanitaryPadsAvailable() {
            return isSanitaryPadsAvailable;
        }

        public void setIsSanitaryPadsAvailable(Integer isSanitaryPadsAvailable) {
            this.isSanitaryPadsAvailable = isSanitaryPadsAvailable;
        }

        public Integer getIsMakeupRoomAvailable() {
            return isMakeupRoomAvailable;
        }

        public void setIsMakeupRoomAvailable(Integer isMakeupRoomAvailable) {
            this.isMakeupRoomAvailable = isMakeupRoomAvailable;
        }

        public Integer getIsCoffeeAvailable() {
            return isCoffeeAvailable;
        }

        public void setIsCoffeeAvailable(Integer isCoffeeAvailable) {
            this.isCoffeeAvailable = isCoffeeAvailable;
        }

        public Integer getIsSanitizerAvailable() {
            return isSanitizerAvailable;
        }

        public void setIsSanitizerAvailable(Integer isSanitizerAvailable) {
            this.isSanitizerAvailable = isSanitizerAvailable;
        }

        public Integer getIsFeedingRoom() {
            return isFeedingRoom;
        }

        public void setIsFeedingRoom(Integer isFeedingRoom) {
            this.isFeedingRoom = isFeedingRoom;
        }

        public Integer getIsWheelchairAccessible() {
            return isWheelchairAccessible;
        }

        public void setIsWheelchairAccessible(Integer isWheelchairAccessible) {
            this.isWheelchairAccessible = isWheelchairAccessible;
        }

        public Integer getIsWashroom() {
            return isWashroom;
        }

        public void setIsWashroom(Integer isWashroom) {
            this.isWashroom = isWashroom;
        }

        public Integer getIsPremium() {
            return isPremium;
        }

        public void setIsPremium(Integer isPremium) {
            this.isPremium = isPremium;
        }

        public String getUserRating() {
            return userRating;
        }

        public void setUserRating(String userRating) {
            this.userRating = userRating;
        }

        public Integer getUserReviewCount() {
            return userReviewCount;
        }

        public void setUserReviewCount(Integer userReviewCount) {
            this.userReviewCount = userReviewCount;
        }
    }

}
