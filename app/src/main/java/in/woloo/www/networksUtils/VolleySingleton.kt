package `in`.woloo.www.networksUtils

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.application_kotlin.api_classes.publickey_pinning.TLSSocketFactory
import javax.net.ssl.SSLSocketFactory

class VolleySingleton private constructor(private val mContext: Context) {
    @JvmField
    var requestQueue: RequestQueue? = null

    init {
        //        requestQueue = Volley.newRequestQueue(context, new HurlStack(null, pinnedSSLSocketFactory()));
        //requestQueue = Volley.newRequestQueue(context);
        requestQueue = if (BuildConfig.LIVE_URL.equals(
                "3",
                ignoreCase = true
            ) || BuildConfig.LIVE_URL.equals("4", ignoreCase = true)
        ) {
            //            requestQueue = Volley.newRequestQueue(context, new HurlStack(null, pinnedSSLSocketFactory()));
            Volley.newRequestQueue(mContext)
        } else {
            Volley.newRequestQueue(mContext)
        }

        imageLoader = ImageLoader(requestQueue, object : ImageLoader.ImageCache {
            private val cache = LruCache<String, Bitmap>(20)

            override fun getBitmap(url: String): Bitmap? {
                return cache[url]
            }

            override fun putBitmap(url: String, bitmap: Bitmap) {
                cache.put(url, bitmap)
            }
        })
    }

    private fun pinnedSSLSocketFactory(): SSLSocketFactory {
        val jetEncryptor = JetEncryptor.getInstance()
        val zz1 = jetEncryptor.certKey
        return TLSSocketFactory(zz1)
    }

    fun setAfterJetEncryptorInitilization(context: Context) {
        instance = VolleySingleton(context)
    }

    companion object {
        private var instance: VolleySingleton? = null
        lateinit var imageLoader: ImageLoader
            private set

        @JvmStatic
        fun getInstance(context: Context): VolleySingleton {
            if (instance == null) {
                instance = VolleySingleton(context)
            }
            return instance!!
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
            */
        /* return new TLSSocketFactory(ZZ1); */ /*
     */
        /* String zz1 = JetEncryptor.getInstance().getCert();
            //String zz1 = BuildConfig.ZZ1;
            return new TLSSocketFactory(zz1);*/
        /*
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
//            Log.d("IO IO IO ","TLS EXCEPTION3");
              CommonUtils.printStackTrace(e)
        }
        return null;

    }
*/
        fun setNewRequestQueue(context: Context?, ZZ1: String?) {
            /*    if (BuildConfig.LIVE_URL.equalsIgnoreCase("3") || BuildConfig.LIVE_URL.equalsIgnoreCase("4")) {
            VolleySingleton.requestQueue = Volley.newRequestQueue(context, new HurlStack(null, VolleySingleton.getInstance(context).pinnedSSLSocketFactory(ZZ1)));
        } else {
            VolleySingleton.requestQueue = Volley.newRequestQueue(context);
        }*/
        }
    }
}
