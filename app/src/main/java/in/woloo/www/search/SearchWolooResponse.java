package in.woloo.www.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchWolooResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data = null;

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

        @SerializedName("total_woloo_count")
        @Expose
        private Integer totalWolooCount;
        @SerializedName("woloo_count")
        @Expose
        private Integer wolooCount;
        @SerializedName("woloos")
        @Expose
        private List<Woloo> woloos = null;
        @SerializedName("next")
        @Expose
        private Integer next;

        public Integer getTotalWolooCount() {
            return totalWolooCount;
        }

        public void setTotalWolooCount(Integer totalWolooCount) {
            this.totalWolooCount = totalWolooCount;
        }

        public Integer getWolooCount() {
            return wolooCount;
        }

        public void setWolooCount(Integer wolooCount) {
            this.wolooCount = wolooCount;
        }

        public List<Woloo> getWoloos() {
            return woloos;
        }

        public void setWoloos(List<Woloo> woloos) {
            this.woloos = woloos;
        }

        public Integer getNext() {
            return next;
        }

        public void setNext(Integer next) {
            this.next = next;
        }

        public class Woloo {

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
            private Object title;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("opening_hours")
            @Expose
            private String openingHours;
            @SerializedName("restaurant")
            @Expose
            private Object restaurant;
            @SerializedName("segregated")
            @Expose
            private String segregated;
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
            private Object userId;
            @SerializedName("status")
            @Expose
            private Integer status;
            @SerializedName("description")
            @Expose
            private String description;
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
            @SerializedName("duration")
            @Expose
            private String duration;
            @SerializedName("distance")
            @Expose
            private String distance;
            @SerializedName("user_rating")
            @Expose
            private String userRating;
            @SerializedName("user_review_count")
            @Expose
            private Integer userReviewCount;
            @SerializedName("offer")
            @Expose
            private Offer offer;

            @SerializedName("cibil_score_image")
            @Expose
            private String cibilScoreImage = "";

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

            public Object getTitle() {
                return title;
            }

            public void setTitle(Object title) {
                this.title = title;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getOpeningHours() {
                return openingHours;
            }

            public void setOpeningHours(String openingHours) {
                this.openingHours = openingHours;
            }

            public Object getRestaurant() {
                return restaurant;
            }

            public void setRestaurant(Object restaurant) {
                this.restaurant = restaurant;
            }

            public String getSegregated() {
                return segregated;
            }

            public void setSegregated(String segregated) {
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

            public Object getUserId() {
                return userId;
            }

            public void setUserId(Object userId) {
                this.userId = userId;
            }

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
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

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
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

            public Offer getOffer() {
                return offer;
            }

            public void setOffer(Offer offer) {
                this.offer = offer;
            }

            public String getCibilScoreImage() {
                return cibilScoreImage;
            }
            public void setCibilScoreImage(String cibilScoreImage) { this.cibilScoreImage = cibilScoreImage; }

        }

        public class Offer {
            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("woloo_id")
            @Expose
            private Integer wolooId;
            @SerializedName("start_date")
            @Expose
            private String startDate;
            @SerializedName("end_date")
            @Expose
            private String endDate;
            @SerializedName("status")
            @Expose
            private Integer status;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;
            @SerializedName("deleted_at")
            @Expose
            private Object deletedAt;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public Integer getWolooId() {
                return wolooId;
            }

            public void setWolooId(Integer wolooId) {
                this.wolooId = wolooId;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
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
        }
    }

}
