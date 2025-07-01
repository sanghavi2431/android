package in.woloo.www.more.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VoucherDetailsResponse{

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

	public class User{

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
		private int roleId;

		@SerializedName("id")
		private int id;

		@SerializedName("woloo_id")
		private String wolooId;

		@SerializedName("email")
		private String email;

		@SerializedName("pincode")
		private String pincode;

		@SerializedName("settings")
		private List<String> settings;

		@SerializedName("address")
		private String address;

		@SerializedName("expiry_date")
		private String expiryDate;

		@SerializedName("mobile")
		private String mobile;

		@SerializedName("otp")
		private int otp;

		@SerializedName("avatar")
		private String avatar;

		@SerializedName("sponsor_id")
		private String sponsorId;

		@SerializedName("deleted_at")
		private String deletedAt;

		@SerializedName("gp_id")
		private String gpId;

		@SerializedName("fb_id")
		private String fbId;

		@SerializedName("dob")
		private String dob;

		@SerializedName("name")
		private String name;

		@SerializedName("voucher_id")
		private int voucherId;

		@SerializedName("status")
		private String status;

		public String getGender(){
			return gender;
		}

		public String getCity(){
			return city;
		}

		public String getCreatedAt(){
			return createdAt;
		}

		public int getIsFirstSession(){
			return isFirstSession;
		}

		public String getRefCode(){
			return refCode;
		}

		public String getSubscriptionId(){
			return subscriptionId;
		}

		public String getUpdatedAt(){
			return updatedAt;
		}

		public int getRoleId(){
			return roleId;
		}

		public int getId(){
			return id;
		}

		public String getWolooId(){
			return wolooId;
		}

		public String getEmail(){
			return email;
		}

		public String getPincode(){
			return pincode;
		}

		public List<String> getSettings(){
			return settings;
		}

		public String getAddress(){
			return address;
		}

		public String getExpiryDate(){
			return expiryDate;
		}

		public String getMobile(){
			return mobile;
		}

		public int getOtp(){
			return otp;
		}

		public String getAvatar(){
			return avatar;
		}

		public String getSponsorId(){
			return sponsorId;
		}

		public String getDeletedAt(){
			return deletedAt;
		}

		public String getGpId(){
			return gpId;
		}

		public String getFbId(){
			return fbId;
		}

		public String getDob(){
			return dob;
		}

		public String getName(){
			return name;
		}

		public int getVoucherId(){
			return voucherId;
		}

		public String getStatus(){
			return status;
		}
	}

	public class Subscription{

		@SerializedName("days")
		private String days;

		public String getDays(){
			return days;
		}
	}

	public class Data{

		@SerializedName("subscription")
		private Subscription subscription;

		@SerializedName("user")
		private User user;

		public Subscription getSubscription(){
			return subscription;
		}

		public User getUser(){
			return user;
		}
	}
}