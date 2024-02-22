package com.sumeyrasimsek.projedersi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sumeyrasimsek.projedersi.Model.User;
import com.sumeyrasimsek.projedersi.common.common;

public class GirisActivity extends AppCompatActivity {
    EditText edtPhone,edtPassword;
    Button btnGirisYap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        edtPassword =(MaterialEditText) findViewById(R.id.edtPassword);
        edtPhone=(MaterialEditText) findViewById(R.id.edtPhone);
        btnGirisYap=(Button) findViewById(R.id.btnGirisYap);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user= database.getReference("Users");
        //Typeface face = Typeface.createFromAsset(getAssets(),"fonts/nabila.TTF");
        //btnGirisYap.setTypeface(face);



        btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog mDialog = new ProgressDialog(GirisActivity.this);
                mDialog.setMessage("Lütfen bekleyiniz...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                                editor.putString("UserId", user.getId());
                                editor.apply();
                                Intent menuintent=new Intent(GirisActivity.this, AnaSayfaActivity.class);
                                common.currentUser=user;
                                startActivity(menuintent);
                                finish();
                            } else {
                                Toast.makeText(GirisActivity.this, "Yanlış Parola.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(GirisActivity.this, "Kullanıcı Bulunamıyor.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}