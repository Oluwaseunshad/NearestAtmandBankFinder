package com.androidhive.androidlistviewwithsearch;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class UnityBankBranches extends ListActivity {
	String [] branches = {
			"Abule Egba Branch\nNo. 513, Lagos/Abeokuta Express Way, Abule Egba Junction, Oko Oba, Abule Egba",
			"Alaba Int'L Branch\nNo. A65, Ojo-Igede Road, Alaba International Market, Alaba",
			"Allen Branch\nNo. 95, Allen Avenue, Ikeja",
			"Apapa Comm Ave Branch\nPlot 3, Block C, L.S.D.P.C. Industrial Scheme, Lagos-Badagry Expessway, Orile-Coker",
			"Aspamda Branch\nBTC 6 New Gate, Aspamda market, Trade Fair Complex, Lagos - Badagry Express way",
			"Awolowo Road Branch\nNo. 128  Awolowo Road, Ikoyi",
			"Balogun Branch\nNo. 32, Balogun Street, Lagos Island",
			"Bar Beach Branch\nNo. 1230E, Ahmadu Bello Way, Victoria Island",
			"Burma Road 1 Branch\nNo. 44 Burma Road, Apapa",
			"Creek Road Branch\nPlot 18, Creek Road, Apapa",
			"Eleganza Plaza Branch\nNo. 1, Wharf Road, Eleganza Plaza, Apapa",
			"Festac Branch\nHouse 26, Second Avenue, Festac Town, Amuwo Odofin",
			"Ibafon Branch\nBank House No.1 Capital Oil Close by WestMinister, Ibru Jetty Complex, Ibafon,Apapa",
			"Iddo Branch\nNo 8, Taylor Road Off G.Cappa Bustop, Iddo",
			"Idi Oro Branch\nNo. 94, Agege Motor Road, Idi Oro, Mushin",
			"Ikorodu Branch\nNo. 32, Lagos Road, Ikorodu",
			"Marina Branch\nNo. 2/4, Davies Street, Off Marina Road, Lagos Island",
			"Mile 12 Branch\nNo. 565, Ikorodu Road, Kosofe, Mile 12",
			"Mushin Branch\nNo. 87, Ladipo Street, Mushin",
			"Oba Akran Branch\nNo.42, Oba Akran Avenue, Ikeja",
			"Opebi Branch\nNo. 37, Opebi Road, Ikeja",
			"Oregun Road Branch\nNo. 100, Kudirat Abiola Way, Oregun Road, Ikeja",
			"Sanusi Fafunwa Road Branch\nPlot 1683, Sanusi Fafunwa Street, Victoria Island",
			"Surulere Branch\nNo. 53, Bode Thomas Street, Surulere",
			"Tincan Port, Branch\nBehind Tincan Port Admin Block, Tincan, Apapa",
			"Yaba Comm Avenue Branch\nNo. 32A, Commercial Avenue,Sabo Yaba"

				
	};
	
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
	setContentView(R.layout.unitybankbranches);
	setListAdapter(new ArrayAdapter<String>(this,
	android.R.layout.simple_list_item_1,
	branches));

	lv = (ListView) findViewById(R.id.list_view);
	inputSearch = (EditText) findViewById(R.id.inputSearch);

	// Adding items to listview
	adapter = new ArrayAdapter<String>(this, R.layout.list_item, branches);
	lv.setAdapter(adapter);
	/**
	 * Enabling Search Filter
	 * */
	inputSearch.addTextChangedListener(new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence cs, int arg1, int arg2,
				int arg3) {
			// When user changed the Text
			UnityBankBranches.this.adapter.getFilter().filter(cs);
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