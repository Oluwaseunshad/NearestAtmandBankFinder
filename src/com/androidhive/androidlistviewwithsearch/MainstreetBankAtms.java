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

public class MainstreetBankAtms extends Activity {

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
		setContentView(R.layout.mainstreetbanksatms);

		// Listview Data
		String atms[] = { 
				
				"Adeniyi Jones\nWahum Building, 2, Adeniyi Jones Avenue, Off Oba Akran, Ikeja",
				"Admiralty Way\nBlock 10, Plot 5, Admiralty Way, Lekki Phase I, Victoria Island",
				"Alaba I\n18, Ojo Igbede Road, Alaba Int'l Market",
				"Alaba II Eagle Plaza\n1 / 2, Eagle Plaza, Block 21, Alaba International Market",	
				"Allen Avenue\n83 Allen Avenue, Ikeja",
				"Ap House\n54/56 Broad Street",
				"Apapa Commercial Road\nCommercial Avenue, Eleganza Building, Apapa",
				"Apapa Warehouse Road\n42/44, Warehouse Road, Apapa",
				"Aspamda\nZone D, Block 6 Aspamda Plaza Trade Fair Complex, Badagry Exp",
				"BBA\nOruku Plaza Opp. Sokoto Plaza, Balogun Business Assoc. Trade Fair Complex",
				"Bishop Aboyade Cole\nPlot 852 Bishop Aboyade Cole Street, Victoria Island",
				"Bode Thomas\n92, Bode Thomas Street, Surulere",
				"Broad Street\nMainstreet Bank Plaza, 51/55, Broad Street",
				"Daleko\n1, Bank Road, Daleko Market Mushin",
				"Fatai Atere\nPlot 11/12, Fatai Atere Way, Mushin",
				"Festac\n12, Second Avenue, Festac Town",
				"Ibafon\nShamrock House, Plot 10, Oshodi-apapa Exp. Way, Ibafon Coconut Area",
				"Iga - Idugaran\n64, Iga - Idugaran Street, Idumota",
				"Ikota\nBlock Rm 5, Ikota Shopping Complex, Vgc, Eti-osa Local Govt. Area",
				"Isaac John\n47, Isaac John Street, Gra, Ikeja",
				"Isolo\nPlot 1, Block F, Isolo Ind. Estate, Toyota Bus Stop, Apapa-oshodi Exp. Way",
				"Issa Williams\n182/184 Broad Street",
				"Marina\nEleganza Building, 15b, Joseph Street",
				"Martins Street\nGreat Nigeria House, 47/57, Martins Street",
				"Murtala Mohammed International Airport\nMurtala Mohammed Airport, Stall 007, Arrival Hall, Ground Flour",
				"NECA\nNeca Building, Plot A2, Hakeem Balogun St., Alausa, Ikeja",
				"Ojuelegba\n68, Ojuelegba Road, Surulere",
				"Oke-Arin\n18, Doherty Street, Oke-arin",
				"Oladele Olashore\n1, Oladele Olashore, Street Off Sanusifafunwa Street, Victoria Island",
				"OPIC\nOpic Plaza, 26, Mobolaji Bank Anthony Way, Maryland, Ikeja",
				"Oregun\nPlot 11, Kudirat Abiola Way, Oregun, Ikeja",
				"Reinsurance - Marina\nReinsurance House 46, Marina",
				"Victoria Island\nMainstreet House, Pc 28, Afribank Street"
		};

		lv = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.mainstreetSearch);

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
				MainstreetBankAtms.this.adapter.getFilter().filter(cs);
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
