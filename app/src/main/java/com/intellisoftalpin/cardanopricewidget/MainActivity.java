package com.intellisoftalpin.cardanopricewidget;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String geckoGetRequest = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=cardano&order=market_cap_desc&per_page=100&page=1&sparkline=false&locale=en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView web = findViewById(R.id.text_link);
        TextView price = findViewById(R.id.price_main);
        TextView change = findViewById(R.id.exchange_main);
        TextView market = findViewById(R.id.market_cap_main);
        TextView volume = findViewById(R.id.volume_main);

        web.setText(
                Html.fromHtml(
                        "<a href='https://blackrocket.space'>https://blackrocket.space</a>"));

        web.setMovementMethod(LinkMovementMethod.getInstance());

        RequestQueue queue = Volley.newRequestQueue(this);
        @SuppressLint("SetTextI18n") JsonArrayRequest jsObjRequest = new JsonArrayRequest(
                Request.Method.GET, geckoGetRequest, null,
                response -> {
                    try {

                        market.setText("4. Response arrived");

                        JSONObject object = response.getJSONObject(0);
                        price.setText("$" + object.getString("current_price"));
                        double exchange = object.getDouble("price_change_percentage_24h");
                        @SuppressLint("DefaultLocale") String num = String.format("%.02f", exchange);

                        String plus = "";
                        if (exchange >= 0) {
                            plus = "+";
                            change.setTextColor(Color.GREEN);
                        } else {
                            change.setTextColor(Color.RED);
                        }
                        change.setText(plus + num + "%");

                        double capExchange = object.getDouble("market_cap_change_24h");
                        int capExchangeInt = (int) capExchange;
                        @SuppressLint("DefaultLocale") String marketExhangeString = String.format("%,d", Long.parseLong(String.valueOf(capExchangeInt)));
                        volume.setText("$" + marketExhangeString);


                        @SuppressLint("DefaultLocale") String marketCap = String.format("%,d", Long.parseLong(object.getString("market_cap")));
                        market.setText("$" + marketCap);
                    } catch (JSONException e) {
                        Log.e("JSONException", "JSONException" + e);
                        e.printStackTrace();
                        String errorMessage = e.getMessage();
                        if (errorMessage == null) {
                            errorMessage = "Unknown error";
                        }
                        market.setText(errorMessage);
                    } catch (Exception e) {
                        String errorMessage = e.getMessage();
                        if (errorMessage == null) {
                            errorMessage = "Unknown error";
                        }
                        market.setText(errorMessage);
                    }
                }, error -> {
            Log.e("Volley Error", "Error in widget network request", error);
            market.setText("Error: " + error.toString());
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Cache-Control", "no-cache");
                return headers;
            }

        };
        queue.add(jsObjRequest);

    }
}