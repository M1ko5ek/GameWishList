package pl.pwr.s241936.gamewishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;

public class GameCardActivity extends AppCompatActivity {

    private TextView gameTitle, gamePrice;
    private Button goToStore, deleteButton;
    private FirebaseAuth mAuth;
    private ImageView imageView;
    private boolean foundImg;
    private boolean foundPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_card);
        setupUIViews();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            gameTitle.setText(bundle.getString("GameTitle"));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Document docImgSteam = Jsoup.connect("https://store.steampowered.com/search/?term=" + gameTitle.getText().toString() + "&category1=998").get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imgDownload(docImgSteam);
                        }
                    });
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Document docSteam = Jsoup.connect("https://store.steampowered.com/search/?term=" + gameTitle.getText().toString() + "&category1=998").get();

                    Float minPrice;
                    final String link;

                    minPrice = steamScrap(docSteam);

                    String name = nameOperations();
                    final Document docGog = Jsoup.connect("https://www.gog.com/game/" + name).get();
                    float gogPrice = gogScrap(docGog);
                    if(gogPrice < minPrice)
                    {
                        minPrice = gogPrice;
                        link = "https://www.gog.com/game/" + name;
                    }else {
                        link = steamLink(docSteam);
                    }

                    final Float finalMinPrice = minPrice;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DecimalFormat df = new DecimalFormat();
                            df.setMinimumFractionDigits(2);
                            gamePrice.setText(df.format(finalMinPrice) + " zł");
                            findViewById(R.id.progressBar2).setVisibility(View.GONE);

                            goToStore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Uri uriUrl = Uri.parse(link);
                                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                    startActivity(launchBrowser);
                                }
                            });
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getUid();
        final String path = "/users/" + userID + "/titles";

        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(path);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if(gameTitle.getText().equals(snapshot.getValue().toString())){
                                myRef.child(snapshot.getKey()).removeValue();
                                myRef.removeEventListener(this);
                                openGameListActivity();
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });
            }
        });

    }

    private void setupUIViews() {
        gameTitle = (TextView)findViewById(R.id.gameTitle);
        gamePrice = (TextView)findViewById(R.id.gamePrice);
        goToStore = (Button)findViewById(R.id.goToStore);
        deleteButton = (Button)findViewById(R.id.deleteButton);
        imageView = (ImageView)findViewById(R.id.imageView);

    }

    private String nameOperations(){
        String name = GameCardActivity.this.gameTitle.getText().toString();
        name = name.replace(" ","_");
        name = name.replace(":","");
        name = name.replace("™","");
        name = name.replace("®","");
        name = name.replace("'","");
        name = name.replace("__","_");
        return name;
    }

    private void imgDownload(Document docSteam){
        Elements el = docSteam.select("#search_results");
        Elements elements = el.select("a");
        foundImg = false;
        for (Element element : elements) {
            String title = element.select("span.title").text();
            if (gameTitle.getText().toString().toUpperCase().equals(title.toUpperCase()) && foundImg == false) {
                foundImg = true;
                Elements images = element.select("div[class=col search_capsule]");
                String bigImage = images.select("img").attr("srcset").toString();
                bigImage = bigImage.split(",",2)[1];
                bigImage = bigImage.substring(0, bigImage.length() - 2).trim();


                Picasso.get()
                        .load(bigImage)
                        .resize(693, 261)
                        .centerCrop()
                        .into(imageView);
            }
        }
    }

    private float steamScrap(Document doc){
        Elements el = doc.select("#search_results");
        Elements elements = el.select("a");
        foundPrice = false;
        for (Element element : elements) {
            String title = element.select("span.title").text();
            if (gameTitle.getText().toString().toUpperCase().equals(title.toUpperCase()) && foundPrice == false) {
                foundPrice = true;
                String price = element.select("div[class=col search_price  responsive_secondrow]").text();
                if(price == ""){
                    String discountedPrice = element.select("div[class=col search_price discounted responsive_secondrow ]").text();
                    discountedPrice = discountedPrice.split("zł",2)[1];
                    discountedPrice = discountedPrice.replace("zł", "");
                    discountedPrice = discountedPrice.replace(",", ".");

                    float discountedPriceFloat = Float.parseFloat(discountedPrice);
                    return discountedPriceFloat;
                }else{
                    if(price.equals("Free to Play") || price.equals("Free")){
                        price = "0.00";
                    }else{
                        price = price.replace("zł", "");
                        price = price.replace(",", ".");
                    }
                    float priceFloat = Float.parseFloat(price);
                    return priceFloat;
                }
            }
        }
        return Float.MAX_VALUE;
    }

    private String steamLink(Document doc)
    {
        Elements el = doc.select("#search_results");
        Elements elements = el.select("a");
        for (Element element : elements) {
            String title = element.select("span.title").text();
            if (gameTitle.getText().toString().toUpperCase().equals(title.toUpperCase())) {
                String link = element.select("a").attr("href");
                return link;
            }
        }
        return "";
    }

    private float gogScrap(Document doc){
        Elements el = doc.select("div[class=product-actions-price]");
        String price ="999.99";
        if(el.toString() != ""){
            price = el.select(".product-actions-price__final-amount").text();
            float priceFloat = Float.parseFloat(price);
            return priceFloat;
        }else{
            float priceFloat = (float) 999.99;
            return priceFloat;
        }
    }

    private void openGameListActivity(){
        Intent intent = new Intent(this, GameListActivity.class);
        startActivity(intent);
    }
}