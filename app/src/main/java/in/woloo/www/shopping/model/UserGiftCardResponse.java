package in.woloo.www.shopping.model;

import com.google.gson.annotations.SerializedName;

public class UserGiftCardResponse{

	@SerializedName("woloo_point")
	private int wolooPoint;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("gift_coins")
	private int giftCoins;

	public int getWolooPoint(){
		return wolooPoint;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	public int getGiftCoins(){
		return giftCoins;
	}
}