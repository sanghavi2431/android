package in.woloo.www.period_tracker.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Log {

	@SerializedName("sexDrive")
	private List<String> sexDrive;

	@SerializedName("habits")
	private List<String> habits;

	@SerializedName("bleeding")
	private List<String> bleeding;

	@SerializedName("mood")
	private List<String> mood;

	@SerializedName("menstruation")
	private List<String> menstruation;

	@SerializedName("diseasesandmedication")
	private List<String> diseasesandmedication;

	@SerializedName("premenstruation")
	private List<String> premenstruation;

	public void setSexDrive(List<String> sexDrive){
		this.sexDrive = sexDrive;
	}

	public List<String> getSexDrive(){
		return sexDrive;
	}

	public void setHabits(List<String> habits){
		this.habits = habits;
	}

	public List<String> getHabits(){
		return habits;
	}

	public void setBleeding(List<String> bleeding){
		this.bleeding = bleeding;
	}

	public List<String> getBleeding(){
		return bleeding;
	}

	public void setMood(List<String> mood){
		this.mood = mood;
	}

	public List<String> getMood(){
		return mood;
	}

	public void setMenstruation(List<String> menstruation){
		this.menstruation = menstruation;
	}

	public List<String> getMenstruation(){
		return menstruation;
	}

	public void setDiseasesandmedication(List<String> diseasesandmedication){
		this.diseasesandmedication = diseasesandmedication;
	}

	public List<String> getDiseasesandmedication(){
		return diseasesandmedication;
	}

	public void setPremenstruation(List<String> premenstruation){
		this.premenstruation = premenstruation;
	}

	public List<String> getPremenstruation(){
		return premenstruation;
	}
}