package tea.example.lifecycle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;


    Button buttonlogin;
    Intent intent;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intent=getIntent();

        email=findViewById(R.id.emaillogin);
        password=findViewById(R.id.passwordlogin);
        buttonlogin = findViewById(R.id.kliklogin);


        auth=FirebaseAuth.getInstance();

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                auth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user=FirebaseAuth.getInstance().getCurrentUser();
                            if(user.getDisplayName().equals("PovozrasnoLice")){
                                startActivity(new Intent(LoginActivity.this,PovozrasnoLice.class));
                            }
                            else if(user.getDisplayName().equals("Volonter")){
                                startActivity(new Intent(LoginActivity.this,Volonter.class));
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }


}