package com.nidec.qrattendance.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.nidec.qrattendance.utils.Constants;

public class ZebraDataWedgeHelper {
    
    private Context context;
    private ScanResultListener listener;
    private BroadcastReceiver scanReceiver;
    
    public interface ScanResultListener {
        void onScanResult(String data);
    }
    
    public ZebraDataWedgeHelper(Context context, ScanResultListener listener) {
        this.context = context;
        this.listener = listener;
        setupScanReceiver();
    }
    
    private void setupScanReceiver() {
        scanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals(Constants.DATAWEDGE_INTENT_ACTION)) {
                    String data = intent.getStringExtra(Constants.DATAWEDGE_INTENT_KEY_DATA);
                    if (data != null && !data.isEmpty() && listener != null) {
                        listener.onScanResult(data);
                    }
                }
            }
        };
    }
    
    public void register() {
        if (scanReceiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constants.DATAWEDGE_INTENT_ACTION);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            context.registerReceiver(scanReceiver, filter);
        }
    }
    
    public void unregister() {
        if (scanReceiver != null) {
            try {
                context.unregisterReceiver(scanReceiver);
            } catch (IllegalArgumentException e) {
                // Receiver was not registered
            }
        }
    }
    
    public void createDataWedgeProfile() {
        // Create DataWedge profile via Intent API
        Intent intent = new Intent();
        intent.setAction("com.symbol.datawedge.api.ACTION");
        
        // Create profile
        intent.putExtra("com.symbol.datawedge.api.CREATE_PROFILE", Constants.DATAWEDGE_PROFILE_NAME);
        context.sendBroadcast(intent);
        
        // Configure profile
        configureDataWedgeProfile();
    }
    
    private void configureDataWedgeProfile() {
        Intent intent = new Intent();
        intent.setAction("com.symbol.datawedge.api.ACTION");
        
        // Set configuration
        Bundle configBundle = new Bundle();
        configBundle.putString("PROFILE_NAME", Constants.DATAWEDGE_PROFILE_NAME);
        configBundle.putString("PROFILE_ENABLED", "true");
        
        Bundle appConfig = new Bundle();
        appConfig.putString("PACKAGE_NAME", context.getPackageName());
        appConfig.putStringArray("ACTIVITY_LIST", new String[]{"*"});
        configBundle.putParcelableArray("APP_LIST", new Bundle[]{appConfig});
        
        // Intent output configuration
        Bundle intentConfig = new Bundle();
        intentConfig.putString("INTENT_OUTPUT_ENABLED", "true");
        intentConfig.putString("INTENT_ACTION", Constants.DATAWEDGE_INTENT_ACTION);
        intentConfig.putString("INTENT_CATEGORY", Intent.CATEGORY_DEFAULT);
        intentConfig.putString("INTENT_DELIVERY", "2"); // Broadcast
        
        Bundle intentProps = new Bundle();
        intentProps.putString("intent_output_enabled", "true");
        intentProps.putString("intent_action", Constants.DATAWEDGE_INTENT_ACTION);
        intentProps.putString("intent_category", Intent.CATEGORY_DEFAULT);
        
        configBundle.putBundle("PLUGIN_CONFIG", new Bundle());
        configBundle.getBundle("PLUGIN_CONFIG").putString("PLUGIN_NAME", "INTENT");
        configBundle.getBundle("PLUGIN_CONFIG").putString("RESET_CONFIG", "true");
        configBundle.getBundle("PLUGIN_CONFIG").putBundle("PARAM_LIST", intentProps);
        
        intent.putExtra("com.symbol.datawedge.api.SET_CONFIG", configBundle);
        context.sendBroadcast(intent);
    }
    
    public static boolean isZebraDevice() {
        // Check if device manufacturer is Zebra
        String manufacturer = android.os.Build.MANUFACTURER;
        return manufacturer != null && (
                manufacturer.equalsIgnoreCase("Zebra") || 
                manufacturer.equalsIgnoreCase("Motorola Solutions") ||
                manufacturer.equalsIgnoreCase("Symbol")
        );
    }
}
