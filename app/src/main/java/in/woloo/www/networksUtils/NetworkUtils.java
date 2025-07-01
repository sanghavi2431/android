package in.woloo.www.networksUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.listners.DialogCallListener;


public class NetworkUtils {

    private SharedPreference mSharedPreference;
    private CommonUtils commonUtils = new CommonUtils();

    public NetworkUtils() {
        // This utility class is not publicly instantiable
    }

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

   /* public boolean isConnected(Context context) {
        boolean isConnectedStatus = false;
        try {

            //here we first check the PROXY SETTING ENABLE OR NOT
            String connectedTo = checkConnectedToStatus(context);

            //VPN DISABLE CODE
           *//* if ("MOBILE_DATA".equals(connectedTo)) {
                isConnectedStatus = true;
            } else if ("WIFI".equals(connectedTo)) {
                //CHECKING WIFI SETTINGS
                isConnectedStatus = true;
            }*//*

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

    public boolean isConnected(Context context) {
        if (mSharedPreference == null) {
            mSharedPreference = new SharedPreference(context);
        }

        if (commonUtils != null) {
            commonUtils = new CommonUtils();
        }

        boolean isConnectedStatus = false;
        try {
            //here we first check the PROXY SETTING ENABLE OR NOT
            String connectedTo = checkConnectedToStatus(context);

            String mStringAmazonTv = Build.MANUFACTURER;


            // Mobile type vpn check
            //VPN DISABLE CODE
          /* if ("MOBILE_DATA".equals(connectedTo)) {
                isConnectedStatus = true;
            } else if ("WIFI".equals(connectedTo)) {
                //CHECKING WIFI SETTINGS
                isConnectedStatus = true;
            }*/
            //VPN ENABLE CHECK CODE
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
              CommonUtils.printStackTrace(e);
            return false;
        }
        return isConnectedStatus;
    }


    public boolean getProxySettingDetails(Context context) {
        String proxyAddress = "";
        String portValue = "";
        int port;
        boolean proxySettingEnable = false;
        try {
            if (preICS()) {
                try {
                    proxyAddress = android.net.Proxy.getHost(context);
                    port = android.net.Proxy.getPort(context);

                    if ((proxyAddress == null) || port == 0) {
                        proxySettingEnable = true;
                    }
                } catch (Exception e) {
                      CommonUtils.printStackTrace(e);
                }
            } else {
                try {
                    proxyAddress = System.getProperty("http.proxyHost");
                    portValue = System.getProperty("http.proxyPort");
                    if (proxyAddress == null || portValue == null || portValue.equals("0")) {
                        proxySettingEnable = true;
                    }
                } catch (Exception e) {
                      CommonUtils.printStackTrace(e);
                }
            }
        } catch (Exception ex) {
            //ex.pr
             CommonUtils.printStackTrace(ex);
        }
        return proxySettingEnable;
    }

    private boolean preICS() {

        return (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH);

    }

    private String checkConnectedToStatus(Context mContext) {
        String connectedTo = "";
        if (mContext == null)
            mContext = WolooApplication.getInstance();

        ConnectivityManager cm = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                connectedTo = "WIFI";
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                connectedTo = "MOBILE_DATA";
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET) {
                connectedTo = "WIFI";
            } else {
                connectedTo = "WIFI";
            }
        } else {
            connectedTo = "NOT_CONNECTED";
            // not connected to the internet
        }
        return connectedTo;
    }

    public boolean isVpnEnabled() {
        List<String> networkList = new ArrayList<>();
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    networkList.add(networkInterface.getName());
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
        if (networkList.contains("tun0") || networkList.contains("tun1")) {
            return true;
        } else return networkList.contains("ppp");
    }

    public void noInternetConnOrServerErrorDialog(Activity context, String title, String button_text, int imageResource, DialogCallListener dialogCallListener) {
        try {
            if (isConnected(context) || isVpnEnabled()) {
                if (getProxySettingDetails(context)) {
                    showProxyDialog(context);
                }
            } else {
                showInternetConnectionServerErrorDialog(context, title, button_text, imageResource, dialogCallListener);
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }

    }

    private void showInternetConnectionServerErrorDialog(Activity context, String title, String button_text, int imageResource, DialogCallListener dialogCallListener) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dailog_no_internet_connection);
            dialog.setCancelable(false);

            TextView tv_connection_title = dialog.findViewById(R.id.tv_connection_title);
            Button btn_tap_to_retry = dialog.findViewById(R.id.btn_tap_to_retry);
            ImageView img_connection = dialog.findViewById(R.id.img_connection);

            img_connection.setImageResource(imageResource);
            tv_connection_title.setText(title);

            if (button_text != null && button_text.length() > 0) {
                btn_tap_to_retry.setText(button_text);
                btn_tap_to_retry.setVisibility(View.VISIBLE);
            } else {
                btn_tap_to_retry.setVisibility(View.GONE);
            }

            btn_tap_to_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        dialog.dismiss();
                        if (dialogCallListener != null)
                            dialogCallListener.positiveButtonClick();
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }


    private void showProxyDialog(Activity context) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent_background)));
            dialog.setContentView(R.layout.dialog_vpn_proxy);
            dialog.setCancelable(false);

            TextView tv_connection_title = dialog.findViewById(R.id.tv_connection_title);
            Button btn_tap_to_retry = dialog.findViewById(R.id.btn_tap_to_retry);
            tv_connection_title.setText(context.getString(R.string.error_vpn_message));
            btn_tap_to_retry.setVisibility(View.VISIBLE);

            btn_tap_to_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        context.finishAffinity();
                        dialog.dismiss();
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {

        }

    }
}
