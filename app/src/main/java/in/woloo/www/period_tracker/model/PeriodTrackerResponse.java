package in.woloo.www.period_tracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PeriodTrackerResponse{

	@SerializedName("code")
	private String code;

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

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

		@SerializedName("period_date")
		private String periodDate;

		@SerializedName("cycle_length")
		private String cycleLenght;

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
		private String periodLength;

		@SerializedName("luteal_length")
		private String lutealLength;

		public void setPeriodDate(String periodDate){
			this.periodDate = periodDate;
		}

		public String getPeriodDate(){
			return periodDate;
		}

		public void setCycleLenght(String cycleLenght){
			this.cycleLenght = cycleLenght;
		}

		public String getCycleLenght(){
			return cycleLenght;
		}

		public void setUpdatedAt(String updatedAt){
			this.updatedAt = updatedAt;
		}

		public String getUpdatedAt(){
			return updatedAt;
		}

		public void setUserId(int userId){
			this.userId = userId;
		}

		public int getUserId(){
			return userId;
		}

		public void setLog(Log log){
			this.log = log;
		}

		public Log getLog(){
			return log;
		}

		public void setCreatedAt(String createdAt){
			this.createdAt = createdAt;
		}

		public String getCreatedAt(){
			return createdAt;
		}

		public void setId(int id){
			this.id = id;
		}

		public int getId(){
			return id;
		}

		public void setPeriodLength(String periodLength){
			this.periodLength = periodLength;
		}

		public String getPeriodLength(){
			return periodLength;
		}

		public void setLutealLength(String lutealLength){
			this.lutealLength = lutealLength;
		}

		public String getLutealLength(){
			return lutealLength;
		}
	}
}