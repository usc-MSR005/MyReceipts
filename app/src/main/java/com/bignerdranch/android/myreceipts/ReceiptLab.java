package com.bignerdranch.android.myreceipts;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReceiptLab {
    private static ReceiptLab sReceiptLab;

    private List<Receipt> mReceipts;

    public static ReceiptLab get(Context context) {
        if (sReceiptLab == null) {
            sReceiptLab = new ReceiptLab(context);
        }
        return sReceiptLab;
    }
    private ReceiptLab(Context context) {
        mReceipts = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Receipt receipt = new Receipt();
            receipt.setTitle("Crime #" + i);
            mReceipts.add(receipt);
        }
    }

    public List<Receipt> getCrimes() {
        return mReceipts;
    }
    public Receipt getCrime(UUID id) {
        for (Receipt receipt : mReceipts) {
            if (receipt.getId().equals(id)) {
                return receipt;
            }
        }
        return null;
    }
}
