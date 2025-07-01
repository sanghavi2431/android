package in.woloo.www.dailylogscreen.models;

import java.io.Serializable;

public class DailyLogSubTitle implements Serializable {
    public DailyLogSubTitle() {
    }

    public DailyLogSubTitle(String subTitleName, int imageUrl, boolean isChecked) {
        this.subTitleName = subTitleName;
        this.imageUrl = imageUrl;
        this.isChecked = isChecked;
    }

    public int id;
    public String subTitleName;
    public int imageUrl;
    public boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
