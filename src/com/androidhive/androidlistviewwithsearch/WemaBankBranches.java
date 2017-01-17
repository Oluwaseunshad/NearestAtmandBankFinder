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

public class WemaBankBranches extends ListActivity {
	String [] branches = {
			
			"Abule Egba Branch\n15 Lagos/Abeokuta Express Way , Abule Egba",
			"Adeniji Adele Branch\n184,Adeniji Adele Road,Lagos-Island",
			"Agege Branch\n185 Old, Abeokuta Motor Rd , Agege",
			"Ajao Estate Branch\n2 Rasmon Close Off Osolo Way, Ajao Estate",
			"Allen Branch\n33 Allen Avenue Ikeja",
			"Aspamda Branch\nBlk 9, Zone D Aspamda Market. Int'l Trade Fair Complex, Amuwo Odofin, Lagos",
			"Awolowo Rd. Branch\n35 Awolowo Rd, Ikoyi, Lagos",
			"Broad Street 1 Branch\n41/45 Broad Street Branch, Lagos Island",
			"Broad Street 2 Branch\n12, Idumagbo Ave, Lagos Isalnd, Lagos",
			"Commercial Rd Branch\n2, Commercila Rd, Apapa",
			"Dopemu Branch\n60,Lagos/Abk Express Road Dopemu",
			"Ebute Metta Branch\n52/54 Muritala Mohammed Way Ebute Meta",
			"Egbeda Branch\n117 Idimu Rd Egbeda Orelope Bus Stop, Lagos State",
			"Idowu Taylor Branch\n8 Idowu Taylor Street, Victoria Island, Lagos",
			"Ijora Branch\nIjora Fishries Terminal Behinde Nepa Workshop, Ijora, Lagos",
			"Ikeja Branch\n24,Oba Akran Avenue Ikeja",
			"Iponri Branch\nIponri Shopping Centre",
			"Isolo Branch\n24, Abimbola Street Isolo",
			"Jibowu Branch\n33, Ikorodu Rd Jibowu, Lagos",
			"Lagos Airport Hotel Branch\n111,Obafemi Awolowo Road Ikeja",
			"Lapal Branch\n241, Igbosere Rd, Lagos",
			"Lawanson Branch\n89 Itire Rd, Lawanson",
			"Mamman Kontagora Branch\n23 Broad Street, Lagos",
			"Maryland Branch\n2, Mobolaji Bank Anthony Way, Maryland, Ikeja, Lagos",
			"Mushin Branch\n236, Agege Motor Road",
			"Nahco Branch\n1st Floor Nahco Building, Muritala Mohamed International Airport Lagos",
			"Npa Branch\nShed 6, Npa Terminal , Lagos",
			"Oba Akran Branch\nPlot 30 Oba Akran Avenue, Ikeja, Lagos",
			"Ogba Branch\nPlot 45 Omole Industrial Estate, Ogba Lagos",
			"Ojota Branch\nOdua Intl Model Market, Ojota, Lagos",
			"Oke-Arin Branch\n104 Alakoro, Oke-Arin Lagos",
			"Oniru Branch\nOdyssey Place, T F Kuboye Road",
			"Orile Iganmu Branch\n34, Opere Street, Off Lagos-Badagry Expressway, Orile Iganmu, Lagos",
			"Oshodi Branch\n455, Agege Moto Road, Oshodi",
			"Tinubu Branch\n27 Nnamdi Azikiwe Street Lagos Island",
			"Unilag Branch\nUnilag Campus, Unilag, Akoka Lagos",
			"Warehouse Rd Branch\n32, Warehouse Road Apapa,Lagos",
			"Wema Head Office Branch\n54, Marina , Lagos"				
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
	setContentView(R.layout.wemabankbranches);
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
			WemaBankBranches.this.adapter.getFilter().filter(cs);
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