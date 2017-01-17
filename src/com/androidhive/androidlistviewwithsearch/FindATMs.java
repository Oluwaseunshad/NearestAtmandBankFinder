package com.androidhive.androidlistviewwithsearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FindATMs extends Activity{

	Button allAtms, diffAtms;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_atm);
	
	allAtms = (Button) findViewById(R.id.allAtms);
	diffAtms = (Button) findViewById(R.id.diffAtms);
	
	allAtms.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startAllAtms();
		}
	});
	
	diffAtms.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startDiffAtms();
		}
	});
	}
	
	
	public void startAllAtms(){
		Intent launchApp = new Intent(this,All_Atms.class);
		startActivity(launchApp);
	}
	
	public void startDiffAtms(){
		Intent launchApp = new Intent(this,Diff_Atms.class);
		startActivity(launchApp);
	}
}
