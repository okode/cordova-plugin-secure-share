package com.okode.secshare;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SecureSharePlugin extends CordovaPlugin {

    private final static String TAG = "SECSHARE";
    private SecureShareHelper ssHelper;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if ("save".equals(action)) {
            JSONObject argsObject = args.getJSONObject(0);
            Log.d(TAG, "Share data called with: " + argsObject);
            try {
                getSSHelper().save(mapFromJSONObject(argsObject));
            } catch (SecureShareHelper.SecureShareException e) {
                callbackContext.error(e.getCause().getMessage());
            }
            callbackContext.success();
            return true;
        }

        if ("retrieve".equals(action)) {
            try {
                Map<String, String> result = getSSHelper().retrieve();
                callbackContext.success(jsonObjectFromMap(result));
                return true;
            } catch (SecureShareHelper.SecureShareException e) {
                callbackContext.error(e.getCause().getMessage());
                return false;
            }
        }

        if ("retrieveFrom".equals(action)) {
            String packageName = args.getString(0);
            Map<String, String> result = getSSHelper().retrieveFrom(packageName);
            callbackContext.success(jsonObjectFromMap(result));
            return true;
        }

        Log.d(TAG, "Action not implemeted: " + action + ", Args: " + args);
        callbackContext.error("Action not implemented");
        return false;
    }

    private Map<String, String> mapFromJSONObject(JSONObject jsonObject) {
        Map<String, String> map = new HashMap<>();
        Iterator<String> keysIterator = jsonObject.keys();
        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            String value = null;
            try {
                value = jsonObject.getString(key);
                map.put(key, value);
            } catch (JSONException e) {
                Log.e(TAG, "Ignoring. Cannot get string value for key: " + key);
            }
        }
        return map;
    }

    private JSONObject jsonObjectFromMap(Map<String, String> map) {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                jsonObject.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                Log.e(TAG, "Cannot convert to JSONObject key " + entry.getKey() + " with value " + entry.getValue());
            }
        }
        return jsonObject;
    }

    private SecureShareHelper getSSHelper() {
        if (ssHelper == null) {
            ssHelper = new SecureShareHelper(this.cordova.getActivity().getApplicationContext());
        }
        return ssHelper;
    }

}
