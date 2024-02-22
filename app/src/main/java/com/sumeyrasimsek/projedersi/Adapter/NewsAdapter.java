package com.sumeyrasimsek.projedersi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sumeyrasimsek.projedersi.Model.News;
import com.sumeyrasimsek.projedersi.R;

import java.util.ArrayList;

public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.RCViewHolder>{

    public class RCViewHolder extends RecyclerView.ViewHolder{
        TextView txttitle,txtsource,txtdate,txtaciklama;
        ImageView imglogo;



        public RCViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdate=itemView.findViewById(R.id.tvDate);
            txtsource=itemView.findViewById(R.id.tvhref);
            txttitle=itemView.findViewById(R.id.tvTitle);
            txtaciklama=itemView.findViewById(R.id.txtAciklama);



        }
    }


    Context context;
    ArrayList<News> modelArrayList;



    public NewsAdapter(Context context, ArrayList<News> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public RCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.newitem,parent,false);


        return new RCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RCViewHolder holder, int position) {
        News news=modelArrayList.get(position);
        holder.txttitle.setText(news.getTitle());
        holder.txtaciklama.setText(news.getAciklama());
        holder.txtdate.setText(news.getTitle_date());
        holder.txtsource.setText(news.getHref());
        holder.txtsource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = holder.txtsource.getText().toString();
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    // Linkin açılması için bir Intent kullan
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

}
