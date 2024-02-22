package com.sumeyrasimsek.projedersi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sumeyrasimsek.projedersi.cerceve.ChatFragment;
import com.sumeyrasimsek.projedersi.cerceve.EkleFragment;
import com.sumeyrasimsek.projedersi.cerceve.HomeFragment;
import com.sumeyrasimsek.projedersi.cerceve.ProfileFragment;
import com.sumeyrasimsek.projedersi.cerceve.SearchFragment;


public class AnaSayfaActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation;
    Fragment seciliCerceve;
    String id;
    FirebaseUser mevcutKullanici;

    public static FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);
        bottom_navigation= findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new HomeFragment()).commit(); //program ilk acildiginda homefragment acılsın.
        fragmentManager = getSupportFragmentManager();

        mevcutKullanici= FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs=getSharedPreferences("PREFS", MODE_PRIVATE);
        id=prefs.getString("UserId","none");

        if(findViewById(R.id.cerceve_kapsayici) != null)
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            HomeFragment homeFragment=new HomeFragment();
            fragmentTransaction.add(R.id.cerceve_kapsayici,homeFragment,null);
            fragmentTransaction.commit();
        }



    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId())
                    {
                        case R.id.nav_home:
                            seciliCerceve=new HomeFragment();
                            break;
                        case R.id.nav_search:
                            seciliCerceve=new SearchFragment();
                            break;
                        case R.id.nav_gonderi:
                            seciliCerceve=new EkleFragment();
                            break;
                        case R.id.nav_chat:
                            seciliCerceve=new ChatFragment();
                            SharedPreferences.Editor editor2 = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor2.putString("UserId",id);
                            editor2.apply();
                            break;
                        case R.id.nav_profile:
                                seciliCerceve = new ProfileFragment();
                                SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                                editor.putString("UserId",id);
                                editor.apply();

                            //mevcut kullanıcının uidsini alir getirir.
                            break;
                    }

                    if(seciliCerceve != null)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,seciliCerceve).commit();
                        //gecise basla-yer degistir-framelayout(cercevekapsayici)'un icine secilicerceveyi ekler
                        //secili cerceve idlere gore degisiyo.id home ise home,arama ise arama...
                    }



                    return false;
                }
            };
}