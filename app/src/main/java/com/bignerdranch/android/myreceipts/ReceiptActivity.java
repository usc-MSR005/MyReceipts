package com.bignerdranch.android.myreceipts;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class ReceiptActivity extends SingleFragmentActivity {

    private static final String EXTRA_RECEIPT_ID =
            "com.bignerdranch.android.myreceipt.receipt_id";
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
}
