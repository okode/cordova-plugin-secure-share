package com.okode.secshare;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class SecureShareHelper {

    private static final String PROVIDER_URI_SUFFIX = ".providers.secureshare/secshare";
    private static final String PROTOCOL = "content://";
    private static final String FILENAME = "secshare";
    private static final String TAG = SecureShareHelper.class.getSimpleName();
    private Context context;

    public SecureShareHelper(Context context) {
        this.context = context;
    }

    public void save(Map<String, String> data) throws SecureShareException {
        try {
            SharedPreferences sharedPreferences = getSecureSharedPrefs();
            SharedPreferences.Editor sharedPrefsEditor = sharedPreferences.edit();
            sharedPrefsEditor.clear();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                sharedPrefsEditor.putString(entry.getKey(), entry.getValue());
            }
            sharedPrefsEditor.apply();
        } catch (GeneralSecurityException e) {
            Log.e(TAG, "Security exception saving sharing data", e);
            throw new SecureShareException(e);
        } catch (IOException e) {
            Log.e(TAG, "I/O exception saving sharing data", e);
            throw new SecureShareException(e);
        }
    }

    public Map<String, String> retrieve() throws SecureShareException {
        Map<String, String> result = new HashMap<>();
        try {
            SharedPreferences sharedPreferences = getSecureSharedPrefs();
            for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
                result.put(entry.getKey(), entry.getValue().toString());
            }
        } catch (GeneralSecurityException e) {
            Log.e(TAG, "Security exception retrieving sharing data", e);
            throw new SecureShareException(e);
        } catch (IOException e) {
            Log.e(TAG, "I/O exception retrieving sharing data", e);
            throw new SecureShareException(e);
        }
        return result;
    }

    public Map<String, String> retrieveFrom(String packageName) {
        Map<String, String> result = new HashMap<>();
        Uri uri = Uri.parse(PROTOCOL + packageName + PROVIDER_URI_SUFFIX);
        Cursor cursor = this.context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            for(int i=0; i<cursor.getColumnCount(); i++) {
                result.put(cursor.getColumnName(i), cursor.getString(i));
            }
        }
        return result;
    }

    private SharedPreferences getSecureSharedPrefs() throws GeneralSecurityException, IOException {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        SharedPreferences sharedPreferences = EncryptedSharedPreferences
                .create(
                        context,
                        FILENAME,
                        masterKey,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );

        return sharedPreferences;
    }

    public static class SecureShareException extends Exception {
        public SecureShareException(Throwable e) {
            super(e);
        };
    }
}
