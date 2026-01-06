package com.nidec.qrattendance.scanner;

import android.app.Activity;
import android.content.Intent;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nidec.qrattendance.utils.Constants;

public class QRScannerManager {
    
    public static void startScan(Activity activity) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Đặt mã QR vào trong khung để quét");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }
    
    public static String getScanResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            return result.getContents();
        }
        return null;
    }
    
    public static boolean isValidEmployeeCode(String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }
        // Check if it's a 5-digit employee code
        return code.matches("^\\d{5}$");
    }
}
