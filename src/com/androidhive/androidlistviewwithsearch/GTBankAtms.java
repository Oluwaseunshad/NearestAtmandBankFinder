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

public class GTBankAtms extends Activity {

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
		setContentView(R.layout.gtbanksatms);

		// Listview Data
		String atms[] = { 
				
				"Abule Egba\n402, Lagos-Abeokuta Expressway, Abule-Egba, Lagos State, Nigeria",
				"Adeniyi Jones\n31, Adeniyi Jones Avenue, Ikeja, Lagos State, Nigeria",
				"Adeola Odeku Branch\n56a Adeola Odeku Street",
				"Adetokunbo Ademola Branch\nPlot 714 Adetokunbo Ademola Street",
				"AFSS\n1 Campground Road,Anthony Village",
				"Ajah Branch\nKm 22, Lekki-Epe Expressway",
				"Ajose Adeogun Branch\n279, Ajose Adeogun street",
				"Akowonjo Branch\n35, Shasha Road, Akowonjo",
				"Alausa\nTechnical Reference Centre, Alausa Secretariat, Ikeja, Lagos State",
				"Allen\n80a Allen Avenue",
				"Anthony Village\n7 Anthony Road, Anthony Village",
				"APM Terminal\nContainer Terminal Apapa Port, Warf Road, Apapa",
				"Arakan Barracks\nCommand Officers Mess, Arakan Barracks off, Park Lane",
				"Aspamda\nAspamda Plaza, Ari Zone A, Trade Fair Complex",
				"Berachah Microfinance Bank",
				"Berger Paints\nBerger Paint House, Oba Akran Road",
				"Boulos Enterprise\nBoulos Enterprise",
				"BRASS LNG\nPlot 1680, Sanusi Fafunwa Street",
				"British Airways\nThe Water Front, Oyinkan Abayomi Drive",
				"Broad Street\n81/86 Broad Street Road",
				"Burma Road\n17 Burma Road",
				"Catholic Mission Branch\n22/24 Catholic mission street",
				"Celtel Call Centre\nOpposite Nigeria Breweries",
				"Chevron Drive\nBlock LXXIV A, Ojomu land, Beside Chevron Roundabout, Lekki/EpeExpressway, Lagos State, Nigeria",
				"Chevron Gbagada\nChevron",
				"Chevron HQ\nCheveron HQ, Lekki",
				"City Mall Onikan\nCity Mall Onikan",
				"CocaCola, Ikoyi\nPemberton Place #16, Gerrard Road, Ikoyi",
				"Commercial Road, Apapa\nDoyin House, 4 Commercial Road",
				"Computer Village, Ikeja\n5 Oshitelu Street, Computer Village",
				"Creek Road, Apapa\n35, Creek Road, Beside Thisday Newspaper",
				"Diya Gbagada E-branch\nDiya Street, Ifako, Gbagada",
				"Ebeano Supermarket\nPlot 1B, Block A9, Obafemi Anibaba Street Lekki Phase 1",
				"e-Branch\nE-Branch 56A, Adeola Odeku Street",
				"Egbeda Idimu\n26-28, Akowonjo Road, Egbeda, Lagos",
				"EKO Hotels\nEko Hotel and Suites",
				"Eterna Oil\nEterna Oil Pen Cinema Agege",
				"ETISALAT Mushin\n195, Lagos, Mushin, 324 Agege Motor Road Challenge Mushin",
				"Etisalat\nEtisalat Building, Banana Island",
				"FAAN\nFAAN Ikeja Lagos",
				"Falomo Ikoyi\nGTBank MTN Golden Plaza Falomo",
				"Festac Branch\n2nd Avenue, 301 Road, House 11",
				"Freisland Foods\nFreisland Foods",
				"Gbagada Branch\n14 Diya street",
				"Golden Plaza\nMTN Golden Plaza Ikoyi",
				"Golden Tulip\nGolden Tulip Hotel Festac Lagos",
				"Grimaldi\nMarble Building Grimaldi Agency Nigeria Ltd PTML Port Tincan Island, Apapa",
				"GT Assets\nGT Assets, Karimu Ikotun Street",
				"GTA Onipanu\nMasard Building, Onipanu, Ikorodu Rd.",
				"GTA\nGuaranty Trust Assurance, Aboyade Cole Street",
				"GTA\nSanta Clara Court, Plot 1412, Ahmadu Bello Way",
				"GTExpress Boundary\nBoundary Bustop, Ajegunle, Apapa, Lagos",
				"GTExpress Campos\nCampos Sqaure, Lagos Island",
				"GTExpress Costain\nOld Apapa Road, Costain, Lagos",
				"GTExpress Ebeano\nEbeano Supermarket, Oniru/Lekki Express Way",
				"GTExpress Festac\n21 Road, Festac Town, Lagos",
				"GTExpress Ikeja\nMM2, Local Airport Road, Ikeja",
				"GTExpress Ikorodu\n2, Sagamu Road, Ikorodu",
				"GTExpress Ikoyi\nBank Road, By Federal Secretariat Ikoyi, Lagos",
				"GTExpress Jebba\nJebba Street, Ebute Meta, Lagos",
				"GTExpress Kingsway\nBeside Tantalizers, Kingsway Road, Apapa, Lagos",
				"GTExpress Mile2\nOshodi-Apapa Expressway, Mile 2, Lagos",
				"GTExpress Mushin\nOlorunsogo Busstop, Mushin, Lagos",
				"GTExpress Shomolu\nOnipanu, Ikorodu Expressway, Shomolu",
				"GTExpress Western Avenue\nWestern Avenue, By Mosalasi/Mushin Ojuelegba",
				"GTExpress Wharf\nBeside Area B Police Station, Wharf Road, Apapa, Lagos",
				"GTHomes\n28 Saka Tinubu Street",
				"Guinness Ogba Plant\nGUINNESS Brewery",
				"Ibafon Branch\n1 Dele Bakare St,Olodi Apapa",
				"Idi Oro Branch\n110, Agege Motor Road, Idi Oro, Mushin",
				"Idimu Rd, Egbeda\n231, Egbeda Idimu Road, Alimosho L.G.A",
				"Idumota Branch\n134 Nnamdi Azikwe Street, Idumota, Lagos Island",
				"Ikeja Cantonement\nATM Gallery at Ikeja Cantonment, Mobolaji Bank Anthony way, Maryland, lkeja, Lagos State",
				"Ikeja General Hospital\nIkeja General Hospital Ikeja",
				"Ikorodu Branch\n47, Lagos Road, Ikorodu Town",
				"Ikosi\nPlot A3C, Ikosi Road, Oregun, Industrial Estate",
				"Ikota Shopping Complex, Ikota",
				"Ikoyi Branch\n178 Awolowo Road",
				"Ikoyi Club\n6 Ikoyi Club 1938 Road, Ikoyi",
				"Ilupeju Branch\n48, Town Planning Way",
				"Ilupeju Bypass\nIlupeju Bypass Road, Ilupeju",
				"International Airport\n15 International Airport Road",
				"Isolo Branch\nIsolo Road off Abimbola Road near Limca",
				"Ketu Branch\n570, Ikorodu Road",
				"KPMG\nKPMG Tower Bishop Aboyade Cole Street Victoria Island",
				"Lagos Business School\nLagos Business School (LBS) Ajah",
				"Lagos House of Assembly\nLagos State house of Assembly Ikeja",
				"LASU\nLagos State University, Ojo",
				"LAWMA Ijora\nLawma Head Quarters Ijora",
				"Lee Group\nPlot 68 Ikorodu Industrial Estate",
				"Lekki Branch\nPlot 5, Block 5, Victoria Island Annex",
				"Local Airport\nNew local terminal , Lagos airport",
				"LUTH Branch\nBeside Physiotherapy Dept., LUTH",
				"Maga Plaza\n12, Idowu Martins Street, Mega Plaza Shopping Mall Victoria Island",
				"Mansard Adeniran Ogunsanya\nMansard Adeniran Ogunsanya, 82 Adeniran Ogunsanya street",
				"Marina\n49a, Marina Street, Marina",
				"Matori\n135, Ladipo Street",
				"MMIA IKEJA\nMuritala Mohammed Airport Ikeja Lagos",
				"Mobalaji Bank Anthony\n21, Mobolaji Bank Anthony, beside Bola Memorial Anglican Church",
				"Moloney\n30 Moloney Street, Lagos Island",
				"MTN Ajah\nKm 22, Lekki-Epe Expressway",
				"MTN Aromire Ikoyi\nMTN Aromire, opp. Golden Gate Ikoyi",
				"MTN Maritime House, Apapa\nMaritime House Oshodi-Apapa Express way",
				"MTN Ojota",
				"MTN Opebi, Opebi Road",
				"MTN Yellowdrome Building\nAdeola Hopewell Street",
				"Muliner Towers\n39 Kingsway road, Ikoyi Lagos",
				"Mushin Branch\n311, Agege Motor Road",
				"NB Plc Head Office\nNigerian Brewery Site",
				"New Awolowo Road Branch\n54 Awolowo Road off Ademola street",
				"NLNG - C&C Towers\nNLNG, C & C Towers, off Sanusi Fafunwa",
				"NPA Marina\nNigeria Ports Authority",
				"NPA Tincan\nNPA Tincan Island Port",
				"Oba Akran Branch\n33, Oba Akran Avenue",
				"Ogba Branch\nPlot 4, Ijaiye Road",
				"Ogudu GRA\n133, Ogudu Road, Ogudu, Lagos State, Nigeria",
				"Ojodu\n50, Isheri road",
				"Ojuelegba Branch\n74/75 Ojuelegba Road, Lagos",
				"Oke Arin\n40, John Street, Oke-Arin",
				"Okota Branch\n115a Okota road",
				"Onipanu Branch\n196 Ikorodu Road",
				"Opebi Branch\n14, Opebi Road",
				"Oregun Branch\n100, Kudirat Abiola Way",
				"Passport Office Ikoyi\nPassport Office Ikoyi",
				"Plural House\nPlot 1669, Oyin Jolayemi Street",
				"Processing Center\nProcessing Center, Plot 741 Adetokunbo Ademola Street Victoria Island",
				"Shoprite Alausa\nIkeja City Mall, Ikeja",
				"Slicks Bar VGC , Ajah 1\nPlot 07, Road 7A Ajah, Eti Osa Lagos",
				"St Nicholas Hospital Onikan\nCatholic Mission Street Lagos",
				"Surulere Branch\n94b, Bode Thomas Street",
				"The Place Restaurant Ikeja\n45, ISAAC JOHN STREET, GRA, Ikeja",
				"The Plaza\nNo 6, Adeyemo Alakija Street",
				"Tiamiyu Savage\nPlot 1400, Tiamiyu Savage Str",
				"Total Implant\nOpposite Eko Hotel Round about, VI",
				"Toyin Street\n47, Toyin Street, Ikeja, Lagos State, , Nigeria",
				"UNILAG DLI\nDLI UNILAG",
				"UPS\nUnited Parcel Service (UPS)",
				"WAMCO\nWAMCO, Admin Block",
				"Yaba Branch\n216/218, Herbert Maculay Road",
				"Yaba\nCrusader House, 16 Commercial Avenue, Sabo, Yaba, Lagos, Nigeria"

				
		};

		lv = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.gtSearch);

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
				GTBankAtms.this.adapter.getFilter().filter(cs);
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
