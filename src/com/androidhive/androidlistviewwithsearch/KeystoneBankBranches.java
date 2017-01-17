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

public class KeystoneBankBranches extends ListActivity {
	String [] branches = {

			"Abule-Egba\n680 Lagos-Abeokuta Express Way, Ijaye, Lagos",
			"Adeola Hopewell\nPlot  707 Adeola Hopewell, Victoria Island, Lagos",
			"Adeola Odeku Office\n52 Adeola Odeku Street, Victoria Island, Lagos",
			"Adeyemo Alakija Street\nHomebase Mortgage (Micro Finance Bank), Adeyemo Alakija Street, Victoria Island, Lagos",
			"Ahmadu Bello Way\nPlot 1226 Ahmadu Bello Way, Victoria Island, Lagos",
			"Ajah\nkm 22 Lekki-Epe Expressway, Ajah, Lagos",
			"Ajao Estate\n13, Murtala Mohammed International Airport Road, Ajao Estate, Lagos",
			"Ajose Adeogun\n1b Ajose Adeogun Street, Victoria Island, Lagos.",
			"Akoka\n23, St Finbarr's College Road,Akoka, Lagos",
			"Akowonjo (2)\n168, Akowonjo Road Cele Bus-Stop, Akowonjo, Egbeda, Lagos",
			"Akowonjo\n168 Akowonjo Road, Egbeda, Lagos",
			"Alaba\n6,Agudosi Street, Alaba International Market, Ojo",
			"Allen\n8, Allen Avenue, Ikeja, Lagos",
			"Amuwo Odofin\n Festac Link Bridge Road, Amuwo Odofin, Lagos",
			"Apapa  (2)\nTriana Building, 18/20, Commercial Road, Apapa, Lagos",
			"Apapa  (3)\n3b Point Road, Apapa, Lagos",
			"Apapa\n20b Warehouse Road,Apapa, Lagos",
			"Aspamda\nZone B, Aspmda International Trade Fair Complex, Badagry Expressway, Lagos",
			"Awolowo Road\nDe Plazaville, 119 Obafemi Awolowo Way, Ikeja, Lagos ",
			"Awolowo Way\n128, Awolowo Way, Ikeja, Lagos",
			"Bba\nHall 3, Bba Plaza International Trade Fair Complex, Lagos",
			"Broad Street (Cash Office)\nSPDC, 21/23 Marina Shell Car Park, Lagos",
			"Broad Street\n127 Broad Street, Lagos Island, Lagos",
			"Commercial Road\n18/20 Commercial Rd, Apapa, Lagos",
			"Computer Village\n7 Pepple Street, Computer Village, Ikeja, Lagos",
			"Ebutte Metta\n1 Thomas Street ,Apapa Road Ebute Metta, Lagos",
			"Epe Office\n21 Lagos Road,Beside Zenith Bank,Aiyetoro, Epe,Lagos",
			"Epe\n21, Lagos Road, Aiyetoro, Epe, Lagos",
			"Ereko\n18 Ereko Street,Idumota, Lagos",
			"Festac\nHouse 16, 2nd Avenue, Festac Town, Lagos",
			"Ibafon\nPlot 17, Apapa-Oshodi Expressway, Tincan, Lagos",
			"Idumota\n111 Nnamdi Azikiwe Street, Idumota, Lagos",
			"Ijesha\n283 Ijesha Road, Ijeshatedo, Surulere, Lagos",
			"Iju\n123, Iju Road, Iju, Lagos",
			"Ikeja (2)\n69 Opebi Road, Opebi, Ikeja, Lagos",
			"Ikeja (3)\n25, Kudirat Abiola Way,Old Oregun Road, Ikeja, Lagos",
			"Ikeja (4)\n78, Oba-Akran Avenue, Ikeja, Lagos",
			"Ikeja (5)\n38, Ogba/Ijaye Road, Ogba, Ikeja, Lagos",
			"Ikeja\n128 Awolowo Way, Ikeja, Lagos",
			"Ikorodu\n17, Lagos Road, Ikorodu, Lagos",
			"Ikota\nShop 0, 121-136 Ikota Shopping Complex, Lekki",
			"Ikotun\n113, Idimu-Ikotun Road, Ikotun, Lagos",
			"Ikoyi (2)\n36 Awolowo Road, Ikoyi, Lagos",
			"Ikoyi (Awolowo Rd)\n36, Awolowo Rd, Ikoyi",
			"Ilupeju (2)\n8 Town Planning Way, Ilupeju, Lagos",
			"Ilupeju\n24 Ilupeju Bye-Pass, Ilupeju, Lagos",
			"Isolo\n46 Ire-Akari Estate Road, Isolo, Lagos",
			"Ketu\nPlot 584 Ikorodu Road, Ketu, Lagos",
			"Key Stone Head Office\nNo 1 Key Stone Head Office Branch, Victoria Island, Lagos.",
			"Kirikiri\nNo. 1 Dillion Road, Kirikiri, Apapa, Lagos",
			"Lawanson\n70, Lawanson Road, Surulere,  Lagos",
			"Lekki\nBlock 94, Plot 15, Lekki Phase 1 Scheme, Lekki-Epe Express Way, Before 2nd Roundabout, Lekki, Lagos",
			"Magodo\nPlot 1, Blk 1a, Ikosi Magodo, Along Cmd Road, Magodo, Lagos",
			"Maryland\n16b Mobolaji Bank-Anthony Way, Maryland, Lagos State",
			"Mushin (2)\nNo. 21 Palm Avenue,  Mushin",
			"Mushin\n285, Agege Motor Road, Mushin, Lagos",
			"Oba Akran\n78, Oba Akran, Ikeja, Lagos",
			"Ogudu\n167, Ogudu Road, Ogudu, Lagos",
			"Ojo\nNigerian Army Industrial Trading Complex, Ojo Military Cantonment, Ojo, Lagos",
			"Oko-Oba (Cash Office)\nLagos State Abattoir, Oko-Oba, Agege, Lagos",
			"Oko-Oba\n1 Williams Estate, Tabontabon, Oko- Oba Lagos",
			"Omole\n1b Obadina Street, Off Isheri Road, Omole, Lagos",
			"Oregun\n25, Kudirat Abiola Way, Oregun, Lagos",
			"Orile (2)\nOpere Street, Lagos ",
			"Orile\nKilometer 3, Lagos-Badagry Expressway By Wema Bus Stop, Coker, Orile, Lagos",
			"Palmgroove\n224 Ikorodu Road, Palmgroove, Lagos State",
			"Point Road Apapa\n3b Point Road, Apapa, Lagos",
			"Surulere (2)\n35/37, Enitan Street, Aguda, Surulere, Lagos",
			"Surulere\n101 Bode Thomas Street, Surulere, Lagos State",
			"Tafewa Balewa Square\n37/37a Tafawa Balewa Square, Lagos Island, Lagos",
			"Tiamiyu Savage\nPlot 1397 Tiamiyu Savage Street, Victoria Island, Lagos.",
			"Trade Fair Complex\nAtiku Abubakar Hall, International Trade Fair Complex, Lagos",
			"Warehouse Road Apapa\n20b Warehouse Road,Apapa, Lagos",
			"Wharf Road\nPlot 17, Wharf Road, Apapa, Lagos",
			"Willoughby\n31 A Willoughby Street, Ebutte-Metta, Lagos State"
				
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
	setContentView(R.layout.keystonebankbranches);
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
			KeystoneBankBranches.this.adapter.getFilter().filter(cs);
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
