package com.bignerdranch.android.myreceipts.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.myreceipts.Receipt;

import java.util.Date;
import java.util.UUID;

public class ReceiptCursorWrapper extends CursorWrapper {
    public ReceiptCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Receipt getReceipt() {
        String uuidString = getString(getColumnIndex(ReceiptDbSchema.ReceiptTable.Cols.UUID));
        String title = getString(getColumnIndex(ReceiptDbSchema.ReceiptTable.Cols.TITLE));
        String shop = getString(getColumnIndex(ReceiptDbSchema.ReceiptTable.Cols.SHOP));
        long date = getLong(getColumnIndex(ReceiptDbSchema.ReceiptTable.Cols.DATE));
        String comment = getString(getColumnIndex(ReceiptDbSchema.ReceiptTable.Cols.COMMENT));
        double latitude = getDouble(getColumnIndex(ReceiptDbSchema.ReceiptTable.Cols.LATITUDE));
        double longitude = getDouble(getColumnIndex(ReceiptDbSchema.ReceiptTable.Cols.LONGITUDE));

        Receipt receipt = new Receipt(UUID.fromString(uuidString));
        receipt.setTitle(title);
        receipt.setShop(shop);
        receipt.setDate(new Date(date));
        receipt.setComment(comment);
        receipt.setLat(latitude);
        receipt.setLong(longitude);

        return receipt;
    }
}
