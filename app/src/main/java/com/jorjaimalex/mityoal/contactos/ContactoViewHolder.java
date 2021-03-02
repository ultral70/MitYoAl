package com.jorjaimalex.mityoal.contactos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jorjaimalex.mityoal.R;
import com.jorjaimalex.mityoal.chat.ChatActivity;

public class ContactoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mMatchId, mMatchName;
    public ImageView mMatchImage;
    public ContactoViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId = (TextView) itemView.findViewById(R.id.Matchid);
        mMatchName = (TextView) itemView.findViewById(R.id.MatchName);

        mMatchImage = (ImageView) itemView.findViewById(R.id.MatchImage);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("matchId", mMatchId.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }

}
