package tea.example.lifecycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.app.TimePickerDialog;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;

import java.util.Calendar;


public class PovozrasnoLice extends AppCompatActivity {
    private static final String TAG="PovozrasnoLice";
   private static final int PERMISSIONS_FINE_LOCATION = 99;

    private Spinner spinner1,spinner2;
    private EditText aktivnost,opis;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Button timeButton;
    private int hour, minute;
    private Button save;
    private Button lokacija;
    private Button listN;
    private Button logout;


    private String userID;


    private FirebaseAuth auth;

    private double lat,lon,newLat,newLon;
    private String fullName,rejting,telefon,datumVreme;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_povozrasno_lice);

        auth=FirebaseAuth.getInstance();

        timeButton = findViewById(R.id.time);


        initDatePicker();
        dateButton = findViewById(R.id.datePicker);
        dateButton.setText(getTodaysDate());
        lokacija=findViewById(R.id.lokacija);
        spinner1=findViewById(R.id.kratnost);
        spinner2=findViewById(R.id.itnost);
        aktivnost=findViewById(R.id.aktivnost);
        opis=findViewById(R.id.opis);
        save=findViewById(R.id.save);
        listN=findViewById(R.id.listN);
        logout=findViewById(R.id.logout);

        Intent intent = getIntent();
        newLat = intent.getDoubleExtra("NewLat",0);
        newLon = intent.getDoubleExtra("NewLon",0);



       /* ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.kratnost, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.itnost, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);*/



        HashMap<String,Object> map=new HashMap<>();
        String emailKorisnik=auth.getCurrentUser().getEmail().toString();
        map.put("EmailKorisnik",emailKorisnik);
        if(spinner1.getSelectedItem().equals("Еднократна")){
            map.put("Повторливост","Еднократна");
        }
        else{
            map.put("Повторливост","Повторлива");
        }
        if(spinner2.getSelectedItem().equals("Итно")){
            map.put("Итност","Итно");
        }
        else {
            map.put("Итност","Не итно");
        }


    lokacija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(PovozrasnoLice.this,LocationActivity.class);
                if (newLat==0.0&&newLon==0.0){
                    intent.putExtra("Lat", lat);
                    intent.putExtra("Lon", lon);
                }
                else {
                    intent.putExtra("Lat",newLat);
                    intent.putExtra("Lon",newLon);
                }
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Korisnici").orderByChild("Email").equalTo(emailKorisnik).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot datas : snapshot.getChildren()) {
                            fullName = datas.child("FullName").getValue().toString();
                           rejting = datas.child("Rejting").getValue().toString();
                            telefon = datas.child("Phone").getValue().toString();
                        }

                        map.put("ActivityType",aktivnost.getText().toString());
                        map.put("Opis",opis.getText().toString());
                        if(newLat == 0.0 && newLon == 0.0){
                            map.put("Lat", lat);
                            map.put("Lon", lon);
                        }else{
                            map.put("Lat", newLat);
                            map.put("Lon", newLon);
                        }
                        datumVreme=dateButton.getText()+" "+timeButton.getText();
                        map.put("Datum",datumVreme);
                        map.put("Ime",fullName);
                        map.put("RejtingNaKorisnik",rejting);
                        map.put("EmailVolonter","");
                        map.put("ImeVolonter","");
                        map.put("Status","Aktivna");
                        map.put("TelefonKorisnik",telefon);
                        map.put("TelefonVolonter","");
                        map.put("OpisZaKorisnik","");
                        map.put("OpisZaVolonter","");
                        map.put("RejtingZaKorisnik",0);
                        map.put("RejtingNaVolonter",0);
                        map.put("RejtingZaVolonter",0);
                        FirebaseDatabase.getInstance().getReference().child("Aktivnosti").push().updateChildren(map);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }


                });
        listN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PovozrasnoLice.this,ViewActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PovozrasnoLice.this,MainActivity.class));
            }
        });

        updateGPS();

    }


    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }
    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }


    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }else{
                    Toast.makeText(PovozrasnoLice.this, "This app requsts permission to be granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void updateGPS() {
      fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(PovozrasnoLice.this);
      if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
          fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
              @Override
              public void onSuccess(@NonNull Location location) {
                  lat = location.getLatitude();
                  lon = location.getLongitude();
              }
          });
      }
      else {
          if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
              requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_FINE_LOCATION);
          }
      }
    }




}