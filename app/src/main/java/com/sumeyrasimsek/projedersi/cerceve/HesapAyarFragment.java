package com.sumeyrasimsek.projedersi.cerceve;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.sumeyrasimsek.projedersi.Model.User;
import com.sumeyrasimsek.projedersi.R;


public class HesapAyarFragment extends Fragment {

    Button fotoyukle;
    FloatingActionButton Guncelle;
    EditText edtSifre,edtisim,edtBilgi,edtKonum;
    ImageView imgyukle;
    private Uri imageUri;
    FirebaseUser mevcutKullanici;
    String id;
    final private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    final  private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_hesap_ayar, container, false);

               mevcutKullanici= FirebaseAuth.getInstance().getCurrentUser();
               SharedPreferences prefs=getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
               id=prefs.getString("UserId","none");
               DatabaseReference yol= FirebaseDatabase.getInstance().getReference("Users").child(id);
              fotoyukle=view.findViewById(R.id.btnfotoyukle);
              Guncelle=view.findViewById(R.id.FbtnGuncelle);
              edtisim=view.findViewById(R.id.edtIsim);
              edtBilgi=view.findViewById(R.id.edtBilgi);
              edtSifre=view.findViewById(R.id.edtSifre);
              edtKonum=view.findViewById(R.id.edtKonum);
              imgyukle=view.findViewById(R.id.imgyukle);
              kullaniciBilgisi();

              Guncelle.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      guncelle();
                  }
              });

        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            Intent data = result.getData();
                            imageUri = data.getData();
                            imgyukle.setImageURI(imageUri);
                        }
                        else{
                            Toast.makeText(getActivity(), "Resim Seçilmedi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        imgyukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        fotoyukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri != null){
                    uploadToFirebase(imageUri);

                }
                else {
                    Toast.makeText(getActivity(), "Lütfen resim seçiniz.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void guncelle(){
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Users").child(id);
        String yeniisim=edtisim.getText().toString().trim();
        String yenisifre=edtSifre.getText().toString().trim();
        String yenimail=edtBilgi.getText().toString().trim();
        String yenikonum=edtKonum.getText().toString().trim();
        kullaniciYolu.child("name").setValue(yeniisim,((error, ref) -> {
            if (error == null) {
                Toast.makeText(getActivity(), "İsim Değiştirildi", Toast.LENGTH_SHORT).show();
            } else {
                // İlk veri güncellenirken bir hata oluştuğunda yapılacak işlemler
            }
        }));
        kullaniciYolu.child("password").setValue(yenisifre,((error, ref) -> {
            if (error==null) {
                Toast.makeText(getActivity(), "Şifre Değiştirildi.", Toast.LENGTH_SHORT).show();
            }
        }));
        kullaniciYolu.child("mail").setValue(yenimail,((error, ref) -> {
            if (error == null) {
                Toast.makeText(getActivity(), "Bilgiler Güncellendi.", Toast.LENGTH_SHORT).show();
            } else {
                // İlk veri güncellenirken bir hata oluştuğunda yapılacak işlemler
            }
        }));
        kullaniciYolu.child("konum").setValue(yenikonum,((error, ref) -> {
            if (error == null) {
                Toast.makeText(getActivity(), "Bilgiler Güncellendi.", Toast.LENGTH_SHORT).show();
            } else {
                // İlk veri güncellenirken bir hata oluştuğunda yapılacak işlemler
            }
        }));

    }


    private void uploadToFirebase(Uri uri){
        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
                       imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                               imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                         @Override
                                         public void onSuccess(Uri uri) {
                                             DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(id);
                                             databaseReference.child("imgUrl").setValue(uri.toString());
                                             Toast.makeText(getActivity(), "yüklendi", Toast.LENGTH_SHORT).show();

                                         }
                                     }).addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {
                                             Toast.makeText(getActivity(), "yükleme başarısız", Toast.LENGTH_SHORT).show();
                                         }
                                     })    ;
                           }

                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(getActivity(), "yükleme başarısız", Toast.LENGTH_SHORT).show();

                           }
                       });

    }

    private String getFileExtension(Uri fileUri)
    {
        ContentResolver contentResolver= getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

    public HesapAyarFragment() {
        // Required empty public constructor
    }


    private void kullaniciBilgisi(){
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Users").child(id);
        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        edtBilgi.setText(user.getMail());
                        edtisim.setText(user.getName());
                        edtKonum.setText(user.getKonum());
                        edtSifre.setText(user.getPassword());

                    } else {
                        Toast.makeText(getContext(), "user null", Toast.LENGTH_SHORT).show();

                    }
                } else {
                }


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

}