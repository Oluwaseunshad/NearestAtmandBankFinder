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

public class FidelityBankAtms extends Activity {

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
		setContentView(R.layout.fidelitybanksatms);

		// Listview Data
		String atms[] = {
				"11, Burma Road, Apapa, Lagos",
				"11C Willoughby Street, Ebute Metta, Lagos",
				"1-4 Balogun Street Financial Trust House, Lagos Island, Lagos",
				"143, Ladipo Street, Matori, Lagos",
				"15, Diya Street Gbagada, Lagos",
				"16, Adetokunbo Ademola Street, Victoria Island, Lagos",
				"16, Apapa Oshodi Express way, Apapa, Lagos",
				"16, Ola Ayeni Street Ikeja Lagos",
				"18 Apapa - Oshodi Express Way, Trinity, Lagos",
				"18, Idumagbo Avenue, Idumagbo, Lagos Island, Lagos",
				"19 , Sari Iganmu Road, New Rd Bus Stop, Ijora, Lagos",
				"1A, Birrel Avenue Sabo, Yaba, Lagos",
				"2, Adeyemo Alakija Street, Victoria Island, Lagos",
				"2, Kofo Abayomi Street, Victoria Island, Lagos",
				"21, Oba Akran Avenue, Ikeja, Lagos",
				"225 Kirikiri Road Trinity Junction, Olodi Apapa, Lagos",
				"23, Awolowo Road, Ikoyi, Lagos",
				"28, Kano Street, Ebute Metta, Lagos",
				"28-29 Iju Road, Agege Lagos",
				"2A, Allen Avenue, Ikeja, Lagos",
				"3/5 Moloney Street, Race Course, Lagos",
				"32, Isa Williams, Okearin, Lagos Island, Lagos",
				"39 Old Ojo Road, Lagos",
				"39, International Airport Road, By Ibis Hotel, Lagos",
				"39, Warehouse Road, Apapa, Lagos",
				"40, Balogun Street, Lagos Island, Lagos",
				"40, Bode Thomas Street, Surulere,  Lagos",
				"42, Coker Road, Ilupeju, Lagos",
				"42, Ire-Akari Estate Isolo, Lagos",
				"51 Olojo Road, Ojo Alaba, Lagos",
				"53 Kudirat Abiola Way, Oregun, Ikeja, Lagos",
				"54 Lagos Road, Ikorodu, Lagos",
				"57, Tejuosho Street, Surulere, Lagos",
				"6/8, Dopemu Roundabout, Dopemu, Lagos",
				"69 Enu Owa Street, Lagos Island, Lagos",
				"70 Ikorodu Road, Fadeyi, Lagos",
				"73, St.Finbars' Road, Akoka, Lagos",
				"7A Alexander Road, Ikoyi, Lagos",
				"Adeola Hopewell Branch Victoria Island Lagos",
				"Bb 68 Japan Lane, Alaba International Market, Lagos",
				"Block O, Shop 80-88 Ik Ikota Shopping Complex, Lagos",
				"Boundary Market , Alon Ajeromi Ifelodun,Lagos",
				"Kazuma Plaza No. 2-4 Ede Street, Apapa, Lagos",
				"Km 16, Ikorodu Road, Ojota, Lagos",
				"Km 16, Ikorodu Road, Ojota, Lagos",
				"Km 47 Lekki/Epe Express Ibeju/Lekki Local Govt, Lagos",
				"Lagos-Epe Express Way, Lekki, Lagos",
				"Layout B Plot 3 Block B, Oniru Estate, Lekki, Lagos",
				"Murtala Muhammed Int'L Airport Cargo, Ikeja, Lagos",
				"Napex Complex, Victoria Island, Lagos",
				"No 1, Ait Road, Alagbado, Lagos",
				"No 28, Adeola Odeku Street, Victoria Island, Lagos",
				"No 299 Agege Motor Road, Olorushogo Bus Stop, Lagos",
				"Plot K, 1St Avenue By Festac Canal, Festac, Lagos",
				"Plot O, Block 2, Ogba Housing Est, Ogunusi, Lagos",
				"Plot8, Km 22, Lagos - Epe Express Way, Vgc, Lagos",
				"Tin Can Island Port Apapa Lagos",
				"Trade Fair Complex, Badagry Express Way, Lagos"

		};

		lv = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.fidelitySearch);

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
				FidelityBankAtms.this.adapter.getFilter().filter(cs);
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

		/*
		 * public void onListItemClick(ListView parent, View v, int position,
		 * long id) {
		 */
		/*
		 * lv.setOnItemClickListener(new OnItemClickListener() { public void
		 * onItemClick(AdapterView<?> parent, View view, int position, long id)
		 * { String myString = (String) parent.getAdapter() .getItem(position);
		 * 
		 * 
		 * } });
		 */

	}

}
