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

public class FirstBankAtms extends Activity {

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
		setContentView(R.layout.firstbanksatms);

		// Listview Data
		String atms[] = { 
				"1 Ap/Conoil Road, Ijora",
				"1 Burma Road, Apapa",
				"1 Point Road, Polysonic",
				"1 Wole Ariyo Street Lekki Off Admiralty Road- Lekki Phase1",
				"1, Imam Abibu Adetoro",
				"1, Mafoluku Road",
				"1,Bale Street Ajeromi Ifelodun Local Government",
				"10, Alakija Street, Jibowu",
				"10, Jagunmolu Street, Bariga",
				"111 Alakoro Street, Ebute Ero",
				"112, Nnamdi Azikiwe Street, Idumota",
				"113, Dosu Joseph Road, Badagry",
				"116, Awolowo Road, Ikeja, Lagos",
				"14 A, Olowu Street, Off Toyin Street",
				"1-5 Dockyard Road (Beside Nipco Depot) Apapa",
				"159, Iju Road, Fagba Bus Stop, Iju",
				"177, Lagos Abeokuta Express Road, Iyana Ipaja",
				"182/ 184, Herbert Macaulay Way, Adekunle, Yaba",
				"188, Ikorodu Road, Onipanu",
				"1a, Ozumba Mbadiwe Street, Lagos",
				"2, Abebe Village Road, Iganmu",
				"2, Lagos Road Aiyetoro Market,Epe",
				"2, Ojodu - Isheri Road, Ojodu Berger",
				"2/4 Customs Street, Lagos",
				"205, Idimu Road, Yem-Kem Shopping Plaza",
				"21 - 25, Broad Street",
				"21, Oba Akran Avenue Ikeja Lagos",
				"21/22 Marina Road",
				"214, Broad Street, Elephant House",
				"23 Road By 72 Road Junction, Festac",
				"235 Igbosere Road, Obalende, Lagos",
				"238 Ijegun Road, Toluwani Bus Stop Ijegun",
				"25, Murtala Mohammed International Airport Road",
				"25, Ogba/ Ijaiye Road, Ogba",
				"254 Agege Motor Rd, Oko Oba, Agege, Lagos",
				"261/271 Ojo Road, Off Mile 2, Orile Express Way, Suru Lagos",
				"28, Berkley Street, Obalende, Lagos",
				"29, Ojo-Igbede Road, Alaba Int'l Mkt, Ojo",
				"3, Ago Palace Way, Okota",
				"32, Creek Road, Apapa",
				"39, Ikotun - Idimu Road",
				"4, Keffi Road, S/W, Ikoyi",
				"415 Abeokuta Expressway Oke-Odo Lagos",
				"42/43,Marina,Lagos",
				"42/44 Enitan Street, Off Adeola Street, Aguda, Surulere",
				"43, Kofo Abayomi Avenue, Apapa",
				"440, Lagos Abeokuta Expressway, U-Turn Bus Stop, Abule Egba, Lagos",
				"45, Diya Street, Opposite Total Filling Station, Ifako",
				"46, Oba Akran Avenue",
				"471 Agege Motor Road",
				"50, Olusegun Osoba Rd, Agbado Crossing",
				"51 Church Street, Dosunmu Market",
				"53 Offin Road Street, Oke-Arin",
				"561, Ikorodu Road, Kosofe Bus Stop Near Mile12 Ketu",
				"59/61 Lawanson Road, Surulere",
				"6 Ashabi Cole Street, Agidingbi, Ikeja",
				"60/62, Enu - Owa Street",
				"62, Allen Avenue,Ikeja",
				"63 Akowonjo Rd Baale Bustop, Akowonjo",
				"7, Dada Iyalode Street, Ojuwoye Market, Off Post Office Road, Ilupeju",
				"70/78 Isolo Road, Kudaki Market, Ilewe B/Stop",
				"8, Oke Aro Rd. Oke Aro",
				"8, Old Ojo Road, Maza-Maza",
				"8/ 10, Broad Street",
				"81 Old Abeokuta Road, Agege",
				"82 Wuraola House Allen Avenue",
				"84, Adeniran Ogunsanya Street, Surulere",
				"84/88 Ladipo Street, Mushin",
				"88, Lagos Road, Ikorodu",
				"8a, Adetokunbo Ademola Street, Lagos",
				"901 Abeokuta Express Way, Abucom Bus Stop, Alagbado, Lagos",
				"Adebola House, 40, Opebi Road, Off Allen Avenue",
				"Adonai Plaza, 80, Akowonjo Road, Baale B/Stop",
				"Airport Juction, Agege Motor Road, Lagos-Abeokuta Exp. Way",
				"Ajah Ultra Modern Market",
				"Ajiwe, Ajah, Along Lekki - Epe Expressway",
				"Alaba Int'l Mkt Main Gate",
				"Area H Police Command, Ogudu",
				"Arrival D & E, Murtala Mohammed International Airport",
				"Article Market Complex, Abule Ogun",
				"Bank Road, Customs Ground, Seme Border",
				"Block 13 Suites 1,2,3,4,5&12, Sura Shopping Complex, Simpson Street",
				"Block 3, Chris Efunyemi Onanuga Street, Off Admirality Way",
				"Bmu Complex, Road 5 Nns Way, Navy Town",
				"Chevron Roundabout, Lekki Epe Expressway, Lagos",
				"City Hall, Catholic Mission Street",
				"Daleko Market, Bank Road, Mushin",
				"Deebo Plaza, 6/8, Dopemu - Akowonjo Road, Dopemu Roundabout",
				"Densine Plaza, Electronics Section, Alaba Int'l Mkt",
				"Entrance Gate, Banana Island",
				"Etisalat - Banana Island",
				"Falomo Shopping Complex, Off Awolowo Road, Ikoyi",
				"Federal Secretariat Complex",
				"Golden Tulip Hotel, Festac, Amuwo-Odofin",
				"Ground Floor, E- Center Commercial Avenue, Yaba Lagos",
				"Ikeja Military Cantonment, 9th Mechanised Brigade, Maryland",
				"Ikorodu Shopping Complex, Ita Elewa",
				"Ikota International Market, Great Brand Building, Kilometer 29 Lekki - Epe Expressway",
				"Ikota Shopping Complex",
				"Iwaya Road, Iwaya Lagos",
				"Iyana Isolo Bus Stop, Apapa Oshodi Exp. Way",
				"Kairo Market, Oshodi Road",
				"Kilometer 36, Awoyaya, Lagos - Epe Expressway",
				"Km 16 Lekki-Epe Express Way Lagos",
				"Lagoon Plaza, By Lagos Central Mosque, Nnamdi Azikiwe Street",
				"Lagos State Abattoir Complex, Oko Oba Agege",
				"Lagos State University,Ojo",
				"Laspotech Surulere Campus",
				"Motorways Building, Toll Gate",
				"New Market Road, Oniru Estate, Off Lagos - Epe Expressway",
				"Niger Dock Nig. Plc, Fze, Snake Island",
				"Nigeria Armed Forces Re-Settlement Center Compound , Oshodi",
				"Nigeria Customs Services Premises, Tincan Island Port Command",
				"Niko Best Executive Complex, Trade Fair",
				"Nnpc Satelite Depot, Ejigbo",
				"No 1 Industrial Road, Olodi, Apapa",
				"No 1 Slavage Road, Ebute Metta, Lagos",
				"No 1 Taylor Road, G Cappa, Iddo, Lagos",
				"No 128 Muritala Mohammed Way, Ebute Metta",
				"No 1-5, Odunlami Street. Cms Bus Stop ,Marina",
				"No 17 Itire Road, Surulere",
				"No 17/19 Ijora Cause Way, Ijora",
				"No 197 Agege Motor Road, Mushin",
				"No 21 Mobil Road, Apapa, Lagos",
				"No 23 Commercial Avenue, Sabo Yaba, Lagos",
				"No 29 Tejuosho Road, Tejuosho Yaba, Lagos",
				"No 32b Ilupeju Bye-Pass",
				"No 4, Akpogunma Street Ikate Aguda Lagos",
				"No 53 Balogun Street",
				"No 9 Willoughby Street, Ebute Metta",
				"No. 5 Adeyemo Alakija Street, Victoria Island, Lagos",
				"No.50 Adeola Odeku Street, Victoria Island, Lagos",
				"Oando Service Stattion Badore Epe Express Way - Lagos",
				"Ogun Guangdong Free Trade Zone Igbesa",
				"Ojo Cantonment Barrack",
				"Oniru Palms - (Shoprite - Lekki)",
				"Owode Ibeshe Road,Ikorodu",
				"Plot 136 Ahmadu Bello Way, Beside Silver Bird Galleria, Victoria Island - Lagos",
				"Plot 1637, Adetokunbo Ademola Street, Victoria-Island, Lagos",
				"Plot 1681 Sanusi Fafunwa Street",
				"Plot 2, Block J, Limca Way, Isolo Ind. Estate",
				"Plot 4, Block C Amuwo Odofin, Industrial Layout, Orile Igamu, Lagos",
				"Plot No. 0 - 2, Road 9, Victoria Garden City",
				"Plot No. 104, Oshodi Mile 2 Expressway, Near Cele Bus Stop",
				"Plot No. 2 B Adewunmi Close, Kudirat Abiola Way, Oregun",
				"Plot No. 286, Ajose Adeogun Street",
				"Plot No. 3, Acme Road, Ogba",
				"Progressive Market, Int'l Trade Fair Complex",
				"Shop G6 City Mall Onikan",
				"Shop No. 1& 2, First Floor, Block 2, Agric Market",
				"Tincan Island Port Complex",
				"Trade Fair Complex, Aspamda",
				"University Of Lagos Library Basement, Lagos",
				"University Of Lagos, Akoka, Lagos",

		
		};

		lv = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.firstSearch);

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
				FirstBankAtms.this.adapter.getFilter().filter(cs);
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
		 
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String myString = (String) parent.getAdapter()
						.getItem(position);

				

			}
		});
		*/

	}

	
}
