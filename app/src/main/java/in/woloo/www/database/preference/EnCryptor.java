package in.woloo.www.database.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;

public class EnCryptor {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private byte[] encryption;
    private byte[] iv;
    private KeyStore keyStore;

    public EnCryptor() {
        try {
            initKeyStore();
        } catch (KeyStoreException e) {
              CommonUtils.printStackTrace(e);
        } catch (CertificateException e) {
              CommonUtils.printStackTrace(e);
        } catch (NoSuchAlgorithmException e) {
              CommonUtils.printStackTrace(e);
        } catch (IOException e) {
              CommonUtils.printStackTrace(e);
        }
    }

    private void initKeyStore() throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);
    }

    public byte[] encryptText(Context context, final String alias, final String textToEncrypt)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
            InvalidAlgorithmParameterException, SignatureException, BadPaddingException,
            IllegalBlockSizeException {

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));

        iv = cipher.getIV();

        byte[] mEx = (encryption = cipher.doFinal(textToEncrypt.getBytes("UTF-8")));
        setProperty(context, alias, iv, new String(mEx, StandardCharsets.ISO_8859_1));
        return mEx;
    }


    private void setProperty(Context mContext, String key, byte[] iv, String encryptedValue) {

//        String ivString = Base64.encodeToString(iv, Base64.DEFAULT);
        String ivString = new String(iv, StandardCharsets.ISO_8859_1);
        if (mContext == null) {
            mContext = WolooApplication.getInstance();
        }

        SharedPreferences sp = getPreferenceModePrivate(mContext, key + "_data");
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, encryptedValue);
        editor.apply();

        SharedPreferences sp1 = getPreferenceModePrivate(mContext, key + "_iv");
        SharedPreferences.Editor editor1 = sp1.edit();
        editor1.putString(key + "_iv", ivString);
        editor1.apply();
    }


    private SharedPreferences getPreferenceModePrivate(Context mContext, String key) {
        return mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
    }


    private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException {

        KeyGenerator keyGenerator = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());
        } else {
//            keyGenerator = KeyGenerator.getInstance(ANDROID_KEY_STORE);
            // or something like
            keyGenerator = KeyGenerator.getInstance("RSA/ECB/PKCS1Padding", ANDROID_KEY_STORE);

            // use the supported init method here such as this one: https://developer.android.com/reference/javax/crypto/KeyGenerator.html#init(int, java.security.SecureRandom)
            keyGenerator.init(128, getSecureRandom());
        }


        return keyGenerator.generateKey();
    }


    public byte[] getEncryption() {
        return encryption;
    }

    public byte[] getIv() {
        return iv;
    }

    private SecureRandom getSecureRandom() {
        SecureRandom secureRandom1 = null;
        try {
            // Create a secure random number generator using the SHA1PRNG algorithm
            SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
            // Get 128 random bytes
            byte[] randomBytes = new byte[128];
            secureRandomGenerator.nextBytes(randomBytes);

            // Create two secure number generators with the same seed
            int seedByteCount = 5;
            byte[] seed = secureRandomGenerator.generateSeed(seedByteCount);

            secureRandom1 = SecureRandom.getInstance("SHA1PRNG");
            secureRandom1.setSeed(seed);

        } catch (NoSuchAlgorithmException e) {
              CommonUtils.printStackTrace(e);
        }
        return secureRandom1;
    }

    public void encryptString(String alias, String initialText) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            // Encrypt the text
            if (initialText.isEmpty()) {
                return;
            }

            Cipher input = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            input.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, input);
            cipherOutputStream.write(initialText.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte[] vals = outputStream.toByteArray();
//            encryptedText.setText(Base64.encodeToString(vals, Base64.DEFAULT));
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

}
