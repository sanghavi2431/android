package in.woloo.www.vtion.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class VitionGlobals {


    public final static  String TAG = "Vition";

    Context context;



    private SharedPreferences sharedPref;
    private Editor editor;

    private static final String SHARED = "Vition";
    private static final String OWNERSHIP = "OWNERSHIP";
    private static final String EDUCATION = "EDUCATION";
    private static final String AGE = "AGE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String DEVICEID = "DEVICEID";
    private static final String LATITUDE = "LATITUDE";



    public VitionGlobals(Context context) {
        sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        this.context=context;
    }


    public void setOwnership(String status) {
        editor.putString(OWNERSHIP, status);
        editor.commit();
    }
    public String getOwnership() {
        return sharedPref.getString(OWNERSHIP, "");
    }

    public void setEducation(String status) {
        editor.putString(EDUCATION, status);
        editor.commit();
    }
    public String getEducation() {
        return sharedPref.getString(EDUCATION, "");
    }




    public void setLongitude(String status) {
        editor.putString(LONGITUDE, status);
        editor.commit();
    }
    public String getLongitude() {
        return sharedPref.getString(LONGITUDE, "");
    }


    public void setLatitude(String status) {
        editor.putString(LATITUDE, status);
        editor.commit();
    }
    public String getLatitude() {
        return sharedPref.getString(LATITUDE, "");
    }



    public void setDeviceId(String status) {
        editor.putString(DEVICEID, status);
        editor.commit();
    }
    public String getDeviceId() {
        return sharedPref.getString(DEVICEID, "");
    }




    public void setAge(String userName) {
        editor.putString(AGE, userName);
        editor.commit();
    }
    public String getAge() {
        return sharedPref.getString(AGE, "");
    }




}
