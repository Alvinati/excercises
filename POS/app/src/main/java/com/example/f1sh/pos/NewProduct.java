package com.example.f1sh.pos;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;



public class NewProduct extends AppCompatActivity {

    EditText nama, harga;
    Button simpan;
    ImageView foto;
    Bitmap bp;
    byte[] byteImage1;

    private static final int SELECT_PICTURE = 1;
    ContentValues values = new ContentValues();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        nama = findViewById(R.id.field_nama_produk);
        harga = findViewById(R.id.field_harga_produk);
        simpan = findViewById(R.id.button_simpan);
        foto = findViewById(R.id.gambar_produk);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ActivityCompat.checkSelfPermission(NewProduct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(NewProduct.this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_PICTURE);
                    } else {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(
                                Intent.createChooser(intent, "Select Picture"),
                                SELECT_PICTURE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            });



        simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!nama.getText().toString().equals("") && !harga.getText().toString().equals("") && bp !=null ){
                        String namaProduk = nama.getText().toString();
                        String hargaProduk = harga.getText().toString();
                        DbHelper mDbHelper = new DbHelper(getApplicationContext());

                        // Gets the data repository in write mode
                        SQLiteDatabase db = mDbHelper.getWritableDatabase();
                        // Create a new map of values, where column names are the keys

                        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PRODUCT, namaProduk);
                        values.put(FeedReaderContract.FeedEntry.COLUMN_PRICE, hargaProduk);
                        insertPicture();


                        // Insert the new row, returning the primary key value of the new row
                        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                        MainActivity.ma.RefreshList();
                        finish();

                    }else {
                        Toast.makeText(getApplicationContext(), "Please fill field & picture", Toast.LENGTH_SHORT).show();
                    }

                }
            });

    }

  public void insertPicture() {
            byteImage1 = profileImage(bp);
            values.put(FeedReaderContract.FeedEntry.COLUMN_IMAGE, byteImage1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if(selectedImageUri !=null){
                    bp = decodeUri(selectedImageUri, 400);
                    foto.setVisibility(View.VISIBLE);
                    foto.setImageBitmap(bp);
                }
            }
        }
    }


    //COnvert and resize image to 400dp for faster uploading our images to DB
    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //Convert bitmap to bytes
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }
}
