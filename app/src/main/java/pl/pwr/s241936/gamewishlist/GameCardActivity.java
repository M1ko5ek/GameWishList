package pl.pwr.s241936.gamewishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
                    final Document docSteam = Jsoup.connect("https://store.steampowered.com/search/?term=" + gameTitle.getText().toString() + "&category1=998").get();

                    Float minPrice;
                    minPrice = steamScrap(docSteam);

                    String name = nameOperations();
                    final Document docGog = Jsoup.connect("https://www.gog.com/game/" + name).get();
                    float gogPrice = gogScrap(docGog);
                    if(gogPrice < minPrice)
                    {
                        minPrice = gogPrice;
                    }

                    final Float finalMinPrice = minPrice;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gamePrice.setText(finalMinPrice.toString() + " zł");
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

    private float steamScrap(Document doc){
        Elements el = doc.select("#search_results");
        Elements elements = el.select("a");
        for (Element element : elements) {
            String title = element.select("span.title").text();
            if (gameTitle.getText().toString().toUpperCase().equals(title.toUpperCase())) {
                String price = element.select("div[class=col search_price  responsive_secondrow]").text();
                if(price == ""){
                    String discountedPrice = element.select("div[class=col search_price discounted responsive_secondrow ]").text();
                    discountedPrice = discountedPrice.split("zł",2)[1];
                    discountedPrice = discountedPrice.replace("zł", "");
                    discountedPrice = discountedPrice.replace(",", ".");
                    float discountedPriceFloat = Float.parseFloat(discountedPrice);
                    return discountedPriceFloat;
                } else{
                    price = price.replace("zł", "");
                    price = price.replace(",", ".");
                    float priceFloat = Float.parseFloat(price);
                    return priceFloat;
                }
            }
        }
        return Float.MAX_VALUE;
    }

    private float gogScrap(Document doc){
        Elements el = doc.select("div[class=product-actions-price]");
        String price ="99999";
        if(el.toString() != ""){
            price = el.select(".product-actions-price__final-amount").text();
            float priceFloat = Float.parseFloat(price);
            return priceFloat;
        }else{
            float priceFloat = Float.MAX_VALUE;
            return priceFloat;
        }
    }
}