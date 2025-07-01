package in.woloo.www.dailylogscreen.models;

import java.util.Objects;

public class DailyLogSymptoms {
    public String subTitleName;

    public int imageURL;

    public String getSubTitleName() {
        return subTitleName;
    }

    public void setSubTitleName(String subTitleName) {
        this.subTitleName = subTitleName;
    }

    public int getImageURL() {
        return imageURL;
    }

    public void setImageURL(int imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyLogSymptoms that = (DailyLogSymptoms) o;
        return imageURL == that.imageURL && subTitleName.equals(that.subTitleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subTitleName, imageURL);
    }
}
