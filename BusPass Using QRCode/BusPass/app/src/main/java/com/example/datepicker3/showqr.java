package com.example.datepicker3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.RemoteViews;

import java.io.File;

/**
 * Implementation of App Widget functionality.
 */
public class showqr extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.showqr);
        //ImageView iview=new ImageView(context.getPackageName(),R.layout)
        //views.setImageViewUri(R.id.imgpicker, Uri.parse("/storage/emulated/0/QRCode/Latest.jpg"));

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/QRCode/Latest.jpg");

        Bitmap myBitmap = BitmapFactory.decodeFile(myDir.getAbsolutePath());

        views.setImageViewBitmap(R.id.imgpicker,myBitmap);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.showqr);
            //ImageView iview=new ImageView(context.getPackageName(),R.layout)
            //views.setImageViewUri(R.id.imgpicker, Uri.parse("/storage/emulated/0/QRCode/Latest.jpg"));

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/QRCode/Latest.jpg");

            Bitmap myBitmap = BitmapFactory.decodeFile(myDir.getAbsolutePath());

            views.setImageViewBitmap(R.id.imgpicker,myBitmap);
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
        Intent intent = new Intent(context, showdetails.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);  // Identifies the particular widget...
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
// Make the pending intent unique...
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.showqr);
        views.setOnClickPendingIntent(R.id.imgpicker, pendIntent);
        appWidgetManager.updateAppWidget(appWidgetIds,views);

    }



    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

