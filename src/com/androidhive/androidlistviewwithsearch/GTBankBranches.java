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

public class GTBankBranches extends ListActivity {
	String [] branches = {
			"635 (Head Office)\n635 Akin Adesola, Victoria Island, Lagos State, Nigeria",
			"Abule Egba 2\n641 Lamgan Plaza Abule Tailor Lagos-Abeokuta Expressway, Abule-Egba, Lagos State",
			"Abule Egba\n402, Lagos-Abeokuta Expressway, Abule-Egba, Lagos State, Nigeria",
			"Adeniyi Jones\n31, Adeniyi Jones Avenue, Ikeja, Lagos State, Nigeria",
			"Adeola Odeku\n56A, Adeola Odeku Street, Victoria Island, Lagos State, Nigeria",
			"Adetokunbo Ademola\n714, Adetokunbo Ademola Street, Victoria Island, Lagos State, Nigeria",
			"Adeyemo Alakija (Plaza)\nNo. 6, Adeyemo Alakija Street, Victoria Island, Lagos State, Nigeria",
			"Airport Road 2\n10, International Airport road, Mafoluku Lagos",
			"Airport Road\n15, International Airport Road, Isolo, Lagos State, Nigeria",
			"Ajah\nKm 22, Lekki-Epe Expressway, Ajah, Lagos State, Nigeria",
			"Ajose Adeogun\n279, Ajose Adeogun Street, Victoria Island, Lagos State, Nigeria",
			"Akowonjo\n35, Shasha Road, Akowonjo, Lagos State, Nigeria",
			"Alaba\n13, Obosi Plaza, Alaba International Market, Lagos State, Nigeria",
			"Allen Avenue\n80/82, Allen Avenue, Ikeja, Lagos State, Nigeria",
			"Amuwo Odofin\nPlot B16, Block 41B, by Akin Mateola Close, Amuwo Odofin Residential Scheme, Along Ago Palace Way, Amuwo Odofiin LGA, Lagos State, Nigeria",
			"Anthony Village\n7, Anthony Village Road, Anthony Village, Lagos State, Nigeria",
			"Apapa Doualla\n12, Duolla Road, Apapa, Lagos",
			"Apapa\nDoyin House, 4 Commercial Avenue Apapa, Lagos State, Nigeria",
			"Aspamda\nZone A, R1 Aspamda Plaza, International Trade Fair Complex, Ojo, Lagos State, Nigeria",
			"Awolowo 2\n54, Awolowo Road, Off Ademola Street, Ikoyi, Lagos",
			"Ayangbure (Ikorodu 2)\nNo 68, Ayangbure Rd, Ikorodu",
			"Bayo Kuku\n19, Bayo Kuku Road, off Osborne Road, Ikoyi, Lagos State, Nigeria",
			"Bode Thomas 2\n124, Bode Thomas Street, Surulere, Lagos State.",
			"Bode Thomas\n94, Bode Thomas Street, Surulere, Lagos State, Nigeria",
			"Broad Street\n82/86, Broad Street, Lagos State, Nigeria",
			"Burma Road, Apapa\nPlot 17, Burma Road, Apapa, Lagos State, Nigeria",
			"Catholic Mission\n22/24, Catholic Mission Street, Lagos Island, Lagos State, Nigeria",
			"CBD Alausa\nPlot 11, Block A, IPM Road, Central Business District, Alausa, Lagos State, Nigeria",
			"Cele Ijesha\n44, Ajijebidun street Ijesha tedo along Apapa expressway, Ijesha Lagos",
			"Chevron drive\nBlock LXXIV A, Ojomu land, Beside Chevron Roundabout, Lekki/EpeExpressway, Lagos State, Nigeria",
			"Computer Village\n5, Osintelu Street, off Oremeji Street, Computer Village, Ikeja, Lagos State, Nigeria",
			"Creek Road\n35, Creek Road, Apapa, Lagos State, Nigeria",
			"Egbeda Idimu\n26 - 28, Akowonjo Road, Egbeda, Lagos",
			"Egbeda\n231, Egbeda-Idimu Road, Alimosho L.G.A, Egbeda, Lagos State, Nigeria",
			"Egbega 2\n26-28, Akonwonjo Road, Egbega, Lagos",
			"Festac 2\nPlot 220, 23 Road, Opposite J Close, Festac Town.",
			"Festac\nHouse 11, 301 Road, 2nd Avenue, Festac Town, Lagos State, Nigeria",
			"Gbagada\n14, Diya Street, Ifako, Gbagada, Lagos State, Nigeria",
			"GTExpress Bank Road\nBank Road by Federal Secretariat, Ikoyi, Lagos",
			"GTExpress Boundary\nBoundary Bustop, Ajegunle, Apapa, Lagos",
			"GTExpress Campos Square\nCampos Square, Lagos Island",
			"GTExpress Festac\n21 Road, Festac Town, Lagos",
			"GTExpress Ikorodu\n2, Sagamu Road, Ikorodu, Lagos",
			"GTExpress Jebba\nJebba Street, Ebute Meta, Lagos",
			"GTExpress Kingsway\nBeside Tantalizers, Kingsway Road, Apapa, Lagos",
			"GTExpress Lekki\nEbeano Supermarket, Oniru/Lekki Expressway, Lagos",
			"GTExpress Mile2\nOshodi-Apapa Expressway, Mile 2, Lagos",
			"GTExpress Mushin\nOlorunsogo Bustop, Mushin-Isolo, Lagos",
			"GTExpress Old Airport\nMM2, Local Airport Road, Ikeja",
			"GTExpress Old Apapa\nOld Apapa Road, Costain, Lagos",
			"GTExpress Shomolu\nOnipanu, Ikorodu Expressway, Shomolu",
			"GTExpress Western Avenue\nWestern Avenue By Mosalasi/Mushin, Ojuelegba",
			"GTExpress Wharf Road\nBeside Area B, Police Station, Wharf Road, Apapa, Lagos",
			"Guiness\n24 Oba Akran Avenue Ikeja, Lagos",
			"Ibafon\n1,Bakare Street, Ibafon, Apapa Expressway, Lagos State, Nigeria",
			"Idi Oro\n110, Agege Motor Road, Idi-Oro, Mushin, Lagos State, Nigeria",
			"Idumota\n134, Nnamdi Azikwe Street, Lagos Island, Lagos State, Nigeria",
			"Iju\n90, Iju Fagba Road, Iju, Lagos",
			"Ikeja Cantonment\nATM Gallery at Ikeja Cantonment, Mobolaji Bank Anthony way, Maryland, Ikeja.",
			"Ikorodu\n47, Lagos Road, Ikorodu Town, Lagos State, Nigeria",
			"Ikosi\nPlot A3C, Ikosi Road, Oregun, Ikeja, Lagos State, Nigeria",
			"Ikota 2\nBlock K7-11 and K18-22, Ikota Shopping Complex, Ajah.",
			"Ikota\nIkota Shopping Complex, VGC, Lekki-Epe Expressway, Lagos State, Nigeria",
			"Ikotun\n49, Ikotun Idimu road Lagos",
			"Ikoyi 1\n178, Awolowo Road, Ikoyi, Lagos State, Nigeria",
			"Ilupeju 2\nIlupeju Bypass Road, Ilupeju",
			"Ilupeju\n48, Town Planning Way, Ilupeju, Lagos State, Nigeria",
			"Ipaja\nNo 199, Ipaja Road, Ipaja",
			"Isolo\n1, Abimbola Way, Isolo, Lagos State, Nigeria",
			"Itire\n41/43 Itire Road, Surulere, Lagos",
			"Ketu\n570, Ikorodu Road, Ketu, Kosofe L.G.A, Lagos State, Nigeria",
			"LASPOTECH\nLagos State Polytechnic, Km 7, Sagamu, Road, Odogunyan, Ikorodu",
			"LASU\nLagos State University Campus, Motion Ground, Ojo Badagry Expressway, Lagos State, Nigeria",
			"Lawanson\nMuniru Baruwa Shopping Complex By Olatilewa Junction Itire Road, Lawanson, Lagos State, Nigeria",
			"Lekki\nBlock 5, Plot 5, Victoria Island Annex, Lekki, Lagos State, Nigeria",
			"LUTH\nRoute 1, Lagos University Teaching Hospital (LUTH) premises, Idi-Araba, Lagos State, Nigeria",
			"Magodo\nNo 16, CMD Road, Magodo",
			"Marina\n49A, Marina Street, Lagos State, Nigeria",
			"Masha\n145 Ogunlana Drive by Masha Roundabout, Surulere, Lagos",
			"Matori\n135, Ladipo Street, Matori, Lagos State, Nigeria",
			"MMA 2\nNew Local Wing, Murtala Mohammed Airport 2, Ikeja, Lagos State, Nigeria",
			"Mobolaji Bank Anthony\n31, Mobolaji Bank-Anthony Way, Ikeja, Lagos State, Nigeria",
			"Moloney\n30, Moloney Street, Lagos Island, Lagos State, Nigeria",
			"Mushin\n311, Agege Motor Road, Olorunsogo, Mushin, Lagos State, Nigeria",
			"Oba Akran II\nPlot 10, Oba Akran Avenue, Ikeja, Lagos State, Nigeria",
			"Oba Akran\n33, Oba Akran Avenue, Ikeja, Lagos State, Nigeria",
			"Ogba 2\n19, Isheri Raod, Opposite WAEC Building, Ijaiye-Ogba, Ikeja, Lagos",
			"Ogba\n4, Ogunnusi Road, Ogba, Lagos State, Nigeria",
			"Ogudu GRA\n133, Ogudu Road, Ogudu, Lagos State, Nigeria.",
			"Ojodu\n50, Isheri Road, Ojodu, Lagos State, Nigeria",
			"Ojuelegba\n74/76, Ojuelegba Road, Lagos State, Nigeria",
			"Oke Arin\n40, John Street, Oke-Arin, Lagos Island, Lagos State, Nigeria",
			"Okota\n115A, Okota Road, Okota, Lagos State, Nigeria",
			"Onipanu\n196, Ikorodu Road, Onipanu, Lagos State, Nigeria",
			"Opebi\n14, Opebi Road, Ikeja, Lagos State, Nigeria",
			"Oregun\n100, Kudirat Abiola Way, Oregun, Ikeja, Lagos State, Nigeria",
			"Orile Coker\nPlot 3 Block C, Amuwo Odofin Industrial scheme, Orile Coker Lagos",
			"Oyin Jolayemi (Plural House)\nPlot 1669, Oyin Jolayemi Street, Victoria Island, Lagos State, Nigeria",
			"Processing Centre\n714, Adetokunbo Ademola Street, Victoria Island, Lagos State, Nigeria",
			"Shogunle\n765 Agege Motor Way Shogunle",
			"St. Gregory\nNo. 1 Obadeyi Close, off St. Gregory Road, Ikoyi, Lagos State, Nigeria",
			"Tiamiyu Savage\nPlot 1400, Tiamiyu Savage Road, Victoria Island, Lagos State, Nigeria",
			"Toyin Street\n47, Toyin Street Ikeja, Lagos State, Nigeria",
			"UNILAG\nUniversity of Lagos, Akoka, Lagos State",
			"Wamco\nPlot 7B Acne Rd Ogba Industrial Estate, Lagos, Nigeria",
			"Western Avenue\n89, Western Avenue, Opposite Abalti Barrack, Ojuelegba Lagos",
			"Yaba\n216/218, Herbert Macaulay Way, Yaba, Lagos State, Nigeria",
			"Yaba 2\nCrusader House, 16 Commercial Avenue, Sabo, Yaba, Lagos, Nigeria."


				
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
	setContentView(R.layout.gtbankbranches);
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
			GTBankBranches.this.adapter.getFilter().filter(cs);
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
