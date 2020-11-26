package pl.pwr.s241936.gamewishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userEmail;
    private EditText userPassword1;
    private EditText userPassword2;
    private Button register;
    private TextView info;
    private TextView login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        SetupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    // send to firebase
                    info.setText("Registered");
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActiviy();
            }
        });
    }

    private void SetupUIViews(){
        userName = (EditText)findViewById(R.id.editTextUserName);
        userEmail = (EditText)findViewById(R.id.editTextUserEmail);
        userPassword1 = (EditText)findViewById(R.id.editTextUserPassword);
        userPassword2 = (EditText)findViewById(R.id.editTextUserPasswordRepeat);
        register = (Button)findViewById(R.id.buttonRegister);
        info = (TextView)findViewById(R.id.textVievRegisterInfo);
        login = (TextView)findViewById(R.id.textViewLogin);
    }

    private Boolean validate (){
        Boolean result = false;
        String name = RegistrationActivity.this.userName.getText().toString();
        String email = RegistrationActivity.this.userEmail.getText().toString();
        String password1 = RegistrationActivity.this.userPassword1.getText().toString();
        String password2 = RegistrationActivity.this.userPassword2.getText().toString();
        if(name.isEmpty() || email.isEmpty() || password1.isEmpty() || password2.isEmpty()){
        {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }
        }else if (!password1.equals(password2)){
            Toast.makeText(this, "Please repeat the password correctly", Toast.LENGTH_SHORT).show();
            info.setText("haslo");
        } else{
            result = true;
        }
        return result;
    }

    private void openLoginActiviy(){
        info.setText("");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}