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

public class AccessBankBranches extends ListActivity {
	String [] branches = {
			/*
			 * List of Access Bank branches in Lagos State
			 */
			"Adeniran Ogunsanya\n71, Adeniran Ogunsanya Street, Surulere, Lagos State",
			"Adeniyi Jones\n53, Adeniyi Jones, Ikeja, Lagos State",
			"Adeola Hopewell\nPlot 1697, Adeola Hopewell, Victoria Island, Lagos State",
			"Adeola Odeku(2)\n44, Adeola Odeku Street , Victoria Island, Lagos State",
			"Adeola Odeku\n11a, Adeola Odeku Street, Victoria Island, Lagos State",
			"Adetokunbo Ademola\n30a Adetokunbo Ademola Street, Victoria Island, Lagos State",
			"Adeyemo Alakija\n1, Idowu Taylor Street, Commerce House, Victoria Island,Lagos State",
			"Agbara\nPlot 2ca/4 Ilaro Road, Agbara Industrial Estate, Agbara, Ogun State",
			"Agege\n653, Abeokuta Express Road, Abule Taylor, Abule-Egba, Lagos State",
			"Aguda\n5, Enitan Street, Aguda, Surulere, Lagos State",
			"Agudosi\n4 Agudosi Str, Off Ojo Road, Alaba Intl Market, Alaba, Lagos State",
			"Ais/Usl\n14a Idowu Taylor,Victoria Island",
			"Ajao Estate\n55, Muritala Mohammed Airport Road, Ajao Estate, Isolo, Lagos State",
			"Ajose Adeogun\n287, Ajose Adeogun Street, Victoria Island, Lagos State",
			"Alaba Intl Mkt\n44/45 Alaba International Market Road, Alaba, Lagos State",
			"Alagbado\nKM 32, Daniel Farm, Lagos Abeokuta Exp Way, Agege, Lagos State",
			"Alausa\n183, Obafemi Awolowo Rd, Alausa, Ikeja, Lagos State",
			"Alfred Rewane Road\n1, Alfred Rewane Road, Ikoyi, Lagos State",
			"Allen Avenue(2)\n92, Allen Avenue, Ikeja, Lagos State",
			"Allen Avenue\n13, Allen Avenue, Ikeja, Lagos State",
			"Aluko & Oyebode\nNo 1 Muritala Muhammed Drive, Ikoyi",
			"Aspamda\nZone B, R4 Mercy Caf, Aspamda Plaza, Trade Fair Complex, Ojo, Lagos State",
			"Avalon House\nAdmiralty Way Lekki",
			"Awolowo Road(2)\n87, Awolowo Road , Ikoyi, Lagos State",
			"Awolowo Road\n58, Awolowo Road, Ikoyi, Lagos State",
			"Ayobo\n158, Ayobo-Ipaja Road, Ayobo, Lagos State",
			"Bode Thomas\n42, Bode Thomas Street, Surulere, Lagos State",
			"Broad Street(2)\n32, Broad Street, Lagos Island, Lagos State",
			"Broad Street\n115/117, Broad Street, Lagos Island, Lagos State",
			"Burma Road Apapa\n4, Burma Road, Apapa, Lagos State",
			"Casalydia\nGlover Road Ikoyi",
			"Commercial Road\n8/10, Commercial Road, Apapa, Lagos State",
			"Creek Road\n24a, Creek Road, Apapa, Lagos State",
			"Css Bookshop\nBroad Street Lagos",
			"Daleko\nShop 822/839 Bank Road, Daleko Market, Daleko, Mushin, Lagos State",
			"Dopemu\n92 Lagos/Abeokuta Expressway, Dopemu, Lagos State",
			"Egbeda\n35, Akowonjo Road,Egbeda, Lagos State",
			"Ejigbo\nAlong Ejigbo/Ikotun Road, Opp Nnpc Junction, Ejigbo, Lagos State",
			"Festac\n4th Avenue, Festac, Lagos State",
			"Gbagada\nPlot 286, Oshodi Apapa Expressway, Gbagada Phase I, Lagos State",
			"Iddo\nIddo Shopping Complex, Iddo, Lagos State",
			"Idejo It\nPlot 1617a Idejo/Danmole St.V/I",
			"Idejo Street\nPlot 161E, Idejo Street, Off Adeola Odeku, Victoria Island, Lagos State",
			"Idi-Araba\nCollege Of Medicine, Ishaga Road, Idi-Araba, Lagos State",
			"Idimu\n71, Egbeda/Idimu Road, Idimu, Lagos State",
			"Idumota\n122 Nnamdi Azikwe Street, Idumota, Lagos State",
			"Ifako-Gbagada\n6 Diya Street, Ifako-Gbagada, Lagos State",
			"Ijeshatedo\n206, Ijesha Road, Ijeshatedo, Surulere, Lagos State",
			"Iju\n134 Water Works Road, Iju-Ishaga, Lagos State",
			"Ikorodu Road\n38/40, Ikorodu Road, Jibowu, Lagos State",
			"Ikorodu(2)\n68, Lagos Road , Ikorodu,Lagos State",
			"Ikorodu\n7, Ayangburen Road, Ikorodu, Lagos State",
			"Ikota\nSuite E 79-81 And 116-118 Vgc, Ikota Shopping Complex, Ikota, Lagos State",
			"Ikotun\n4, Ikotun Junction, Ikotun, Lagos State",
			"Ilupeju(2)\n11, Town Planning Way, Ilupeju, Lagos State",
			"Ilupeju\n25a, Ilupeju Bye-Pass, Ilupeju, Lagos State",
			"Ipaja\n171, Abeokuta Expressway, Iyana Ipaja, Lagos State",
			"Ire Akari\n1, Godwin Omonua Street, Ire Akari Estate, Isolo, Lagos State",
			"Isolo\n113, Okota Road, Okota, Isolo, Lagos State",
			"Issa Williams\n27/29, Issa Wiliams Street, Lagos Island, Lagos State",
			"Ketu\n533, Ikorodu Road, Ketu, Lagos State",
			"Kosoko Street\n52/54, Kosoko Street, Lagos Island, Lagos State",
			"Lasu\nkm 20, Lagos Badagry Express Way, Ojo, Lagos State",
			"Lawanson\n87, Itire / Lawanson Road, Surulere, Lagos State",
			"Lekki Chevron\nKM 17, Lekki Epe Exp.Way, Chevron Round About, Lekki, Lagos State",
			"Lekki\nPlot 7, Blk 2, Oniru Private Estate, Lekki, Lagos State",
			"Ligali Annex\n177 Ligali Ayorinde Street, Victoria Island, Lagos",
			"Ligali Ayorinde\nPlot 15, Ligali Ayorinde, Victoria Island, Lagos State",
			"Marina B.O.I.\n23, Bank Of Industry Buliding, Broad Street, Marina, Lagos State",
			"Marina\n48, Marina Street, Lagos Island, Lagos State",
			"Maryland\n6, Mobolaji Bank-Anthony Way, Ikeja, Lagos State",
			"Matori\n125 Ladipo Street, Matori, Lagos State",
			"Maza Maza\n17, Sikiru Otunba Str, Old Ojo Road, Badagry Expressway, Mazamaza, Lagos State",
			"Moloney\n34, Moloney Street, Lagos Island, Lagos State",
			"Muri Okunola\n211 Muri Okunola Street Victoria Island, Lagos State",
			"Muritala Mohamed Way\n68/70 Muritala Mohammed Way, Oyingbo, Ebute-Metta, Lagos State",
			"Mushin\n279, Agege Motor Road, Mushin, Lagos State",
			"Nahco\nNahco Complex 1, Murtala Mohammed Intl Airport Rd, Ikeja, Lagos State",
			"Nnamdi Azikwe Idumota\n68, Nnamdi Azikwe Road Street, Idumota ,Lagos State",
			"Oando Ikota\nOando Filling Station Ikota",
			"Oba Akran\n23-25, Oba Akran Avenue, Off Awolowo Way, Ikeja, Lagos State",
			"Obafemi Awolowo Way\n77, Obafemi Awolowo Way (Man House), Ikeja, Lagos State",
			"Obas Palace\nObas Compound, Adeniji Adele",
			"Odogunyan\nOpp. 2nd Gate Lagos Polytechnic, Odogunyan, Ikorodu, Lagos State",
			"Ogba\n40a/40b, Ogba Ijaiye Road, Ogba, Lagos State",
			"Ogunlana Drive\n150, Ogunlana Drive, Surulere, Lagos State",
			"Ojodu Berger\nPlot 101, Isheri Road, Berger, Lagos State",
			"Ojota\n1a, Ogudu Road, Ojota, Lagos State",
			"Ojuelegba\n78, Ojuelegba Road, Tejuosho, Yaba, Lagos State",
			"Old Alaba Motor Park\nOld Alaba Motor Park, Off Ojo Igbede, Ojo, Lagos State",
			"Old Ojo\n153, Old Ojo Road, Kuje-Amuwo, Agboju, Lagos State",
			"Olodi-Apapa\nPlot 24, Opposite Ibru Jetty, Olodi Apapa, Lagos State",
			"Onikan\n30, King George V Road, Onikan, Lagos Island, Lagos State",
			"Opebi\n23, Opebi Road, Ikeja, Lagos State",
			"Oregun\n51, Kudirat Abiola Way, Oregun Road, Ikeja, Lagos State",
			"Orile Coker\n3, Alhaji Owokoniran Street, Orile Coker, Lagos State",
			"Oshodi\nMosafejo Market, Oshodi, Lagos State",
			"Palm Avenue\n56,Palm Avenue, Mushin,Lagos State",
			"Point Road\n1, Point Road, Apapa, Lagos State",
			"Resort Savings\nno 8 Boyle Street, Onikan",
			"Sabo\n290, Herbert Macaulay Way, Sabo, Yaba, Lagos State",
			"Saka Tinubu\n44a, Saka Tinubu Street, Off Adeola Odeku Street, Vi, Lagos State",
			"Satellite Town\n438, Old Ojo Road, Abule - Ado, Satelite Town, Lagos State",
			"Simbiat Abiola\n20, Simbiat Abiola Road, Ikeja, Lagos State",
			"Somolu\n47, Mkt/Odunlami Street, Somolu, Lagos State",
			"St Finbarrs\n67, St Finbarrs Road, Akoka , Yaba, Lagos State",
			"Tejuosho\n31, Tejuosho Street, Yaba, Lagos State",
			"Tincan\n1 Tincan Lighter Terminal, Kirikiri, Lagos State",
			"Toyin Street\n54, Toyin Street, Ikeja, Lagos State",
			"Trade Fair\n45 Atiku Abubakar Hall, Bba Trade Fair Complex, Off Badagry Express, Lagos",
			"University Of Lagos\nUniveristy Of Lagos, Akoka, Yaba, Lagos State",
			"Wharf Road\n13-15 Wharf Road, Apapa, Lagos State"
			
				
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
	setContentView(R.layout.accessbankbranches);
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
			AccessBankBranches.this.adapter.getFilter().filter(cs);
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
