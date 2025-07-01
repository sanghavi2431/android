package in.woloo.www.giftSubscription.model;

import com.google.gson.annotations.SerializedName;

public class SendGiftCardResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("message")
	private String message;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("status")
	private String status;

	public int getCode(){
		return code;
	}

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