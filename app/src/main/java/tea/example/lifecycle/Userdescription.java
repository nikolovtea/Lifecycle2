package tea.example.lifecycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Userdescription extends AppCompatActivity {

    private EditText opisZaKorisnik;
    private RatingBar rejtingZaKorisnik;
    private Button oceniKorisnik;
    private TextView emailDesno, telefonDesno;
    private String emailKorisnik, telefonKorisnik, tipNaUsluga;
    private float sumaRejting,rejting;
    private Integer brojRejting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdescription);

        emailDesno = findViewById(R.id.emailKorisnikDesno);
        telefonDesno = findViewById(R.id.telefonKorisnikDesno);
        opisZaKorisnik = findViewById(R.id.opisZaKorisnikText);
        rejtingZaKorisnik = findViewById(R.id.rejtingZaKorisnikText);
        oceniKorisnik = findViewById(R.id.ocenkaZaKorisnik);

        HashMap<String, Object> mapNaracka = new HashMap<>();
        HashMap<String, Object> mapUser = new HashMap<>();
        Intent intent = getIntent();
        emailKorisnik = intent.getStringExtra("EmailKorisnik");
        telefonKorisnik = intent.getStringExtra("TelefonKorisnik");
        tipNaUsluga = intent.getStringExtra("ActivityType");

        emailDesno.setText(emailKorisnik);
        telefonDesno.setText(telefonKorisnik);

        oceniKorisnik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Aktivnosti").orderByChild("ActivityType").equalTo(tipNaUsluga).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            mapNaracka.put("Status", "Zavrsena zadaca");
                            mapNaracka.put("OpisZaKorisnik", opisZaKorisnik.getText().toString());
                            mapNaracka.put("RejtingZaKorisnik", rejtingZaKorisnik.getRating());
                            FirebaseDatabase.getInstance().getReference("Aktivnosti").child(snap.getKey()).updateChildren(mapNaracka);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                FirebaseDatabase.getInstance().getReference("Korisnici").orderByChild("Email").equalTo(emailKorisnik).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            sumaRejting = Integer.parseInt(snap.child("SumaRejting").getValue().toString());
                            brojRejting = Integer.parseInt(snap.child("BrojNaRejting").getValue().toString());
                        }
                        sumaRejting += rejtingZaKorisnik.getRating();
                        brojRejting += 1;
                        rejting = sumaRejting/brojRejting;
                        mapUser.put("BrojNaRejting", brojRejting);
                        mapUser.put("SumaRejting", sumaRejting);
                        mapUser.put("Rejting", rejting);
                        for(DataSnapshot snap: snapshot.getChildren()){
                            FirebaseDatabase.getInstance().getReference("Korisnici").child(snap.getKey()).updateChildren(mapUser);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                startActivity(new Intent(Userdescription.this,Volonter.class));
            }
        });
    }
}