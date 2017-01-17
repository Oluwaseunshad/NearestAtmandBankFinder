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

public class EcoBankBranches extends ListActivity {
	String [] branches = {
			/*
			 * List of Eco Bank branches in Lagos State
			 */
			"Ahmadu Bello\nPlot 21, Ahmadu Bello Way",
			"Airport Road\n14 International Airport Road",
			"Alaba - Agudosi Branch\n1 Agudosi Street",
			"Alaba-Ojo Igbede\nAlaba Int'L Market Ojo-Igbede Road",
			"Alaba-St Patricks Junction\nSt Patrick Junction",
			"Apapa - Creek Road\n4 Creek Road",
			"Apapa - Wharf Road\n13/15 Wharf Road",
			"Apapa-Warehouse Road 2\n42/44 Warehouse Road",
			"Apapa-Warehouse Road\n2, Warehouse Road",
			"Bba\nAtiku Abubakar Hall Trade Fair Complex",
			"Broad Street 2\n137/139 Broad Street",
			"Broad Street\n130 Broad Street",
			"Chevron\nChevron Drive",
			"Coker Branch\n35A Badagry Express Way",
			"Daleko\n812/813 Bank Road",
			"Eleganza\nEpe Expressway-Shopping Complex",
			"Festac\nHouse 22, 2Nd Avenue",
			"Gbagada Branch\n228 Gbagada Residential Scheme",
			"Herbert Macaulay-Sabo\n302 Herbert Macaulay Way",
			"Idumagbo\n74 Iga-Idungaran Street",
			"Idumota - Enu-Owa\n37/43 Enu-Owa Street",
			"Idumota-Ashogbon\n10 Ashogbon Street",
			"Idumota-Nnamdi Azikwe\n100 Nnamdi Azikwe",
			"Ikeja - Allen Avenue 2\nBuffalo House, 2 Allen Avenue",
			"Ikeja - Allen Avenue\n86 Allen Avenue",
			"Ikeja - Ogba\n21 Ijaiye Road",
			"Ikeja- Intl Airport\n'D' Arrival Hall",
			"Ikeja-Adeniyi Jones\n84, Adeniyi Jones Street",
			"Ikeja-Gra\n6 Joel Ogunaike Street",
			"Ikorodu Road Branch\nElizade Plaza, 322A Ikorodu Road",
			"Ikorodu Road Ketu\n487, Ikorodu Road",
			"Ikota Branch\nIkota Shopping Complex",
			"Ikoyi - Awolowo Road\n67 Awolowo Road",
			"Ilupeju\n1 Bank Lane",
			"Intnl Trade Fair-Aspamda\nChief Olusegun Obasanjo Hall-Aspamda",
			"Isheri Branch\nOba Ogunnusi Road",
			"Isolo - Ire Akari\n1A Ire-Akari Estate Road",
			"Isolo - Oke Afa Road\n99B Okota Road",
			"Isolo - Okota\n99B Okota Road, Ago Palace Junction",
			"Ladipo\nAguiyi Ironsi Shopping Complex",
			"Matori-Ladipo Street\n97, Ladipo Street",
			"Maza Maza\n13 Old Ojo Road",
			"Mushin-Idi Oro\n118 Agege Motor Road",
			"Oba Akran 1\n19A Oba Akran Avenue",
			"Ogudu\n114 Ogudu-Ojota Road",
			"Ojuelegba\n30-32 Ojuelegba, Road",
			"Oke-Arin\n7 Sanusi Olusi Street",
			"Olodi Apapa\nPlot 17, Oshodi Apapa Express Way",
			"Orile\nKm 85,Lagos Badagry Expressway",
			"Oyingbo\n22, Herbert Macauley Street",
			"Point Road Apapa\nPoint Road",
			"Seme Border\nBank Avenue",
			"St Patrick\nAlaba International Market",
			"Surulere\n60 Adeniran Ogunsanya Street",
			"Tbs\n39/40B, Tafawa Balewa Square",
			"Unilag\nUnilag Bookshop Building",
			"VI - Adeola Odeku\n58A Adeola Odeku Street",
			"VI - Bishop Aboyade Cole Branch\nPlot 3 Bishop Aboyade Cole Street",
			"VI - Ligali Ayorinde\nPlot 2B Ligali Ayorinde St",
			"VI-Ajose Adeogun\nNo. 2 Ajose Adeogun Street",
			"VI-Akin Adesola\n25 Akin Adesola Street",
			"VI-Oyin Jolayemi\nPlot 1675 Oyin Jolayemi Street"

				
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
	setContentView(R.layout.ecobankbranches);
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
			EcoBankBranches.this.adapter.getFilter().filter(cs);
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
