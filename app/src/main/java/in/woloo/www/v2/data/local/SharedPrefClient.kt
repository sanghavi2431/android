package `in`.woloo.www.v2.data.local

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import `in`.woloo.www.app.WolooApplication


class SharedPrefClient {

    private var sharedPreferences: SharedPreferences = WolooApplication.instance!!.getSharedPreferences(
        SharedPrefSettings.SHARED_PREF_NAME, MODE_PRIVATE)

    companion object {
        var mSharedPrefClient: SharedPrefClient? = null
        val getSharedPrefClient: SharedPrefClient
            get() {
                if (mSharedPrefClient == null) {
                    mSharedPrefClient = SharedPrefClient()
                }
                return mSharedPrefClient!!
            }
    }


    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    fun removeKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun setString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defValue: String): String? {
        return sharedPreferences.getString(key, defValue)
    }

    fun setLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    fun getLong(key: String, defValue: Long): Long {
        return sharedPreferences.getLong(key, defValue)
    }

    fun setInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    fun getString(key: String): String? {
        return getString(key, "")
    }

    fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

    fun getBoolean(key: String): Boolean {
        return getBoolean(key, false)
    }
}