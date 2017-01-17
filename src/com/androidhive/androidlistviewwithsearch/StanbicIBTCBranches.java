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

public class StanbicIBTCBranches extends ListActivity {
	String [] branches = {
			"Abule Egba\n633, Lagos Abeokuta Expressway, Abule Egba, Lagos State, Nigeria",
			"Adeniran Ogunsanya\nBlock 1A, 84 Adeniran Ogunsanya Street, Surulere, Lagos State, Nigeria",
			"Adetokunbo Ademola\n76 Adetokunbo, Ademola Street Victoria Island, Lagos",
			"Agege\n173, Old Abeokuta, Motor Road Agege, Lagos",
			"Ajah\nMega Wave Plaza, 4A, Addo Road Ajah, Lagos",
			"Ajegunle\n32, Nosamu Street Ajegunle, Lagos",
			"Ajose Adeogun\nPlot 290E Ajose, Adeogun Street Victoria Island, Lagos",
			"Akoka\n100 St. Finbarr Road, Akoka, Lagos State, Nigeria",
			"Alaba\nNo. H48/49, International Market Alaba, Lagos",
			"Alausa\nWapco Building, Alausa Ikeja, Lagos",
			"Allen Avenue\nNo. 31, Allen Avenue Ikeja, Lagos",
			"BBA\nPlaza 12, BBA, Trade Fair, Badagry Expressway Ojo, Lagos state",
			"Broad Street\n143/145, Broad Street Lagos Island, Lagos",
			"City Mall, Ikeja\nShop L55, City Mall Ikeja, Lagos",
			"Daleko\nBank Road, Daleko Market, Mushin, Lagos State, Nigeria",
			"Egbeda\n38, Sasha Road, Egbeda, Lagos State, Nigeria",
			"Ejigbo\nNo 91, Isolo-Ikotun Road, Ejigbo, Lagos State, Nigeria",
			"Federal Palace Hotel\nFederal Palace Hotel, Ahmadu Bello Way Victoria Island, Lagos",
			"Festac Town\nGACOUN PLAZA, 23 Road, Festac Town, Lagos State, Nigeria",
			"Gbaja\nNo. 10, Gbaja Street Market Surulere, Lagos",
			"Herbert Macaulay Road,Yaba\n220, Herbert Macaulay Road, Yaba, Lagos State, Nigeria",
			"Idejo\nPlot 1712, Idejo Street Victoria Island, Lagos",
			"Idumagbo\n61, Idumagbo Avenue Lagos Island, Lagos",
			"Igando\n51, Lasu- Iba Road, Igando, Lagos State, Nigeria",
			"Ikorodu\n108, Lagos Road, Ikorodu, Lagos State, Nigeria",
			"Ikota\n167 -194, Block 1, Ikota Shopping Complex Ajah, Lagos",
			"Ikotun\n45 Idimu Road, Ikotun, Lagos State, Nigeria",
			"Ikoyi\n85, Awolowo Road Ikoyi, Lagos",
			"Ipaja\n142 Ipaja Road, Baruwa, Ipaja, Lagos State, Nigeria",
			"karimu Kotun\n1321B Karimu, Kotun Street Victoria Island, Lagos",
			"Ketu\n463, Ikorodu Road Ketu, Lagos",
			"Ladipo\n103, Ladipo Street, Mushin, Lagos State, Nigeria",
			"Lawanson Road, Surulere\n35, Lawanson Road, Surulere, Lagos State, Nigeria",
			"Lekki Admiralty\nPlot A Block 12E, Admiralty Way Lekki, Lagos",
			"Lekki Ajah\nKm 18, Lekki - Epe Express Way, Agungi, Lagos State, Nigeria",
			"Martins street\n19, Martins Street Lagos Island, Lagos",
			"Maryland\n10, Mobolaji Bank, Anthony Way Maryland, Lagos",
			"Murtala Mohammed Airport Ikeja\nArrival Hall, Murtala Mohammed Airport Ikeja, Lagos",
			"Mushin\nAwolowo, Modern Market Mushin, Lagos",
			"Oba Akran\n20, Oba Akran Avenue Ikeja, Lagos",
			"Ogba\n32, Ijaiye Road Ogba, Lagos",
			"Ogudu\n54 Ogudu - Ojota Road, Ogudu, Lagos State, Nigeria",
			"Ojo\nInternational Trade Fair Complex, Ojo, Lagos State",
			"Ojodu\n102, Isheri Road, Ojodu Berger, Lagos State, Nigeria",
			"Ojuelegba Road Yaba\nNo. 77, Ojuelegba Road Yaba, Lagos",
			"Ojuwoye, Mushin\n214, Agege Motor Road, Ojuwoye, Mushin, Lagos State, Nigeria",
			"Oke Arin\n120, Alakoro Street, Oke Arin, Lagos State, Nigeria",
			"Oko-oba\nAbattoir Market, New Oko Oba Agege, Lagos",
			"Okota\nAdenekan Mega Plaza, Okota Isolo, Lagos",
			"Opebi\nNo. 43, Opebi Road Ikeja, Lagos",
			"Oshodi\n6/8 Brown Street, Oshodi, Lagos State, Nigeria",
			"Osolo\nOsolo Way, Ajao Estate, Ajao, Lagos State, Nigeria",
			"Oyingbo\n7 Coates Street, Ebuta-Metta, Oyingbo, Lagos State, Nigeria",
			"Palms, Lekki\nShop 38, Palms Shopping Mall Lekki, Lagos",
			"Shomolu\n22 Market Street, Shomolu, Lagos State, Nigeria",
			"Tincan Island\n8 Apapa-Oshodi Express Way, Apapa, Lagos State, Nigeria",
			"Toyin Street\nNo. 36A, Toyin Street Ikeja, Lagos",
			"Victoria Island\nChurchgate Towers, Plot 30, Afribank Street Victoria Island, Lagos",
			"Walter Carrington Crescent\nIBTC House, Walter Carrington Crescent Victoria Island, Lagos",
			"Warehouse Road\nNo. 38, Warehouse Road Apapa, Lagos",
			"Warehouse road 2\nNo. 10/12, Warehouse Road Apapa, Lagos",
			"Wharf Road\nAccount Block, Wharf Road Apapa, Lagos"

				
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
	setContentView(R.layout.stanbic_ibtcbankbranches);
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
			StanbicIBTCBranches.this.adapter.getFilter().filter(cs);
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
