package com.autohush.www.dah;

/**
 * Created by Naveen on 21-03-2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Naveen on 19-03-2016.
 */
public class RelatedSearches extends Activity {
    public final static String key = "abc";
    public final static String key1 = "abcd";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.relatedsearches);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .6));
        Intent intent=getIntent();
        String[] searches=intent.getStringArrayExtra(key);
        populatesearchlist(searches);
        registerClickCallback();

    }

    //    public void registerClickCallback()
//    {
//        ListView list = (ListView) findViewById(R.id.relatedlist);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
//                TextView textView = (TextView) viewClicked;
//                String message = "You clicked # " + position + ", which is string: " + textView.getText().toString();
////                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//                Toast.makeText(MapsActivity.this, ""+message, Toast.LENGTH_SHORT).show();
//            }
//        });
//
////        Intent intent = new Intent(this, RelatedSearches.class);
////        intent.putExtra("position", pos);
////        this.startActivity(intent);
//
//    }
//
//
//    public void populatesearchlist(String mystringarry[])
//    {
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.da_item,mystringarry);
//
//        ListView listView=(ListView) findViewById(R.id.relatedlist);
//        listView.setAdapter(adapter);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
//            Bundle extras = data.getExtras();
//            // String[] mystring = extras.getStringArray("relatedsearches");
//            // txtinput1.setText(str);
//            Intent intent = getIntent();
//            String[] mystringArray = getIntent().getStringArrayExtra("relatedsearches");
//            Toast.makeText(this, "" + mystringArray, Toast.LENGTH_SHORT).show();
//
//               populatesearchlist(mystringArray);
//
//
//        }
//    }

    public void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.relatedlist);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You clicked # " + position + ", which is string: " + textView.getText().toString();
//                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Toast.makeText(RelatedSearches.this, "" + message, Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.putExtra(key1, position);
                setResult(RESULT_OK, intent1);
                finish();
            }
        });

//        Intent intent = new Intent(this, RelatedSearches.class);
//        intent.putExtra("position", pos);
//        this.startActivity(intent);

    }


    public void populatesearchlist(String mystringarry[]) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.da_item, mystringarry);

        ListView listView = (ListView) findViewById(R.id.relatedlist);
        listView.setAdapter(adapter);
    }
}


