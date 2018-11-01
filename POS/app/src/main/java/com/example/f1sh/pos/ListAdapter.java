package com.example.f1sh.pos;


import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ListContent> {

    public ListAdapter(Activity context, ArrayList<ListContent> content){

        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, content);
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_content, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        final ListContent currentContent = getItem(position);


            // Find the TextView in the list_item.xml layout with the ID version_name
            TextView namaTextView = (TextView) listItemView.findViewById(R.id.nama_produk);
            namaTextView.setText(currentContent.getmNamaProduk());

            TextView hargaTextView = (TextView) listItemView.findViewById(R.id.harga);
            hargaTextView.setText(currentContent.getmHargaProduk());

            ImageView imageView = (ImageView) listItemView.findViewById(R.id.list_image);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(currentContent.getImageResourceByte(), 0,
                     currentContent.getImageResourceByte().length));


        return listItemView;

    }


}