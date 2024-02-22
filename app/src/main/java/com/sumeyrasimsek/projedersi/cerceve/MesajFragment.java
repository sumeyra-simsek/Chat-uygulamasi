package com.sumeyrasimsek.projedersi.cerceve;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.sumeyrasimsek.projedersi.Adapter.ChatAdapter;
import com.sumeyrasimsek.projedersi.Model.Chat;
import com.sumeyrasimsek.projedersi.Model.User;
import com.sumeyrasimsek.projedersi.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MesajFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Chat> mesajlarimiz;
    private StringBuilder saat,tarih;

    ImageButton geritusu;
    CircleImageView profilresmi;
    TextView isim;
    EditText mesajyaz;
    ImageView gonder;
    FirebaseUser mevcutKullanici;
    String BizimId;
    String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_mesaj, container, false);
        geritusu=view.findViewById(R.id.geritusu);
        profilresmi=view.findViewById(R.id.profilresmichat);
        isim=view.findViewById(R.id.txtisim);
        mesajyaz=view.findViewById(R.id.edtmesaj);
        gonder=view.findViewById(R.id.gonder);
        saat=new StringBuilder();
        tarih=new StringBuilder();
        Date bugün= Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String date= simpleDateFormat.format(bugün);
        tarih.append(date);

        Date zaman=Calendar.getInstance().getTime();
        SimpleDateFormat saatformat=new SimpleDateFormat("hh:mm");
        String time=saatformat.format(zaman);
        saat.append(time);

        recyclerView=view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mesajlarimiz = new ArrayList<>();
        chatAdapter = new ChatAdapter(getActivity(), mesajlarimiz);
        recyclerView.setAdapter(chatAdapter);



        mevcutKullanici= FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs=getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        id=prefs.getString("kisininId","none"); // konustugumuz kisi

        SharedPreferences prefs2=getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        BizimId =prefs2.getString("UserId","none"); //bizim id


        geritusu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.cerceve_kapsayici, new SearchFragment());
                fragmentTransaction.commit();
            }
        });

        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesaj=mesajyaz.getText().toString();
                if(!mesaj.equals("")){
                    mesajGonderen(BizimId,id,mesaj);
                    mesajAlan(BizimId,id,mesaj);

                }
                else
                {
                    Toast.makeText(getActivity(), "Mesaj kısmı boş.", Toast.LENGTH_SHORT).show();
                }
                mesajyaz.setText("");
            }
        });




        kullaniciBilgisi();
        return view;
    }

    private void mesajGonderen(String gonderen, String alici, String mesaj) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("gonderen",gonderen);
        hashMap.put("alici",alici);
        hashMap.put("mesaj",mesaj);
        hashMap.put("resim","");
        hashMap.put("saat",saat.toString());
        hashMap.put("tarih",tarih.toString());

        FirebaseDatabase.getInstance().getReference().child("Mesajlar").child(gonderen).push().setValue(hashMap); // id2 yerine gonderen yazdm

    }
    private void mesajAlan(String gonderen, String alici, String mesaj) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("gonderen",gonderen);
        hashMap.put("alici",alici);
        hashMap.put("mesaj",mesaj);
        hashMap.put("resim","");
        hashMap.put("saat",saat.toString());
        hashMap.put("tarih",tarih.toString());
        FirebaseDatabase.getInstance().getReference().child("Mesajlar").child(alici).push().setValue(hashMap); // id yerine alici yazdm

    }


    private void kullaniciBilgisi(){
        FirebaseDatabase.getInstance().getReference("Users").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        isim.setText(user.getName());
                        Picasso.get().load(user.getImgUrl()).into(profilresmi);
                        mesajOku(BizimId,id);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void mesajOku(final String gonderen, final String alici) {
        FirebaseDatabase.getInstance().getReference("Mesajlar").child(BizimId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mesajlarimiz.clear();
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            Chat chat= snapshot1.getValue(Chat.class);
                                if (chat.getAlici().equals(gonderen) && chat.getGonderen().equals(alici) ||
                                        chat.getAlici().equals(alici) && chat.getGonderen().equals(gonderen))
                                {
                                    mesajlarimiz.add(chat);

                                }

                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    public MesajFragment() {
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}