package in.woloo.www.refer_woloo_host.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ReferredWolooListResponse {

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public List<DataItem> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}

	public class DataItem{

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

		@SerializedName("image")
		private List<String> image;

		@SerializedName("pincode")
		private int pincode;

		@SerializedName("address")
		private String address;

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

		@SerializedName("deleted_at")
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

		public String getTitle(){
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

		public int getRecommendedBy(){
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

		public List<String> getImage(){
			return image;
		}

		public int getPincode(){
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

		public String getRestaurant(){
			return restaurant;
		}

		public int getIsCleanAndHygiene(){
			return isCleanAndHygiene;
		}

		public int getIsWashroom(){
			return isWashroom;
		}

		public String getDeletedAt(){
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

		public String getUserId(){
			return userId;
		}

		public String getName(){
			return name;
		}

		public String getOpeningHours(){
			return openingHours;
		}

		public String getRecommendedMobile(){
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