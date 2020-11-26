package pl.pwr.s241936.gamewishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password1;
    private EditText password2;
    private Button register;
    private TextView info;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = (EditText)findViewById(R.id.editTextUserName);
        email = (EditText)findViewById(R.id.editTextUserPassword);
        password1 = (EditText)findViewById(R.id.editTextTextPassword);
        password2 = (EditText)findViewById(R.id.editTextUserPasswordRepeat);
        register = (Button)findViewById(R.id.buttonRegister);
        info = (TextView)findViewById(R.id.textVievRegisterInfo);
        login = (TextView)findViewById(R.id.textViewLogin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActiviy();
            }
        });
    }

    private void validate (){
        info.setText("Registered");
    }

    private void openLoginActiviy(){
        info.setText("");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}