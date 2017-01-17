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

public class SterlingBankAtms extends Activity {

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
		setContentView(R.layout.sterlingbanksatms);

		// Listview Data
		String atms[] = { 
				"1 Creek Road (Nnewi Building)\n1- 3 Creek Rd Apapa Lagos, Apapa , Lagos State",
				"114 Awolowo Road - Ikoyi\n114, Awolowo Road, Ikoyi, Lagos, Ikoyi , Lagos State",
				"228a Awolowo Road - Ikoyi\n228,Awolowo Rd Ikoyi Lagos, Ikoyi , Lagos State",
				"26b Creek Road\n26b Creek Rd Apapa Lagos, Apapa , Lagos State",
				"2b Opebi\n2b, Opebi Road Ikeja Lagos, Ikeja , Lagos State",
				"30 Adetokunbo Ademola\n30, Adetokunbo Ademola Street V/Island, Victoria Island , Lagos State",
				"62 Adetokunbo Ademola\n62 Adetokunbo Ademola Str (By Ajose Adeogun Roundabout), V/I, Lagos, Victoria Island , Lagos State",
				"68 Opebi\n68, Opebi Str Ikeja Lagos, Ikeja , Lagos State",
				"Adebola House\n38, Opebi Road Lagos, Ikeja , Lagos State",
				"Adeniran Ogunsanya - Surulere\n74, Adeniran Ogunsanya Surulere Lagos, Surulere , Lagos State",
				"Adeola Hopewell\n42,Adeola Hopewell Str V/I Lagos, Victoria Island , Lagos State",
				"Adeola Odeku\nPlot 300 Adeola Odeku Street, V/Island Lagos, Victoria Island , Lagos State",
				"Airport Road - Ikeja\nAirport Road, Ikeja, Lagos., Ikeja , Lagos State",
				"Alaba Service Centre\n50/51, Alaba International Market Road, Alaba, Lagos., Alaba , Lagos State",
				"Alaba\n5, Alaba International Market Road, Alaba Lagos, Alaba , Lagos State",
				"Allen\n53 Allen Ave. Ikeja Lagos, Ikeja , Lagos State",
				"Aromire\n9, Aromire Avenue Ikeja Lagos, Ikeja , Lagos State",
				"Awolowo Way - Ikeja\n104, Awolowo Way Ikeja Lagos, Ikeja , Lagos State",
				"Awoyaya\nAwoyaya Beside Gommek Petrol Station, Awoyaya, Ajah Lagos, Ajah , Lagos State",
				"Bakky Plaza - Lekki\nAgungi Bus Stop Bakky Plaza Lekki, Lekki , Lagos State",
				"Coker\n29,Badagry Express Way Coker Orile Lagos, Coker-Orile , Lagos State",
				"Commercial Road\n17, Commercial Rd Apapa Lagos, Apapa , Lagos State",
				"Conoil Station - Ikeja Gra\nConoil Station, Opp. General Hospital (Lasuth), Gra Ikeja, Lagos, Ikeja , Lagos State",
				"Daleko\nPlot 8,Blk E Daleko Market Isolo Express Way, Lagos, Ikeja , Lagos State",
				"Demurin\n131/ 133 Demurin Street, Ketu, Lagos., Ketu , Lagos State",
				"Ejigbo - Nnpc\nNnpc Depot, Ejigbo, Ejigbo , Lagos State",
				"Fadeyi\n96, Ikorodu Road, Fadeyi, Lagos., Fadeyi , Lagos State",
				"Herbert Macaulay- Yaba\n260/262 Herbert Marcaulay Way Yaba, Lagos, Yaba , Lagos State",
				"Ibru Jetty - Apapa\n31, Ikudaisi Str Apapa Oshodi Exp Way Lagos, Apapa , Lagos State",
				"Iddo\nRailway Terminus Ebute Metta, Lagos, Ebute Metta , Lagos State",
				"Idimu\n294, Idimu Rd Isheri Lagos., Idimu , Lagos State",
				"Iju Road - Ifako\n102, Iju Rd, Ifako Lagos , Iju , Lagos State",
				"Ikorodu\n43, Lagos-Ikorodu Road Lagos, Ikorodu , Lagos State",
				"Ikota Shopping Complex - Ajah\nShop 14/15 Blk F Ikota Shopping Complex Ajah, Ajah , Lagos State",
				"Ilupeju\nAkintola Williams Delloite Building 235 Ikorodu Rd Lagos, Illupeju , Lagos State",
				"Ire-Akari - Isolo\n68, Ire-Akari Estate, Isolo, Lagos, Isolo , Lagos State",
				"Itire\nItire Rd By Iyana-Itire Bus Stop Off Apapa-Oshodi, Lagos, Oshodi , Lagos State",
				"Iyana-Ipaja\n109, Lagos Abeokuta Exp Way Iyana Ipaja Lagos, Iyana-Ipaja , Lagos State",
				"Ketu\n548, Ikorodu Road, Ketu, Lagos, Ketu , Lagos State",
				"Kirikiri Road\n250, Kirikiri Rd Apapa Lagos, Apapa , Lagos State",
				"Langbasa Service Centre\n26, Langbasa Road, Ajah, Lagos, Ajah , Lagos State",
				"Lasu\nLasu Ojo Lagos, Ojo , Lagos State",
				"Matori Service Centre\n1/5 Jimade Close, Matori Market. Lagos , Oshodi , Lagos State",
				"Matori\n26, Fatai Atere Way, Matori Ind. Est.Lagos , Oshodi , Lagos State",
				"Mobil Road - Ajegunle\n66, Mobil Road, Ajegunle, Lagos, Apapa , Lagos State",
				"Mushin\n122 Agege Motor Road, Mushin, Lagos, Mushin , Lagos State",
				"Oba Akran\nNo 142 Oba Akran Av. Lagos, Ikeja , Lagos State",
				"Ogba\n38 Ijaiye Rd,Ogba Lagos, Ikeja , Lagos State",
				"Ogijo\n1 Bishop Close, Ogijo, Lagos-Shagamu, Ogun State, Ikorodu , Lagos State",
				"Ogudu\n28,Ogudu Road, Ojota, Lagos., Ojota , Lagos State",
				"Ogunlana Drive- Surulere\n141, Ogunlana Drive Surulere Lagos, Surulere , Lagos State",
				"Ojuwoye Service Centre\n9, Dada Iyalode Str Ojuwoye Mushin Lagos",
				"Okota Road\n101 Okota Road Isolo, Lagos, Isolo , Lagos State",
				"Oshodi\nOyetayo Street ,Oshodi Local Govt, Oshodi, Lagos, Oshodi , Lagos State",
				"Oworonsoki Service Centre\n10 Adams Street, Oworonsoki, Lagos, Gbagada , Lagos State",
				"Saka Tinubu\n50, Saka Tinubu, Victoria Island, Lagos, Victoria Island , Lagos State",
				"Shasha Road - Dopemu\n32, Shasha Rd, Akowonjo Lagos, Egbeda , Lagos State",
				"Tincan\n10,Tincan Island Port Rd, Apapa Lagos, Apapa , Lagos State",
				"Trade Fair\n8c, Executive Plaza, Bba, Trade Fair. Lagos, Amuwo Odofin , Lagos State",
				"Wharf Road\n13/15 Wharf Rd Apapa, Apapa , Lagos State",
				"Willoughby- Ebute-Metta\n28, Willoughby Street Ebute Metta, Lagos, Ebute Metta , Lagos State"

				
		};

		lv = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.sterlingSearch);

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
				SterlingBankAtms.this.adapter.getFilter().filter(cs);
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
