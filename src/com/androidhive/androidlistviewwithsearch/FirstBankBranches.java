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

public class FirstBankBranches extends ListActivity {
	String [] branches = {
			/*
			 * List of First Bank branches in Lagos State
			 */
			"Abattoir Cash Centre\nLagos State Govt. Abattoir, Oko-Oba, Agege, Lagos State",
			"Abibu Adetoro Branch\n51 Abibu Adetoro St, Off Ajose Adeogun St, Victoria Island, Lagos",
			"Abibu-Oki Branch\nA.G. Leventis Building, 42/43 Marina",
			"Abule Egba Branch\n440, Lagos Abeokuta Expressway, U-Turn Bus Stop, Abule Egba, Lagos State",
			"Adekunle Branch\n182/184 Herbert Macaulay Way, Yaba",
			"Adeola Odeku Branch\n15B Adeola Odeku Street, Victoria Island, Lagos, State",
			"Adetokunbo Ademola Branch\n8, Adetokunbo Ademola Street, V/I, Lagos",
			"Agege Branch\n254, Agege Motor Rd, Oko-Oba, Agege, Lagos",
			"Agege Cash Centre\n27, Abeokuta, Motor Road, Agege",
			"Agidingbi Branch\n6, Asabi Cole Road, Off Lateef Jakande Way, Agidingbi, Ikeja",
			"Ajah Branch\nAjiwe, Ajah, Along Lekki-Epe Expressway, Lagos",
			"Ajah Cash Centre\nAjah Market, Eti Osa L.G.A, Lagos",
			"Ajao Estate Branch\n25 Murtala Mohammed International Airport Road, Ajao Estate",
			"Akowonjo Branch\nAkowonjo Road, Akowonjo",
			"Alaba Int’l Market Branch\n29, Ojo-Igbede Rd. New Alaba, Lagos",
			"Alaba Int’l Market Cash Centre\nDensine Mall, Dobbil Avenue, Alaba Int’l Market, Alaba, Lagos",
			"Alaba Rago Mkt. Cash Centre\nAlaba Rago Market, Alaba Rago, Lagos-Badagry Express Way",
			"Alaba Suru Branch\n269/271 Ojoo Road, Off Mile 2-Orile Expressway Road, Lagos",
			"Alausa Branch\nMotorways Building, Toll Gate, Alausa, Lagos",
			"Aliko Cement Terminal Cash Centre\nAliko Dangote Cement Depot, Abule Oshun, Via Satellite Town",
			"Apapa Branch\n1 Burma Road, Apapa",
			"Article Market Cash Centre\nArticle Dealers Association. (ADA), Shopping Complex, Opp. Int’l, Trade fair, Lagos-Badagry Expressway, AbuleOshun, Ojo",
			"Awolowo Road Branch\n116 Awolowo Road, Ikoyi, Ikoyi-Lagos",
			"Badagry Branch\n113 Joseph Dosu Way, Old Lagos Road, Badagry-Lagos",
			"Badore Branch\nOando Service Station by Coca Cola Bus Stop, Ajah-Badore Road, Badore, Off Ajah, Lekki, Lagos",
			"Bariga Branch\n10, Jagunmolu Street, Bariga",
			"Broad Street Branch\n214 Broad Street, (Elephant House), Lagos",
			"Chevron-Texaco Branch\nAlong Chevron Drive, Chevron Complex, Lekki, Lagos",
			"Coker Branch\nPlot 4 Block C, Amuwo Odofin Ind.Layout, Orile Iganmu, Lagos",
			"Creek Road Branch\n32, Creek Road, Apapa, Lagos",
			"Daleko Market Branch\nDaleko Market, Bank Road, Mushin, Lagos",
			"Domino Cash Centre\n1–11 Commercial Avenue, Sabo Yaba, Lagos",
			"Dopemu Branch\nDeebo Plaza, 618, Dopemu-Akowonjo Road, Dopemu Roundabout, Dopemu, Lagos",
			"Ebute Metta Branch\n1 Savage Street, Apapa Road, Ebute Metta",
			"Eko Hotel Branch\nCity Express building, Plot 1637, Adetokunbo Ademola, Victoria Island",
			"Enu-Owa Cash Centre\n62, Enu-Owa Street, Lagos",
			"Falomo Shopping Centre Branch\nAwolowo Road, Ikoyi",
			"Fed. Secretariat Branch\nFederal Secretariat Complex Ikoyi, Lagos",
			"Festac Branch\n32 Road, Festac Town, Lagos",
			"Iddo Market Branch\n1, Taylor Road, Iddo",
			"Idimu Branch\n205, Idimu Road, Yem-Kem Shopping Plaza, Agege, Lagos State",
			"Ifako Gbagada Branch\n45 Diya Street, Opposite Total Filling Station, Ifako, Gbagada",
			"Iganmu Branch\n2 Abebe Village Road, Iganmu",
			"Ijora Branch\n23-25 Ijora Crossway, Apapa",
			"Iju Branch\n159 Iju Road by Fagba B/Stop,Iju Agege",
			"Ikeja Allen Avenue Branch\nAllen Avenue, Ikeja",
			"Ikeja Ind. Estate Branch\n21, Oba Akran Avenue, Ikeja",
			"Ikeja Military Cantonment Agency\nIkeja Military Cantonment, 9th Mechanised Brigade, Maryland, Ikeja",
			"Ikorodu Branch\n88 Lagos Road, Ikorodu",
			"Ikota Branch\nIkota Int’l Market. Ikota Shopping Complex, Victoria Garden City, Falomo",
			"Ikota Main Branch\nGreat Brand Building, KM 24, Lekki-Epe Expressway",
			"Ikotun Branch\n39, Ikotun-Idimu Rd, Ikotun",
			"Ilupeju Branch\nIlupeju Bye-Pass, Ikeja",
			"Int’l Trade Fair Complex II (Balogun) Cash Centre\nNIICO Best Executive Plaza, Opp. Atiku Hall, In’tl Trade Fair Complex, Mile 2-Badagry Expressway, Lagos",
			"International Trade Fair Complex Branch\nWing B, Hall 2, Hexagon 9, Int’l Trade Fair Complex, Badagry Express Road, Festac Town, Lagos",
			"Investment House Branch\n21–25 Broad Street, Lagos",
			"IPMAN Cash Centre\n1–15 Dockyard Road, Apapa, Lagos",
			"Isolo Branch\nApapa/Oshodi Express Way, Iyana Isolo, Mushin",
			"Isolo Industrial Estate Branch\nLimca Way, Ilasamaja, Off Apapa-Oshodi Express Way. Lagos State",
			"Ita Elewa Ikorodu Branch\nIkorodu Shopping Complex, Ita Elewa",
			"Iyana Ipaja Branch\n177 Lagos Abeokuta Express Rd., Iyana Ipaja, Lagos",
			"Jibowu Branch\n10, Alakija Street, Jibowu, Yaba, Lagos",
			"Kairo Market Cash Centre\nOshodi Road, Oshodi",
			"Keffi Branch\n4, Keffi Street, Ikoyi, Lagos",
			"Ketu Branch\n561 Ikorodu Road, Mile 12, Ketu, Lagos",
			"Kofo Abayomi Branch\n43 Kofo Abayomi Avenue, Apapa",
			"Lagos State University Cash Centre\nLASU Main Campus, Badagry",
			"Lapal House Branch\n235, Igbosere Road, Obalende, Lagos",
			"Lawanson Branch\n59/61 Lawanson Road, Surulere, Lagos",
			"Lekki Branch\nBlock 90o, Chris Efunyemi Onanuga Street, Off Admirality Way, Lekki Phase 1",
			"M.M. Int’l Airport Branch\nM.M Airport Complex, Ikeja",
			"M.M. Way Branch\n128 Murtala Moh’d Way, Ebute-Metta",
			"Marina Branch\n35 Marina, P.O. Box 2006, Lagos",
			"Matori Branch\n84/88 Ladipo Street, Papa Ajao, Mushin",
			"Mayfair Gardens Branch\nKM 36 Awoyaya, Lagos/Epe Expressway, Lekki Peninsula, Lagos",
			"Mazamaza Branch\n8, Old Ojo Road, Mazamaza, Lagos",
			"Mobil Road Branch\n21 Mobil Road, P.M.B. 1180, Apapa",
			"Moloney Branch\n28 Berkley Street, Lagos",
			"Mushin Branch\n197 Agege Motor Road, Lagos",
			"N.1.J. House Branch\n5 Adeyemo Alakija Street, Victoria Island, Falomo",
			"Navy Town Branch\nB.M.U. Complex (Road 8), Navy Town, Festac Town",
			"Niger House Branch\n1/5 Odunlami Street, Lagos",
			"NNPC Ejigbo Cash centre\nNNPC Ejigbo Depot",
			"Oba Akran Branch\n46, Oba Akran Avenue, Ikeja",
			"Obun-Eko Branch\n112 Nnamdi Azikiwe Street, Idumota, P.O. Box 2353, Lagos",
			"Odun Ade Cash Centre\nShop 1&2, First Floor, Block 2, Agric, Odun Ade, Coker",
			"Ogba Branch\nPlot 7, Block C, Acme Road, Ikeja",
			"Ogudu Agency\nBanking on Wheels Van, Opposite Area H, Ogudu Area Command, Ogudu",
			"Ojo Cantonment Agency\nOjo Military Cantonment, Ojo, Lagos-Badagry Exp. Road, Lagos",
			"Ojodu-Isheri Branch\n2, Ojodu-Isheri Road, Ojodu Berger, Ikeja, Lagos",
			"Ojuwoye Cash Centre\n7, Dada Iyalode Street, Ojuwoye Market, Off Post Office Road, Mushin Lagos",
			"Oke-Arin Market Branch\n53 Offin Road, Lagos",
			"Oke-Odo Branch\n415, Abeokuta Expressway, Ile-Epo Bus Stop, Agege, Lagos",
			"Okota Branch\n3, Ago Palace Way, Okota, Lagos",
			"Oniru Market Branch\nOniru Market, Oniru Estate, Lagos/Epe Expressway, Lekki Peninsula, Lagos",
			"Opebi Branch\nAdebola House, 40, Opebi Road, Off Allen Avenue, Ikeja-Lagos",
			"Oregun Ind. Estate Branch\nPlot 2B Adewunmi Close, Ikeja",
			"Osapa London Branch\nKilometer 7, Lekki-Epe Expressway, Osapa-London, Lekki",
			"Oshodi Branch\n471 Agege Motors Road, Oshodi",
			"Oshodi Cantonment Agency\nc/o Ilupeju Branch, Ikeja, Lagos",
			"Oshodi-Mile 2 Expressway Branch\nPlot 104 Oshodi Mile 2 Expressway,Near Cele Bus-stop, Lagos",
			"Owode Branch\nIbeshe Road, Ikorodu",
			"Progressive Market Branch\nAssociation of Progressive Traders Plaza",
			"Sanusi Fafunwa Branch\nPlot 1681, Sanusi Fafunwa Street, Victoria Island, Lagos",
			"Saudi Eko Branch\nLagoon Plaza by Lagos Central Mosque, Nnamdi Azikiwe Str, Lagos Island",
			"Seme Border Branch\nNigeria Customs Ground, Seme Border",
			"Shell Agency\nShell Petroleum & Dev. Company, Freeman House, Marina",
			"Shomolu Branch\n188, Ikorodu Road, Onipanu, Shomolu",
			"Snake Island Cash Centre\nNiger Dock Premises",
			"Stock Exchange House Branch\nCustoms Street, Lagos",
			"Sura Cash Centre\nBlock 13 Sura Shopping Centre, Simpson Street, Lagos",
			"Surulere Aguda Branch\n42/44 Enitan Street, Surulere, Lagos",
			"Surulere Branch\n17 Itire Road, Surulere",
			"Surulere S/Centre Branch\n84 Adeniran Ogunsanya St, Surulere",
			"Tejuosho Branch\nNo. 29 Tejuosho Street, Yaba",
			"Tin Can Island Branch\nTin Can Island Port Complex, Off Apapa/Oshodi Express Road, Apapa",
			"Toyin Olowu Branch\n14A, Olowu Street, Off Toyin Street, Ikeja, Lagos",
			"Trinity Branch\nOlodi-Apapa, No. 1, Industrial Road, By Trinity Police Station, Olodi Apapa, Lagos",
			"University of Lagos Branch\nUnilag Consult Building, Commercial Avenue/Ransome Kuti Road Junction,Opposite International School, Unilag, Lagos",
			"Western House Branch\n8/10 Broad Street, Lagos",
			"Willoughby Branch\n9A Willoughby Street, Ebute Metta",
			"Yaba Branch\n322 Herbert Macaulay Street, Yaba"

				
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
	setContentView(R.layout.firstbankbranches);
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
			FirstBankBranches.this.adapter.getFilter().filter(cs);
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
