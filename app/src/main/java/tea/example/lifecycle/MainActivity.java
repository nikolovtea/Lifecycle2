package tea.example.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login= findViewById(R.id.button1);
        Button signup=findViewById(R.id.button2);
    }


    public void login(View view) {
        intent=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void signup(View view) {
        intent=new Intent(getApplicationContext(),SignupActivity.class);
        startActivity(intent);
        finish();
    }
}
