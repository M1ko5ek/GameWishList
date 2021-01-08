package pl.pwr.s241936.gamewishlist;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GameListActivity extends AppCompatActivity {

    private Button addGame;
    private TextView gameList;
    private Button logut;
    private FirebaseAuth mAuth;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        setupUIViews();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getUid();
        String path = "/users/" + userID + "/titles";

        DatabaseReference myRef = database.getReference(path);

        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item,list);
        listView.setAdapter(adapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    list.add(snapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                if(list.isEmpty() == true)
                {
                    Toast.makeText(GameListActivity.this, "Your wish list is empty", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });




       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent = new Intent(GameListActivity.this, GameCardActivity.class);
               intent.putExtra("GameTitle", listView.getItemAtPosition(i).toString());
               startActivity(intent);
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
        listView = (ListView)findViewById(R.id.listView);
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


