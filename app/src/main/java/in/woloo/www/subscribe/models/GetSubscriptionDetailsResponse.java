package in.woloo.www.subscribe.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetSubscriptionDetailsResponse{

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

	public class FutureSubscription{

		@SerializedName("image")
		private String image;

		@SerializedName("backgroud_color")
		private String backgroudColor;

		@SerializedName("price_with_gst")
		private String priceWithGst;

		@SerializedName("description")
		private String description;

		@SerializedName("discount")
		private String discount;

		@SerializedName("created_at")
		private String createdAt;

		@SerializedName("shield_color")
		private String shieldColor;

		@SerializedName("is_voucher")
		private int isVoucher;

		@SerializedName("deleted_at")
		private Object deletedAt;

		@SerializedName("frequency")
		private String frequency;

		@SerializedName("is_recommended")
		private int isRecommended;

		@SerializedName("is_expired")
		private int isExpired;

		@SerializedName("updated_at")
		private String updatedAt;

		@SerializedName("price")
		private String price;

		@SerializedName("name")
		private String name;

		@SerializedName("days")
		private int days;

		@SerializedName("currency")
		private String currency;

		@SerializedName("id")
		private int id;

		@SerializedName("before_discount_price")
		private int beforeDiscountPrice;

		@SerializedName("plan_id")
		private String planId;

		@SerializedName("status")
		private int status;

		@SerializedName("start_at")
		private String start_at;

		@SerializedName("end_at")
		private String end_at;

		public String getImage(){
			return image;
		}

		public String getBackgroudColor(){
			return backgroudColor;
		}

		public String getPriceWithGst(){
			return priceWithGst;
		}

		public String getDescription(){
			return description;
		}

		public String getDiscount(){
			return discount;
		}

		public String getCreatedAt(){
			return createdAt;
		}

		public String getShieldColor(){
			return shieldColor;
		}

		public int getIsVoucher(){
			return isVoucher;
		}

		public Object getDeletedAt(){
			return deletedAt;
		}

		public String getFrequency(){
			return frequency;
		}

		public int getIsRecommended(){
			return isRecommended;
		}

		public int getIsExpired(){
			return isExpired;
		}

		public String getUpdatedAt(){
			return updatedAt;
		}

		public String getPrice(){
			return price;
		}

		public String getName(){
			return name;
		}

		public int getDays(){
			return days;
		}

		public String getCurrency(){
			return currency;
		}

		public int getId(){
			return id;
		}

		public int getBeforeDiscountPrice(){
			return beforeDiscountPrice;
		}

		public String getPlanId(){
			return planId;
		}

		public int getStatus(){
			return status;
		}

		public String getStart_at() {
			return start_at;
		}

		public String getEnd_at() {
			return end_at;
		}
	}

	public class Data{

		@SerializedName("futureSubscription")
		private ArrayList<FutureSubscription> futureSubscription;

		@SerializedName("activeSubscription")
		private ActiveSubscription activeSubscription;

		@SerializedName("purchase_by")
		private String purchase_by;

		public String getPurchase_by() {
			return purchase_by;
		}

		public  ArrayList<FutureSubscription> getFutureSubscription(){
			return futureSubscription;
		}

		public ActiveSubscription getActiveSubscription(){
			return activeSubscription;
		}
	}

	public class ActiveSubscription{

		@SerializedName("image")
		private String image;

		@SerializedName("backgroud_color")
		private Object backgroudColor;

		@SerializedName("price_with_gst")
		private String priceWithGst;

		@SerializedName("description")
		private String description;

		@SerializedName("discount")
		private String discount;

		@SerializedName("created_at")
		private String createdAt;

		@SerializedName("shield_color")
		private Object shieldColor;

		@SerializedName("is_voucher")
		private int isVoucher;

		@SerializedName("deleted_at")
		private Object deletedAt;

		@SerializedName("frequency")
		private String frequency;

		@SerializedName("is_recommended")
		private int isRecommended;

		@SerializedName("is_expired")
		private int isExpired;

		@SerializedName("updated_at")
		private String updatedAt;

		@SerializedName("price")
		private String price;

		@SerializedName("name")
		private String name;

		@SerializedName("days")
		private String days;

		@SerializedName("currency")
		private String currency;

		@SerializedName("id")
		private int id;

		@SerializedName("before_discount_price")
		private Integer beforeDiscountPrice;

		@SerializedName("plan_id")
		private String planId;

		@SerializedName("corporate_name")
		private String corporate_name;

		@SerializedName("status")
		private int status;

		@SerializedName("start_at")
		private String start_at;

		@SerializedName("end_at")
		private String end_at;

		@SerializedName("gifted_by")
		private String gifted_by;

		public String getGifted_by() {
			return gifted_by;
		}

		public String getImage(){
			return image;
		}

		public Object getBackgroudColor(){
			return backgroudColor;
		}

		public String getPriceWithGst(){
			return priceWithGst;
		}

		public String getDescription(){
			return description;
		}

		public String getDiscount(){
			return discount;
		}

		public String getCreatedAt(){
			return createdAt;
		}

		public Object getShieldColor(){
			return shieldColor;
		}

		public int getIsVoucher(){
			return isVoucher;
		}

		public Object getDeletedAt(){
			return deletedAt;
		}

		public String getFrequency(){
			return frequency;
		}

		public int getIsRecommended(){
			return isRecommended;
		}

		public int getIsExpired(){
			return isExpired;
		}

		public String getUpdatedAt(){
			return updatedAt;
		}

		public String getPrice(){
			return price;
		}

		public String getName(){
			return name;
		}

		public String getDays(){
			return days;
		}

		public String getCurrency(){
			return currency;
		}

		public int getId(){
			return id;
		}

		public Integer getBeforeDiscountPrice(){
			return beforeDiscountPrice;
		}

		public String getPlanId(){
			return planId;
		}

		public int getStatus(){
			return status;
		}

		public String getStart_at() {
			return start_at;
		}

		public String getEnd_at() {
			return end_at;
		}

		public String getCorporate_name() {
			return corporate_name;
		}
	}
}