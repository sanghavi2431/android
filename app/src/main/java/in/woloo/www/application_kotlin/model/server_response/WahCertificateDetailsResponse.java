package in.woloo.www.application_kotlin.model.server_response;

import com.google.gson.annotations.SerializedName;

public class WahCertificateDetailsResponse {

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public int getCode(){
		return code;
	}

	public Data getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}

	public class Data{

		@SerializedName("code")
		private String code;

		@SerializedName("city")
		private String city;

		@SerializedName("description")
		private String description;

		@SerializedName("created_at")
		private String createdAt;

		@SerializedName("title")
		private Object title;

		@SerializedName("is_safe_space")
		private int isSafeSpace;

		@SerializedName("updated_at")
		private String updatedAt;

		@SerializedName("is_feeding_room")
		private int isFeedingRoom;

		@SerializedName("recommended_by")
		private Object recommendedBy;

		@SerializedName("id")
		private int id;

		@SerializedName("is_sanitizer_available")
		private int isSanitizerAvailable;

		@SerializedName("lat")
		private String lat;

		@SerializedName("image")
		private Object image;

		@SerializedName("pincode")
		private Object pincode;

		@SerializedName("address")
		private String address;

		@SerializedName("lng")
		private String lng;

		@SerializedName("is_makeup_room_available")
		private int isMakeupRoomAvailable;

		@SerializedName("restaurant")
		private Object restaurant;

		@SerializedName("is_clean_and_hygiene")
		private int isCleanAndHygiene;

		@SerializedName("is_washroom")
		private int isWashroom;

		@SerializedName("deleted_at")
		private Object deletedAt;

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
		private Object userId;

		@SerializedName("name")
		private String name;

		@SerializedName("opening_hours")
		private Object openingHours;

		@SerializedName("recommended_mobile")
		private Object recommendedMobile;

		@SerializedName("segregated")
		private String segregated;

		@SerializedName("status")
		private int status;

		@SerializedName("is_covid_free")
		private int isCovidFree;

		public String getCode(){
			return code;
		}

		public String getCity(){
			return city;
		}

		public String getDescription(){
			return description;
		}

		public String getCreatedAt(){
			return createdAt;
		}

		public Object getTitle(){
			return title;
		}

		public int getIsSafeSpace(){
			return isSafeSpace;
		}

		public String getUpdatedAt(){
			return updatedAt;
		}

		public int getIsFeedingRoom(){
			return isFeedingRoom;
		}

		public Object getRecommendedBy(){
			return recommendedBy;
		}

		public int getId(){
			return id;
		}

		public int getIsSanitizerAvailable(){
			return isSanitizerAvailable;
		}

		public String getLat(){
			return lat;
		}

		public Object getImage(){
			return image;
		}

		public Object getPincode(){
			return pincode;
		}

		public String getAddress(){
			return address;
		}

		public String getLng(){
			return lng;
		}

		public int getIsMakeupRoomAvailable(){
			return isMakeupRoomAvailable;
		}

		public Object getRestaurant(){
			return restaurant;
		}

		public int getIsCleanAndHygiene(){
			return isCleanAndHygiene;
		}

		public int getIsWashroom(){
			return isWashroom;
		}

		public Object getDeletedAt(){
			return deletedAt;
		}

		public int getIsCoffeeAvailable(){
			return isCoffeeAvailable;
		}

		public int getIsWheelchairAccessible(){
			return isWheelchairAccessible;
		}

		public int getIsSanitaryPadsAvailable(){
			return isSanitaryPadsAvailable;
		}

		public int getIsFranchise(){
			return isFranchise;
		}

		public int getIsPremium(){
			return isPremium;
		}

		public Object getUserId(){
			return userId;
		}

		public String getName(){
			return name;
		}

		public Object getOpeningHours(){
			return openingHours;
		}

		public Object getRecommendedMobile(){
			return recommendedMobile;
		}

		public String getSegregated(){
			return segregated;
		}

		public int getStatus(){
			return status;
		}

		public int getIsCovidFree(){
			return isCovidFree;
		}
	}
}