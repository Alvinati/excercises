package com.example.f1sh.pos;


import android.provider.BaseColumns;


public final class FeedReaderContract {


    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry2";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_NAME_PRODUCT = "name";
        public static final String COLUMN_PRICE = "price";
    }


}


