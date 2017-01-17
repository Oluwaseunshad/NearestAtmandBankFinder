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

public class FCMBAtms extends Activity {

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
		setContentView(R.layout.fcmbatms);

		// Listview Data
		String atms[] = { 
				
				"Adeniran Ogunsanya\n33 Adeniran Ogunsanya Street Surulere, Lagos State",
				"Adeola Hopewell\n38, Adeola Hopewell Street, Victoria Island, Lagos",
				"Adeola Hopewell 2\nBanana Island, Ikoyi, Lagos State",
				"Adeola Odeku - The Place\nIGI Pension Office, Beside Globe Motors On Adeola Odeku Road",
				"Adeola Odeku\n11b, Adeola Odeku Street Victoria Island Lagos",
				"Adetokunbo\nPlot 719 Adetokunbo Ademola Street, Victoria Island, Lagos",
				"Admiralty\n12e Plot B Waterfront Plaza,Admiralty Way Lekki Phase 1-Lekki",
				"Agege\nOld Abeokuta Expressway, Tabon, Agege, Lagos State",
				"Airport Rd\nPlot 23/25 Muritala Mohammed Airport Road  Ajao Estate Isolo Lagos",
				"Ajah\nKm 23 Berger Busstop Lagos Epe Expressway",
				"Ajose Adeogun\nPlot 273a,Ajose Adeogun Street,Victoria Island Lagos",
				"Akin Adesola\nOperating Under Ajose Adeogun",
				"Akowonjo\nShasha Akowonjo Round-About, Akowonjo, Egbeda Lagos State",
				"Alaba\nObosi Plaza,Alaba Int'l Market,Alaba",
				"Alagbado\n757, Lagos Abeokuta Expressway, Ap Service Station Salolo Bus Stop Alagbado",
				"Allen\n103 Allen Avenue Ikeja, Lagos",
				"Allen\n36, Allen Avenue Ikeja Lagos",
				"Apapa Creek Road\n1-3 Nnewi Building, Creek Road, Apapa, Lagos",
				"Apapa Warehouse\n16, Warehouse Road, Apapa, Lagos",
				"Apapa\n28 Creek Road Apapa",
				"Aspamda\nOlusegun Obasanjo Hall, Aspamda. Trade Fair Complex, Badagry Expressway, Lagos",
				"Awolowo Road\n68 Awolowo Road, Ikoyi Lagos",
				"Bba Tradefair\nAbove Plaza Bba Trade Fair Badagry Express Way Lagos",
				"Broad Street\nBanuso House, 88/89 Broad Street Lagos",
				"Commercial Road\n24, Apapa Commercial Road, Apapa, Lagos",
				"Computer Village Mini Branch\n17, Oremeji Street, Computer Village Lagos",
				"Cpc\n10,University Road, Akoka, Yaba",
				"Davies\n1 Davies Street, Untl Buildingoff Marina, Lagos Island, Lagos",
				"Festac\nPlot 1572 4th Avenue Festac Town Lagos",
				"Gra Ikeja\n48/50 Isaac John Street, Ikeja, Lagos",
				"Iddo\nAg Leventis House 2-4 Iddo Road, Iddo Lagos",
				"Idimu\n218, Egbeda Idimu Road Faith B/Stop, Opp Yemkem Int'l Plaza Idimu-Lagos",
				"Idowu Taylor\nOkoi-Arikpo House, 5,Idowu Taylor Street Victoria Island, Lagos",
				"Idumagbo\n34, Idumagbo Avenue (Daddy Doherty's House) Lagos",
				"Idumota\n22, Idoluwo Street Idumota Lagos",
				"Ikeja\n29, Oba Akran Avenue Ikeja, Lagos",
				"Ikorodu\n7 Lagos Road, Ikorodu, Lagos State",
				"Ikoyi\n154 Awolowo Road, Ikoyi, Lagos",
				"Ilupeju\n25b Ilupeju By Pass, Ilupeju, Lagos",
				"Iponri\nShop 529/531 Abibat Mogaji Shopping Complex,Iponri Market,Iponri,Lagos State",
				"Joseph Street\n2 Joseph Street, Lagos Island, Lagos",
				"Ketu\n545/547 Ketu,Ikorodu Road, Lagos",
				"Ladipo\n122/124 Ladipo Street, Beside Ap Filling Station, Ladipo, Mushin Lagos",
				"Lekki Chevron\nKm 18,Lekki-Epe Expressway, Before Chevronrounabout, Lekki, Lagos",
				"Lekki\n63/64 Igbokushu Village Opp Jakande Estate Lekki",
				"Lekki\nBlue Island Hotel, Lekki-Epe Expressway",
				"Local Airport\nMMA, Local Airport, Lagos",
				"Macarthy\n10/12 Macarthy Street, Onikan, Lagos",
				"Marina\n44, Marina, Lagos",
				"Matori\n91 Ladipo Street, Matori Lagos",
				"Mazamaza\n15 Sikiru Otunoba Street,Mazamaza, Lagos",
				"Mikano Office\nOgba Mikano Office Ogba Lagos",
				"Mobolaji Bank Anthony Way\n18-20 Mobolaji Bank Anthony Way,Ikeja",
				"Motorways\nM1 Point Motorways Complex Ikeja, Lagos State",
				"Mushin\n253 Agege Motor Road, Lagos State",
				"Oba Akran\n34 Oba Akran Road, Ikeja, Lagos",
				"Ogba\n23 Ogba Ijaiye Road Opp. Waec Ogba, Lagos",
				"Ogudu\nPlot 111 Ogudu Gra,Ojota Road, Ogudu, Lagos",
				"Oke Afa\n6 Jakande Estate Road, Oke Afa, Isolo, Lagos",
				"Oke Arin\n11, Ijaiye Street Oke Arin Lagos",
				"Okota\n117 Okota Road, Opposite Phcn Okota, Lagos",
				"Okota\n88, Ago Palace Way, Okota, Lagos State",
				"Onipanu\n178, Ikorodu Road, Onipanu, Lagos",
				"Oregun\n80 Kudirat Abiola Way, Oregun, Ikeja, Lagos",
				"Orile Coker\nBlock 11, Suites 3-8 Agric Market,Orile Coker Lagos",
				"Oroyinyin Street\n12 Oroyinyin Street, Idumota, Lagos Island, Lagos",
				"Osolo Way\n33 Osolo Way, Ajao Estate, Lagos",
				"Oyin Jolayemi\nPlot 1661 Oyin Jolayemi Street Victoria Island, Lagos",
				"Palms\nThe Palms Shopping Mall Lekki - Epe Expressway Lekki, Lagos",
				"Randle Road\n10, Randle Road, Apapa, Lagos",
				"Sanusi Fafunwa\n2, Sanusi Fafunwa Street, V/Island, Lagos",
				"Shomolu\n31 Shipeolu Street, Onipan Shomolu, Lagos",
				"Surulere\n57, Ogunlana Drive, Surulere, Lagos State",
				"Toyin Street\n29 Toyin Street, Ikeja, Lagos",
				"Wharf Road\nEleganza Plaza Wharf Road Lagos",
				"Yaba\n43 Ojuelegba Road Yaba"
		};

		lv = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.fcmbSearch);

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
				FCMBAtms.this.adapter.getFilter().filter(cs);
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
