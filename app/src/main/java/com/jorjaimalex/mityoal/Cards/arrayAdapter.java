package com.jorjaimalex.mityoal.Cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.jorjaimalex.mityoal.R;
import com.bumptech.glide.Glide;
import java.util.List;

public class arrayAdapter extends ArrayAdapter<tarjetas> {
    Context context;

    public arrayAdapter(@NonNull Context context, int resource, @NonNull List<tarjetas> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        tarjetas tarjeta = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);

        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        name.setText(tarjeta.getName());
        switch(tarjeta.getImageUrl()){
            case "default":
                Glide.with(convertView.getContext()).load(R.drawable.ic_launcher).into(image);
                break;
            default:
                Glide.with(convertView.getContext()).load(tarjeta.getImageUrl()).into(image);
                break;
        }
        return  convertView;
    }
}
