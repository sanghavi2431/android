package in.woloo.www.shopping.model;

import com.google.gson.annotations.SerializedName;

public class UserGiftCardUpdateResponse {

	@SerializedName("transaction_id")
	private int transaction_id;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("gift_coins")
	private int giftCoins;

	public int getTransaction_id(){
		return transaction_id;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}


}