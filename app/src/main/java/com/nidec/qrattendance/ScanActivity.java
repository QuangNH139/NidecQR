package com.nidec.qrattendance;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ScanActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Note: ScanActivity is not needed as we're using IntentIntegrator directly
        // This class is kept for future enhancements
        finish();
    }
}
