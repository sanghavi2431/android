package in.woloo.www.refer_woloo_host.model;

import com.google.gson.annotations.SerializedName;

public class ReferWolooResponse{

	@SerializedName("data")
	private Data data;

	public Data getData(){
		return data;
	}

	public class Data{

		@SerializedName("message")
		private String message;

		@SerializedName("status")
		private String status;

		public String getMessage(){
			return message;
		}

		public String getStatus(){
			return status;
		}
	}
}