package in.woloo.www.database.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.utils.Logger;


public class SharedPreference {
    private static final String TAG = SharedPreference.class.getSimpleName();
    private DeCryptor deCryptor;
    private EnCryptor enCryptor;


    public SharedPreference(Context context) {
        try {
            deCryptor = new DeCryptor();
            enCryptor = new EnCryptor();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }


    private SharedPreferences getPreferenceModePrivate(Context mContext, String key) {
        return mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
    }


    public void setStoredPreference(Context mContext, String key, String value) {
        if (mContext == null) {
            mContext = WolooApplication.getInstance();
        }
        SharedPreferences sp = getPreferenceModePrivate(mContext, key);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStoredPreference(Context mContext, String key) {
        if (mContext == null) {
            mContext = WolooApplication.getInstance();
        }
        SharedPreferences sp = getPreferenceModePrivate(mContext, key);
        return sp.getString(key, null);
//       return getProperty(mContext,key,"");
    }

    /**
     * getStoredPreference
     *
     * @param mContext      context
     * @param key
     * @param defaultString
     * @return
     */
    public String getStoredPreference(Context mContext, String key, String defaultString) {
        if (mContext == null) {
            mContext = WolooApplication.getInstance();
        }

        SharedPreferences sp = getPreferenceModePrivate(mContext, key);
        return sp.getString(key, defaultString);
//        return getProperty(mContext,key,defaultString);
    }


    public boolean getStoredBooleanPreference(Context mContext, String key, boolean defaultValue) {
        if (mContext == null) {
            mContext = WolooApplication.getInstance();
        }
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences sp = getPreferenceModePrivate(mContext, key);
        return sp.getBoolean(key, defaultValue);

    }


    public void setStoredBooleanPreference(Context mContext, String key, boolean value) {
        if (mContext == null) {
            mContext = WolooApplication.getInstance();
        }
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences sp = getPreferenceModePrivate(mContext, key);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void removeOnClearAppData(Context mContext, String key) {
//        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences mySPrefs = getPreferenceModePrivate(mContext, key);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(key);
        editor.apply();
    }

    public void removeAllUserData(Context mContext) {
        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        //SharedPreferences mySPrefs = getPreferenceModePrivate(mContext, key);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.clear();
        editor.apply();
        mContext.getSharedPreferences(SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), 0).edit().clear().apply();
        mContext.getSharedPreferences(SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), 0).edit().clear().apply();
        mContext.getSharedPreferences(SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(), 0).edit().clear().apply();
    }

    public void clearStoredPreference(Context mContext, String key) {
        if (mContext == null) {
            mContext = WolooApplication.getInstance();
        }
        SharedPreferences sp = getPreferenceModePrivate(mContext, key);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }


    public String getProperty(Context context, String key, String defaultValue) {
//        EncryptInfo mInfo = new EncryptInfo();
        String data = getPreferenceModePrivate(context, key + "_data")
                .getString(key, null);

        String iv = getPreferenceModePrivate(context, key + "_iv")
                .getString(key + "_iv", null);
        if (data == null || iv == null)
            return defaultValue;

//        byte[] ivBase = Base64.decode(iv, Base64.DEFAULT);
        byte[] ivBase = iv.getBytes(StandardCharsets.ISO_8859_1);
//        mInfo.setData(data);
//        mInfo.setIv(ivBase);
        String mStrDecrypt = defaultValue;
        try {

            if (data != null) {
                byte[] bytes = data.getBytes(StandardCharsets.ISO_8859_1);
                mStrDecrypt = deCryptor.decryptData(key, bytes, ivBase);
            }
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        } /*catch (NoSuchAlgorithmException e) {
             CommonUtils.printStackTrace(e);
        } catch (KeyStoreException e) {
             CommonUtils.printStackTrace(e);
        } catch (NoSuchProviderException e) {
             CommonUtils.printStackTrace(e);
        } catch (NoSuchPaddingException e) {
             CommonUtils.printStackTrace(e);
        } catch (InvalidKeyException e) {
             CommonUtils.printStackTrace(e);
        } catch (IOException e) {
             CommonUtils.printStackTrace(e);
        } catch (BadPaddingException e) {
             CommonUtils.printStackTrace(e);
        } catch (IllegalBlockSizeException e) {
             CommonUtils.printStackTrace(e);
        } catch (InvalidAlgorithmParameterException e) {
             CommonUtils.printStackTrace(e);
        }*/
        return mStrDecrypt;

    }
}
