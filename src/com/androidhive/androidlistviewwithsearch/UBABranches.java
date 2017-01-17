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

public class UBABranches extends ListActivity {
	String [] branches = {
			"Abbatoir\nAbbatoir Complex, Oko Oba Rd. Agege",
			"Abule Egba\n473, Lagos-Abeokuta Express Way, Abule Egba, Lagos",
			"Abule Egba\nAlong Lag/Abek Exp Way Abule Egba",
			"Adeniyi Jones\n51, Adeniyi Jones Street, Ikeja",
			"Adeola Hopewell\nPlot 1607 Adeola Hope-Well Street, V.I",
			"Adeola Odeku\nPlot 1115a Adeola Odeku Street V.I",
			"Adetokunbo Ademola 2\n29, Adetokunbo Ademola Str V.I Lagos",
			"Adetokunbo\nPlot 80a Ademola Adetokunbo Street Vi",
			"Agbara  Estate\nIlaro Road, Agbara Industrial Estate",
			"Airport Road\n62-64 MM International Airport Rd",
			"Ajose Adeogun\nAjose Adeogun, V. I.",
			"Akin Adesola\n11b Akin Adesola Street  V-I",
			"Akowonjo\n49, Akowonjo-Egbeda Rd, Akowonjo",
			"Alaba\n3 Agudosi St Ojo-Alaba Int. Market",
			"Alaba 2\n5  Ojo Alaba Way  Ojo-Lagos",
			"Alaba 3\n6, Agudosi Street Alaba Int’l Market Alaba, Lagos",
			"Alaba 4\nBK Dublin Street, Alaba, Lagos",
			"Alaba 5\nH25/H26 Alaba Int’l Market, Alaba",
			"Alaba 6\nBK 19, Aika Building, Pace-Setter Complex, International Market Alaba, Lagos",
			"Alausa\nWAPCO Alausa  Ikeja",
			"Amodu Tijani\nPlot 697,Amodu Tijani Str,Vi,Lagos",
			"Aocoed(Adeniran Ogunsanya Coll.)\nBadagry Expressway Lagos State",
			"Apapa Ii\n27  Wharf Road  Apapa Lagos",
			"Apapa W-House\n46 Warehouse Road, Apapa",
			"Apapa\n1, Warehouse Road Apapa",
			"Apapa\n22, Wharf Road, Apapa, Lagos",
			"Apapa\n46,  Ware-House Road  Apapa  Lagos",
			"Aspamda Trade Fair 2\nZone A Block 12, Divine Favour Plaza, Aspamda Trade Fair, Lagos",
			"Aspamda  Trade Fair\nOlusegun Obasanjo Hall,Aspamda  Trade Fair",
			"Babs Animashaun Street\nPlot 148, Babs Animashaun Street Surulere, Lagos",
			"Badagry Business Office\nAfrican Church Road Badagry",
			"Bba Brn\nNickok Best Plaza,Bba Tradefair Complex Badagry Expressway",
			"Bba-Tradefair\nAtiku Abubakar Hall  Bba Trade Fair Complex  Lagos",
			"Berger\nApapa Oshodi Expressway, Berger",
			"Bishop Aboyade Cole\nPlot 33 Bishop Aboyade Cole Street V/I Lagos",
			"Bishop Oluwole\n31, Bishop Oluwole Street, Victoria Island",
			"Breadruit\n11-12 Davies St, Lagos Island",
			"Broad Street\n216 Broad Street",
			"Burma\n16 Burma Road Apapa",
			"Central Pavilion\nCentral Pavilion, Olusegun Obasanjo Hall Trade Fair Complex, Lagos",
			"Chevron\n2, Chevron Drive, Lekki Peninsula",
			"Coker Cash Center\nCoker Mrk,Lagos -Badagry  Expressway",
			"Corp & Energy Bank Directorate\nCorporate & Energy Bank Directorate, Head Office",
			"Corporate Branch\nGroup Executive Office, Head Office",
			"Corporate, Marina\nMarine View Plaza, 60, Marina, Lagos",
			"Cpc\n57 Marina Lagos",
			"Daleko\n810-811 Bank Rd Daleko  Mkt. Mushin",
			"Docemo\n21/23, Docemo Street, Lagos",
			"Dolphin Estate\nSuite 69/70/104, Dolphin Plaza, Dolphin Estate, Ikoyi, Lagos",
			"Dopemu 1\n101 Abeokuta Express Way, Dopemu Iyana Ipaja Rd",
			"Dopemu 2\n85, Abeokuta Express Way, Beside Dopemu Bridge",
			"Ebute Metta\n31a  Willoughby Street  Ebute-Metta  Lagos",
			"Ebutemetta 2\n20 Borno Way Ebute/Metta Lagos",
			"Ebute-Metta 4\n35, Apapa Road, Ebute Metta",
			"Ebute-Metta(Oyingbo)\n86 Muritala Muhammed Way,Ebute Metta, Yaba, Lagos",
			"Ejigbo 2\n133, Ejigbo - Ikotun Road, Ejigbo, Lagos State, Nigeria",
			"Ejigbo\nPPMC Depot  Ejigbo-Lagos",
			"Elephant Cement House\nElephant Cement House, Central Business District, Alausa Ikeja, Lagos",
			"Falomo\n172 Awolowo Way Falomo",
			"Festac Ii(New)\n23 Road Junction  Festac Town  Lagos",
			"Festac\n21 By 72 Road Junction, Festac",
			"Fola Agoro\n366, Herbert Macaulay Rd. Yaba",
			"Foundation\n15th Floor Uba House 57 Marina Lagos",
			"Gbagada – Ifako\n38 Ajayi Aina Street Gbagada-Ifako",
			"Gbagada Expressway\n285, Gbagada Expressway, Gbagada",
			"Global Markets\n11th Floor, Uba House,57 Marina Street, Lagos",
			"H/O Branch\n42a Afribank Street, Victoria Island",
			"Ibafon\n12, Oshodi Apapa Expressway, Ibafon, Lagos State, Nigeria",
			"Iddo\n1  Taylor Road  (G-Cappa) Iddo  Lagos",
			"Idowu Taylor 2\n1 Idowu Taylor Street Victoria Island",
			"Idowu Taylor\nPlot 22b Idowu Taylor Street V.I",
			"Idumagbo\n43, Idumagbo Avenue Off Nnamdi Azikiwe Street, Lagos Island, Lagos.",
			"Idumota 2\n28 Obun Eko Street Idumota Lagos",
			"Idumota 3\n144 Namdi Azikwe St",
			"Idumota 4\n21 Obun Eko Str. Idumota",
			"Idumota 5\nNo 75 Enu- Owa Street Idumota",
			"Idumota\n169  Nnamdi Azikiwe St. Idumota  Lagos",
			"Iganmu\n10, Abebe Village Rd Iganmu",
			"Igbosere\n12/14, Broad Street, Lagos Island",
			"Ijaiye Ogba\n50,Ijaiye Road Ogba Lagos",
			"Ijora\n10, Ijora Causeway, Ijora, Lagos",
			"Iju Road\n96, Iju Road, Ifako-Ijaye",
			"Ikeja 1\nOluwalogbon House, Plot A. Obafemi Awolowo Way, Ikeja",
			"Ikeja Computer Village\n9, Oremeji St, Off Kodesoh Road, Ikeja, Lagos",
			"Ikeja G.R.A.\n39, Joel Ogunnaike Street, Ikeja",
			"Ikeja 2\n73  Allen Avenue  Ikeja",
			"Ikeja(Wu)\nAwolowo House 29/31 Obafemi Awolowo Way Ikeja",
			"Ikeja3\n1, Simbiat Abiola Road, Ikeja, Lagos",
			"Ikeja4\n13 Allen Avenue Ikeja Lagos",
			"Ikorodu 108 Lagos Rd.\n108 Lagos Rd Haruna Bus Stop",
			"Ikorodu Road\n300 Ikorodu Road, Anthony Bus-Stop, Lagos",
			"Ikorodu Town\n53, Lagos Road Ikorodu",
			"Ikosi – Ketu\n51 Ikosi Road  Ketu  Lagos",
			"Ikota Vgc\nIkota Shopping Complex, Victoria Garden City, Lekki",
			"Ikotun 1\nOld Alimosho Str Ikotun/Igando Lga",
			"Ikotun 2\n74,Ikotun-Idimu Road,Arida Bus Stop,Ikotun, Lagos State",
			"Ilupeju\n15, Industrial Avenue, Ilupeju",
			"Ipaja Nysc\n241 New Ipaja Rd, Near Nysc, Iyana Ipaja Lagos",
			"Iponri Shopping Complex\nIponri Shopping Complex, Iponri, Lagos",
			"Isolo\n10, Isolo Way Isolo Lagos",
			"Itire Road\nItire Road",
			"Kariko Towers\n9, Wharf Road, Kariko Towers, Apapa, Lagos",
			"Ketu\n549 Ikorodu Rd. Mile 12 Ketu",
			"Lagos  Central\n97-105 Broad Street",
			"Lagos  East\n12-14 Broad St.",
			"Lasu\nLasu Ojo Campus Ojo Town Lagos",
			"Lawanson\n66, Lawanson Road, Itire Surulere Lagos.",
			"Lekki Oba Elegushi Int’l Mkt\nOba Elegushi Int’l Mkt Jakande Estate",
			"Lekki Phase 1\n80a Admirality Way,Lekki Phase 1",
			"Lekki(2) Protea\nKm 18, Lekki-Epe Expressway, Near Protea Hotel, Lekki Peninsula, Lagos",
			"Lekki\nkm 26 Ajah Express Road, Lekki",
			"Luth\nroad 1  Luth Premises  Idi-Araba  Lagos",
			"Marina West\n53 Marina Street, Lagos",
			"Marina\n55, Marina  Street , Lagos",
			"Martins\n15, Martins Street, Lagos",
			"Maryland\n22, Mobalaji Bank Anthony Way Lagos",
			"Matori Ii\nCortex Plaza  Plot 126, Ladipo Street, Mushin, Lagos",
			"Matori\nNucleus House, 21a Fatai Atere Way  Matori  Lagos.",
			"Mmia Arrival Hall\nMurtala Mohammed Int'l Airport, Arrival Terminal, Ikeja",
			"Mobil House\nMobil House, 1 Lekki Expressway, Victoria Island, Lagos",
			"Mushin\n128  Isolo Road  Mushin-Lagos",
			"Naf Base, Ikeja\nSam Ethnan Naf Base, Ikeja, Lagos",
			"Navy Town\n2nd Avenue, Navy Town",
			"Necom\n15, Marina, Necom Marina, Lagos",
			"New Isheri Rd.\n26 New Isheri Rd Akiode Bus Stop, Ojodu",
			"Nns Quorra\nHarbour Road Off Dockyard Road, Apapa, Lagos",
			"Oba Akran 2\n24, Oba Akran Avenue, Ikeja, Lagos",
			"Oba Akran 3\n138, Oba Akran Avenue, Ikeja, Lagos",
			"Oba Akran\n37  Oba Akran Avenue  Ikeja  Lagos",
			"Obalende\n1 St Gregory Road, Obalende",
			"Ogba\n21a Acme Road, Ogba-Ikeja, Lagos",
			"Ogudu\n14b, Ogudu Road, Ogudu G.R.A.",
			"Ogunlana Drive\n154, Ogunlana Drive Surulere",
			"Ojodu\nplot 104, Ojodu-Isheri Road, Beside Mr. Biggs, Ojodu, Lagos",
			"Ojo-Igbede Road\n8, Ojo-Igbede Road",
			"Ojuelegba 2\n81, Ojuelegba Road, Surulere, Lagos",
			"Ojuelegba 3\n123  Ojuelegba Road  Surulere  Lagos",
			"Ojuelegba\n57, Ojuelegba Rd. Tejuosho Yaba",
			"Oke Arin 1\n23 Oke-Arin Street, Lagos Island",
			"Oke Arin 2\n20, Ijaiye Road, Off John Street, Okearin, Lagos",
			"Oke Arin\n5, Ijaye Street, Lagos.",
			"Oke-Arin 3\n33 Issa Williams Street Oke-Arin Lagos",
			"Okokomaiko 2 (Iyana Isashi)\nplot 1004, Mascot House, Iyana Isashi, Lagos",
			"Okokomaiko\n29/31 Badagry Exp Way Okokomaiko, Lagos",
			"Oko-Oba\n25/27 Charity Road, Oko Oba, Agege",
			"Okota\n152, Okota Road, Isolo",
			"Onipanu\n135, Ikorodu Road, Lagos",
			"Oregun 2\n56, Kudirat Abiola Way, Oregun, Ikeja, Lagos",
			"Oregun\n16b  Oregun Rd",
			"Orile Coker\nLagos-Badagry Exp. Rd, Coker Orile, Lagos",
			"Oshodi\n20a Oshodi Apapa Express Way",
			"Osolo\n10  Osolo Way Ajao Estate",
			"Otta\nIdiroko Rd Otta",
			"Oyin Jolayemi\nPlot 1662 Oyin Jolayemi Street  V-I  Lagos",
			"Palm Avenue\n20, Palm Avenue, Mushin, Lagos",
			"Palmgrove\n220, Ikorodu Rd. Palmgrove",
			"Passport Office\nPassport Office, Festac Town, Lagos State, Nigeria",
			"Prestige  Apapa\n9, Point Road, Apapa",
			"Redeemed Camp\nRedemption Camp",
			"Retail Financial Services\n57, Marina Lagos (Head Office)",
			"Seme Border\nLagos-Badagry Exp Way, Seme Border Post",
			"Simpson\n85, Simpson Street, Ebute Metta, Lagos",
			"Surulere\n20  Bode Thomas Street  Surulere",
			"Surulere\n68 Western Avenue",
			"Tinubu\n5/7 Kakawa Street,Lagos",
			"Trade Fair\nTrade Fair Complex  Lagos",
			"Tradefair\nhall 2 Trade Fair Complex Badagry Way Lagos",
			"Uba Custody\n9th Floor Uba House 57 Marina Lagos",
			"Uba House\n35 Broad Street,Lagos",
			"Unilag\nUniversity Of Lagos, Akoka",
			"Victoria Island\n19 Adeyemo Alakija Street Victoria Island Lagos",
			"Victoria Island\nPlot 1637 Adetokunbo Ademola, V I",
			"Warehouse Rd\n11-13 Warehouse Rd, Lagos",
			"Wharf Rd\n300, Wharf Road, Apapa, Lagos"
					
			
				
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
	setContentView(R.layout.ubabranches);
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
			UBABranches.this.adapter.getFilter().filter(cs);
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
