package com.autohush.www.dah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PKBEST on 17-03-2016.
 */
public class MainActivityFragment1 extends Fragment implements AdapterView.OnItemClickListener,CompoundButton.OnCheckedChangeListener {

    Boolean mySwitch_on;
    public MainActivityFragment1() {
    }

    private List<ListItems> LocationList;
    private ArrayAdapter<ListItems> adapter;
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // do something when checked is selected
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("com.example.xyz", Context.MODE_PRIVATE).edit();
            editor.putBoolean("NameOfThingToSave", true);
            editor.apply();
            startservice();
        } else {
            //do something when unchecked
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("com.example.xyz", Context.MODE_PRIVATE).edit();
            editor.putBoolean("NameOfThingToSave", false);
            editor.apply();
            stopservice();
        }

    }
    public void startservice(){
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(getActivity(), "Turn on your Location", Toast.LENGTH_SHORT).show();
            Switch mySwitch = (Switch) getActivity().findViewById(R.id.onoff);
            mySwitch.setChecked(false);
        }
        else {
            Toast.makeText(getActivity(), "AutoHush is On", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MyService.class);
            getActivity().startService(intent);
        }
    }
    public void stopservice(){
        Intent intent=new Intent(getActivity(),MyService.class);
        getActivity().stopService(intent);
    }

    private class MyAdapter extends ArrayAdapter<ListItems> {

        public MyAdapter(Context context, List<ListItems> values) {

            super(context, R.layout.list_item, values);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            LayoutInflater inflater = LayoutInflater.from(getContext());

            if( v == null){
                v = inflater.inflate(R.layout.list_item, parent, false);
            }

            ListItems currLoc = LocationList.get(position);

            TextView theTextView = (TextView) v.findViewById(R.id.list_item_textview);
            theTextView.setText(currLoc.getLocation());

            TextView theTextView2 = (TextView) v.findViewById(R.id.list_item_textviewP);
            theTextView2.setText(currLoc.getProfile());

            ImageView theImageView = (ImageView) v.findViewById(R.id.imageview1);

              theImageView.setImageResource(R.drawable.bullet);




            return v;
        }

    }

    private void populateLocationList() {
        Context ctx = getActivity();
        DBoperations db = new DBoperations(ctx);
        Cursor cr = db.getInfo(db);
        boolean empty = cr.moveToFirst();
        String Name;
        String Profile;
        if(empty == false)
            Toast.makeText(getActivity(), "Enter A New Location", Toast.LENGTH_LONG).show();
        else {
            do {
                Name = cr.getString(1);
                Profile = cr.getString(4);
                LocationList.add((new ListItems(Name,Profile)));
            } while (cr.moveToNext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_view, container, false);

        final EditText txtinput;

        LocationList = new ArrayList<ListItems>();
        list = (ListView) rootView.findViewById(R.id.list_view);
        adapter = new MyAdapter(getActivity(),LocationList);

        list.setAdapter(adapter);

        txtinput = (EditText) rootView.findViewById(R.id.txtinput);

        Switch mySwitch=(Switch) rootView.findViewById(R.id.onoff);
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("com.example.xyz", Context.MODE_PRIVATE);
        mySwitch_on = sharedPrefs.getBoolean("NameOfThingToSave", false);

        if(mySwitch_on)
            mySwitch.setChecked(true);
        else
            mySwitch.setChecked(false);
        mySwitch.setOnCheckedChangeListener(MainActivityFragment1.this);

        ImageButton btnadd = (ImageButton) rootView.findViewById(R.id.imageButton);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = txtinput.getText().toString();
                if (newItem.equals(""))
                    Toast.makeText(getActivity(), "Enter Location Name", Toast.LENGTH_LONG).show();
                else {

                    //Hide Keyboard
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    Context ctx = getActivity();
                    DBoperations db = new DBoperations(ctx);
                    LocationList.add(new ListItems(newItem,"Setup"));
                    adapter.notifyDataSetChanged();
                    txtinput.setText("");
                    db.putInfo(db, newItem, null, null, "Setup");
                }
            }
        });

        populateLocationList();
        list.setOnItemClickListener(this);
        setRetainInstance(true);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getActivity(),LocationInfo.class);
        Context ctx = getActivity();
        DBoperations db = new DBoperations(ctx);
        Cursor cr = db.getInfo(db);
        cr.moveToFirst();
        long count = id;
        while( count > 0){
            cr.moveToNext();
            count --;
        }
        String ID = Integer.toString(cr.getInt(0));
        String Name = cr.getString(1);
        intent.putExtra("extra",ID + " " + Name);
        startActivity(intent);
    }
}
