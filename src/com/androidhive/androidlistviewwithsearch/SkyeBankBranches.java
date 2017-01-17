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

public class SkyeBankBranches extends ListActivity {
	String [] branches = {
			"1st Avenue Branch\nP.M.B. 055 Festac Town",
			"Adeniran Ogunsanya\n81, Adeniran Ogunsanya Street,",
			"Adeola Hopewell\n708/709 Adeola Hopewell Street",
			"Adeola Odeku Street\nPlot 232b Adeola Odeku Street, Victoria Island",
			"Adetokunbo Ademola\nAdetokunbo Ademola Street, Victoria Island",
			"Ago Palace Way\n64 Ago Palace Way",
			"Ajose Adeogun St.\n287 Ajose Adeogun Street, Victoria Island",
			"Akin Adesola Street\n3 Akin Adesola Street. Victoria Island Lagos.",
			"Akowonjo\n35 Shasha Road By Akowonjo Roundaboutakowonjo",
			"Alaba Int'l Market\nH27/28 Alaba International Market",
			"Alausa Secretariat\nLagos Government Secretariat Complex, Alausa,",
			"Alausa\nEib House, Plot 5, Commercial Scheme, Alausa,",
			"Alfred Rewane Road\n5,Alfred Rewane Road Ikoyi",
			"Allen Avenue Branch\nAllen Avenue",
			"Aspamda Intl Market\nInt.Trade Fair Complex,Badagry Exp.Way",
			"Awolowo Rd\nAwolowo Road, Ikoyi",
			"Badagry\nBadagry Local Govt. Ajara, Badagry,",
			"Bba Branch\nAtiku Abubakar Hall, Int'l Trade Fair Complex",
			"City Hall\nLagos Island Local Government, Igbosere Road,",
			"Coker Orile Branch\nPlot 1a, Block C Lsdpc Industrial Estate, Amuwo Odofin",
			"Computer Village\n4 Oremeji Street Off Simbiat Abiola Road",
			"Creek Road Branch\n34, Creek Road,",
			"Egbe\nEgbe Road",
			"Epe Local Govt\nEpe Lga Secretariat,",
			"Fatai Atere\n19, Fatai Atere Way, Matori Industrial Estate,",
			"Glover Road Branch\nEti-Osa Local Government, Kingsway Road,",
			"Idoluwo Street\n15, Idoluwo Street,",
			"Iju\n118 Iju Road Fagba",
			"Ikeja Plaza\nIkeja Plaza, 81, Mobolaji Bank Anthony Way",
			"Ikorodu Local Govt\nIkorodu Local Govt. Secretariat,",
			"Ikorodu\nAyangburin Road",
			"Ikotun\nCollege Bus Stop",
			"Isheri-Ojodu\nOlowora Junction Omole Phase Ii",
			"Isolo Branch\n27 Mushin Rd. Isolo Lagos",
			"Kudirat Abiola Way\nPlot 32 Kudirat Abiola Road,Oregun",
			"Luth Idi Araba\nIdi Araba",
			"Magodo\n14 Cmd Road",
			"Marina Branch\n30 Marina P.M.B. 2200 Lagos",
			"Matori Market\n2/4 Jimade Close,Off Ladipo Street",
			"Montgomery Road\n2, Montgomery Road,",
			"Nahco\nBlk.2,Wing A,M/M International Airport",
			"Nnamdi Azikwe St.\nLsdpc House, Nnamdi Azikwe / Alli Balogun Street,",
			"Ogba\n37/38 Ogba-Isheri Road, Ogba Ikeja",
			"Ogudu Branch\nOjota Road Ogudu",
			"Oke- Arin\n60, Kosoko Street,",
			"Opebi Road Branch\nOpebi Road",
			"Osapa London\n14 Lekki Epe Expressway , Osapa London",
			"Osolo Way Branch\n26 Osolo Way",
			"Sango Ota\nLagos/Abeokuta Rd. Temidire Sango Otta",
			"Shomolu\n6 Bajulaiye Road Beside Mr Biggs",
			"Toyin Street Branch\n13 Toyin Street",
			"Warehouse Road\n48 Warehouse Road",
			"Wharf Road2\nWharf Road"

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
	setContentView(R.layout.skyebankbranches);
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
			SkyeBankBranches.this.adapter.getFilter().filter(cs);
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
