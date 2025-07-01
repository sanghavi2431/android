package in.woloo.www.my_history.model;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import in.woloo.www.dailylogscreen.models.DailyLogSymptoms;

public class MoodsModel {


    List<DailyLogSymptoms> bleeding;
    List<DailyLogSymptoms> mood;
    List<DailyLogSymptoms> symptoms;
    List<DailyLogSymptoms> sex_and_sex_drive;

    public List<DailyLogSymptoms> getBleeding() {
        return bleeding;
    }

    public void setBleeding(List<DailyLogSymptoms> bleeding) {
        this.bleeding = bleeding;
    }

    public List<DailyLogSymptoms> getMood() {
        return mood;
    }

    public void setMood(List<DailyLogSymptoms> mood) {
        this.mood = mood;
    }

    public List<DailyLogSymptoms> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<DailyLogSymptoms> symptoms) {
        this.symptoms = symptoms;
    }

    public List<DailyLogSymptoms> getSex_and_sex_drive() {
        return sex_and_sex_drive;
    }

    public void setSex_and_sex_drive(List<DailyLogSymptoms> sex_and_sex_drive) {
        this.sex_and_sex_drive = sex_and_sex_drive;
    }
}
