package com.sumeyrasimsek.projedersi.cerceve;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.sumeyrasimsek.projedersi.MainActivity;
import com.sumeyrasimsek.projedersi.Model.User;
import com.sumeyrasimsek.projedersi.R;

import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

    TextView txtProfilname,bilgi,konum;
    ImageView imgProfile,btnHesap,btnCikis;
    FirebaseUser mevcutKullanici;
    String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnHesap=view.findViewById(R.id.btnHesap);
        btnCikis=view.findViewById(R.id.btnCikis);
        imgProfile=view.findViewById(R.id.imgProfile);
        txtProfilname=view.findViewById(R.id.txtProfilname);
        bilgi=view.findViewById(R.id.txtBilgi);
        konum=view.findViewById(R.id.txtKonum);
        ButterKnife.bind(getActivity());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            txtProfilname.setText("Mailiniz: "+ (user.getEmail() != null ? user.getEmail(): user.getPhoneNumber()));
            btnCikis.setOnClickListener(v -> {


            });
        }
        btnHesap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.cerceve_kapsayici, new HesapAyarFragment());
                fragmentTransaction.commit();

            }
        });
        btnCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });




        mevcutKullanici= FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs=getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        id=prefs.getString("UserId","none");

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public ProfileFragment() {
        // Required empty public constructor
    }

}