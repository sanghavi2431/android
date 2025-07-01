package in.woloo.www.more.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserCoinHistoryModel {

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public class Data{

		@SerializedName("total_count")
		private int totalCount;

		@SerializedName("history_count")
		private int historyCount;

		@SerializedName("history")
		private List<HistoryItem> history;

//		@SerializedName("next")
		private Integer next;

		@SerializedName("last_page")
		private Integer lastPage;

		public Integer getLastPage() {
			return lastPage;
		}

		public void setLastPage(Integer lastPage) {
			this.lastPage = lastPage;
		}
		public Integer getNext() {
			return next;
		}

		public void setNext(Integer next) {
			this.next = next;
		}

		public void setTotalCount(int totalCount){
			this.totalCount = totalCount;
		}

		public int getTotalCount(){
			return totalCount;
		}

		public void setHistoryCount(int historyCount){
			this.historyCount = historyCount;
		}

		public int getHistoryCount(){
			return historyCount;
		}

		public void setHistory(List<HistoryItem> history){
			this.history = history;
		}

		public List<HistoryItem> getHistory(){
			return history;
		}

		public class HistoryItem implements Serializable {

			@SerializedName("woloo_details")
			private WolooDetails wolooDetails;

			@SerializedName("sender_receiver_id")
			private int senderReceiverId;

			@SerializedName("created_at")
			private String createdAt;

			@SerializedName("transaction_type")
			private String transactionType;

			@SerializedName("message")
			private String message;

			@SerializedName("type")
			private String type;

			@SerializedName("is_expired")
			private int isExpired;

			@SerializedName("is_gift")
			private int isGift;

			@SerializedName("updated_at")
			private String updatedAt;

			@SerializedName("user_id")
			private int userId;

			@SerializedName("id")
			private int id;

			@SerializedName("woloo_id")
			private int wolooId;

			@SerializedName("value")
			private String value;

			@SerializedName("remarks")
			private String remarks;

			@SerializedName("status")
			private int status;

			@SerializedName("expired_on")
			private Object expiredOn;

			@SerializedName("sender")
			private Sender sender;

			public void setWolooDetails(WolooDetails wolooDetails){
				this.wolooDetails = wolooDetails;
			}

			public WolooDetails getWolooDetails(){
				return wolooDetails;
			}

			public void setSenderReceiverId(int senderReceiverId){
				this.senderReceiverId = senderReceiverId;
			}

			public int getSenderReceiverId(){
				return senderReceiverId;
			}

			public void setCreatedAt(String createdAt){
				this.createdAt = createdAt;
			}

			public String getCreatedAt(){
				return createdAt;
			}

			public void setTransactionType(String transactionType){
				this.transactionType = transactionType;
			}

			public String getTransactionType(){
				return transactionType;
			}

			public void setMessage(String message){
				this.message = message;
			}

			public String getMessage(){
				return message;
			}

			public void setType(String type){
				this.type = type;
			}

			public String getType(){
				return type;
			}

			public void setIsExpired(int isExpired){
				this.isExpired = isExpired;
			}

			public int getIsExpired(){
				return isExpired;
			}

			public void setIsGift(int isGift){
				this.isGift = isGift;
			}

			public int getIsGift(){
				return isGift;
			}

			public void setUpdatedAt(String updatedAt){
				this.updatedAt = updatedAt;
			}

			public String getUpdatedAt(){
				return updatedAt;
			}

			public void setUserId(int userId){
				this.userId = userId;
			}

			public int getUserId(){
				return userId;
			}

			public void setId(int id){
				this.id = id;
			}

			public int getId(){
				return id;
			}

			public void setWolooId(int wolooId){
				this.wolooId = wolooId;
			}

			public int getWolooId(){
				return wolooId;
			}

			public void setValue(String value){
				this.value = value;
			}

			public String getValue(){
				return value;
			}

			public void setRemarks(String remarks){
				this.remarks = remarks;
			}

			public String getRemarks(){
				return remarks;
			}

			public void setStatus(int status){
				this.status = status;
			}

			public int getStatus(){
				return status;
			}

			public void setExpiredOn(Object expiredOn){
				this.expiredOn = expiredOn;
			}

			public Object getExpiredOn(){
				return expiredOn;
			}

			public void setSender(Sender sender){
				this.sender = sender;
			}

			public Sender getSender(){
				return sender;
			}

			public class WolooDetails implements Serializable{

				@SerializedName("code")
				private String code;

				@SerializedName("city")
				private String city;

				@SerializedName("description")
				private String description;

				@SerializedName("created_at")
				private String createdAt;

				@SerializedName("title")
				private String title;

				@SerializedName("is_safe_space")
				private int isSafeSpace;

				@SerializedName("updated_at")
				private String updatedAt;

				@SerializedName("is_feeding_room")
				private int isFeedingRoom;

				@SerializedName("recommended_by")
				private int recommendedBy;

				@SerializedName("id")
				private int id;

				@SerializedName("is_sanitizer_available")
				private int isSanitizerAvailable;

				@SerializedName("lat")
				private String lat;

				@SerializedName("user_rating")
				private String userRating;


//				@SerializedName("pincode")
				private int pincode;

				@SerializedName("address")
				private String address;

//				@SerializedName("user_review_count")
				private int userReviewCount;

				@SerializedName("lng")
				private String lng;

				@SerializedName("is_makeup_room_available")
				private int isMakeupRoomAvailable;

				@SerializedName("restaurant")
				private String restaurant;

				@SerializedName("is_clean_and_hygiene")
				private int isCleanAndHygiene;

				@SerializedName("is_washroom")
				private int isWashroom;

//				@SerializedName("deleted_at")
				private String deletedAt;

				@SerializedName("is_coffee_available")
				private int isCoffeeAvailable;

				@SerializedName("is_wheelchair_accessible")
				private int isWheelchairAccessible;

				@SerializedName("is_sanitary_pads_available")
				private int isSanitaryPadsAvailable;

				@SerializedName("is_franchise")
				private int isFranchise;

				@SerializedName("is_premium")
				private int isPremium;

				@SerializedName("user_id")
				private String userId;

				@SerializedName("name")
				private String name;

				@SerializedName("opening_hours")
				private String openingHours;

				@SerializedName("recommended_mobile")
				private String recommendedMobile;

				@SerializedName("segregated")
				private String segregated;

//				@SerializedName("status")
				private int status;

				@SerializedName("is_covid_free")
				private int isCovidFree;

				public void setCode(String code){
					this.code = code;
				}

				public String getCode(){
					return code;
				}

				public void setCity(String city){
					this.city = city;
				}

				public String getCity(){
					return city;
				}

				public void setDescription(String description){
					this.description = description;
				}

				public String getDescription(){
					return description;
				}

				public void setCreatedAt(String createdAt){
					this.createdAt = createdAt;
				}

				public String getCreatedAt(){
					return createdAt;
				}

				public void setTitle(String title){
					this.title = title;
				}

				public String getTitle(){
					return title;
				}

				public void setIsSafeSpace(int isSafeSpace){
					this.isSafeSpace = isSafeSpace;
				}

				public int getIsSafeSpace(){
					return isSafeSpace;
				}

				public void setUpdatedAt(String updatedAt){
					this.updatedAt = updatedAt;
				}

				public String getUpdatedAt(){
					return updatedAt;
				}

				public void setIsFeedingRoom(int isFeedingRoom){
					this.isFeedingRoom = isFeedingRoom;
				}

				public int getIsFeedingRoom(){
					return isFeedingRoom;
				}

				public void setRecommendedBy(int recommendedBy){
					this.recommendedBy = recommendedBy;
				}

				public int getRecommendedBy(){
					return recommendedBy;
				}

				public void setId(int id){
					this.id = id;
				}

				public int getId(){
					return id;
				}

				public void setIsSanitizerAvailable(int isSanitizerAvailable){
					this.isSanitizerAvailable = isSanitizerAvailable;
				}

				public int getIsSanitizerAvailable(){
					return isSanitizerAvailable;
				}

				public void setLat(String lat){
					this.lat = lat;
				}

				public String getLat(){
					return lat;
				}

				public void setUserRating(String userRating){
					this.userRating = userRating;
				}

				public String getUserRating(){
					return userRating;
				}

				public void setPincode(int pincode){
					this.pincode = pincode;
				}

				public int getPincode(){
					return pincode;
				}

				public void setAddress(String address){
					this.address = address;
				}

				public String getAddress(){
					return address;
				}

				public void setUserReviewCount(int userReviewCount){
					this.userReviewCount = userReviewCount;
				}

				public int getUserReviewCount(){
					return userReviewCount;
				}

				public void setLng(String lng){
					this.lng = lng;
				}

				public String getLng(){
					return lng;
				}

				public void setIsMakeupRoomAvailable(int isMakeupRoomAvailable){
					this.isMakeupRoomAvailable = isMakeupRoomAvailable;
				}

				public int getIsMakeupRoomAvailable(){
					return isMakeupRoomAvailable;
				}

				public void setRestaurant(String restaurant){
					this.restaurant = restaurant;
				}

				public String getRestaurant(){
					return restaurant;
				}

				public void setIsCleanAndHygiene(int isCleanAndHygiene){
					this.isCleanAndHygiene = isCleanAndHygiene;
				}

				public int getIsCleanAndHygiene(){
					return isCleanAndHygiene;
				}

				public void setIsWashroom(int isWashroom){
					this.isWashroom = isWashroom;
				}

				public int getIsWashroom(){
					return isWashroom;
				}

				public void setDeletedAt(String deletedAt){
					this.deletedAt = deletedAt;
				}

				public String getDeletedAt(){
					return deletedAt;
				}

				public void setIsCoffeeAvailable(int isCoffeeAvailable){
					this.isCoffeeAvailable = isCoffeeAvailable;
				}

				public int getIsCoffeeAvailable(){
					return isCoffeeAvailable;
				}

				public void setIsWheelchairAccessible(int isWheelchairAccessible){
					this.isWheelchairAccessible = isWheelchairAccessible;
				}

				public int getIsWheelchairAccessible(){
					return isWheelchairAccessible;
				}

				public void setIsSanitaryPadsAvailable(int isSanitaryPadsAvailable){
					this.isSanitaryPadsAvailable = isSanitaryPadsAvailable;
				}

				public int getIsSanitaryPadsAvailable(){
					return isSanitaryPadsAvailable;
				}

				public void setIsFranchise(int isFranchise){
					this.isFranchise = isFranchise;
				}

				public int getIsFranchise(){
					return isFranchise;
				}

				public void setIsPremium(int isPremium){
					this.isPremium = isPremium;
				}

				public int getIsPremium(){
					return isPremium;
				}

				public void setUserId(String userId){
					this.userId = userId;
				}

				public String getUserId(){
					return userId;
				}

				public void setName(String name){
					this.name = name;
				}

				public String getName(){
					if(name == null ||name.equals("")){
						return "";
					}else {
						return name;
					}
				}

				public void setOpeningHours(String openingHours){
					this.openingHours = openingHours;
				}

				public String getOpeningHours(){
					return openingHours;
				}

				public void setRecommendedMobile(String recommendedMobile){
					this.recommendedMobile = recommendedMobile;
				}

				public String getRecommendedMobile(){
					return recommendedMobile;
				}

				public void setSegregated(String segregated){
					this.segregated = segregated;
				}

				public String getSegregated(){
					return segregated;
				}

				public void setStatus(int status){
					this.status = status;
				}

				public int getStatus(){
					return status;
				}

				public void setIsCovidFree(int isCovidFree){
					this.isCovidFree = isCovidFree;
				}

				public int getIsCovidFree(){
					return isCovidFree;
				}


			}

			public class Sender implements Serializable{

				@SerializedName("gender")
				private String gender;

				@SerializedName("city")
				private String city;

				@SerializedName("created_at")
				private String createdAt;

				@SerializedName("is_first_session")
				private int isFirstSession;

				@SerializedName("ref_code")
				private String refCode;

				@SerializedName("subscription_id")
				private String subscriptionId;

				@SerializedName("updated_at")
				private String updatedAt;

				@SerializedName("role_id")
				private String roleId;

				@SerializedName("id")
				private int id;

				@SerializedName("woloo_id")
				private String wolooId;

				@SerializedName("email")
				private String email;

				@SerializedName("pincode")
				private String pincode;

				/*@SerializedName("settings")
				private List<String> settings;*/

				@SerializedName("address")
				private String address;

				@SerializedName("expiry_date")
				private String expiryDate;

				@SerializedName("mobile")
				private String mobile;

//				@SerializedName("otp")
				private int otp;

				@SerializedName("avatar")
				private String avatar;

				@SerializedName("sponsor_id")
				private String sponsorId;

//				@SerializedName("deleted_at")
				private String deletedAt;

				@SerializedName("gp_id")
				private String gpId;

				@SerializedName("fb_id")
				private String fbId;

				@SerializedName("dob")
				private String dob;

				@SerializedName("name")
				private String name="";

				@SerializedName("voucher_id")
				private String voucherId;

				@SerializedName("status")
				private String status;

				public void setGender(String gender){
					this.gender = gender;
				}

				public String getGender(){
					return gender;
				}

				public void setCity(String city){
					this.city = city;
				}

				public String getCity(){
					return city;
				}

				public void setCreatedAt(String createdAt){
					this.createdAt = createdAt;
				}

				public String getCreatedAt(){
					return createdAt;
				}

				public void setIsFirstSession(int isFirstSession){
					this.isFirstSession = isFirstSession;
				}

				public int getIsFirstSession(){
					return isFirstSession;
				}

				public void setRefCode(String refCode){
					this.refCode = refCode;
				}

				public String getRefCode(){
					return refCode;
				}

				public void setSubscriptionId(String subscriptionId){
					this.subscriptionId = subscriptionId;
				}

				public String getSubscriptionId(){
					return subscriptionId;
				}

				public void setUpdatedAt(String updatedAt){
					this.updatedAt = updatedAt;
				}

				public String getUpdatedAt(){
					return updatedAt;
				}

				public void setRoleId(String roleId){
					this.roleId = roleId;
				}

				public String getRoleId(){
					return roleId;
				}

				public void setId(int id){
					this.id = id;
				}

				public int getId(){
					return id;
				}

				public void setWolooId(String wolooId){
					this.wolooId = wolooId;
				}

				public String getWolooId(){
					return wolooId;
				}

				public void setEmail(String email){
					this.email = email;
				}

				public String getEmail(){
					return email;
				}

				public void setPincode(String pincode){
					this.pincode = pincode;
				}

				public String getPincode(){
					return pincode;
				}

				/*public void setSettings(List<String> settings){
					this.settings = settings;
				}

				public List<String> getSettings(){
					return settings;
				}*/

				public void setAddress(String address){
					this.address = address;
				}

				public String getAddress(){
					return address;
				}

				public void setExpiryDate(String expiryDate){
					this.expiryDate = expiryDate;
				}

				public String getExpiryDate(){
					return expiryDate;
				}

				public void setMobile(String mobile){
					this.mobile = mobile;
				}

				public String getMobile(){
					return mobile;
				}

				public void setOtp(int otp){
					this.otp = otp;
				}

				public int getOtp(){
					return otp;
				}

				public void setAvatar(String avatar){
					this.avatar = avatar;
				}

				public String getAvatar(){
					return avatar;
				}

				public void setSponsorId(String sponsorId){
					this.sponsorId = sponsorId;
				}

				public String getSponsorId(){
					return sponsorId;
				}

				public void setDeletedAt(String deletedAt){
					this.deletedAt = deletedAt;
				}

				public String getDeletedAt(){
					return deletedAt;
				}

				public void setGpId(String gpId){
					this.gpId = gpId;
				}

				public String getGpId(){
					return gpId;
				}

				public void setFbId(String fbId){
					this.fbId = fbId;
				}

				public String getFbId(){
					return fbId;
				}

				public void setDob(String dob){
					this.dob = dob;
				}

				public String getDob(){
					return dob;
				}

				public void setName(String name){
					this.name = name;
				}

				public String getName(){
					if(name == null ||name.equals("")){
						return "";
					}else {
						return name;
					}
				}

				public void setVoucherId(String voucherId){
					this.voucherId = voucherId;
				}

				public String getVoucherId(){
					return voucherId;
				}

				public void setStatus(String status){
					this.status = status;
				}

				public String getStatus(){
					return status;
				}
			}


		}

	}




}