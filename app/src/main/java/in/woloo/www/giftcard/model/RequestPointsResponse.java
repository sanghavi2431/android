package in.woloo.www.giftcard.model;

import com.google.gson.annotations.SerializedName;

public class RequestPointsResponse{

	@SerializedName("data")
	private Data data;

	public Data getData(){
		return data;
	}

	public class Data{

		@SerializedName("message")
		private String message;

		@SerializedName("order_id")
		private String orderId;

		@SerializedName("status")
		private String status;

		public String getMessage(){
			return message;
		}

		public String getOrderId(){
			return orderId;
		}

		public String getStatus(){
			return status;
		}
	}
}