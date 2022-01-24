package tea.example.lifecycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Volonter extends AppCompatActivity {
    private ArrayList<String> lines = new ArrayList<>();
    private ListView lista;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private Button  prijaveniZadaci;
    private Button logout;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String celoIme, datum, tipNaUsluga, rastojanie, rejting, lat, lon;
    private double volonterLat, volonterLon, korisnikLat, korisnikLon, distance;
   FusedLocationProviderClient fusedLocationProviderClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volonter);
        lista = findViewById(R.id.listaVolonter);

        prijaveniZadaci = findViewById(R.id.prijaveniZadaci);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Volonter.this,MainActivity.class));
            }
        });

        prijaveniZadaci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Volonter.this, ActiveAccVolonter.class));
            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Volonter.this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {
                    volonterLat = location.getLatitude();
                    volonterLon = location.getLongitude();
                }
            });
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }

        FirebaseDatabase.getInstance().getReference("Aktivnosti").orderByChild("Status").equalTo("Aktivna").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                  //  korisnikLat =(Double) postSnapshot.child("Lat").getValue();
                  //  korisnikLon =(Double) postSnapshot.child("Lon").getValue();
                    Location startPoint = new Location("locationA");
                    startPoint.setLatitude(volonterLat);
                    startPoint.setLongitude(volonterLon);

                    Location endPoint = new Location("locationB");
                    endPoint.setLatitude(korisnikLat);
                    endPoint.setLongitude(korisnikLon);
                    DecimalFormat f = new DecimalFormat("##.00");
                    distance=startPoint.distanceTo(endPoint);
                    lines.add(postSnapshot.child("ActivityType").getValue().toString()+ " - " + f.format(distance) + " m away");

                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(Volonter.this, android.R.layout.simple_list_item_1,lines);
                lista.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] niza = lines.get(i).split(" - ");
                String tipUsluga = niza[0];
                Intent intent = new Intent(Volonter.this,VolonterDetails.class);
                FirebaseDatabase.getInstance().getReference("Aktivnosti").orderByChild("ActivityType").equalTo(tipUsluga).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                          korisnikLat =(Double) postSnapshot.child("Lat").getValue();
                            korisnikLon =(Double) postSnapshot.child("Lon").getValue();
                            Location startPoint=new Location("locationA");
                            startPoint.setLatitude(volonterLat);
                            startPoint.setLongitude(volonterLon);

                            Location endPoint=new Location("locationA");
                            endPoint.setLatitude(korisnikLat);
                            endPoint.setLongitude(korisnikLon);

                            rastojanie = String.valueOf(startPoint.distanceTo(endPoint));

                            celoIme = postSnapshot.child("Ime").getValue().toString();
                            datum = postSnapshot.child("Datum").getValue().toString();
                            tipNaUsluga = postSnapshot.child("ActivityType").getValue().toString();
                            rejting = postSnapshot.child("RejtingNaKorisnik").getValue().toString();
                        }
                        intent.putExtra("ActivityType",tipNaUsluga);
                        intent.putExtra("Ime",celoIme);
                        intent.putExtra("Datum",datum);
                        intent.putExtra("Rastojanie",rastojanie);
                        intent.putExtra("RejtingZaVolonter",rejting);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}