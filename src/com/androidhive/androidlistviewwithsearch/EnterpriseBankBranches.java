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

public class EnterpriseBankBranches extends ListActivity {
	String [] branches = {
			/*
			 * List of Enterprise Bank branches in Lagos State
			 */
			"Adeola Hopewell\nPlot 1611, Adeola Hopewel Street",
			"Adeola Odeku\n42, Adeola Odeku Street V/Island, Lagos",
			"Agidingbi(2)\n5a Ashabi Cole Street Off Lateef Jakande Agidingbi",
			"Agindigbi\nMikano Head Office, Wampco Road Agidingbi",
			"Ago Palace Way\n116/118 Ago Palace Way, Okota",
			"Agudosi\n6, Agudosi Street Alaba International Market",
			"Ajah\nAjah Ultra Moderm Market, Lekki/Epe Express Road",
			"Ajao Estate\nNo 1,Ganiyu Williams,Isolo",
			"Allen\n44, Allen Avenue Ikeja",
			"Aspamda Tradefair\nBanking Hall 2 Tradefair",
			"Balogun Street\n33, Balogun Street",
			"Bba 1\nBenue Plaza, Trade Fair Complex",
			"Broad Street\n14/136 Broad Street",
			"Canaanland\nkm 10 Idiroko Rd, Ota Canaanland",
			"Commercial Road\n32, Commercial Road, Apapa",
			"Creek Road\n24, Creek Road, Apapa",
			"Divine Grace Plaza\nLeocolo Plaza Progresive Market, Trade-Fair",
			"Ebute Metta\n31, Coates Str, Ebute Meta",
			"Enu-Owa, Idumota\n93,Enu-Owa Street Idumota Lagos",
			"Femi Pearse\n5a, Femi Pearse Street",
			"Head Office\n143 Ahmadu Bello Way, Vi, Lagos",
			"Idumagbo\n53a Idumagbo Avenue ,Idumagbo.Lagos Island",
			"Idumota\n138 Nnamdi Azikwe Rd, Idumota",
			"Igbesa\n4,Main Road Igbesa",
			"Ikoyi\n93, Awolowo Road, Ikoyi",
			"Ilupeju Bypass\n30, Ilupeju Bypass Rd, Ilupeju",
			"Ilupeju Industrial Avenue\n6, Ilupeju Industrial Avenue,Ilupeju",
			"Karimu Ikotun\n27, Karimu Ikotun Street",
			"Martins Street\n31/33 Martins Street, Lagos",
			"Matori\nplot 151, Ladipo Street, Mushin",
			"Melford Okilo\nMelford Okilo Plaza Bba Tradefair",
			"Navy Town, Mammy Market\nMammy Market Navy Town Ojo",
			"Nnewi Building Apapa\n1-3 Creek Road Apapa Lagos",
			"Obun-Eko, Idumota\n14,Obun-Eko Street Idumota Lagos",
			"Ogba\n23,Wempco Road Ogba, Ikeja",
			"Ojuwoye\n12, Dada Iyalode Street, Ojuwoye Mushin",
			"Oke-Arin\n14/16 Daddy Alaja Oke-Arin Lagos",
			"Oshodi\n1,Agege Motor Road,Olupese House, Oshodi",
			"Otigba\n46 Otigba Street, Computer Village",
			"Raybross Plaza\n2a Ojo-Igbede Road, Old Garage, Alaba International Market",
			"Saka Tinubu\n11, Saka Tinubu Street",
			"Surulere\n24, Ogulana Drive, Surulere",
			"Tincan\n9 North Gate, Tincan Island",
			"Trinity\n11, Industrial Road, By Sadiku Str Olodi Apapa",
			"Warehouse Apapa\n52 Warehouse Road, Apapa",
			"Wharf Road\n20 Wharf Road, Apapa"

				
			
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
	setContentView(R.layout.enterprisebankbranches);
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
			EnterpriseBankBranches.this.adapter.getFilter().filter(cs);
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
