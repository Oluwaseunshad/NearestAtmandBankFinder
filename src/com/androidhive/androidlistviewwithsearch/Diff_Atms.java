package com.androidhive.androidlistviewwithsearch;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Diff_Atms extends ListActivity{

	String [] banks = {
			"Access Bank",
			"Citibank",
			"Diamond Bank",
			"Ecobank Nigeria",
			"Enterprise Bank Limited",
			"Fidelity Bank Nigeria",
			"First City Monument Bank",
			"Guaranty Trust Bank",
			"Heritage Bank Plc",
			"Keystone Bank Limited",
			"Mainstreet Bank Limited",
			"Skye Bank",
			"Stanbic IBTC Bank",
			"Standard Chartered Bank",
			"Sterling Bank",
			"Union Bank of Nigeria",
			"United Bank of Africa",
			"Unity Bank Plc",
			"Wema Bank",
			"Zenith Bank"
				
		};
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diff_banks_atm);
		setListAdapter(new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1,
		banks));
			}
		public void onListItemClick(ListView parent, View v, int position,
		long id) {
		
		switch (position){
		case 0:
			 startApp();
			 break;
		case 1:
			startApp1();
			break;
		case 2:
			startApp2();
			break;
		case 3:
			startApp3();
			break;
		case 4:
			startApp4();
			break;
		case 5:
			startApp5();
			break;
		case 6:
			startApp6();
			break;
		case 7:
			startApp7();
			break;
		case 8:
			startApp8();
			break;
		case 9:
			startApp9();
			break;
		case 10:
			startApp10();
			break;
		case 11:
			startApp11();
			break;
		case 12:
			startApp12();
			break;
		case 13:
			startApp13();
			break;
		case 14:
			startApp14();
			break;
		case 15:
			startApp15();
			break;
		case 16:
			startApp16();
			break;
		case 17:
			startApp17();
			break;
		case 18:
			startApp18();
			break;
		case 19:
			startApp19();
			break;
		}
		}

		public void startApp() {
			// TODO Auto-generated method stub
	    	Intent launchApp = new Intent(this, AccessBankAtms.class);
	    	
	    	startActivity(launchApp);
		}
	 
	 public void startApp1() {
			// TODO Auto-generated method stub
	    	Intent launchApp1 = new Intent(this, CitiBankAtms.class);
	    	
	    	startActivity(launchApp1);
		}
	 public void startApp2() {
			// TODO Auto-generated method stub
	    	Intent launchApp1 = new Intent(this, DiamondBankAtms.class);
	    	
	    	startActivity(launchApp1);
		}
	 public void startApp3() {
			// TODO Auto-generated method stub
	    	Intent launchApp1 = new Intent(this, EcoBankAtms.class);
	    	
	    	startActivity(launchApp1);
		}
	 public void startApp4() {
			// TODO Auto-generated method stub
	    	Intent launchApp1 = new Intent(this, EnterpriseBankAtms.class);
	    	
	    	startActivity(launchApp1);
		}
	 public void startApp5() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, FidelityBankAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp6() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, FirstBankAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp7() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, FCMBAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp8() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, GTBankAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp9() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, HeritageBankAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp10() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, KeystoneBankAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp11() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, MainstreetBankAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp12() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, SkyeBankAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp13() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, StanbicIBTCAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp14() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, SterlingBankAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp15() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, UnionBankAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp16() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, UBAAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp17() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, UnityBankAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp18() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, WemaBankAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	public void startApp19() {
			// TODO Auto-generated method stub
	 	Intent launchApp1 = new Intent(this, ZenithBankAtms.class);
	 	
	 	startActivity(launchApp1);
		}
	 
	}