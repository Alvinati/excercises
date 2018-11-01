package com.example.f1sh.pos;


public class ListContent {

    private String mNamaProduk;
    private String mHargaProduk;
    private String mId;
    private byte[] mImageResourceByte;


    public ListContent(String namaproduk, String hargaProduk, byte[] imageResourceId, String id){
        mNamaProduk = namaproduk;
        mHargaProduk = hargaProduk;
        mId = id;
        mImageResourceByte = imageResourceId;
    }

    public String getmNamaProduk(){
        return mNamaProduk;
    }
    public String getmHargaProduk() {
        return mHargaProduk;
    }
    public String getmId(){return mId;}
    public byte[] getImageResourceByte(){return mImageResourceByte;}

}




