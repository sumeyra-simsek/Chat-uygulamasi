package com.sumeyrasimsek.projedersi.cerceve;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.sumeyrasimsek.projedersi.Model.User;
import com.sumeyrasimsek.projedersi.R;


public class kisiFragment extends Fragment {

    TextView txtProfilname,bilgi,konum;
    ImageView imgProfile;
    FirebaseUser mevcutKullanici;
    String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kisi, container, false);


        imgProfile=view.findViewById(R.id.imgProfile);
        txtProfilname=view.findViewById(R.id.txtProfilname);
        bilgi=view.findViewById(R.id.txtBilgi);
        konum=view.findViewById(R.id.txtKonum);


        mevcutKullanici= FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs=getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        id=prefs.getString("kisininId","none");

        kullaniciBilgisi();
        return view;
    }
    private void kullaniciBilgisi(){
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Users").child(id);
        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        txtProfilname.setText(user.getName());
                        //Glide.with(getContext()).load(user.getImgUrl()).into(imgProfile);
                        Picasso.get().load(user.getImgUrl()).into(imgProfile);
                        bilgi.setText(user.getMail());
                        konum.setText(user.getKonum());


                    } else {
                        Toast.makeText(getContext(), "user null", Toast.LENGTH_SHORT).show();
                        // user null olduğunda yapılacak işlemler

                    }
                } else {
                    // snapshot null veya veri yok
                }

                /*if (getContext()==null)
                    return;
                if(){
                    User user=snapshot.getValue(User.class);
                   // Glide.with(getContext()).load(user.get..).into(imgProfile);
                    txtProfilname.setText(user.getName());
                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public kisiFragment() {
    }



}