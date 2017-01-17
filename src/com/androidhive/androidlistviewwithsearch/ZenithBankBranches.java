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

public class ZenithBankBranches extends ListActivity {
	String [] branches = {
			"Abule Egba\n384, Lagos/Abeokuta Expressway, Abule - Egba, Lagos",
			"Acmeroad\nPlot 12, Block E Ogba Industrial Esatate, Acme Road, Ogba, Ikeja.",
			"Adeniran Ogunsanya\n58b , Adeniran Ogunsanya Street, Surulere",
			"Adeniyi Jones\nNo. 65, Adeniyi Jones Str., Ikeja, Lagos.",
			"Adeola Odeku\nPlot 234a, Adeola Odeku Street, Victoria Island Lagos",
			"Adetokunbo Ademola\nPlot 861, Adetokunbo Ademola Street, Victoria Island Lagos",
			"Adeyemo Alakija\n4a Adeyemo Alakija Victoria Island",
			"Agbara\nNear Phcn Zonal H/Q , Agbara Industrial Estate, Ogun State",
			"Agege Motor Road\n53/55, Old Lagos-Abeokuta Road Agege",
			"Agidingbi\n8/10 Lateef Jakande Agidingbi Road, Ikeja",
			"Aguda\n15 Enitan Street,Aguda, Surulere",
			"Akin Adesola\n10b, Akin Adeosola Street, Victoria Island Lagos",
			"Akowonjo\n32 Shasha Rd Akowonjo, Lagos",
			"Alaba International\nRocky Plaza, Dobil Avenue, Alaba International Market",
			"Alagbado\nKm 34,Lagos Abeokuta Expressway, Casso B/Stop ,Alagbado",
			"Alaka\nNo.1 Western Avenue, Alaka, Surulere, Lagos State",
			"Alausa\nPlot 1, Mobolaji Johnson Avenue, Alausa Secretariat, Ikeja, Lagos State.",
			"Allen 2\n64, Allen Aveune, Ikeja, Lagos",
			"Allen\n5, Allen Avenue, Ikeja, Lagos",
			"Aluminium Village\n4,Dopemu/Akowonjo Roundabout Road, Aluminium Village,Dopemu,",
			"Amuwo Odofin\nPlot 131 Block 10 Amuwo Odofin Residential Scheme.",
			"Anthony Village\n3 Sylvia Crescent, Anthony, Ikeja, Lagos.",
			"Apapa (Warehouse Road)\n15, Warehouse Road, Apapa, Lagos",
			"Apapa Road\n112/114, Apapa Road Ebute Metta Lagos",
			"Aspamda\nHall 10 Olusegun Obasanjo, Aspamda Trade Fair, Lagos",
			"Awolowo Way\n83, Awolowo Way, Ikeja, Lagos Nigeria",
			"Badagry\n19/21 Market Road Badagry",
			"Bar Beach\n74a, Adetokunbo Ademola Str, Victoria Island",
			"Bolade Oshodi\n533, Agege Motor Rd Shogunle, Osodi, Lagos",
			"Bourdillion\nPlot 224a, Awolowo Road, Ikoyi Lagos",
			"Broad Street\n72 Broad Street, Lagos",
			"Catholic Mission\n9/11 Catholic Mission Str, Lagos Island",
			"Charity Oshodi\nPlot 14, Oshodi-Apapa Express Way. Lagos State.",
			"Coker\nAwaye House, Lagos-Badagry Expressway, Coker Bustop Lagos",
			"Computer Village\n8 Simbiat Abiola Way Ikeja",
			"Creek Road\n8 Creek Road, Apapa Lagos.",
			"Dopemu\n89, Abeokuta Expressway, Pako Bus Stop, Dopemu Lagos",
			"Ebute-Ero\n101 Alakoro Street, Ebute Ero, Lagos Island, Lagos.",
			"Ejigbo\nOpp. Nnpc Depot, Isolo - Egbe Road",
			"Epe\nNo 22/23 Lagos Road Opp. Ben-Jude Supermarket Epe, Lagos",
			"Eric-Moore\n43, Eric Moore Close Surulere Lagos",
			"Falomo Roundaout\nZenon Direct Diesel Filling Station, Falomo Roundabout.221, Awolowo Road, Ikoyi.",
			"Falomo\n176 Awolowo Road, Ikoyi, Lagos",
			"Festac\n1st Avenue House 1, Beside Coorporate Bank, Festac Lagos.",
			"Gbagada 2\n280, Oshodi-Oworonshoki Expressway, 1st Petro/Charly Boy B/Stop, Gbagada Phase 1",
			"Gbagada\n17/19 Diya Str, Ifako, Gbagada.",
			"Head Office Annex 2\nPlot 277 Ajose Adeogun St. Victoria Island Lagos.",
			"Head Office Annex 3\nAjose Adeogun, Victoria Island, Lagos",
			"Head Office\nPlot 84, Ajose Adeogun Street, P. O .Box 75315 Victoria Island Lagos.",
			"Herbert Macaulay\n244/ 246, Herbert Macaulay Way, Alagomeji, Yaba, Lagos",
			"Herbert Macauley 2\nNo 171, Herbert Macaulay Way, Yaba, Lagos State.",
			"Idimu\n211/213, Egbeda-Idimu Road, Idimu,",
			"Idi-Oro\n128, Agege Motor Road, Idi-Oro Mushin.",
			"Idumagbo\n82,Enu-Owa Street, Idumagbo Lagos",
			"Idumota\n18, Ereko Street, Idumota Lagos",
			"Iju\n149, Iju Road, Agege",
			"Ikeja (Oba Akran)\n24, Oba Akran Avenue, Lagos",
			"Ikeja Gra\n32 Sobo Arobiodu Street Gra Ikeja, Ikeja.",
			"Ikorodu Road\n152 Ikorodu Road, Beside Adebowale Elect. Stores, Onipanu Lagos.",
			"Ikorodu\n45 Lagos- Ikorodu Road, Ikorodu",
			"Ikota Shopping Complex\nIkota Shopping Complex",
			"Ikota\nAlong Lekki Epe Express Road, Opposite Spg Petroleum, Lekki",
			"Ikotun\n44, Idimu Ikotun Road, Ikotun",
			"Ikoyi\n51, Awolowo Road, Ikoyi, Lagos",
			"Ilupeju 2\n201 Ikorodu Road, Illupeju, Lagos",
			"Ilupeju\n7/9, Industrial Avenue, Ilupeju, Lagos",
			"International Airport (Terminal)\nMurtala International Airport, Ikeja.",
			"Ipaja (Oke-Odo)\n413b Lagos Abeokuta Exp.Way By Ile-Epo B/Stop, Oke-Odo, Alimosho L.G.A. Lagos",
			"Isolo 2 (Int’l Airport Rd. Ii, Mafoluku)\n58, Airport Road, Mafoluku, Lagos.",
			"Isolo\n9, Murtala Mohammed, Intl. Airport Road, Lagos.",
			"Keffi(S/ West Ikoyi)\n8 Keffi Str, South-West, Ikoyi, Lagos",
			"Ketu\n547, Ikorodu Road, By Iyana School B/Stop, Ketu, Lagos.",
			"Kingsway\n2, Aromire Str, By Kingsway Road, Ikoyi, Lagos.",
			"Kofo Abayomi\nPlot 43, Kofo Abayomi Street, Victoria Island Lagos",
			"Kudirat Abiola\n21 Kudirat Abiola Way Oregun, Lagos",
			"Ladipo Oluwole\n3a & B, Oladipo Oluwole Street, Off Adeniyi Jones Avenue, Ikeja, Lagos State.",
			"Lagos Central\n9, Joseph St. Lagos Island, Lagos.",
			"Laspotech\n10a, Isolo Road, Opposite Lagos, State Polythecnic, Isolo.",
			"Lasu\nLasu Main Campus, Lagos- Badagry Expressway, Ojo, Lagos.",
			"Lawanson\n27/29, Lawanson Road, Surulere Lagos",
			"Lekki Exp\nNo 1 Truddy Ebhodagbe Crescent Osapa, Lekki - Epe Expressway, Lagos.",
			"Lekki Expressway2 Branch\nPlot 12, Block 113a Lekki Peninsula Residential Scheme, Lagos State",
			"Lekki\nPlot 6 Layi Yusuf Cresent Off Adiralty Road Lekki Penissula, Lagos",
			"Liverpool\n3, Point Road, Liverpool Junction, Apapa, Lagos.",
			"Magodo\n5,Cmd Road, Shangisha, Magodo",
			"Marina 2\n10 Abibu Oki Street",
			"Marina\n58, Marina, Lagos",
			"Marine Road\n6 Marine Road",
			"Matori\n133, Ladipo Street, Mushin Lagos",
			"Medical Road\n6, Pepple Street Ikeja, Lagos",
			"Mobil Rd\n19, Mobil Road, Apapa.",
			"Moloney\n20-22, Moloney Streett, Obalende, Lagos Island.",
			"Muri Okonola\nNo 232a Murri Okonola Victoria Island Lagos",
			"Ogba\nPlot1 Block B, Ogba Housing Estate, By Waec, Lagos",
			"Ogba2\n42 Ogba Ijaiye Road Ogba",
			"Ogudu\n147, Ogudu Road, Ojota,",
			"Ogunlana Drive\n162,Ogunlana Drive, Surulere, Lagos",
			"Ojodu\n1b, Bashorun Str., Isheri- Ojodu Road, Ojodu, Lagos.",
			"Okearin 2\n46, Sanusi Olusi Street, Oke Arin, Lagos.",
			"Oke-Arin\n33, Isa Williams Street, Oke-Arin, Lagos",
			"Okota\n103, Okota Road, Isolo",
			"Old Ojo Road\n45 Old Ojo Road, By 1st Bus/Stop, Mazamaza.",
			"Olosa\nNo 11a, Olosa Street, Victoria Island Lagos",
			"Olowu\n23 Olowu Street, Ikeja, Lagos",
			"Opebi\n85, Opebi Road, Opebi, Ikeja, Lagos",
			"Oregun\nOregun/Awolowo Road Ikeja , Lagos",
			"Ozumba Mbadiwe 2\nPlot 168, Ozumba Mbadiwe Street, Victoria Island, Lagos",
			"Ozumba Mbadiwe\nPlot 873, Ozumba Mbadiwe, V/Island.",
			"Palm Avenue\n309, Agege Motor Road, Mushin Lagos",
			"Pen Cinema\n18, Iju Road, Pen Cinema, Agege, Lagos.",
			"Saka Tinubu\nPlot 29 Saka Tinubu Street, Victoria Island, Lagos",
			"Sango 2\nKilometer 59, Abeokuta Expressway, Sango Otta, Ogun State.",
			"Sango Ota 3\nNo. 111, Idiroko Road, Sango Ota, Ogun State",
			"Sango Ota\nKm 37, Lagos Abeokuta Exp.Way, Sango Otta, Ogun State.",
			"Sanusi Fafunwa\n31 Sanusi Fafunwa, Victoria Island Lagos.",
			"Satelite Town\n436, Old Ojo Road Abule Ado Satellite Town",
			"Seme Border\nBank Street, Seme Border, Badagry Expressway, Seme, Lagos State.",
			"Snake Island\nNigerdock Complex By Tin-Can 1st Gate, Tin Can Island",
			"St Finbarrs\n47, St. Finbarrs Road, Akoka Lagos.",
			"Surulere\n86, Ojuelegba Road, Surulere Lagos",
			"Tejuosho\n65, Tejuosho Rd, Yaba Lagos.",
			"Trade Fair Complex\nPlaza 10, Balogun Business Association, Int'l Trade Fair Complex Lagos",
			"Trinity 2\n18, Apapa-Oshodi Expressway Apapa, Lagos",
			"Trinity\nNo 36 Apapa Oshodi Expressway Lagos",
			"Warehouse Road 2\nNo. 35 Warehouse Road Apapa Lagos State.",
			"Wharf Road\n24a, Wharf Road,Apapa Lagos",
			"Yabatech\nYaba College Of Technology, Yaba, Lagos State."
				
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
	setContentView(R.layout.zenithbankbranches);
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
			ZenithBankBranches.this.adapter.getFilter().filter(cs);
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
