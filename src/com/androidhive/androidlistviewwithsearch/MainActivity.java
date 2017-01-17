package com.androidhive.androidlistviewwithsearch;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	
    String [] items = {
    		"Find Banks",
    		"Find ATMs",
    		"Near You",
    		"More"};
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.mainactivity);
	setListAdapter(new ArrayAdapter<String>(this,
	android.R.layout.simple_list_item_1,
	items));
		}
	public void onListItemClick(ListView parent, View v, int position,
	long id) {
	
	switch (position){
	case 0:
		 startApp1();
		 break;
	case 1:
		startApp2();
		break;
	case 2:
		startApp3();
		break;
	case 3:
		startApp4();
		break;
	
	}
	}

 
 public void startApp1() {
		// TODO Auto-generated method stub
    	Intent launchApp1 = new Intent(this, FindBanks.class);
    	
    	startActivity(launchApp1);
	}
 public void startApp2() {
		// TODO Auto-generated method stub
    	Intent launchApp1 = new Intent(this, FindATMs.class);
    	
    	startActivity(launchApp1);
	}
 public void startApp3() {
		// TODO Auto-generated method stub
    	Intent launchApp1 = new Intent(this, NearYou.class);
    	
    	startActivity(launchApp1);
	}
 public void startApp4() {
		// TODO Auto-generated method stub
    	Intent launchApp1 = new Intent(this, More.class);
    	
    	startActivity(launchApp1);
	}
}