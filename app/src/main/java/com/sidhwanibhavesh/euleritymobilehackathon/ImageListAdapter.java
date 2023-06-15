package com.sidhwanibhavesh.euleritymobilehackathon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

class ImageListAdapter extends ArrayAdapter<Image> {

   public ImageListAdapter(Context context, ArrayList<Image> imageUrls) {
      super(context, 0, imageUrls);
   }

   @Override
   public int getCount() {
      return Constants.imageArray.size();
   }

   @NonNull
   @Override
   public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      View listItemView = convertView;
      if(listItemView == null) {
         listItemView = LayoutInflater.from(getContext()).inflate(
                 R.layout.image_list_element, parent, false);
      }

      Image currentImage = (Image) getItem(position);

      TextView textView = listItemView.findViewById(R.id.url_text_view);
      ImageView imageView = listItemView.findViewById(R.id.image_view);

      textView.setText(currentImage.url);
      imageView.setImageBitmap(currentImage.imageBitmap);

      return listItemView;
   }
}
