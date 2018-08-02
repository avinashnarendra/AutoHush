package com.autohush.www.dah;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.Duration;

/**
 * Created by PKBEST on 17-03-2016.
 */

public class TimePickerPopup extends Activity {

    String th, tm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.time_picker);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .7+10));

        final TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker1);

        ImageButton btn = (ImageButton) findViewById(R.id.select);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                th = timePicker.getCurrentHour().toString();
                tm = timePicker.getCurrentMinute().toString();
                resultIntent .putExtra("extra",th + " : " + tm);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
