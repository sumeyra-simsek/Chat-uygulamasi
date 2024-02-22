package com.sumeyrasimsek.projedersi.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sumeyrasimsek.projedersi.Model.Chat;
import com.sumeyrasimsek.projedersi.Model.User;
import com.sumeyrasimsek.projedersi.R;
import com.sumeyrasimsek.projedersi.cerceve.MesajFragment;
import com.sumeyrasimsek.projedersi.cerceve.kisiFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {


    FirebaseUser mevcutKullanici;
    String UserId;
    Context context;
    List<User> UserList;
    String id2;
    private String sonmesaj;
    private boolean ischat;

    public UserAdapter(Context context, List<User> modelUserList,boolean ischat) {
        this.context = context;
        this.UserList = modelUserList;
        this.ischat=ischat;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.arama_ogesi,parent ,false);


        mevcutKullanici= FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs2= context.getSharedPreferences("PREFS", MODE_PRIVATE);
        id2=prefs2.getString("UserId","none");

        return new UserAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       final User user= UserList.get(position);
       holder.txt_aramakisi.setText(user.getName());
       Glide.with(context).load(UserList.get(position).getImgUrl())
                .into(holder.img_Aramaogesi);
       if(ischat){
           sonmesajj(user.getId(),holder.txtSonmesaj);
       }
       else{
           holder.txtSonmesaj.setVisibility(View.GONE);
       }
        if(user.getId().equals(id2))
        {
            holder.txt_aramakisi.setVisibility(View.GONE);
            holder.img_Aramaogesi.setVisibility(View.GONE);
            holder.txtSonmesaj.setVisibility(View.GONE);
            holder.mesaj.setVisibility(View.GONE);
            holder.info.setVisibility(View.GONE);


        }
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("kisininId",user.getId());
                editor.apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.cerceve_kapsayici,
                                new kisiFragment()).commit();
            }
        });


       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            SharedPreferences.Editor editor = context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
            editor.putString("kisininId",user.getId());
            editor.apply();
           }
       });
       holder.mesaj.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               mevcutKullanici= FirebaseAuth.getInstance().getCurrentUser();
               SharedPreferences prefs2= context.getSharedPreferences("PREFS", MODE_PRIVATE);
               id2=prefs2.getString("UserId","none");


               SharedPreferences.Editor editor = context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
               editor.putString("kisininId",user.getId());
               editor.apply();

               ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                       .replace(R.id.cerceve_kapsayici,
                               new MesajFragment()).commit();
           }
       });


    }

    private void sonmesajj(String id, TextView txtSonmesaj) {
        sonmesaj="default";
        FirebaseDatabase.getInstance().getReference("Mesajlar").child(id2)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren())
                        {
                            Chat chat = snapshot1.getValue(Chat.class);
                            if(chat.getAlici().equals(id2) && chat.getGonderen().equals(id)||
                                chat.getAlici().equals(id) && chat.getGonderen().equals(id2))
                            {
                                sonmesaj=chat.getMesaj();
                            }
                        }
                        switch (sonmesaj){
                            case "default":
                                txtSonmesaj.setText("İlk mesajı gönderin");
                                break;
                            default:
                                txtSonmesaj.setText(sonmesaj);
                                break;
                        }
                        sonmesaj="default";
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    @Override
    public int getItemCount() {
        return UserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_aramakisi,txtSonmesaj;
        CircleImageView img_Aramaogesi;
        ImageView mesaj,info;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           txt_aramakisi=itemView.findViewById(R.id.txt_aramakisi);
           img_Aramaogesi=itemView.findViewById(R.id.image_aramaogesi);
           mesaj=itemView.findViewById(R.id.mesajgonder);
           txtSonmesaj=itemView.findViewById(R.id.txtSonmesaj);
           info=itemView.findViewById(R.id.kisibilgisi);


        }
    }
}
