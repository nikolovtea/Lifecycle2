package tea.example.lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ViewActivity extends AppCompatActivity {
    private ArrayList<String> lines=new ArrayList<>();
    private ListView lista;
    private FirebaseAuth auth=FirebaseAuth.getInstance();

    private String aktivnost,opisNaUsluga,data,kratnost,itnost,status,volonter,rejting,telefon,emailVolonter,rejtingVolonter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        lista=findViewById(R.id.lista);

        String emailKorisnik=auth.getCurrentUser().getEmail().toString();
        FirebaseDatabase.getInstance().getReference("Aktivnosti").orderByChild("EmailKorisnik").equalTo(emailKorisnik
        ).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot:snapshot.getChildren()){
                    lines.add(postSnapshot.child("ActivityType").getValue().toString());
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(ViewActivity.this, android.R.layout.simple_list_item_1,lines);
                lista.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick (AdapterView < ? > parent, View view, int index, long id){
                String tipUsluga = lines.get(index);
                Intent intent = new Intent(ViewActivity.this,UserDetails.class);
                FirebaseDatabase.getInstance().getReference("Aktivnosti").orderByChild("ActivityType").equalTo(tipUsluga).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            aktivnost = postSnapshot.child("ActivityType").getValue().toString();
                            opisNaUsluga = postSnapshot.child("Opis").getValue().toString();
                            data = postSnapshot.child("Datum").getValue().toString();
                            kratnost = postSnapshot.child("Повторливост").getValue().toString();
                            itnost = postSnapshot.child("Итност").getValue().toString();
                            status = postSnapshot.child("Status").getValue().toString();
                            volonter = postSnapshot.child("ImeVolonter").getValue().toString();
                            rejting = postSnapshot.child("RejtingNaVolonter").getValue().toString();
                            telefon = postSnapshot.child("TelefonVolonter").getValue().toString();
                            emailVolonter = postSnapshot.child("EmailVolonter").getValue().toString();
                            rejtingVolonter = postSnapshot.child("RejtingZaVolonter").getValue().toString();
                        }
                        intent.putExtra("ActivityType",aktivnost);
                        intent.putExtra("Opis",opisNaUsluga);
                        intent.putExtra("Datum",data);
                        intent.putExtra("Повторливост",kratnost);
                        intent.putExtra("Итност",itnost);
                        intent.putExtra("Status",status);
                        intent.putExtra("ImeVolonter",volonter);
                        intent.putExtra("RejtingNaVolonter",rejting);
                        intent.putExtra("TelefonVolonter",telefon);
                        intent.putExtra("EmailVolonter",emailVolonter);
                        intent.putExtra("RejtingZaVolonter",rejtingVolonter);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });

    }

   /*public void addActivity(View view) {
        startActivity(new Intent(ViewActivity.this,PovozrasnoLice.class));
    }*/
}