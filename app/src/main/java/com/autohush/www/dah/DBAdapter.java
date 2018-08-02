package com.autohush.www.dah;

import android.provider.BaseColumns;

/**
 * Created by PKBEST on 17-03-2016.
 */
public class DBAdapter{

	public DBAdapter(){

	}

public static abstract class DBtimeinfo implements BaseColumns {
		public static final String ID = "id";
		public static final String StartTimeHour = "sth";
		public static final String StartTimeMin = "stm";
		public static final String EndTimeHour = "eth";
		public static final String EndTimeMin = "etm";
		public static final String Profile = "pro";
		public static final String Tablename = "Time_Based";
	}
	public static abstract class DBinfo implements BaseColumns {
		public static final String ID = "id";
		public static final String Name = "name";
		public static final String Latitude = "lat";
		public static final String Longitude = "lon";
		public static final String Profile = "pro";
		public static final String DBname = "AutoHush_DB";
		public static final String Tablename = "Location_Based";
	}

}
