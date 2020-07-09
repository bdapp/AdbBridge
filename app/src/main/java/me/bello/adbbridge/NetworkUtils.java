package me.bello.adbbridge;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.WIFI_SERVICE;

/**
 * @Info 网络检查类
 * @Auth Bello
 * @Time 18-11-22 下午5:49
 * @Ver
 */
public class NetworkUtils {

    /**
     * 获取网络类型
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static NetworkType getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        NetworkType type = NetworkType.NETWORK_NO;
        if (null != info && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                type = NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        type = NetworkType.NETWORK_3G;
                        break;

                    case TelephonyManager.NETWORK_TYPE_LTE:
                    case TelephonyManager.NETWORK_TYPE_IWLAN:
                        type = NetworkType.NETWORK_4G;
                        break;

                    case TelephonyManager.NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        type = NetworkType.NETWORK_2G;
                        break;

                    default:
                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            type = NetworkType.NETWORK_3G;
                        } else {
                            type = NetworkType.NETWORK_UNKNOWN;
                        }
                        break;
                }
            } else {
                type = NetworkType.NETWORK_UNKNOWN;
            }

        }
        return type;
    }


    /**
     * 获取IP地址
     *
     * @return
     */
    public static Map<String, String> getLocalIpAddress(Context mContext) {
        Map<String, String> map = new HashMap<String, String>();

        try {
            WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
            String ssid = wifiManager.getConnectionInfo().getSSID();
            int ip = wifiManager.getConnectionInfo().getIpAddress();
            String ipString = "";
            // 获得IP地址的方法一：
            if (ip != 0) {
                ipString = ((ip & 0xff) + "." + (ip >> 8 & 0xff) + "." + (ip >> 16 & 0xff) + "." + (ip >> 24 & 0xff));
            }

            if (ssid==null || ipString.equals("")) {
                return map;
            }
            map.put("ssid", ssid);
            map.put("ip", ipString);

        } catch (Exception e) {
           e.printStackTrace();
        }
        return map;
    }


    /**
     * 判断当前是否有网络连接,但是如果该连接的网络无法上网，也会返回true
     * @param mContext
     * @return
     */
    private static boolean isNetConnection(Context mContext) {
        if (mContext!=null){
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (null != networkInfo) {
                boolean connected = networkInfo.isConnected();
                if (networkInfo != null && connected) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 在子线程里开启该方法，可检测当前网络是否能打开网页
     * true是可以上网，false是不能上网
     *
     */
    private static boolean isOnline(){
        URL url;
        try {
            url = new URL("https://www.baidu.com");
            InputStream stream = url.openStream();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean networkIsOnline(Context context){
        if (isNetConnection(context) && isOnline()){
            return true;
        }
        return false;
    }

}
