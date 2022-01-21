package tea.example.lifecycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class VolonterDetails extends AppCompatActivity {
    private TextView imeKorisnikDesno, datumDesno, tipNaUslugaDesno, rastojanieDesno, rejtingDesno;
    private Button prifatiVolonter;
    private String imeVolonter, rejtingVolonter, telefonVolonter, emailVolonter;
    private FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volonter_details);

        prifatiVolonter = findViewById(R.id.prifatiVolonter);
        imeKorisnikDesno = findViewById(R.id.imeKorisnikDesno);
        datumDesno = findViewById(R.id.datumVolonterDesno);
        tipNaUslugaDesno = findViewById(R.id.tipUslugaVolonterDesno);
        rastojanieDesno = findViewById(R.id.rastojanieDesno);
        rejtingDesno = findViewById(R.id.rejtingVolonterDesno);

        Intent intent = getIntent();
        String celoIme = intent.getStringExtra("Ime");
        String datum = intent.getStringExtra("Datum");
        String tipNaUsluga = intent.getStringExtra("ActivityType");
       // String rastojanie = intent.getStringExtra("Rastojanie");
        String rejting = intent.getStringExtra("RejtingVolonter");

        imeKorisnikDesno.setText(celoIme);
        datumDesno.setText(datum);
        tipNaUslugaDesno.setText(tipNaUsluga);
        //rastojanieDesno.setText(rastojanie + " km");
        rejtingDesno.setText(rejting);

        HashMap<String, Object> map = new HashMap<>();

        prifatiVolonter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prifatiVolonter.setVisibility(View.GONE);
                emailVolonter = auth.getCurrentUser().getEmail().toString();
                FirebaseDatabase.getInstance().getReference("Korisnici").orderByChild("Email").equalTo(emailVolonter).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot datas: snapshot.getChildren()){
                            imeVolonter = datas.child("FullName").getValue().toString();
                            rejtingVolonter = datas.child("Rejting").getValue().toString();
                            telefonVolonter = datas.child("Phone").getValue().toString();
                        }
                        map.put("EmailVolonter",emailVolonter);
                        map.put("ImeVolonter",imeVolonter);
                        map.put("TelefonVolonter",telefonVolonter);
                        map.put("RejtingNaVolonter", rejtingVolonter);
                        map.put("Status","Prijaven volonter");
                        FirebaseDatabase.getInstance().getReference("Aktivnosti").orderByChild("ActivityType").equalTo(tipNaUsluga).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                    FirebaseDatabase.getInstance().getReference().child("Aktivnosti").child(postSnapshot.getKey()).updateChildren(map);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
}