package pl.pwr.s241936.gamewishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GameCardActivity extends AppCompatActivity {

    private TextView gameTitle, gamePrice, link;

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
                                    String discountedPrice = element.select("div[class=col search_price discounted responsive_secondrow]").text();
                                    gamePrice.setText(price + " " + discountedPrice);
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
        link = (TextView)findViewById(R.id.link);
    }
}