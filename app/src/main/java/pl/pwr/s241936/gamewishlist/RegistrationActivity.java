package pl.pwr.s241936.gamewishlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userEmail;
    private EditText userPassword1;
    private EditText userPassword2;
    private Button register;
    private TextView info;
    private TextView login;
    private FirebaseAuth mAuth;
    private String user_email;
    private String user_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        SetupUIViews();

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    // send to firebase
                    user_email = RegistrationActivity.this.userEmail.getText().toString().trim();
                    user_password = RegistrationActivity.this.userPassword1.getText().toString().trim();

                    mAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(RegistrationActivity.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Registration succesful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Registration failed ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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