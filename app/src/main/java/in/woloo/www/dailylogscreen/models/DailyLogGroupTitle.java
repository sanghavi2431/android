package in.woloo.www.dailylogscreen.models;

import java.util.ArrayList;
import java.util.List;

import in.woloo.www.database.preference.SharedPreferencesEnum;

public class DailyLogGroupTitle {
    public SharedPreferencesEnum id;
    public String groupName;
    public List<DailyLogSubTitle> subTitle;
}
