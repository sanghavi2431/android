package in.woloo.www.trendingblog.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearByWolooAndOfferCountResponse {

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

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

		@SerializedName("offerCount")
		private int offerCount;

		@SerializedName("shopOffer")
		private List<ShopOfferItem> shopOffer;

		@SerializedName("wolooCount")
		private int wolooCount;

		public void setOfferCount(int offerCount){
			this.offerCount = offerCount;
		}

		public int getOfferCount(){
			return offerCount;
		}

		public void setShopOffer(List<ShopOfferItem> shopOffer){
			this.shopOffer = shopOffer;
		}

		public List<ShopOfferItem> getShopOffer(){
			return shopOffer;
		}

		public void setWolooCount(int wolooCount){
			this.wolooCount = wolooCount;
		}

		public int getWolooCount(){
			return wolooCount;
		}
	}

	public class ShopOfferItem{

		@SerializedName("end_date")
		private String endDate;

		@SerializedName("coupon_code")
		private String couponCode;

		@SerializedName("description")
		private String description;

		@SerializedName("title")
		private String title;

		@SerializedName("product_ids")
		private String productIds;

		@SerializedName("date_time")
		private String dateTime;

		@SerializedName("sub_category_ids")
		private Object subCategoryIds;

		@SerializedName("value_unit")
		private String valueUnit;

		@SerializedName("vendors_ids")
		private String vendorsIds;

		@SerializedName("id")
		private String id;

		@SerializedName("category_ids")
		private String categoryIds;

		@SerializedName("value")
		private String value;

		@SerializedName("start_date")
		private String startDate;

		@SerializedName("status")
		private String status;

		public void setEndDate(String endDate){
			this.endDate = endDate;
		}

		public String getEndDate(){
			return endDate;
		}

		public void setCouponCode(String couponCode){
			this.couponCode = couponCode;
		}

		public String getCouponCode(){
			return couponCode;
		}

		public void setDescription(String description){
			this.description = description;
		}

		public String getDescription(){
			return description;
		}

		public void setTitle(String title){
			this.title = title;
		}

		public String getTitle(){
			return title;
		}

		public void setProductIds(String productIds){
			this.productIds = productIds;
		}

		public String getProductIds(){
			return productIds;
		}

		public void setDateTime(String dateTime){
			this.dateTime = dateTime;
		}

		public String getDateTime(){
			return dateTime;
		}

		public void setSubCategoryIds(Object subCategoryIds){
			this.subCategoryIds = subCategoryIds;
		}

		public Object getSubCategoryIds(){
			return subCategoryIds;
		}

		public void setValueUnit(String valueUnit){
			this.valueUnit = valueUnit;
		}

		public String getValueUnit(){
			return valueUnit;
		}

		public void setVendorsIds(String vendorsIds){
			this.vendorsIds = vendorsIds;
		}

		public String getVendorsIds(){
			return vendorsIds;
		}

		public void setId(String id){
			this.id = id;
		}

		public String getId(){
			return id;
		}

		public void setCategoryIds(String categoryIds){
			this.categoryIds = categoryIds;
		}

		public String getCategoryIds(){
			return categoryIds;
		}

		public void setValue(String value){
			this.value = value;
		}

		public String getValue(){
			return value;
		}

		public void setStartDate(String startDate){
			this.startDate = startDate;
		}

		public String getStartDate(){
			return startDate;
		}

		public void setStatus(String status){
			this.status = status;
		}

		public String getStatus(){
			return status;
		}
	}
}