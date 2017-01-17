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

public class DiamondBankAtms extends Activity {

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
		setContentView(R.layout.diamondbanksatms);

		// Listview Data
		String atms[] = { 
				/*
				 * List of all Diamond Bank ATMs in Lagos
				 */
				"Adeniyi Jones\n34, Ladipo Oluwole Street, Off Adeniyi Jones",
				"Adeola Odeku\nPlot 64, Adeola Odeku Street",
				"Ajah\nLekki-Epe Expressway, Thomas Estate (By Doren Hospital), Opp. Royal Garden, Ajiwe - Ajah",
				"Ajao Estate\n25, Asa-Afariogun Street, Ajao Estate",
				"Alaba International Market(2)\nOld Garage, Alaba International Market2",
				"Alaba International Market\nDobbil Plaza Avenue, Alaba International Market",
				"Alausa\n4, Ashabi Cole Road, Agidingbi Ikeja, Lagos",
				"Alimosho - Iyana Ipaja\n55/57 New Ipaja Road, Alimosho",
				"Amuwo Odofin\nPlot Nos 21, 22 & 23 Opposite Abc Transport Terminal, Amuwo Odofin, Lagos State",
				"Atiku Abubakar Plaza\nBalogun Business Association (Bba 1), Atiku Abubakar Plaza",
				"Awolowo Ikeja\n79/80 Awolowo Way, Ikeja	",
				"Awolowo Road\n80, Awolowo Road, Ikoyi",
				"Badagry Expressway\n1, Hall 2 Aspamda Plaza, Trade Fair Complex, Badagry Expressway",
				"Bank Plaza\nBalogun Business Association, (Bba 2), Bank Plaza, Trade Fair",
				"Bode Thomas\n31, Bode Thomas Street",
				"Burma Road\n11 Burma Rd, Apapa",
				"Coker Street, Badgry Expressway\nCoker, Km 19, Lagos/ Badagry Expressway, Coker Bus Stop Orile",
				"College Road\n71, College Road, Ogba",
				"Creek Road\n16, Creek Road Apapa, Lagos",
				"Daleko Market\nShops 661-670, Bank Road, Daleko Market, Mushin",
				"Dopemu (Aluminium Village)\nNo. 17, Ogeretedo St., Alum. Village, Dopemu",
				"Enu Owa\nNo. 71, Enu Owa Street, Lagos Island",
				"Festac Mini\nPlot 1609, E Close, 4th Avenue, Festac Town",
				"Festac\nHouse 20, 2nd Avenue Festac Town",
				"Gbagada/Ifako\n20, Diya Street, Ifako, Gbagada",
				"Head Office\nHead Office, Plot 1261, Adeola Hopewell Street, Lekki Epe Expressway, Lagos",
				"Herbert Macaulay\n238, Herbert Macaulay Street, Yaba",
				"Iddo Market\nIddo Market Mini, Iddo Ultramodern Market",
				"Idimu Road\n20, Idimu Rd, Ikotun",
				"Idumagbo Road\nNo. 7 Idumagbo Road, Lagos Island",
				"Ijaiye road\n36, Ijaiye Road, Ogba",
				"Ikorudu-Lagos Road\n83, Lagos Road, Ikorodu",
				"Ikota Retail\nShop, C96 -101, Ikota Shopping Complex, Vgc",
				"Ilupeju\n26a&B Ilupeju Byepass, Ilupeju",
				"Isheri Road\nPlot 47, George Crescent, Ogba",
				"Iyana Ipaja\nNo. 166, Abeokuta Expressway, Iyana Ipaja",
				"Jibowu\n32, Ikorodu Road, Jibowu",
				"Ketu\nPlot 608, Lagos - Ikorodu Road, Ketu Mile 12",
				"Kirikiri Towwn\nKarimu Street, Kirikiri Town, Apapa",
				"Lagos Island Balogun\n136, Balogun Street, Lagos Island",
				"Lagos Island Broad Street\n121, Broad Street, Lagos Island",
				"Lagos Island Idumota\n118, Nnamdi Azikiwe Street, Lagos Island",
				"Lagos Island\n71, Enuowa Street, Idumota, Lagos",
				"Lawanson\n58, Lawanson Road, Surulere, Lagos",
				"Lekki\nPlot 10, Block 117, Lekki Penisula",
				"Liverpool\n21 A&B, Liverpool Road, Apapa, Lagos State",
				"Mafoloku\nNo 77/79, Old Ewu Road, Mafoloku",
				"Marina\n23a,Mamman Kontagora House, Marina",
				"Matori\n129, Ladipo Street, Matori",
				"Maza Maza\n37, Old Ojo Rd, Maza Maza, Ojo",
				"Mushin Road\n51, Mushin Rd, Isolo",
				"NAHCO Shed\nNigeria Avaition Handling Compnay, Ikeja",
				"Ogunlana\n33 Ogunlana Drive, Surulere",
				"Ojuwoye Market\n190, Agege Motor Road, Ojuwoye",
				"Oke Arin\n 1, Oke Arin Street,Lagos Island, Lagos State",
				"Okota Retail\nNo. 116/118, Ago Palace Way, Okota",
				"Olorunsogo\nNo 281, Agege Motor Road, Olorunsogo, Mushin",
				"Onikan\n11, King George V Road, Onikan",
				"Opebi Street(2)\n10, Opebi Street Ikeja, Lagos State,",
				"Opebi Street\n60, Opebi Street Ikeja, Lagos State",
				"Oregun\nPlot E Ziatech Rd, Oregun",
				"Oshodi Expressway\n30, Apapa Oshodi Expressway, Coconut B/Stop",
				"Oyingbo\n1, Market Street,Oyingbo, Opposite Bhojsons Ltd",
				"Roro Port\nRoro Port, Tin Can Island, Apapa",
				"Satellite Town Mini\nBlock 11, Plot 4, Old Ojo Rd., Satellite Town",
				"Seme Border\nSeme Border, Badagry, Lagos State",
				"St. Finbarrs Road, Bariga\nPlot 103 & 105, St. Finbarrs Road, Akoka",
				"Tejuosho Retail\nNo. 6, Ojuelegba Road, Opp. Tejuosho Market",
				"The Palms Shopping Complex\nShop 41, The Palms Shopping Complex, V/ Island",
				"Wharf Road\nAgittarius Block, Eleganza Plaza, Wharf Road"

				
		};

		lv = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.diamondSearch);

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
				DiamondBankAtms.this.adapter.getFilter().filter(cs);
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

