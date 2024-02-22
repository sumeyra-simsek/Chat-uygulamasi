package com.sumeyrasimsek.projedersi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    Button btnGirisYap,btnKayitOl;
    TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGirisYap=(Button) findViewById(R.id.btnGirisYap);
        btnKayitOl=(Button) findViewById(R.id.btnKayitOl);
        txtSlogan=(TextView) findViewById(R.id.txtSlogan);
       // Typeface face = Typeface.createFromAsset(getAssets(),"fonts/nabila.TTF");
        //txtSlogan.setTypeface(face);
       // btnKayitOl.setTypeface(face);
        //btnGirisYap.setTypeface(face);

        btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GirisYap=new Intent(MainActivity.this, GirisActivity.class);
                startActivity(GirisYap);
            }
        });

        btnKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent KayitOl=new Intent(MainActivity.this,KayitActivity.class);
                startActivity(KayitOl);
            }
        });
    }
}