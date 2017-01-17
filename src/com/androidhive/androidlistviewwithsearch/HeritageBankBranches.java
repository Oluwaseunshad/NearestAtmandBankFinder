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

public class HeritageBankBranches extends ListActivity {
	String [] branches = {
			"Adeniyi Jones\n34/36 Adeniyi Jones Street, Ikeja, Lagos",
			"Adetokunbo Ademola, VI\nNo. 64 Adetokunbo Ademola Street,Victoria Island, Lagos",
			"Apapa\n36 Warehouse Road, Apapa, Lagos",
			"Ashabi Cole, Alausa\nNo. 7, Ashabi Cole Street, Alausa Central Business District (CBD), Ikeja, Lagos",
			"Awolowo Road, Ikoyi\nNo. 65 Awolowo road, Ikoyi, Lagos",
			"Head Office\nPlot 292B Ajose Adeogun Street, Victoria Island, Lagos",
			"Marina\nWesley House, 21/22 Marina, Lagos"				
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
	setContentView(R.layout.heritagebankbranches);
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
			HeritageBankBranches.this.adapter.getFilter().filter(cs);
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
