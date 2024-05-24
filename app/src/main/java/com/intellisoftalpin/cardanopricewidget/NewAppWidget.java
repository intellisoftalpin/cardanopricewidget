package com.intellisoftalpin.cardanopricewidget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewAppWidget extends AppWidgetProvider {
    public static final String geckoGetRequest = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=cardano&order=market_cap_desc&per_page=100&page=1&sparkline=false&locale=en";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        widgetView.setTextViewText(R.id.market_cap, "1. Request created");

        RequestQueue queue = Volley.newRequestQueue(context);

        widgetView.setTextViewText(R.id.market_cap, "2. RequestQueue created");
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(
                Request.Method.GET, geckoGetRequest, null,
                response -> {
                    try {

                        widgetView.setTextViewText(R.id.market_cap, "4. Response arrived");

                        JSONObject object = response.getJSONObject(0);
                        widgetView.setTextViewText(R.id.price, "$"+object.getString("current_price"));
                        double exchange = object.getDouble("price_change_percentage_24h");
                        @SuppressLint("DefaultLocale") String num = String.format("%.02f", exchange);

                        String plus = "";
                        if (exchange >= 0) {
                            plus = "+";
                            widgetView.setInt(R.id.exchange, "setTextColor", Color.GREEN);
                        } else {
                            widgetView.setInt(R.id.exchange, "setTextColor", Color.RED);
                        }
                        widgetView.setTextViewText(R.id.exchange, plus + num + "%");

                        @SuppressLint("DefaultLocale") String marketCap = String.format("%,d", Long.parseLong(object.getString("market_cap")));
                        widgetView.setTextViewText(R.id.market_cap, "$"+marketCap);
                    } catch (JSONException e) {
                        Log.e("JSONException", "JSONException" + e);
                        e.printStackTrace();
                        String errorMessage = e.getMessage();
                        if (errorMessage == null) {
                            errorMessage = "Unknown error"; // More user-friendly message
                        }
                        widgetView.setTextViewText(R.id.market_cap, errorMessage);
                    } catch (Exception e) {
                        String errorMessage = e.getMessage();
                        if (errorMessage == null) {
                            errorMessage = "Unknown error"; // More user-friendly message
                        }
                        widgetView.setTextViewText(R.id.market_cap, errorMessage);
                    }
                    appWidgetManager.updateAppWidget(appWidgetId, widgetView);
                }, error -> {
            Log.e("Volley Error", "Error in widget network request", error);
            widgetView.setTextViewText(R.id.market_cap, "Error: " + error.toString());
            appWidgetManager.updateAppWidget(appWidgetId, widgetView);
        }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Cache-Control", "no-cache"); // This ensures the request does not use the cache
                    return headers;
                }

        };
        queue.add(jsObjRequest);
        widgetView.setTextViewText(R.id.market_cap, "3. jsObjRequest added to queue");


//        // open main activity from button
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//        views1.setOnClickPendingIntent(R.id.openMainButton, pendingIntent);
//
        // update widget on btn update
//        Intent intentUpdate = new Intent(context, NewAppWidget.class);
//        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        int[] idArray = new int[]{appWidgetId};
//        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
//        PendingIntent pendingUpdate = PendingIntent.getBroadcast(
//                context, appWidgetId, intentUpdate,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        views1.setOnClickPendingIntent(R.id.button_update, pendingUpdate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, widgetView);
    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled

    }
}