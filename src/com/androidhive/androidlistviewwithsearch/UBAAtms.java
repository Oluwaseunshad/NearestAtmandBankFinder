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

public class UBAAtms extends Activity {

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
		setContentView(R.layout.ubaatms);

		// Listview Data
		String atms[] = { 
				
				"Aboyade Cole\n33b Bishop Aboyade Cole Street, Victoria Island, Lagos",
				"Abule Egba\n473b,Abeokuta Express Road, Abule Egba, Lagos",
				"Adeola Hopewell\nPlot 1607 Adeola Hopewell Street Victoria Island, Lagos",
				"Adeola Odeku\nPlot 1115a Adeola Odeku Street, Victoria Island, Lagos",
				"Adetokunbo Ademola\n80a, Adetokunbo Ademola Street, Victoria Island, Lagos",
				"Adetokunbo Ademola 2\n29 Adetokunbo Ademola Victoria Island, Lagos",
				"Adeyemo Alakija\n19 Adeyemo Alakija Street, Victoria Island, Lagos",
				"Ajose Adeogun\nAjose Adeogun Str, Victoria Island, Lagos",
				"Akin Adesola Atm\n11b Akin Adesola Str, Victoria Island, Lagos",
				"Alaba Flex\n3 Agudosi Street Alaba Int'l, Alaba",
				"Alausa\nElephant Cement House Alausa, Ikeja",
				"Allen 2\n13 Allen Avenue, Ikeja, Lagos",
				"Allen Avenue\n73 Allen Avenue, Ikeja",
				"Amodu Tijani\nPlot 697 Amodu Tijani Street Victoria Island Lagos",
				"Apapa 2 Wharf\n27 Wharf, Apapa, Lagos",
				"Aspamda 1\nOlu Obasanjo Hall Trade Fair Complex, Badagry Expressway, Lagos",
				"Babs Animashaun\nBabs Animashaun Street, Surulere, Lagos",
				"Badagry\nMarket Road, Badagry, Lagos",
				"Berger 1\nBerger, Oshodi Express Way, Lagos",
				"Bode Thomas\nBode Thomas, Surulere",
				"Bonny Camp Military Barrack\nBonny Camp Military Barracks, Victoria Island, Lagos",
				"Bornu Way\n20 Bornu Way, Ebutte Metta, Yaba, Lagos",
				"Breadfruit\n11 Davies Street, Lagos Island",
				"Burma Road\n16 Burma Road Apapa",
				"Cadbury Nigeria Plc\nCadbury Nigeria Plc, Ikeja",
				"Chevron\n2, Chevron Drive, Lekki Peninsula. Lagos",
				"Computer Village\n9 Oremeji Street, Computer Village, Ikeja, Lagos",
				"Daleko\nDaleko Market, Mushin, Lagos",
				"Docemo\n21/23 Docemo Street Lagos Island, Lagos",
				"Dolphin Estate Atm\nSuite 69/70/104, Dolphin Plaza, Dolphin Estate, Ikoyi, Lagos",
				"Dopemu 2\n15 Aina Layout, Lagos-Abeokuta Expressway, Dopemu, Lagos",
				"Ebute-Metta\n86 Murtala Mohammed Way,Yaba",
				"Egbeda\nPlot 94 Akowonjo Egbeda Rd, Egbeda, Lagos",
				"Ejigbo\nNnpc Ejigbo Lagos",
				"Falomo\n172, Awolowo Road, Falomo, Ikoyi",
				"Festac 1 Business Office\n23/72 Road Junction, Festac Town, Lagos",
				"Festac 2  Atm\n21 Road, 31 Junction, Festac Town, Lagos",
				"Festac 2 Business Office\n21 Road, 31 Junction, Festac Town. Lagos State",
				"Gbagada\n38, Ajayi Aina Str, (Barrack B/Stop), Ifako-Gbagada, Gbagada, Lagos",
				"Gbagada Express Atm\nGbagada Express Way, Gbagada, Lagos",
				"H25/H26 Alaba International\nH25/H26 Alaba International, Alaba, Lagos",
				"Herbert Macauley\n366 Herbert Macauley Sabo Yaba, Lagos",
				"Ibafon\n12 Apapa Oshodi Express Way By Coconut Bus-Stop, Apapa",
				"Iddo Business Office\n1 Taylor Road G.Cappa, Iddo, Lagos",
				"Idowu Taylor\n22b Idowu Taylor, Victoria Island, Lagos",
				"Idumagbo\n53 Idumagbo Avenue, Lagos Island",
				"Idumota 2\n144 Nnamdi Azikiwe Street Idumota, Lagos",
				"Idumota\n169 Idumota Street, Lagos Island, Lagos",
				"Igbosere\n14 Joseph Street Igbosere, Lagos Island",
				"Ijaye Ogba\n50 Ijaiye Ogba, Lagos",
				"Ijora\n10 Ijora Cause Way, Ijora, Lagos",
				"Iju\n96, Iju Road, Fagba, Lagos",
				"Ikorodu Road Atm\n300 Ikorodu Road, Anthony Village, Lagos",
				"Ikota\nShop E41-E45 And E74-E78, Ikota Shopping Complex, Vgc, Lekki, Lagos",
				"Ikotun Business Office\nIkotun Idimu Rd, Ikotun, Lagos",
				"Ilupeju\n15 Industrial Avenue, Ilupeju",
				"Iponri\nIponri Shopping Complex, Iponri, Surulere, Lagos",
				"Isolo\n 10 Osolo Way, Ajao Estate, Lagos",
				"Itire Rd\n41/43 Itire Road Ojuelegba, Surulere, Lagos",
				"Ketu\n549, Ikorodu Road, Ketu, Lagos",
				"Lagos East Branch\n12/14 Broad Street,Lagos Island",
				"Lasu\nLagos State University, Ojo, Lagos",
				"Lawanson\n66 Lawanson Street, Surulere, Lagos",
				"Lcb\n97/105 Broad Street, Lagos Island",
				"Lekki 1\n80a, Admiralty Way, Lekki Phase 1, Lekki, Lagos",
				"Luth\nRoad 1,Lagos University Teaching Hospital, Idi-Araba, Yaba, Lagos",
				"Marina\n55 Marina, Lagos Island",
				"Marina West\n53 Marina, Lagos Island",
				"Marine View\n60 Marina, Lagos",
				"Martin Street\n15 Martin Street Lagos Island, Lagos",
				"Matori 1 Atm1\nNucleus House, 21a Fatai Atere Way, Matori, Mushin, Lagos",
				"Mobil House\n1 Lekki-Epe Express Way, Maroko. Victoria Island, Lagos",
				"Moneygram Office, Ikeja\nAwolowo House (Moneygram Office), Ikeja, Lagos",
				"Mtn Opebi\nMtn Opebi, Ikeja",
				"Murtala Mohammed Int'l Airport Atm1\n62/64 Murtala Mohammed Int'l Airport Road, Ikeja, Lagos",
				"Mushin\n20 Palm Avenue, Mushin, Lagos",
				"Naf Base\nSam Ethan Naf Base Ikeja",
				"Navy Town\nNavy Town Satellite Town, Lagos",
				"Nigeria Bottle Company, Apapa\nNigeria Bottle Company, Apapa, Lagos",
				"Nigerian Bottling Company\nNigerian Bottling Company, Agidingbi, Ikeja, Lagos",
				"Nigerian Breweries, Iganmu\nNigerian Breweries, Iganmu",
				"Nns Quorra\nHarbour Rd, Off Dockyard Rd, Apapa, Lagos",
				"Oando Head Office\nOando Head Office, Victoria Island, Lagos",
				"Oba Akran 1\n37 Oba Akran Avenue, Besides Ap Filling Station, Ikeja, Lagos",
				"Oba Akran 2\n24 Oba Akran Avenue, Ikeja",
				"Oba Akran\n138, Oba Akran Avenue, Ikeja, Lagos",
				"Obalende\nSt Gregory Road Obalende Lagos",
				"Ogba\n21a, Acme Road, Ogba Industrial Layout, Ogba, Lagos",
				"Ogudu\n14b Ogudu Road, Ogudu, Lagos",
				"Ogunlana\nOgunlana Drive, Surulere",
				"Ojo Egbeda\n8 Ojo Egbeda, Alaba International, Lagos",
				"Ojodu\n104 Isheri Road Ojodu, Lagos",
				"Ojodu 2\n26 Isheri Ojodu, Lagos",
				"Okearin 2\n5 Ofin Canal Lagos Island, Lagos",
				"Okokomaiko\n29/31 Badagry Exp Way Okokomaiko, Lagos",
				"Okota\n152 Okota Rd Isolo, Off Apapa-Oshodi Expressway, Dagos",
				"Oluwalogbon House\nPlot A Obafemi Awolowo Way, Ikeja, Lagos",
				"Onipanu\n135 Ikorodu Road Onipanu, Lagos",
				"Oregun\n52 Kudirat Abiola Way, Oregun, Ikeja",
				"Oshodi Expressway\n20a Oshodi/Apapa Expressway, By Charity Bus Stop",
				"Oyin-Jolayemi\nPlot 1662, Oyin Jolayemi Street, Victoria Island, Lagos",
				"Pacesetter\n19 Pacesetter Street Alaba Market, Lagos",
				"Prestige\nPrestige, Gra Ikeja, Lagos",
				"Protea, Lekki\nProtea Hotel, Lekki, Lagos",
				"Seme Border\nSeme Border, Seme",
				"Simbiat\n8 Simbiat Abiola Rd, Ikeja, Lagos",
				"Tinubu\n Plot 357, Kakawa Street, Tinubu, Lagos Island",
				"Total V/I\nTotal Head Office, Victoria Island",
				"Uba House\n57 Marina, Lagos Island",
				"Uba Iganmu\n10, Abebe Village Road, Iganmu, Lagos",
				"Uba Maryland Atm1\n22 Mobolaji Bank Anthony, Maryland, Lagos",
				"Unilag\nUniversity Of Lagos Campus, Akoka,Yaba",
				"Unilever Head Office\n1 Billings Way, Oregun, Ikeja",
				"Us Embassy\nUS Embassy, Victoria Island",
				"Vnaark\nVirgin Nigeria Ark Tower Office, Victoria Island, Lagos",
				"Warehouse Road 2\n46 Ware House Road, Apapa.",
				"Warehouse Road 3\n1 Warehouse Rd, Tas Plaza, Apapa, Lagos",
				"Warehouse Road\n11/13/Ware House Road, Apapa",
				"Western Avenue\n126, Ojuelegba Rd Surulere, Lagos",
				"Wharf Road\nKariko Towers, 9 Wharf Road, Apapa, Lagos",
				"Wharf Road 2\n22 Wharf Road Apapa, Lagos"

		};

		lv = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.ubaSearch);

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
				UBAAtms.this.adapter.getFilter().filter(cs);
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
