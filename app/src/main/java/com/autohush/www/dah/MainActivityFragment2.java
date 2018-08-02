package com.autohush.www.dah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by PKBEST on 17-03-2016.
 */
public class MainActivityFragment2 extends Fragment implements AdapterView.OnItemClickListener {

    private List<TimeList> TDList;
    private ArrayAdapter<TimeList> adapter;
    private ListView list;
    private EditText txtinput1;
    private EditText txtinput2;
    Spinner dropdown;
    ArrayAdapter<CharSequence> Aadapter;
    String Pro;
    String YES_NO = "NO";
    long ListID = 0;
    int ListPosition = 0;

    private static final int REQUEST_GET_DATA_FROM_SOME_ACTIVITY1 = 0;
    private static final int REQUEST_GET_DATA_FROM_SOME_ACTIVITY2 = 1;
    private static final int YES_OR_NO = 2;

    public MainActivityFragment2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private class MyAdapter extends ArrayAdapter<TimeList> {

        public MyAdapter(Context context, List<TimeList> values) {

            super(context, R.layout.time_list_item, values);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            LayoutInflater inflater = LayoutInflater.from(getContext());

            if( v == null){
                v = inflater.inflate(R.layout.time_list_item, parent, false);
            }

            TimeList currLoc = TDList.get(position);

            TextView theTextView = (TextView) v.findViewById(R.id.time_list_item_textview1);
            theTextView.setText(currLoc.getTime()+ " - " +currLoc.getDuration());

            TextView theTextView2 = (TextView) v.findViewById(R.id.time_list_item_textviewP);
            theTextView2.setText(currLoc.getProfile());

            ImageView theImageView = (ImageView) v.findViewById(R.id.imageview2);
            theImageView.setImageResource(R.drawable.bullet);

            ImageView theImageView2 = (ImageView) v.findViewById(R.id.imageview3);
            theImageView2.setImageResource(R.drawable.sub);

            return v;
        }

    }

    private void populateTimeList() {
        Context ctx = getActivity();
        DBoperations db = new DBoperations(ctx);
        Cursor cr = db.gettimeInfo(db);
        boolean empty =  cr.moveToFirst();
        String StartH;
        String StartM;
        String EndH;
        String EndM;
        String Profile;
        if(empty == false)
            return;
        else {
            do {
                StartH = Integer.toString(cr.getInt(1));
                StartM = Integer.toString(cr.getInt(2));
                EndH = Integer.toString(cr.getInt(3));
                EndM = Integer.toString(cr.getInt(4));
                Profile = cr.getString(5);
                TDList.add((new TimeList(StartH + " : " + StartM, EndH + " : " + EndM,Profile)));
            } while (cr.moveToNext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.time_list_view, container, false);

        TDList = new ArrayList<TimeList>();
        list = (ListView) rootView.findViewById(R.id.time_list_view);
        adapter = new MyAdapter(getActivity(),TDList);

        list.setAdapter(adapter);

        txtinput1 = (EditText) rootView.findViewById(R.id.start_time);
        txtinput2 = (EditText) rootView.findViewById(R.id.end_time);

        txtinput1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TimePickerPopup.class);
                startActivityForResult(i, 1);
            }
        });

        dropdown = (Spinner)rootView.findViewById(R.id.spinnert);
        Aadapter = ArrayAdapter.createFromResource(getActivity(),R.array.profile,android.R.layout.simple_spinner_item);
        Aadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(Aadapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Pro = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageButton ib = (ImageButton) rootView.findViewById(R.id.imageButton1);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st = txtinput1.getText().toString();
                String et = txtinput2.getText().toString();
                if (st.equals(""))
                    Toast.makeText(getActivity(), "Enter Start Time", Toast.LENGTH_LONG).show();
                else if (et.equals(""))
                    Toast.makeText(getActivity(), "Enter End Time", Toast.LENGTH_LONG).show();
                else
                {
                    Context ctx = getActivity();
                    DBoperations db = new DBoperations(ctx);
                    TDList.add(new TimeList(st, et, Pro));
                    adapter.notifyDataSetChanged();
                    txtinput1.setText("");
                    txtinput2.setText("");
                    StringTokenizer str = new StringTokenizer(st+":"+et,": ");

                    db.puttimeInfo(db, Integer.parseInt(str.nextToken()), Integer.parseInt(str.nextToken()), Integer.parseInt(str.nextToken()), Integer.parseInt(str.nextToken()),Pro);
                }
            }
        });

        txtinput1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TimePickerPopup.class);
                startActivityForResult(intent, REQUEST_GET_DATA_FROM_SOME_ACTIVITY1);
            }
        });

        txtinput2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimePickerPopup.class);
                startActivityForResult(intent, REQUEST_GET_DATA_FROM_SOME_ACTIVITY2);
            }
        });

        populateTimeList();
        list.setOnItemClickListener(this);
        setRetainInstance(true);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ListID = id;
        ListPosition = position;
        Intent intent = new Intent(getActivity(), ConfirmationPopup.class);
        startActivityForResult(intent, YES_OR_NO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GET_DATA_FROM_SOME_ACTIVITY1 && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            String str = extras.getString("extra");
            txtinput1.setText(str);
        }
        else if(requestCode == REQUEST_GET_DATA_FROM_SOME_ACTIVITY2 && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            String str = extras.getString("extra");
            txtinput2.setText(str);
        }
        else if(requestCode == YES_OR_NO && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            YES_NO = extras.getString("extra");
            delete();
        }
    }

    public void delete(){
        if(YES_NO.equals("YES")) {

            TimeList item = TDList.get(ListPosition);
            TDList.remove(item);
            adapter = new MyAdapter(getActivity(), TDList);
            list.setAdapter(adapter);

            Context ctx = getActivity();
            DBoperations db = new DBoperations(ctx);
            Cursor cr = db.gettimeInfo(db);
            cr.moveToFirst();
            long count = ListID;
            while (count > 0) {
                cr.moveToNext();
                count--;
            }
            String ID = Integer.toString(cr.getInt(0));
            db.deltimeInfo(db, ID);
            YES_NO = "NO";
        }
    }
}