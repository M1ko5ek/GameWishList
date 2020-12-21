package pl.pwr.s241936.gamewishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class AddGameActivity extends AppCompatActivity {

    private TextView info;
    private Button button;
    private EditText gameName;
    private String name;
    private TextView text0,text1,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,text12,text13,text14,text15,text16,text17,text18,text19,text20;
    private TextView[] textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        setupUIViews();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = AddGameActivity.this.gameName.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Document doc = Jsoup.connect("https://store.steampowered.com/search/?term=" + name + "&category1=998").get();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String result = doc.select("div[class=search_results_count]").text();
                                    if (result.equals("0 results match your search.")) {
                                        info.setText("can't find game with this title");
                                        for (int i = 0; i <= 20; i++) {
                                            textViews[i].setText("");
                                        }
                                    } else {
                                        info.setText("");
                                        Elements el2 = doc.select("#search_resultsRows");
                                        Elements elements = el2.select("a");

                                        int n = 0;
                                        for (Element element : elements) {
                                            if (n <= 20) {
                                                final String title = element.select("span.title").text();
                                                final String price = element.select("div[class=col search_price  responsive_secondrow]").text();
                                                final String discountedPrice = element.select("div[class=col search_price discounted responsive_secondrow]").text();
                                                textViews[n].setText(title + " " + price + " " + discountedPrice);
                                                textViews[n].setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        info.setText(title + " " + price + " " + discountedPrice);
                                                    }
                                                });
                                                n = n + 1;
                                            }
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
        });


    }

    private void setupUIViews(){
        info = (TextView)findViewById(R.id.textView);
        button = (Button)findViewById(R.id.button);
        gameName = (EditText)findViewById(R.id.editText);
        text0 = (TextView)findViewById(R.id.text0);
        text1 = (TextView)findViewById(R.id.text1);
        text2 = (TextView)findViewById(R.id.text2);
        text3 = (TextView)findViewById(R.id.text3);
        text4 = (TextView)findViewById(R.id.text4);
        text5 = (TextView)findViewById(R.id.text5);
        text6 = (TextView)findViewById(R.id.text6);
        text7 = (TextView)findViewById(R.id.text7);
        text8 = (TextView)findViewById(R.id.text8);
        text9 = (TextView)findViewById(R.id.text9);
        text10 = (TextView)findViewById(R.id.text10);
        text11 = (TextView)findViewById(R.id.text11);
        text12= (TextView)findViewById(R.id.text12);
        text13 = (TextView)findViewById(R.id.text13);
        text14 = (TextView)findViewById(R.id.text14);
        text15= (TextView)findViewById(R.id.text15);
        text16 = (TextView)findViewById(R.id.text16);
        text17 = (TextView)findViewById(R.id.text17);
        text18 = (TextView)findViewById(R.id.text18);
        text19= (TextView)findViewById(R.id.text19);
        text20 = (TextView)findViewById(R.id.text20);
        textViews = new TextView[]{text0, text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12 ,text13, text14, text15, text16, text17, text18, text19, text20};
    }

}