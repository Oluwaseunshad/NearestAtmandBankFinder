package com.androidhive.androidlistviewwithsearch;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class UnityBankAtms extends Activity {

	// List view
	private ListView lv;

	// Listview Adapter
	ArrayAdapter<String> adapter;

	// Search EditText
	EditText inputSearch;

	// ArrayList for Listview
	ArrayList<HashMap<String, String>> productList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unitybanksatms);

		// Listview Data
		String atms[] = { 
				
				"Apapa Comm Ave Branch\n 1, Commercial Anevue, Eleganza Plaza",
				"Abule Egba Branch\nAbule Egba Junction,Oko Oba",
				"Adeola Odeku Br\n19,Adeola Odeku Street",
				"Akin Adesola Branch\nNo 38, Adetokunbo Ademola St. Vi",
				"Alaba Branch\n9/11 Dubbil Avenue, Alaba Int'l Market",
				"Allen Avenue, Lagos\nNo 95,Allen Avenue",
				"Aspamda Branch\nAspamda Market, Lagos - Badagry Express Way, Lagos",
				"Awolowo Rd, Ikoyi\nAwolowo Road,Lagos",
				"Broad Str Branch, Lagos\n114 Broad Street, Lagos",
				"Burma Rd 1 Branch\n44,Burma Road",
				"Creek Rd, Apapa\n18,Creek Road",
				"Eleganza Branch\nEleganza Plaza,Wharf Road",
				"Festac\nNo 26,Second Avenue,Festac Town",
				"Head Office, Annex\nPlot 290a,Akin Olugbade Street",
				"Ibafon Branch\nBank House, No 1. Capital Oil Close, Westminster, Ibru Jetty Complex, Apapa, Lagos",
				"Iddo Branch\nNo 8, Taylor Road, Iddo, Lagos",
				"Idi-Oro Branch\nNo. 94, Agege Motor Road, Idi Oro, Mushin, Lagos",
				"Ikorodu Branch\nNo 32 Lagos Road,Ikorodu",
				"Lebanon Branch\n9,Lebanon Road",
				"Lekki Branch, Lagos\nNo 1, Prince Ibrahim Odofin Street, Lekki Expressway",
				"Marina Branch\n2/4 Davies Street Off Marina",
				"Mile 12, Lagos\n565,Ikorodu Road,Mile 12",
				"Mushin Branch\n87,Ladipo Street, Mushin",
				"Oba Akran, Lagos\n42,Oba Akran Avenue",
				"Opebi Branch\n37,Opebi Road",
				"Oregun Branch\n100,Kudirat Abiola Way,Ikeja",
				"Safe Court Estate\nSafe Court Estate, Lagos",
				"Sanusi Fafunwa Br, Lagos\nPlot 1683,Sanusi Fafunwa Street",
				"Surulere Branch\nNo. 53 Bode Thomas Street, Surulere, Lagos",
				"Tincan Port Branch\nBehind Tincan Port,Admin Block",
				"Yaba Branch\n32a,Commercial Avenue,Sabo Yaba"
						
		};

		lv = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.unitySearch);

		// Adding items to listview
		adapter = new ArrayAdapter<String>(this, R.layout.list_item, atms);
		lv.setAdapter(adapter);

		/**
		 * Enabling Search Filter
		 * */
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				UnityBankAtms.this.adapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});

	}

}
