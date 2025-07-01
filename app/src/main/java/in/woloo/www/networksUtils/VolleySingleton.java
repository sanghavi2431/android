package in.woloo.www.networksUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jetsynthesys.encryptor.JetEncryptor;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLSocketFactory;

import in.woloo.www.BuildConfig;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.networksUtils.publickey_pinning.TLSSocketFactory;


public class VolleySingleton {

    private static VolleySingleton instance;
    private static ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private Context mContext;

    private VolleySingleton(Context context) {
        mContext = context;
//        requestQueue = Volley.newRequestQueue(context, new HurlStack(null, pinnedSSLSocketFactory()));
        //requestQueue = Volley.newRequestQueue(context);


        if (BuildConfig.LIVE_URL.equalsIgnoreCase("3") || BuildConfig.LIVE_URL.equalsIgnoreCase("4")) {
//            requestQueue = Volley.newRequestQueue(context, new HurlStack(null, pinnedSSLSocketFactory()));
            requestQueue = Volley.newRequestQueue(context);
        } else {
            requestQueue = Volley.newRequestQueue(context);
        }

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String,
                    Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });

    }

    public static VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }


    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /* private SSLSocketFactory pinnedSSLSocketFactory() {

         try {
             // Get an instance of the Bouncy Castle KeyStore format
             KeyStore trusted = null;
             try {
                 trusted = KeyStore.getInstance("BKS");
             } catch (KeyStoreException e) {
 //                Log.d("IO IO IO ","TLS EXCEPTION-1");
                   CommonUtils.printStackTrace(e)
             }
             // Get the raw resource, which contains the keystore with
             // your trusted certificates (root and any intermediate certs)
             InputStream in = mContext.getApplicationContext().getResources().openRawResource(R.raw.sonybpapi);
             try {
                 // Initialize the keystore with the provided trusted certificates
                 // Provide the password of the keystore
                 try {
                     if (trusted != null) {
                         trusted.load(in, mContext.getString(R.string.KEYSTORE_PASS).toCharArray());
                     }
                 } catch (IOException | CertificateException e) {
 //                    Log.d("IO IO IO ","TLS EXCEPTION");
                       CommonUtils.printStackTrace(e)
                 }
             } finally {
                 try {
                     in.close();
                 } catch (IOException e) {
 //                    Log.d("IO IO IO ","TLS EXCEPTION1");
                       CommonUtils.printStackTrace(e)
                 }
             }

             String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
             TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
             try {
                 tmf.init(trusted);
             } catch (KeyStoreException e) {
 //                Log.d("IO IO IO ","TLS EXCEPTION2");
                   CommonUtils.printStackTrace(e)
             }

             SSLContext context = SSLContext.getInstance("TLSv1.2");
             context.init(null, tmf.getTrustManagers(), null);

             HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                 @Override
                 public boolean verify(String hostname, SSLSession session) {
                     Log.i("Volley", "Verifing host:" + hostname);
                     return true;
                 }
             });

             return context.getSocketFactory();
             *//* return new TLSSocketFactory(ZZ1); *//*
     *//* String zz1 = JetEncryptor.getInstance().getCert();
            //String zz1 = BuildConfig.ZZ1;
            return new TLSSocketFactory(zz1);*//*
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
//            Log.d("IO IO IO ","TLS EXCEPTION3");
              CommonUtils.printStackTrace(e)
        }
        return null;

    }
*/
    public static void setNewRequestQueue(Context context, String ZZ1) {
    /*    if (BuildConfig.LIVE_URL.equalsIgnoreCase("3") || BuildConfig.LIVE_URL.equalsIgnoreCase("4")) {
            VolleySingleton.requestQueue = Volley.newRequestQueue(context, new HurlStack(null, VolleySingleton.getInstance(context).pinnedSSLSocketFactory(ZZ1)));
        } else {
            VolleySingleton.requestQueue = Volley.newRequestQueue(context);
        }*/
    }

    private SSLSocketFactory pinnedSSLSocketFactory() {
        try {
            JetEncryptor jetEncryptor = JetEncryptor.getInstance();
            String zz1 = jetEncryptor.getCertKey();
            return new TLSSocketFactory(zz1);
        } catch (KeyManagementException e) {
              CommonUtils.printStackTrace(e);
        } catch (NoSuchAlgorithmException e) {
              CommonUtils.printStackTrace(e);
        }
        return null;
    }

    public void setAfterJetEncryptorInitilization(Context context) {
        instance = new VolleySingleton(context);

    }
}
