package com.intellisoftalpin.cardanopricewidget;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Objects.requireNonNull(getSupportActionBar()).setIcon(R.drawable.blr_logo_best);

        TextView web = (TextView) findViewById(R.id.text_link);

        web.setText(
                Html.fromHtml(
                        "<a href='https://blackrocket.space'>https://blackrocket.space</a>"));

        web.setMovementMethod(LinkMovementMethod.getInstance());

    }
}