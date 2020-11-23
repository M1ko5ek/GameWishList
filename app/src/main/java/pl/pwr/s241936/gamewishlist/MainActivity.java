package pl.pwr.s241936.gamewishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private TextView info;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText)findViewById(R.id.editTextTextEmailAddress);
        password = (EditText)findViewById(R.id.editTextTextPassword);
        login = (Button)findViewById(R.id.buttonLogin);
        info = (TextView)findViewById(R.id.textVievLoginInfo);
        register = (TextView)findViewById(R.id.textViewRegister);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(email.getText().toString(), password.getText().toString());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterActiviy();
            }
        });
    }

    private void validate (String userName, String userPassword){
        if((userName.equals("admin")) && (userPassword.equals("1234"))){
            info.setText("");
            Intent intent = new Intent(this, GameListActivity.class);
            startActivity(intent);

        }else{
            info.setText("Wrong email or password");
        }
    }

    private void openRegisterActiviy(){
        info.setText("");
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

}