package pl.pwr.s241936.gamewishlist;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameListActivity extends AppCompatActivity {

    private Button addGame;
    private TextView gameList;
    private Button logut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        setupUIViews();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("games");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                gameList.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


        addGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddGameActivity();
            }
        });

        logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                openLoginActiviy();
            }
        });
    }

    private void setupUIViews(){
        addGame = (Button)findViewById(R.id.addGame);
        gameList = (TextView)findViewById(R.id.textViewGameList);
        logut = (Button)findViewById(R.id.logout);
    }

    private void openAddGameActivity(){
        Intent intent = new Intent(this, AddGameActivity.class);
        startActivity(intent);
    }

    private void openLoginActiviy(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}


