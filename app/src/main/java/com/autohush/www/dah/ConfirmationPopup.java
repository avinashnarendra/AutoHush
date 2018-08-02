package com.autohush.www.dah;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by PKBEST on 22-03-2016.
 */
public class ConfirmationPopup extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.confirm_layout);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .4));

        ImageButton ok = (ImageButton) findViewById(R.id.ok1);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent .putExtra("extra","YES");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
