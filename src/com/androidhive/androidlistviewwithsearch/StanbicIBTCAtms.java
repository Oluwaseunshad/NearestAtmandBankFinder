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

public class StanbicIBTCAtms extends Activity {

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
		setContentView(R.layout.stanbicibtcbanksatms);

		// Listview Data
		String atms[] = { 
				"Abule Egba\n633, Lagos Abeokuta Expressway, Abule Egba,Lagos",
				"Ademola Adetokunbo Branch\n76 Adetokunbo Ademola Street, V.I. Lagos",
				"Afribank Street Branch\nChurchgate Towers, Plot 30, Afribank Street, Victoria Island. Lagos",
				"Agege Market, Lagos\n173, Old Abeokuta Road, Agege, Lagos",
				"Ahmadu Bello Way, V.I (Federal Palace Hotel)\nFederal Palace Hotel, Victoria Island, Lagos",
				"Ajah, Lagos\nMega Wave Plaza, 4a Addo Road About, Off Badore Road, Ajah",
				"Ajegunle\n11, Orodu Street, Ajegunle, Lagos",
				"Akoka\nNo. 100, St. Finbarr's Road, Akoka, Lagos",
				"Alaba Branch\nH48/49, Alaba International Market, Lagos",
				"Alausa Branch\nWapco Building, Alausa, Ikeja.",
				"Allen Avenue Branch\n31, Allen Avenue, Ikeja , Lagos",
				"Awolowo Model Market, Mushin, Lagos\nShop M1/M48, Awolowo Ultra Modern Market, Mushin, Lagos State.",
				"Awolowo Road Branch\n85, Awolowo Road Branch, Ikoyi, Lagos",
				"Balogun Business Association Branch.\nExecutive Plaza, No12, Bba Market, Trade Fair Complex, Badagry Express Way, Lagos.",
				"Broad Street Branch (Former Offin Road)\n143/145 Broad Street , Apongbon, Lagos Island, Lagos",
				"Daleko, Lagos\nBank Road, Daleko Market, Off Isolo Road, Mushin, Lagos",
				"Egbeda, Lagos\n38, Shasha Road, Egbeda, Lagos State",
				"Ejigbo Lagos\n91 Isolo Ikotun Road (Inside Ap Filling Station) Ejigbo Lagos",
				"Festac, Lagos\n23 Road, 2nd  Avenue, Gacoun Shopping Plaza, Festac  Town, Lagos",
				"Gbaja Market, Surulere, Lagos Branch\n12, Gbaja Market, Surulere, Lagos State",
				"Herbert Macaulay, Yaba Branch\n220, Herbert Macaulay Road, Yaba",
				"Idejo Branch\nPlot 1712, Idejo Street, Victoria Island",
				"Idumagbo Branch\n61, Idumagbo Avenue, Lagos Island",
				"Igando Branch\nNo. 51, Lasu - Iba Road, Igando, Lagos",
				"Ikeja City Mall\nShop L55, Ikeja City Mall, Opposite Elephant House, Alausa, Ikeja. Lagos",
				"Ikorodu Lagos\n108 Lagos Road, Ikorodu, Lagos",
				"Ikota Lekki, (Ajah Road) Branch\n167 -194, Block 1, Ikota Shopping Complex,Ajah",
				"Ikotun\n45 Idimu Road, Ikotun, Lagos State",
				"Ipaja\n142 Ipaja Road, Baruwa - Ipaja, Lagos",
				"Karimu Kotun Branch\n1321b Karimu Kotun Street, Victoria Island, Lagos",
				"Ketu\n463, Ikorodu Road, Ketu, Lagos",
				"Lawanson, Lagos\nNo 35, Lawanson Road, Surulere, Lagos",
				"Lekki (Lekki 1) Branch\nThe Palms Shopping Centre, Lekki, Lagos",
				"Lekki Admiralty\nPlot A Block 12e, Admiralty Way, Lekki Phase 1, Lekki, Lagos",
				"Lekki-Ajah Express Way\nKm 18, Lekki – Epe Express Way, Agungi, Lagos State",
				"Martins Street Branch\n19, Martins Street, Lagos.",
				"Maryland\n10, Mobolaji Bank Anthony Way, Maryland, Lagos",
				"Muri Okunola Branch (Relocated To Ajose Adeogun)\nPlot 290e Ajose Adeogun Street, Victoria Island, Lagos",
				"Murtala Moh'd Int'l Airport Branch\nArrival Hall, Murtala Mohamed International Airport, Ikeja, Lagos.",
				"Npa Branch\nAccount Block, Wharf Road, Apapa, Lagos.",
				"Oba Akran Branch\n20, Oba Akran Avenue, Ikeja",
				"Ogba, Lagos\n32, Ijaiye Road, Ogba, Lagos",
				"Ogudu Branch\n54 Ogudu - Ojota Road, Ogudu, Lagos",
				"Ojodu\n102, Isheri Road, Ojodu Berger, Lagos,",
				"Ojuwoye, Mushin\nNo. 214, Agege Motor Road,Ouwoye, Mushin, Lagos",
				"Oke Arin\n120, Alakoro Street, Oke Arin, Lagos Island",
				"Oko Oba, Lagos\nAbattoire Market, New Oko Oba, Agege, Lagos",
				"Okota\nAdenekan Mega Plaza, Okota, Isolo",
				"Opebi Branch\n43, Opedi Road, Ikeja, Lagos",
				"Oshodi Market, Lagos\n6/8, Brown Street, Oshodi, Lagos",
				"Osolo Way Branch\nOsolo Way, Ajao Estate, Isolo, Lagos (Beside Ascon Filling Station)",
				"Oyingbo, Lagos\n7, Coates Street, Ebute Metta, Oyingbo, Lagos",
				"Palms Avenue Area, Mushin Branch\n103, Ladipo Street, Mushin, Lagos",
				"Shomolu\n22 Market Street, Shomolu, Lagos",
				"Surulere Branch.\nBlock 1a, Adeniran Ogunsanya Shopping Mall, 84 Adeniran Ogunsanya Street, Surulere, Lagos.",
				"Tejuosho Branch\nNo 77, Ojuelegba Road Yaba, Lagos",
				"Tin Can Branch\nNo.8 Apapa-Oshodi Express Way (By Coconut Bus Stop), Apapa",
				"Toyin Street Branch\n36a, Toyin Street, Ikeja",
				"Trade Fair Branch\nInternational Trade Fair Complex, (Aspamda), Lagos",
				"Walter Carrington\nIbtc Place, Walter Carrington Crescent",
				"Warehouse Road Branch, Apapa\n10/12, Wharehouse Road, Apapa",
				"Yinka Folawiyo Branch, Apapa\n38,Warehouse Road, Apapa, Lagos",
				
				
		};

		lv = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.stanbicibtcSearch);

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
				StanbicIBTCAtms.this.adapter.getFilter().filter(cs);
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
