package in.woloo.www.period_tracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPeriodDataResponse{

	@SerializedName("code")
	private String code;

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public String getCode(){
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

		@SerializedName("period_date")
		private String periodDate;

		@SerializedName("cycle_lenght")
		private int cycleLenght;

		@SerializedName("updated_at")
		private String updatedAt;

		@SerializedName("user_id")
		private int userId;

		@SerializedName("log")
		private Log log;

		@SerializedName("created_at")
		private String createdAt;

		@SerializedName("id")
		private int id;

		@SerializedName("period_length")
		private int periodLength;

		@SerializedName("luteal_length")
		private String lutealLength;

		public String getPeriodDate(){
			return periodDate;
		}

		public int getCycleLenght(){
			return cycleLenght;
		}

		public String getUpdatedAt(){
			return updatedAt;
		}

		public int getUserId(){
			return userId;
		}

		public Log getLog(){
			return log;
		}

		public String getCreatedAt(){
			return createdAt;
		}

		public int getId(){
			return id;
		}

		public int getPeriodLength(){
			return periodLength;
		}

		public String getLutealLength(){
			return lutealLength;
		}
	}
}