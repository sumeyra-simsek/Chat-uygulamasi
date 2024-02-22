package com.sumeyrasimsek.projedersi.cerceve;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sumeyrasimsek.projedersi.Adapter.ChatAdapter;
import com.sumeyrasimsek.projedersi.Adapter.UserAdapter;
import com.sumeyrasimsek.projedersi.Model.Chat;
import com.sumeyrasimsek.projedersi.Model.User;
import com.sumeyrasimsek.projedersi.R;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> users;
    private List<String> userlist = new ArrayList<>();
    FirebaseUser mevcutKullanici;
    String BizimId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView=view.findViewById(R.id.chatrecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        users = new ArrayList<>();
        userAdapter = new UserAdapter(getActivity(), users,true);
        recyclerView.setAdapter(userAdapter);

        mevcutKullanici= FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs2=getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        BizimId =prefs2.getString("UserId","none"); //bizim id


        FirebaseDatabase.getInstance().getReference("Mesajlar").child(BizimId)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                userlist.clear();
                                if(snapshot.exists()){
                                    for(DataSnapshot snapshot1: snapshot.getChildren())
                                    {
                                            Chat chat = snapshot1.getValue(Chat.class);
                                            if (chat.getGonderen().equals(BizimId)) {
                                                userlist.add(chat.getAlici());
                                            }
                                            if (chat.getAlici().equals(BizimId)) {
                                                userlist.add(chat.getGonderen());
                                            }


                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


        kullaniciOku();

        return view;
    }

    private void kullaniciOku() {
        FirebaseDatabase.getInstance().getReference("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        users.clear();
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            User user = snapshot1.getValue(User.class);
                            for(String a: userlist){
                                if(user.getId().equals(a)&& !users.contains(user)) {
                                    users.add(user);
                                    if(users.size()!=0){

                                    }
                                    else {
                                        users.add(user);
                                    }
                                }
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public ChatFragment() {
    }

}