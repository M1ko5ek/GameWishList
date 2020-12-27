package pl.pwr.s241936.gamewishlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private TextView info;
    private TextView register;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUIViews();

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    String user_email = MainActivity.this.email.getText().toString().trim();
                    String user_password = MainActivity.this.password.getText().toString().trim();
                    loginUser(user_email, user_password);

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterActiviy();
            }
        });
    }

    private void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            openGameListActivity();
                        } else {
                            Toast.makeText(MainActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setupUIViews(){
        email = (EditText)findViewById(R.id.editTextTextEmailAddress);
        password = (EditText)findViewById(R.id.editTextTextPassword);
        login = (Button)findViewById(R.id.buttonLogin);
        info = (TextView)findViewById(R.id.textVievLoginInfo);
        register = (TextView)findViewById(R.id.textViewRegister);
    }


    private void openRegisterActiviy(){
        info.setText("");
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void openGameListActivity(){
        info.setText("");
        Intent intent = new Intent(this, GameListActivity.class);
        startActivity(intent);
    }

    private Boolean validate (){
        Boolean result = false;
        String email = MainActivity.this.email.getText().toString();
        String password = MainActivity.this.password.getText().toString();
        if( email.isEmpty() || password.isEmpty()){
            {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
            }
        }else{
            result = true;
        }
        return result;
    }

    @Override
    public void onBackPressed() {

    }

}