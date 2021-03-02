package com.jorjaimalex.mityoal.contactos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jorjaimalex.mityoal.R;

import java.util.List;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoViewHolder>{

    private List<Contacto> matchesList;
    private Context context;


    public ContactoAdapter(List<Contacto> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }

    @Override
    public ContactoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ContactoViewHolder rcv = new ContactoViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ContactoViewHolder holder, int position) {
        holder.mMatchId.setText(matchesList.get(position).getUserId());
        holder.mMatchName.setText(matchesList.get(position).getName());
        if(!matchesList.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(matchesList.get(position).getProfileImageUrl()).into(holder.mMatchImage);
        }
    }

    @Override
    public int getItemCount() {
        return this.matchesList.size();
    }

}
