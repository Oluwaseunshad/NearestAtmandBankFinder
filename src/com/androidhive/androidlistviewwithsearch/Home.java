package com.androidhive.androidlistviewwithsearch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Home extends Activity{

	Button proceed;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		proceed = (Button)findViewById(R.id.proceed);
		
		proceed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startMainActivity();
			}
		});
	
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
	      boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER); 
	      // Check if enabled and if not send user to the GPS settings
	      if (!enabled) {
	    	  	AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
  				builder.setTitle("GPS Disabled");
  				builder.setMessage("Kindly enable GPS");
  				builder.setCancelable(true);
  				builder.setNeutralButton(android.R.string.ok,
  			        new DialogInterface.OnClickListener() {
  			    public void onClick(DialogInterface dialog, int id) {
  		          Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
  		          startActivity(intent);  			    	}
  				});

  				AlertDialog alert11 = builder.create();
  				alert11.show();
  			
	      }

	
	}
	
	public void startMainActivity(){
		Intent launchApp = new Intent(this, MainActivity.class);
		startActivity(launchApp);
	}
}