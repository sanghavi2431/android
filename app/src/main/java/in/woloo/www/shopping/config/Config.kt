package `in`.woloo.www.shopping.config

import `in`.woloo.www.BuildConfig

object Config {
    //  public static String server_name="http://192.168.1.//:10004//";
    /*  public static String server_name="http://isntmumbai.in/woloo_shopping/";
    public static String hostname="http://isntmumbai.in/woloo_shopping/app_api/";*/
    /*public static String server_name="http://apiwoloo.verifinow.com/";
    public static String hostname="http://apiwoloo.verifinow.com/app_api/";*/
    var server_name: String = "https://shop.woloo.in/"
    @JvmField
    var hostname: String = BuildConfig.SHOP_URL //new staging apis - 18-APRIL
    //        public static String server_name="https://staging-shop.woloo.in/";
    //    public static String hostname="https://staging-shop.woloo.in/app_api/";
    //  public static String hostname="http://192.168.1.161:10004//app_api/";
}
