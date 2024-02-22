package com.sumeyrasimsek.projedersi.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sumeyrasimsek.projedersi.Model.Chat;
import com.sumeyrasimsek.projedersi.Model.User;
import com.sumeyrasimsek.projedersi.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    String BizimId;



    final static public int mesaj_sag=0;
    final static public int mesaj_sol=1;

    FirebaseUser mevcutKullanici;
    String UserId;
    Context context;
    List<Chat> mesajlar;
    int mesajkonumu=-1;


    public ChatAdapter(Context context, List<Chat> mesajlar) {
        this.context = context;
        this.mesajlar = mesajlar;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mevcutKullanici= FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs2=context.getSharedPreferences("PREFS", MODE_PRIVATE);
        BizimId =prefs2.getString("UserId","none"); //bizim id

        if(viewType==mesaj_sag) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.sag, parent, false);
            return new ChatAdapter.MyViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.sol, parent, false);
            return new ChatAdapter.MyViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       final Chat chat = mesajlar.get(position);
       holder.mesaj.setText(chat.getMesaj());
       holder.tarih.setText(chat.getTarih());
       holder.saat.setText(chat.getSaat());
        holder.mesaj.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mesajkonumu=holder.getAdapterPosition();
                notifyDataSetChanged();

                return false;
            }
        });

        if (mesajkonumu == holder.getAdapterPosition()) {
            holder.card.setBackgroundColor(Color.parseColor("#D5D8D3"));
            if (holder.sil != null) {
                holder.sil.setVisibility(View.VISIBLE);
                holder.sil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int clickedPosition = holder.getAdapterPosition();
                        mesajsil(clickedPosition);
                    }
                });
            }
            if (holder.kopyala != null) {
                holder.kopyala.setVisibility(View.VISIBLE);
                holder.kopyala.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboardManager= (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData=ClipData.newPlainText("",holder.mesaj.getText());
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(context, "Mesaj kopyalandı.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }





        mevcutKullanici= FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs2=context.getSharedPreferences("PREFS", MODE_PRIVATE);
        BizimId =prefs2.getString("UserId","none"); //bizim id


    }

    private void mesajsil(int position) {
        String msg = mesajlar.get(position).getMesaj();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Mesajlar").child(BizimId);
        Query sorgu=reference.orderByChild("mesaj").equalTo(msg);
        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    snapshot1.getRef().removeValue();
                    Toast.makeText(context, "Mesaj silindi", Toast.LENGTH_SHORT).show();
                    notifyItemChanged(position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return mesajlar.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mesaj,goruldu,tarih,saat;
        CardView card;
        ImageView sil,kopyala;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           goruldu=itemView.findViewById(R.id.goruldu);
           mesaj=itemView.findViewById(R.id.mesaj);
           tarih=itemView.findViewById(R.id.tarih);
           saat=itemView.findViewById(R.id.saat);
           card=itemView.findViewById(R.id.card);
           sil=itemView.findViewById(R.id.imgsil);
           kopyala=itemView.findViewById(R.id.imgkopyala);



        }
    }
    public int getItemViewType(int position){
        if (mesajlar.get(position).getGonderen().equals(BizimId)){
            return mesaj_sag;
        }
        else {
            return mesaj_sol;
        }
    }
}
