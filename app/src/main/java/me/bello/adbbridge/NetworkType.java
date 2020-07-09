package me.bello.adbbridge;

/**
 * @Info 定义网络类型
 * @Auth Bello
 * @Time 18-11-22 下午5:41
 * @Ver
 */
public enum NetworkType {
    NETWORK_WIFI("WiFi"),
    NETWORK_4G("4G"),
    NETWORK_3G("3G"),
    NETWORK_2G("2G"),
    NETWORK_UNKNOWN("UnKnown"),
    NETWORK_NO("No");


    private String type;

    NetworkType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
