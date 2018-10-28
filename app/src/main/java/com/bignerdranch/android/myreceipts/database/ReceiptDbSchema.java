package com.bignerdranch.android.myreceipts.database;

public class ReceiptDbSchema {
    public static final class ReceiptTable {
        public static final String NAME = "receipts";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String SHOP = "shop";
            public static final String DATE = "date";
            public static final String COMMENT = "comment";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
        }
    }
}
