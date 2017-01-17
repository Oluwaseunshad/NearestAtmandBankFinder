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

public class FidelityBankBranches extends ListActivity {
	String [] branches = {
			/*
			 * List of Fidelity Bank branches in Lagos State
			 */	
			"Adeola Hopewell\nAdeola Hopewell Branch, Victoria Island Lagos",
			"Adeola Odeku\nNo 28, Adeola Odeku Street, Victoria Island, Lagos",
			"Adetokunbo Ademola\n16, Adetokunbo Ademola Street, Victoria Island, Lagos",
			"Adeyemo Alakija\n2, Adeyemo Alakija Street, Victoria Island, Lagos",
			"Agege\n28-29 Iju Road, Agege Lagos",
			"Airport Road Lagos\n39, International Airport Road, By Ibis Hotel, Lagos",
			"Ait Mini\nNo 1, Ait Road, Alagbado, Lagos",
			"Akoka\n73, St.Finbars' Road, Akoka, Lagos",
			"Alaba 2\nBb 68 Japan Lane, Alaba International Market, Lagos",
			"Alexander (Ensec)\n7A Alexander Road, Ikoyi, Lagos",
			"Allen Avenu, Ikeja\n2A, Allen Avenue, Ikeja, Lagos",
			"Awolowo Road\n23, Awolowo Road, Ikoyi, Lagos",
			"Balogun\n40, Balogun Street, Lagos Island, Lagos",
			"Bba\nTrade Fair Complex, Badagry Express Way, Lagos",
			"Bode Thomas\n40, Bode Thomas Street, Surulere,  Lagos",
			"Boundary Market Ajegunle\nBoundary Market , Alon Ajeromi Ifelodun,Lagos",
			"Burma Roda, Apapa\n11, Burma Road, Apapa, Lagos",
			"Commercial Road, Apapa\nKazuma Plaza No. 2-4 Ede Street, Apapa, Lagos",
			"Computer Village Micro\n16, Ola Ayeni Street Ikeja Lagos",
			"Corporate\n2, Kofo Abayomi Street, Victoria Island, Lagos",
			"Dopemu\n6/8, Dopemu Roundabout, Dopemu, Lagos",
			"Ebute Metta\n11C Willoughby Street, Ebute Metta, Lagos",
			"Enu Owa\n69 Enu Owa Street, Lagos Island, Lagos",
			"Fadeyi\n70 Ikorodu Road, Fadeyi, Lagos",
			"Festac\nPlot K, 1St Avenue By Festac Canal, Festac, Lagos",
			"Financial Trust House (Balogun)\n1-4 Balogun Street Financial Trust House, Lagos Island, Lagos",
			"Gbagada\n15, Diya Street Gbagada, Lagos",
			"Ibafon\n16, Apapa Oshodi Express way, Apapa, Lagos",
			"Ibeju Lekki\nKm 47 Lekki/Epe Express Ibeju/Lekki Local Govt, Lagos",
			"Idumagbo\n18, Idumagbo Avenue, Idumagbo, Lagos Island, Lagos",
			"Ijora Badia\n19 , Sari Iganmu Road, New Rd Bus Stop, Ijora, Lagos",
			"Ikorodu Town\n54 Lagos Road, Ikorodu, Lagos",
			"Ikota Mini\nBlock O, Shop 80-88 Ik Ikota Shopping Complex, Lagos",
			"Ilupeju , Lagos\n42, Coker Road, Ilupeju, Lagos",
			"Ire Akari\n42, Ire-Akari Estate Isolo, Lagos",
			"Lagos Business School\nLagos-Epe Express Way, Lekki, Lagos",
			"Lekki\nLayout B Plot 3 Block B, Oniru Estate, Lekki, Lagos",
			"Matori\n143, Ladipo Street, Matori, Lagos",
			"Moloney\n3/5 Moloney Street, Race Course, Lagos",
			"Mushin\nNo 299 Agege Motor Road, Olorushogo Bus Stop, Lagos",
			"Nahco\nMurtala Muhammed Int'L Airport Cargo, Ikeja, Lagos",
			"Napex\nNapex Complex, Victoria Island, Lagos",
			"Oba Akran Ikeja\n21, Oba Akran Avenue, Ikeja, Lagos",
			"Obasanjo Main, Aspamda\nTrade Fair Complex, Badagry Express, Lagos",
			"Ogba\nPlot O, Block 2, Ogba Housing Est, Ogunusi, Lagos",
			"Ojo Alaba\n51 Olojo Road, Ojo Alaba, Lagos",
			"Ojota\nKm 16, Ikorodu Road, Ojota, Lagos",
			"Ojota\nKm 16, Ikorodu Road, Ojota, Lagos",
			"Okearin\n32, Isa Williams, Okearin, Lagos Island, Lagos",
			"Old Ojo Road\n39 Old Ojo Road, Lagos",
			"Oregun\n53 Kudirat Abiola Way, Oregun, Ikeja, Lagos",
			"Oshodi\n18 Apapa - Oshodi Express Way, Trinity, Lagos",
			"Oyingbo\n28, Kano Street, Ebute Metta, Lagos",
			"Tejuosho\n57, Tejuosho Street, Surulere, Lagos",
			"Tin Can Island\nTin Can Island Port Apapa Lagos",
			"Trinity\n225 Kirikiri Road Trinity Junction, Olodi Apapa, Lagos",
			"Victoria Garden City\nPlot8, Km 22, Lagos - Epe Express Way, Vgc, Lagos",
			"Warehouse Road Apapa,\n39, Warehouse Road, Apapa, Lagos",
			"Yaba\n1A, Birrel Avenue Sabo, Yaba, Lagos",

			
				
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
	setContentView(R.layout.fidelitybankbranches);
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
			FidelityBankBranches.this.adapter.getFilter().filter(cs);
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
