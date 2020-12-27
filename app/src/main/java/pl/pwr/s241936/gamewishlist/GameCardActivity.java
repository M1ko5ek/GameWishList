package pl.pwr.s241936.gamewishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GameCardActivity extends AppCompatActivity {

    private TextView gameTitle, gamePrice;
    private Button goToStore;

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
                    final Document doc = Jsoup.connect("https://store.steampowered.com/search/?term=" + gameTitle.getText().toString() + "&category1=998").get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Elements el = doc.select("#search_results");
                            Elements elements = el.select("a");
                            for (Element element : elements) {
                                String title = element.select("span.title").text();
                                if (gameTitle.getText().toString().toUpperCase().equals(title.toUpperCase())) {
                                    String price = element.select("div[class=col search_price  responsive_secondrow]").text();
                                    if(price == ""){
                                        String discountedPrice = element.select("div[class=col search_price discounted responsive_secondrow ]").text();
                                        discountedPrice = discountedPrice.split("zł",2)[1];
                                        gamePrice.setText(discountedPrice);
                                    } else{
                                        gamePrice.setText(price);
                                    }
                                    final String link = element.select("a").attr("href");
                                    goToStore.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Uri uriUrl = Uri.parse(link);
                                            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                            startActivity(launchBrowser);
                                        }
                                    });
                                }
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setupUIViews() {
        gameTitle = (TextView)findViewById(R.id.gameTitle);
        gamePrice = (TextView)findViewById(R.id.gamePrice);
        goToStore = (Button)findViewById(R.id.goToStore);

    }
}