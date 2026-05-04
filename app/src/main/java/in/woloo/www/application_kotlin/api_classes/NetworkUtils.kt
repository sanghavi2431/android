package `in`.woloo.www.application_kotlin.api_classes

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Proxy
import android.os.Build
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.interfaces.DialogCallListener
import `in`.woloo.www.common.CommonUtils
import java.net.NetworkInterface
import java.util.Collections

class NetworkUtils {
    private var mSharedPreference: SharedPreference? = null
    private var commonUtils: CommonUtils? = CommonUtils()
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    /* public boolean isConnected(Context context) {
        boolean isConnectedStatus = false;
        try {

            //here we first check the PROXY SETTING ENABLE OR NOT
            String connectedTo = checkConnectedToStatus(context);

            //VPN DISABLE CODE
           */
    /* if ("MOBILE_DATA".equals(connectedTo)) {
                isConnectedStatus = true;
            } else if ("WIFI".equals(connectedTo)) {
                //CHECKING WIFI SETTINGS
                isConnectedStatus = true;
            }*/
    /*

            //VPN ENABLE CODE
            boolean isVpnEnabled = isVpnEnabled();
            if ("MOBILE_DATA".equals(connectedTo)) {
                isConnectedStatus = true;
            } else if ("WIFI".equals(connectedTo)) {
                //CHECKING WIFI SETTINGS
                isConnectedStatus = getProxySettingDetails(context);
            } else {
                isConnectedStatus = false;
            }
            if (isVpnEnabled) {
                isConnectedStatus = false;
            }

        } catch (Exception e) {
              CommonUtils.printStackTrace(e)
            return false;
        }
        return isConnectedStatus;
    }*/
    fun isConnected(context: Context): Boolean {
        if (mSharedPreference == null) {
            mSharedPreference = SharedPreference(context)
        }
        if (commonUtils != null) {
            commonUtils = CommonUtils()
        }
        var isConnectedStatus = false
        try {
            //here we first check the PROXY SETTING ENABLE OR NOT
            val connectedTo = checkConnectedToStatus(context)
            val mStringAmazonTv = Build.MANUFACTURER


            // Mobile type vpn check
            //VPN DISABLE CODE
            /* if ("MOBILE_DATA".equals(connectedTo)) {
                isConnectedStatus = true;
            } else if ("WIFI".equals(connectedTo)) {
                //CHECKING WIFI SETTINGS
                isConnectedStatus = true;
            }*/
            //VPN ENABLE CHECK CODE
            val isVpnEnabled = isVpnEnabled
            isConnectedStatus = if ("MOBILE_DATA" == connectedTo) {
                true
            } else if ("WIFI" == connectedTo) {
                //CHECKING WIFI SETTINGS
                getProxySettingDetails(context)
            } else {
                false
            }
            if (isVpnEnabled) {
                isConnectedStatus = false
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
            return false
        }
        return isConnectedStatus
    }

    fun getProxySettingDetails(context: Context?): Boolean {
        var proxyAddress: String? = ""
        var portValue: String? = ""
        val port: Int
        var proxySettingEnable = false
        try {
            if (preICS()) {
                try {
                    proxyAddress = Proxy.getHost(context)
                    port = Proxy.getPort(context)
                    if (proxyAddress == null || port == 0) {
                        proxySettingEnable = true
                    }
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            } else {
                try {
                    proxyAddress = System.getProperty("http.proxyHost")
                    portValue = System.getProperty("http.proxyPort")
                    if (proxyAddress == null || portValue == null || portValue == "0") {
                        proxySettingEnable = true
                    }
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
        } catch (ex: Exception) {
            //ex.pr
            CommonUtils.printStackTrace(ex)
        }
        return proxySettingEnable
    }

    private fun preICS(): Boolean {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH
    }

    private fun checkConnectedToStatus(mContext: Context): String {
        var mContext: Context? = mContext
        var connectedTo = ""
        if (mContext == null) mContext = WolooApplication.instance!!
        val cm =
            mContext?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        connectedTo = if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                "WIFI"
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                "MOBILE_DATA"
            } else if (activeNetwork.type == ConnectivityManager.TYPE_ETHERNET) {
                "WIFI"
            } else {
                "WIFI"
            }
        } else {
            "NOT_CONNECTED"
            // not connected to the internet
        }
        return connectedTo
    }

    val isVpnEnabled: Boolean
        get() {
            val networkList: MutableList<String> = ArrayList()
            try {
                for (networkInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
                    if (networkInterface.isUp) networkList.add(networkInterface.name)
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
            return if (networkList.contains("tun0") || networkList.contains("tun1")) {
                true
            } else networkList.contains("ppp")
        }

    fun noInternetConnOrServerErrorDialog(
        context: Activity,
        title: String,
        button_text: String?,
        imageResource: Int,
        dialogCallListener: DialogCallListener?
    ) {
        try {
            if (isConnected(context) || isVpnEnabled) {
                if (getProxySettingDetails(context)) {
                    showProxyDialog(context)
                }
            } else {
                showInternetConnectionServerErrorDialog(
                    context,
                    title,
                    button_text,
                    imageResource,
                    dialogCallListener
                )
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    private fun showInternetConnectionServerErrorDialog(
        context: Activity,
        title: String,
        button_text: String?,
        imageResource: Int,
        dialogCallListener: DialogCallListener?
    ) {
        try {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dailog_no_internet_connection)
            dialog.setCancelable(false)
            val tv_connection_title = dialog.findViewById<TextView>(R.id.tv_connection_title)
            val btn_tap_to_retry = dialog.findViewById<Button>(R.id.btn_tap_to_retry)
            val img_connection = dialog.findViewById<ImageView>(R.id.img_connection)
            img_connection.setImageResource(imageResource)
            tv_connection_title.text = title
            if (button_text != null && button_text.length > 0) {
                btn_tap_to_retry.text = button_text
                btn_tap_to_retry.visibility = View.VISIBLE
            } else {
                btn_tap_to_retry.visibility = View.GONE
            }
            btn_tap_to_retry.setOnClickListener {
                try {
                    dialog.dismiss()
                    if (dialogCallListener != null) dialogCallListener.positiveButtonClick()
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    private fun showProxyDialog(context: Activity) {
        try {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_vpn_proxy)
            dialog.setCancelable(false)
            val tv_connection_title = dialog.findViewById<TextView>(R.id.tv_connection_title)
            val btn_tap_to_retry = dialog.findViewById<Button>(R.id.btn_tap_to_retry)
            tv_connection_title.text = context.getString(R.string.error_vpn_message)
            btn_tap_to_retry.visibility = View.VISIBLE
            btn_tap_to_retry.setOnClickListener {
                try {
                    context.finishAffinity()
                    dialog.dismiss()
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
            dialog.show()
        } catch (e: Exception) {
        }
    }
}
