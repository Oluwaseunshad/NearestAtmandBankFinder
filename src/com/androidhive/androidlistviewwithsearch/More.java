package com.androidhive.androidlistviewwithsearch;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class More extends Activity{

	Button about, contact, share;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
	
	about = (Button) findViewById(R.id.about);
	contact = (Button) findViewById(R.id.contact);
	share = (Button) findViewById(R.id.shareus);
	
	about.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startAbout();
		}
	});
	
	contact.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startContact();
		}
	});
	
	share.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startShare();
		}
	});
	}
	
	
	public void startAbout(){
		Intent launchApp = new Intent(this,About_Us.class);
		startActivity(launchApp);
	}
	
	public void startContact(){
		Log.i("Send email", "");

		String[] TO = { "oluwaseun4life@gmail.com" };
		String[] CC = { "baddymann1@gmail.com" };
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");

		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_CC, CC);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

		try {
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			finish();
			Log.i("Finished sending email...", "");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(More.this,
					"There is no email client installed.", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	public void startShare(){
		Intent sendMailIntent = new Intent(Intent.ACTION_SEND);
		sendMailIntent.putExtra(Intent.EXTRA_SUBJECT,
				getString(R.string.Share_Mail_Subject));
		sendMailIntent.putExtra(Intent.EXTRA_TEXT,
				getString(R.string.Share_Mail_Text));
		sendMailIntent.setType("text/plain");

		startActivity(Intent.createChooser(sendMailIntent,
				"Email / SMS / Tweet ?"));
	}
}