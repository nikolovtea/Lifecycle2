package tea.example.lifecycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Volonterdescription extends AppCompatActivity {

    private EditText opisZaVolonter;
    private RatingBar rejtingZaVolonter;
    private Button potvrdi;
    private float sumaRejting,rejting;
    private Integer brojRejting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volonterdescription);

        opisZaVolonter = findViewById(R.id.opisZaVolonterText);
        rejtingZaVolonter = findViewById(R.id.rejtingZaVolonterText);
        potvrdi = findViewById(R.id.ocenkaZaVolonter);

        Intent intent = getIntent();
        String tipNaUsluga = intent.getStringExtra("ActivityType");
        String emailVolonter = intent.getStringExtra("EmailVolonter");
        HashMap<String, Object> mapUser = new HashMap<>();
        HashMap<String, Object> mapNaracka = new HashMap<>();

        potvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Aktivnosti").orderByChild("ActivityType").equalTo(tipNaUsluga).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            mapNaracka.put("OpisZaVolonter", opisZaVolonter.getText().toString());
                            mapNaracka.put("RejtingZaVolonter", rejtingZaVolonter.getRating());
                            FirebaseDatabase.getInstance().getReference("Aktivnosti").child(snap.getKey()).updateChildren(mapNaracka);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                FirebaseDatabase.getInstance().getReference("Korisnici").orderByChild("Email").equalTo(emailVolonter).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            sumaRejting = Integer.parseInt(snap.child("SumaRejting").getValue().toString());
                            brojRejting = Integer.parseInt(snap.child("BrojNaRejting").getValue().toString());
                        }
                        sumaRejting += rejtingZaVolonter.getRating();
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
                startActivity(new Intent(Volonterdescription.this,ViewActivity.class));
            }
        });
    }
}