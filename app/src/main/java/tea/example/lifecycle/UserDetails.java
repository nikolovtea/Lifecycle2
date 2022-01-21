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

public class UserDetails extends AppCompatActivity {
    private Button prifati, otkazi, oceniVolonter;
    private TextView tipUslugaDesno, opisUslugaDesno, datumDesno, povtorlivostDesno, itnostDesno, statusDesno, volonterDesno, rejtingDesno, telefonDesno, emailVolonterDesno;
    private TextView volonterLevo, rejtingLevo, telefonLevo,emailVolonterLevo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Intent intent = getIntent();
        String tipNaUsluga = intent.getStringExtra("ActivityType");
        String datum = intent.getStringExtra("Datum");
        String opisNaUsluga = intent.getStringExtra("Opis");
        String povtorlivost = intent.getStringExtra("Повторливост");
        String itnost = intent.getStringExtra("Итност");
        String status = intent.getStringExtra("Status");
        String volonter = intent.getStringExtra("ImeVolonter");
        String rejting = intent.getStringExtra("RejtingNaVolonter");
        String telefon = intent.getStringExtra("TelefonVolonter");
        String emailVolonter = intent.getStringExtra("EmailVolonter");
        String rejtingZaVolonter = intent.getStringExtra("RejtingZaVolonter");

        prifati = findViewById(R.id.prifati);
        otkazi = findViewById(R.id.otkazi);
        oceniVolonter = findViewById(R.id.oceniVolonter);
        tipUslugaDesno = findViewById(R.id.tipUslugaDesno);
        opisUslugaDesno = findViewById(R.id.opisUslugaDesno);
        datumDesno = findViewById(R.id.datumDesno);
        povtorlivostDesno = findViewById(R.id.povtorlivostDesno);
        itnostDesno = findViewById(R.id.itnostDesno);
        statusDesno = findViewById(R.id.statusDesno);
        volonterDesno = findViewById(R.id.volonterDesno);
        rejtingDesno = findViewById(R.id.rejtingDesno);
        telefonDesno = findViewById(R.id.telefonDesno);
        emailVolonterDesno = findViewById(R.id.emailVolonterDesno);
        volonterLevo = findViewById(R.id.volonterLevo);
        rejtingLevo = findViewById(R.id.rejtingLevo);
        telefonLevo = findViewById(R.id.telefonLevo);
        emailVolonterLevo = findViewById(R.id.emailVolonterLevo);

        tipUslugaDesno.setText(tipNaUsluga);
        opisUslugaDesno.setText(opisNaUsluga);
        datumDesno.setText(datum);
        povtorlivostDesno.setText(povtorlivost);
        itnostDesno.setText(itnost);
        statusDesno.setText(status);
        volonterDesno.setText(volonter);
        rejtingDesno.setText(rejting);
        telefonDesno.setText(telefon);
        emailVolonterDesno.setText(emailVolonter);

        if(status.equals("Aktivna")){
            prifati.setVisibility(View.GONE);
            otkazi.setVisibility(View.GONE);
            volonterDesno.setVisibility(View.GONE);
            rejtingDesno.setVisibility(View.GONE);
            telefonDesno.setVisibility(View.GONE);
            emailVolonterDesno.setVisibility(View.GONE);
            volonterLevo.setVisibility(View.GONE);
            rejtingLevo.setVisibility(View.GONE);
            telefonLevo.setVisibility(View.GONE);
            emailVolonterLevo.setVisibility(View.GONE);
            oceniVolonter.setVisibility(View.GONE);
        }else if(status.equals("Prijaven volonter")){
            oceniVolonter.setVisibility(View.GONE);
        }else if(status.equals("Zakazana zadaca")){
            prifati.setVisibility(View.GONE);
            otkazi.setVisibility(View.GONE);
            oceniVolonter.setVisibility(View.GONE);
        }else if(status.equals("Zavrsena zadaca")){
            if (Integer.parseInt(rejtingZaVolonter) != 0){
                oceniVolonter.setVisibility(View.GONE);
            }
            prifati.setVisibility(View.GONE);
            otkazi.setVisibility(View.GONE);
        }

        HashMap<String, Object> map = new HashMap<>();
        prifati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusDesno.setText("Zakazana zadaca");
                prifati.setVisibility(View.GONE);
                otkazi.setVisibility(View.GONE);
                FirebaseDatabase.getInstance().getReference("Aktivnosti").orderByChild("ActivityType").equalTo(tipNaUsluga).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            map.put("Status","Zakazana zadaca");
                            FirebaseDatabase.getInstance().getReference("Aktivnosti").child(snap.getKey()).updateChildren(map);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        otkazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusDesno.setText("Aktivna");
                prifati.setVisibility(View.GONE);
                otkazi.setVisibility(View.GONE);
                volonterDesno.setVisibility(View.GONE);
                rejtingDesno.setVisibility(View.GONE);
                telefonDesno.setVisibility(View.GONE);
                emailVolonterDesno.setVisibility(View.GONE);
                volonterLevo.setVisibility(View.GONE);
                rejtingLevo.setVisibility(View.GONE);
                telefonLevo.setVisibility(View.GONE);
                emailVolonterLevo.setVisibility(View.GONE);
                oceniVolonter.setVisibility(View.GONE);
                FirebaseDatabase.getInstance().getReference("Aktivnosti").orderByChild("ActivityType").equalTo(tipNaUsluga).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            map.put("Status","Aktivna");
                            map.put("EmailVolonter","");
                            map.put("ImeVolonter","");
                            map.put("TelefonVolonter","");
                            map.put("RejtingNaVolonter", 0);
                            FirebaseDatabase.getInstance().getReference("Aktivnosti").child(snap.getKey()).updateChildren(map);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        oceniVolonter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDetails.this, Volonterdescription.class);
                intent.putExtra("ActivityType",tipNaUsluga);
                intent.putExtra("EmailVolonter", emailVolonter);
                startActivity(intent);
            }
        });
    }
}