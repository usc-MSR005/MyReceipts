package com.bignerdranch.android.myreceipts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.UUID;

public class ReceiptActivity extends SingleFragmentActivity {

    private static final String EXTRA_RECEIPT_ID =
            "com.bignerdranch.android.myreceipt.receipt_id";
    private static final int REQUEST_ERROR = 0;

    public static Intent newIntent(Context packageContext, UUID receiptId) {
        Intent intent = new Intent(packageContext, ReceiptActivity.class);
        intent.putExtra(EXTRA_RECEIPT_ID, receiptId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID receiptId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_RECEIPT_ID);
        return ReceiptFragment.newInstance(receiptId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = apiAvailability
                    .getErrorDialog(this, errorCode, REQUEST_ERROR,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                // Leave if services are unavailable.
                                    finish();
                                }
                            });
            errorDialog.show();
        }
    }
}
