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

public class UnionBankBranches extends ListActivity {
	String [] branches = {
			"118 Broad Str\n118/120 Broad Street",
			"Adeola-Odeku\nPlot 97, Ahmadu Bello Way, Victoria Island.",
			"Agbara\nIlaro Road Agara Ogun State",
			"Agege\n118 Ipaja Rd Shofunde Agege",
			"Ajose Adeogun\nPlot 275, Ajose Adeogun Street, Victoria Island.",
			"Amuwo-Odofin\nI Oju Road Maza Maza",
			"Apapa - Eleganza Plaza\n1 Commercial Road Apapa",
			"Awolowo Road\n77,Awolowo Road, Ikoyi",
			"Creek Apapa\n30/32, Creek Road, Terebor Roudabout, Apapa.",
			"Davies Street\n4 Davies Street Lagos",
			"Docemo Idumota\n50 Docemo Street Idumota",
			"Ebute Metta\n3/8 Muritala Mohammed Way Ebutte Meta",
			"Egbe Ikotun\n64 Ikotun- Isolo Road, Egbe Lagos",
			"Falomo\n1,Alfred Rewane Road, Falomo Round About, Ikoyi",
			"Federal Secretariat\nFederal Secretariat, Ikoyi.",
			"Foreshore Towers\n2a, Osborne Road,Ikoyi",
			"Idimu\n40 Ikotun Idimu Road Idimu",
			"Iganmu\nEric Moore Road Iganmu",
			"Ijora\n6 Causeway Road Ijora",
			"Ikorodu\n62 Lagos Road Ikorodu",
			"Ilupeju\n25 Industrial Avenue Ilupeju",
			"Isolo\nPlot 8, Block K Isolo Industrial Estate",
			"Lawanson\n123 Itire Rd, Lawanson",
			"Lewis Str.,\n61,Lewis Street",
			"Moloney\n6 Moloney Street Lagos",
			"Obalende\n13/15 Nojeem Maiyegun Street, Obalende.",
			"Obun Eko\n6/8 Obun Eko Str Idumota",
			"Oke-Arin\n32 John Street Oke Arin",
			"Orile Coker\nAwaye House Odunade B/Stop, Orile Coker",
			"Oyin Jola V/I\n1668b, Oyin Jolayemi Street, Victoria Island.",
			"Pencinema\n4 Iju Road Agege Pen Cinema",
			"Plaza Branch\nStallion Building, M36, Marina",
			"Sanusi Olusi\n70 Sanusi Olusi Street",
			"Somolu\n150 Ikorodu Road Onipanu",
			"Surulere\n3 Western Avenue Surulere",
			"Tin-Can Island\nTincan Island Port, Apapa",
			"Victoria Island\nPc33, Adeola Hopewell, Victoria Island.",
			"Wharf Rd., Apapa\n32 Wharf Road Apapa",
			"Yaba\n349 Herbet Macaulay Yaba",
			"Yinka Folawiyo, Apapa\n27 Yinka Folawiyo Avenue , Apapa"
				
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
	setContentView(R.layout.unionbankbranches);
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
			UnionBankBranches.this.adapter.getFilter().filter(cs);
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
