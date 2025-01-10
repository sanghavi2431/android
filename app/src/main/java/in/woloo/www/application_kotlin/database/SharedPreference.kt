package `in`.woloo.www.application_kotlin.database

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.utils.Logger
import java.nio.charset.StandardCharsets

class SharedPreference(context: Context?) {
    private var deCryptor: DeCryptor? = null
    private var enCryptor: EnCryptor? = null

    init {
        try {
            deCryptor = DeCryptor()
            enCryptor = EnCryptor()
        } catch (e: Exception) {
            Logger.e(TAG, e)
        }
    }

    private fun getPreferenceModePrivate(mContext: Context, key: String): SharedPreferences {
        return mContext.getSharedPreferences(key, Context.MODE_PRIVATE)
    }

    fun setStoredPreference(mContext: Context?, key: String, value: String?) {
        var mContext = mContext
        if (mContext == null) {
            mContext = WolooApplication.instance!!
        }
        val sp = getPreferenceModePrivate(mContext!!, key)
        val editor = sp.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStoredPreference(mContext: Context?, key: String): String? {
        var mContext = mContext
        if (mContext == null) {
            mContext = WolooApplication.instance!!
        }
        val sp = getPreferenceModePrivate(mContext!!, key)
        return sp.getString(key, null)
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
    fun getStoredPreference(mContext: Context?, key: String, defaultString: String?): String? {
        var mContext = mContext
        if (mContext == null) {
            mContext = WolooApplication.instance!!
        }
        val sp = getPreferenceModePrivate(mContext, key)
        return sp.getString(key, defaultString)
        //        return getProperty(mContext,key,defaultString);
    }

    fun getStoredBooleanPreference(
        mContext: Context?,
        key: String,
        defaultValue: Boolean
    ): Boolean {
        var mContext = mContext
        if (mContext == null) {
            mContext = WolooApplication.instance!!
        }
        //        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        val sp = getPreferenceModePrivate(mContext!!, key)
        return sp.getBoolean(key, defaultValue)
    }

    fun setStoredBooleanPreference(mContext: Context?, key: String, value: Boolean) {
        var mContext = mContext
        if (mContext == null) {
            mContext = WolooApplication.instance!!
        }
        //        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        val sp = getPreferenceModePrivate(mContext!!, key)
        val editor = sp.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun removeOnClearAppData(mContext: Context, key: String) {
//        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        val mySPrefs = getPreferenceModePrivate(mContext, key)
        val editor = mySPrefs.edit()
        editor.remove(key)
        editor.apply()
    }

    fun removeAllUserData(mContext: Context) {
        val mySPrefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        //SharedPreferences mySPrefs = getPreferenceModePrivate(mContext, key);
        val editor = mySPrefs.edit()
        editor.clear()
        editor.apply()
        mContext.getSharedPreferences(SharedPreferencesEnum.PERIOD_LENGTH.preferenceKey, 0).edit()
            .clear().apply()
        mContext.getSharedPreferences(SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.preferenceKey, 0)
            .edit().clear().apply()
        mContext.getSharedPreferences(SharedPreferencesEnum.PERIOD_STARTING_DATE.preferenceKey, 0)
            .edit().clear().apply()
    }

    fun clearStoredPreference(mContext: Context?, key: String) {
        var mContext = mContext
        if (mContext == null) {
            mContext = WolooApplication.instance!!
        }
        val sp = getPreferenceModePrivate(mContext!!, key)
        val editor = sp.edit()
        editor.remove(key)
        editor.apply()
    }

    fun getProperty(context: Context, key: String, defaultValue: String): String {
//        EncryptInfo mInfo = new EncryptInfo();
        val data = getPreferenceModePrivate(context, key + "_data")
            .getString(key, null)
        val iv = getPreferenceModePrivate(context, key + "_iv")
            .getString(key + "_iv", null)
        if (data == null || iv == null) return defaultValue

//        byte[] ivBase = Base64.decode(iv, Base64.DEFAULT);
        val ivBase = iv.toByteArray(StandardCharsets.ISO_8859_1)
        //        mInfo.setData(data);
//        mInfo.setIv(ivBase);
        var mStrDecrypt = defaultValue
        try {
            if (data != null) {
                val bytes = data.toByteArray(StandardCharsets.ISO_8859_1)
                mStrDecrypt = deCryptor!!.decryptData(key, bytes, ivBase)
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
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
        return mStrDecrypt
    }

    companion object {
        private val TAG = SharedPreference::class.java.simpleName
    }
}
