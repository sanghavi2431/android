/*
 * Copyright (c) 2018 - 5 - 1. JetSynthesys Pvt. Ltd.
 * @Rahul Pawar
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package in.woloo.www.firebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.utils.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NotificationDB extends SQLiteOpenHelper {

    private static final String TAG = "NotificationDB";


    private static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "NOTIFICATION_DB";
    private static String TABLE_NAME = "NOTIFICATION";
    private static String COL_NOTIFICATION_TITLE = "Notification_Title";
    public static  String COL_NOTIFICATION_USER_ID="userMobile";
    private static String COL_NOTIFICATION_DESCRIPTION = "Notification_Description";
    private static String COL_NOTIFICATION_CATEGORY = "Notification_Category";
    private static String COL_NOTIFICATION_LANDING_URL = "Notification_Landing_Url";
    private static String COL_NOTIFICATION_IMAGE_URL = "Notification_Image_Url";
    private static String COL_NOTIFICATION_READ = "Notification_read";
    private String COL_NOTIFICATION_DATE = "Notification_Date";
    private DateFormat dateFormat = null;
    private Date date = null;
//    private static NotificationDB singleton;
    /*public static synchronized NotificationDB getInstance(){
        return  singleton;
    }*/
    public NotificationDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        if (singleton == null)
//            singleton = NotificationDB.this;

        String COL_NOTIFICATION_ID = "Notification_Id";
        db.execSQL("create table " + TABLE_NAME + "("+ COL_NOTIFICATION_ID +" integer primary key autoincrement," +
                COL_NOTIFICATION_TITLE + " text," +
                COL_NOTIFICATION_DESCRIPTION + " text," +
                COL_NOTIFICATION_CATEGORY + " text," +
                COL_NOTIFICATION_DATE + " text," +
                COL_NOTIFICATION_IMAGE_URL + " text," +
                COL_NOTIFICATION_READ + " text," +
                COL_NOTIFICATION_USER_ID + " text," +
                COL_NOTIFICATION_LANDING_URL + " text)");

    }

    public void deleteNotification(String date) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_NAME, COL_NOTIFICATION_DATE + "='" + date+"'", null);
        } catch (Exception e) {
        }
    }

    public int getNotificationCount(){
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    private String getCurrentDate() {
      /*  if (dateFormat == null)
            dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        if (date == null)
            date = new Date();*/
        return DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    }

    public void addNotifications(String title, String summaryText, String contentText, String imageUrl,
                                 String landingUrl, String read,String userId) {

        try {
            ContentValues cv = new ContentValues();

            cv.put(COL_NOTIFICATION_TITLE, title);
            cv.put(COL_NOTIFICATION_DESCRIPTION, contentText);
            cv.put(COL_NOTIFICATION_CATEGORY, summaryText);
            cv.put(COL_NOTIFICATION_DATE, getTodayDate());
            cv.put(COL_NOTIFICATION_LANDING_URL, landingUrl);
            cv.put(COL_NOTIFICATION_IMAGE_URL, imageUrl);
            cv.put(COL_NOTIFICATION_READ, read);
            cv.put(COL_NOTIFICATION_USER_ID, userId);
//        cv.put(COL_NOTIFICATION_ID, notificationId);
            SQLiteDatabase db = getWritableDatabase();
            long i = db.insertOrThrow(TABLE_NAME, null, cv);
            Logger.e("test",cv.toString());
        } catch (SQLException e) {
              CommonUtils.printStackTrace(e);
        }
//        System.out.println("NOTIFICATION I "+i);

    }


    public void updateNotificationStatus(String userMobile, String value){
        SQLiteDatabase mDataBase = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_NOTIFICATION_READ, "1");
            String[] args = new String[]{"0",userMobile};
            int result = mDataBase.update(TABLE_NAME, values, "Notification_read=? AND userMobile=?", args);
            Logger.e(TABLE_NAME, ""+result);
        } catch (SQLiteException e) {
             CommonUtils.printStackTrace(e);
        }finally {
            mDataBase.close();
        }
    }

    /**
     * get unread message count
     */
    public int  getUnReadCount(String userMobile,String value) {
        int data = 0;
        SQLiteDatabase mDataBase = this.getWritableDatabase();
        Cursor cur = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cur = mDataBase.rawQuery("select * from " + TABLE_NAME + " where " + COL_NOTIFICATION_READ + " = " + value + " AND " + COL_NOTIFICATION_USER_ID + " = " + userMobile + "", null);
            if (cur != null) {
                data=cur.getCount();
            }
        } catch (SQLException e) {
              CommonUtils.printStackTrace(e);
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        } finally {
            if (cur != null)
                cur.close();
            mDataBase.close();
        }
        return data;
    }

    public ArrayList<NotificationBean> getNotifications(String userMobile) {

        ArrayList<NotificationBean> notificationListBeans = new ArrayList<NotificationBean>();

        ArrayList<NotificationBean> beanArrayList = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
          //  String query="select * from " + TABLE_NAME + " WHERE " + COL_NOTIFICATION_USER_ID + " = " + userMobile + "";
            //Cursor c = db.rawQuery(query, null);
            Cursor c = db.rawQuery("select * from " + TABLE_NAME, null);
            if (c.moveToFirst()) {
                do {
                    NotificationBean notificationBean =new NotificationBean();
                    notificationBean.setTitle(c.getString(c.getColumnIndex(COL_NOTIFICATION_TITLE)));
                    notificationBean.setDescription(c.getString(c.getColumnIndex(COL_NOTIFICATION_DESCRIPTION)));
                    notificationBean.setCategory(c.getString(c.getColumnIndex(COL_NOTIFICATION_CATEGORY)));
                    notificationBean.setDate(c.getString(c.getColumnIndex(COL_NOTIFICATION_DATE)));
                    notificationBean.setNotificationImageUrl(c.getString(c.getColumnIndex(COL_NOTIFICATION_IMAGE_URL)));
                    notificationBean.setLandingUrl(c.getString(c.getColumnIndex(COL_NOTIFICATION_LANDING_URL)));
                    notificationBean.setRead(c.getString(c.getColumnIndex(COL_NOTIFICATION_READ)));
                    notificationListBeans.add(notificationBean);

    //                System.out.println("CUR NOTIF "+c.getColumnIndex(COL_NOTIFICATION_READ));
                } while (c.moveToNext());
            }
            beanArrayList = new ArrayList<>();

            for (int j = notificationListBeans.size(); j > 0; j--) {
                beanArrayList.add(notificationListBeans.get(j - 1));
            }
//        System.out.println("CURSOR "+ beanArrayList);

            if (c!=null);
            c.close();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }

        return beanArrayList;
    }

    /*public void deleteOutDatedNotifications() {

        ArrayList<NotificationBean> notificationBeanArrayList = getNotifications();

        for (NotificationBean bean : notificationBeanArrayList) {
            compareDateAndDeleteNotification(bean.getDate(), bean.getId());
        }
    }
*/

    public long deleteAllNotification(){
        SQLiteDatabase db = this.getWritableDatabase();
        return (long) db.delete(TABLE_NAME, null, null);
    }
    private void compareDateAndDeleteNotification(String endDateString, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateTimeString = sdf.format(new Date());

        try {
            Date currentDate = sdf.parse(currentDateTimeString);

            Date endDate = sdf.parse(endDateString);

            long different = currentDate.getTime() - endDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;
            long elapsedDays = different / daysInMilli;

            if (elapsedDays > 44)
                deleteNotification(date);

        } catch (Exception e) {

        }
    }

    private String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
