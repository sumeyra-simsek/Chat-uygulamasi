package com.sumeyrasimsek.projedersi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sumeyrasimsek.projedersi.Model.User;
import com.sumeyrasimsek.projedersi.common.common;

public class KayitActivity extends AppCompatActivity {

    MaterialEditText edtPhone,edtPassword,edtName;
    Button btnKayitOl;
    DatabaseReference table_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);
        edtPassword =(MaterialEditText) findViewById(R.id.edtPassword);
        edtPhone=(MaterialEditText) findViewById(R.id.edtPhone);
        edtName=(MaterialEditText) findViewById(R.id.edtName);
        btnKayitOl=(Button) findViewById(R.id.btnKayitOl);

       // Typeface face = Typeface.createFromAsset(getAssets(),"fonts/nabila.TTF");
        //btnKayitOl.setTypeface(face);



        table_user= FirebaseDatabase.getInstance().getReference("Users");
        btnKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                table_user.child(edtPhone.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            Toast.makeText(KayitActivity.this, "Kayıtlı Hesap.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            User user = new User(edtName.getText().toString().trim(),edtPassword.getText().toString().trim(),edtPhone.getText().toString().trim(),null,null,null);
                            table_user.child(edtPhone.getText().toString().trim()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(KayitActivity.this, "Veri başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                                        editor.putString("UserId", user.getId());
                                        editor.apply();

                                        Intent menuintent=new Intent(KayitActivity.this, AnaSayfaActivity.class);
                                        common.currentUser=user;
                                        startActivity(menuintent);
                                        finish();
                                    } else {
                                        Toast.makeText(KayitActivity.this, "Veri kaydedilirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(KayitActivity.this, "Veri kaydedilirken bir hata oluştu.", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


    }
}