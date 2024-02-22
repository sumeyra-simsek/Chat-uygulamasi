package com.sumeyrasimsek.projedersi.cerceve;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sumeyrasimsek.projedersi.Adapter.NewsAdapter;
import com.sumeyrasimsek.projedersi.Model.News;
import com.sumeyrasimsek.projedersi.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment{
    RecyclerView recyclerView;
    ArrayList<News> modelArrayList;
    NewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        modelArrayList=new ArrayList<>();
        newsAdapter=new NewsAdapter(getContext(),modelArrayList);
        recyclerView.setAdapter(newsAdapter);
        goster();
        return view;



    }
    private void goster ()
    {

        DatabaseReference yol = FirebaseDatabase.getInstance().getReference("news");
        yol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    News news =snapshot.getValue(News.class);
                    modelArrayList.add(news);

                }
                newsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }



    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



}