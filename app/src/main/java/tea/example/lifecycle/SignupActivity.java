package tea.example.lifecycle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class SignupActivity extends AppCompatActivity {
    private EditText name;
    private EditText lastName;
    private EditText phoneNumber;
    private EditText email;
    private EditText password;
    Intent intent;
    public RadioGroup radioGrupa;
    public RadioButton radioButton;
    Button button;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        intent = getIntent();
        button = findViewById(R.id.klik);

        name = findViewById(R.id.ime);
        lastName = findViewById(R.id.prezime);
        phoneNumber = findViewById(R.id.tel);
        email = findViewById(R.id.email);
        password = findViewById(R.id.epassword);
        radioGrupa=findViewById(R.id.radioGrupa);

        auth = FirebaseAuth.getInstance();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_name = name.getText().toString();
                String txt_lastName = lastName.getText().toString();
                String txt_phone = phoneNumber.getText().toString();
                auth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            HashMap<String,Object> map=new HashMap<>();
                            map.put("Name",txt_name);
                            map.put("LastName",txt_lastName);
                            map.put("Email",txt_email);
                            map.put("Password",txt_password);
                            map.put("Phone",txt_phone);
                            map.put("BrojNaRejting",0);
                            map.put("Rejting",0);
                            map.put("SumaRejting",0);
                            String fullName = txt_name + " " + txt_lastName;
                            map.put("FullName",fullName);
                            int selectedId=radioGrupa.getCheckedRadioButtonId();
                            radioButton=(RadioButton)findViewById(selectedId);
                            if(radioButton.getId()==R.id.povozrasnoLice){
                                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                String uid=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                                FirebaseDatabase.getInstance().getReference().child("Korisnici").child(uid).updateChildren(map);
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("PovozrasnoLice").build();
                                user.updateProfile(profileUpdates);
                                Toast.makeText(SignupActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this,PovozrasnoLice.class));

                            }
                            else if (radioButton.getId()==R.id.volonter){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                                FirebaseDatabase.getInstance().getReference().child("Korisnici").child(uid).updateChildren(map);
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("Volonter").build();
                                user.updateProfile(profileUpdates);
                                Toast.makeText(SignupActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this,Volonter.class));
                            }
                            else {
                                Toast.makeText(SignupActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

            }

        });



    }



}
