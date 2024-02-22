package com.sumeyrasimsek.projedersi.cerceve;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sumeyrasimsek.projedersi.Model.News;
import com.sumeyrasimsek.projedersi.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EkleFragment extends Fragment {
    EditText Baslik,Aciklama,Link;
    Button btnGonder;
    private DatabaseReference table_news;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    String currentDate = dateFormat.format(new Date());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_ekle, container, false);
        Baslik=view.findViewById(R.id.edtBaslik);
        Aciklama=view.findViewById(R.id.edtAciklama);
        Link=view.findViewById(R.id.edtLink);
        btnGonder=view.findViewById(R.id.btnGonder);
        table_news = FirebaseDatabase.getInstance().getReference("news");
        btnGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        return view;
    }
    private void saveData() {
        String baslik = Baslik.getText().toString().trim();
        String aciklama = Aciklama.getText().toString().trim();
        String link = Link.getText().toString().trim();
        String key = table_news.push().getKey();
        // Verileri Firebase veritabanına kaydet
        table_news.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // ID numarası zaten varsa mesajı göster
                    Toast.makeText(getContext(), "Bu oda kullanılıyor.", Toast.LENGTH_SHORT).show();
                } else {
                    News news = new News(aciklama,link,key,baslik,currentDate);
                    table_news.child(key).setValue(news)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Gönderi başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Gönderi kaydedilirken hata oluştu.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public EkleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}