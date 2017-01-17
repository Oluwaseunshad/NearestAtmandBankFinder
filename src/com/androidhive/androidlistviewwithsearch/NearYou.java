package com.androidhive.androidlistviewwithsearch;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings("deprecation")
public class NearYou extends FragmentActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the three primary sections of the app. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	GoogleMap googleMap;
	MapView mMapView;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the
	 * app, one at a time.
	 */
	ViewPager mViewPager;
	private String[] tabs = { "ATMs Near You", "Banks Near You" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.near_you);

		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		/*
		 * for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) { //
		 * Create a tab with text corresponding to the page title defined by the
		 * adapter. // Also specify this Activity object, which implements the
		 * TabListener interface, as the // listener for when this tab is
		 * selected. actionBar.addTab( actionBar.newTab()
		 * .setText(mAppSectionsPagerAdapter.getPageTitle(i))
		 * .setTabListener(this)); }
		 */
		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				// The first section of the app is the most interesting -- it
				// offers
				// a launchpad into the other demonstrations in this example
				// application.
				NearYou near = new NearYou();
				return near.new ATMsFragment();

			default:
				// The other sections of the app are dummy placeholders.
				NearYou near1 = new NearYou();
				Fragment fragment = near1.new BanksFragment();
				// return near1.new BanksFragment();
				return fragment;
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

	}

	/**
	 * A fragment that launches other parts of the demo application.
	 */
	public class ATMsFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View atmView = inflater.inflate(R.layout.atm_near_you, container,
					false);

			setUpMapIfNeeded();

			return atmView;

		}

		@Override
		public void onResume() {
			super.onResume();
			setUpMapIfNeeded();
		}

		/**
		 * Sets up the map if it is possible to do so (i.e., the Google Play
		 * services APK is correctly installed) and the map has not already been
		 * instantiated.. This will ensure that we only ever call
		 * {@link #setUpMap()} once when {@link #mMap} is not null.
		 * <p>
		 * If it isn't installed {@link SupportMapFragment} (and
		 * {@link com.google.android.gms.maps.MapView MapView}) will show a
		 * prompt for the user to install/update the Google Play services APK on
		 * their device.
		 * <p>
		 * A user can return to this FragmentActivity after following the prompt
		 * and correctly installing/updating/enabling the Google Play services.
		 * Since the FragmentActivity may not have been completely destroyed
		 * during this process (it is likely that it would only be stopped or
		 * paused), {@link #onCreate(Bundle)} may not be called again so we
		 * should call this method in {@link #onResume()} to guarantee that it
		 * will be called.
		 */
		private void setUpMapIfNeeded() {
			// Do a null check to confirm that we have not already instantiated
			// the map.
			if (googleMap == null) {
				// Try to obtain the map from the SupportMapFragment.
				googleMap = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.atmMapView)).getMap();
				// Check if we were successful in obtaining the map.
				if (googleMap != null) {
					setUpMap();
				}
			}
		}

		/**
		 * This is where we can add markers or lines, add listeners or move the
		 * camera. In this case, we just add a marker near Africa.
		 * <p>
		 * This should only be called once and when we are sure that
		 * {@link #mMap} is not null.
		 */
		private void setUpMap() {
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			String provider = locationManager.getBestProvider(criteria, true);
			LocationListener locationListener = new LocationListener() {
				@Override
				public void onLocationChanged(Location location) {
					showCurrentLocation(location);
				}

				@Override
				public void onStatusChanged(String s, int i, Bundle bundle) {
				}

				@Override
				public void onProviderEnabled(String s) {
				}

				@Override
				public void onProviderDisabled(String s) {
				}
			};
			locationManager.requestLocationUpdates(provider, 2000, 0,
					locationListener);
			// Getting initial Location
			Location location = locationManager.getLastKnownLocation(provider);
			// Show the initial location
			if (location != null) {
				showCurrentLocation(location);
			}
		}

		private void showCurrentLocation(Location location) {
			googleMap.clear();
			LatLng currentPosition = new LatLng(location.getLatitude(),
					location.getLongitude());
			// Zoom in, animating the camera.
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					currentPosition, 15));

			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);

			// Enable / Disable my location button
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			//
			googleMap.getUiSettings().setZoomControlsEnabled(true);

			/*
			 * mMap.addMarker(new MarkerOptions() .position(currentPosition)
			 * .snippet("Lat: " + location.getLatitude() + ", Lng: " +
			 * location.getLongitude())
			 * .icon(BitmapDescriptorFactory.fromResource
			 * (R.drawable.traffic_jams)) .flat(true); .title("I'm here!")); //
			 * Zoom in, animating the camera.
			 * mMap.animateCamera(CameraUpdateFactory
			 * .newLatLngZoom(currentPosition, 18));
			 */
			markersOne();
			markersTwo();
			markersThree();
			markersFour();
			markersFive();
			// Adding marker on the Google Map

			
			
			
			
			

		}

		private void markersOne() {
			MarkerOptions marker1 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458900"), Double
									.parseDouble("3.298813")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Old Ojo Road Branch, Lagos").flat(true);

			MarkerOptions marker2 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.555901"), Double
									.parseDouble("3.336582")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Oshodi Branch").flat(true);

			MarkerOptions marker3 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.484525"), Double
									.parseDouble("3.567123")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Lagos Business School").flat(true);

			MarkerOptions marker4 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.641419"), Double
									.parseDouble("3.338814")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Ait Mini Branch").flat(true);

			MarkerOptions marker5 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.44393"), Double
									.parseDouble("3.501205")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Victoria Garden City").flat(true);

			MarkerOptions marker6 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.630762"), Double
									.parseDouble("3.338041")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Ogba Branch").flat(true);

			MarkerOptions marker7 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.484199"), Double
									.parseDouble("3.386377")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Oyingbo Branch").flat(true);

			MarkerOptions marker8 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.470283"), Double
									.parseDouble("3.351088")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Ijora Badia Branch").flat(true);

			MarkerOptions marker9 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459475"), Double
									.parseDouble("3.417244")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Moloney Branch").flat(true);

			MarkerOptions marker10 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.537220"), Double
									.parseDouble("3.346541")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Matori Branch").flat(true);

			MarkerOptions marker11 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.598108"), Double
									.parseDouble("3.380356")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Ojota Branch").flat(true);

			MarkerOptions marker12 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.515115"), Double
									.parseDouble("3.381262")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Yaba Branch").flat(true);

			MarkerOptions marker13 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.457861"), Double
									.parseDouble("3.384827")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Balogun Branch").flat(true);

			MarkerOptions marker14 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.468151"), Double
									.parseDouble("3.390999")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Idumagbo Branch").flat(true);

			MarkerOptions marker15 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.472053"), Double
									.parseDouble("3.324763")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Ibafon Branch").flat(true);

			MarkerOptions marker16 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.628098"), Double
									.parseDouble("3.331701")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Agege Branch").flat(true);

			MarkerOptions marker17 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.557692"), Double
									.parseDouble("3.393745")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Okearin Branch").flat(true);

			MarkerOptions marker18 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460379"), Double
									.parseDouble("3.191024")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Ojo Alaba Branch").flat(true);

			MarkerOptions marker19 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.451046"), Double
									.parseDouble("3.449118")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Alexander (Ensec) Branch").flat(true);

			MarkerOptions marker20 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.436264"), Double
									.parseDouble("3.410193")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Corporate Branch").flat(true);

			MarkerOptions marker21 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.452458"), Double
									.parseDouble("3.415375")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Awolowo Road Branch, Ikoyi").flat(true);

			MarkerOptions marker22 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455145"), Double
									.parseDouble("3.350143")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Boundary Market Ajegunle Branch").flat(true);

			MarkerOptions marker23 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.473836"), Double
									.parseDouble("3.294969")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Festac Branch").flat(true);

			MarkerOptions marker25 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.445635"), Double
									.parseDouble("3.430481")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Adetokunbo Ademola Branch").flat(true);

			MarkerOptions marker26 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.551392"), Double
									.parseDouble("3.392318")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Gbagada Branch").flat(true);

			MarkerOptions marker27 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.552713"), Double
									.parseDouble("3.362246")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Ilupeju Branch").flat(true);

			MarkerOptions marker28 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.433023"), Double
									.parseDouble("3.421028")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Napex Branch").flat(true);

			MarkerOptions marker29 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.604481"), Double
									.parseDouble("3.350959")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Allen Avenue, Ikeja Branch").flat(true);

			MarkerOptions marker30 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.557915"), Double
									.parseDouble("3.366687")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Ikorodu Town Branch").flat(true);

			MarkerOptions marker31 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.536203"), Double
									.parseDouble("3.349457")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Ladipo Branch").flat(true);

			MarkerOptions marker32 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.476608"), Double
									.parseDouble("3.603745")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Lekki Branch").flat(true);

			MarkerOptions marker33 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.599472"), Double
									.parseDouble("3.365604")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Oregun Branch").flat(true);

			MarkerOptions marker34 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.439072"), Double
									.parseDouble("3.371487")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Burma Roda, Apapa Branch").flat(true);

			MarkerOptions marker35 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.476072"), Double
									.parseDouble("3.204819")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Bba Branch").flat(true);

			MarkerOptions marker38 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4412"), Double
									.parseDouble("3.416748")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Adeola Odeku Branch").flat(true);

			MarkerOptions marker39 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.468536"), Double
									.parseDouble("3.243668")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Apt, Trade Fair Branch").flat(true);

			MarkerOptions marker40 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.506268"), Double
									.parseDouble("3.366235")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Tejuosho Branch").flat(true);

			MarkerOptions marker41 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.469516"), Double
									.parseDouble("3.351517")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Obasanjo Main, Aspamda Branch").flat(true);

			MarkerOptions marker42 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.47395"), Double
									.parseDouble("3.56781")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Ikota Mini Branch").flat(true);

			MarkerOptions marker43 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.531264"), Double
									.parseDouble("3.344083")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Ire Akari Branch").flat(true);

			MarkerOptions marker44 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.490303"), Double
									.parseDouble("3.354757")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Bode Thomas Branch, Lagos").flat(true);

			MarkerOptions marker45 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.595229"), Double
									.parseDouble("3.339947")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Computer Village Micro Branch").flat(true);

			MarkerOptions marker46 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431723"), Double
									.parseDouble("3.424366")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Adeyemo Alakija Branch").flat(true);

			MarkerOptions marker47 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.434814"), Double
									.parseDouble("3.429301")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Adeola Hopewell Branch").flat(true);

			MarkerOptions marker48 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.525661"), Double
									.parseDouble("3.367449")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Fadeyi Branch").flat(true);

			MarkerOptions marker49 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.448301"), Double
									.parseDouble("3.326497")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Trinity Branch").flat(true);

			MarkerOptions marker50 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.553599"), Double
									.parseDouble("3.334351")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Airport Road Lagos Branch").flat(true);

			MarkerOptions marker51 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.479876"), Double
									.parseDouble("3.385742")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Ebute Metta Branch").flat(true);

			MarkerOptions marker52 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.445977"), Double
									.parseDouble("3.364563")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Warehouse Road Apapa, Branch").flat(true);

			MarkerOptions marker53 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444004"), Double
									.parseDouble("3.369849")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Commercial Road, Apapa Branch").flat(true);

			MarkerOptions marker54 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.502323"), Double
									.parseDouble("3.379717")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Financial Trust House (Balogun)").flat(true);

			MarkerOptions marker55 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.536175"), Double
									.parseDouble("3.352859")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Mushin Branch").flat(true);

			MarkerOptions marker56 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.617444"), Double
									.parseDouble("3.312793")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Dopemu Branch").flat(true);

			MarkerOptions marker57 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.521085"), Double
									.parseDouble("3.385385")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Akoka Branch").flat(true);

			MarkerOptions marker57a = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.433402"), Double
									.parseDouble("3.356050")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Tin Can Island Branch").flat(true);

			MarkerOptions marker58 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.461669"), Double
									.parseDouble("3.204918")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Alaba 2 Branch").flat(true);

			MarkerOptions marker58a = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.581855"), Double
									.parseDouble("3.321280")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Nahco Branch").flat(true);

			MarkerOptions marker59 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.462925"), Double
									.parseDouble("3.389182")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Enu Owa Branch").flat(true);

			MarkerOptions marker60 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.584252"), Double
									.parseDouble("3.983359")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Ibeju Lekki Branch").flat(true);

			MarkerOptions marker61 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.613370"), Double
									.parseDouble("3.336086")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fidelity))

					.title("Oba Akran Ikeja Branch").flat(true);

			/*
			 * End of Fidelity Bank Atms branches
			 */

			/*
			 * First Bank Atms branches
			 */
			MarkerOptions marker62 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429529"), Double
									.parseDouble("3.386236")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Abibu Adetoro Branch").flat(true);

			MarkerOptions marker63 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453447"), Double
									.parseDouble("3.440329")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Abibu-Oki Branch").flat(true);

			MarkerOptions marker64 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.524379"), Double
									.parseDouble("3.379206")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Abule Egba Branch").flat(true);

			MarkerOptions marker65 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.495078"), Double
									.parseDouble("3.380797")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Adekunle Branch").flat(true);

			MarkerOptions marker66 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430533"), Double
									.parseDouble("3.415598")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Adeola Odeku").flat(true);

			MarkerOptions marker67 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.436120"), Double
									.parseDouble("3.416443")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Adeola Odeku (Radisson Blu)").flat(true);

			MarkerOptions marker68 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429955"), Double
									.parseDouble("3.429904")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Adetokunbo Ademola Branch").flat(true);

			MarkerOptions marker69 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.679615"), Double
									.parseDouble("3.308275")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Agbado crossing QSP").flat(true);

			MarkerOptions marker70 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.639784"), Double
									.parseDouble("3.317924")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Agege Annex Agency").flat(true);

			MarkerOptions marker71 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.562215"), Double
									.parseDouble("3.349115")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Agege Branch").flat(true);

			MarkerOptions marker72 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.620920"), Double
									.parseDouble("3.353341")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Agidingbi Branch").flat(true);

			MarkerOptions marker73 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.496214"), Double
									.parseDouble("3.341090")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Aguda Branch").flat(true);

			MarkerOptions marker74 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.427537"), Double
									.parseDouble("3.409279")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ahmadu Bello Branch").flat(true);

			MarkerOptions marker75 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.469773"), Double
									.parseDouble(" 3.584543")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ajah Branch").flat(true);

			MarkerOptions marker76 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.524379"), Double
									.parseDouble("3.379206")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ajah Market").flat(true);

			MarkerOptions marker77 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.584742"), Double
									.parseDouble("3.332763")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ajao Estate Branch").flat(true);

			MarkerOptions marker78 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.445974"), Double
									.parseDouble("3.324582")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ajegunle Trinity Branch").flat(true);

			MarkerOptions marker79 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.524379"), Double
									.parseDouble("3.379206")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("AJEROMI CASH CENTRE").flat(true);

			MarkerOptions marker80 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430981"), Double
									.parseDouble("3.430790")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ajose Adeogun Branch").flat(true);

			MarkerOptions marker81 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600693"), Double
									.parseDouble("3.300757")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Akowonjo Branch").flat(true);

			MarkerOptions marker82 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.550563"), Double
									.parseDouble("3.268390")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Akowonjo QSP").flat(true);

			MarkerOptions marker83 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.516804"), Double
									.parseDouble("3.353981")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("ALABA 2").flat(true);

			MarkerOptions marker84 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460204"), Double
									.parseDouble("3.165468")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("ALABA INT'L").flat(true);

			MarkerOptions marker85 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.524379"), Double
									.parseDouble("3.379206")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Alausa Branch").flat(true);

			MarkerOptions marker86 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438087"), Double
									.parseDouble("3.368215")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("APAPA MAIN").flat(true);

			MarkerOptions marker87 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.668391"), Double
									.parseDouble("3.276136")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("ARTICLE MARKET Agency").flat(true);

			MarkerOptions marker88 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.441977"), Double
									.parseDouble("3.418408")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Awolowo Road Branch").flat(true);

			MarkerOptions marker89 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.422020"), Double
									.parseDouble("2.888884")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("BADAGRY Branch").flat(true);

			MarkerOptions marker90 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.568505"), Double
									.parseDouble("3.941316")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Badore Branch").flat(true);

			MarkerOptions marker91 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.497352"), Double
									.parseDouble("3.360319")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Balogun Branch").flat(true);

			MarkerOptions marker92 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.466667"), Double
									.parseDouble("3.450000")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Banana Island QSP").flat(true);

			MarkerOptions marker93 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.551475"), Double
									.parseDouble("3.392124")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Bariga Branch").flat(true);

			MarkerOptions marker94 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.457597"), Double
									.parseDouble("3.380553")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Broad Street Branch").flat(true);

			MarkerOptions marker95 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431224"), Double
									.parseDouble("3.469051")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Chevron Branch").flat(true);

			MarkerOptions marker96 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.449989"), Double
									.parseDouble("3.397055")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("City Hall QSP").flat(true);

			MarkerOptions marker97 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444280"), Double
									.parseDouble("3.402340")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("City Mall QSP").flat(true);

			MarkerOptions marker98 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.436210"), Double
									.parseDouble("3.364495")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("CREEK ROAD").flat(true);

			MarkerOptions marker99 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.519554"), Double
									.parseDouble("3.327479")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("DALEKO Branch").flat(true);

			MarkerOptions marker100 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.505287"), Double
									.parseDouble("3.381173")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("DOMINO CASH CENTER (YABA)").flat(true);

			MarkerOptions marker101 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.617108"), Double
									.parseDouble("3.312788")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Dopemu Branch").flat(true);

			MarkerOptions marker102 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.507804"), Double
									.parseDouble("3.592470")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Dosunmu Branch").flat(true);

			MarkerOptions marker103 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.485720"), Double
									.parseDouble("3.388029")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ebute Metta Branch").flat(true);

			MarkerOptions marker104 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.552000"), Double
									.parseDouble("3.307000")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("EJIGBO Agency").flat(true);

			MarkerOptions marker105 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430031"), Double
									.parseDouble("3.430219")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Eko Hotel Branch").flat(true);

			MarkerOptions marker106 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.462684"), Double
									.parseDouble("3.389359")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Enu-Owa Agency").flat(true);

			MarkerOptions marker107 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.594595"), Double
									.parseDouble("3.977639")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Epe Branch").flat(true);

			MarkerOptions marker108 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.461300"), Double
									.parseDouble("3.458670")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Etisalat Agency").flat(true);

			MarkerOptions marker109 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442010"), Double
									.parseDouble("3.417631")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Falomo Shopping Centre Branch").flat(true);

			MarkerOptions marker110 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.454811"), Double
									.parseDouble("3.434691")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Federal Secretariat Branch").flat(true);

			MarkerOptions marker111 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.468089"), Double
									.parseDouble("3.285545")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("FESTAC Branch").flat(true);

			MarkerOptions marker112 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.471319"), Double
									.parseDouble("3.319313")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("FESTAC 2").flat(true);

			MarkerOptions marker113 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.466445"), Double
									.parseDouble("3.379206")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("GOLDEN TULIP HOTEL QSP").flat(true);

			MarkerOptions marker114 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.461949"), Double
									.parseDouble("3.383831")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Gorodomu Branch").flat(true);

			MarkerOptions marker116 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.485720"), Double
									.parseDouble("3.388029")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Iddo Branch").flat(true);

			MarkerOptions marker117 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.587110"), Double
									.parseDouble("3.286114")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Idimu Branch").flat(true);

			MarkerOptions marker118 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.647452"), Double
									.parseDouble("3.300627")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ifako Gbagada Branch").flat(true);

			MarkerOptions marker119 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.481378"), Double
									.parseDouble("3.361504")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Iganmu Branch").flat(true);

			MarkerOptions marker120 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.468446"), Double
									.parseDouble("3.367254")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ijora Branch").flat(true);

			MarkerOptions marker121 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.642644"), Double
									.parseDouble("3.323412")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Iju Branch").flat(true);

			MarkerOptions marker122 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.598134"), Double
									.parseDouble("3.353417")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ikeja Allen Avenue Branch").flat(true);

			MarkerOptions marker123 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.636999"), Double
									.parseDouble("3.315727")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ikeja Branch").flat(true);

			MarkerOptions marker124 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.612325"), Double
									.parseDouble("3.335936")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ikeja Industrial Estate Branch").flat(true);

			MarkerOptions marker125 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.624305"), Double
									.parseDouble("3.494602")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ikorodu Branch").flat(true);

			MarkerOptions marker126 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430031"), Double
									.parseDouble("3.365344")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ikeja Military Cantonment Branch").flat(true);

			MarkerOptions marker127 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.472940"), Double
									.parseDouble("3.597668")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ikota Branch").flat(true);

			MarkerOptions marker128 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.462051"), Double
									.parseDouble("3.543320")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ikota Cash Centre").flat(true);

			MarkerOptions marker129 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.550574"), Double
									.parseDouble("3.268333")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ikotun 1 Branch").flat(true);

			MarkerOptions marker130 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.516106"), Double
									.parseDouble("3.262028")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ikotun 2 Branch").flat(true);

			MarkerOptions marker131 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.530369"), Double
									.parseDouble("3.343927")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ikotun Egbe QSP").flat(true);

			MarkerOptions marker132 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.533333"), Double
									.parseDouble("3.350000")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ilupeju Branch").flat(true);

			MarkerOptions marker133 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453695"), Double
									.parseDouble("3.389512")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Investment House Branch").flat(true);

			MarkerOptions marker134 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438650"), Double
									.parseDouble("3.371480")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("ISOLO IND. ESTATE").flat(true);

			MarkerOptions marker135 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.531492"), Double
									.parseDouble("3.335938")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("ISOLO MAIN").flat(true);

			MarkerOptions marker136 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600000"), Double
									.parseDouble("3.500000")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ita Elewa Ikorodu Branch").flat(true);

			MarkerOptions marker137 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.505660"), Double
									.parseDouble("3.390314")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("IWAYA QSP (UNILAG BRANCH)").flat(true);

			MarkerOptions marker138 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.619514"), Double
									.parseDouble("3.299869")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Iyana Ipaja Branch").flat(true);

			MarkerOptions marker139 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.521101"), Double
									.parseDouble("3.371102")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Jibowu Branch").flat(true);

			MarkerOptions marker140 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.556488"), Double
									.parseDouble("3.347876")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Kairo Market Agency").flat(true);

			MarkerOptions marker141 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.447461"), Double
									.parseDouble("3.415040")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Keffi Branch").flat(true);

			MarkerOptions marker142 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.604248"), Double
									.parseDouble("3.390883")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ketu Branch").flat(true);

			MarkerOptions marker143 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.439697"), Double
									.parseDouble("3.369825")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("KOFO ABAYOMI").flat(true);

			MarkerOptions marker144 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.618860"), Double
									.parseDouble("3.300407")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Kollington QSP").flat(true);

			MarkerOptions marker145 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.451397"), Double
									.parseDouble("3.396397")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Lapal House Branch").flat(true);

			MarkerOptions marker146 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.530239"), Double
									.parseDouble("3.344255")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("LAPOTECH QSP").flat(true);

			MarkerOptions marker147 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.502463"), Double
									.parseDouble("3.374893")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("LASPOTECH CASH CENTRE").flat(true);

			MarkerOptions marker148 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460204"), Double
									.parseDouble("3.165468")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("LASU AGENCY").flat(true);

			MarkerOptions marker149 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.511148"), Double
									.parseDouble("3.339403")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Lawanson Branch").flat(true);

			MarkerOptions marker150 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.443308"), Double
									.parseDouble("3.458753")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Lekki Admarilty Way Lekki").flat(true);

			MarkerOptions marker151 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.533007"), Double
									.parseDouble("3.358864")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Lekki Branch").flat(true);

			MarkerOptions marker152 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.559816"), Double
									.parseDouble("3.334856")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Mafoluku QSP").flat(true);

			MarkerOptions marker153 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.452070"), Double
									.parseDouble("3.388239")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Marina Branch").flat(true);

			MarkerOptions marker154 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.541722"), Double
									.parseDouble("3.343349")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Matori Branch").flat(true);

			MarkerOptions marker155 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.472940"), Double
									.parseDouble("3.597668")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Mayfair Gardens Branch").flat(true);

			MarkerOptions marker156 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458928"), Double
									.parseDouble("3.304246")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("MAZA-MAZA Branch").flat(true);

			MarkerOptions marker157 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.581818"), Double
									.parseDouble("3.321135")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("MMIA Branch").flat(true);

			MarkerOptions marker158 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455098"), Double
									.parseDouble("3.356747")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Mobil Road Branch").flat(true);

			MarkerOptions marker159 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.447521"), Double
									.parseDouble("3.406022")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Moloney Branch").flat(true);

			MarkerOptions marker160 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.495215"), Double
									.parseDouble("3.377601")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Murtala Mohammed Way Branch").flat(true);

			MarkerOptions marker161 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.552176"), Double
									.parseDouble("3.353047")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Mushin Branch").flat(true);

			MarkerOptions marker162 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.445291"), Double
									.parseDouble("3.263392")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Navy Town Branch").flat(true);

			MarkerOptions marker163 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444641"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Niger House Branch").flat(true);

			MarkerOptions marker164 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.423823"), Double
									.parseDouble("3.293578")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("NIGERDOCK MINI BRANCH SNAKE ISLAND").flat(true);

			MarkerOptions marker165 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431127"), Double
									.parseDouble("3.424576")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("NIJ Branch").flat(true);

			MarkerOptions marker166 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.450000"), Double
									.parseDouble("3.366667")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title(" NIPCO CASH AGENCY").flat(true);

			MarkerOptions marker167 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.612325"), Double
									.parseDouble("3.335936")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Oba Akran Branch").flat(true);

			MarkerOptions marker168 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.461338"), Double
									.parseDouble("3.387165")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Obun - Eko Branch").flat(true);

			MarkerOptions marker169 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.524496"), Double
									.parseDouble("3.379170")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("ODUN ADE CASH CENTRE").flat(true);

			MarkerOptions marker170 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.621413"), Double
									.parseDouble("3.336593")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ogba Branch").flat(true);

			MarkerOptions marker171 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.631662"), Double
									.parseDouble("3.340252")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ogba II Branch").flat(true);

			MarkerOptions marker172 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.574911"), Double
									.parseDouble("3.391811")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ogudu Branch").flat(true);

			MarkerOptions marker173 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.639775"), Double
									.parseDouble("3.368130")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Ojodu - Isheri Branch").flat(true);

			MarkerOptions marker174 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.529999"), Double
									.parseDouble("3.354469")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("OJUWOYE CASH CENTER (ILUPEJU BRANCH)").flat(true);

			MarkerOptions marker175 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.668897"), Double
									.parseDouble("3.325151")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Oke Aro QSP").flat(true);

			MarkerOptions marker176 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.620765"), Double
									.parseDouble("3.303777")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Oke Odo Branch").flat(true);

			MarkerOptions marker177 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.544633"), Double
									.parseDouble("3.400503")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Oke-Arin Branch").flat(true);

			MarkerOptions marker178 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.507282"), Double
									.parseDouble("3.310140")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("OKOTA Branch").flat(true);

			MarkerOptions marker179 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430413"), Double
									.parseDouble("3.456945")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Oniru Branch").flat(true);

			MarkerOptions marker180 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458985"), Double
									.parseDouble("3.601521")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Oniru QSP").flat(true);

			MarkerOptions marker181 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.590644"), Double
									.parseDouble("3.359696")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Opebi Branch").flat(true);

			MarkerOptions marker182 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.598357"), Double
									.parseDouble("3.366659")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Oregun Industrial Estate Branch").flat(true);

			MarkerOptions marker183 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.434858"), Double
									.parseDouble("3.495726")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Osapa London Branch").flat(true);

			MarkerOptions marker184 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.462237"), Double
									.parseDouble("3.288179")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Oshodi - Mile 2 Expressway Branch").flat(true);

			MarkerOptions marker185 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.562012"), Double
									.parseDouble("3.348746")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Oshodi Branch").flat(true);

			MarkerOptions marker186 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.574147"), Double
									.parseDouble("3.486191")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Owode Branch").flat(true);

			MarkerOptions marker187 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.448536"), Double
									.parseDouble("3.367381")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("POINT ROAD").flat(true);

			MarkerOptions marker188 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431217"), Double
									.parseDouble("3.424997")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Sanusi Fafunwa Branch").flat(true);

			MarkerOptions marker189 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453941"), Double
									.parseDouble("3.389609")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Saudi Eko Branch").flat(true);

			MarkerOptions marker190 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.451209"), Double
									.parseDouble("3.422425")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("SEME BORDER").flat(true);

			MarkerOptions marker191 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.435419"), Double
									.parseDouble("3.366146")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Shell Agency Branch").flat(true);

			MarkerOptions marker192 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.570721"), Double
									.parseDouble("3.372329")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Shomolu Branch").flat(true);

			MarkerOptions marker193 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.603095"), Double
									.parseDouble("3.351388")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Starcomms Agency").flat(true);

			MarkerOptions marker194 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.454322"), Double
									.parseDouble("3.386083")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Stock Exchange House Branch").flat(true);

			MarkerOptions marker195 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.454093"), Double
									.parseDouble("3.402931")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Sura Market Agency").flat(true);

			MarkerOptions marker196 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460072"), Double
									.parseDouble("3.326754")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Suru Alaba Branch").flat(true);

			MarkerOptions marker197 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.512144"), Double
									.parseDouble("3.357697")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Surulere Branch").flat(true);

			MarkerOptions marker198 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.491343"), Double
									.parseDouble("3.356396")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Surulere Shopping Centre Branch").flat(true);

			MarkerOptions marker199 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.506645"), Double
									.parseDouble("3.368306")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Tejuosho Branch").flat(true);

			MarkerOptions marker200 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.596337"), Double
									.parseDouble("3.347153")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Toyin Olowu Branch").flat(true);

			MarkerOptions marker201 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.520164"), Double
									.parseDouble("3.399769")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("UNILAG CASH CENTER (UNILAG BRANCH)").flat(true);

			MarkerOptions marker202 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.513669"), Double
									.parseDouble("3.402527")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("University of Lagos Branch").flat(true);

			MarkerOptions marker203 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460891"), Double
									.parseDouble("3.554781")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Victoria Garden City Branch").flat(true);

			MarkerOptions marker204 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453425"), Double
									.parseDouble("3.389592")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Western House Branch").flat(true);

			MarkerOptions marker205 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.479268"), Double
									.parseDouble("3.385473")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Willoughby Branch").flat(true);

			MarkerOptions marker206 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.505226"), Double
									.parseDouble(" 3.378523")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.firstbank))

					.title("Yaba Branch").flat(true);
			// CitiBank ATM Locations

			MarkerOptions marker207 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.433355"), Double
									.parseDouble("3.424837")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.citibank))

					.title("Victoria Island Branch").flat(true);

			MarkerOptions marker208 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.435407"), Double
									.parseDouble("3.366100")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.citibank))

					.title("Apapa Branch").flat(true);

			MarkerOptions marker209 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.604731"), Double
									.parseDouble("3.350408")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.citibank))

					.title("Ikeja Branch").flat(true);

			MarkerOptions marker210 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459621"), Double
									.parseDouble("3.386811")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.citibank))

					.title("Idumota Branch").flat(true);

			// Access Bank ATM Locations
			MarkerOptions marker211 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.491320"), Double
									.parseDouble("3.356569")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Adeniran Ogunsanya Branch").flat(true);

			MarkerOptions marker212 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.613778"), Double
									.parseDouble("3.346025")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Adeniyi Jones Branch").flat(true);

			MarkerOptions marker213 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.437527"), Double
									.parseDouble("3.427856")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Adeola Hopewell Branch").flat(true);

			MarkerOptions marker214 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430306"), Double
									.parseDouble("3.415599")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Adeola Odeku Branch").flat(true);

			MarkerOptions marker215 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430336"), Double
									.parseDouble("3.416127")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Adeola Odeku(2) Branch").flat(true);

			MarkerOptions marker216 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431109"), Double
									.parseDouble("3.424660")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Adetokunbo Ademola Branch").flat(true);

			MarkerOptions marker217 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.433355"), Double
									.parseDouble("3.424837")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Adeyemo Alakija Branch").flat(true);

			MarkerOptions marker218 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.620765"), Double
									.parseDouble("3.303777")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Agege Branch").flat(true);

			MarkerOptions marker219 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.496249"), Double
									.parseDouble("3.340090")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Aguda Branch").flat(true);

			MarkerOptions marker220 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.551417"), Double
									.parseDouble("3.330633")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ajao Estate Branch").flat(true);

			MarkerOptions marker221 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430981"), Double
									.parseDouble("3.430790")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ajose Adeogun Branch").flat(true);

			MarkerOptions marker222 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430050"), Double
									.parseDouble("3.423508")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("AIS/USL Branch").flat(true);

			MarkerOptions marker223 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.631205"), Double
									.parseDouble("3.321252")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Alagbado Branch").flat(true);

			MarkerOptions marker224 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.613939"), Double
									.parseDouble("3.355366")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Alausa Branch").flat(true);

			MarkerOptions marker225 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.447933"), Double
									.parseDouble("3.409792")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Alfred Rewane Road Branch").flat(true);

			MarkerOptions marker226 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.604458"), Double
									.parseDouble("3.351066")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Allen Avenue Branch").flat(true);

			MarkerOptions marker227 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.605823"), Double
									.parseDouble("3.349845")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Allen Avenue(2) Branch").flat(true);

			MarkerOptions marker228 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.450993"), Double
									.parseDouble("3.422171")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Aluko & Oyebode Branch").flat(true);

			MarkerOptions marker229 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.461914"), Double
									.parseDouble("3.157888")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Aspamda Branch").flat(true);

			MarkerOptions marker230 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.434437"), Double
									.parseDouble("3.480987")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Avalon House Branch").flat(true);

			MarkerOptions marker231 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444464"), Double
									.parseDouble("3.424111")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Awolowo Road Branch").flat(true);

			MarkerOptions marker232 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442569"), Double
									.parseDouble("3.420384")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Awolowo Road(2) Branch").flat(true);

			MarkerOptions marker233 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.604505"), Double
									.parseDouble("3.265937")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ayobo Branch").flat(true);

			MarkerOptions marker234 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.490126"), Double
									.parseDouble("3.354747")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Bode Thomas Branch").flat(true);

			MarkerOptions marker235 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453559"), Double
									.parseDouble("3.389415")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Broad Street Branch").flat(true);

			MarkerOptions marker236 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453539"), Double
									.parseDouble("3.389551")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Broad Street(2) Branch").flat(true);

			MarkerOptions marker237 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440612"), Double
									.parseDouble("3.375243")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Burma Road Apapa Branch").flat(true);

			MarkerOptions marker238 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.454107"), Double
									.parseDouble("3.432666")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Casalydia Branch").flat(true);

			MarkerOptions marker239 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438147"), Double
									.parseDouble("3.372780")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Commercial Road Branch").flat(true);

			MarkerOptions marker240 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.435671"), Double
									.parseDouble("3.365721")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Creek Road Branch").flat(true);

			MarkerOptions marker241 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453457"), Double
									.parseDouble("3.389311")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("CSS Bookshop Branch").flat(true);

			MarkerOptions marker242 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.519554"), Double
									.parseDouble("3.327479")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Daleko Branch").flat(true);

			MarkerOptions marker243 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.659350"), Double
									.parseDouble("3.292328")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Dopemu Branch").flat(true);

			MarkerOptions marker244 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.628888"), Double
									.parseDouble("3.337987")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Egbeda Branch").flat(true);

			MarkerOptions marker245 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.530204"), Double
									.parseDouble("3.303644")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ejigbo Branch").flat(true);

			MarkerOptions marker246 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.475960"), Double
									.parseDouble("3.279926")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Festac Branch").flat(true);

			MarkerOptions marker247 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.554140"), Double
									.parseDouble("3.347539")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Gbagada Branch").flat(true);

			MarkerOptions marker248 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429954"), Double
									.parseDouble("3.412111")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Idejo Branch").flat(true);

			MarkerOptions marker249 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.428269"), Double
									.parseDouble("3.412118")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Idejo Street Branch").flat(true);

			MarkerOptions marker250 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.594268"), Double
									.parseDouble("3.287842")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Idimu Branch").flat(true);

			MarkerOptions marker251 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.552627"), Double
									.parseDouble("3.391306")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ifako-Gbagada Branch").flat(true);

			MarkerOptions marker252 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.513669"), Double
									.parseDouble("3.402527")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ijeshatedo Branch").flat(true);

			MarkerOptions marker253 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.591350"), Double
									.parseDouble("3.970968")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Iju Branch").flat(true);

			MarkerOptions marker254 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.619636"), Double
									.parseDouble("3.503432")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ikorodu Branch").flat(true);

			MarkerOptions marker255 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.624305"), Double
									.parseDouble("3.494602")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ikorodu(2) Branch").flat(true);

			MarkerOptions marker256 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.649561"), Double
									.parseDouble(" 3.518328")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ikorodu Road Branch").flat(true);

			MarkerOptions marker257 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.539055"), Double
									.parseDouble("3.286535")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ikotun Branch").flat(true);

			MarkerOptions marker258 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.538844"), Double
									.parseDouble("3.356549")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ilupeju Branch").flat(true);

			MarkerOptions marker259 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.556950"), Double
									.parseDouble("3.363518")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ilupeju(2) Branch").flat(true);

			MarkerOptions marker260 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.663199"), Double
									.parseDouble("3.283547")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ipaja Branch").flat(true);

			MarkerOptions marker261 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.520184"), Double
									.parseDouble("3.323155")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ire Akari Branch").flat(true);

			MarkerOptions marker262 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.515733"), Double
									.parseDouble("3.319681")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Isolo Branch").flat(true);

			MarkerOptions marker263 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455652"), Double
									.parseDouble("3.382714")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Issa Williams Branch").flat(true);

			MarkerOptions marker264 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.603071"), Double
									.parseDouble("3.389915")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ketu Branch").flat(true);

			MarkerOptions marker265 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460530"), Double
									.parseDouble("3.385828")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Kosoko Street Branch").flat(true);

			MarkerOptions marker266 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.499098"), Double
									.parseDouble("3.091407")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Lasu Branch").flat(true);

			MarkerOptions marker267 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.511169"), Double
									.parseDouble("3.338359")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Lawanson Branch").flat(true);

			MarkerOptions marker268 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458985"), Double
									.parseDouble("3.601521")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Lekki Branch").flat(true);

			MarkerOptions marker269 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.432266"), Double
									.parseDouble("3.440493")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ligali Ayorinde Branch").flat(true);

			MarkerOptions marker270 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.434820"), Double
									.parseDouble("3.440483")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ligali Annex Branch").flat(true);

			MarkerOptions marker271 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.452514"), Double
									.parseDouble("3.390239")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Marina B.O.I. Branch").flat(true);

			MarkerOptions marker272 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.450329"), Double
									.parseDouble("3.390610")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Marina Branch").flat(true);

			MarkerOptions marker273 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.572026"), Double
									.parseDouble("3.366511")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Maryland Branch").flat(true);

			MarkerOptions marker274 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.537044"), Double
									.parseDouble("3.346589")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Matori Branch").flat(true);

			MarkerOptions marker275 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.456595"), Double
									.parseDouble("3.302463")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Maza Maza Branch").flat(true);

			MarkerOptions marker276 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.448570"), Double
									.parseDouble("3.406520")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Moloney Branch").flat(true);

			MarkerOptions marker277 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431965"), Double
									.parseDouble("3.431930")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Muri Okunola Branch").flat(true);

			MarkerOptions marker278 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.485720"), Double
									.parseDouble("3.388029")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Muritala Mohamed Way Branch").flat(true);

			MarkerOptions marker279 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.527188"), Double
									.parseDouble("3.354507")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Mushin Branch").flat(true);

			MarkerOptions marker280 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.577359"), Double
									.parseDouble("3.333956")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Nahco Branch").flat(true);

			MarkerOptions marker281 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453711"), Double
									.parseDouble("3.389654")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Nnamdi Azikwe Idumota Branch").flat(true);

			MarkerOptions marker282 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.472940"), Double
									.parseDouble("3.597668")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Oando Ikota Branch").flat(true);

			MarkerOptions marker283 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.614044"), Double
									.parseDouble("3.361640")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Oba Akran Branch").flat(true);

			MarkerOptions marker284 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.465427"), Double
									.parseDouble("3.390012")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Obas Palace Branch").flat(true);

			MarkerOptions marker285 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.598699"), Double
									.parseDouble("3.342881")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Obafemi Awolowo Way Branch").flat(true);

			MarkerOptions marker286 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.638627"), Double
									.parseDouble("3.525839")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Odogunyan Branch").flat(true);

			MarkerOptions marker287 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.628888"), Double
									.parseDouble("3.337987")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ogba Branch").flat(true);

			MarkerOptions marker288 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.503890"), Double
									.parseDouble("3.351442")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ogunlana Drive Branch").flat(true);

			MarkerOptions marker289 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.638997"), Double
									.parseDouble("3.361653")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ojodu Berger Branch").flat(true);

			MarkerOptions marker290 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.574359"), Double
									.parseDouble("3.393584")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ojota Branch").flat(true);

			MarkerOptions marker291 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.509891"), Double
									.parseDouble("3.364440")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Ojuelegba Branch").flat(true);

			MarkerOptions marker292 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458169"), Double
									.parseDouble("3.290573")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Old Ojo Branch").flat(true);

			MarkerOptions marker293 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455280"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Olodi-Apapa Branch").flat(true);

			MarkerOptions marker294 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.446401"), Double
									.parseDouble("3.405665")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Onikan Branch").flat(true);

			MarkerOptions marker295 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.593021"), Double
									.parseDouble("3.358375")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Opebi Branch").flat(true);

			MarkerOptions marker296 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.599442"), Double
									.parseDouble("3.365735")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Oregun Branch").flat(true);

			MarkerOptions marker297 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601838"), Double
									.parseDouble("3.351486")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Oshodi Branch").flat(true);

			MarkerOptions marker298 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.534762"), Double
									.parseDouble("3.347028")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Palm Avenue Branch").flat(true);

			MarkerOptions marker299 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.448718"), Double
									.parseDouble("3.367120")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Point Road Branch").flat(true);

			MarkerOptions marker300 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.445951"), Double
									.parseDouble("3.404037")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Resort Savings Branch").flat(true);
			
			googleMap.addMarker(marker1);
			googleMap.addMarker(marker2);
			googleMap.addMarker(marker3);
			googleMap.addMarker(marker4);
			googleMap.addMarker(marker5);
			googleMap.addMarker(marker6);
			googleMap.addMarker(marker7);
			googleMap.addMarker(marker8);
			googleMap.addMarker(marker9);
			googleMap.addMarker(marker10);
			googleMap.addMarker(marker11);
			googleMap.addMarker(marker12);
			googleMap.addMarker(marker13);
			googleMap.addMarker(marker14);
			googleMap.addMarker(marker15);
			googleMap.addMarker(marker16);
			googleMap.addMarker(marker17);
			googleMap.addMarker(marker18);
			googleMap.addMarker(marker19);
			googleMap.addMarker(marker20);
			googleMap.addMarker(marker21);
			googleMap.addMarker(marker22);
			googleMap.addMarker(marker23);
			googleMap.addMarker(marker25);
			googleMap.addMarker(marker26);
			googleMap.addMarker(marker27);
			googleMap.addMarker(marker28);
			googleMap.addMarker(marker29);
			googleMap.addMarker(marker30);
			googleMap.addMarker(marker31);
			googleMap.addMarker(marker32);
			googleMap.addMarker(marker33);
			googleMap.addMarker(marker34);
			googleMap.addMarker(marker35);
			googleMap.addMarker(marker38);
			googleMap.addMarker(marker39);
			googleMap.addMarker(marker40);
			googleMap.addMarker(marker41);
			googleMap.addMarker(marker42);
			googleMap.addMarker(marker43);
			googleMap.addMarker(marker44);
			googleMap.addMarker(marker45);
			googleMap.addMarker(marker46);
			googleMap.addMarker(marker47);
			googleMap.addMarker(marker48);
			googleMap.addMarker(marker49);
			googleMap.addMarker(marker50);
			googleMap.addMarker(marker51);
			googleMap.addMarker(marker52);
			googleMap.addMarker(marker53);
			googleMap.addMarker(marker54);
			googleMap.addMarker(marker55);
			googleMap.addMarker(marker56);
			googleMap.addMarker(marker57);
			googleMap.addMarker(marker57a);
			googleMap.addMarker(marker58);
			googleMap.addMarker(marker58a);
			googleMap.addMarker(marker59);
			googleMap.addMarker(marker60);
			googleMap.addMarker(marker61);
			googleMap.addMarker(marker62);
			googleMap.addMarker(marker63);
			googleMap.addMarker(marker64);
			googleMap.addMarker(marker65);
			googleMap.addMarker(marker66);
			googleMap.addMarker(marker67);
			googleMap.addMarker(marker68);
			googleMap.addMarker(marker69);
			googleMap.addMarker(marker70);
			googleMap.addMarker(marker71);
			googleMap.addMarker(marker72);
			googleMap.addMarker(marker73);
			googleMap.addMarker(marker74);
			googleMap.addMarker(marker75);
			googleMap.addMarker(marker76);
			googleMap.addMarker(marker77);
			googleMap.addMarker(marker78);
			googleMap.addMarker(marker79);
			googleMap.addMarker(marker80);
			googleMap.addMarker(marker81);
			googleMap.addMarker(marker82);
			googleMap.addMarker(marker83);
			googleMap.addMarker(marker84);
			googleMap.addMarker(marker85);
			googleMap.addMarker(marker86);
			googleMap.addMarker(marker87);
			googleMap.addMarker(marker88);
			googleMap.addMarker(marker89);
			googleMap.addMarker(marker90);
			googleMap.addMarker(marker91);
			googleMap.addMarker(marker92);
			googleMap.addMarker(marker93);
			googleMap.addMarker(marker94);
			googleMap.addMarker(marker95);
			googleMap.addMarker(marker96);
			googleMap.addMarker(marker97);
			googleMap.addMarker(marker98);
			googleMap.addMarker(marker99);
			googleMap.addMarker(marker100);
			googleMap.addMarker(marker101);
			googleMap.addMarker(marker102);
			googleMap.addMarker(marker103);
			googleMap.addMarker(marker104);
			googleMap.addMarker(marker105);
			googleMap.addMarker(marker106);
			googleMap.addMarker(marker107);
			googleMap.addMarker(marker108);
			googleMap.addMarker(marker109);
			googleMap.addMarker(marker110);
			googleMap.addMarker(marker111);
			googleMap.addMarker(marker112);
			googleMap.addMarker(marker113);
			googleMap.addMarker(marker114);
			googleMap.addMarker(marker116);
			googleMap.addMarker(marker117);
			googleMap.addMarker(marker118);
			googleMap.addMarker(marker119);
			googleMap.addMarker(marker120);
			googleMap.addMarker(marker121);
			googleMap.addMarker(marker122);
			googleMap.addMarker(marker123);
			googleMap.addMarker(marker124);
			googleMap.addMarker(marker125);
			googleMap.addMarker(marker126);
			googleMap.addMarker(marker127);
			googleMap.addMarker(marker128);
			googleMap.addMarker(marker129);
			googleMap.addMarker(marker130);
			googleMap.addMarker(marker131);
			googleMap.addMarker(marker132);
			googleMap.addMarker(marker133);
			googleMap.addMarker(marker134);
			googleMap.addMarker(marker135);
			googleMap.addMarker(marker136);
			googleMap.addMarker(marker137);
			googleMap.addMarker(marker138);
			googleMap.addMarker(marker139);
			googleMap.addMarker(marker140);
			googleMap.addMarker(marker141);
			googleMap.addMarker(marker142);
			googleMap.addMarker(marker143);
			googleMap.addMarker(marker144);
			googleMap.addMarker(marker145);
			googleMap.addMarker(marker146);
			googleMap.addMarker(marker147);
			googleMap.addMarker(marker148);
			googleMap.addMarker(marker149);
			googleMap.addMarker(marker150);
			googleMap.addMarker(marker151);
			googleMap.addMarker(marker152);
			googleMap.addMarker(marker153);
			googleMap.addMarker(marker154);
			googleMap.addMarker(marker155);
			googleMap.addMarker(marker156);
			googleMap.addMarker(marker157);
			googleMap.addMarker(marker158);
			googleMap.addMarker(marker159);
			googleMap.addMarker(marker160);
			googleMap.addMarker(marker161);
			googleMap.addMarker(marker162);
			googleMap.addMarker(marker163);
			googleMap.addMarker(marker164);
			googleMap.addMarker(marker165);
			googleMap.addMarker(marker166);
			googleMap.addMarker(marker167);
			googleMap.addMarker(marker168);
			googleMap.addMarker(marker169);
			googleMap.addMarker(marker170);
			googleMap.addMarker(marker171);
			googleMap.addMarker(marker172);
			googleMap.addMarker(marker173);
			googleMap.addMarker(marker174);
			googleMap.addMarker(marker175);
			googleMap.addMarker(marker176);
			googleMap.addMarker(marker177);
			googleMap.addMarker(marker178);
			googleMap.addMarker(marker179);
			googleMap.addMarker(marker180);
			googleMap.addMarker(marker181);
			googleMap.addMarker(marker182);
			googleMap.addMarker(marker183);
			googleMap.addMarker(marker184);
			googleMap.addMarker(marker185);
			googleMap.addMarker(marker186);
			googleMap.addMarker(marker187);
			googleMap.addMarker(marker188);
			googleMap.addMarker(marker189);
			googleMap.addMarker(marker190);
			googleMap.addMarker(marker191);
			googleMap.addMarker(marker192);
			googleMap.addMarker(marker193);
			googleMap.addMarker(marker194);
			googleMap.addMarker(marker195);
			googleMap.addMarker(marker196);
			googleMap.addMarker(marker197);
			googleMap.addMarker(marker198);
			googleMap.addMarker(marker199);
			googleMap.addMarker(marker200);
			googleMap.addMarker(marker201);
			googleMap.addMarker(marker202);
			googleMap.addMarker(marker203);
			googleMap.addMarker(marker204);
			googleMap.addMarker(marker205);
			googleMap.addMarker(marker206);
			googleMap.addMarker(marker207);
			googleMap.addMarker(marker208);
			googleMap.addMarker(marker209);
			googleMap.addMarker(marker210);
			googleMap.addMarker(marker211);
			googleMap.addMarker(marker212);
			googleMap.addMarker(marker213);
			googleMap.addMarker(marker214);
			googleMap.addMarker(marker215);
			googleMap.addMarker(marker216);
			googleMap.addMarker(marker217);
			googleMap.addMarker(marker218);
			googleMap.addMarker(marker219);
			googleMap.addMarker(marker220);
			googleMap.addMarker(marker221);
			googleMap.addMarker(marker222);
			googleMap.addMarker(marker223);
			googleMap.addMarker(marker224);
			googleMap.addMarker(marker225);
			googleMap.addMarker(marker226);
			googleMap.addMarker(marker227);
			googleMap.addMarker(marker228);
			googleMap.addMarker(marker229);
			googleMap.addMarker(marker230);
			googleMap.addMarker(marker231);
			googleMap.addMarker(marker232);
			googleMap.addMarker(marker233);
			googleMap.addMarker(marker234);
			googleMap.addMarker(marker235);
			googleMap.addMarker(marker236);
			googleMap.addMarker(marker237);
			googleMap.addMarker(marker238);
			googleMap.addMarker(marker239);
			googleMap.addMarker(marker240);
			googleMap.addMarker(marker241);
			googleMap.addMarker(marker242);
			googleMap.addMarker(marker243);
			googleMap.addMarker(marker244);
			googleMap.addMarker(marker245);
			googleMap.addMarker(marker246);
			googleMap.addMarker(marker247);
			googleMap.addMarker(marker248);
			googleMap.addMarker(marker249);
			googleMap.addMarker(marker250);
			googleMap.addMarker(marker251);
			googleMap.addMarker(marker252);
			googleMap.addMarker(marker253);
			googleMap.addMarker(marker254);
			googleMap.addMarker(marker255);
			googleMap.addMarker(marker256);
			googleMap.addMarker(marker257);
			googleMap.addMarker(marker258);
			googleMap.addMarker(marker259);
			googleMap.addMarker(marker260);
			googleMap.addMarker(marker261);
			googleMap.addMarker(marker262);
			googleMap.addMarker(marker263);
			googleMap.addMarker(marker264);
			googleMap.addMarker(marker265);
			googleMap.addMarker(marker266);
			googleMap.addMarker(marker267);
			googleMap.addMarker(marker268);
			googleMap.addMarker(marker269);
			googleMap.addMarker(marker270);
			googleMap.addMarker(marker271);
			googleMap.addMarker(marker272);
			googleMap.addMarker(marker273);
			googleMap.addMarker(marker274);
			googleMap.addMarker(marker275);
			googleMap.addMarker(marker276);
			googleMap.addMarker(marker277);
			googleMap.addMarker(marker278);
			googleMap.addMarker(marker279);
			googleMap.addMarker(marker280);
			googleMap.addMarker(marker281);
			googleMap.addMarker(marker282);
			googleMap.addMarker(marker283);
			googleMap.addMarker(marker284);
			googleMap.addMarker(marker285);
			googleMap.addMarker(marker286);
			googleMap.addMarker(marker287);
			googleMap.addMarker(marker288);
			googleMap.addMarker(marker289);
			googleMap.addMarker(marker290);
			googleMap.addMarker(marker291);
			googleMap.addMarker(marker292);
			googleMap.addMarker(marker293);
			googleMap.addMarker(marker294);
			googleMap.addMarker(marker295);
			googleMap.addMarker(marker296);
			googleMap.addMarker(marker297);
			googleMap.addMarker(marker298);
			googleMap.addMarker(marker299);
			googleMap.addMarker(marker300);

		}

		private void markersTwo() {
			MarkerOptions marker301 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.505717"), Double
									.parseDouble("3.377993")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Sabo Branch").flat(true);

			MarkerOptions marker302 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.427023"), Double
									.parseDouble("3.421173")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Saka Tinubu Branch").flat(true);

			MarkerOptions marker303 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.454908"), Double
									.parseDouble("3.257607")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Satellite Town Branch").flat(true);

			MarkerOptions marker304 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.594850"), Double
									.parseDouble("3.338498")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Simbiat Abiola Branch").flat(true);

			MarkerOptions marker305 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.531762"), Double
									.parseDouble("3.380467")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Somolu Branch").flat(true);

			MarkerOptions marker306 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.522806"), Double
									.parseDouble("3.385982")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("St Finbarrs Branch").flat(true);

			MarkerOptions marker307 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.506645"), Double
									.parseDouble("3.368306")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Tejuosho Branch").flat(true);

			MarkerOptions marker308 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.596325"), Double
									.parseDouble("3.347881")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Toyin Street Branch").flat(true);

			MarkerOptions marker309 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.519240"), Double
									.parseDouble("3.391757")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("University Of Lagos Branch").flat(true);

			MarkerOptions marker310 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.445259"), Double
									.parseDouble("3.371347")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Wharf Road Branch").flat(true);

			MarkerOptions marker311 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.448258"), Double
									.parseDouble("3.331141")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.accessbank))

					.title("Tincan Branch").flat(true);
			MarkerOptions marker312 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.648185"), Double
									.parseDouble("3.305788")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Abule Egba Branch").flat(true);

			MarkerOptions marker313 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.605831"), Double
									.parseDouble("3.349841")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Allen Branch").flat(true);

			MarkerOptions marker314 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444465"), Double
									.parseDouble("3.425174")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Awolowo Road Branch").flat(true);

			MarkerOptions marker315 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.457696"), Double
									.parseDouble("3.384947")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Balogun Branch").flat(true);

			MarkerOptions marker316 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429636"), Double
									.parseDouble("3.40952")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Bar Beach Branch").flat(true);

			MarkerOptions marker317 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438096"), Double
									.parseDouble("3.368179")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Burma Road 1 Branch").flat(true);

			MarkerOptions marker318 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.437433"), Double
									.parseDouble("3.371878")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Creek Road Branch").flat(true);

			MarkerOptions marker319 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.449180"), Double
									.parseDouble("3.368195")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Eleganza Plaza Branch").flat(true);

			MarkerOptions marker320 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.462154"), Double
									.parseDouble("3.285871")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Festac Branch").flat(true);

			MarkerOptions marker321 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.555499"), Double
									.parseDouble("3.350641")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Ibafon Branch").flat(true);

			MarkerOptions marker322 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.433051"), Double
									.parseDouble("3.425816")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Iddo Branch").flat(true);

			MarkerOptions marker323 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.540799"), Double
									.parseDouble("3.356247")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Idi Oro Branch").flat(true);

			MarkerOptions marker324 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455374"), Double
									.parseDouble("3.384912")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Marina Branch").flat(true);

			MarkerOptions marker325 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.604248"), Double
									.parseDouble("3.390883")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Mile 12 Branch").flat(true);

			MarkerOptions marker326 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.537933"), Double
									.parseDouble("3.346039")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Mushin Branch").flat(true);

			MarkerOptions marker327 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601051"), Double
									.parseDouble("3.338637")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Oba Akran Branch").flat(true);

			MarkerOptions marker328 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.589014"), Double
									.parseDouble("3.361565")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Opebi Branch").flat(true);

			MarkerOptions marker329 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600453"), Double
									.parseDouble("3.364171")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Oregun Road Branch").flat(true);

			MarkerOptions marker330 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431107"), Double
									.parseDouble("3.428121")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Sanusi Fafunwa Road Branch").flat(true);

			MarkerOptions marker331 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.490125"), Double
									.parseDouble("3.35476")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Surulere Branch").flat(true);

			MarkerOptions marker332 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.433161"), Double
									.parseDouble("3.356054")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Tincan Port Branch").flat(true);

			MarkerOptions marker333 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.505226"), Double
									.parseDouble("3.378523")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unitybank))

					.title("Yaba Comm Avenue Branch").flat(true);

			// contd from here for heritage bank
			MarkerOptions marker334 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.617306"), Double
									.parseDouble("3.345972")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.heritagebank))

					.title("Adeniyi Jones Branch").flat(true);

			MarkerOptions marker335 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431109"), Double
									.parseDouble("3.42474")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.heritagebank))

					.title("Adetokunbo Ademola, VI Branch").flat(true);

			MarkerOptions marker336 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45528"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.heritagebank))

					.title("Apapa Branch").flat(true);

			MarkerOptions marker337 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6206"), Double
									.parseDouble("3.355075")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.heritagebank))

					.title("Ashabi Cole, Alausa Branch").flat(true);

			MarkerOptions marker338 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444312"), Double
									.parseDouble("3.424119")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.heritagebank))

					.title("Awolowo Road, Ikoyi Branch").flat(true);

			MarkerOptions marker339 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430981"), Double
									.parseDouble("3.43079")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.heritagebank))

					.title("Head Office").flat(true);

			MarkerOptions marker340 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.450707"), Double
									.parseDouble("3.393327")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.heritagebank))

					.title("Marina Branch").flat(true);

			MarkerOptions marker341 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.61886"), Double
									.parseDouble("3.300407")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Abule-Egba Branch").flat(true);

			MarkerOptions marker342 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.437527"), Double
									.parseDouble("3.427856")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Adeola Hopewell Branch").flat(true);

			MarkerOptions marker343 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430534"), Double
									.parseDouble("3.415569")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Adeola Odeku Branch").flat(true);

			MarkerOptions marker344 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.433967"), Double
									.parseDouble("3.42457")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Adeyemo Alakija Branch").flat(true);

			MarkerOptions marker345 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.427537"), Double
									.parseDouble("3.409279")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ahmadu Bello Way Branch").flat(true);
			MarkerOptions marker346 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.46926"), Double
									.parseDouble("3.574415")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ajah Branch").flat(true);

			MarkerOptions marker347 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.577359"), Double
									.parseDouble("3.333956")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ajao Estate Branch").flat(true);

			MarkerOptions marker348 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430998"), Double
									.parseDouble("3.434132")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ajose Adeogun Branch").flat(true);

			MarkerOptions marker349 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.522806"), Double
									.parseDouble("3.385982")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Akoka Branch").flat(true);

			MarkerOptions marker350 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.591574"), Double
									.parseDouble("3.291062")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Akowonjo (2) Branch").flat(true);

			MarkerOptions marker351 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.599254"), Double
									.parseDouble("3.294097")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Akowonjo Branch").flat(true);

			MarkerOptions marker352 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.488372"), Double
									.parseDouble("3.182987")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Alaba Branch").flat(true);

			MarkerOptions marker353 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.60167"), Double
									.parseDouble("3.35191")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Allen Branch").flat(true);

			MarkerOptions marker354 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458264"), Double
									.parseDouble("3.365785")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Amuwo Odofin Branch").flat(true);

			MarkerOptions marker355 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438158"), Double
									.parseDouble("3.372527")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Apapa (2) Branch").flat(true);

			MarkerOptions marker356 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.448533"), Double
									.parseDouble("3.367295")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Apapa (3) Branch").flat(true);

			MarkerOptions marker357 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45528"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Apapa Branch").flat(true);

			MarkerOptions marker358 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459993"), Double
									.parseDouble("3.230824")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Aspamda Branch").flat(true);

			MarkerOptions marker359 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6071"), Double
									.parseDouble("3.348513")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Awolowo Road Branch").flat(true);

			MarkerOptions marker360 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.608248"), Double
									.parseDouble("3.350109")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Awolowo Way Branch").flat(true);

			MarkerOptions marker361 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45615"), Double
									.parseDouble("3.382467")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Broad Street Branch").flat(true);

			MarkerOptions marker362 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438158"), Double
									.parseDouble("3.372527")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Commercial Road Branch").flat(true);

			MarkerOptions marker363 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.595759"), Double
									.parseDouble("3.340094")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Computer Village Branch").flat(true);

			MarkerOptions marker364 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442277"), Double
									.parseDouble("3.376492")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ebute Metta Branch").flat(true);

			MarkerOptions marker365 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.57987"), Double
									.parseDouble("3.973659")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Epe Branch").flat(true);

			MarkerOptions marker366 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459624"), Double
									.parseDouble("3.386802")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ereko Branch").flat(true);

			MarkerOptions marker367 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.462154"), Double
									.parseDouble("3.285871")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Festac Branch").flat(true);

			MarkerOptions marker368 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440859"), Double
									.parseDouble("3.331767")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ibafon Branch").flat(true);

			MarkerOptions marker369 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459575"), Double
									.parseDouble("3.387545")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Idumota Branch").flat(true);

			MarkerOptions marker370 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.497672"), Double
									.parseDouble("3.326374")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ijesha Branch").flat(true);

			MarkerOptions marker371 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.668958"), Double
									.parseDouble("3.326395")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Iju Branch").flat(true);

			MarkerOptions marker372 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.588305"), Double
									.parseDouble("3.362123")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ikeja (2) Branch").flat(true);

			MarkerOptions marker373 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.608219"), Double
									.parseDouble("3.362096")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ikeja (3) Branch").flat(true);

			MarkerOptions marker374 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.602002"), Double
									.parseDouble("3.338118")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ikeja (4) Branch").flat(true);

			MarkerOptions marker375 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.628888"), Double
									.parseDouble("3.337987")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ikeja (5) Branch").flat(true);

			MarkerOptions marker376 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.608248"), Double
									.parseDouble("3.350109")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ikeja Branch").flat(true);

			MarkerOptions marker377 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.621666"), Double
									.parseDouble("3.500503")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ikorodu Branch").flat(true);

			MarkerOptions marker378 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458985"), Double
									.parseDouble("3.601521")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ikota Branch").flat(true);

			MarkerOptions marker379 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.58711"), Double
									.parseDouble("3.286114")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ikotun Branch").flat(true);

			MarkerOptions marker380 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444402"), Double
									.parseDouble("3.42423")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ikoyi (2) Branch").flat(true);

			MarkerOptions marker381 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.556985"), Double
									.parseDouble("3.363519")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ilupeju (2) Branch").flat(true);

			MarkerOptions marker382 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.553648"), Double
									.parseDouble("3.356674")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ilupeju Branch").flat(true);

			MarkerOptions marker383 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.521598"), Double
									.parseDouble("3.322123")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Isolo Branch").flat(true);

			MarkerOptions marker384 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.606338"), Double
									.parseDouble("3.392326")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ketu Branch").flat(true);

			MarkerOptions marker385 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430928"), Double
									.parseDouble("3.427121")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Key Stone Head Office").flat(true);

			MarkerOptions marker386 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45528"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Kirikiri Branch").flat(true);

			MarkerOptions marker387 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.511148"), Double
									.parseDouble("3.339403")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Lawanson Branch").flat(true);

			MarkerOptions marker388 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.434858"), Double
									.parseDouble("3.495726")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Lekki Branch").flat(true);

			MarkerOptions marker389 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.599882"), Double
									.parseDouble("3.37306")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Magodo Branch").flat(true);

			MarkerOptions marker390 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.573246"), Double
									.parseDouble("3.363175")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Maryland Branch").flat(true);

			MarkerOptions marker391 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.535592"), Double
									.parseDouble("3.348665")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Mushin (2) Branch").flat(true);

			MarkerOptions marker392 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.527188"), Double
									.parseDouble("3.354507")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Mushin Branch").flat(true);

			MarkerOptions marker393 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.602002"), Double
									.parseDouble("3.338118")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Oba Akran Branch").flat(true);

			MarkerOptions marker394 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.574413"), Double
									.parseDouble("3.393515")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ogudu Branch").flat(true);

			MarkerOptions marker395 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.488372"), Double
									.parseDouble("3.182987")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Ojo Branch").flat(true);

			MarkerOptions marker396 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.636999"), Double
									.parseDouble("3.315727")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Oko-Oba Branch").flat(true);

			MarkerOptions marker397 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.637053"), Double
									.parseDouble("3.355236")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Omole Branch").flat(true);

			MarkerOptions marker398 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600111"), Double
									.parseDouble("3.364972")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Oregun Branch").flat(true);

			MarkerOptions marker399 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.52897"), Double
									.parseDouble("3.379794")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Orile (2) Branch").flat(true);

			MarkerOptions marker400 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459993"), Double
									.parseDouble("3.230824")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Orile Branch").flat(true);

			MarkerOptions marker401 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.54125"), Double
									.parseDouble("3.367015")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Palmgroove Branch").flat(true);

			MarkerOptions marker402 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.448533"), Double
									.parseDouble("3.367295")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Point Road Branch").flat(true);

			MarkerOptions marker403 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.496249"), Double
									.parseDouble("3.34009")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Surulere (2) Branch").flat(true);

			MarkerOptions marker404 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.488394"), Double
									.parseDouble("3.360574")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Surulere Branch").flat(true);

			MarkerOptions marker405 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.446721"), Double
									.parseDouble("3.401582")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Tafewa Balewa Square Branch").flat(true);

			MarkerOptions marker406 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.423566"), Double
									.parseDouble("3.426346")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Tiamiyu Savage Branch").flat(true);

			MarkerOptions marker407 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460506"), Double
									.parseDouble("3.250034")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Trade Fair Complex Branch").flat(true);

			MarkerOptions marker408 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45528"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Warehouse Road Apapa Branch").flat(true);

			MarkerOptions marker409 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.441594"), Double
									.parseDouble("3.377797")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Wharf Road Branch").flat(true);

			MarkerOptions marker410 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.479268"), Double
									.parseDouble("3.385473")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.keystonebank))

					.title("Willoughby Branch").flat(true);

			MarkerOptions marker411 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.614867"), Double
									.parseDouble("3.345913")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Adeniyi Jones Branch").flat(true);

			MarkerOptions marker412 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.447763"), Double
									.parseDouble("3.470191")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Admiralty Way Branch").flat(true);

			MarkerOptions marker413 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460204"), Double
									.parseDouble("3.165468")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Alaba I Branch").flat(true);

			MarkerOptions marker414 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.603728"), Double
									.parseDouble("3.350973")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Allen Avenue Branch").flat(true);

			MarkerOptions marker415 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453439"), Double
									.parseDouble("3.389274")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Ap House Branch").flat(true);

			MarkerOptions marker416 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440372"), Double
									.parseDouble("3.372087")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Apapa Commercial Road Branch").flat(true);

			MarkerOptions marker417 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442554"), Double
									.parseDouble("3.375931")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Apapa Warehouse Road Branch").flat(true);

			MarkerOptions marker418 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.469089"), Double
									.parseDouble("3.243196")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Aspamda Branch").flat(true);

			MarkerOptions marker419 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.457432"), Double
									.parseDouble("3.249641")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("BBA Branch").flat(true);

			MarkerOptions marker420 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4343"), Double
									.parseDouble("3.431065")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Bishop Aboyade Cole Branch").flat(true);

			MarkerOptions marker421 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.489503"), Double
									.parseDouble("3.357232")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Bode Thomas Branch").flat(true);

			MarkerOptions marker422 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45354"), Double
									.parseDouble("3.38955")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Broad Street Branch").flat(true);

			MarkerOptions marker423 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.535233"), Double
									.parseDouble("3.348967")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Daleko Branch").flat(true);

			MarkerOptions marker424 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.542354"), Double
									.parseDouble("3.347335")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Fatai Atere Branch").flat(true);

			MarkerOptions marker425 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.462154"), Double
									.parseDouble("3.285871")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Festac Branch").flat(true);

			MarkerOptions marker426 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.437592"), Double
									.parseDouble("3.357608")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Ibafon Branch").flat(true);

			MarkerOptions marker427 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4622"), Double
									.parseDouble("3.389271")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Iga - Idugaran Branch").flat(true);

			MarkerOptions marker428 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458985"), Double
									.parseDouble("3.601521")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Ikota Branch").flat(true);

			MarkerOptions marker429 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.547603"), Double
									.parseDouble("3.33751")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Isolo Branch").flat(true);

			MarkerOptions marker430 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455315"), Double
									.parseDouble("3.385434")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Issa Williams Branch").flat(true);

			MarkerOptions marker431 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.449894"), Double
									.parseDouble("3.394942")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Marina Branch").flat(true);

			MarkerOptions marker432 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.456459"), Double
									.parseDouble("3.385995")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Martins Street Branch").flat(true);

			MarkerOptions marker433 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.581818"), Double
									.parseDouble("3.321135")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Murtala Mohammed International Airport Branch")
					.flat(true);

			MarkerOptions marker434 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.623276"), Double
									.parseDouble("3.354223")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("NECA Branch").flat(true);

			MarkerOptions marker435 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.509921"), Double
									.parseDouble("3.365004")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Ojuelegba Branch").flat(true);

			MarkerOptions marker436 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459691"), Double
									.parseDouble("3.38508")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Oke-Arin Branch").flat(true);

			MarkerOptions marker437 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431577"), Double
									.parseDouble("3.426843")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Oladele Olashore Branch").flat(true);

			MarkerOptions marker438 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.574817"), Double
									.parseDouble("3.362084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("OPIC Branch").flat(true);

			MarkerOptions marker439 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.593152"), Double
									.parseDouble("3.372016")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Oregun Branch").flat(true);

			MarkerOptions marker440 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453149"), Double
									.parseDouble("3.385621")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Reinsurance - Marina Branch").flat(true);

			MarkerOptions marker441 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.437293"), Double
									.parseDouble("3.427797")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.mainstreetbank))

					.title("Victoria Island Branch").flat(true);

			MarkerOptions marker442 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453559"), Double
									.parseDouble("3.389415")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("118 Broad Street Branch").flat(true);

			MarkerOptions marker443 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.43007"), Double
									.parseDouble("3.409131")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Adeola-Odeku Branch").flat(true);

			MarkerOptions marker444 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.895364"), Double
									.parseDouble("3.012567")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Agbara Branch").flat(true);

			MarkerOptions marker445 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.625288"), Double
									.parseDouble("3.317424")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Agege Branch").flat(true);
			MarkerOptions marker446 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430981"), Double
									.parseDouble("3.43079")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Ajose Adeogun Branch").flat(true);

			MarkerOptions marker447 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458769"), Double
									.parseDouble("3.305654")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Amuwo-Odofin Branch").flat(true);

			MarkerOptions marker448 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442666"), Double
									.parseDouble("3.373309")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Apapa - Eleganza Plaza Branch").flat(true);

			MarkerOptions marker449 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444368"), Double
									.parseDouble("3.424981")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Awolowo Road Branch").flat(true);

			MarkerOptions marker450 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.436208"), Double
									.parseDouble("3.364495")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Creek Apapa Branch").flat(true);

			MarkerOptions marker451 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.59794"), Double
									.parseDouble("3.396442")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Davies Street Branch").flat(true);

			MarkerOptions marker452 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.463546"), Double
									.parseDouble("3.388017")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Docemo Idumota Branch").flat(true);

			MarkerOptions marker453 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.517655"), Double
									.parseDouble("3.368239")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Ebute Metta Branch").flat(true);

			MarkerOptions marker454 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.541813"), Double
									.parseDouble("3.281616")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Egbe Ikotun Branch").flat(true);

			MarkerOptions marker455 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.447926"), Double
									.parseDouble("3.408958")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Falomo Branch").flat(true);

			MarkerOptions marker456 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.454114"), Double
									.parseDouble("3.420854")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Federal Secretariat Branch").flat(true);

			MarkerOptions marker457 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.443784"), Double
									.parseDouble("3.42591")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Foreshore Towers Branch").flat(true);

			MarkerOptions marker458 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.550559"), Double
									.parseDouble("3.268389")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Idimu Branch").flat(true);

			MarkerOptions marker459 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.483041"), Double
									.parseDouble("3.358044")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Iganmu Branch").flat(true);

			MarkerOptions marker460 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.469274"), Double
									.parseDouble("3.369002")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Ijora Branch").flat(true);

			MarkerOptions marker461 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.621244"), Double
									.parseDouble("3.501697")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Ikorodu Branch").flat(true);

			MarkerOptions marker462 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.550982"), Double
									.parseDouble("3.355711")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Ilupeju Branch").flat(true);

			MarkerOptions marker463 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.529962"), Double
									.parseDouble("3.331985")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Isolo Branch").flat(true);

			MarkerOptions marker464 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.510648"), Double
									.parseDouble("3.360527")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Lawanson Branch").flat(true);

			MarkerOptions marker465 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.471402"), Double
									.parseDouble("3.576347")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Lewis Street Branch").flat(true);

			MarkerOptions marker466 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.448225"), Double
									.parseDouble("3.405648")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Moloney Branch").flat(true);

			MarkerOptions marker467 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.446984"), Double
									.parseDouble("3.411985")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Obalende Branch").flat(true);

			MarkerOptions marker468 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.461271"), Double
									.parseDouble("3.387499")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Obun Eko Branch").flat(true);

			MarkerOptions marker469 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460913"), Double
									.parseDouble("3.385534")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Oke-Arin Branch").flat(true);

			MarkerOptions marker470 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429449"), Double
									.parseDouble("3.428863")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Oyin Jola V/I Branch").flat(true);

			MarkerOptions marker471 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.642644"), Double
									.parseDouble("3.323412")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Pencinema Branch").flat(true);

			MarkerOptions marker472 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444641"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Plaza Branch").flat(true);

			MarkerOptions marker473 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458868"), Double
									.parseDouble("3.383543")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Sanusi Olusi Branch").flat(true);

			MarkerOptions marker474 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.533313"), Double
									.parseDouble("3.367627")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Somolu Branch").flat(true);

			MarkerOptions marker475 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.498054"), Double
									.parseDouble("3.360898")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Surulere Branch").flat(true);

			MarkerOptions marker476 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431048"), Double
									.parseDouble("3.343551")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Tin-Can Island Branch").flat(true);

			MarkerOptions marker477 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430928"), Double
									.parseDouble("3.427121")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Victoria Island Branch").flat(true);

			MarkerOptions marker478 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442277"), Double
									.parseDouble("3.375733")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Wharf Road, Apapa Branch").flat(true);

			MarkerOptions marker479 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.495014"), Double
									.parseDouble("3.38082")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Yaba Branch").flat(true);

			MarkerOptions marker480 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.457383"), Double
									.parseDouble("3.388563")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.unionbank))

					.title("Yinka Folawiyo, Apapa Branch").flat(true);

			MarkerOptions marker481 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.437527"), Double
									.parseDouble("3.427856")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Adeola Hopewell Branch").flat(true);

			MarkerOptions marker482 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430533"), Double
									.parseDouble("3.415567")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Adeola Odeku Branch").flat(true);

			MarkerOptions marker483 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.620749"), Double
									.parseDouble("3.353115")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Agidingbi(2) Branch").flat(true);

			MarkerOptions marker484 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.629868"), Double
									.parseDouble("3.345142")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Agindigbi Branch").flat(true);

			MarkerOptions marker485 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.502206"), Double
									.parseDouble("3.305082")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Ago Palace Way Branch").flat(true);

			MarkerOptions marker486 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.449192"), Double
									.parseDouble("3.550009")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Ajah Branch").flat(true);

			MarkerOptions marker487 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600369"), Double
									.parseDouble("3.352789")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Allen Branch").flat(true);

			MarkerOptions marker488 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.467056"), Double
									.parseDouble("3.244451")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Aspamda Tradefair Branch").flat(true);

			MarkerOptions marker489 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.457694"), Double
									.parseDouble("3.384947")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Balogun Street Branch").flat(true);

			MarkerOptions marker490 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.457589"), Double
									.parseDouble("3.251056")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Bba 1 Branch").flat(true);

			MarkerOptions marker491 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453566"), Double
									.parseDouble("3.389659")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Broad Street Branch").flat(true);

			MarkerOptions marker492 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438715"), Double
									.parseDouble("3.372911")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Commercial Road Branch").flat(true);

			MarkerOptions marker493 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.435707"), Double
									.parseDouble("3.365984")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Creek Road Branch").flat(true);

			MarkerOptions marker494 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.483301"), Double
									.parseDouble("3.387331")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Ebute Metta Branch").flat(true);

			MarkerOptions marker495 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.462661"), Double
									.parseDouble("3.388121")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Enu-Owa, Idumota Branch").flat(true);

			MarkerOptions marker496 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431074"), Double
									.parseDouble("3.411751")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Femi Pearse Branch").flat(true);

			MarkerOptions marker497 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.42825"), Double
									.parseDouble("3.408571")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Head Office").flat(true);

			MarkerOptions marker498 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460058"), Double
									.parseDouble("3.389629")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Idumagbo Branch").flat(true);

			MarkerOptions marker499 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.461311"), Double
									.parseDouble("3.387277")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Idumota Branch").flat(true);
			MarkerOptions marker500 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.557722"), Double
									.parseDouble("3.111087")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Igbesa Branch").flat(true);

			MarkerOptions marker501 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442016"), Double
									.parseDouble("3.418364")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Ikoyi Branch").flat(true);

			MarkerOptions marker502 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.553648"), Double
									.parseDouble("3.356674")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Ilupeju Bypass Branch").flat(true);

			MarkerOptions marker503 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.554823"), Double
									.parseDouble("3.354142")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Ilupeju Industrial Avenue Branch").flat(true);

			MarkerOptions marker504 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.504237"), Double
									.parseDouble("3.381618")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Karimu Ikotun Branch").flat(true);

			MarkerOptions marker505 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.433257"), Double
									.parseDouble("3.420131")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Martins Street Branch").flat(true);

			MarkerOptions marker506 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459794"), Double
									.parseDouble("3.331242")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Navy Town, Mammy Market Branch").flat(true);

			MarkerOptions marker507 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.435406"), Double
									.parseDouble("3.366094")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Nnewi Building Apapa Branch").flat(true);

			MarkerOptions marker508 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.461274"), Double
									.parseDouble("3.387499")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Obun-Eko, Idumota Branch").flat(true);

			MarkerOptions marker509 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.628492"), Double
									.parseDouble("3.341502")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Ogba Branch").flat(true);

			MarkerOptions marker510 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.529999"), Double
									.parseDouble("3.354469")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Ojuwoye Branch").flat(true);

			MarkerOptions marker511 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459166"), Double
									.parseDouble("3.38486")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Oke-Arin Branch").flat(true);

			MarkerOptions marker512 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.556807"), Double
									.parseDouble("3.351295")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Oshodi Branch").flat(true);

			MarkerOptions marker513 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.595173"), Double
									.parseDouble("3.340223")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Otigba Branch").flat(true);

			MarkerOptions marker514 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460204"), Double
									.parseDouble("3.165468")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Raybross Plaza Branch").flat(true);

			MarkerOptions marker515 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.427023"), Double
									.parseDouble("3.421211")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Saka Tinubu Branch").flat(true);

			MarkerOptions marker516 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.509111"), Double
									.parseDouble("3.352603")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Surulere Branch").flat(true);

			MarkerOptions marker517 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45114"), Double
									.parseDouble("3.363874")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Tincan Branch").flat(true);

			MarkerOptions marker518 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45528"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Trinity Branch").flat(true);

			MarkerOptions marker519 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45528"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Warehouse Apapa Branch").flat(true);

			MarkerOptions marker520 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442277"), Double
									.parseDouble("3.376492")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.enterprisebank))

					.title("Wharf Road Branch").flat(true);

			MarkerOptions marker521 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.435406"), Double
									.parseDouble("3.366094")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("1 Creek Road (Nnewi Building) Branch").flat(true);

			MarkerOptions marker522 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.441976"), Double
									.parseDouble("3.418365")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("114 Awolowo Road - Ikoyi Branch").flat(true);

			MarkerOptions marker523 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444457"), Double
									.parseDouble("3.423829")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("228a Awolowo Road - Ikoyi Branch").flat(true);

			MarkerOptions marker524 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.587961"), Double
									.parseDouble("3.362264")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("2b Opebi Branch").flat(true);

			MarkerOptions marker525 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430035"), Double
									.parseDouble("3.429916")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("30 Adetokunbo Ademola Branch").flat(true);

			MarkerOptions marker526 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.426402"), Double
									.parseDouble("3.430325")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("62 Adetokunbo Ademola Branch").flat(true);

			MarkerOptions marker527 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.588342"), Double
									.parseDouble("3.362094")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("68 Opebi Branch").flat(true);

			MarkerOptions marker528 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.589014"), Double
									.parseDouble("3.361565")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Adebola House Branch").flat(true);

			MarkerOptions marker529 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.491461"), Double
									.parseDouble("3.356596")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Adeniran Ogunsanya - Surulere Branch").flat(true);

			MarkerOptions marker530 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431398"), Double
									.parseDouble("3.429643")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Adeola Hopewell Branch").flat(true);

			MarkerOptions marker531 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430082"), Double
									.parseDouble("3.417924")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Adeola Odeku Branch").flat(true);

			MarkerOptions marker532 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.577359"), Double
									.parseDouble("3.333956")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Airport Road - Ikeja Branch").flat(true);

			MarkerOptions marker533 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601984"), Double
									.parseDouble("3.351766")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Allen Branch").flat(true);

			// contd from here for heritage bank
			MarkerOptions marker534 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.607361"), Double
									.parseDouble("3.348854")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Aromire Branch").flat(true);

			MarkerOptions marker535 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.617266"), Double
									.parseDouble("3.361942")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Awolowo Way - Ikeja Branch").flat(true);

			MarkerOptions marker536 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.472516"), Double
									.parseDouble("3.568616")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Awoyaya Branch").flat(true);

			MarkerOptions marker537 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.437858"), Double
									.parseDouble("3.516692")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Bakky Plaza - Lekki Branch").flat(true);

			MarkerOptions marker538 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.613606"), Double
									.parseDouble("3.347191")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Coker Branch").flat(true);

			MarkerOptions marker539 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440377"), Double
									.parseDouble("3.372105")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Commercial Road Branch").flat(true);

			MarkerOptions marker540 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601838"), Double
									.parseDouble("3.351486")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Conoil Station - Ikeja GRA Branch").flat(true);

			MarkerOptions marker541 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.531297"), Double
									.parseDouble("3.338601")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Daleko Branch").flat(true);

			MarkerOptions marker542 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.597334"), Double
									.parseDouble("3.394274")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Demurin Branch").flat(true);

			MarkerOptions marker543 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.530204"), Double
									.parseDouble("3.303644")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Ejigbo - NNPC Branch").flat(true);

			MarkerOptions marker544 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.54125"), Double
									.parseDouble("3.367015")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Fadeyi Branch").flat(true);

			MarkerOptions marker545 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.495063"), Double
									.parseDouble("3.380802")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Herbert Macaulay- Yaba Branch").flat(true);
			MarkerOptions marker546 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45528"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Ibru Jetty - Apapa Branch").flat(true);

			MarkerOptions marker547 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.48572"), Double
									.parseDouble("3.388029")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Iddo Branch").flat(true);

			MarkerOptions marker548 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.594268"), Double
									.parseDouble("3.287842")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Idimu Branch").flat(true);

			MarkerOptions marker549 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.642644"), Double
									.parseDouble("3.323412")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Iju Road - Ifako Branch").flat(true);

			MarkerOptions marker550 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.552406"), Double
									.parseDouble("3.367351")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Ikorodu Branch").flat(true);

			MarkerOptions marker551 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.576941"), Double
									.parseDouble("3.360331")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Ikota Shopping Complex - Ajah Branch").flat(true);

			MarkerOptions marker552 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.570721"), Double
									.parseDouble("3.372329")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Ilupeju Branch").flat(true);

			MarkerOptions marker553 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.521598"), Double
									.parseDouble("3.322123")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Ire-Akari - Isolo Branch").flat(true);

			MarkerOptions marker554 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.513398"), Double
									.parseDouble("3.334084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Itire Branch").flat(true);

			MarkerOptions marker555 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.61886"), Double
									.parseDouble("3.300407")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Iyana-Ipaja Branch").flat(true);

			MarkerOptions marker556 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.604268"), Double
									.parseDouble("3.390899")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Ketu Branch").flat(true);

			MarkerOptions marker557 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.448029"), Double
									.parseDouble("3.326542")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Kirikiri Road Branch").flat(true);

			MarkerOptions marker558 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.504018"), Double
									.parseDouble("3.582007")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Langbasa Service Centre").flat(true);

			MarkerOptions marker559 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.469632"), Double
									.parseDouble("3.200564")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Lasu Branch").flat(true);

			MarkerOptions marker560 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.544276"), Double
									.parseDouble("3.342851")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Matori Service Centre").flat(true);

			MarkerOptions marker561 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.540395"), Double
									.parseDouble("3.346236")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Matori Branch").flat(true);

			MarkerOptions marker562 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455224"), Double
									.parseDouble("3.356731")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Mobil Road - Ajegunle Branch").flat(true);

			MarkerOptions marker563 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.527188"), Double
									.parseDouble("3.354507")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Mushin Branch").flat(true);

			MarkerOptions marker564 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.604424"), Double
									.parseDouble("3.337162")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Oba Akran Branch").flat(true);

			MarkerOptions marker565 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.63304"), Double
									.parseDouble("3.341531")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Ogba Branch").flat(true);

			MarkerOptions marker566 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.619413"), Double
									.parseDouble("3.510454")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Ogijo Branch").flat(true);

			MarkerOptions marker567 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.581701"), Double
									.parseDouble("3.3825")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Ogudu Branch").flat(true);

			MarkerOptions marker568 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.503889"), Double
									.parseDouble("3.351442")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Ogunlana Drive- Surulere Branch").flat(true);

			MarkerOptions marker569 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.529999"), Double
									.parseDouble("3.354469")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Ojuwoye Service Centre").flat(true);

			MarkerOptions marker570 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.515823"), Double
									.parseDouble("3.319842")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Okota Road Branch").flat(true);

			MarkerOptions marker571 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.563075"), Double
									.parseDouble("3.346502")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Oshodi Branch").flat(true);

			MarkerOptions marker572 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.503066"), Double
									.parseDouble("3.37672")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Oworonsoki Service Centre").flat(true);

			MarkerOptions marker573 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.427023"), Double
									.parseDouble("3.421212")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Saka Tinubu Branch").flat(true);

			MarkerOptions marker574 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600783"), Double
									.parseDouble("3.309706")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Shasha Road - Dopemu Branch").flat(true);

			MarkerOptions marker575 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45528"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Tincan Branch").flat(true);

			MarkerOptions marker576 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429302"), Double
									.parseDouble("3.268421")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Trade Fair Branch").flat(true);

			MarkerOptions marker577 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.445259"), Double
									.parseDouble("3.371347")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Wharf Road Branch").flat(true);

			MarkerOptions marker578 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.479268"), Double
									.parseDouble("3.385473")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.sterlingbank))

					.title("Willoughby- Ebute-Metta Branch").flat(true);

			MarkerOptions marker579 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.61886"), Double
									.parseDouble("3.300407")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Abule Egba Branch").flat(true);

			MarkerOptions marker580 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.462428"), Double
									.parseDouble("3.38236")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Adeniji Adele Branch").flat(true);

			MarkerOptions marker581 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.634383"), Double
									.parseDouble("3.319783")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Agege Branch").flat(true);

			MarkerOptions marker582 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.524379"), Double
									.parseDouble("3.379206")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Ajao Estate Branch").flat(true);

			MarkerOptions marker583 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601621"), Double
									.parseDouble("3.351934")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Allen Branch").flat(true);

			MarkerOptions marker584 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429302"), Double
									.parseDouble("3.268421")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Aspamda Branch").flat(true);

			MarkerOptions marker585 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444464"), Double
									.parseDouble("3.424082")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Awolowo Road Branch").flat(true);

			MarkerOptions marker586 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453539"), Double
									.parseDouble("3.38955")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Broad Street 1 Branch").flat(true);

			MarkerOptions marker587 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458713"), Double
									.parseDouble("3.389669")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Broad Street 2 Branch").flat(true);

			MarkerOptions marker588 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442615"), Double
									.parseDouble("3.373286")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Commercial Road Branch").flat(true);

			MarkerOptions marker589 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.617108"), Double
									.parseDouble("3.312788")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Dopemu Branch").flat(true);

			MarkerOptions marker590 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.483471"), Double
									.parseDouble("3.382513")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Ebute Metta Branch").flat(true);

			MarkerOptions marker591 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.591574"), Double
									.parseDouble("3.291062")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Egbeda Branch").flat(true);

			MarkerOptions marker592 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.43305"), Double
									.parseDouble("3.425816")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Idowu Taylor Branch").flat(true);

			MarkerOptions marker593 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601053"), Double
									.parseDouble("3.338636")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Ikeja Branch").flat(true);

			MarkerOptions marker594 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.522763"), Double
									.parseDouble("3.330044")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Isolo Branch").flat(true);

			MarkerOptions marker595 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.552406"), Double
									.parseDouble("3.36735")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Jibowu Branch").flat(true);

			MarkerOptions marker596 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.602276"), Double
									.parseDouble("3.344614")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Lagos Airport Hotel Branch").flat(true);

			MarkerOptions marker597 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.451397"), Double
									.parseDouble("3.396397")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Lapal Branch").flat(true);

			MarkerOptions marker598 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453539"), Double
									.parseDouble("3.389551")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Mamman Kontagora Branch").flat(true);

			MarkerOptions marker599 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.572514"), Double
									.parseDouble("3.365293")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Maryland Branch").flat(true);

			MarkerOptions marker600 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.627364"), Double
									.parseDouble("3.322609")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Mushin Branch").flat(true);
			
			googleMap.addMarker(marker301);
			googleMap.addMarker(marker302);
			googleMap.addMarker(marker303);
			googleMap.addMarker(marker304);
			googleMap.addMarker(marker305);
			googleMap.addMarker(marker306);
			googleMap.addMarker(marker307);
			googleMap.addMarker(marker308);
			googleMap.addMarker(marker309);
			googleMap.addMarker(marker310);
			googleMap.addMarker(marker311);
			googleMap.addMarker(marker312);
			googleMap.addMarker(marker313);
			googleMap.addMarker(marker314);
			googleMap.addMarker(marker315);
			googleMap.addMarker(marker316);
			googleMap.addMarker(marker317);
			googleMap.addMarker(marker318);
			googleMap.addMarker(marker319);
			googleMap.addMarker(marker320);
			googleMap.addMarker(marker321);
			googleMap.addMarker(marker322);
			googleMap.addMarker(marker323);
			googleMap.addMarker(marker324);
			googleMap.addMarker(marker325);
			googleMap.addMarker(marker326);
			googleMap.addMarker(marker327);
			googleMap.addMarker(marker328);
			googleMap.addMarker(marker329);
			googleMap.addMarker(marker330);
			googleMap.addMarker(marker331);
			googleMap.addMarker(marker332);
			googleMap.addMarker(marker333);
			googleMap.addMarker(marker334);
			googleMap.addMarker(marker335);
			googleMap.addMarker(marker336);
			googleMap.addMarker(marker337);
			googleMap.addMarker(marker338);
			googleMap.addMarker(marker339);
			googleMap.addMarker(marker340);
			googleMap.addMarker(marker341);
			googleMap.addMarker(marker342);
			googleMap.addMarker(marker343);
			googleMap.addMarker(marker344);
			googleMap.addMarker(marker345);
			googleMap.addMarker(marker346);
			googleMap.addMarker(marker347);
			googleMap.addMarker(marker348);
			googleMap.addMarker(marker349);
			googleMap.addMarker(marker350);
			googleMap.addMarker(marker351);
			googleMap.addMarker(marker352);
			googleMap.addMarker(marker353);
			googleMap.addMarker(marker354);
			googleMap.addMarker(marker355);
			googleMap.addMarker(marker356);
			googleMap.addMarker(marker357);
			googleMap.addMarker(marker358);
			googleMap.addMarker(marker359);
			googleMap.addMarker(marker360);
			googleMap.addMarker(marker361);
			googleMap.addMarker(marker362);
			googleMap.addMarker(marker363);
			googleMap.addMarker(marker364);
			googleMap.addMarker(marker365);
			googleMap.addMarker(marker366);
			googleMap.addMarker(marker367);
			googleMap.addMarker(marker368);
			googleMap.addMarker(marker369);
			googleMap.addMarker(marker370);
			googleMap.addMarker(marker371);
			googleMap.addMarker(marker372);
			googleMap.addMarker(marker373);
			googleMap.addMarker(marker374);
			googleMap.addMarker(marker375);
			googleMap.addMarker(marker376);
			googleMap.addMarker(marker377);
			googleMap.addMarker(marker378);
			googleMap.addMarker(marker379);
			googleMap.addMarker(marker380);
			googleMap.addMarker(marker381);
			googleMap.addMarker(marker382);
			googleMap.addMarker(marker383);
			googleMap.addMarker(marker384);
			googleMap.addMarker(marker385);
			googleMap.addMarker(marker386);
			googleMap.addMarker(marker387);
			googleMap.addMarker(marker388);
			googleMap.addMarker(marker389);
			googleMap.addMarker(marker390);
			googleMap.addMarker(marker391);
			googleMap.addMarker(marker392);
			googleMap.addMarker(marker393);
			googleMap.addMarker(marker394);
			googleMap.addMarker(marker395);
			googleMap.addMarker(marker396);
			googleMap.addMarker(marker397);
			googleMap.addMarker(marker398);
			googleMap.addMarker(marker399);
			googleMap.addMarker(marker400);
			googleMap.addMarker(marker401);
			googleMap.addMarker(marker402);
			googleMap.addMarker(marker403);
			googleMap.addMarker(marker404);
			googleMap.addMarker(marker405);
			googleMap.addMarker(marker406);
			googleMap.addMarker(marker407);
			googleMap.addMarker(marker408);
			googleMap.addMarker(marker409);
			googleMap.addMarker(marker410);
			googleMap.addMarker(marker411);
			googleMap.addMarker(marker412);
			googleMap.addMarker(marker413);
			googleMap.addMarker(marker414);
			googleMap.addMarker(marker415);
			googleMap.addMarker(marker416);
			googleMap.addMarker(marker417);
			googleMap.addMarker(marker418);
			googleMap.addMarker(marker419);
			googleMap.addMarker(marker420);
			googleMap.addMarker(marker421);
			googleMap.addMarker(marker422);
			googleMap.addMarker(marker423);
			googleMap.addMarker(marker424);
			googleMap.addMarker(marker425);
			googleMap.addMarker(marker426);
			googleMap.addMarker(marker427);
			googleMap.addMarker(marker428);
			googleMap.addMarker(marker429);
			googleMap.addMarker(marker430);
			googleMap.addMarker(marker431);
			googleMap.addMarker(marker432);
			googleMap.addMarker(marker433);
			googleMap.addMarker(marker434);
			googleMap.addMarker(marker435);
			googleMap.addMarker(marker436);
			googleMap.addMarker(marker437);
			googleMap.addMarker(marker438);
			googleMap.addMarker(marker439);
			googleMap.addMarker(marker440);
			googleMap.addMarker(marker441);
			googleMap.addMarker(marker442);
			googleMap.addMarker(marker443);
			googleMap.addMarker(marker444);
			googleMap.addMarker(marker445);
			googleMap.addMarker(marker446);
			googleMap.addMarker(marker447);
			googleMap.addMarker(marker448);
			googleMap.addMarker(marker449);
			googleMap.addMarker(marker450);
			googleMap.addMarker(marker451);
			googleMap.addMarker(marker452);
			googleMap.addMarker(marker453);
			googleMap.addMarker(marker454);
			googleMap.addMarker(marker455);
			googleMap.addMarker(marker456);
			googleMap.addMarker(marker457);
			googleMap.addMarker(marker458);
			googleMap.addMarker(marker459);
			googleMap.addMarker(marker460);
			googleMap.addMarker(marker461);
			googleMap.addMarker(marker462);
			googleMap.addMarker(marker463);
			googleMap.addMarker(marker464);
			googleMap.addMarker(marker465);
			googleMap.addMarker(marker466);
			googleMap.addMarker(marker467);
			googleMap.addMarker(marker468);
			googleMap.addMarker(marker469);
			googleMap.addMarker(marker470);
			googleMap.addMarker(marker471);
			googleMap.addMarker(marker472);
			googleMap.addMarker(marker473);
			googleMap.addMarker(marker474);
			googleMap.addMarker(marker475);
			googleMap.addMarker(marker476);
			googleMap.addMarker(marker477);
			googleMap.addMarker(marker478);
			googleMap.addMarker(marker479);
			googleMap.addMarker(marker480);
			googleMap.addMarker(marker481);
			googleMap.addMarker(marker482);
			googleMap.addMarker(marker483);
			googleMap.addMarker(marker484);
			googleMap.addMarker(marker485);
			googleMap.addMarker(marker486);
			googleMap.addMarker(marker487);
			googleMap.addMarker(marker488);
			googleMap.addMarker(marker489);
			googleMap.addMarker(marker490);
			googleMap.addMarker(marker491);
			googleMap.addMarker(marker492);
			googleMap.addMarker(marker493);
			googleMap.addMarker(marker494);
			googleMap.addMarker(marker495);
			googleMap.addMarker(marker496);
			googleMap.addMarker(marker497);
			googleMap.addMarker(marker498);
			googleMap.addMarker(marker499);
			googleMap.addMarker(marker500);
			googleMap.addMarker(marker501);
			googleMap.addMarker(marker502);
			googleMap.addMarker(marker503);
			googleMap.addMarker(marker504);
			googleMap.addMarker(marker505);
			googleMap.addMarker(marker506);
			googleMap.addMarker(marker507);
			googleMap.addMarker(marker508);
			googleMap.addMarker(marker509);
			googleMap.addMarker(marker510);
			googleMap.addMarker(marker511);
			googleMap.addMarker(marker512);
			googleMap.addMarker(marker513);
			googleMap.addMarker(marker514);
			googleMap.addMarker(marker515);
			googleMap.addMarker(marker516);
			googleMap.addMarker(marker517);
			googleMap.addMarker(marker518);
			googleMap.addMarker(marker519);
			googleMap.addMarker(marker520);
			googleMap.addMarker(marker521);
			googleMap.addMarker(marker522);
			googleMap.addMarker(marker523);
			googleMap.addMarker(marker524);
			googleMap.addMarker(marker525);
			googleMap.addMarker(marker526);
			googleMap.addMarker(marker527);
			googleMap.addMarker(marker528);
			googleMap.addMarker(marker529);
			googleMap.addMarker(marker530);
			googleMap.addMarker(marker531);
			googleMap.addMarker(marker532);
			googleMap.addMarker(marker533);
			googleMap.addMarker(marker534);
			googleMap.addMarker(marker535);
			googleMap.addMarker(marker536);
			googleMap.addMarker(marker537);
			googleMap.addMarker(marker538);
			googleMap.addMarker(marker539);
			googleMap.addMarker(marker540);
			googleMap.addMarker(marker541);
			googleMap.addMarker(marker542);
			googleMap.addMarker(marker543);
			googleMap.addMarker(marker544);
			googleMap.addMarker(marker545);
			googleMap.addMarker(marker546);
			googleMap.addMarker(marker547);
			googleMap.addMarker(marker548);
			googleMap.addMarker(marker549);
			googleMap.addMarker(marker550);
			googleMap.addMarker(marker551);
			googleMap.addMarker(marker552);
			googleMap.addMarker(marker553);
			googleMap.addMarker(marker554);
			googleMap.addMarker(marker555);
			googleMap.addMarker(marker556);
			googleMap.addMarker(marker557);
			googleMap.addMarker(marker558);
			googleMap.addMarker(marker559);
			googleMap.addMarker(marker560);
			googleMap.addMarker(marker561);
			googleMap.addMarker(marker562);
			googleMap.addMarker(marker563);
			googleMap.addMarker(marker564);
			googleMap.addMarker(marker565);
			googleMap.addMarker(marker566);
			googleMap.addMarker(marker567);
			googleMap.addMarker(marker568);
			googleMap.addMarker(marker569);
			googleMap.addMarker(marker570);
			googleMap.addMarker(marker571);
			googleMap.addMarker(marker572);
			googleMap.addMarker(marker573);
			googleMap.addMarker(marker574);
			googleMap.addMarker(marker575);
			googleMap.addMarker(marker576);
			googleMap.addMarker(marker577);
			googleMap.addMarker(marker578);
			googleMap.addMarker(marker579);
			googleMap.addMarker(marker580);
			googleMap.addMarker(marker581);
			googleMap.addMarker(marker582);
			googleMap.addMarker(marker583);
			googleMap.addMarker(marker584);
			googleMap.addMarker(marker585);
			googleMap.addMarker(marker586);
			googleMap.addMarker(marker587);
			googleMap.addMarker(marker588);
			googleMap.addMarker(marker589);
			googleMap.addMarker(marker590);
			googleMap.addMarker(marker591);
			googleMap.addMarker(marker592);
			googleMap.addMarker(marker593);
			googleMap.addMarker(marker594);
			googleMap.addMarker(marker595);
			googleMap.addMarker(marker596);
			googleMap.addMarker(marker597);
			googleMap.addMarker(marker598);
			googleMap.addMarker(marker599);
			googleMap.addMarker(marker600);
		}

		private void markersThree() {
			MarkerOptions marker601 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.577359"), Double
									.parseDouble("3.333956")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Nahco Branch").flat(true);

			MarkerOptions marker602 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45528"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("NPA Branch").flat(true);

			MarkerOptions marker603 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601053"), Double
									.parseDouble("3.338636")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Oba Akran Branch").flat(true);

			MarkerOptions marker604 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601838"), Double
									.parseDouble("3.351486")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Ogba Branch").flat(true);

			MarkerOptions marker605 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.583348"), Double
									.parseDouble("3.380486")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Ojota Branch").flat(true);

			MarkerOptions marker606 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429044"), Double
									.parseDouble("3.46314")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Oniru Branch").flat(true);

			MarkerOptions marker607 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459993"), Double
									.parseDouble("3.230824")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Orile Iganmu Branch").flat(true);

			MarkerOptions marker608 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.562183"), Double
									.parseDouble("3.349129")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Oshodi Branch").flat(true);

			MarkerOptions marker609 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.456087"), Double
									.parseDouble("3.388204")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Tinubu Branch").flat(true);

			MarkerOptions marker610 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.517568"), Double
									.parseDouble("3.385567")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Unilag Branch").flat(true);

			MarkerOptions marker611 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45528"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Warehouse Road Branch").flat(true);

			MarkerOptions marker612 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.454928"), Double
									.parseDouble("3.383926")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.wemabank))

					.title("Head Office").flat(true);

			MarkerOptions marker613 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6141676"), Double
									.parseDouble("3.3397747")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Adeniyi Jones Branch").flat(true);

			MarkerOptions marker614 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430251"), Double
									.parseDouble("3.417994")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Adeola Odeku Branch").flat(true);

			MarkerOptions marker615 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5449299"), Double
									.parseDouble("3.3299973")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Ajao Estate Branch").flat(true);

			MarkerOptions marker616 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.546308"), Double
									.parseDouble("3.325172")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Alaba International Market(2) Branch").flat(true);

			MarkerOptions marker617 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4592261"), Double
									.parseDouble("3.1869181")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Alaba International Market Branch").flat(true);

			MarkerOptions marker618 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6247007"), Double
									.parseDouble("3.3508324")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Alausa Branch").flat(true);

			MarkerOptions marker619 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.610069"), Double
									.parseDouble("3.289122")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Alimosho - Iyana Ipaja Branch").flat(true);

			MarkerOptions marker620 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460603"), Double
									.parseDouble("3.250380")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Atiku Abubakar Plaza Branch").flat(true);

			MarkerOptions marker621 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.617266"), Double
									.parseDouble("3.361942")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Awolowo Ikeja Branch").flat(true);

			MarkerOptions marker622 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442244"), Double
									.parseDouble("3.419196")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Awolowo Road Branch").flat(true);

			MarkerOptions marker623 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440208"), Double
									.parseDouble("3.3495853")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Badagry Expressway Branch").flat(true);

			MarkerOptions marker624 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4591748"), Double
									.parseDouble("3.2490443")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Bank Plaza Branch").flat(true);

			MarkerOptions marker625 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.490126"), Double
									.parseDouble("3.354734")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Bode Thomas Branch").flat(true);

			MarkerOptions marker626 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.43815"), Double
									.parseDouble("3.368106")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Burma Road Branch").flat(true);

			MarkerOptions marker627 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4598084"), Double
									.parseDouble("3.216476")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Coker Street, Badagry Expressway Branch")
					.flat(true);

			MarkerOptions marker628 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.642300"), Double
									.parseDouble("3.329095")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("College Road Branch").flat(true);

			MarkerOptions marker629 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.437673"), Double
									.parseDouble("3.372437")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Creek Road Branch").flat(true);

			MarkerOptions marker630 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.503281"), Double
									.parseDouble("3.3531145")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Daleko Market Branch").flat(true);

			MarkerOptions marker631 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.608194"), Double
									.parseDouble("3.314786")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Dopemu (Aluminium Village) Branch").flat(true);

			MarkerOptions marker632 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.462683"), Double
									.parseDouble("3.389359")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Enu Owa Branch").flat(true);

			MarkerOptions marker633 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4759598"), Double
									.parseDouble("3.2777372")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Festac Mini Branch").flat(true);

			// contd from here for heritage bank
			MarkerOptions marker634 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4621536"), Double
									.parseDouble("3.283682")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Festac Branch").flat(true);

			MarkerOptions marker635 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.552584"), Double
									.parseDouble("3.391345")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Gbagada/Ifako Branch").flat(true);

			MarkerOptions marker636 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4375266"), Double
									.parseDouble("3.4256677")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Head Office Branch").flat(true);

			MarkerOptions marker637 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.499737"), Double
									.parseDouble("3.378419")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Herbert Macaulay Branch").flat(true);

			MarkerOptions marker638 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.550559"), Double
									.parseDouble("3.268388")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Idimu Road Branch").flat(true);

			MarkerOptions marker639 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458846"), Double
									.parseDouble("3.389710")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Idumagbo Road Branch").flat(true);

			MarkerOptions marker640 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.633040"), Double
									.parseDouble("3.341531")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Ijaiye Road Branch").flat(true);

			MarkerOptions marker641 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.624305"), Double
									.parseDouble("3.494602")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Ikorodu-Lagos Road Branch").flat(true);

			MarkerOptions marker642 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4639538"), Double
									.parseDouble("3.5544102")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Ikota Retail Branch").flat(true);

			MarkerOptions marker643 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5455577"), Double
									.parseDouble("3.3577745")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Ilupeju Branch").flat(true);

			MarkerOptions marker644 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.632787"), Double
									.parseDouble("3.351322")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Isheri Road Branch").flat(true);

			MarkerOptions marker645 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.619982"), Double
									.parseDouble("3.300588")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Iyana Ipaja Branch").flat(true);
			MarkerOptions marker646 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.520983"), Double
									.parseDouble("3.367283")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Jibowu Branch").flat(true);

			MarkerOptions marker647 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6035667"), Double
									.parseDouble("3.4019385")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Ketu Branch").flat(true);

			MarkerOptions marker648 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453559"), Double
									.parseDouble("3.389415")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Lagos Island Broad Street Branch").flat(true);

			MarkerOptions marker649 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4537191"), Double
									.parseDouble("3.3874838")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Lagos Island Idumota Branch").flat(true);

			MarkerOptions marker650 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4625675"), Double
									.parseDouble("3.3870282")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Lagos Island Branch").flat(true);

			MarkerOptions marker651 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.511148"), Double
									.parseDouble("3.339403")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Lawanson Branch").flat(true);

			MarkerOptions marker652 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438254"), Double
									.parseDouble("3.515492")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Lekki Branch").flat(true);

			MarkerOptions marker653 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4485289"), Double
									.parseDouble("3.3648525")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Liverpool Road Branch").flat(true);

			MarkerOptions marker654 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.553495"), Double
									.parseDouble("3.331337")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Mafoluku Branch").flat(true);

			MarkerOptions marker655 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.450347"), Double
									.parseDouble("3.391594")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Marina Branch").flat(true);

			MarkerOptions marker656 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.537044"), Double
									.parseDouble("3.346589")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Matori Branch").flat(true);

			MarkerOptions marker657 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458714"), Double
									.parseDouble("3.298797")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Maza Maza Branch").flat(true);

			MarkerOptions marker658 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.545201"), Double
									.parseDouble("3.333265")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Mushin Road Branch").flat(true);

			MarkerOptions marker659 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5683959"), Double
									.parseDouble("3.3175024")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("NAHCO Shed Branch").flat(true);

			MarkerOptions marker660 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.506417"), Double
									.parseDouble("3.352473")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Ogunlana Branch").flat(true);

			MarkerOptions marker661 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.627364"), Double
									.parseDouble("3.322609")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Ojuwoye Market Branch").flat(true);

			MarkerOptions marker662 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.538753"), Double
									.parseDouble("3.359821")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Oke Arin Branch").flat(true);

			MarkerOptions marker663 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.502314"), Double
									.parseDouble("3.304878")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Okota Retail Branch").flat(true);

			MarkerOptions marker664 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5294667"), Double
									.parseDouble("3.3513182")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Olorunsogo Branch").flat(true);

			MarkerOptions marker665 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.446035"), Double
									.parseDouble("3.405693")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Onikan Branch").flat(true);

			MarkerOptions marker666 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.589033"), Double
									.parseDouble("3.361554")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Opebi Street(2) Branch").flat(true);

			MarkerOptions marker667 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.588472"), Double
									.parseDouble("3.361944")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Opebi Street Branch").flat(true);

			MarkerOptions marker668 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5997994"), Double
									.parseDouble("3.3684121")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Oregun Branch").flat(true);

			MarkerOptions marker669 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440065"), Double
									.parseDouble("3.3329505")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Oshodi Expressway Branch").flat(true);

			MarkerOptions marker670 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4805951"), Double
									.parseDouble("3.3838963")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Oyingbo Branch").flat(true);

			MarkerOptions marker671 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4327968"), Double
									.parseDouble("3.3429999")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Roro Port Branch").flat(true);

			MarkerOptions marker672 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4587507"), Double
									.parseDouble("3.3034635")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Satellite Town Mini Branch").flat(true);

			MarkerOptions marker673 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.3803005"), Double
									.parseDouble("2.7070355")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Seme Border Branch").flat(true);

			MarkerOptions marker674 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.529489"), Double
									.parseDouble("3.3849603")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("St. Finbarrs Road, Bariga Branch").flat(true);

			MarkerOptions marker675 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.508661"), Double
									.parseDouble("3.368150")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Tejuosho Retail Branch").flat(true);

			MarkerOptions marker676 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.436440"), Double
									.parseDouble("3.451316")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("The Palms Shopping Complex Branch").flat(true);

			MarkerOptions marker677 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4424505"), Double
									.parseDouble("3.3712765")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.diamondbank))

					.title("Wharf Road Branch").flat(true);

			MarkerOptions marker678 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.655992"), Double
									.parseDouble("3.293338")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Abule Egba Branch").flat(true);

			MarkerOptions marker679 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431109"), Double
									.parseDouble("3.424768")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ademola Adetokunbo Branch").flat(true);

			MarkerOptions marker680 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.437241"), Double
									.parseDouble("3.42738")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Afribank Street Branch").flat(true);

			MarkerOptions marker681 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.631163"), Double
									.parseDouble("3.32147")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Agege Market Branch").flat(true);

			MarkerOptions marker682 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430928"), Double
									.parseDouble("3.427121")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ahmadu Bello Way, V.I  Branch").flat(true);

			MarkerOptions marker683 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.499558"), Double
									.parseDouble("3.594102")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ajah Branch").flat(true);

			MarkerOptions marker684 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.454369"), Double
									.parseDouble("3.344673")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ajegunle Branch").flat(true);

			MarkerOptions marker685 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.522806"), Double
									.parseDouble("3.385982")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Akoka Branch").flat(true);

			MarkerOptions marker686 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459205"), Double
									.parseDouble("3.189128")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Alaba Branch").flat(true);

			MarkerOptions marker687 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.611546"), Double
									.parseDouble("3.345009")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Alausa Branch").flat(true);

			MarkerOptions marker688 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.599878"), Double
									.parseDouble("3.352595")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Allen Avenue Branch").flat(true);

			MarkerOptions marker689 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.535233"), Double
									.parseDouble("3.348967")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Awolowo Model Market, Mushin Branch").flat(true);

			MarkerOptions marker690 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.446495"), Double
									.parseDouble("3.418634")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Awolowo Road Branch").flat(true);

			MarkerOptions marker691 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.441110"), Double
									.parseDouble("2.919306")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Balogun Business Association Branch").flat(true);

			MarkerOptions marker692 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.454879"), Double
									.parseDouble("3.424598")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Broad Street Branch").flat(true);

			MarkerOptions marker693 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.530441"), Double
									.parseDouble("3.343977")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Daleko Branch").flat(true);

			MarkerOptions marker694 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600783"), Double
									.parseDouble("3.309706")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Egbeda Branch").flat(true);

			MarkerOptions marker695 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.543283"), Double
									.parseDouble("3.296270")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ejigbo Branch").flat(true);

			MarkerOptions marker696 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.466445"), Double
									.parseDouble("3.283514")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Festac Branch").flat(true);

			MarkerOptions marker697 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.497988"), Double
									.parseDouble("3.343929")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Gbaja Market, Surulere Branch").flat(true);

			MarkerOptions marker698 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.495085"), Double
									.parseDouble("3.380794")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Herbert Macaulay, Yaba Branch").flat(true);

			MarkerOptions marker699 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429666"), Double
									.parseDouble("3.412145")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Idejo Branch").flat(true);

			MarkerOptions marker700 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458846"), Double
									.parseDouble("3.389710")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Idumagbo Branch").flat(true);

			MarkerOptions marker701 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.475129"), Double
									.parseDouble("3.202317")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Igando Branch").flat(true);

			MarkerOptions marker702 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.615417"), Double
									.parseDouble("3.356933")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ikeja City Mall Branch").flat(true);

			MarkerOptions marker703 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.624296"), Double
									.parseDouble("3.494599")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ikorodu Branch").flat(true);

			MarkerOptions marker704 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.472516"), Double
									.parseDouble("3.568616")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ikota Lekki Branch").flat(true);

			MarkerOptions marker705 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.550560"), Double
									.parseDouble("3.268389")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ikotun Branch").flat(true);

			MarkerOptions marker706 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.619837"), Double
									.parseDouble("3.301572")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ipaja Branch").flat(true);

			MarkerOptions marker707 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430892"), Double
									.parseDouble("3.425503")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Karimu Kotun Branch").flat(true);

			MarkerOptions marker708 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.598850"), Double
									.parseDouble("3.386851")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ketu Branch").flat(true);

			MarkerOptions marker709 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.511148"), Double
									.parseDouble("3.339403")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Lawanson Branch").flat(true);

			MarkerOptions marker710 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458985"), Double
									.parseDouble("3.601521")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Lekki (Lekki 1) Branch").flat(true);

			MarkerOptions marker711 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.434482"), Double
									.parseDouble("3.481649")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Lekki Admiralty Branch").flat(true);

			MarkerOptions marker712 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.437858"), Double
									.parseDouble("3.516692")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Lekki-Ajah Express Way Branch").flat(true);

			MarkerOptions marker713 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.456454"), Double
									.parseDouble("3.386002")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Martins Street Branch").flat(true);

			MarkerOptions marker714 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.572528"), Double
									.parseDouble("3.363859")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Maryland Branch").flat(true);

			MarkerOptions marker715 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430981"), Double
									.parseDouble("3.430790")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Muri Okunola Branch").flat(true);

			MarkerOptions marker716 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.581797"), Double
									.parseDouble("3.321135")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Murtala Moh'd Int'l Airport Branch").flat(true);

			MarkerOptions marker717 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442277"), Double
									.parseDouble("3.376492")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Npa Branch").flat(true);

			MarkerOptions marker718 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601053"), Double
									.parseDouble("3.338636")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Oba Akran Branch").flat(true);

			MarkerOptions marker719 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.632640"), Double
									.parseDouble("3.341314")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ogba Branch").flat(true);

			MarkerOptions marker720 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.571095"), Double
									.parseDouble("3.396020")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ogudu Branch").flat(true);

			MarkerOptions marker721 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.638589"), Double
									.parseDouble("3.361825")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ojodu Branch").flat(true);

			MarkerOptions marker722 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.527188"), Double
									.parseDouble("3.354507")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Ojuwoye, Mushin Branch").flat(true);

			MarkerOptions marker723 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.454879"), Double
									.parseDouble("3.424598")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Oke Arin Branch").flat(true);

			MarkerOptions marker724 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.636999"), Double
									.parseDouble("3.315727")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Oko Oba Branch").flat(true);

			MarkerOptions marker725 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.512844"), Double
									.parseDouble("3.321089")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Okota Branch").flat(true);

			MarkerOptions marker726 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.588922"), Double
									.parseDouble("3.361415")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Opebi Branch").flat(true);

			MarkerOptions marker727 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.492847"), Double
									.parseDouble("3.340751")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Oshodi Market Branch").flat(true);

			MarkerOptions marker728 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.524379"), Double
									.parseDouble("3.379206")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Osolo Way Branch").flat(true);

			MarkerOptions marker729 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.483301"), Double
									.parseDouble("3.387331")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Oyingbo Branch").flat(true);

			MarkerOptions marker730 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.539309"), Double
									.parseDouble("3.344956")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Palms Avenue Area, Mushin Branch").flat(true);

			MarkerOptions marker731 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.533483"), Double
									.parseDouble("3.377708")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Shomolu Branch").flat(true);

			MarkerOptions marker732 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.491327"), Double
									.parseDouble("3.356532")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Surulere Branch").flat(true);

			MarkerOptions marker733 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.509892"), Double
									.parseDouble("3.364494")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Tejuosho Branch").flat(true);

			// contd from here for heritage bank
			MarkerOptions marker734 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440060"), Double
									.parseDouble("3.335198")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Tin Can Branch").flat(true);

			MarkerOptions marker735 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.595820"), Double
									.parseDouble("3.350968")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Toyin Street Branch").flat(true);

			MarkerOptions marker736 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.524379"), Double
									.parseDouble("3.379206")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Trade Fair Branch").flat(true);

			MarkerOptions marker737 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.439216"), Double
									.parseDouble("3.409053")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Walter Carrington Branch").flat(true);

			MarkerOptions marker738 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455280"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Warehouse Road Branch").flat(true);

			MarkerOptions marker739 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438971"), Double
									.parseDouble("3.366600")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.stanbicibtc))

					.title("Yinka Folawiyo Branch").flat(true);

			MarkerOptions marker740 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.427537"), Double
									.parseDouble("3.409279")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ahmadu Bello Branch").flat(true);

			MarkerOptions marker741 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.564497"), Double
									.parseDouble("3.322811")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Airport Road Branch").flat(true);

			MarkerOptions marker742 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5243793"), Double
									.parseDouble("3.377017")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Alaba - Agudosi Branch").flat(true);

			MarkerOptions marker743 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4592261"), Double
									.parseDouble("3.1869181")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Alaba-Ojo Igbede Branch").flat(true);

			MarkerOptions marker744 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5243793"), Double
									.parseDouble("3.377017")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Alaba-St Patricks Junction Branch").flat(true);

			MarkerOptions marker745 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4405854"), Double
									.parseDouble("3.3749239")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Apapa - Creek Road Branch").flat(true);
			MarkerOptions marker746 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4452751"), Double
									.parseDouble("3.3706176")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Apapa - Wharf Road Branch").flat(true);

			MarkerOptions marker747 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5243793"), Double
									.parseDouble("3.377017")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Apapa-Warehouse Road 2 Branch").flat(true);

			MarkerOptions marker748 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.449999"), Double
									.parseDouble("3.3644773")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Apapa-Warehouse Road Branch").flat(true);

			MarkerOptions marker749 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4499942"), Double
									.parseDouble("3.296625")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Bba Branch").flat(true);

			MarkerOptions marker750 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4535761"), Double
									.parseDouble("3.3872332")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Broad Street 2 Branch").flat(true);

			MarkerOptions marker751 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4533611"), Double
									.parseDouble("3.3874713")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Broad Street Branch").flat(true);

			MarkerOptions marker752 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.449621"), Double
									.parseDouble("3.5285153")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Chevron Branch").flat(true);

			MarkerOptions marker753 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460848"), Double
									.parseDouble("3.258734")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Coker Branch").flat(true);

			MarkerOptions marker754 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.451209"), Double
									.parseDouble("3.4202362")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Daleko Branch").flat(true);

			MarkerOptions marker755 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.596245"), Double
									.parseDouble("3.3382763")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Eleganza Branch").flat(true);

			MarkerOptions marker756 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.462154"), Double
									.parseDouble("3.285871")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Festac Branch").flat(true);

			MarkerOptions marker757 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5616377"), Double
									.parseDouble("3.3820586")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Gbagada Branch").flat(true);

			MarkerOptions marker758 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.495039"), Double
									.parseDouble("3.380811")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Herbert Macaulay-Sabo Branch").flat(true);

			MarkerOptions marker759 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4622005"), Double
									.parseDouble("3.3870828")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Idumagbo Branch").flat(true);

			MarkerOptions marker760 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4624943"), Double
									.parseDouble("3.3885628")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Idumota - Enu-Owa Branch").flat(true);

			MarkerOptions marker761 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4633854"), Double
									.parseDouble("3.3856868")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Idumota-Ashogbon Branch").flat(true);

			MarkerOptions marker762 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4596883"), Double
									.parseDouble("3.3853342")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Idumota- Nnamdi Azikwe Branch").flat(true);

			MarkerOptions marker763 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.59609"), Double
									.parseDouble("3.3530113")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ikeja - Allen Avenue 2 Branch").flat(true);

			MarkerOptions marker764 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6044331"), Double
									.parseDouble("3.3485431")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ikeja - Allen Avenue Branch").flat(true);

			MarkerOptions marker765 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6326615"), Double
									.parseDouble("3.3391142")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ikeja - Ogba Branch").flat(true);

			MarkerOptions marker766 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5243793"), Double
									.parseDouble("3.377017")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ikeja- Intl Airport Branch").flat(true);

			MarkerOptions marker767 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.618717"), Double
									.parseDouble("3.345765")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ikeja-Adeniyi Jones Branch").flat(true);

			MarkerOptions marker768 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.585291"), Double
									.parseDouble("3.351723")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ikeja-Gra Branch").flat(true);

			MarkerOptions marker769 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.56244"), Double
									.parseDouble("3.3648213")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ikorodu Road Branch").flat(true);

			MarkerOptions marker770 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601573"), Double
									.parseDouble("3.388936")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ikorodu Road Ketu Branch").flat(true);

			MarkerOptions marker771 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4639538"), Double
									.parseDouble("3.5544102")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ikota Branch").flat(true);

			MarkerOptions marker772 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.441971"), Double
									.parseDouble("3.418322")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ikoyi - Awolowo Road Branch").flat(true);

			MarkerOptions marker773 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.553648"), Double
									.parseDouble("3.356674")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ilupeju Branch").flat(true);

			MarkerOptions marker774 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.46918"), Double
									.parseDouble("3.2409313")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Intnl Trade Fair-Aspamda Branch").flat(true);

			MarkerOptions marker775 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6326615"), Double
									.parseDouble("3.3391142")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Isheri Branch").flat(true);

			MarkerOptions marker776 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.521598"), Double
									.parseDouble("3.322123")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Isolo - Ire Akari Branch").flat(true);

			MarkerOptions marker777 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.515840"), Double
									.parseDouble("3.319866")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Isolo - Oke Afa Road Branch").flat(true);

			MarkerOptions marker778 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5243793"), Double
									.parseDouble("3.377017")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ladipo Branch").flat(true);

			MarkerOptions marker779 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.539393"), Double
									.parseDouble("3.345083")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Matori-Ladipo Street Branch").flat(true);

			MarkerOptions marker780 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459011"), Double
									.parseDouble("3.304256")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Maza Maza Branch").flat(true);

			MarkerOptions marker781 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.627364"), Double
									.parseDouble("3.322609")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Mushin-Idi Oro Branch").flat(true);

			MarkerOptions marker782 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601053"), Double
									.parseDouble("3.338636")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Oba Akran 1  Branch").flat(true);

			MarkerOptions marker783 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.571095"), Double
									.parseDouble("3.396020")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ogudu Branch").flat(true);

			MarkerOptions marker784 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.508738"), Double
									.parseDouble("3.368106")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Ojuelegba Branch").flat(true);

			MarkerOptions marker785 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45989"), Double
									.parseDouble("3.3828913")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Oke-Arin Branch").flat(true);

			MarkerOptions marker786 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.437592"), Double
									.parseDouble("3.357608")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Olodi Apapa Branch").flat(true);

			MarkerOptions marker787 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4598084"), Double
									.parseDouble("3.216476")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Orile Branch").flat(true);

			MarkerOptions marker788 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.498510"), Double
									.parseDouble("3.378995")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Oyingbo Branch").flat(true);

			MarkerOptions marker789 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.450192"), Double
									.parseDouble("3.3613066")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Point Road Apapa Branch").flat(true);

			MarkerOptions marker790 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4478905"), Double
									.parseDouble("3.2545262")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Seme Border Branch").flat(true);

			MarkerOptions marker791 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5243793"), Double
									.parseDouble("3.377017")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("St Patrick Branch").flat(true);

			MarkerOptions marker792 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.491330"), Double
									.parseDouble("3.356570")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Surulere Branch").flat(true);

			MarkerOptions marker793 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.497366"), Double
									.parseDouble("3.355607")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Tbs Branch").flat(true);

			MarkerOptions marker794 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5222786"), Double
									.parseDouble("3.3831812")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("Unilag Branch").flat(true);

			MarkerOptions marker795 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430235"), Double
									.parseDouble("3.409348")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("VI - Adeola Odeku Branch").flat(true);

			MarkerOptions marker796 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.434338"), Double
									.parseDouble("3.431110")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("VI - Bishop Aboyade Cole Branch").flat(true);

			MarkerOptions marker797 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.432266"), Double
									.parseDouble("3.440493")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("VI - Ligali Ayorinde Branch").flat(true);

			MarkerOptions marker798 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429553"), Double
									.parseDouble("3.4374949")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("VI-Ajose Adeogun Branch").flat(true);

			MarkerOptions marker799 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431605"), Double
									.parseDouble("3.423955")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("VI-Akin Adesola Branch").flat(true);

			MarkerOptions marker800 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4295529"), Double
									.parseDouble("3.4309288")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.ecobank))

					.title("VI-Oyin Jolayemi Branch").flat(true);

			MarkerOptions marker801 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.631205"), Double
									.parseDouble("3.321252")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Abule Egba Branch").flat(true);

			MarkerOptions marker802 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.626694"), Double
									.parseDouble("3.34729")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Acme Road Branch").flat(true);

			MarkerOptions marker803 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.49454"), Double
									.parseDouble("3.357266")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Adeniran Ogunsanya Branch").flat(true);

			MarkerOptions marker804 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.613956"), Double
									.parseDouble("3.3437596")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Adeniyi Jones Branch").flat(true);

			MarkerOptions marker805 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4322078"), Double
									.parseDouble("3.4172476")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Adeola Odeku Branch").flat(true);

			MarkerOptions marker806 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.426339"), Double
									.parseDouble("3.430141")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Adetokunbo Ademola Branch").flat(true);

			MarkerOptions marker807 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430899"), Double
									.parseDouble("3.425151")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Adeyemo Alakija Branch").flat(true);

			MarkerOptions marker808 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.631163"), Double
									.parseDouble("3.32147")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Agege Motor Road Branch").flat(true);

			MarkerOptions marker809 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.618848"), Double
									.parseDouble("3.3451772")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Agidingbi Branch").flat(true);

			MarkerOptions marker810 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.496249"), Double
									.parseDouble("3.340090")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Aguda Branch").flat(true);

			MarkerOptions marker811 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431642"), Double
									.parseDouble("3.424192")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Akin Adesola Branch").flat(true);

			MarkerOptions marker812 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600783"), Double
									.parseDouble("3.309706")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Akowonjo Branch").flat(true);

			MarkerOptions marker813 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458343"), Double
									.parseDouble("3.192084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Alaba International Branch").flat(true);

			MarkerOptions marker814 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.659350"), Double
									.parseDouble("3.292328")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Alagbado Branch").flat(true);

			MarkerOptions marker815 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.497988"), Double
									.parseDouble("3.343929")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Alaka Branch").flat(true);

			MarkerOptions marker816 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6157002"), Double
									.parseDouble("3.3616816")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Alausa Branch").flat(true);

			MarkerOptions marker817 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6023364"), Double
									.parseDouble("3.3496158")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Allen 2 Branch").flat(true);

			MarkerOptions marker818 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5985698"), Double
									.parseDouble("3.3515707")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Allen Branch").flat(true);

			MarkerOptions marker819 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6079978"), Double
									.parseDouble("3.3080864")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Aluminium Village Branch").flat(true);

			MarkerOptions marker820 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.449999"), Double
									.parseDouble("3.2644773")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Amuwo Odofin Branch").flat(true);

			MarkerOptions marker821 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5593403"), Double
									.parseDouble("3.3667964")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Anthony Village Branch").flat(true);

			MarkerOptions marker822 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4395353"), Double
									.parseDouble("3.3667588")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Apapa (Warehouse Road) Branch").flat(true);

			MarkerOptions marker823 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4423343"), Double
									.parseDouble("3.3741953")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Apapa Road Branch").flat(true);

			MarkerOptions marker824 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.46918"), Double
									.parseDouble("3.2409313")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Aspamda Branch").flat(true);

			MarkerOptions marker825 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6172768"), Double
									.parseDouble("3.3597688")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Awolowo Way Branch").flat(true);

			MarkerOptions marker826 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.417948"), Double
									.parseDouble("2.8796518")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Badagry Branch").flat(true);

			MarkerOptions marker827 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4345961"), Double
									.parseDouble("3.4227223")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Bar Beach Branch").flat(true);

			MarkerOptions marker828 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.562177"), Double
									.parseDouble("3.349131")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Bolade Oshodi Branch").flat(true);

			MarkerOptions marker829 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4436168"), Double
									.parseDouble("3.4269045")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Bourdillion Branch").flat(true);

			MarkerOptions marker830 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4562323"), Double
									.parseDouble("3.3806404")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Broad Street Branch").flat(true);

			MarkerOptions marker831 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4509322"), Double
									.parseDouble("3.3935254")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Catholic Mission Branch").flat(true);

			MarkerOptions marker832 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.437592"), Double
									.parseDouble("3.357608")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Charity Oshodi Branch").flat(true);

			MarkerOptions marker833 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.46947"), Double
									.parseDouble("3.3287113")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Coker Branch").flat(true);

			MarkerOptions marker834 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.593152"), Double
									.parseDouble("3.372016")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Computer Village Branch").flat(true);

			MarkerOptions marker835 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.439160"), Double
									.parseDouble("3.375242")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Creek Road Branch").flat(true);

			MarkerOptions marker836 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6093474"), Double
									.parseDouble("3.305496")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Dopemu Branch").flat(true);

			MarkerOptions marker837 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.461949"), Double
									.parseDouble("3.383831")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ebute-Ero Branch").flat(true);

			MarkerOptions marker838 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5350041"), Double
									.parseDouble("3.3038793")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ejigbo Branch").flat(true);

			MarkerOptions marker839 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5820936"), Double
									.parseDouble("3.9740334")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Epe Branch").flat(true);

			MarkerOptions marker840 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.481813"), Double
									.parseDouble("3.358966")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Eric-Moore Branch").flat(true);

			MarkerOptions marker841 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.443729"), Double
									.parseDouble("3.422630")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Falomo Round-about Branch").flat(true);

			MarkerOptions marker842 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.443751"), Double
									.parseDouble("3.422661")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Falomo Branch").flat(true);

			MarkerOptions marker843 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.466445"), Double
									.parseDouble("3.283514")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Festac Branch").flat(true);

			MarkerOptions marker844 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.550587"), Double
									.parseDouble("3.394398")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Gbagada 2 Branch").flat(true);

			MarkerOptions marker845 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.552657"), Double
									.parseDouble("3.391425")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Gbagada Branch").flat(true);
			MarkerOptions marker846 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430981"), Double
									.parseDouble("3.430790")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Head Office Annex 2 Branch").flat(true);

			MarkerOptions marker847 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430327"), Double
									.parseDouble("3.439700")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Head Office Branch").flat(true);

			MarkerOptions marker848 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.495071"), Double
									.parseDouble("3.380799")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Herbert Macaulay Branch").flat(true);

			MarkerOptions marker849 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.492398"), Double
									.parseDouble("3.381951")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Herbert Macaulay 2 Branch").flat(true);

			MarkerOptions marker850 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.587109"), Double
									.parseDouble("3.286114")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Idimu Branch").flat(true);

			MarkerOptions marker851 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.527188"), Double
									.parseDouble("3.354507")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Idi-Oro Branch").flat(true);

			MarkerOptions marker852 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.462684"), Double
									.parseDouble("3.389359")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Idumagbo Branch").flat(true);

			MarkerOptions marker853 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459624"), Double
									.parseDouble("3.386802")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Idumota Branch").flat(true);

			MarkerOptions marker854 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.627886"), Double
									.parseDouble("3.331713")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Iju Branch").flat(true);

			MarkerOptions marker855 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601053"), Double
									.parseDouble("3.338636")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ikeja (Oba Akran) Branch").flat(true);

			MarkerOptions marker856 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.577182"), Double
									.parseDouble("3.355327")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ikeja Gra Branch").flat(true);

			MarkerOptions marker857 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.534135"), Double
									.parseDouble("3.367835")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ikorodu Road Branch").flat(true);

			MarkerOptions marker858 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.622457"), Double
									.parseDouble("3.498771")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ikorodu Branch").flat(true);

			MarkerOptions marker859 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4639538"), Double
									.parseDouble("3.5544102")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ikota Shopping Complex Branch").flat(true);

			MarkerOptions marker860 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.43833"), Double
									.parseDouble("3.5145313")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ikota Branch").flat(true);

			MarkerOptions marker861 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.550560"), Double
									.parseDouble("3.268389")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ikotun Branch").flat(true);

			MarkerOptions marker862 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.441982"), Double
									.parseDouble("3.418491")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ikoyi Branch").flat(true);

			MarkerOptions marker863 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.570721"), Double
									.parseDouble("3.372329")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ilupeju 2 Branch").flat(true);

			MarkerOptions marker864 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.554823"), Double
									.parseDouble("3.354141")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ilupeju Branch").flat(true);

			MarkerOptions marker865 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5818185"), Double
									.parseDouble("3.3189461")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("International Airport (Terminal) Branch")
					.flat(true);

			MarkerOptions marker866 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6489168"), Double
									.parseDouble("3.3029958")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ipaja (Oke-Odo) Branch").flat(true);

			MarkerOptions marker867 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.551528"), Double
									.parseDouble("3.330687")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Isolo Branch").flat(true);

			MarkerOptions marker868 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.447103"), Double
									.parseDouble("3.414820")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Keffi Branch").flat(true);

			MarkerOptions marker869 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.604336"), Double
									.parseDouble("3.390854")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ketu Branch").flat(true);

			MarkerOptions marker870 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.452716"), Double
									.parseDouble("3.432365")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Kingsway Branch").flat(true);

			MarkerOptions marker871 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4349954"), Double
									.parseDouble("3.4146728")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Kofo Abayomi Branch").flat(true);

			MarkerOptions marker872 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600111"), Double
									.parseDouble("3.364972")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Kudirat Abiola Branch").flat(true);

			MarkerOptions marker873 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6146283"), Double
									.parseDouble("3.3435764")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ladipo Oluwole Branch").flat(true);

			MarkerOptions marker874 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.450225"), Double
									.parseDouble("3.394831")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Lagos Central Branch").flat(true);

			MarkerOptions marker875 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.531302"), Double
									.parseDouble("3.334066")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Laspotech Branch").flat(true);

			MarkerOptions marker876 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4625174"), Double
									.parseDouble("3.2005352")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Lasu Branch").flat(true);

			MarkerOptions marker877 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.511148"), Double
									.parseDouble("3.339403")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Lawanson Branch").flat(true);

			MarkerOptions marker878 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431224"), Double
									.parseDouble("3.469051")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Lekki Exp Branch").flat(true);

			MarkerOptions marker879 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.679638"), Double
									.parseDouble("3.279789")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Lekki Branch").flat(true);

			MarkerOptions marker880 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.448735"), Double
									.parseDouble("3.367127")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Liverpool Branch").flat(true);

			MarkerOptions marker881 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.61189"), Double
									.parseDouble("3.3671593")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Magodo Branch").flat(true);

			MarkerOptions marker882 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.464573"), Double
									.parseDouble("3.388349")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Marina 2  Branch").flat(true);

			MarkerOptions marker883 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453503"), Double
									.parseDouble("3.385663")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Marina Branch").flat(true);

			MarkerOptions marker884 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440543"), Double
									.parseDouble("3.358876")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Marine Road Branch").flat(true);

			MarkerOptions marker885 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.539437"), Double
									.parseDouble("3.3428737")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Matori Branch").flat(true);

			MarkerOptions marker886 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.595759"), Double
									.parseDouble("3.340095")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Medical Road Branch").flat(true);

			MarkerOptions marker887 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455097"), Double
									.parseDouble("3.356763")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Mobil Rd Branch").flat(true);

			MarkerOptions marker888 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.448331"), Double
									.parseDouble("3.405916")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Moloney Branch").flat(true);

			MarkerOptions marker889 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431965"), Double
									.parseDouble("3.431930")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Muri Okonola Branch").flat(true);

			MarkerOptions marker890 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6308684"), Double
									.parseDouble("3.3379769")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ogba Branch").flat(true);

			MarkerOptions marker891 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.628888"), Double
									.parseDouble("3.337987")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ogba 2 Branch").flat(true);

			MarkerOptions marker892 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.581701"), Double
									.parseDouble("3.382500")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ogudu Branch").flat(true);

			MarkerOptions marker893 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.503890"), Double
									.parseDouble("3.351442")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ogunlana Drive Branch").flat(true);

			MarkerOptions marker894 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.64021"), Double
									.parseDouble("3.3639713")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ojodu Branch").flat(true);

			MarkerOptions marker895 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458868"), Double
									.parseDouble("3.383543")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Okearin 2 Branch").flat(true);

			MarkerOptions marker896 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.554166"), Double
									.parseDouble("3.393732")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Oke-Arin Branch").flat(true);

			MarkerOptions marker897 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.515218"), Double
									.parseDouble("3.320300")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Okota Branch").flat(true);

			MarkerOptions marker898 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458962"), Double
									.parseDouble("3.301448")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Old Ojo Road Branch").flat(true);

			MarkerOptions marker899 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.427624"), Double
									.parseDouble("3.429145")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Olosa Branch").flat(true);

			MarkerOptions marker900 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.596484"), Double
									.parseDouble("3.345167")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Olowu Branch").flat(true);
			
			googleMap.addMarker(marker601);
			googleMap.addMarker(marker602);
			googleMap.addMarker(marker603);
			googleMap.addMarker(marker604);
			googleMap.addMarker(marker605);
			googleMap.addMarker(marker606);
			googleMap.addMarker(marker607);
			googleMap.addMarker(marker608);
			googleMap.addMarker(marker609);
			googleMap.addMarker(marker610);
			googleMap.addMarker(marker611);
			googleMap.addMarker(marker612);
			googleMap.addMarker(marker613);
			googleMap.addMarker(marker614);
			googleMap.addMarker(marker615);
			googleMap.addMarker(marker616);
			googleMap.addMarker(marker617);
			googleMap.addMarker(marker618);
			googleMap.addMarker(marker619);
			googleMap.addMarker(marker620);
			googleMap.addMarker(marker621);
			googleMap.addMarker(marker622);
			googleMap.addMarker(marker623);
			googleMap.addMarker(marker624);
			googleMap.addMarker(marker625);
			googleMap.addMarker(marker626);
			googleMap.addMarker(marker627);
			googleMap.addMarker(marker628);
			googleMap.addMarker(marker629);
			googleMap.addMarker(marker630);
			googleMap.addMarker(marker631);
			googleMap.addMarker(marker632);
			googleMap.addMarker(marker633);
			googleMap.addMarker(marker634);
			googleMap.addMarker(marker635);
			googleMap.addMarker(marker636);
			googleMap.addMarker(marker637);
			googleMap.addMarker(marker638);
			googleMap.addMarker(marker639);
			googleMap.addMarker(marker640);
			googleMap.addMarker(marker641);
			googleMap.addMarker(marker642);
			googleMap.addMarker(marker643);
			googleMap.addMarker(marker644);
			googleMap.addMarker(marker645);
			googleMap.addMarker(marker646);
			googleMap.addMarker(marker647);
			googleMap.addMarker(marker648);
			googleMap.addMarker(marker649);
			googleMap.addMarker(marker650);
			googleMap.addMarker(marker651);
			googleMap.addMarker(marker652);
			googleMap.addMarker(marker653);
			googleMap.addMarker(marker654);
			googleMap.addMarker(marker655);
			googleMap.addMarker(marker656);
			googleMap.addMarker(marker657);
			googleMap.addMarker(marker658);
			googleMap.addMarker(marker659);
			googleMap.addMarker(marker660);
			googleMap.addMarker(marker661);
			googleMap.addMarker(marker662);
			googleMap.addMarker(marker663);
			googleMap.addMarker(marker664);
			googleMap.addMarker(marker665);
			googleMap.addMarker(marker666);
			googleMap.addMarker(marker667);
			googleMap.addMarker(marker668);
			googleMap.addMarker(marker669);
			googleMap.addMarker(marker670);
			googleMap.addMarker(marker671);
			googleMap.addMarker(marker672);
			googleMap.addMarker(marker673);
			googleMap.addMarker(marker674);
			googleMap.addMarker(marker675);
			googleMap.addMarker(marker676);
			googleMap.addMarker(marker677);
			googleMap.addMarker(marker678);
			googleMap.addMarker(marker679);
			googleMap.addMarker(marker680);
			googleMap.addMarker(marker681);
			googleMap.addMarker(marker682);
			googleMap.addMarker(marker683);
			googleMap.addMarker(marker684);
			googleMap.addMarker(marker685);
			googleMap.addMarker(marker686);
			googleMap.addMarker(marker687);
			googleMap.addMarker(marker688);
			googleMap.addMarker(marker689);
			googleMap.addMarker(marker690);
			googleMap.addMarker(marker691);
			googleMap.addMarker(marker692);
			googleMap.addMarker(marker693);
			googleMap.addMarker(marker694);
			googleMap.addMarker(marker695);
			googleMap.addMarker(marker696);
			googleMap.addMarker(marker697);
			googleMap.addMarker(marker698);
			googleMap.addMarker(marker699);
			googleMap.addMarker(marker700);
			googleMap.addMarker(marker701);
			googleMap.addMarker(marker702);
			googleMap.addMarker(marker703);
			googleMap.addMarker(marker704);
			googleMap.addMarker(marker705);
			googleMap.addMarker(marker706);
			googleMap.addMarker(marker707);
			googleMap.addMarker(marker708);
			googleMap.addMarker(marker709);
			googleMap.addMarker(marker710);
			googleMap.addMarker(marker711);
			googleMap.addMarker(marker712);
			googleMap.addMarker(marker713);
			googleMap.addMarker(marker714);
			googleMap.addMarker(marker715);
			googleMap.addMarker(marker716);
			googleMap.addMarker(marker717);
			googleMap.addMarker(marker718);
			googleMap.addMarker(marker719);
			googleMap.addMarker(marker720);
			googleMap.addMarker(marker721);
			googleMap.addMarker(marker722);
			googleMap.addMarker(marker723);
			googleMap.addMarker(marker724);
			googleMap.addMarker(marker725);
			googleMap.addMarker(marker726);
			googleMap.addMarker(marker727);
			googleMap.addMarker(marker728);
			googleMap.addMarker(marker729);
			googleMap.addMarker(marker730);
			googleMap.addMarker(marker731);
			googleMap.addMarker(marker732);
			googleMap.addMarker(marker733);
			googleMap.addMarker(marker734);
			googleMap.addMarker(marker735);
			googleMap.addMarker(marker736);
			googleMap.addMarker(marker737);
			googleMap.addMarker(marker738);
			googleMap.addMarker(marker739);
			googleMap.addMarker(marker740);
			googleMap.addMarker(marker741);
			googleMap.addMarker(marker742);
			googleMap.addMarker(marker743);
			googleMap.addMarker(marker744);
			googleMap.addMarker(marker745);
			googleMap.addMarker(marker746);
			googleMap.addMarker(marker747);
			googleMap.addMarker(marker748);
			googleMap.addMarker(marker749);
			googleMap.addMarker(marker750);
			googleMap.addMarker(marker751);
			googleMap.addMarker(marker752);
			googleMap.addMarker(marker753);
			googleMap.addMarker(marker754);
			googleMap.addMarker(marker755);
			googleMap.addMarker(marker756);
			googleMap.addMarker(marker757);
			googleMap.addMarker(marker758);
			googleMap.addMarker(marker759);
			googleMap.addMarker(marker760);
			googleMap.addMarker(marker761);
			googleMap.addMarker(marker762);
			googleMap.addMarker(marker763);
			googleMap.addMarker(marker764);
			googleMap.addMarker(marker765);
			googleMap.addMarker(marker766);
			googleMap.addMarker(marker767);
			googleMap.addMarker(marker768);
			googleMap.addMarker(marker769);
			googleMap.addMarker(marker770);
			googleMap.addMarker(marker771);
			googleMap.addMarker(marker772);
			googleMap.addMarker(marker773);
			googleMap.addMarker(marker774);
			googleMap.addMarker(marker775);
			googleMap.addMarker(marker776);
			googleMap.addMarker(marker777);
			googleMap.addMarker(marker778);
			googleMap.addMarker(marker779);
			googleMap.addMarker(marker780);
			googleMap.addMarker(marker781);
			googleMap.addMarker(marker782);
			googleMap.addMarker(marker783);
			googleMap.addMarker(marker784);
			googleMap.addMarker(marker785);
			googleMap.addMarker(marker786);
			googleMap.addMarker(marker787);
			googleMap.addMarker(marker788);
			googleMap.addMarker(marker789);
			googleMap.addMarker(marker790);
			googleMap.addMarker(marker791);
			googleMap.addMarker(marker792);
			googleMap.addMarker(marker793);
			googleMap.addMarker(marker794);
			googleMap.addMarker(marker795);
			googleMap.addMarker(marker796);
			googleMap.addMarker(marker797);
			googleMap.addMarker(marker798);
			googleMap.addMarker(marker799);
			googleMap.addMarker(marker800);
			googleMap.addMarker(marker801);
			googleMap.addMarker(marker802);
			googleMap.addMarker(marker803);
			googleMap.addMarker(marker804);
			googleMap.addMarker(marker805);
			googleMap.addMarker(marker806);
			googleMap.addMarker(marker807);
			googleMap.addMarker(marker808);
			googleMap.addMarker(marker809);
			googleMap.addMarker(marker810);
			googleMap.addMarker(marker811);
			googleMap.addMarker(marker812);
			googleMap.addMarker(marker813);
			googleMap.addMarker(marker814);
			googleMap.addMarker(marker815);
			googleMap.addMarker(marker816);
			googleMap.addMarker(marker817);
			googleMap.addMarker(marker818);
			googleMap.addMarker(marker819);
			googleMap.addMarker(marker820);
			googleMap.addMarker(marker821);
			googleMap.addMarker(marker822);
			googleMap.addMarker(marker823);
			googleMap.addMarker(marker824);
			googleMap.addMarker(marker825);
			googleMap.addMarker(marker826);
			googleMap.addMarker(marker827);
			googleMap.addMarker(marker828);
			googleMap.addMarker(marker829);
			googleMap.addMarker(marker830);
			googleMap.addMarker(marker831);
			googleMap.addMarker(marker832);
			googleMap.addMarker(marker833);
			googleMap.addMarker(marker834);
			googleMap.addMarker(marker835);
			googleMap.addMarker(marker836);
			googleMap.addMarker(marker837);
			googleMap.addMarker(marker838);
			googleMap.addMarker(marker839);
			googleMap.addMarker(marker840);
			googleMap.addMarker(marker841);
			googleMap.addMarker(marker842);
			googleMap.addMarker(marker843);
			googleMap.addMarker(marker844);
			googleMap.addMarker(marker845);
			googleMap.addMarker(marker846);
			googleMap.addMarker(marker847);
			googleMap.addMarker(marker848);
			googleMap.addMarker(marker849);
			googleMap.addMarker(marker850);
			googleMap.addMarker(marker851);
			googleMap.addMarker(marker852);
			googleMap.addMarker(marker853);
			googleMap.addMarker(marker854);
			googleMap.addMarker(marker855);
			googleMap.addMarker(marker856);
			googleMap.addMarker(marker857);
			googleMap.addMarker(marker858);
			googleMap.addMarker(marker859);
			googleMap.addMarker(marker860);
			googleMap.addMarker(marker861);
			googleMap.addMarker(marker862);
			googleMap.addMarker(marker863);
			googleMap.addMarker(marker864);
			googleMap.addMarker(marker865);
			googleMap.addMarker(marker866);
			googleMap.addMarker(marker867);
			googleMap.addMarker(marker868);
			googleMap.addMarker(marker869);
			googleMap.addMarker(marker870);
			googleMap.addMarker(marker871);
			googleMap.addMarker(marker872);
			googleMap.addMarker(marker873);
			googleMap.addMarker(marker874);
			googleMap.addMarker(marker875);
			googleMap.addMarker(marker876);
			googleMap.addMarker(marker877);
			googleMap.addMarker(marker878);
			googleMap.addMarker(marker879);
			googleMap.addMarker(marker880);
			googleMap.addMarker(marker881);
			googleMap.addMarker(marker882);
			googleMap.addMarker(marker883);
			googleMap.addMarker(marker884);
			googleMap.addMarker(marker885);
			googleMap.addMarker(marker886);
			googleMap.addMarker(marker887);
			googleMap.addMarker(marker888);
			googleMap.addMarker(marker889);
			googleMap.addMarker(marker890);
			googleMap.addMarker(marker891);
			googleMap.addMarker(marker892);
			googleMap.addMarker(marker893);
			googleMap.addMarker(marker894);
			googleMap.addMarker(marker895);
			googleMap.addMarker(marker896);
			googleMap.addMarker(marker897);
			googleMap.addMarker(marker898);
			googleMap.addMarker(marker899);
			googleMap.addMarker(marker900);

		}

		private void markersFour() {
			MarkerOptions marker901 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.586388"), Double
									.parseDouble("3.363701")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Opebi Branch").flat(true);

			MarkerOptions marker902 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6097874"), Double
									.parseDouble("3.3505178")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Oregun Branch").flat(true);

			MarkerOptions marker903 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.436120"), Double
									.parseDouble("3.416443")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ozumba Mbadiwe 2 Branch").flat(true);

			MarkerOptions marker904 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.437975"), Double
									.parseDouble("3.438117")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Ozumba Mbadiwe Branch").flat(true);

			MarkerOptions marker905 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5622275"), Double
									.parseDouble("3.3469411")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Palm Avenue Branch").flat(true);

			MarkerOptions marker906 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6426449"), Double
									.parseDouble("3.321242")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Pen Cinema Branch").flat(true);

			MarkerOptions marker907 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.427023"), Double
									.parseDouble("3.421211")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Saka Tinubu Branch").flat(true);

			MarkerOptions marker908 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.659350"), Double
									.parseDouble("3.292328")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Sango 2 Branch").flat(true);

			MarkerOptions marker909 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.681769"), Double
									.parseDouble("3.162099")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Sango Ota 3 Branch").flat(true);

			MarkerOptions marker910 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.702393"), Double
									.parseDouble("3.252543")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Sango Ota Branch").flat(true);

			MarkerOptions marker911 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4308995"), Double
									.parseDouble("3.4229997")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Sanusi Fafunwa Branch").flat(true);

			MarkerOptions marker912 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.454908"), Double
									.parseDouble("3.257607")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Satelite Town Branch").flat(true);

			MarkerOptions marker913 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.3803005"), Double
									.parseDouble("2.7070355")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Seme Border Branch").flat(true);

			MarkerOptions marker914 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4293029"), Double
									.parseDouble("3.3431761")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Snake Island Branch").flat(true);

			MarkerOptions marker915 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5403377"), Double
									.parseDouble("3.3906389")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("St Finbarrs Branch").flat(true);

			MarkerOptions marker916 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.510031"), Double
									.parseDouble("3.364180")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Surulere Branch").flat(true);

			MarkerOptions marker917 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.505781"), Double
									.parseDouble("3.365714")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Tejuosho Branch").flat(true);

			MarkerOptions marker918 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4660194"), Double
									.parseDouble("3.2417607")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Trade Fair Complex Branch").flat(true);

			MarkerOptions marker919 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440880"), Double
									.parseDouble("3.331643")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Trinity 2 Branch").flat(true);

			MarkerOptions marker920 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.441008"), Double
									.parseDouble("3.330899")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Trinity Branch").flat(true);

			MarkerOptions marker921 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455280"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Warehouse Road 2 Branch").flat(true);

			MarkerOptions marker922 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.443915"), Double
									.parseDouble("3.373027")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Wharf Road Branch").flat(true);

			MarkerOptions marker923 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.517745"), Double
									.parseDouble("3.373935")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.zenithbank))

					.title("Yabatech Branch").flat(true);

			MarkerOptions marker924 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.434129"), Double
									.parseDouble("3.43094")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Aboyade Cole Branch").flat(true);

			MarkerOptions marker925 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6340585"), Double
									.parseDouble("3.2986175")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Abule Egba Branch").flat(true);

			MarkerOptions marker926 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.435443"), Double
									.parseDouble("3.42959")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Adeola Hopewell Branch").flat(true);

			MarkerOptions marker927 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430533"), Double
									.parseDouble("3.415624")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Adeola Odeku Branch").flat(true);

			MarkerOptions marker928 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431109"), Double
									.parseDouble("3.424777")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Adetokunbo Ademola Branch").flat(true);

			MarkerOptions marker929 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431108"), Double
									.parseDouble("3.424658")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Adetokunbo Ademola 2 Branch").flat(true);

			MarkerOptions marker930 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431597"), Double
									.parseDouble("3.42458")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Adeyemo Alakija Branch").flat(true);

			MarkerOptions marker931 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.43164"), Double
									.parseDouble("3.424149")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Akin Adesola Branch").flat(true);

			MarkerOptions marker932 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4547538"), Double
									.parseDouble("3.381842")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Alaba Flex Branch").flat(true);

			MarkerOptions marker933 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.617472"), Double
									.parseDouble("3.35621")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Alausa Branch").flat(true);

			MarkerOptions marker934 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.598016"), Double
									.parseDouble("3.353736")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Allen 2 Branch").flat(true);

			MarkerOptions marker935 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.602866"), Double
									.parseDouble("3.351214")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Allen Avenue Branch").flat(true);

			MarkerOptions marker936 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.432127"), Double
									.parseDouble("3.426987")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Amodu Tijani Branch").flat(true);

			MarkerOptions marker937 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.44264"), Double
									.parseDouble("3.376463")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Apapa 2 Wharf Branch").flat(true);

			MarkerOptions marker938 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4598084"), Double
									.parseDouble("3.216476")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Aspamda 1 Branch").flat(true);

			MarkerOptions marker939 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4861217"), Double
									.parseDouble("3.3462882")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Babs Animashaun Branch").flat(true);

			MarkerOptions marker940 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4180866"), Double
									.parseDouble("2.8802794")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Badagry Branch").flat(true);

			MarkerOptions marker941 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6201498"), Double
									.parseDouble("3.2985721")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Berger 1 Branch").flat(true);

			MarkerOptions marker942 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4887951"), Double
									.parseDouble("3.3496425")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Bode Thomas Branch").flat(true);

			MarkerOptions marker943 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4375158"), Double
									.parseDouble("3.4057569")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Bonny Camp Military Barrack Branch").flat(true);

			MarkerOptions marker944 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.495384"), Double
									.parseDouble("3.3785")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Bornu Way Branch").flat(true);

			MarkerOptions marker945 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.59794"), Double
									.parseDouble("3.396442")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Breadfruit Branch").flat(true);
			MarkerOptions marker946 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438201"), Double
									.parseDouble("3.368106")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Burma Road Branch").flat(true);

			MarkerOptions marker947 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6225091"), Double
									.parseDouble("3.3486678")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Cadbury Nigeria Plc Branch").flat(true);

			MarkerOptions marker948 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4471697"), Double
									.parseDouble("3.4191359")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Chevron Branch").flat(true);

			MarkerOptions marker949 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.594849"), Double
									.parseDouble("3.339137")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Computer Village Branch").flat(true);

			MarkerOptions marker950 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.531297"), Double
									.parseDouble("3.338601")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Daleko Branch").flat(true);

			MarkerOptions marker951 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4608258"), Double
									.parseDouble("3.3858788")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Docemo Branch").flat(true);

			MarkerOptions marker952 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458267"), Double
									.parseDouble("3.415138")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Dolphin Estate Branch").flat(true);

			MarkerOptions marker953 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.483472"), Double
									.parseDouble("3.382513")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ebute-Metta Branch").flat(true);

			MarkerOptions marker954 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.591574"), Double
									.parseDouble("3.291062")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Egbeda Branch").flat(true);

			MarkerOptions marker955 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5358889"), Double
									.parseDouble("3.3031243")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ejigbo Branch").flat(true);

			MarkerOptions marker956 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442479"), Double
									.parseDouble("3.420340")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Falomo Branch").flat(true);

			MarkerOptions marker957 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.471552"), Double
									.parseDouble("3.274113")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Festac 1 Business Office Branch").flat(true);

			MarkerOptions marker958 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.470262"), Double
									.parseDouble("3.292430")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Festac 2 Branch").flat(true);

			MarkerOptions marker959 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.558397"), Double
									.parseDouble("3.391162")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Gbagada Branch").flat(true);

			MarkerOptions marker960 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.549108"), Double
									.parseDouble("3.377803")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Gbagada Express Branch").flat(true);

			MarkerOptions marker961 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4592261"), Double
									.parseDouble("3.1869181")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("H25/H26 Alaba International Branch").flat(true);

			MarkerOptions marker962 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.505717"), Double
									.parseDouble("3.377993")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Herbert Macaulay Branch").flat(true);

			MarkerOptions marker963 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.471879"), Double
									.parseDouble("3.324806")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ibafon Branch").flat(true);

			MarkerOptions marker964 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.433161"), Double
									.parseDouble("3.429141")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Idowu Taylor Branch").flat(true);

			MarkerOptions marker965 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458846"), Double
									.parseDouble("3.389710")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Idumagbo Branch").flat(true);

			MarkerOptions marker966 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453378"), Double
									.parseDouble("3.389908")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Idumota 2 Branch").flat(true);

			MarkerOptions marker967 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.450225"), Double
									.parseDouble("3.394831")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Igbosere Branch").flat(true);

			MarkerOptions marker968 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.633040"), Double
									.parseDouble("3.341532")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ijaye Ogba Branch").flat(true);

			MarkerOptions marker969 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.468982"), Double
									.parseDouble("3.366602")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ijora Branch").flat(true);

			MarkerOptions marker970 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.656860"), Double
									.parseDouble("3.323311")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Iju Branch").flat(true);

			MarkerOptions marker971 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.557820"), Double
									.parseDouble("3.367232")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ikorodu Road Branch").flat(true);

			MarkerOptions marker972 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4639538"), Double
									.parseDouble("3.5544102")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ikota Branch").flat(true);

			MarkerOptions marker973 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.561917"), Double
									.parseDouble("3.273772")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ikotun Business Office Branch").flat(true);

			MarkerOptions marker974 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.550982"), Double
									.parseDouble("3.355711")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ilupeju Branch").flat(true);

			MarkerOptions marker975 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4850477"), Double
									.parseDouble("3.361634")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Iponri Branch").flat(true);

			MarkerOptions marker976 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.549462"), Double
									.parseDouble("3.331832")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Isolo Branch").flat(true);

			MarkerOptions marker977 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.511113"), Double
									.parseDouble("3.339342")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Itire Rd Branch").flat(true);

			MarkerOptions marker978 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.604298"), Double
									.parseDouble("3.390818")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ketu Branch").flat(true);

			MarkerOptions marker979 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453559"), Double
									.parseDouble("3.389415")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Lagos East Branch").flat(true);

			MarkerOptions marker980 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4696317"), Double
									.parseDouble("3.1983754")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Lasu Branch").flat(true);

			MarkerOptions marker981 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.511620"), Double
									.parseDouble("3.342598")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Lawanson Branch").flat(true);

			MarkerOptions marker982 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4535761"), Double
									.parseDouble("3.3872332")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Lcb  Branch").flat(true);

			MarkerOptions marker983 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.447614"), Double
									.parseDouble("3.481414")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Lekki 1 Branch").flat(true);

			MarkerOptions marker984 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.516721"), Double
									.parseDouble("3.354041")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Luth Branch").flat(true);

			MarkerOptions marker985 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453504"), Double
									.parseDouble("3.385662")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Marina Branch").flat(true);

			MarkerOptions marker986 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.454655"), Double
									.parseDouble("3.383966")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Marina West Branch").flat(true);

			MarkerOptions marker987 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455682"), Double
									.parseDouble("3.381729")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Marine View Branch").flat(true);

			MarkerOptions marker988 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.456024"), Double
									.parseDouble("3.387936")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Martin Street Branch").flat(true);

			MarkerOptions marker989 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.535233"), Double
									.parseDouble("3.348967")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Matori 1 Branch").flat(true);

			MarkerOptions marker990 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.597164"), Double
									.parseDouble("3.340230")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Moneygram Office, Ikeja Branch").flat(true);

			MarkerOptions marker991 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.596082"), Double
									.parseDouble("3.3530583")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Mtn Opebi Branch").flat(true);

			MarkerOptions marker992 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.535592"), Double
									.parseDouble("3.348665")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Mushin Branch").flat(true);

			MarkerOptions marker993 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.572784"), Double
									.parseDouble("3.3221948")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("NAF Base Branch").flat(true);

			MarkerOptions marker994 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.614387"), Double
									.parseDouble("3.354593")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Nigerian Bottling Company Branch").flat(true);

			MarkerOptions marker995 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.48143"), Double
									.parseDouble("3.3593013")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Nigerian Breweries, Iganmu Branch").flat(true);

			MarkerOptions marker996 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459599"), Double
									.parseDouble("3.370183")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Nns Quorra Branch").flat(true);

			MarkerOptions marker997 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429553"), Double
									.parseDouble("3.439684")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Oando Head Office Branch").flat(true);

			MarkerOptions marker998 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601765"), Double
									.parseDouble("3.338247")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Oba Akran 1 Branch").flat(true);

			MarkerOptions marker999 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601053"), Double
									.parseDouble("3.338636")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Oba Akran 2 Branch").flat(true);

			MarkerOptions marker1000 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.602002"), Double
									.parseDouble("3.338117")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Oba Akran Branch").flat(true);

			MarkerOptions marker1001 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4496293"), Double
									.parseDouble("3.4077029")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Obalende Branch").flat(true);

			MarkerOptions marker1002 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.621752"), Double
									.parseDouble("3.336442")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ogba Branch").flat(true);

			MarkerOptions marker1003 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.575752"), Double
									.parseDouble("3.391100")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ogudu Branch").flat(true);

			MarkerOptions marker1004 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4619141"), Double
									.parseDouble("3.1556988")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ojo Egbeda Branch").flat(true);

			MarkerOptions marker1005 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6398349"), Double
									.parseDouble("3.3659236")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ojodu Branch").flat(true);

			MarkerOptions marker1006 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.637129"), Double
									.parseDouble("3.353147")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ojodu 2 Branch").flat(true);

			MarkerOptions marker1007 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.504053"), Double
									.parseDouble("3.319281")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Okearin 2 Branch").flat(true);

			MarkerOptions marker1008 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.483865"), Double
									.parseDouble("3.170312")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Okokomaiko Branch").flat(true);

			MarkerOptions marker1009 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.513850"), Double
									.parseDouble("3.321080")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Okota Branch").flat(true);

			MarkerOptions marker1010 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6127055"), Double
									.parseDouble("3.3521919")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Oluwalogbon House Branch").flat(true);

			MarkerOptions marker1011 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.551655"), Double
									.parseDouble("3.367418")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Onipanu Branch").flat(true);

			MarkerOptions marker1012 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600111"), Double
									.parseDouble("3.364972")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Oregun Branch").flat(true);

			MarkerOptions marker1013 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440923"), Double
									.parseDouble("3.331395")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Oshodi Expressway Branch").flat(true);

			MarkerOptions marker1014 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.42634"), Double
									.parseDouble("3.4281013")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Oyin-Jolayemi Branch").flat(true);

			MarkerOptions marker1015 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.44174"), Double
									.parseDouble("3.5286313")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Protea, Lekki Branch").flat(true);

			MarkerOptions marker1016 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.3803005"), Double
									.parseDouble("2.7070355")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Seme Border Branch").flat(true);

			MarkerOptions marker1017 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.594850"), Double
									.parseDouble("3.338498")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Simbiat Branch").flat(true);

			MarkerOptions marker1018 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.452344"), Double
									.parseDouble("3.389301")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Tinubu Branch").flat(true);

			MarkerOptions marker1019 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.434769"), Double
									.parseDouble("3.427438")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Total V/I Branch").flat(true);

			MarkerOptions marker1020 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453812"), Double
									.parseDouble("3.384465")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Uba House Branch").flat(true);

			MarkerOptions marker1021 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.481618"), Double
									.parseDouble("3.363330")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Uba Iganmu Branch").flat(true);

			MarkerOptions marker1022 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.574817"), Double
									.parseDouble("3.362084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Uba Maryland Branch").flat(true);

			MarkerOptions marker1023 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5192397"), Double
									.parseDouble("3.3895685")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Unilag Branch").flat(true);

			MarkerOptions marker1024 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.604557"), Double
									.parseDouble("3.367301")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Unilever Head Office Branch").flat(true);

			MarkerOptions marker1025 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440536"), Double
									.parseDouble("3.4045944")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("US Embassy Branch").flat(true);

			MarkerOptions marker1026 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438327"), Double
									.parseDouble("3.365099")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Warehouse Road 2 Branch").flat(true);

			MarkerOptions marker1027 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438820"), Double
									.parseDouble("3.367943")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Warehouse Road 3 Branch").flat(true);

			MarkerOptions marker1028 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438309"), Double
									.parseDouble("3.365059")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Warehouse Road Branch").flat(true);

			MarkerOptions marker1029 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.509923"), Double
									.parseDouble("3.363592")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Western Avenue Branch").flat(true);

			MarkerOptions marker1030 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442208"), Double
									.parseDouble("3.376386")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Wharf Road Branch").flat(true);

			MarkerOptions marker1031 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440838"), Double
									.parseDouble("3.379573")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Wharf Road 2 Branch").flat(true);

			MarkerOptions marker1032 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5581221"), Double
									.parseDouble("3.2696488")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Abule Egba Branch").flat(true);

			MarkerOptions marker1033 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.617304"), Double
									.parseDouble("3.345973")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Adeniyi Jones Branch").flat(true);

			MarkerOptions marker1034 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430251"), Double
									.parseDouble("3.417993")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Adeola Odeku Branch").flat(true);

			MarkerOptions marker1035 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429936"), Double
									.parseDouble("3.429901")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Adetokunbo Ademola Branch").flat(true);

			MarkerOptions marker1036 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.562407"), Double
									.parseDouble("3.372950")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("AFSS Branch").flat(true);

			MarkerOptions marker1037 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4495038"), Double
									.parseDouble("3.575799")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ajah Branch").flat(true);

			MarkerOptions marker1038 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430981"), Double
									.parseDouble("3.430790")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ajose Adeogun Branch").flat(true);

			MarkerOptions marker1039 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600783"), Double
									.parseDouble("3.309706")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Akowonjo Branch").flat(true);

			MarkerOptions marker1040 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601838"), Double
									.parseDouble("3.3492976")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Alausa Branch").flat(true);

			MarkerOptions marker1041 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6047313"), Double
									.parseDouble("3.3482193")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Allen Branch").flat(true);

			MarkerOptions marker1042 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5590852"), Double
									.parseDouble("3.3667892")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Anthony Village Branch").flat(true);

			MarkerOptions marker1043 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4630872"), Double
									.parseDouble("3.3595794")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("APM Terminal Branch").flat(true);

			MarkerOptions marker1044 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4489852"), Double
									.parseDouble("3.3572541")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Arakan Barracks Branch").flat(true);

			MarkerOptions marker1045 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4619194"), Double
									.parseDouble("3.1556988")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Aspamda Branch").flat(true);
			MarkerOptions marker1046 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5343359"), Double
									.parseDouble("3.3709238")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Berachah Branch").flat(true);

			MarkerOptions marker1047 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.612325"), Double
									.parseDouble("3.335936")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Berger Paints Branch").flat(true);

			MarkerOptions marker1048 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.609266"), Double
									.parseDouble("3.3502305")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Boulos Enterprise Branch").flat(true);

			MarkerOptions marker1049 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431195"), Double
									.parseDouble("3.425038")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("BRASS LNG Branch").flat(true);

			MarkerOptions marker1050 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4434695"), Double
									.parseDouble("3.4307102")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("British Airways Branch").flat(true);

			MarkerOptions marker1051 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453577"), Double
									.parseDouble("3.389471")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Broad Street Branch").flat(true);

			MarkerOptions marker1052 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.438133"), Double
									.parseDouble("3.369925")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Burma Road Branch").flat(true);

			MarkerOptions marker1053 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.449595"), Double
									.parseDouble("3.397894")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Catholic Mission Branch").flat(true);

			MarkerOptions marker1054 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.561121"), Double
									.parseDouble("3.380940")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Chevron Gbagada Branch").flat(true);

			MarkerOptions marker1055 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.441670"), Double
									.parseDouble("3.529524")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Chevron HQ Branch").flat(true);

			MarkerOptions marker1056 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444494"), Double
									.parseDouble("3.402341")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("City Mall Onikan Branch").flat(true);

			MarkerOptions marker1057 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459610"), Double
									.parseDouble("3.434863")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("CocaCola, Ikoyi Branch").flat(true);

			MarkerOptions marker1058 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.443298"), Double
									.parseDouble("3.372930")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Commercial Road, Apapa Branch").flat(true);

			MarkerOptions marker1059 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.594496"), Double
									.parseDouble("3.340365")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Computer Village Branch").flat(true);

			MarkerOptions marker1060 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.435797"), Double
									.parseDouble("3.364509")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Creek Road, Apapa Branch").flat(true);

			MarkerOptions marker1061 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.443901"), Double
									.parseDouble("3.475084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ebeano Supermarket Branch").flat(true);

			MarkerOptions marker1062 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430533"), Double
									.parseDouble("3.41557")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("e-Branch").flat(true);

			MarkerOptions marker1063 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600693"), Double
									.parseDouble("3.300757")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Egbeda Idimu Branch").flat(true);

			MarkerOptions marker1064 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4313815"), Double
									.parseDouble("3.4256958")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("EKO Hotels Branch").flat(true);

			MarkerOptions marker1065 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.627641"), Double
									.parseDouble("3.323574")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Pen Cinema Branch").flat(true);

			MarkerOptions marker1066 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5622275"), Double
									.parseDouble("3.3469411")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("ETISALAT Mushin Branch").flat(true);

			MarkerOptions marker1067 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4613001"), Double
									.parseDouble("3.4564816")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Etisalat Branch").flat(true);

			MarkerOptions marker1068 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4437484"), Double
									.parseDouble("3.4244031")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Falomo Ikoyi Branch").flat(true);

			MarkerOptions marker1069 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444099"), Double
									.parseDouble("3.294300")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Festac Branch").flat(true);

			MarkerOptions marker1070 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.623828"), Double
									.parseDouble("3.339685")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Freisland Foods Branch").flat(true);

			MarkerOptions marker1071 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.552627"), Double
									.parseDouble("3.391306")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Gbagada Branch").flat(true);

			MarkerOptions marker1072 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4647147"), Double
									.parseDouble("3.3005057")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Golden Tulip Branch").flat(true);

			MarkerOptions marker1073 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455280"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Grimaldi Branch").flat(true);

			MarkerOptions marker1074 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5036131"), Double
									.parseDouble("3.379734")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GT Assets Branch").flat(true);

			MarkerOptions marker1075 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.570721"), Double
									.parseDouble("3.372329")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTA Onipanu Branch").flat(true);

			MarkerOptions marker1076 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458519"), Double
									.parseDouble("3.3790603")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTA Aboyade Cole Branch").flat(true);

			MarkerOptions marker1077 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.427537"), Double
									.parseDouble("3.409279")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTA Santa Clara Court Branch").flat(true);

			MarkerOptions marker1078 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.599328"), Double
									.parseDouble("3.293679")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Boundary Branch").flat(true);

			MarkerOptions marker1079 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4507071"), Double
									.parseDouble("3.3911381")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Campos Branch").flat(true);

			MarkerOptions marker1080 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4816628"), Double
									.parseDouble("3.3682456")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Costain Branch").flat(true);

			MarkerOptions marker1081 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4657408"), Double
									.parseDouble("3.5467568")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Ebeano Branch").flat(true);

			MarkerOptions marker1082 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.470262"), Double
									.parseDouble("3.292430")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Festac Branch").flat(true);

			MarkerOptions marker1083 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.581601"), Double
									.parseDouble("3.320906")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Ikeja Branch").flat(true);

			MarkerOptions marker1084 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.649561"), Double
									.parseDouble("3.518328")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Ikorodu Branch").flat(true);

			MarkerOptions marker1085 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4439688"), Double
									.parseDouble("3.4215902")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Ikoyi Branch").flat(true);

			MarkerOptions marker1086 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4856163"), Double
									.parseDouble("3.3855351")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Jebba Branch").flat(true);

			MarkerOptions marker1087 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5010749"), Double
									.parseDouble("3.3196996")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Kingsway Branch").flat(true);

			MarkerOptions marker1088 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5010696"), Double
									.parseDouble("3.3196996")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Mile 2 Branch").flat(true);

			MarkerOptions marker1089 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5830719"), Double
									.parseDouble("3.3731849")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Shomolu Branch").flat(true);

			MarkerOptions marker1090 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.498054"), Double
									.parseDouble("3.360898")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Western Avenue Branch").flat(true);

			MarkerOptions marker1091 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455280"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTExpress Wharf Branch").flat(true);

			MarkerOptions marker1092 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.427023"), Double
									.parseDouble("3.421211")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("GTHomes Branch").flat(true);

			MarkerOptions marker1093 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6149615"), Double
									.parseDouble("3.3329104")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Guinness Ogba Plant Branch").flat(true);

			MarkerOptions marker1094 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.590638"), Double
									.parseDouble("3.393489")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ibafon Branch").flat(true);

			MarkerOptions marker1095 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5533455"), Double
									.parseDouble("3.3509883")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Idi Oro Branch").flat(true);

			MarkerOptions marker1096 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.587109"), Double
									.parseDouble("3.286114")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Idimu Rd, Egbeda Branch").flat(true);

			MarkerOptions marker1097 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4537212"), Double
									.parseDouble("3.3874837")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Idumota Branch").flat(true);

			MarkerOptions marker1098 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.574969"), Double
									.parseDouble("3.361438")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ikeja Cantonement Branch").flat(true);

			MarkerOptions marker1099 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5713629"), Double
									.parseDouble("3.319443")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ikeja General Hospital Branch").flat(true);

			MarkerOptions marker1100 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.621244"), Double
									.parseDouble("3.501697")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ikorodu Branch").flat(true);

			MarkerOptions marker1101 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600568"), Double
									.parseDouble("3.366069")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ikosi Branch").flat(true);

			MarkerOptions marker1102 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4639538"), Double
									.parseDouble("3.5544102")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ikota Shopping Complex Branch").flat(true);

			MarkerOptions marker1103 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444465"), Double
									.parseDouble("3.425280")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ikoyi Branch").flat(true);

			MarkerOptions marker1104 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.452562"), Double
									.parseDouble("3.427860")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ikoyi Club Branch").flat(true);

			MarkerOptions marker1105 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5564335"), Double
									.parseDouble("3.3545371")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ilupeju Branch").flat(true);

			MarkerOptions marker1106 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.546334"), Double
									.parseDouble("3.361303")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ilupeju Bypass Branch").flat(true);

			MarkerOptions marker1107 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.564844"), Double
									.parseDouble("3.322720")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("International Airport Branch").flat(true);

			MarkerOptions marker1108 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5357993"), Double
									.parseDouble("3.3803197")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Isolo Branch").flat(true);

			MarkerOptions marker1109 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.606289"), Double
									.parseDouble("3.392394")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ketu Branch").flat(true);

			MarkerOptions marker1110 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4342487"), Double
									.parseDouble("3.4348452")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("KPMG Branch").flat(true);

			MarkerOptions marker1111 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6136771"), Double
									.parseDouble("3.362613")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Lagos House of Assembly Branch").flat(true);

			MarkerOptions marker1112 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4696317"), Double
									.parseDouble("3.1983754")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("LASU Branch").flat(true);

			MarkerOptions marker1113 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458985"), Double
									.parseDouble("3.601521")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Lekki Branch").flat(true);

			MarkerOptions marker1114 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5243793"), Double
									.parseDouble("3.377017")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("LUTH Branch").flat(true);

			MarkerOptions marker1115 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.433308"), Double
									.parseDouble("3.420143")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Mega Plaza Branch").flat(true);

			MarkerOptions marker1116 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.491536"), Double
									.parseDouble("3.356611")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Mansard Adeniran Ogunsanya Branch").flat(true);

			MarkerOptions marker1117 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453505"), Double
									.parseDouble("3.385659")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Marina Branch").flat(true);

			MarkerOptions marker1118 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.539336"), Double
									.parseDouble("3.344935")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Matori Branch").flat(true);

			MarkerOptions marker1119 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5748099"), Double
									.parseDouble("3.3598781")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Mobolaji Bank Anthony Branch").flat(true);

			MarkerOptions marker1120 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.448543"), Double
									.parseDouble("3.406453")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Moloney Branch").flat(true);

			MarkerOptions marker1121 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.607762"), Double
									.parseDouble("3.348560")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("MTN Aromire Ikoyi Branch").flat(true);

			MarkerOptions marker1122 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.443586"), Double
									.parseDouble("3.427059")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Muliner Towers Branch").flat(true);

			MarkerOptions marker1123 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.536949"), Double
									.parseDouble("3.352278")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Mushin Branch").flat(true);

			MarkerOptions marker1124 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.480133"), Double
									.parseDouble("3.365968")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("NB Plc Head Office Branch").flat(true);

			MarkerOptions marker1125 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444464"), Double
									.parseDouble("3.424106")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("New Awolowo Road Branch").flat(true);

			MarkerOptions marker1126 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.43083"), Double
									.parseDouble("3.4257013")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("NLNG - C&C Towers Branch").flat(true);

			MarkerOptions marker1127 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.481425"), Double
									.parseDouble("3.361493")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("NPA Marina Branch").flat(true);

			MarkerOptions marker1128 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.450326"), Double
									.parseDouble("3.390657")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("NPA Tincan Branch").flat(true);

			MarkerOptions marker1129 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601250"), Double
									.parseDouble("3.338827")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Oba Akran Branch").flat(true);

			MarkerOptions marker1130 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.631661"), Double
									.parseDouble("3.340252")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ogba Branch").flat(true);

			MarkerOptions marker1131 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.575734"), Double
									.parseDouble("3.390600")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ogudu GRA Branch").flat(true);

			MarkerOptions marker1132 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.639815"), Double
									.parseDouble("3.368123")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ojodu Branch").flat(true);

			MarkerOptions marker1133 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.509901"), Double
									.parseDouble("3.364558")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Ojuelegba Branch").flat(true);

			MarkerOptions marker1134 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4609128"), Double
									.parseDouble("3.3853977")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Oke Arin Branch").flat(true);

			MarkerOptions marker1135 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.512296"), Double
									.parseDouble("3.321680")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Okota Branch").flat(true);

			MarkerOptions marker1136 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5830719"), Double
									.parseDouble("3.3731849")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Onipanu Branch").flat(true);

			MarkerOptions marker1137 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.589030"), Double
									.parseDouble("3.361555")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Opebi Branch").flat(true);

			MarkerOptions marker1138 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.589682"), Double
									.parseDouble("3.374843")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Oregun Branch").flat(true);

			MarkerOptions marker1139 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5180493"), Double
									.parseDouble("3.2815797")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Passport Office, Ikoyi Branch").flat(true);

			MarkerOptions marker1140 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429450"), Double
									.parseDouble("3.428862")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Plural House Branch").flat(true);

			MarkerOptions marker1141 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431110"), Double
									.parseDouble("3.425168")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Processing Center").flat(true);

			MarkerOptions marker1142 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.6141618"), Double
									.parseDouble("3.3578869")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Shoprite Alausa Branch").flat(true);

			MarkerOptions marker1143 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.472516"), Double
									.parseDouble("3.568616")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Slicks Bar VGC , Ajah Branch").flat(true);

			MarkerOptions marker1144 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.489487"), Double
									.parseDouble("3.357289")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Surulere Branch").flat(true);

			MarkerOptions marker1145 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.584242"), Double
									.parseDouble("3.3548985")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("The Place Restaurant Ikeja Branch").flat(true);
			MarkerOptions marker1146 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4311268"), Double
									.parseDouble("3.4223687")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("The Plaza Branch").flat(true);

			MarkerOptions marker1147 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.423565"), Double
									.parseDouble("3.426346")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Tiamiyu Savage Branch").flat(true);

			MarkerOptions marker1148 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.595711"), Double
									.parseDouble("3.351864")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Toyin Street Branch").flat(true);

			MarkerOptions marker1149 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.517619"), Double
									.parseDouble("3.397036")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("UNILAG Branch").flat(true);

			MarkerOptions marker1150 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.623259"), Double
									.parseDouble("3.339286")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("WAMCO Branch").flat(true);

			MarkerOptions marker1151 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.495087"), Double
									.parseDouble("3.380793")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Yaba Branch").flat(true);

			MarkerOptions marker1152 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.505226"), Double
									.parseDouble("3.378523")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("Yaba 2 Branch").flat(true);
			MarkerOptions marker1152b = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5767975"), Double
									.parseDouble("3.3199058")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("FAAN Branch").flat(true);

			MarkerOptions marker1152c = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4904856"), Double
									.parseDouble("3.3560933")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("LAWMA Ijora Branch").flat(true);

			MarkerOptions marker1152d = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5857553"), Double
									.parseDouble("3.3310613")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("MTN Maritime House, Apapa Branch").flat(true);

			MarkerOptions marker1152e = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5833854"), Double
									.parseDouble("3.3782406")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("MTN Ojota Branch").flat(true);

			MarkerOptions marker1152f = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.5834331"), Double
									.parseDouble("3.3629196")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.gtb))

					.title("MTN Opebi Branch").flat(true);

			MarkerOptions marker1153 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.463167"), Double
									.parseDouble("3.278171")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("1st Avenue Branch").flat(true);

			MarkerOptions marker1154 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.491527"), Double
									.parseDouble("3.356609")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Adeniran Ogunsanya Branch").flat(true);

			MarkerOptions marker1155 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431641"), Double
									.parseDouble("3.426722")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Adeola Hopewell Branch").flat(true);

			MarkerOptions marker1156 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430533"), Double
									.parseDouble("3.415597")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Adeola Odeku Branch").flat(true);

			MarkerOptions marker1157 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.426731"), Double
									.parseDouble("3.429311")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Adetokunbo Ademola Branch").flat(true);

			MarkerOptions marker1158 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.508857"), Double
									.parseDouble("3.311774")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Ago Palace Way Branch").flat(true);

			MarkerOptions marker1159 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430981"), Double
									.parseDouble("3.430790")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Ajose Adeogun Branch").flat(true);

			MarkerOptions marker1160 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430231"), Double
									.parseDouble("3.423828")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Akin Adesola Branch").flat(true);

			MarkerOptions marker1161 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600783"), Double
									.parseDouble("3.309706")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Akowonjo Branch").flat(true);

			MarkerOptions marker1162 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.588972"), Double
									.parseDouble("3.340286")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Alausa Secretariat Branch").flat(true);

			MarkerOptions marker1163 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.621108"), Double
									.parseDouble("3.360934")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Alausa Branch").flat(true);

			MarkerOptions marker1164 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.447933"), Double
									.parseDouble("3.409792")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Alfred Rewane Road Branch").flat(true);

			MarkerOptions marker1165 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.605025"), Double
									.parseDouble("3.350262")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Allen Avenue Branch").flat(true);

			MarkerOptions marker1166 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.441110"), Double
									.parseDouble("2.919306")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Aspamda Intl Market Branch").flat(true);

			MarkerOptions marker1167 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442141"), Double
									.parseDouble("3.418543")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Awolowo Road Branch").flat(true);

			MarkerOptions marker1168 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429357"), Double
									.parseDouble("2.892092")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Badagry Branch").flat(true);

			MarkerOptions marker1169 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460506"), Double
									.parseDouble("3.250034")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Bba Branch").flat(true);

			MarkerOptions marker1170 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.449646"), Double
									.parseDouble("3.403150")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("City Hall Branch").flat(true);

			MarkerOptions marker1171 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429302"), Double
									.parseDouble("3.268421")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Coker Orile Branch").flat(true);

			MarkerOptions marker1172 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.593582"), Double
									.parseDouble("3.338569")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Computer Village Branch").flat(true);

			MarkerOptions marker1173 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.436204"), Double
									.parseDouble("3.364503")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Creek Road Branch").flat(true);

			MarkerOptions marker1174 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.542545"), Double
									.parseDouble("3.296734")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Egbe Branch").flat(true);

			MarkerOptions marker1175 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.593111"), Double
									.parseDouble("3.986328")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Epe Branch").flat(true);

			MarkerOptions marker1176 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.542254"), Double
									.parseDouble("3.348606")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Fatai Atere Branch").flat(true);

			MarkerOptions marker1177 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.450815"), Double
									.parseDouble("3.429570")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Glover Road Branch").flat(true);

			MarkerOptions marker1178 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.461456"), Double
									.parseDouble("3.390013")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Idoluwo Branch").flat(true);

			MarkerOptions marker1179 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.656860"), Double
									.parseDouble("3.323311")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Iju Branch").flat(true);

			MarkerOptions marker1180 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.593611"), Double
									.parseDouble("3.357842")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Ikeja Plaza Branch").flat(true);

			MarkerOptions marker1181 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.589954"), Double
									.parseDouble("3.517641")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Ikorodu LG Branch").flat(true);

			MarkerOptions marker1182 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.619897"), Double
									.parseDouble("3.503553")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Ikorodu Branch").flat(true);

			MarkerOptions marker1183 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.554935"), Double
									.parseDouble("3.269658")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Ikotun Branch").flat(true);

			MarkerOptions marker1184 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.530430"), Double
									.parseDouble("3.343957")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Isolo Branch").flat(true);

			MarkerOptions marker1185 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.594850"), Double
									.parseDouble("3.338498")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Kudirat Abiola Way Branch").flat(true);

			MarkerOptions marker1186 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.523224"), Double
									.parseDouble("3.353109")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Luth Idi Araba Branch").flat(true);

			MarkerOptions marker1187 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.612587"), Double
									.parseDouble("3.368805")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Magodo Branch").flat(true);

			MarkerOptions marker1188 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.450581"), Double
									.parseDouble("3.390201")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Marina Branch").flat(true);

			MarkerOptions marker1189 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.532684"), Double
									.parseDouble("3.349595")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Matori Market Branch").flat(true);

			MarkerOptions marker1190 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.509280"), Double
									.parseDouble("3.374761")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Montgomery Branch").flat(true);

			MarkerOptions marker1191 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.581818"), Double
									.parseDouble("3.321135")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Nahco Branch").flat(true);

			MarkerOptions marker1192 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.457694"), Double
									.parseDouble("3.384947")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Nnamdi Azikwe Branch").flat(true);

			MarkerOptions marker1193 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.638978"), Double
									.parseDouble("3.360186")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Ogba Branch").flat(true);

			MarkerOptions marker1194 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.574911"), Double
									.parseDouble("3.396020")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Ogudu Branch").flat(true);

			MarkerOptions marker1195 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.639977"), Double
									.parseDouble("3.369289")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Oke- Arin Branch").flat(true);

			MarkerOptions marker1196 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.589770"), Double
									.parseDouble("3.361081")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Opebi Branch").flat(true);

			MarkerOptions marker1197 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.472548"), Double
									.parseDouble("3.596261")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Osapa London Branch").flat(true);

			MarkerOptions marker1198 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.545878"), Double
									.parseDouble("3.332688")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Osolo Way Branch").flat(true);

			MarkerOptions marker1199 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.674561"), Double
									.parseDouble("3.271174")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Sango Ota Branch").flat(true);

			MarkerOptions marker1200 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.530670"), Double
									.parseDouble("3.379079")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Shomolu Branch").flat(true);
			
			googleMap.addMarker(marker901);
			googleMap.addMarker(marker902);
			googleMap.addMarker(marker903);
			googleMap.addMarker(marker904);
			googleMap.addMarker(marker905);
			googleMap.addMarker(marker906);
			googleMap.addMarker(marker907);
			googleMap.addMarker(marker908);
			googleMap.addMarker(marker909);
			googleMap.addMarker(marker910);
			googleMap.addMarker(marker911);
			googleMap.addMarker(marker912);
			googleMap.addMarker(marker913);
			googleMap.addMarker(marker914);
			googleMap.addMarker(marker915);
			googleMap.addMarker(marker916);
			googleMap.addMarker(marker917);
			googleMap.addMarker(marker918);
			googleMap.addMarker(marker919);
			googleMap.addMarker(marker920);
			googleMap.addMarker(marker921);
			googleMap.addMarker(marker922);
			googleMap.addMarker(marker923);
			googleMap.addMarker(marker924);
			googleMap.addMarker(marker925);
			googleMap.addMarker(marker926);
			googleMap.addMarker(marker927);
			googleMap.addMarker(marker928);
			googleMap.addMarker(marker929);
			googleMap.addMarker(marker930);
			googleMap.addMarker(marker931);
			googleMap.addMarker(marker932);
			googleMap.addMarker(marker933);
			googleMap.addMarker(marker934);
			googleMap.addMarker(marker935);
			googleMap.addMarker(marker936);
			googleMap.addMarker(marker937);
			googleMap.addMarker(marker938);
			googleMap.addMarker(marker939);
			googleMap.addMarker(marker940);
			googleMap.addMarker(marker941);
			googleMap.addMarker(marker942);
			googleMap.addMarker(marker943);
			googleMap.addMarker(marker944);
			googleMap.addMarker(marker945);
			googleMap.addMarker(marker946);
			googleMap.addMarker(marker947);
			googleMap.addMarker(marker948);
			googleMap.addMarker(marker949);
			googleMap.addMarker(marker950);
			googleMap.addMarker(marker951);
			googleMap.addMarker(marker952);
			googleMap.addMarker(marker953);
			googleMap.addMarker(marker954);
			googleMap.addMarker(marker955);
			googleMap.addMarker(marker956);
			googleMap.addMarker(marker957);
			googleMap.addMarker(marker958);
			googleMap.addMarker(marker959);
			googleMap.addMarker(marker960);
			googleMap.addMarker(marker961);
			googleMap.addMarker(marker962);
			googleMap.addMarker(marker963);
			googleMap.addMarker(marker964);
			googleMap.addMarker(marker965);
			googleMap.addMarker(marker966);
			googleMap.addMarker(marker967);
			googleMap.addMarker(marker968);
			googleMap.addMarker(marker969);
			googleMap.addMarker(marker970);
			googleMap.addMarker(marker971);
			googleMap.addMarker(marker972);
			googleMap.addMarker(marker973);
			googleMap.addMarker(marker974);
			googleMap.addMarker(marker975);
			googleMap.addMarker(marker976);
			googleMap.addMarker(marker977);
			googleMap.addMarker(marker978);
			googleMap.addMarker(marker979);
			googleMap.addMarker(marker980);
			googleMap.addMarker(marker981);
			googleMap.addMarker(marker982);
			googleMap.addMarker(marker983);
			googleMap.addMarker(marker984);
			googleMap.addMarker(marker985);
			googleMap.addMarker(marker986);
			googleMap.addMarker(marker987);
			googleMap.addMarker(marker988);
			googleMap.addMarker(marker989);
			googleMap.addMarker(marker990);
			googleMap.addMarker(marker991);
			googleMap.addMarker(marker992);
			googleMap.addMarker(marker993);
			googleMap.addMarker(marker994);
			googleMap.addMarker(marker995);
			googleMap.addMarker(marker996);
			googleMap.addMarker(marker997);
			googleMap.addMarker(marker998);
			googleMap.addMarker(marker999);
			googleMap.addMarker(marker1000);
			googleMap.addMarker(marker1001);
			googleMap.addMarker(marker1002);
			googleMap.addMarker(marker1003);
			googleMap.addMarker(marker1004);
			googleMap.addMarker(marker1005);
			googleMap.addMarker(marker1006);
			googleMap.addMarker(marker1007);
			googleMap.addMarker(marker1008);
			googleMap.addMarker(marker1009);
			googleMap.addMarker(marker1010);
			googleMap.addMarker(marker1011);
			googleMap.addMarker(marker1012);
			googleMap.addMarker(marker1013);
			googleMap.addMarker(marker1014);
			googleMap.addMarker(marker1015);
			googleMap.addMarker(marker1016);
			googleMap.addMarker(marker1017);
			googleMap.addMarker(marker1018);
			googleMap.addMarker(marker1019);
			googleMap.addMarker(marker1020);
			googleMap.addMarker(marker1021);
			googleMap.addMarker(marker1022);
			googleMap.addMarker(marker1023);
			googleMap.addMarker(marker1024);
			googleMap.addMarker(marker1025);
			googleMap.addMarker(marker1026);
			googleMap.addMarker(marker1027);
			googleMap.addMarker(marker1028);
			googleMap.addMarker(marker1029);
			googleMap.addMarker(marker1030);
			googleMap.addMarker(marker1031);
			googleMap.addMarker(marker1032);
			googleMap.addMarker(marker1033);
			googleMap.addMarker(marker1034);
			googleMap.addMarker(marker1035);
			googleMap.addMarker(marker1036);
			googleMap.addMarker(marker1037);
			googleMap.addMarker(marker1038);
			googleMap.addMarker(marker1039);
			googleMap.addMarker(marker1040);
			googleMap.addMarker(marker1041);
			googleMap.addMarker(marker1042);
			googleMap.addMarker(marker1043);
			googleMap.addMarker(marker1044);
			googleMap.addMarker(marker1045);
			googleMap.addMarker(marker1046);
			googleMap.addMarker(marker1047);
			googleMap.addMarker(marker1048);
			googleMap.addMarker(marker1049);
			googleMap.addMarker(marker1050);
			googleMap.addMarker(marker1051);
			googleMap.addMarker(marker1052);
			googleMap.addMarker(marker1053);
			googleMap.addMarker(marker1054);
			googleMap.addMarker(marker1055);
			googleMap.addMarker(marker1056);
			googleMap.addMarker(marker1057);
			googleMap.addMarker(marker1058);
			googleMap.addMarker(marker1059);
			googleMap.addMarker(marker1060);
			googleMap.addMarker(marker1061);
			googleMap.addMarker(marker1062);
			googleMap.addMarker(marker1063);
			googleMap.addMarker(marker1064);
			googleMap.addMarker(marker1065);
			googleMap.addMarker(marker1066);
			googleMap.addMarker(marker1067);
			googleMap.addMarker(marker1068);
			googleMap.addMarker(marker1069);
			googleMap.addMarker(marker1070);
			googleMap.addMarker(marker1071);
			googleMap.addMarker(marker1072);
			googleMap.addMarker(marker1073);
			googleMap.addMarker(marker1074);
			googleMap.addMarker(marker1075);
			googleMap.addMarker(marker1076);
			googleMap.addMarker(marker1077);
			googleMap.addMarker(marker1078);
			googleMap.addMarker(marker1079);
			googleMap.addMarker(marker1080);
			googleMap.addMarker(marker1081);
			googleMap.addMarker(marker1082);
			googleMap.addMarker(marker1083);
			googleMap.addMarker(marker1084);
			googleMap.addMarker(marker1085);
			googleMap.addMarker(marker1086);
			googleMap.addMarker(marker1087);
			googleMap.addMarker(marker1088);
			googleMap.addMarker(marker1089);
			googleMap.addMarker(marker1090);
			googleMap.addMarker(marker1091);
			googleMap.addMarker(marker1092);
			googleMap.addMarker(marker1093);
			googleMap.addMarker(marker1094);
			googleMap.addMarker(marker1095);
			googleMap.addMarker(marker1096);
			googleMap.addMarker(marker1097);
			googleMap.addMarker(marker1098);
			googleMap.addMarker(marker1099);
			googleMap.addMarker(marker1100);
			googleMap.addMarker(marker1101);
			googleMap.addMarker(marker1102);
			googleMap.addMarker(marker1103);
			googleMap.addMarker(marker1104);
			googleMap.addMarker(marker1105);
			googleMap.addMarker(marker1106);
			googleMap.addMarker(marker1107);
			googleMap.addMarker(marker1108);
			googleMap.addMarker(marker1109);
			googleMap.addMarker(marker1110);
			googleMap.addMarker(marker1111);
			googleMap.addMarker(marker1112);
			googleMap.addMarker(marker1113);
			googleMap.addMarker(marker1114);
			googleMap.addMarker(marker1115);
			googleMap.addMarker(marker1116);
			googleMap.addMarker(marker1117);
			googleMap.addMarker(marker1118);
			googleMap.addMarker(marker1119);
			googleMap.addMarker(marker1120);
			googleMap.addMarker(marker1121);
			googleMap.addMarker(marker1122);
			googleMap.addMarker(marker1123);
			googleMap.addMarker(marker1124);
			googleMap.addMarker(marker1125);
			googleMap.addMarker(marker1126);
			googleMap.addMarker(marker1127);
			googleMap.addMarker(marker1128);
			googleMap.addMarker(marker1129);
			googleMap.addMarker(marker1130);
			googleMap.addMarker(marker1131);
			googleMap.addMarker(marker1132);
			googleMap.addMarker(marker1133);
			googleMap.addMarker(marker1134);
			googleMap.addMarker(marker1135);
			googleMap.addMarker(marker1136);
			googleMap.addMarker(marker1137);
			googleMap.addMarker(marker1138);
			googleMap.addMarker(marker1139);
			googleMap.addMarker(marker1140);
			googleMap.addMarker(marker1141);
			googleMap.addMarker(marker1142);
			googleMap.addMarker(marker1143);
			googleMap.addMarker(marker1144);
			googleMap.addMarker(marker1145);
			googleMap.addMarker(marker1146);
			googleMap.addMarker(marker1147);
			googleMap.addMarker(marker1148);
			googleMap.addMarker(marker1149);
			googleMap.addMarker(marker1150);
			googleMap.addMarker(marker1151);
			googleMap.addMarker(marker1152);
			googleMap.addMarker(marker1152b);
			googleMap.addMarker(marker1152c);
			googleMap.addMarker(marker1152d);
			googleMap.addMarker(marker1152e);
			googleMap.addMarker(marker1152f);
			googleMap.addMarker(marker1153);
			googleMap.addMarker(marker1154);
			googleMap.addMarker(marker1155);
			googleMap.addMarker(marker1156);
			googleMap.addMarker(marker1157);
			googleMap.addMarker(marker1158);
			googleMap.addMarker(marker1159);
			googleMap.addMarker(marker1160);
			googleMap.addMarker(marker1161);
			googleMap.addMarker(marker1162);
			googleMap.addMarker(marker1163);
			googleMap.addMarker(marker1164);
			googleMap.addMarker(marker1165);
			googleMap.addMarker(marker1166);
			googleMap.addMarker(marker1167);
			googleMap.addMarker(marker1168);
			googleMap.addMarker(marker1169);
			googleMap.addMarker(marker1170);
			googleMap.addMarker(marker1171);
			googleMap.addMarker(marker1172);
			googleMap.addMarker(marker1173);
			googleMap.addMarker(marker1174);
			googleMap.addMarker(marker1175);
			googleMap.addMarker(marker1176);
			googleMap.addMarker(marker1177);
			googleMap.addMarker(marker1178);
			googleMap.addMarker(marker1179);
			googleMap.addMarker(marker1180);
			googleMap.addMarker(marker1181);
			googleMap.addMarker(marker1182);
			googleMap.addMarker(marker1183);
			googleMap.addMarker(marker1184);
			googleMap.addMarker(marker1185);
			googleMap.addMarker(marker1186);
			googleMap.addMarker(marker1187);
			googleMap.addMarker(marker1188);
			googleMap.addMarker(marker1189);
			googleMap.addMarker(marker1190);
			googleMap.addMarker(marker1191);
			googleMap.addMarker(marker1192);
			googleMap.addMarker(marker1193);
			googleMap.addMarker(marker1194);
			googleMap.addMarker(marker1195);
			googleMap.addMarker(marker1196);
			googleMap.addMarker(marker1197);
			googleMap.addMarker(marker1198);
			googleMap.addMarker(marker1199);
			googleMap.addMarker(marker1200);
		}

		private void markersFive() {
			MarkerOptions marker1201 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.596321"), Double
									.parseDouble("3.347949")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Toyin Street Branch").flat(true);

			MarkerOptions marker1202 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455280"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Warehouse Road Branch").flat(true);

			MarkerOptions marker1203 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442277"), Double
									.parseDouble("3.376492")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.skyebank))

					.title("Wharf Road 2 Branch").flat(true);

			MarkerOptions marker1204 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.492975"), Double
									.parseDouble("3.356992")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Adeniran Ogunsanya Branch").flat(true);

			MarkerOptions marker1205 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.434641"), Double
									.parseDouble("3.429385")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Adeola Hopewell Branch").flat(true);

			MarkerOptions marker1206 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4613"), Double
									.parseDouble("3.45867")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Adeola Hopewell 2 Branch").flat(true);

			MarkerOptions marker1207 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430022"), Double
									.parseDouble("3.419289")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Adeola Odeku - The Place Branch").flat(true);

			MarkerOptions marker1208 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.430306"), Double
									.parseDouble("3.415599")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Adeola Odeku Branch").flat(true);

			MarkerOptions marker1209 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.43111"), Double
									.parseDouble("3.425168")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Adetokunbo Ademola Branch").flat(true);

			MarkerOptions marker1210 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.434356"), Double
									.parseDouble("3.48013")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Admiralty Branch").flat(true);

			MarkerOptions marker1211 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.524379"), Double
									.parseDouble("3.379206")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Airport Road Branch").flat(true);

			MarkerOptions marker1212 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.47294"), Double
									.parseDouble("3.597668")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Ajah Branch").flat(true);

			MarkerOptions marker1213 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431472"), Double
									.parseDouble("3.429642")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Ajose Adeogun Branch").flat(true);

			MarkerOptions marker1214 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600693"), Double
									.parseDouble("3.300757")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Akowonjo Branch").flat(true);

			MarkerOptions marker1215 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.652838"), Double
									.parseDouble("3.299147")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Alagbado Branch").flat(true);

			MarkerOptions marker1216 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.605850"), Double
									.parseDouble("3.349831")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Allen Branch").flat(true);

			MarkerOptions marker1217 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601615"), Double
									.parseDouble("3.351937")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Allen 2 Branch").flat(true);

			MarkerOptions marker1218 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440795"), Double
									.parseDouble("3.378266")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Apapa Creek Road Branch").flat(true);

			MarkerOptions marker1219 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.45528"), Double
									.parseDouble("3.364084")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Apapa Warehouse Branch").flat(true);

			MarkerOptions marker1220 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.435232"), Double
									.parseDouble("3.366052")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Apapa Branch").flat(true);

			MarkerOptions marker1221 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.459993"), Double
									.parseDouble("3.230824")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Aspamda Branch").flat(true);

			MarkerOptions marker1222 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444132"), Double
									.parseDouble("3.423186")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Awolowo Road Branch").flat(true);

			MarkerOptions marker1223 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.44111"), Double
									.parseDouble("2.919306")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Bba Tradefair Branch").flat(true);

			MarkerOptions marker1224 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.453434"), Double
									.parseDouble("3.389502")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Broad Street Branch").flat(true);

			MarkerOptions marker1225 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.440372"), Double
									.parseDouble("3.372087")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Commercial Road Branch").flat(true);

			MarkerOptions marker1226 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.595173"), Double
									.parseDouble("3.340223")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Computer Village Mini Branch").flat(true);

			MarkerOptions marker1227 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.517791"), Double
									.parseDouble("3.382686")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Cpc Branch").flat(true);

			MarkerOptions marker1228 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.455374"), Double
									.parseDouble("3.384912")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Davies Branch").flat(true);

			MarkerOptions marker1229 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.47596"), Double
									.parseDouble("3.279926")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Festac Branch").flat(true);

			MarkerOptions marker1230 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.584249"), Double
									.parseDouble("3.357104")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Gra Ikeja Branch").flat(true);

			MarkerOptions marker1231 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.599415"), Double
									.parseDouble("3.282268")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Iddo Branch").flat(true);

			MarkerOptions marker1232 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.564985"), Double
									.parseDouble("3.280998")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Idimu Branch").flat(true);

			MarkerOptions marker1233 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.43295"), Double
									.parseDouble("3.42567")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Idowu Taylor Branch").flat(true);

			MarkerOptions marker1234 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458846"), Double
									.parseDouble("3.38971")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Idumagbo Branch").flat(true);

			MarkerOptions marker1235 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.461456"), Double
									.parseDouble("3.390013")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Idumota Branch").flat(true);

			MarkerOptions marker1236 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601053"), Double
									.parseDouble("3.338636")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Ikeja Branch").flat(true);

			MarkerOptions marker1237 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.57406"), Double
									.parseDouble("3.368788")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Ikorodu Branch").flat(true);

			MarkerOptions marker1238 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442004"), Double
									.parseDouble("3.418622")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Ikoyi Branch").flat(true);

			MarkerOptions marker1239 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.550982"), Double
									.parseDouble("3.355711")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Ilupeju Branch").flat(true);

			MarkerOptions marker1240 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.450225"), Double
									.parseDouble("3.39483")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Joseph Street Branch").flat(true);

			MarkerOptions marker1241 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.604336"), Double
									.parseDouble("3.390854")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Ketu Branch").flat(true);

			MarkerOptions marker1242 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.539335"), Double
									.parseDouble("3.344935")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Ladipo Branch").flat(true);

			MarkerOptions marker1243 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.472548"), Double
									.parseDouble("3.596261")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Lekki Chevron Branch").flat(true);

			MarkerOptions marker1244 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.458985"), Double
									.parseDouble("3.601521")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Lekki Branch").flat(true);

			MarkerOptions marker1245 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431224"), Double
									.parseDouble("3.469051")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Lekki 2 Branch").flat(true);
			MarkerOptions marker1246 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.589773"), Double
									.parseDouble("3.331411")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Local Airport Branch").flat(true);

			MarkerOptions marker1247 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.446512"), Double
									.parseDouble("3.405086")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Macarthy Branch").flat(true);

			MarkerOptions marker1248 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.448183"), Double
									.parseDouble("3.394522")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Marina Branch").flat(true);

			MarkerOptions marker1249 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.537044"), Double
									.parseDouble("3.346589")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Matori Branch").flat(true);

			MarkerOptions marker1250 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601838"), Double
									.parseDouble("3.351486")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Mikano Office Branch").flat(true);

			MarkerOptions marker1251 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.573279"), Double
									.parseDouble("3.363191")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Mobolaji Bank Anthony Way Branch").flat(true);

			MarkerOptions marker1252 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601838"), Double
									.parseDouble("3.351486")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Motorways Branch").flat(true);

			MarkerOptions marker1253 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.627364"), Double
									.parseDouble("3.322609")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Mushin Branch").flat(true);

			MarkerOptions marker1254 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.601409"), Double
									.parseDouble("3.338441")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Oba Akran Branch").flat(true);

			MarkerOptions marker1255 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.628888"), Double
									.parseDouble("3.337987")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Ogba Branch").flat(true);

			MarkerOptions marker1256 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.574911"), Double
									.parseDouble("3.391811")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Ogudu Branch").flat(true);

			MarkerOptions marker1257 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.465455"), Double
									.parseDouble("3.332868")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Oke Afa Branch").flat(true);

			MarkerOptions marker1258 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.514071"), Double
									.parseDouble("3.321081")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Okota Branch").flat(true);

			MarkerOptions marker1259 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.502206"), Double
									.parseDouble("3.305082")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Okota 2 Branch").flat(true);

			MarkerOptions marker1260 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.536498"), Double
									.parseDouble("3.367796")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Onipanu Branch").flat(true);

			MarkerOptions marker1261 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.600111"), Double
									.parseDouble("3.364972")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Oregun Branch").flat(true);

			MarkerOptions marker1262 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.636360"), Double
									.parseDouble("3.321271")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Orile Coker Branch").flat(true);

			MarkerOptions marker1263 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.460553"), Double
									.parseDouble("3.388744")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Oroyinyin Branch").flat(true);

			MarkerOptions marker1264 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.545843"), Double
									.parseDouble("3.333073")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Osolo Way Branch").flat(true);

			MarkerOptions marker1265 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.429449"), Double
									.parseDouble("3.428866")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Oyin Jolayemi Branch").flat(true);

			MarkerOptions marker1266 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.472548"), Double
									.parseDouble("3.596261")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Palms Branch").flat(true);

			MarkerOptions marker1267 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.444837"), Double
									.parseDouble("3.369387")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Randle Road Branch").flat(true);

			MarkerOptions marker1268 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.431489"), Double
									.parseDouble("3.425037")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Sanusi Fafunwa Branch").flat(true);

			MarkerOptions marker1269 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.540836"), Double
									.parseDouble("3.368308")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Shomolu Branch").flat(true);

			MarkerOptions marker1270 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.503878"), Double
									.parseDouble("3.351437")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Surulere Branch").flat(true);

			MarkerOptions marker1271 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.595774"), Double
									.parseDouble("3.352128")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Toyin Street Branch").flat(true);

			MarkerOptions marker1272 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.442277"), Double
									.parseDouble("3.376492")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Wharf Road Branch").flat(true);

			MarkerOptions marker1273 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.508648"), Double
									.parseDouble("3.368051")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.fcmb))

					.title("Yaba Branch").flat(true);

			MarkerOptions marker1274 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4624902"), Double
									.parseDouble("3.3843349")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Idumota Branch").flat(true);

			MarkerOptions marker1275 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4595453"), Double
									.parseDouble("3.1851613")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Ojo Egbeda Branch").flat(true);

			MarkerOptions marker1276 = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble("6.4487406"), Double
									.parseDouble("3.3649384")))
					.icon(BitmapDescriptorFactory

					.fromResource(R.drawable.uba))

					.title("Prestige Branch").flat(true);
			
			googleMap.addMarker(marker1201);
			googleMap.addMarker(marker1202);
			googleMap.addMarker(marker1203);
			googleMap.addMarker(marker1204);
			googleMap.addMarker(marker1205);
			googleMap.addMarker(marker1206);
			googleMap.addMarker(marker1207);
			googleMap.addMarker(marker1208);
			googleMap.addMarker(marker1209);
			googleMap.addMarker(marker1210);
			googleMap.addMarker(marker1211);
			googleMap.addMarker(marker1212);
			googleMap.addMarker(marker1213);
			googleMap.addMarker(marker1214);
			googleMap.addMarker(marker1215);
			googleMap.addMarker(marker1216);
			googleMap.addMarker(marker1217);
			googleMap.addMarker(marker1218);
			googleMap.addMarker(marker1219);
			googleMap.addMarker(marker1220);
			googleMap.addMarker(marker1221);
			googleMap.addMarker(marker1222);
			googleMap.addMarker(marker1223);
			googleMap.addMarker(marker1224);
			googleMap.addMarker(marker1225);
			googleMap.addMarker(marker1226);
			googleMap.addMarker(marker1227);
			googleMap.addMarker(marker1228);
			googleMap.addMarker(marker1229);
			googleMap.addMarker(marker1230);
			googleMap.addMarker(marker1231);
			googleMap.addMarker(marker1232);
			googleMap.addMarker(marker1233);
			googleMap.addMarker(marker1234);
			googleMap.addMarker(marker1235);
			googleMap.addMarker(marker1236);
			googleMap.addMarker(marker1237);
			googleMap.addMarker(marker1238);
			googleMap.addMarker(marker1239);
			googleMap.addMarker(marker1240);
			googleMap.addMarker(marker1241);
			googleMap.addMarker(marker1242);
			googleMap.addMarker(marker1243);
			googleMap.addMarker(marker1244);
			googleMap.addMarker(marker1245);
			googleMap.addMarker(marker1246);
			googleMap.addMarker(marker1247);
			googleMap.addMarker(marker1248);
			googleMap.addMarker(marker1249);
			googleMap.addMarker(marker1250);
			googleMap.addMarker(marker1251);
			googleMap.addMarker(marker1252);
			googleMap.addMarker(marker1253);
			googleMap.addMarker(marker1254);
			googleMap.addMarker(marker1255);
			googleMap.addMarker(marker1256);
			googleMap.addMarker(marker1257);
			googleMap.addMarker(marker1258);
			googleMap.addMarker(marker1259);
			googleMap.addMarker(marker1260);
			googleMap.addMarker(marker1261);
			googleMap.addMarker(marker1262);
			googleMap.addMarker(marker1263);
			googleMap.addMarker(marker1264);
			googleMap.addMarker(marker1265);
			googleMap.addMarker(marker1266);
			googleMap.addMarker(marker1267);
			googleMap.addMarker(marker1268);
			googleMap.addMarker(marker1269);
			googleMap.addMarker(marker1270);
			googleMap.addMarker(marker1271);
			googleMap.addMarker(marker1272);
			googleMap.addMarker(marker1273);
			googleMap.addMarker(marker1274);
			googleMap.addMarker(marker1275);
			googleMap.addMarker(marker1276);
		}

	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public class BanksFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.banks_near_you,
					container, false);

			setUpMapIfNeeded();

			return rootView;
		}

		@Override
		public void onResume() {
			super.onResume();
			setUpMapIfNeeded();
		}

		/**
		 * Sets up the map if it is possible to do so (i.e., the Google Play
		 * services APK is correctly installed) and the map has not already been
		 * instantiated.. This will ensure that we only ever call
		 * {@link #setUpMap()} once when {@link #mMap} is not null.
		 * <p>
		 * If it isn't installed {@link SupportMapFragment} (and
		 * {@link com.google.android.gms.maps.MapView MapView}) will show a
		 * prompt for the user to install/update the Google Play services APK on
		 * their device.
		 * <p>
		 * A user can return to this FragmentActivity after following the prompt
		 * and correctly installing/updating/enabling the Google Play services.
		 * Since the FragmentActivity may not have been completely destroyed
		 * during this process (it is likely that it would only be stopped or
		 * paused), {@link #onCreate(Bundle)} may not be called again so we
		 * should call this method in {@link #onResume()} to guarantee that it
		 * will be called.
		 */
		private void setUpMapIfNeeded() {
			// Do a null check to confirm that we have not already instantiated
			// the map.
			if (googleMap == null) {
				// Try to obtain the map from the SupportMapFragment.
				googleMap = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.atmMapView)).getMap();
				// Check if we were successful in obtaining the map.
				if (googleMap != null) {
					setUpMap();
				}
			}
		}

		/**
		 * This is where we can add markers or lines, add listeners or move the
		 * camera. In this case, we just add a marker near Africa.
		 * <p>
		 * This should only be called once and when we are sure that
		 * {@link #mMap} is not null.
		 */
		private void setUpMap() {
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			String provider = locationManager.getBestProvider(criteria, true);
			LocationListener locationListener = new LocationListener() {
				@Override
				public void onLocationChanged(Location location) {
					showCurrentLocation(location);
				}

				@Override
				public void onStatusChanged(String s, int i, Bundle bundle) {
				}

				@Override
				public void onProviderEnabled(String s) {
				}

				@Override
				public void onProviderDisabled(String s) {
				}
			};
			locationManager.requestLocationUpdates(provider, 2000, 0,
					locationListener);
			// Getting initial Location
			Location location = locationManager.getLastKnownLocation(provider);
			// Show the initial location
			if (location != null) {
				showCurrentLocation(location);
			}
		}

		private void showCurrentLocation(Location location) {
			googleMap.clear();
			LatLng currentPosition = new LatLng(location.getLatitude(),
					location.getLongitude());
			// Zoom in, animating the camera.
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					currentPosition, 15));

			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);

			// Enable / Disable my location button
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			//
			googleMap.getUiSettings().setZoomControlsEnabled(true);

			/*
			 * mMap.addMarker(new MarkerOptions() .position(currentPosition)
			 * .snippet("Lat: " + location.getLatitude() + ", Lng: " +
			 * location.getLongitude())
			 * .icon(BitmapDescriptorFactory.fromResource
			 * (R.drawable.traffic_jams)) .flat(true); .title("I'm here!")); //
			 * Zoom in, animating the camera.
			 * mMap.animateCamera(CameraUpdateFactory
			 * .newLatLngZoom(currentPosition, 18));
			 */
			
			bankMarkerOne();
			bankMarkerTwo();
			bankMarkerThree();
			bankMarkerFour();
			bankMarkerFive();
			// Adding marker on the Google Map

			
			
			
			
			

		}
		
		private void bankMarkerOne(){
			MarkerOptions marker1 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458900"), Double
							.parseDouble("3.298813")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Old Ojo Road Branch, Lagos").flat(true);

	MarkerOptions marker2 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.555901"), Double
							.parseDouble("3.336582")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Oshodi Branch").flat(true);

	MarkerOptions marker3 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.484525"), Double
							.parseDouble("3.567123")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Lagos Business School").flat(true);

	MarkerOptions marker4 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.641419"), Double
							.parseDouble("3.338814")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Ait Mini Branch").flat(true);

	MarkerOptions marker5 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.44393"), Double
							.parseDouble("3.501205")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Victoria Garden City").flat(true);

	MarkerOptions marker6 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.630762"), Double
							.parseDouble("3.338041")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Ogba Branch").flat(true);

	MarkerOptions marker7 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.484199"), Double
							.parseDouble("3.386377")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Oyingbo Branch").flat(true);

	MarkerOptions marker8 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.470283"), Double
							.parseDouble("3.351088")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Ijora Badia Branch").flat(true);

	MarkerOptions marker9 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459475"), Double
							.parseDouble("3.417244")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Moloney Branch").flat(true);

	MarkerOptions marker10 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.537220"), Double
							.parseDouble("3.346541")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Matori Branch").flat(true);

	MarkerOptions marker11 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.598108"), Double
							.parseDouble("3.380356")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Ojota Branch").flat(true);

	MarkerOptions marker12 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.515115"), Double
							.parseDouble("3.381262")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Yaba Branch").flat(true);

	MarkerOptions marker13 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.457861"), Double
							.parseDouble("3.384827")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Balogun Branch").flat(true);

	MarkerOptions marker14 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.468151"), Double
							.parseDouble("3.390999")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Idumagbo Branch").flat(true);

	MarkerOptions marker15 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.472053"), Double
							.parseDouble("3.324763")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Ibafon Branch").flat(true);

	MarkerOptions marker16 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.628098"), Double
							.parseDouble("3.331701")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Agege Branch").flat(true);

	MarkerOptions marker17 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.557692"), Double
							.parseDouble("3.393745")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Okearin Branch").flat(true);

	MarkerOptions marker18 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460379"), Double
							.parseDouble("3.191024")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Ojo Alaba Branch").flat(true);

	MarkerOptions marker19 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.451046"), Double
							.parseDouble("3.449118")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Alexander (Ensec) Branch").flat(true);

	MarkerOptions marker20 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.436264"), Double
							.parseDouble("3.410193")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Corporate Branch").flat(true);

	MarkerOptions marker21 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.452458"), Double
							.parseDouble("3.415375")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Awolowo Road Branch, Ikoyi").flat(true);

	MarkerOptions marker22 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455145"), Double
							.parseDouble("3.350143")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Boundary Market Ajegunle Branch").flat(true);

	MarkerOptions marker23 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.473836"), Double
							.parseDouble("3.294969")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Festac Branch").flat(true);

	MarkerOptions marker25 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.445635"), Double
							.parseDouble("3.430481")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Adetokunbo Ademola Branch").flat(true);

	MarkerOptions marker26 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.551392"), Double
							.parseDouble("3.392318")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Gbagada Branch").flat(true);

	MarkerOptions marker27 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.552713"), Double
							.parseDouble("3.362246")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Ilupeju Branch").flat(true);

	MarkerOptions marker28 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.433023"), Double
							.parseDouble("3.421028")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Napex Branch").flat(true);

	MarkerOptions marker29 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.604481"), Double
							.parseDouble("3.350959")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Allen Avenue, Ikeja Branch").flat(true);

	MarkerOptions marker30 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.557915"), Double
							.parseDouble("3.366687")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Ikorodu Town Branch").flat(true);

	MarkerOptions marker31 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.536203"), Double
							.parseDouble("3.349457")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Ladipo Branch").flat(true);

	MarkerOptions marker32 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.476608"), Double
							.parseDouble("3.603745")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Lekki Branch").flat(true);

	MarkerOptions marker33 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.599472"), Double
							.parseDouble("3.365604")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Oregun Branch").flat(true);

	MarkerOptions marker34 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.439072"), Double
							.parseDouble("3.371487")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Burma Roda, Apapa Branch").flat(true);

	MarkerOptions marker35 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.476072"), Double
							.parseDouble("3.204819")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Bba Branch").flat(true);

	MarkerOptions marker38 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4412"), Double
							.parseDouble("3.416748")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Adeola Odeku Branch").flat(true);

	MarkerOptions marker39 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.468536"), Double
							.parseDouble("3.243668")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Apt, Trade Fair Branch").flat(true);

	MarkerOptions marker40 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.506268"), Double
							.parseDouble("3.366235")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Tejuosho Branch").flat(true);

	MarkerOptions marker41 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.469516"), Double
							.parseDouble("3.351517")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Obasanjo Main, Aspamda Branch").flat(true);

	MarkerOptions marker42 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.47395"), Double
							.parseDouble("3.56781")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Ikota Mini Branch").flat(true);

	MarkerOptions marker43 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.531264"), Double
							.parseDouble("3.344083")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Ire Akari Branch").flat(true);

	MarkerOptions marker44 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.490303"), Double
							.parseDouble("3.354757")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Bode Thomas Branch, Lagos").flat(true);

	MarkerOptions marker45 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.595229"), Double
							.parseDouble("3.339947")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Computer Village Micro Branch").flat(true);

	MarkerOptions marker46 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431723"), Double
							.parseDouble("3.424366")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Adeyemo Alakija Branch").flat(true);

	MarkerOptions marker47 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.434814"), Double
							.parseDouble("3.429301")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Adeola Hopewell Branch").flat(true);

	MarkerOptions marker48 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.525661"), Double
							.parseDouble("3.367449")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Fadeyi Branch").flat(true);

	MarkerOptions marker49 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.448301"), Double
							.parseDouble("3.326497")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Trinity Branch").flat(true);

	MarkerOptions marker50 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.553599"), Double
							.parseDouble("3.334351")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Airport Road Lagos Branch").flat(true);

	MarkerOptions marker51 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.479876"), Double
							.parseDouble("3.385742")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Ebute Metta Branch").flat(true);

	MarkerOptions marker52 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.445977"), Double
							.parseDouble("3.364563")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Warehouse Road Apapa, Branch").flat(true);

	MarkerOptions marker53 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444004"), Double
							.parseDouble("3.369849")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Commercial Road, Apapa Branch").flat(true);

	MarkerOptions marker54 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.502323"), Double
							.parseDouble("3.379717")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Financial Trust House (Balogun)").flat(true);

	MarkerOptions marker55 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.536175"), Double
							.parseDouble("3.352859")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Mushin Branch").flat(true);

	MarkerOptions marker56 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.617444"), Double
							.parseDouble("3.312793")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Dopemu Branch").flat(true);

	MarkerOptions marker57 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.521085"), Double
							.parseDouble("3.385385")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Akoka Branch").flat(true);

	MarkerOptions marker57a = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.433402"), Double
							.parseDouble("3.356050")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Tin Can Island Branch").flat(true);

	MarkerOptions marker58 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.461669"), Double
							.parseDouble("3.204918")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Alaba 2 Branch").flat(true);

	MarkerOptions marker58a = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.581855"), Double
							.parseDouble("3.321280")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Nahco Branch").flat(true);

	MarkerOptions marker59 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.462925"), Double
							.parseDouble("3.389182")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Enu Owa Branch").flat(true);

	MarkerOptions marker60 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.584252"), Double
							.parseDouble("3.983359")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Ibeju Lekki Branch").flat(true);

	MarkerOptions marker61 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.613370"), Double
							.parseDouble("3.336086")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fidelity))

			.title("Oba Akran Ikeja Branch").flat(true);

	MarkerOptions marker62 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.636999"), Double
							.parseDouble("3.315727")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Abattoir Cash Centre").flat(true);

	MarkerOptions marker63 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430998"), Double
							.parseDouble("3.434132")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Abibu Adetoro Branch").flat(true);

	MarkerOptions marker64 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444641"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Abibu-Oki Branch").flat(true);

	MarkerOptions marker65 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.620765"), Double
							.parseDouble("3.303777")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Abule Egba Branch").flat(true);

	MarkerOptions marker66 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.492994"), Double
							.parseDouble("3.381728")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Adekunle Branch").flat(true);

	MarkerOptions marker67 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430603"), Double
							.parseDouble("3.413519")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Adeola Odeku Branch").flat(true);

	MarkerOptions marker68 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431348"), Double
							.parseDouble("3.425027")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Adetokunbo Ademola Branch").flat(true);

	MarkerOptions marker69 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.636999"), Double
							.parseDouble("3.315727")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Agege Branch").flat(true);

	MarkerOptions marker70 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.631163"), Double
							.parseDouble("3.32147")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Agege Cash Centre").flat(true);

	MarkerOptions marker71 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.620951"), Double
							.parseDouble("3.353336")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Agidingbi Branch").flat(true);

	MarkerOptions marker72 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431224"), Double
							.parseDouble("3.469051")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ajah Branch").flat(true);

	MarkerOptions marker73 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.438318"), Double
							.parseDouble("3.526015")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ajah Cash Centre").flat(true);

	MarkerOptions marker74 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.577359"), Double
							.parseDouble("3.333956")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ajao Estate Branch").flat(true);

	MarkerOptions marker75 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600693"), Double
							.parseDouble("3.300757")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Akowonjo Branch").flat(true);

	MarkerOptions marker76 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460204"), Double
							.parseDouble("3.165468")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Alaba Int’l Market Branch").flat(true);

	MarkerOptions marker77 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.524406"), Double
							.parseDouble("3.379206")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Alaba Int’l Market Cash Centre").flat(true);

	MarkerOptions marker78 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.441110"), Double
							.parseDouble("2.919306")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Alaba Rago Mkt. Cash Centre").flat(true);

	MarkerOptions marker79 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460072"), Double
							.parseDouble("3.326754")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Alaba Suru Branch").flat(true);

	MarkerOptions marker80 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.621108"), Double
							.parseDouble("3.360934")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Alausa Branch").flat(true);

	MarkerOptions marker81 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.446680"), Double
							.parseDouble("3.240165")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Aliko Cement Terminal Cash Centre").flat(true);

	MarkerOptions marker82 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.438087"), Double
							.parseDouble("3.368215")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Apapa Branch").flat(true);

	MarkerOptions marker83 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.488372"), Double
							.parseDouble("3.182987")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Article Market Cash Centre").flat(true);

	MarkerOptions marker84 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.441977"), Double
							.parseDouble("3.418408")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Awolowo Road Branch").flat(true);

	MarkerOptions marker85 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.422020"), Double
							.parseDouble("2.888884")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Badagry Branch").flat(true);

	MarkerOptions marker86 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.472516"), Double
							.parseDouble("3.568616")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Badore Branch").flat(true);

	MarkerOptions marker87 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.551475"), Double
							.parseDouble("3.392124")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Bariga Branch").flat(true);

	MarkerOptions marker88 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.457806"), Double
							.parseDouble("3.380847")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Broad Street Branch").flat(true);

	MarkerOptions marker89 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.441456"), Double
							.parseDouble("3.530527")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Chevron-Texaco Branch").flat(true);

	MarkerOptions marker90 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.450026"), Double
							.parseDouble("3.266655")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Coker Branch").flat(true);

	MarkerOptions marker91 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.43621"), Double
							.parseDouble("3.364495")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Creek Road Branch").flat(true);

	MarkerOptions marker92 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.503297"), Double
							.parseDouble("3.355314")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Daleko Market Branch").flat(true);

	MarkerOptions marker93 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.506338"), Double
							.parseDouble("3.374293")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Domino Cash Centre").flat(true);

	MarkerOptions marker94 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.607476"), Double
							.parseDouble("3.310022")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Dopemu Branch").flat(true);

	MarkerOptions marker95 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.485028"), Double
							.parseDouble("3.379920")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ebute Metta Branch").flat(true);

	MarkerOptions marker96 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431116"), Double
							.parseDouble("3.415823")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Eko Hotel Branch").flat(true);

	MarkerOptions marker97 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.462684"), Double
							.parseDouble("3.389359")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Enu-Owa Cash Centre").flat(true);

	MarkerOptions marker98 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442047"), Double
							.parseDouble("3.417631")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Falomo Shopping Centre Branch").flat(true);

	MarkerOptions marker99 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.454811"), Double
							.parseDouble("3.434691")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Federal Secretariat Branch").flat(true);

	MarkerOptions marker100 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.468089"), Double
							.parseDouble("3.285545")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Festac Branch").flat(true);

	MarkerOptions marker101 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.587110"), Double
							.parseDouble("3.286114")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Idimu Branch").flat(true);

	MarkerOptions marker102 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.552584"), Double
							.parseDouble("3.391345")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ifako Gbagada Branch").flat(true);

	MarkerOptions marker103 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.481378"), Double
							.parseDouble("3.361504")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Iganmu Branch").flat(true);

	MarkerOptions marker104 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.450006"), Double
							.parseDouble("3.366666")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ijora Branch").flat(true);

	MarkerOptions marker105 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.627902"), Double
							.parseDouble("3.331692")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Iju Branch").flat(true);

	MarkerOptions marker106 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.604873"), Double
							.parseDouble("3.350245")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ikeja Allen Avenue Branch").flat(true);

	MarkerOptions marker107 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.612313"), Double
							.parseDouble("3.335949")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ikeja Ind. Estate Branch").flat(true);

	MarkerOptions marker108 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601839"), Double
							.parseDouble("3.351489")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ikeja Military Cantonment Agency").flat(true);

	MarkerOptions marker109 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.624305"), Double
							.parseDouble("3.494602")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ikorodu Branch").flat(true);

	MarkerOptions marker110 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431225"), Double
							.parseDouble("3.469051")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ikota Main Branch").flat(true);

	MarkerOptions marker111 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.550559"), Double
							.parseDouble("3.268389")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ikotun Branch").flat(true);

	MarkerOptions marker112 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.564523"), Double
							.parseDouble("3.356520")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ilupeju Branch").flat(true);

	MarkerOptions marker113 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460250"), Double
							.parseDouble("3.307955")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Int’l Trade Fair Complex II (Balogun) Cash Centre")
			.flat(true);

	MarkerOptions marker114 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.456794"), Double
							.parseDouble("3.269090")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("International Trade Fair Complex Branch")
			.flat(true);

	MarkerOptions marker116 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453695"), Double
							.parseDouble("3.389512")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Investment House Branch").flat(true);

	MarkerOptions marker117 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453030"), Double
							.parseDouble("3.369574")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("IPMAN Cash Centre").flat(true);

	MarkerOptions marker118 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.530441"), Double
							.parseDouble("3.343977")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Isolo Branch").flat(true);

	MarkerOptions marker119 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.450004"), Double
							.parseDouble("3.366666")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Isolo Industrial Estate Branch").flat(true);

	MarkerOptions marker120 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600027"), Double
							.parseDouble("3.500000")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ita Elewa Ikorodu Branch").flat(true);

	MarkerOptions marker121 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.609649"), Double
							.parseDouble("3.307819")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Iyana Ipaja Branch").flat(true);

	MarkerOptions marker122 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.517791"), Double
							.parseDouble("3.382686")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Jibowu Branch").flat(true);

	MarkerOptions marker123 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.556490"), Double
							.parseDouble("3.347875")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Kairo Market Cash Centre").flat(true);

	MarkerOptions marker124 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.447461"), Double
							.parseDouble("3.415040")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Keffi Branch").flat(true);

	MarkerOptions marker125 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.604248"), Double
							.parseDouble("3.390883")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ketu Branch").flat(true);

	MarkerOptions marker126 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.439697"), Double
							.parseDouble("3.369825")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Kofo Abayomi Branch").flat(true);

	MarkerOptions marker127 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431580"), Double
							.parseDouble("2.887644")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Lagos State University Cash Centre").flat(true);

	MarkerOptions marker128 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.451397"), Double
							.parseDouble("3.396398")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Lapal House Branch").flat(true);

	MarkerOptions marker129 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.511148"), Double
							.parseDouble("3.339403")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Lawanson Branch").flat(true);

	MarkerOptions marker130 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.435235"), Double
							.parseDouble("3.458470")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Lekki Branch").flat(true);

	MarkerOptions marker131 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.483471"), Double
							.parseDouble("3.382513")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("IMurtala Moh’d Way Branch").flat(true);

	MarkerOptions marker132 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.445155"), Double
							.parseDouble("3.351070")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Marina Branch").flat(true);

	MarkerOptions marker133 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.541722"), Double
							.parseDouble("3.343349")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Matori Branch").flat(true);

	MarkerOptions marker134 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431224"), Double
							.parseDouble("3.469051")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Mayfair Gardens Branch").flat(true);

	MarkerOptions marker135 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458848"), Double
							.parseDouble("3.302113")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Mazamaza Branch").flat(true);

	MarkerOptions marker136 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455098"), Double
							.parseDouble("3.356747")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Mobil Road Branch").flat(true);

	MarkerOptions marker137 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.447521"), Double
							.parseDouble("3.406022")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Moloney Branch").flat(true);

	MarkerOptions marker138 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.527188"), Double
							.parseDouble("3.354507")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Mushin Branch").flat(true);

	MarkerOptions marker139 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431127"), Double
							.parseDouble("3.424576")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("N.1.J. House Branch").flat(true);

	MarkerOptions marker140 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.467318"), Double
							.parseDouble("3.284681")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Navy Town Branch").flat(true);

	MarkerOptions marker141 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.451895"), Double
							.parseDouble("3.391202")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Niger House Branch").flat(true);

	MarkerOptions marker142 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.552000"), Double
							.parseDouble("3.307000")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("NNPC Ejigbo Cash centre").flat(true);

	MarkerOptions marker143 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601051"), Double
							.parseDouble("3.338637")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Oba Akran Branch").flat(true);

	MarkerOptions marker144 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.461338"), Double
							.parseDouble("3.387165")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Obun-Eko Branch").flat(true);

	MarkerOptions marker145 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.524381"), Double
							.parseDouble("3.379206")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Odun Ade Cash Centre").flat(true);

	MarkerOptions marker146 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.627247"), Double
							.parseDouble("3.348000")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ogba Branch").flat(true);

	MarkerOptions marker147 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.582498"), Double
							.parseDouble("3.384382")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ogudu Agency").flat(true);

	MarkerOptions marker148 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.488372"), Double
							.parseDouble("3.182987")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ojo Cantonment Agency").flat(true);

	MarkerOptions marker149 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.639775"), Double
							.parseDouble("3.368130")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ojodu-Isheri Branch").flat(true);

	MarkerOptions marker150 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.531224"), Double
							.parseDouble("3.355701")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Ojuwoye Cash Centre").flat(true);

	MarkerOptions marker151 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458886"), Double
							.parseDouble("3.385483")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Oke-Arin Market Branch").flat(true);

	MarkerOptions marker152 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.637036"), Double
							.parseDouble("3.302646")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Oke-Odo Branch").flat(true);

	MarkerOptions marker153 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.502206"), Double
							.parseDouble("3.305082")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Okota Branch").flat(true);

	MarkerOptions marker154 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431224"), Double
							.parseDouble("3.469051")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Oniru Market Branch").flat(true);

	MarkerOptions marker155 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.590622"), Double
							.parseDouble("3.359203")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Opebi Branch").flat(true);

	MarkerOptions marker156 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.591774"), Double
							.parseDouble("3.373455")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Oregun Ind. Estate Branch").flat(true);

	MarkerOptions marker157 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.472331"), Double
							.parseDouble("3.595544")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Osapa London Branch").flat(true);

	MarkerOptions marker158 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.562012"), Double
							.parseDouble("3.348746")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Oshodi Branch").flat(true);

	MarkerOptions marker159 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.564487"), Double
							.parseDouble("3.356524")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Oshodi Cantonment Agency").flat(true);

	MarkerOptions marker160 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.524380"), Double
							.parseDouble("3.379206")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Oshodi-Mile 2 Expressway Branch").flat(true);

	MarkerOptions marker161 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.574147"), Double
							.parseDouble("3.486191")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Owode Branch").flat(true);

	MarkerOptions marker162 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.524380"), Double
							.parseDouble("3.379205")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Progressive Market Branch").flat(true);

	MarkerOptions marker163 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431217"), Double
							.parseDouble("3.424997")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Sanusi Fafunwa Branch").flat(true);

	MarkerOptions marker164 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.456010"), Double
							.parseDouble("3.389341")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Saudi Eko Branch").flat(true);

	MarkerOptions marker165 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.524380"), Double
							.parseDouble("3.379206")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Seme Border Branch").flat(true);

	MarkerOptions marker166 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444639"), Double
							.parseDouble("3.364083")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Shell Agency").flat(true);

	MarkerOptions marker167 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.557773"), Double
							.parseDouble("3.366715")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Shomolu Branch").flat(true);

	MarkerOptions marker168 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.524380"), Double
							.parseDouble("3.379206")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Snake Island Cash Centre").flat(true);

	MarkerOptions marker169 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.454322"), Double
							.parseDouble("3.386083")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Stock Exchange House Branch").flat(true);

	MarkerOptions marker170 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.454093"), Double
							.parseDouble("3.402931")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Sura Cash Centre").flat(true);

	MarkerOptions marker171 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.496214"), Double
							.parseDouble("3.341090")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Surulere Aguda Branch").flat(true);

	MarkerOptions marker172 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.512144"), Double
							.parseDouble("3.357697")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Surulere Branch").flat(true);

	MarkerOptions marker173 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.491320"), Double
							.parseDouble("3.356569")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Surulere S/Centre Branch").flat(true);

	MarkerOptions marker174 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.506645"), Double
							.parseDouble("3.368306")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Tejuosho Branch").flat(true);

	MarkerOptions marker175 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.449997"), Double
							.parseDouble("3.366667")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Tin Can Island Branch").flat(true);

	MarkerOptions marker176 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.596337"), Double
							.parseDouble("3.347153")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Toyin Olowu Branch").flat(true);

	MarkerOptions marker177 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455280"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Trinity Branch").flat(true);

	MarkerOptions marker178 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.519241"), Double
							.parseDouble("3.391757")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("University of Lagos Branch").flat(true);

	MarkerOptions marker179 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453425"), Double
							.parseDouble("3.389592")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Western House Branch").flat(true);

	MarkerOptions marker180 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.479268"), Double
							.parseDouble("3.385473")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Willoughby Branch").flat(true);

	MarkerOptions marker181 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.495028"), Double
							.parseDouble("3.380815")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.firstbank))

			.title("Yaba Branch").flat(true);

	// End of first bank branches

	MarkerOptions marker207 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.433355"), Double
							.parseDouble("3.424837")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.citibank))

			.title("Victoria Island Branch").flat(true);

	MarkerOptions marker208 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.435407"), Double
							.parseDouble("3.366100")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.citibank))

			.title("Apapa Branch").flat(true);

	MarkerOptions marker209 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.604731"), Double
							.parseDouble("3.350408")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.citibank))

			.title("Ikeja Branch").flat(true);

	MarkerOptions marker210 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459621"), Double
							.parseDouble("3.386811")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.citibank))

			.title("Idumota Branch").flat(true);

	MarkerOptions marker211 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.491320"), Double
							.parseDouble("3.356569")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Adeniran Ogunsanya Branch").flat(true);

	MarkerOptions marker212 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.613778"), Double
							.parseDouble("3.346025")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Adeniyi Jones Branch").flat(true);

	MarkerOptions marker213 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.437527"), Double
							.parseDouble("3.427856")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Adeola Hopewell Branch").flat(true);

	MarkerOptions marker214 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430306"), Double
							.parseDouble("3.415599")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Adeola Odeku Branch").flat(true);

	MarkerOptions marker215 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430336"), Double
							.parseDouble("3.416127")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Adeola Odeku(2) Branch").flat(true);

	MarkerOptions marker216 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431109"), Double
							.parseDouble("3.424660")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Adetokunbo Ademola Branch").flat(true);

	MarkerOptions marker217 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.433355"), Double
							.parseDouble("3.424837")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Adeyemo Alakija Branch").flat(true);

	MarkerOptions marker218 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.620765"), Double
							.parseDouble("3.303777")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Agege Branch").flat(true);

	MarkerOptions marker219 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.496249"), Double
							.parseDouble("3.340090")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Aguda Branch").flat(true);

	MarkerOptions marker220 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.551417"), Double
							.parseDouble("3.330633")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ajao Estate Branch").flat(true);

	MarkerOptions marker221 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430981"), Double
							.parseDouble("3.430790")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ajose Adeogun Branch").flat(true);

	MarkerOptions marker222 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430050"), Double
							.parseDouble("3.423508")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("AIS/USL Branch").flat(true);

	MarkerOptions marker223 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.631205"), Double
							.parseDouble("3.321252")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Alagbado Branch").flat(true);

	MarkerOptions marker224 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.613939"), Double
							.parseDouble("3.355366")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Alausa Branch").flat(true);

	MarkerOptions marker225 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.447933"), Double
							.parseDouble("3.409792")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Alfred Rewane Road Branch").flat(true);

	MarkerOptions marker226 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.604458"), Double
							.parseDouble("3.351066")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Allen Avenue Branch").flat(true);

	MarkerOptions marker227 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.605823"), Double
							.parseDouble("3.349845")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Allen Avenue(2) Branch").flat(true);

	MarkerOptions marker228 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.450993"), Double
							.parseDouble("3.422171")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Aluko & Oyebode Branch").flat(true);

	MarkerOptions marker229 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.461914"), Double
							.parseDouble("3.157888")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Aspamda Branch").flat(true);

	MarkerOptions marker230 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.434437"), Double
							.parseDouble("3.480987")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Avalon House Branch").flat(true);

	MarkerOptions marker231 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444464"), Double
							.parseDouble("3.424111")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Awolowo Road Branch").flat(true);

	MarkerOptions marker232 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442569"), Double
							.parseDouble("3.420384")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Awolowo Road(2) Branch").flat(true);

	MarkerOptions marker233 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.604505"), Double
							.parseDouble("3.265937")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ayobo Branch").flat(true);

	MarkerOptions marker234 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.490126"), Double
							.parseDouble("3.354747")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Bode Thomas Branch").flat(true);

	MarkerOptions marker235 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453559"), Double
							.parseDouble("3.389415")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Broad Street Branch").flat(true);

	MarkerOptions marker236 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453539"), Double
							.parseDouble("3.389551")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Broad Street(2) Branch").flat(true);

	MarkerOptions marker237 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.440612"), Double
							.parseDouble("3.375243")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Burma Road Apapa Branch").flat(true);

	MarkerOptions marker238 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.454107"), Double
							.parseDouble("3.432666")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Casalydia Branch").flat(true);

	MarkerOptions marker239 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.438147"), Double
							.parseDouble("3.372780")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Commercial Road Branch").flat(true);

	MarkerOptions marker240 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.435671"), Double
							.parseDouble("3.365721")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Creek Road Branch").flat(true);

	MarkerOptions marker241 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453457"), Double
							.parseDouble("3.389311")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("CSS Bookshop Branch").flat(true);

	MarkerOptions marker242 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.519554"), Double
							.parseDouble("3.327479")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Daleko Branch").flat(true);

	MarkerOptions marker243 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.659350"), Double
							.parseDouble("3.292328")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Dopemu Branch").flat(true);

	MarkerOptions marker244 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.628888"), Double
							.parseDouble("3.337987")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Egbeda Branch").flat(true);

	MarkerOptions marker245 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.530204"), Double
							.parseDouble("3.303644")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ejigbo Branch").flat(true);

	MarkerOptions marker246 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.475960"), Double
							.parseDouble("3.279926")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Festac Branch").flat(true);

	MarkerOptions marker247 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.554140"), Double
							.parseDouble("3.347539")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Gbagada Branch").flat(true);

	MarkerOptions marker248 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.429954"), Double
							.parseDouble("3.412111")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Idejo Branch").flat(true);

	MarkerOptions marker249 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.428269"), Double
							.parseDouble("3.412118")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Idejo Street Branch").flat(true);

	MarkerOptions marker250 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.594268"), Double
							.parseDouble("3.287842")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Idimu Branch").flat(true);

	MarkerOptions marker251 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.552627"), Double
							.parseDouble("3.391306")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ifako-Gbagada Branch").flat(true);

	MarkerOptions marker252 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.513669"), Double
							.parseDouble("3.402527")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ijeshatedo Branch").flat(true);

	MarkerOptions marker253 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.591350"), Double
							.parseDouble("3.970968")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Iju Branch").flat(true);

	MarkerOptions marker254 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.619636"), Double
							.parseDouble("3.503432")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ikorodu Branch").flat(true);

	MarkerOptions marker255 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.624305"), Double
							.parseDouble("3.494602")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ikorodu(2) Branch").flat(true);

	MarkerOptions marker256 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.649561"), Double
							.parseDouble(" 3.518328")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ikorodu Road Branch").flat(true);

	MarkerOptions marker257 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.539055"), Double
							.parseDouble("3.286535")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ikotun Branch").flat(true);

	MarkerOptions marker258 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.538844"), Double
							.parseDouble("3.356549")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ilupeju Branch").flat(true);

	MarkerOptions marker259 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.556950"), Double
							.parseDouble("3.363518")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ilupeju(2) Branch").flat(true);

	MarkerOptions marker260 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.663199"), Double
							.parseDouble("3.283547")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ipaja Branch").flat(true);

	MarkerOptions marker261 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.520184"), Double
							.parseDouble("3.323155")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ire Akari Branch").flat(true);

	MarkerOptions marker262 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.515733"), Double
							.parseDouble("3.319681")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Isolo Branch").flat(true);

	MarkerOptions marker263 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455652"), Double
							.parseDouble("3.382714")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Issa Williams Branch").flat(true);

	MarkerOptions marker264 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.603071"), Double
							.parseDouble("3.389915")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ketu Branch").flat(true);

	MarkerOptions marker265 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460530"), Double
							.parseDouble("3.385828")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Kosoko Street Branch").flat(true);

	MarkerOptions marker266 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.499098"), Double
							.parseDouble("3.091407")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Lasu Branch").flat(true);

	MarkerOptions marker267 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.511169"), Double
							.parseDouble("3.338359")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Lawanson Branch").flat(true);

	MarkerOptions marker268 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458985"), Double
							.parseDouble("3.601521")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Lekki Branch").flat(true);

	MarkerOptions marker269 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.432266"), Double
							.parseDouble("3.440493")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ligali Ayorinde Branch").flat(true);

	MarkerOptions marker270 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.434820"), Double
							.parseDouble("3.440483")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ligali Annex Branch").flat(true);

	MarkerOptions marker271 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.452514"), Double
							.parseDouble("3.390239")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Marina B.O.I. Branch").flat(true);

	MarkerOptions marker272 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.450329"), Double
							.parseDouble("3.390610")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Marina Branch").flat(true);

	MarkerOptions marker273 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.572026"), Double
							.parseDouble("3.366511")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Maryland Branch").flat(true);

	MarkerOptions marker274 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.537044"), Double
							.parseDouble("3.346589")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Matori Branch").flat(true);

	MarkerOptions marker275 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.456595"), Double
							.parseDouble("3.302463")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Maza Maza Branch").flat(true);

	MarkerOptions marker276 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.448570"), Double
							.parseDouble("3.406520")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Moloney Branch").flat(true);

	MarkerOptions marker277 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431965"), Double
							.parseDouble("3.431930")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Muri Okunola Branch").flat(true);

	MarkerOptions marker278 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.485720"), Double
							.parseDouble("3.388029")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Muritala Mohamed Way Branch").flat(true);

	MarkerOptions marker279 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.527188"), Double
							.parseDouble("3.354507")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Mushin Branch").flat(true);

	MarkerOptions marker280 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.577359"), Double
							.parseDouble("3.333956")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Nahco Branch").flat(true);

	MarkerOptions marker281 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453711"), Double
							.parseDouble("3.389654")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Nnamdi Azikwe Idumota Branch").flat(true);

	MarkerOptions marker282 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.472940"), Double
							.parseDouble("3.597668")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Oando Ikota Branch").flat(true);

	MarkerOptions marker283 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.614044"), Double
							.parseDouble("3.361640")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Oba Akran Branch").flat(true);

	MarkerOptions marker284 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.465427"), Double
							.parseDouble("3.390012")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Obas Palace Branch").flat(true);

	MarkerOptions marker285 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.598699"), Double
							.parseDouble("3.342881")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Obafemi Awolowo Way Branch").flat(true);

	MarkerOptions marker286 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.638627"), Double
							.parseDouble("3.525839")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Odogunyan Branch").flat(true);

	MarkerOptions marker287 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.628888"), Double
							.parseDouble("3.337987")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ogba Branch").flat(true);

	MarkerOptions marker288 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.503890"), Double
							.parseDouble("3.351442")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ogunlana Drive Branch").flat(true);

	MarkerOptions marker289 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.638997"), Double
							.parseDouble("3.361653")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ojodu Berger Branch").flat(true);

	MarkerOptions marker290 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.574359"), Double
							.parseDouble("3.393584")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ojota Branch").flat(true);

	MarkerOptions marker291 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.509891"), Double
							.parseDouble("3.364440")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Ojuelegba Branch").flat(true);

	MarkerOptions marker292 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458169"), Double
							.parseDouble("3.290573")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Old Ojo Branch").flat(true);

	MarkerOptions marker293 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455280"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Olodi-Apapa Branch").flat(true);

	MarkerOptions marker294 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.446401"), Double
							.parseDouble("3.405665")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Onikan Branch").flat(true);

	MarkerOptions marker295 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.593021"), Double
							.parseDouble("3.358375")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Opebi Branch").flat(true);

	MarkerOptions marker296 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.599442"), Double
							.parseDouble("3.365735")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Oregun Branch").flat(true);

	MarkerOptions marker297 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601838"), Double
							.parseDouble("3.351486")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Oshodi Branch").flat(true);

	MarkerOptions marker298 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.534762"), Double
							.parseDouble("3.347028")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Palm Avenue Branch").flat(true);

	MarkerOptions marker299 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.448718"), Double
							.parseDouble("3.367120")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Point Road Branch").flat(true);

	MarkerOptions marker300 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.445951"), Double
							.parseDouble("3.404037")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Resort Savings Branch").flat(true);
	
	googleMap.addMarker(marker1);
	googleMap.addMarker(marker2);
	googleMap.addMarker(marker3);
	googleMap.addMarker(marker4);
	googleMap.addMarker(marker5);
	googleMap.addMarker(marker6);
	googleMap.addMarker(marker7);
	googleMap.addMarker(marker8);
	googleMap.addMarker(marker9);
	googleMap.addMarker(marker10);
	googleMap.addMarker(marker11);
	googleMap.addMarker(marker12);
	googleMap.addMarker(marker13);
	googleMap.addMarker(marker14);
	googleMap.addMarker(marker15);
	googleMap.addMarker(marker16);
	googleMap.addMarker(marker17);
	googleMap.addMarker(marker18);
	googleMap.addMarker(marker19);
	googleMap.addMarker(marker20);
	googleMap.addMarker(marker21);
	googleMap.addMarker(marker22);
	googleMap.addMarker(marker23);
	googleMap.addMarker(marker25);
	googleMap.addMarker(marker26);
	googleMap.addMarker(marker27);
	googleMap.addMarker(marker28);
	googleMap.addMarker(marker29);
	googleMap.addMarker(marker30);
	googleMap.addMarker(marker31);
	googleMap.addMarker(marker32);
	googleMap.addMarker(marker33);
	googleMap.addMarker(marker34);
	googleMap.addMarker(marker35);
	googleMap.addMarker(marker38);
	googleMap.addMarker(marker39);
	googleMap.addMarker(marker40);
	googleMap.addMarker(marker41);
	googleMap.addMarker(marker42);
	googleMap.addMarker(marker43);
	googleMap.addMarker(marker44);
	googleMap.addMarker(marker45);
	googleMap.addMarker(marker46);
	googleMap.addMarker(marker47);
	googleMap.addMarker(marker48);
	googleMap.addMarker(marker49);
	googleMap.addMarker(marker50);
	googleMap.addMarker(marker51);
	googleMap.addMarker(marker52);
	googleMap.addMarker(marker53);
	googleMap.addMarker(marker54);
	googleMap.addMarker(marker55);
	googleMap.addMarker(marker56);
	googleMap.addMarker(marker57);
	googleMap.addMarker(marker57a);
	googleMap.addMarker(marker58);
	googleMap.addMarker(marker58a);
	googleMap.addMarker(marker59);
	googleMap.addMarker(marker60);
	googleMap.addMarker(marker61);
	googleMap.addMarker(marker62);
	googleMap.addMarker(marker63);
	googleMap.addMarker(marker64);
	googleMap.addMarker(marker65);
	googleMap.addMarker(marker66);
	googleMap.addMarker(marker67);
	googleMap.addMarker(marker68);
	googleMap.addMarker(marker69);
	googleMap.addMarker(marker70);
	googleMap.addMarker(marker71);
	googleMap.addMarker(marker72);
	googleMap.addMarker(marker73);
	googleMap.addMarker(marker74);
	googleMap.addMarker(marker75);
	googleMap.addMarker(marker76);
	googleMap.addMarker(marker77);
	googleMap.addMarker(marker78);
	googleMap.addMarker(marker79);
	googleMap.addMarker(marker80);
	googleMap.addMarker(marker81);
	googleMap.addMarker(marker82);
	googleMap.addMarker(marker83);
	googleMap.addMarker(marker84);
	googleMap.addMarker(marker85);
	googleMap.addMarker(marker86);
	googleMap.addMarker(marker87);
	googleMap.addMarker(marker88);
	googleMap.addMarker(marker89);
	googleMap.addMarker(marker90);
	googleMap.addMarker(marker91);
	googleMap.addMarker(marker92);
	googleMap.addMarker(marker93);
	googleMap.addMarker(marker94);
	googleMap.addMarker(marker95);
	googleMap.addMarker(marker96);
	googleMap.addMarker(marker97);
	googleMap.addMarker(marker98);
	googleMap.addMarker(marker99);
	googleMap.addMarker(marker100);
	googleMap.addMarker(marker101);
	googleMap.addMarker(marker102);
	googleMap.addMarker(marker103);
	googleMap.addMarker(marker104);
	googleMap.addMarker(marker105);
	googleMap.addMarker(marker106);
	googleMap.addMarker(marker107);
	googleMap.addMarker(marker108);
	googleMap.addMarker(marker109);
	googleMap.addMarker(marker110);
	googleMap.addMarker(marker111);
	googleMap.addMarker(marker112);
	googleMap.addMarker(marker113);
	googleMap.addMarker(marker114);
	googleMap.addMarker(marker116);
	googleMap.addMarker(marker117);
	googleMap.addMarker(marker118);
	googleMap.addMarker(marker119);
	googleMap.addMarker(marker120);
	googleMap.addMarker(marker121);
	googleMap.addMarker(marker122);
	googleMap.addMarker(marker123);
	googleMap.addMarker(marker124);
	googleMap.addMarker(marker125);
	googleMap.addMarker(marker126);
	googleMap.addMarker(marker127);
	googleMap.addMarker(marker128);
	googleMap.addMarker(marker129);
	googleMap.addMarker(marker130);
	googleMap.addMarker(marker131);
	googleMap.addMarker(marker132);
	googleMap.addMarker(marker133);
	googleMap.addMarker(marker134);
	googleMap.addMarker(marker135);
	googleMap.addMarker(marker136);
	googleMap.addMarker(marker137);
	googleMap.addMarker(marker138);
	googleMap.addMarker(marker139);
	googleMap.addMarker(marker140);
	googleMap.addMarker(marker141);
	googleMap.addMarker(marker142);
	googleMap.addMarker(marker143);
	googleMap.addMarker(marker144);
	googleMap.addMarker(marker145);
	googleMap.addMarker(marker146);
	googleMap.addMarker(marker147);
	googleMap.addMarker(marker148);
	googleMap.addMarker(marker149);
	googleMap.addMarker(marker150);
	googleMap.addMarker(marker151);
	googleMap.addMarker(marker152);
	googleMap.addMarker(marker153);
	googleMap.addMarker(marker154);
	googleMap.addMarker(marker155);
	googleMap.addMarker(marker156);
	googleMap.addMarker(marker157);
	googleMap.addMarker(marker158);
	googleMap.addMarker(marker159);
	googleMap.addMarker(marker160);
	googleMap.addMarker(marker161);
	googleMap.addMarker(marker162);
	googleMap.addMarker(marker163);
	googleMap.addMarker(marker164);
	googleMap.addMarker(marker165);
	googleMap.addMarker(marker166);
	googleMap.addMarker(marker167);
	googleMap.addMarker(marker168);
	googleMap.addMarker(marker169);
	googleMap.addMarker(marker170);
	googleMap.addMarker(marker171);
	googleMap.addMarker(marker172);
	googleMap.addMarker(marker173);
	googleMap.addMarker(marker174);
	googleMap.addMarker(marker175);
	googleMap.addMarker(marker176);
	googleMap.addMarker(marker177);
	googleMap.addMarker(marker178);
	googleMap.addMarker(marker179);
	googleMap.addMarker(marker180);
	googleMap.addMarker(marker181);
	googleMap.addMarker(marker207);
	googleMap.addMarker(marker208);
	googleMap.addMarker(marker209);
	googleMap.addMarker(marker210);
	googleMap.addMarker(marker211);
	googleMap.addMarker(marker212);
	googleMap.addMarker(marker213);
	googleMap.addMarker(marker214);
	googleMap.addMarker(marker215);
	googleMap.addMarker(marker216);
	googleMap.addMarker(marker217);
	googleMap.addMarker(marker218);
	googleMap.addMarker(marker219);
	googleMap.addMarker(marker220);
	googleMap.addMarker(marker221);
	googleMap.addMarker(marker222);
	googleMap.addMarker(marker223);
	googleMap.addMarker(marker224);
	googleMap.addMarker(marker225);
	googleMap.addMarker(marker226);
	googleMap.addMarker(marker227);
	googleMap.addMarker(marker228);
	googleMap.addMarker(marker229);
	googleMap.addMarker(marker230);
	googleMap.addMarker(marker231);
	googleMap.addMarker(marker232);
	googleMap.addMarker(marker233);
	googleMap.addMarker(marker234);
	googleMap.addMarker(marker235);
	googleMap.addMarker(marker236);
	googleMap.addMarker(marker237);
	googleMap.addMarker(marker238);
	googleMap.addMarker(marker239);
	googleMap.addMarker(marker240);
	googleMap.addMarker(marker241);
	googleMap.addMarker(marker242);
	googleMap.addMarker(marker243);
	googleMap.addMarker(marker244);
	googleMap.addMarker(marker245);
	googleMap.addMarker(marker246);
	googleMap.addMarker(marker247);
	googleMap.addMarker(marker248);
	googleMap.addMarker(marker249);
	googleMap.addMarker(marker250);
	googleMap.addMarker(marker251);
	googleMap.addMarker(marker252);
	googleMap.addMarker(marker253);
	googleMap.addMarker(marker254);
	googleMap.addMarker(marker255);
	googleMap.addMarker(marker256);
	googleMap.addMarker(marker257);
	googleMap.addMarker(marker258);
	googleMap.addMarker(marker259);
	googleMap.addMarker(marker260);
	googleMap.addMarker(marker261);
	googleMap.addMarker(marker262);
	googleMap.addMarker(marker263);
	googleMap.addMarker(marker264);
	googleMap.addMarker(marker265);
	googleMap.addMarker(marker266);
	googleMap.addMarker(marker267);
	googleMap.addMarker(marker268);
	googleMap.addMarker(marker269);
	googleMap.addMarker(marker270);
	googleMap.addMarker(marker271);
	googleMap.addMarker(marker272);
	googleMap.addMarker(marker273);
	googleMap.addMarker(marker274);
	googleMap.addMarker(marker275);
	googleMap.addMarker(marker276);
	googleMap.addMarker(marker277);
	googleMap.addMarker(marker278);
	googleMap.addMarker(marker279);
	googleMap.addMarker(marker280);
	googleMap.addMarker(marker281);
	googleMap.addMarker(marker282);
	googleMap.addMarker(marker283);
	googleMap.addMarker(marker284);
	googleMap.addMarker(marker285);
	googleMap.addMarker(marker286);
	googleMap.addMarker(marker287);
	googleMap.addMarker(marker288);
	googleMap.addMarker(marker289);
	googleMap.addMarker(marker290);
	googleMap.addMarker(marker291);
	googleMap.addMarker(marker292);
	googleMap.addMarker(marker293);
	googleMap.addMarker(marker294);
	googleMap.addMarker(marker295);
	googleMap.addMarker(marker296);
	googleMap.addMarker(marker297);
	googleMap.addMarker(marker298);
	googleMap.addMarker(marker299);
	googleMap.addMarker(marker300);
		}
		
		private void bankMarkerTwo(){
			MarkerOptions marker301 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.505717"), Double
							.parseDouble("3.377993")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Sabo Branch").flat(true);

	MarkerOptions marker302 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.427023"), Double
							.parseDouble("3.421173")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Saka Tinubu Branch").flat(true);

	MarkerOptions marker303 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.454908"), Double
							.parseDouble("3.257607")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Satellite Town Branch").flat(true);

	MarkerOptions marker304 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.594850"), Double
							.parseDouble("3.338498")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Simbiat Abiola Branch").flat(true);

	MarkerOptions marker305 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.531762"), Double
							.parseDouble("3.380467")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Somolu Branch").flat(true);

	MarkerOptions marker306 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.522806"), Double
							.parseDouble("3.385982")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("St Finbarrs Branch").flat(true);

	MarkerOptions marker307 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.506645"), Double
							.parseDouble("3.368306")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Tejuosho Branch").flat(true);

	MarkerOptions marker308 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.596325"), Double
							.parseDouble("3.347881")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Toyin Street Branch").flat(true);

	MarkerOptions marker309 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.519240"), Double
							.parseDouble("3.391757")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("University Of Lagos Branch").flat(true);

	MarkerOptions marker310 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.445259"), Double
							.parseDouble("3.371347")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Wharf Road Branch").flat(true);

	MarkerOptions marker311 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.448258"), Double
							.parseDouble("3.331141")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.accessbank))

			.title("Tincan Branch").flat(true);

	MarkerOptions marker312 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.648185"), Double
							.parseDouble("3.305788")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Abule Egba Branch").flat(true);

	MarkerOptions marker313 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.605831"), Double
							.parseDouble("3.349841")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Allen Branch").flat(true);

	MarkerOptions marker314 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444465"), Double
							.parseDouble("3.425174")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Awolowo Road Branch").flat(true);

	MarkerOptions marker315 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.457696"), Double
							.parseDouble("3.384947")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Balogun Branch").flat(true);

	MarkerOptions marker316 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.429636"), Double
							.parseDouble("3.40952")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Bar Beach Branch").flat(true);

	MarkerOptions marker317 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.438096"), Double
							.parseDouble("3.368179")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Burma Road 1 Branch").flat(true);

	MarkerOptions marker318 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.437433"), Double
							.parseDouble("3.371878")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Creek Road Branch").flat(true);

	MarkerOptions marker319 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.449180"), Double
							.parseDouble("3.368195")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Eleganza Plaza Branch").flat(true);

	MarkerOptions marker320 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.462154"), Double
							.parseDouble("3.285871")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Festac Branch").flat(true);

	MarkerOptions marker321 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.555499"), Double
							.parseDouble("3.350641")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Ibafon Branch").flat(true);

	MarkerOptions marker322 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.433051"), Double
							.parseDouble("3.425816")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Iddo Branch").flat(true);

	MarkerOptions marker323 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.540799"), Double
							.parseDouble("3.356247")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Idi Oro Branch").flat(true);

	MarkerOptions marker324 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455374"), Double
							.parseDouble("3.384912")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Marina Branch").flat(true);

	MarkerOptions marker325 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.604248"), Double
							.parseDouble("3.390883")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Mile 12 Branch").flat(true);

	MarkerOptions marker326 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.537933"), Double
							.parseDouble("3.346039")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Mushin Branch").flat(true);

	MarkerOptions marker327 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601051"), Double
							.parseDouble("3.338637")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Oba Akran Branch").flat(true);

	MarkerOptions marker328 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.589014"), Double
							.parseDouble("3.361565")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Opebi Branch").flat(true);

	MarkerOptions marker329 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600453"), Double
							.parseDouble("3.364171")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Oregun Road Branch").flat(true);

	MarkerOptions marker330 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431107"), Double
							.parseDouble("3.428121")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Sanusi Fafunwa Road Branch").flat(true);

	MarkerOptions marker331 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.490125"), Double
							.parseDouble("3.35476")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Surulere Branch").flat(true);

	MarkerOptions marker332 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.433161"), Double
							.parseDouble("3.356054")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Tincan Port Branch").flat(true);

	MarkerOptions marker333 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.505226"), Double
							.parseDouble("3.378523")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unitybank))

			.title("Yaba Comm Avenue Branch").flat(true);

	MarkerOptions marker334 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.617306"), Double
							.parseDouble("3.345972")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.heritagebank))

			.title("Adeniyi Jones Branch").flat(true);

	MarkerOptions marker335 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431109"), Double
							.parseDouble("3.42474")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.heritagebank))

			.title("Adetokunbo Ademola, VI Branch").flat(true);

	MarkerOptions marker336 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45528"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.heritagebank))

			.title("Apapa Branch").flat(true);

	MarkerOptions marker337 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6206"), Double
							.parseDouble("3.355075")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.heritagebank))

			.title("Ashabi Cole, Alausa Branch").flat(true);

	MarkerOptions marker338 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444312"), Double
							.parseDouble("3.424119")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.heritagebank))

			.title("Awolowo Road, Ikoyi Branch").flat(true);

	MarkerOptions marker339 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430981"), Double
							.parseDouble("3.43079")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.heritagebank))

			.title("Head Office").flat(true);

	MarkerOptions marker340 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.450707"), Double
							.parseDouble("3.393327")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.heritagebank))

			.title("Marina Branch").flat(true);

	MarkerOptions marker341 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.61886"), Double
							.parseDouble("3.300407")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Abule-Egba Branch").flat(true);

	MarkerOptions marker342 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.437527"), Double
							.parseDouble("3.427856")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Adeola Hopewell Branch").flat(true);

	MarkerOptions marker343 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430534"), Double
							.parseDouble("3.415569")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Adeola Odeku Branch").flat(true);

	MarkerOptions marker344 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.433967"), Double
							.parseDouble("3.42457")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Adeyemo Alakija Branch").flat(true);

	MarkerOptions marker345 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.427537"), Double
							.parseDouble("3.409279")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ahmadu Bello Way Branch").flat(true);
	MarkerOptions marker346 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.46926"), Double
							.parseDouble("3.574415")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ajah Branch").flat(true);

	MarkerOptions marker347 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.577359"), Double
							.parseDouble("3.333956")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ajao Estate Branch").flat(true);

	MarkerOptions marker348 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430998"), Double
							.parseDouble("3.434132")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ajose Adeogun Branch").flat(true);

	MarkerOptions marker349 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.522806"), Double
							.parseDouble("3.385982")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Akoka Branch").flat(true);

	MarkerOptions marker350 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.591574"), Double
							.parseDouble("3.291062")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Akowonjo (2) Branch").flat(true);

	MarkerOptions marker351 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.599254"), Double
							.parseDouble("3.294097")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Akowonjo Branch").flat(true);

	MarkerOptions marker352 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.488372"), Double
							.parseDouble("3.182987")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Alaba Branch").flat(true);

	MarkerOptions marker353 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.60167"), Double
							.parseDouble("3.35191")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Allen Branch").flat(true);

	MarkerOptions marker354 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458264"), Double
							.parseDouble("3.365785")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Amuwo Odofin Branch").flat(true);

	MarkerOptions marker355 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.438158"), Double
							.parseDouble("3.372527")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Apapa (2) Branch").flat(true);

	MarkerOptions marker356 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.448533"), Double
							.parseDouble("3.367295")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Apapa (3) Branch").flat(true);

	MarkerOptions marker357 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45528"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Apapa Branch").flat(true);

	MarkerOptions marker358 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459993"), Double
							.parseDouble("3.230824")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Aspamda Branch").flat(true);

	MarkerOptions marker359 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6071"), Double
							.parseDouble("3.348513")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Awolowo Road Branch").flat(true);

	MarkerOptions marker360 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.608248"), Double
							.parseDouble("3.350109")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Awolowo Way Branch").flat(true);

	MarkerOptions marker361 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45615"), Double
							.parseDouble("3.382467")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Broad Street Branch").flat(true);

	MarkerOptions marker362 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.438158"), Double
							.parseDouble("3.372527")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Commercial Road Branch").flat(true);

	MarkerOptions marker363 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.595759"), Double
							.parseDouble("3.340094")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Computer Village Branch").flat(true);

	MarkerOptions marker364 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442277"), Double
							.parseDouble("3.376492")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ebute Metta Branch").flat(true);

	MarkerOptions marker365 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.57987"), Double
							.parseDouble("3.973659")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Epe Branch").flat(true);

	MarkerOptions marker366 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459624"), Double
							.parseDouble("3.386802")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ereko Branch").flat(true);

	MarkerOptions marker367 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.462154"), Double
							.parseDouble("3.285871")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Festac Branch").flat(true);

	MarkerOptions marker368 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.440859"), Double
							.parseDouble("3.331767")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ibafon Branch").flat(true);

	MarkerOptions marker369 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459575"), Double
							.parseDouble("3.387545")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Idumota Branch").flat(true);

	MarkerOptions marker370 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.497672"), Double
							.parseDouble("3.326374")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ijesha Branch").flat(true);

	MarkerOptions marker371 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.668958"), Double
							.parseDouble("3.326395")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Iju Branch").flat(true);

	MarkerOptions marker372 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.588305"), Double
							.parseDouble("3.362123")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ikeja (2) Branch").flat(true);

	MarkerOptions marker373 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.608219"), Double
							.parseDouble("3.362096")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ikeja (3) Branch").flat(true);

	MarkerOptions marker374 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.602002"), Double
							.parseDouble("3.338118")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ikeja (4) Branch").flat(true);

	MarkerOptions marker375 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.628888"), Double
							.parseDouble("3.337987")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ikeja (5) Branch").flat(true);

	MarkerOptions marker376 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.608248"), Double
							.parseDouble("3.350109")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ikeja Branch").flat(true);

	MarkerOptions marker377 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.621666"), Double
							.parseDouble("3.500503")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ikorodu Branch").flat(true);

	MarkerOptions marker378 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458985"), Double
							.parseDouble("3.601521")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ikota Branch").flat(true);

	MarkerOptions marker379 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.58711"), Double
							.parseDouble("3.286114")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ikotun Branch").flat(true);

	MarkerOptions marker380 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444402"), Double
							.parseDouble("3.42423")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ikoyi (2) Branch").flat(true);

	MarkerOptions marker381 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.556985"), Double
							.parseDouble("3.363519")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ilupeju (2) Branch").flat(true);

	MarkerOptions marker382 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.553648"), Double
							.parseDouble("3.356674")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ilupeju Branch").flat(true);

	MarkerOptions marker383 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.521598"), Double
							.parseDouble("3.322123")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Isolo Branch").flat(true);

	MarkerOptions marker384 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.606338"), Double
							.parseDouble("3.392326")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ketu Branch").flat(true);

	MarkerOptions marker385 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430928"), Double
							.parseDouble("3.427121")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Key Stone Head Office").flat(true);

	MarkerOptions marker386 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45528"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Kirikiri Branch").flat(true);

	MarkerOptions marker387 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.511148"), Double
							.parseDouble("3.339403")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Lawanson Branch").flat(true);

	MarkerOptions marker388 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.434858"), Double
							.parseDouble("3.495726")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Lekki Branch").flat(true);

	MarkerOptions marker389 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.599882"), Double
							.parseDouble("3.37306")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Magodo Branch").flat(true);

	MarkerOptions marker390 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.573246"), Double
							.parseDouble("3.363175")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Maryland Branch").flat(true);

	MarkerOptions marker391 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.535592"), Double
							.parseDouble("3.348665")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Mushin (2) Branch").flat(true);

	MarkerOptions marker392 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.527188"), Double
							.parseDouble("3.354507")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Mushin Branch").flat(true);

	MarkerOptions marker393 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.602002"), Double
							.parseDouble("3.338118")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Oba Akran Branch").flat(true);

	MarkerOptions marker394 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.574413"), Double
							.parseDouble("3.393515")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ogudu Branch").flat(true);

	MarkerOptions marker395 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.488372"), Double
							.parseDouble("3.182987")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Ojo Branch").flat(true);

	MarkerOptions marker396 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.636999"), Double
							.parseDouble("3.315727")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Oko-Oba Branch").flat(true);

	MarkerOptions marker397 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.637053"), Double
							.parseDouble("3.355236")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Omole Branch").flat(true);

	MarkerOptions marker398 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600111"), Double
							.parseDouble("3.364972")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Oregun Branch").flat(true);

	MarkerOptions marker399 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.52897"), Double
							.parseDouble("3.379794")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Orile (2) Branch").flat(true);

	MarkerOptions marker400 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459993"), Double
							.parseDouble("3.230824")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Orile Branch").flat(true);

	MarkerOptions marker401 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.54125"), Double
							.parseDouble("3.367015")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Palmgroove Branch").flat(true);

	MarkerOptions marker402 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.448533"), Double
							.parseDouble("3.367295")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Point Road Branch").flat(true);

	MarkerOptions marker403 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.496249"), Double
							.parseDouble("3.34009")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Surulere (2) Branch").flat(true);

	MarkerOptions marker404 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.488394"), Double
							.parseDouble("3.360574")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Surulere Branch").flat(true);

	MarkerOptions marker405 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.446721"), Double
							.parseDouble("3.401582")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Tafewa Balewa Square Branch").flat(true);

	MarkerOptions marker406 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.423566"), Double
							.parseDouble("3.426346")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Tiamiyu Savage Branch").flat(true);

	MarkerOptions marker407 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460506"), Double
							.parseDouble("3.250034")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Trade Fair Complex Branch").flat(true);

	MarkerOptions marker408 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45528"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Warehouse Road Apapa Branch").flat(true);

	MarkerOptions marker409 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.441594"), Double
							.parseDouble("3.377797")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Wharf Road Branch").flat(true);

	MarkerOptions marker410 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.479268"), Double
							.parseDouble("3.385473")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.keystonebank))

			.title("Willoughby Branch").flat(true);

	MarkerOptions marker411 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.614867"), Double
							.parseDouble("3.345913")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Adeniyi Jones Branch").flat(true);

	MarkerOptions marker412 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.447763"), Double
							.parseDouble("3.470191")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Admiralty Way Branch").flat(true);

	MarkerOptions marker413 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460204"), Double
							.parseDouble("3.165468")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Alaba I Branch").flat(true);

	MarkerOptions marker414 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.603728"), Double
							.parseDouble("3.350973")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Allen Avenue Branch").flat(true);

	MarkerOptions marker415 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453439"), Double
							.parseDouble("3.389274")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Ap House Branch").flat(true);

	MarkerOptions marker416 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.440372"), Double
							.parseDouble("3.372087")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Apapa Commercial Road Branch").flat(true);

	MarkerOptions marker417 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442554"), Double
							.parseDouble("3.375931")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Apapa Warehouse Road Branch").flat(true);

	MarkerOptions marker418 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.469089"), Double
							.parseDouble("3.243196")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Aspamda Branch").flat(true);

	MarkerOptions marker419 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.457432"), Double
							.parseDouble("3.249641")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("BBA Branch").flat(true);

	MarkerOptions marker420 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4343"), Double
							.parseDouble("3.431065")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Bishop Aboyade Cole Branch").flat(true);

	MarkerOptions marker421 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.489503"), Double
							.parseDouble("3.357232")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Bode Thomas Branch").flat(true);

	MarkerOptions marker422 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45354"), Double
							.parseDouble("3.38955")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Broad Street Branch").flat(true);

	MarkerOptions marker423 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.535233"), Double
							.parseDouble("3.348967")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Daleko Branch").flat(true);

	MarkerOptions marker424 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.542354"), Double
							.parseDouble("3.347335")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Fatai Atere Branch").flat(true);

	MarkerOptions marker425 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.462154"), Double
							.parseDouble("3.285871")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Festac Branch").flat(true);

	MarkerOptions marker426 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.437592"), Double
							.parseDouble("3.357608")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Ibafon Branch").flat(true);

	MarkerOptions marker427 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4622"), Double
							.parseDouble("3.389271")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Iga - Idugaran Branch").flat(true);

	MarkerOptions marker428 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458985"), Double
							.parseDouble("3.601521")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Ikota Branch").flat(true);

	MarkerOptions marker429 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.547603"), Double
							.parseDouble("3.33751")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Isolo Branch").flat(true);

	MarkerOptions marker430 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455315"), Double
							.parseDouble("3.385434")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Issa Williams Branch").flat(true);

	MarkerOptions marker431 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.449894"), Double
							.parseDouble("3.394942")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Marina Branch").flat(true);

	MarkerOptions marker432 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.456459"), Double
							.parseDouble("3.385995")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Martins Street Branch").flat(true);

	MarkerOptions marker433 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.581818"), Double
							.parseDouble("3.321135")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Murtala Mohammed International Airport Branch")
			.flat(true);

	MarkerOptions marker434 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.623276"), Double
							.parseDouble("3.354223")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("NECA Branch").flat(true);

	MarkerOptions marker435 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.509921"), Double
							.parseDouble("3.365004")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Ojuelegba Branch").flat(true);

	MarkerOptions marker436 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459691"), Double
							.parseDouble("3.38508")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Oke-Arin Branch").flat(true);

	MarkerOptions marker437 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431577"), Double
							.parseDouble("3.426843")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Oladele Olashore Branch").flat(true);

	MarkerOptions marker438 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.574817"), Double
							.parseDouble("3.362084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("OPIC Branch").flat(true);

	MarkerOptions marker439 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.593152"), Double
							.parseDouble("3.372016")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Oregun Branch").flat(true);

	MarkerOptions marker440 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453149"), Double
							.parseDouble("3.385621")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Reinsurance - Marina Branch").flat(true);

	MarkerOptions marker441 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.437293"), Double
							.parseDouble("3.427797")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.mainstreetbank))

			.title("Victoria Island Branch").flat(true);

	MarkerOptions marker442 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453559"), Double
							.parseDouble("3.389415")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("118 Broad Street Branch").flat(true);

	MarkerOptions marker443 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.43007"), Double
							.parseDouble("3.409131")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Adeola-Odeku Branch").flat(true);

	MarkerOptions marker444 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.895364"), Double
							.parseDouble("3.012567")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Agbara Branch").flat(true);

	MarkerOptions marker445 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.625288"), Double
							.parseDouble("3.317424")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Agege Branch").flat(true);
	MarkerOptions marker446 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430981"), Double
							.parseDouble("3.43079")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Ajose Adeogun Branch").flat(true);

	MarkerOptions marker447 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458769"), Double
							.parseDouble("3.305654")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Amuwo-Odofin Branch").flat(true);

	MarkerOptions marker448 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442666"), Double
							.parseDouble("3.373309")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Apapa - Eleganza Plaza Branch").flat(true);

	MarkerOptions marker449 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444368"), Double
							.parseDouble("3.424981")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Awolowo Road Branch").flat(true);

	MarkerOptions marker450 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.436208"), Double
							.parseDouble("3.364495")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Creek Apapa Branch").flat(true);

	MarkerOptions marker451 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.59794"), Double
							.parseDouble("3.396442")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Davies Street Branch").flat(true);

	MarkerOptions marker452 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.463546"), Double
							.parseDouble("3.388017")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Docemo Idumota Branch").flat(true);

	MarkerOptions marker453 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.517655"), Double
							.parseDouble("3.368239")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Ebute Metta Branch").flat(true);

	MarkerOptions marker454 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.541813"), Double
							.parseDouble("3.281616")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Egbe Ikotun Branch").flat(true);

	MarkerOptions marker455 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.447926"), Double
							.parseDouble("3.408958")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Falomo Branch").flat(true);

	MarkerOptions marker456 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.454114"), Double
							.parseDouble("3.420854")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Federal Secretariat Branch").flat(true);

	MarkerOptions marker457 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.443784"), Double
							.parseDouble("3.42591")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Foreshore Towers Branch").flat(true);

	MarkerOptions marker458 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.550559"), Double
							.parseDouble("3.268389")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Idimu Branch").flat(true);

	MarkerOptions marker459 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.483041"), Double
							.parseDouble("3.358044")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Iganmu Branch").flat(true);

	MarkerOptions marker460 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.469274"), Double
							.parseDouble("3.369002")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Ijora Branch").flat(true);

	MarkerOptions marker461 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.621244"), Double
							.parseDouble("3.501697")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Ikorodu Branch").flat(true);

	MarkerOptions marker462 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.550982"), Double
							.parseDouble("3.355711")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Ilupeju Branch").flat(true);

	MarkerOptions marker463 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.529962"), Double
							.parseDouble("3.331985")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Isolo Branch").flat(true);

	MarkerOptions marker464 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.510648"), Double
							.parseDouble("3.360527")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Lawanson Branch").flat(true);

	MarkerOptions marker465 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.471402"), Double
							.parseDouble("3.576347")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Lewis Street Branch").flat(true);

	MarkerOptions marker466 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.448225"), Double
							.parseDouble("3.405648")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Moloney Branch").flat(true);

	MarkerOptions marker467 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.446984"), Double
							.parseDouble("3.411985")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Obalende Branch").flat(true);

	MarkerOptions marker468 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.461271"), Double
							.parseDouble("3.387499")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Obun Eko Branch").flat(true);

	MarkerOptions marker469 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460913"), Double
							.parseDouble("3.385534")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Oke-Arin Branch").flat(true);

	MarkerOptions marker470 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.429449"), Double
							.parseDouble("3.428863")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Oyin Jola V/I Branch").flat(true);

	MarkerOptions marker471 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.642644"), Double
							.parseDouble("3.323412")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Pencinema Branch").flat(true);

	MarkerOptions marker472 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444641"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Plaza Branch").flat(true);

	MarkerOptions marker473 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458868"), Double
							.parseDouble("3.383543")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Sanusi Olusi Branch").flat(true);

	MarkerOptions marker474 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.533313"), Double
							.parseDouble("3.367627")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Somolu Branch").flat(true);

	MarkerOptions marker475 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.498054"), Double
							.parseDouble("3.360898")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Surulere Branch").flat(true);

	MarkerOptions marker476 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431048"), Double
							.parseDouble("3.343551")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Tin-Can Island Branch").flat(true);

	MarkerOptions marker477 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430928"), Double
							.parseDouble("3.427121")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Victoria Island Branch").flat(true);

	MarkerOptions marker478 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442277"), Double
							.parseDouble("3.375733")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Wharf Road, Apapa Branch").flat(true);

	MarkerOptions marker479 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.495014"), Double
							.parseDouble("3.38082")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Yaba Branch").flat(true);

	MarkerOptions marker480 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.457383"), Double
							.parseDouble("3.388563")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.unionbank))

			.title("Yinka Folawiyo, Apapa Branch").flat(true);

	MarkerOptions marker481 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.437527"), Double
							.parseDouble("3.427856")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Adeola Hopewell Branch").flat(true);

	MarkerOptions marker482 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430533"), Double
							.parseDouble("3.415567")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Adeola Odeku Branch").flat(true);

	MarkerOptions marker483 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.620749"), Double
							.parseDouble("3.353115")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Agidingbi(2) Branch").flat(true);

	MarkerOptions marker484 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.629868"), Double
							.parseDouble("3.345142")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Agindigbi Branch").flat(true);

	MarkerOptions marker485 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.502206"), Double
							.parseDouble("3.305082")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Ago Palace Way Branch").flat(true);

	MarkerOptions marker486 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.449192"), Double
							.parseDouble("3.550009")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Ajah Branch").flat(true);

	MarkerOptions marker487 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600369"), Double
							.parseDouble("3.352789")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Allen Branch").flat(true);

	MarkerOptions marker488 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.467056"), Double
							.parseDouble("3.244451")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Aspamda Tradefair Branch").flat(true);

	MarkerOptions marker489 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.457694"), Double
							.parseDouble("3.384947")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Balogun Street Branch").flat(true);

	MarkerOptions marker490 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.457589"), Double
							.parseDouble("3.251056")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Bba 1 Branch").flat(true);

	MarkerOptions marker491 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453566"), Double
							.parseDouble("3.389659")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Broad Street Branch").flat(true);

	MarkerOptions marker492 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.438715"), Double
							.parseDouble("3.372911")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Commercial Road Branch").flat(true);

	MarkerOptions marker493 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.435707"), Double
							.parseDouble("3.365984")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Creek Road Branch").flat(true);

	MarkerOptions marker494 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.483301"), Double
							.parseDouble("3.387331")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Ebute Metta Branch").flat(true);

	MarkerOptions marker495 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.462661"), Double
							.parseDouble("3.388121")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Enu-Owa, Idumota Branch").flat(true);

	MarkerOptions marker496 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431074"), Double
							.parseDouble("3.411751")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Femi Pearse Branch").flat(true);

	MarkerOptions marker497 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.42825"), Double
							.parseDouble("3.408571")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Head Office").flat(true);

	MarkerOptions marker498 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460058"), Double
							.parseDouble("3.389629")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Idumagbo Branch").flat(true);

	MarkerOptions marker499 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.461311"), Double
							.parseDouble("3.387277")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Idumota Branch").flat(true);
	MarkerOptions marker500 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.557722"), Double
							.parseDouble("3.111087")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Igbesa Branch").flat(true);

	MarkerOptions marker501 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442016"), Double
							.parseDouble("3.418364")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Ikoyi Branch").flat(true);

	MarkerOptions marker502 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.553648"), Double
							.parseDouble("3.356674")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Ilupeju Bypass Branch").flat(true);

	MarkerOptions marker503 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.554823"), Double
							.parseDouble("3.354142")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Ilupeju Industrial Avenue Branch").flat(true);

	MarkerOptions marker504 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.504237"), Double
							.parseDouble("3.381618")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Karimu Ikotun Branch").flat(true);

	MarkerOptions marker505 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.433257"), Double
							.parseDouble("3.420131")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Martins Street Branch").flat(true);

	MarkerOptions marker506 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459794"), Double
							.parseDouble("3.331242")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Navy Town, Mammy Market Branch").flat(true);

	MarkerOptions marker507 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.435406"), Double
							.parseDouble("3.366094")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Nnewi Building Apapa Branch").flat(true);

	MarkerOptions marker508 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.461274"), Double
							.parseDouble("3.387499")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Obun-Eko, Idumota Branch").flat(true);

	MarkerOptions marker509 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.628492"), Double
							.parseDouble("3.341502")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Ogba Branch").flat(true);

	MarkerOptions marker510 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.529999"), Double
							.parseDouble("3.354469")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Ojuwoye Branch").flat(true);

	MarkerOptions marker511 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459166"), Double
							.parseDouble("3.38486")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Oke-Arin Branch").flat(true);

	MarkerOptions marker512 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.556807"), Double
							.parseDouble("3.351295")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Oshodi Branch").flat(true);

	MarkerOptions marker513 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.595173"), Double
							.parseDouble("3.340223")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Otigba Branch").flat(true);

	MarkerOptions marker514 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460204"), Double
							.parseDouble("3.165468")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Raybross Plaza Branch").flat(true);

	MarkerOptions marker515 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.427023"), Double
							.parseDouble("3.421211")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Saka Tinubu Branch").flat(true);

	MarkerOptions marker516 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.509111"), Double
							.parseDouble("3.352603")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Surulere Branch").flat(true);

	MarkerOptions marker517 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45114"), Double
							.parseDouble("3.363874")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Tincan Branch").flat(true);

	MarkerOptions marker518 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45528"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Trinity Branch").flat(true);

	MarkerOptions marker519 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45528"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Warehouse Apapa Branch").flat(true);

	MarkerOptions marker520 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442277"), Double
							.parseDouble("3.376492")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.enterprisebank))

			.title("Wharf Road Branch").flat(true);

	MarkerOptions marker521 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.435406"), Double
							.parseDouble("3.366094")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("1 Creek Road (Nnewi Building) Branch").flat(true);

	MarkerOptions marker522 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.441976"), Double
							.parseDouble("3.418365")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("114 Awolowo Road - Ikoyi Branch").flat(true);

	MarkerOptions marker523 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444457"), Double
							.parseDouble("3.423829")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("228a Awolowo Road - Ikoyi Branch").flat(true);

	MarkerOptions marker524 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.587961"), Double
							.parseDouble("3.362264")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("2b Opebi Branch").flat(true);

	MarkerOptions marker525 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430035"), Double
							.parseDouble("3.429916")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("30 Adetokunbo Ademola Branch").flat(true);

	MarkerOptions marker526 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.426402"), Double
							.parseDouble("3.430325")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("62 Adetokunbo Ademola Branch").flat(true);

	MarkerOptions marker527 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.588342"), Double
							.parseDouble("3.362094")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("68 Opebi Branch").flat(true);

	MarkerOptions marker528 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.589014"), Double
							.parseDouble("3.361565")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Adebola House Branch").flat(true);

	MarkerOptions marker529 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.491461"), Double
							.parseDouble("3.356596")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Adeniran Ogunsanya - Surulere Branch").flat(true);

	MarkerOptions marker530 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431398"), Double
							.parseDouble("3.429643")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Adeola Hopewell Branch").flat(true);

	MarkerOptions marker531 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430082"), Double
							.parseDouble("3.417924")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Adeola Odeku Branch").flat(true);

	MarkerOptions marker532 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.577359"), Double
							.parseDouble("3.333956")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Airport Road - Ikeja Branch").flat(true);

	MarkerOptions marker533 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601984"), Double
							.parseDouble("3.351766")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Allen Branch").flat(true);

	// contd from here for heritage bank
	MarkerOptions marker534 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.607361"), Double
							.parseDouble("3.348854")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Aromire Branch").flat(true);

	MarkerOptions marker535 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.617266"), Double
							.parseDouble("3.361942")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Awolowo Way - Ikeja Branch").flat(true);

	MarkerOptions marker536 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.472516"), Double
							.parseDouble("3.568616")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Awoyaya Branch").flat(true);

	MarkerOptions marker537 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.437858"), Double
							.parseDouble("3.516692")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Bakky Plaza - Lekki Branch").flat(true);

	MarkerOptions marker538 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.613606"), Double
							.parseDouble("3.347191")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Coker Branch").flat(true);

	MarkerOptions marker539 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.440377"), Double
							.parseDouble("3.372105")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Commercial Road Branch").flat(true);

	MarkerOptions marker540 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601838"), Double
							.parseDouble("3.351486")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Conoil Station - Ikeja GRA Branch").flat(true);

	MarkerOptions marker541 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.531297"), Double
							.parseDouble("3.338601")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Daleko Branch").flat(true);

	MarkerOptions marker542 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.597334"), Double
							.parseDouble("3.394274")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Demurin Branch").flat(true);

	MarkerOptions marker543 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.530204"), Double
							.parseDouble("3.303644")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Ejigbo - NNPC Branch").flat(true);

	MarkerOptions marker544 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.54125"), Double
							.parseDouble("3.367015")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Fadeyi Branch").flat(true);

	MarkerOptions marker545 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.495063"), Double
							.parseDouble("3.380802")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Herbert Macaulay- Yaba Branch").flat(true);
	MarkerOptions marker546 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45528"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Ibru Jetty - Apapa Branch").flat(true);

	MarkerOptions marker547 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.48572"), Double
							.parseDouble("3.388029")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Iddo Branch").flat(true);

	MarkerOptions marker548 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.594268"), Double
							.parseDouble("3.287842")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Idimu Branch").flat(true);

	MarkerOptions marker549 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.642644"), Double
							.parseDouble("3.323412")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Iju Road - Ifako Branch").flat(true);

	MarkerOptions marker550 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.552406"), Double
							.parseDouble("3.367351")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Ikorodu Branch").flat(true);

	MarkerOptions marker551 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.576941"), Double
							.parseDouble("3.360331")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Ikota Shopping Complex - Ajah Branch").flat(true);

	MarkerOptions marker552 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.570721"), Double
							.parseDouble("3.372329")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Ilupeju Branch").flat(true);

	MarkerOptions marker553 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.521598"), Double
							.parseDouble("3.322123")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Ire-Akari - Isolo Branch").flat(true);

	MarkerOptions marker554 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.513398"), Double
							.parseDouble("3.334084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Itire Branch").flat(true);

	MarkerOptions marker555 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.61886"), Double
							.parseDouble("3.300407")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Iyana-Ipaja Branch").flat(true);

	MarkerOptions marker556 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.604268"), Double
							.parseDouble("3.390899")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Ketu Branch").flat(true);

	MarkerOptions marker557 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.448029"), Double
							.parseDouble("3.326542")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Kirikiri Road Branch").flat(true);

	MarkerOptions marker558 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.504018"), Double
							.parseDouble("3.582007")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Langbasa Service Centre").flat(true);

	MarkerOptions marker559 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.469632"), Double
							.parseDouble("3.200564")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Lasu Branch").flat(true);

	MarkerOptions marker560 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.544276"), Double
							.parseDouble("3.342851")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Matori Service Centre").flat(true);

	MarkerOptions marker561 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.540395"), Double
							.parseDouble("3.346236")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Matori Branch").flat(true);

	MarkerOptions marker562 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455224"), Double
							.parseDouble("3.356731")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Mobil Road - Ajegunle Branch").flat(true);

	MarkerOptions marker563 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.527188"), Double
							.parseDouble("3.354507")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Mushin Branch").flat(true);

	MarkerOptions marker564 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.604424"), Double
							.parseDouble("3.337162")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Oba Akran Branch").flat(true);

	MarkerOptions marker565 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.63304"), Double
							.parseDouble("3.341531")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Ogba Branch").flat(true);

	MarkerOptions marker566 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.619413"), Double
							.parseDouble("3.510454")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Ogijo Branch").flat(true);

	MarkerOptions marker567 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.581701"), Double
							.parseDouble("3.3825")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Ogudu Branch").flat(true);

	MarkerOptions marker568 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.503889"), Double
							.parseDouble("3.351442")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Ogunlana Drive- Surulere Branch").flat(true);

	MarkerOptions marker569 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.529999"), Double
							.parseDouble("3.354469")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Ojuwoye Service Centre").flat(true);

	MarkerOptions marker570 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.515823"), Double
							.parseDouble("3.319842")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Okota Road Branch").flat(true);

	MarkerOptions marker571 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.563075"), Double
							.parseDouble("3.346502")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Oshodi Branch").flat(true);

	MarkerOptions marker572 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.503066"), Double
							.parseDouble("3.37672")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Oworonsoki Service Centre").flat(true);

	MarkerOptions marker573 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.427023"), Double
							.parseDouble("3.421212")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Saka Tinubu Branch").flat(true);

	MarkerOptions marker574 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600783"), Double
							.parseDouble("3.309706")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Shasha Road - Dopemu Branch").flat(true);

	MarkerOptions marker575 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45528"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Tincan Branch").flat(true);

	MarkerOptions marker576 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.429302"), Double
							.parseDouble("3.268421")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Trade Fair Branch").flat(true);

	MarkerOptions marker577 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.445259"), Double
							.parseDouble("3.371347")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Wharf Road Branch").flat(true);

	MarkerOptions marker578 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.479268"), Double
							.parseDouble("3.385473")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.sterlingbank))

			.title("Willoughby- Ebute-Metta Branch").flat(true);

	MarkerOptions marker579 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.61886"), Double
							.parseDouble("3.300407")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Abule Egba Branch").flat(true);

	MarkerOptions marker580 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.462428"), Double
							.parseDouble("3.38236")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Adeniji Adele Branch").flat(true);

	MarkerOptions marker581 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.634383"), Double
							.parseDouble("3.319783")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Agege Branch").flat(true);

	MarkerOptions marker582 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.524379"), Double
							.parseDouble("3.379206")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Ajao Estate Branch").flat(true);

	MarkerOptions marker583 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601621"), Double
							.parseDouble("3.351934")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Allen Branch").flat(true);

	MarkerOptions marker584 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.429302"), Double
							.parseDouble("3.268421")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Aspamda Branch").flat(true);

	MarkerOptions marker585 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444464"), Double
							.parseDouble("3.424082")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Awolowo Road Branch").flat(true);

	MarkerOptions marker586 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453539"), Double
							.parseDouble("3.38955")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Broad Street 1 Branch").flat(true);

	MarkerOptions marker587 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458713"), Double
							.parseDouble("3.389669")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Broad Street 2 Branch").flat(true);

	MarkerOptions marker588 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442615"), Double
							.parseDouble("3.373286")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Commercial Road Branch").flat(true);

	MarkerOptions marker589 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.617108"), Double
							.parseDouble("3.312788")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Dopemu Branch").flat(true);

	MarkerOptions marker590 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.483471"), Double
							.parseDouble("3.382513")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Ebute Metta Branch").flat(true);

	MarkerOptions marker591 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.591574"), Double
							.parseDouble("3.291062")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Egbeda Branch").flat(true);

	MarkerOptions marker592 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.43305"), Double
							.parseDouble("3.425816")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Idowu Taylor Branch").flat(true);

	MarkerOptions marker593 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601053"), Double
							.parseDouble("3.338636")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Ikeja Branch").flat(true);

	MarkerOptions marker594 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.522763"), Double
							.parseDouble("3.330044")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Isolo Branch").flat(true);

	MarkerOptions marker595 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.552406"), Double
							.parseDouble("3.36735")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Jibowu Branch").flat(true);

	MarkerOptions marker596 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.602276"), Double
							.parseDouble("3.344614")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Lagos Airport Hotel Branch").flat(true);

	MarkerOptions marker597 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.451397"), Double
							.parseDouble("3.396397")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Lapal Branch").flat(true);

	MarkerOptions marker598 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453539"), Double
							.parseDouble("3.389551")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Mamman Kontagora Branch").flat(true);

	MarkerOptions marker599 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.572514"), Double
							.parseDouble("3.365293")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Maryland Branch").flat(true);

	MarkerOptions marker600 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.627364"), Double
							.parseDouble("3.322609")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Mushin Branch").flat(true);
	
	googleMap.addMarker(marker301);
	googleMap.addMarker(marker302);
	googleMap.addMarker(marker303);
	googleMap.addMarker(marker304);
	googleMap.addMarker(marker305);
	googleMap.addMarker(marker306);
	googleMap.addMarker(marker307);
	googleMap.addMarker(marker308);
	googleMap.addMarker(marker309);
	googleMap.addMarker(marker310);
	googleMap.addMarker(marker311);
	googleMap.addMarker(marker312);
	googleMap.addMarker(marker313);
	googleMap.addMarker(marker314);
	googleMap.addMarker(marker315);
	googleMap.addMarker(marker316);
	googleMap.addMarker(marker317);
	googleMap.addMarker(marker318);
	googleMap.addMarker(marker319);
	googleMap.addMarker(marker320);
	googleMap.addMarker(marker321);
	googleMap.addMarker(marker322);
	googleMap.addMarker(marker323);
	googleMap.addMarker(marker324);
	googleMap.addMarker(marker325);
	googleMap.addMarker(marker326);
	googleMap.addMarker(marker327);
	googleMap.addMarker(marker328);
	googleMap.addMarker(marker329);
	googleMap.addMarker(marker330);
	googleMap.addMarker(marker331);
	googleMap.addMarker(marker332);
	googleMap.addMarker(marker333);
	googleMap.addMarker(marker334);
	googleMap.addMarker(marker335);
	googleMap.addMarker(marker336);
	googleMap.addMarker(marker337);
	googleMap.addMarker(marker338);
	googleMap.addMarker(marker339);
	googleMap.addMarker(marker340);
	googleMap.addMarker(marker341);
	googleMap.addMarker(marker342);
	googleMap.addMarker(marker343);
	googleMap.addMarker(marker344);
	googleMap.addMarker(marker345);
	googleMap.addMarker(marker346);
	googleMap.addMarker(marker347);
	googleMap.addMarker(marker348);
	googleMap.addMarker(marker349);
	googleMap.addMarker(marker350);
	googleMap.addMarker(marker351);
	googleMap.addMarker(marker352);
	googleMap.addMarker(marker353);
	googleMap.addMarker(marker354);
	googleMap.addMarker(marker355);
	googleMap.addMarker(marker356);
	googleMap.addMarker(marker357);
	googleMap.addMarker(marker358);
	googleMap.addMarker(marker359);
	googleMap.addMarker(marker360);
	googleMap.addMarker(marker361);
	googleMap.addMarker(marker362);
	googleMap.addMarker(marker363);
	googleMap.addMarker(marker364);
	googleMap.addMarker(marker365);
	googleMap.addMarker(marker366);
	googleMap.addMarker(marker367);
	googleMap.addMarker(marker368);
	googleMap.addMarker(marker369);
	googleMap.addMarker(marker370);
	googleMap.addMarker(marker371);
	googleMap.addMarker(marker372);
	googleMap.addMarker(marker373);
	googleMap.addMarker(marker374);
	googleMap.addMarker(marker375);
	googleMap.addMarker(marker376);
	googleMap.addMarker(marker377);
	googleMap.addMarker(marker378);
	googleMap.addMarker(marker379);
	googleMap.addMarker(marker380);
	googleMap.addMarker(marker381);
	googleMap.addMarker(marker382);
	googleMap.addMarker(marker383);
	googleMap.addMarker(marker384);
	googleMap.addMarker(marker385);
	googleMap.addMarker(marker386);
	googleMap.addMarker(marker387);
	googleMap.addMarker(marker388);
	googleMap.addMarker(marker389);
	googleMap.addMarker(marker390);
	googleMap.addMarker(marker391);
	googleMap.addMarker(marker392);
	googleMap.addMarker(marker393);
	googleMap.addMarker(marker394);
	googleMap.addMarker(marker395);
	googleMap.addMarker(marker396);
	googleMap.addMarker(marker397);
	googleMap.addMarker(marker398);
	googleMap.addMarker(marker399);
	googleMap.addMarker(marker400);
	googleMap.addMarker(marker401);
	googleMap.addMarker(marker402);
	googleMap.addMarker(marker403);
	googleMap.addMarker(marker404);
	googleMap.addMarker(marker405);
	googleMap.addMarker(marker406);
	googleMap.addMarker(marker407);
	googleMap.addMarker(marker408);
	googleMap.addMarker(marker409);
	googleMap.addMarker(marker410);
	googleMap.addMarker(marker411);
	googleMap.addMarker(marker412);
	googleMap.addMarker(marker413);
	googleMap.addMarker(marker414);
	googleMap.addMarker(marker415);
	googleMap.addMarker(marker416);
	googleMap.addMarker(marker417);
	googleMap.addMarker(marker418);
	googleMap.addMarker(marker419);
	googleMap.addMarker(marker420);
	googleMap.addMarker(marker421);
	googleMap.addMarker(marker422);
	googleMap.addMarker(marker423);
	googleMap.addMarker(marker424);
	googleMap.addMarker(marker425);
	googleMap.addMarker(marker426);
	googleMap.addMarker(marker427);
	googleMap.addMarker(marker428);
	googleMap.addMarker(marker429);
	googleMap.addMarker(marker430);
	googleMap.addMarker(marker431);
	googleMap.addMarker(marker432);
	googleMap.addMarker(marker433);
	googleMap.addMarker(marker434);
	googleMap.addMarker(marker435);
	googleMap.addMarker(marker436);
	googleMap.addMarker(marker437);
	googleMap.addMarker(marker438);
	googleMap.addMarker(marker439);
	googleMap.addMarker(marker440);
	googleMap.addMarker(marker441);
	googleMap.addMarker(marker442);
	googleMap.addMarker(marker443);
	googleMap.addMarker(marker444);
	googleMap.addMarker(marker445);
	googleMap.addMarker(marker446);
	googleMap.addMarker(marker447);
	googleMap.addMarker(marker448);
	googleMap.addMarker(marker449);
	googleMap.addMarker(marker450);
	googleMap.addMarker(marker451);
	googleMap.addMarker(marker452);
	googleMap.addMarker(marker453);
	googleMap.addMarker(marker454);
	googleMap.addMarker(marker455);
	googleMap.addMarker(marker456);
	googleMap.addMarker(marker457);
	googleMap.addMarker(marker458);
	googleMap.addMarker(marker459);
	googleMap.addMarker(marker460);
	googleMap.addMarker(marker461);
	googleMap.addMarker(marker462);
	googleMap.addMarker(marker463);
	googleMap.addMarker(marker464);
	googleMap.addMarker(marker465);
	googleMap.addMarker(marker466);
	googleMap.addMarker(marker467);
	googleMap.addMarker(marker468);
	googleMap.addMarker(marker469);
	googleMap.addMarker(marker470);
	googleMap.addMarker(marker471);
	googleMap.addMarker(marker472);
	googleMap.addMarker(marker473);
	googleMap.addMarker(marker474);
	googleMap.addMarker(marker475);
	googleMap.addMarker(marker476);
	googleMap.addMarker(marker477);
	googleMap.addMarker(marker478);
	googleMap.addMarker(marker479);
	googleMap.addMarker(marker480);
	googleMap.addMarker(marker481);
	googleMap.addMarker(marker482);
	googleMap.addMarker(marker483);
	googleMap.addMarker(marker484);
	googleMap.addMarker(marker485);
	googleMap.addMarker(marker486);
	googleMap.addMarker(marker487);
	googleMap.addMarker(marker488);
	googleMap.addMarker(marker489);
	googleMap.addMarker(marker490);
	googleMap.addMarker(marker491);
	googleMap.addMarker(marker492);
	googleMap.addMarker(marker493);
	googleMap.addMarker(marker494);
	googleMap.addMarker(marker495);
	googleMap.addMarker(marker496);
	googleMap.addMarker(marker497);
	googleMap.addMarker(marker498);
	googleMap.addMarker(marker499);
	googleMap.addMarker(marker500);
	googleMap.addMarker(marker501);
	googleMap.addMarker(marker502);
	googleMap.addMarker(marker503);
	googleMap.addMarker(marker504);
	googleMap.addMarker(marker505);
	googleMap.addMarker(marker506);
	googleMap.addMarker(marker507);
	googleMap.addMarker(marker508);
	googleMap.addMarker(marker509);
	googleMap.addMarker(marker510);
	googleMap.addMarker(marker511);
	googleMap.addMarker(marker512);
	googleMap.addMarker(marker513);
	googleMap.addMarker(marker514);
	googleMap.addMarker(marker515);
	googleMap.addMarker(marker516);
	googleMap.addMarker(marker517);
	googleMap.addMarker(marker518);
	googleMap.addMarker(marker519);
	googleMap.addMarker(marker520);
	googleMap.addMarker(marker521);
	googleMap.addMarker(marker522);
	googleMap.addMarker(marker523);
	googleMap.addMarker(marker524);
	googleMap.addMarker(marker525);
	googleMap.addMarker(marker526);
	googleMap.addMarker(marker527);
	googleMap.addMarker(marker528);
	googleMap.addMarker(marker529);
	googleMap.addMarker(marker530);
	googleMap.addMarker(marker531);
	googleMap.addMarker(marker532);
	googleMap.addMarker(marker533);
	googleMap.addMarker(marker534);
	googleMap.addMarker(marker535);
	googleMap.addMarker(marker536);
	googleMap.addMarker(marker537);
	googleMap.addMarker(marker538);
	googleMap.addMarker(marker539);
	googleMap.addMarker(marker540);
	googleMap.addMarker(marker541);
	googleMap.addMarker(marker542);
	googleMap.addMarker(marker543);
	googleMap.addMarker(marker544);
	googleMap.addMarker(marker545);
	googleMap.addMarker(marker546);
	googleMap.addMarker(marker547);
	googleMap.addMarker(marker548);
	googleMap.addMarker(marker549);
	googleMap.addMarker(marker550);
	googleMap.addMarker(marker551);
	googleMap.addMarker(marker552);
	googleMap.addMarker(marker553);
	googleMap.addMarker(marker554);
	googleMap.addMarker(marker555);
	googleMap.addMarker(marker556);
	googleMap.addMarker(marker557);
	googleMap.addMarker(marker558);
	googleMap.addMarker(marker559);
	googleMap.addMarker(marker560);
	googleMap.addMarker(marker561);
	googleMap.addMarker(marker562);
	googleMap.addMarker(marker563);
	googleMap.addMarker(marker564);
	googleMap.addMarker(marker565);
	googleMap.addMarker(marker566);
	googleMap.addMarker(marker567);
	googleMap.addMarker(marker568);
	googleMap.addMarker(marker569);
	googleMap.addMarker(marker570);
	googleMap.addMarker(marker571);
	googleMap.addMarker(marker572);
	googleMap.addMarker(marker573);
	googleMap.addMarker(marker574);
	googleMap.addMarker(marker575);
	googleMap.addMarker(marker576);
	googleMap.addMarker(marker577);
	googleMap.addMarker(marker578);
	googleMap.addMarker(marker579);
	googleMap.addMarker(marker580);
	googleMap.addMarker(marker581);
	googleMap.addMarker(marker582);
	googleMap.addMarker(marker583);
	googleMap.addMarker(marker584);
	googleMap.addMarker(marker585);
	googleMap.addMarker(marker586);
	googleMap.addMarker(marker587);
	googleMap.addMarker(marker588);
	googleMap.addMarker(marker589);
	googleMap.addMarker(marker590);
	googleMap.addMarker(marker591);
	googleMap.addMarker(marker592);
	googleMap.addMarker(marker593);
	googleMap.addMarker(marker594);
	googleMap.addMarker(marker595);
	googleMap.addMarker(marker596);
	googleMap.addMarker(marker597);
	googleMap.addMarker(marker598);
	googleMap.addMarker(marker599);
	googleMap.addMarker(marker600);
		}

		private void bankMarkerThree(){
			MarkerOptions marker601 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.577359"), Double
							.parseDouble("3.333956")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Nahco Branch").flat(true);

	MarkerOptions marker602 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45528"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("NPA Branch").flat(true);

	MarkerOptions marker603 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601053"), Double
							.parseDouble("3.338636")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Oba Akran Branch").flat(true);

	MarkerOptions marker604 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601838"), Double
							.parseDouble("3.351486")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Ogba Branch").flat(true);

	MarkerOptions marker605 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.583348"), Double
							.parseDouble("3.380486")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Ojota Branch").flat(true);

	MarkerOptions marker606 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.429044"), Double
							.parseDouble("3.46314")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Oniru Branch").flat(true);

	MarkerOptions marker607 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459993"), Double
							.parseDouble("3.230824")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Orile Iganmu Branch").flat(true);

	MarkerOptions marker608 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.562183"), Double
							.parseDouble("3.349129")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Oshodi Branch").flat(true);

	MarkerOptions marker609 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.456087"), Double
							.parseDouble("3.388204")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Tinubu Branch").flat(true);

	MarkerOptions marker610 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.517568"), Double
							.parseDouble("3.385567")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Unilag Branch").flat(true);

	MarkerOptions marker611 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45528"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Warehouse Road Branch").flat(true);

	MarkerOptions marker612 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.454928"), Double
							.parseDouble("3.383926")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.wemabank))

			.title("Head Office").flat(true);

	MarkerOptions marker613 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6141676"), Double
							.parseDouble("3.3397747")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Adeniyi Jones Branch").flat(true);

	MarkerOptions marker614 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430251"), Double
							.parseDouble("3.417994")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Adeola Odeku Branch").flat(true);

	MarkerOptions marker615 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5449299"), Double
							.parseDouble("3.3299973")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Ajao Estate Branch").flat(true);

	MarkerOptions marker616 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.546308"), Double
							.parseDouble("3.325172")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Alaba International Market(2) Branch").flat(true);

	MarkerOptions marker617 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4592261"), Double
							.parseDouble("3.1869181")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Alaba International Market Branch").flat(true);

	MarkerOptions marker618 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6247007"), Double
							.parseDouble("3.3508324")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Alausa Branch").flat(true);

	MarkerOptions marker619 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.610069"), Double
							.parseDouble("3.289122")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Alimosho - Iyana Ipaja Branch").flat(true);

	MarkerOptions marker620 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460603"), Double
							.parseDouble("3.250380")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Atiku Abubakar Plaza Branch").flat(true);

	MarkerOptions marker621 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.617266"), Double
							.parseDouble("3.361942")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Awolowo Ikeja Branch").flat(true);

	MarkerOptions marker622 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442244"), Double
							.parseDouble("3.419196")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Awolowo Road Branch").flat(true);

	MarkerOptions marker623 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.440208"), Double
							.parseDouble("3.3495853")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Badagry Expressway Branch").flat(true);

	MarkerOptions marker624 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4591748"), Double
							.parseDouble("3.2490443")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Bank Plaza Branch").flat(true);

	MarkerOptions marker625 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.490126"), Double
							.parseDouble("3.354734")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Bode Thomas Branch").flat(true);

	MarkerOptions marker626 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.43815"), Double
							.parseDouble("3.368106")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Burma Road Branch").flat(true);

	MarkerOptions marker627 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4598084"), Double
							.parseDouble("3.216476")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Coker Street, Badagry Expressway Branch")
			.flat(true);

	MarkerOptions marker628 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.642300"), Double
							.parseDouble("3.329095")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("College Road Branch").flat(true);

	MarkerOptions marker629 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.437673"), Double
							.parseDouble("3.372437")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Creek Road Branch").flat(true);

	MarkerOptions marker630 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.503281"), Double
							.parseDouble("3.3531145")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Daleko Market Branch").flat(true);

	MarkerOptions marker631 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.608194"), Double
							.parseDouble("3.314786")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Dopemu (Aluminium Village) Branch").flat(true);

	MarkerOptions marker632 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.462683"), Double
							.parseDouble("3.389359")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Enu Owa Branch").flat(true);

	MarkerOptions marker633 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4759598"), Double
							.parseDouble("3.2777372")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Festac Mini Branch").flat(true);

	// contd from here for heritage bank
	MarkerOptions marker634 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4621536"), Double
							.parseDouble("3.283682")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Festac Branch").flat(true);

	MarkerOptions marker635 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.552584"), Double
							.parseDouble("3.391345")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Gbagada/Ifako Branch").flat(true);

	MarkerOptions marker636 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4375266"), Double
							.parseDouble("3.4256677")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Head Office Branch").flat(true);

	MarkerOptions marker637 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.499737"), Double
							.parseDouble("3.378419")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Herbert Macaulay Branch").flat(true);

	MarkerOptions marker638 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.550559"), Double
							.parseDouble("3.268388")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Idimu Road Branch").flat(true);

	MarkerOptions marker639 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458846"), Double
							.parseDouble("3.389710")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Idumagbo Road Branch").flat(true);

	MarkerOptions marker640 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.633040"), Double
							.parseDouble("3.341531")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Ijaiye Road Branch").flat(true);

	MarkerOptions marker641 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.624305"), Double
							.parseDouble("3.494602")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Ikorodu-Lagos Road Branch").flat(true);

	MarkerOptions marker642 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4639538"), Double
							.parseDouble("3.5544102")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Ikota Retail Branch").flat(true);

	MarkerOptions marker643 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5455577"), Double
							.parseDouble("3.3577745")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Ilupeju Branch").flat(true);

	MarkerOptions marker644 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.632787"), Double
							.parseDouble("3.351322")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Isheri Road Branch").flat(true);

	MarkerOptions marker645 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.619982"), Double
							.parseDouble("3.300588")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Iyana Ipaja Branch").flat(true);
	MarkerOptions marker646 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.520983"), Double
							.parseDouble("3.367283")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Jibowu Branch").flat(true);

	MarkerOptions marker647 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6035667"), Double
							.parseDouble("3.4019385")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Ketu Branch").flat(true);

	MarkerOptions marker648 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453559"), Double
							.parseDouble("3.389415")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Lagos Island Broad Street Branch").flat(true);

	MarkerOptions marker649 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4537191"), Double
							.parseDouble("3.3874838")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Lagos Island Idumota Branch").flat(true);

	MarkerOptions marker650 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4625675"), Double
							.parseDouble("3.3870282")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Lagos Island Branch").flat(true);

	MarkerOptions marker651 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.511148"), Double
							.parseDouble("3.339403")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Lawanson Branch").flat(true);

	MarkerOptions marker652 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.438254"), Double
							.parseDouble("3.515492")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Lekki Branch").flat(true);

	MarkerOptions marker653 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4485289"), Double
							.parseDouble("3.3648525")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Liverpool Road Branch").flat(true);

	MarkerOptions marker654 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.553495"), Double
							.parseDouble("3.331337")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Mafoluku Branch").flat(true);

	MarkerOptions marker655 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.450347"), Double
							.parseDouble("3.391594")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Marina Branch").flat(true);

	MarkerOptions marker656 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.537044"), Double
							.parseDouble("3.346589")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Matori Branch").flat(true);

	MarkerOptions marker657 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458714"), Double
							.parseDouble("3.298797")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Maza Maza Branch").flat(true);

	MarkerOptions marker658 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.545201"), Double
							.parseDouble("3.333265")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Mushin Road Branch").flat(true);

	MarkerOptions marker659 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5683959"), Double
							.parseDouble("3.3175024")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("NAHCO Shed Branch").flat(true);

	MarkerOptions marker660 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.506417"), Double
							.parseDouble("3.352473")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Ogunlana Branch").flat(true);

	MarkerOptions marker661 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.627364"), Double
							.parseDouble("3.322609")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Ojuwoye Market Branch").flat(true);

	MarkerOptions marker662 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.538753"), Double
							.parseDouble("3.359821")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Oke Arin Branch").flat(true);

	MarkerOptions marker663 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.502314"), Double
							.parseDouble("3.304878")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Okota Retail Branch").flat(true);

	MarkerOptions marker664 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5294667"), Double
							.parseDouble("3.3513182")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Olorunsogo Branch").flat(true);

	MarkerOptions marker665 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.446035"), Double
							.parseDouble("3.405693")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Onikan Branch").flat(true);

	MarkerOptions marker666 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.589033"), Double
							.parseDouble("3.361554")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Opebi Street(2) Branch").flat(true);

	MarkerOptions marker667 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.588472"), Double
							.parseDouble("3.361944")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Opebi Street Branch").flat(true);

	MarkerOptions marker668 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5997994"), Double
							.parseDouble("3.3684121")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Oregun Branch").flat(true);

	MarkerOptions marker669 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.440065"), Double
							.parseDouble("3.3329505")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Oshodi Expressway Branch").flat(true);

	MarkerOptions marker670 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4805951"), Double
							.parseDouble("3.3838963")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Oyingbo Branch").flat(true);

	MarkerOptions marker671 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4327968"), Double
							.parseDouble("3.3429999")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Roro Port Branch").flat(true);

	MarkerOptions marker672 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4587507"), Double
							.parseDouble("3.3034635")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Satellite Town Mini Branch").flat(true);

	MarkerOptions marker673 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.3803005"), Double
							.parseDouble("2.7070355")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Seme Border Branch").flat(true);

	MarkerOptions marker674 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.529489"), Double
							.parseDouble("3.3849603")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("St. Finbarrs Road, Bariga Branch").flat(true);

	MarkerOptions marker675 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.508661"), Double
							.parseDouble("3.368150")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Tejuosho Retail Branch").flat(true);

	MarkerOptions marker676 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.436440"), Double
							.parseDouble("3.451316")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("The Palms Shopping Complex Branch").flat(true);

	MarkerOptions marker677 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4424505"), Double
							.parseDouble("3.3712765")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.diamondbank))

			.title("Wharf Road Branch").flat(true);

	MarkerOptions marker740 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.427537"), Double
							.parseDouble("3.409279")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ahmadu Bello Branch").flat(true);

	MarkerOptions marker741 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.564497"), Double
							.parseDouble("3.322811")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Airport Road Branch").flat(true);

	MarkerOptions marker742 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5243793"), Double
							.parseDouble("3.377017")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Alaba - Agudosi Branch").flat(true);

	MarkerOptions marker743 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4592261"), Double
							.parseDouble("3.1869181")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Alaba-Ojo Igbede Branch").flat(true);

	MarkerOptions marker744 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5243793"), Double
							.parseDouble("3.377017")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Alaba-St Patricks Junction Branch").flat(true);

	MarkerOptions marker745 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4405854"), Double
							.parseDouble("3.3749239")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Apapa - Creek Road Branch").flat(true);
	MarkerOptions marker746 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4452751"), Double
							.parseDouble("3.3706176")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Apapa - Wharf Road Branch").flat(true);

	MarkerOptions marker747 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5243793"), Double
							.parseDouble("3.377017")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Apapa-Warehouse Road 2 Branch").flat(true);

	MarkerOptions marker748 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.449999"), Double
							.parseDouble("3.3644773")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Apapa-Warehouse Road Branch").flat(true);

	MarkerOptions marker749 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4499942"), Double
							.parseDouble("3.296625")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Bba Branch").flat(true);

	MarkerOptions marker750 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4535761"), Double
							.parseDouble("3.3872332")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Broad Street 2 Branch").flat(true);

	MarkerOptions marker751 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4533611"), Double
							.parseDouble("3.3874713")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Broad Street Branch").flat(true);

	MarkerOptions marker752 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.449621"), Double
							.parseDouble("3.5285153")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Chevron Branch").flat(true);

	MarkerOptions marker753 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460848"), Double
							.parseDouble("3.258734")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Coker Branch").flat(true);

	MarkerOptions marker754 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.451209"), Double
							.parseDouble("3.4202362")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Daleko Branch").flat(true);

	MarkerOptions marker755 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.596245"), Double
							.parseDouble("3.3382763")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Eleganza Branch").flat(true);

	MarkerOptions marker756 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.462154"), Double
							.parseDouble("3.285871")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Festac Branch").flat(true);

	MarkerOptions marker757 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5616377"), Double
							.parseDouble("3.3820586")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Gbagada Branch").flat(true);

	MarkerOptions marker758 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.495039"), Double
							.parseDouble("3.380811")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Herbert Macaulay-Sabo Branch").flat(true);

	MarkerOptions marker759 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4622005"), Double
							.parseDouble("3.3870828")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Idumagbo Branch").flat(true);

	MarkerOptions marker760 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4624943"), Double
							.parseDouble("3.3885628")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Idumota - Enu-Owa Branch").flat(true);

	MarkerOptions marker761 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4633854"), Double
							.parseDouble("3.3856868")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Idumota-Ashogbon Branch").flat(true);

	MarkerOptions marker762 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4596883"), Double
							.parseDouble("3.3853342")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Idumota- Nnamdi Azikwe Branch").flat(true);

	MarkerOptions marker763 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.59609"), Double
							.parseDouble("3.3530113")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ikeja - Allen Avenue 2 Branch").flat(true);

	MarkerOptions marker764 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6044331"), Double
							.parseDouble("3.3485431")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ikeja - Allen Avenue Branch").flat(true);

	MarkerOptions marker765 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6326615"), Double
							.parseDouble("3.3391142")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ikeja - Ogba Branch").flat(true);

	MarkerOptions marker766 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5243793"), Double
							.parseDouble("3.377017")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ikeja- Intl Airport Branch").flat(true);

	MarkerOptions marker767 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.618717"), Double
							.parseDouble("3.345765")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ikeja-Adeniyi Jones Branch").flat(true);

	MarkerOptions marker768 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.585291"), Double
							.parseDouble("3.351723")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ikeja-Gra Branch").flat(true);

	MarkerOptions marker769 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.56244"), Double
							.parseDouble("3.3648213")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ikorodu Road Branch").flat(true);

	MarkerOptions marker770 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601573"), Double
							.parseDouble("3.388936")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ikorodu Road Ketu Branch").flat(true);

	MarkerOptions marker771 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4639538"), Double
							.parseDouble("3.5544102")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ikota Branch").flat(true);

	MarkerOptions marker772 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.441971"), Double
							.parseDouble("3.418322")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ikoyi - Awolowo Road Branch").flat(true);

	MarkerOptions marker773 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.553648"), Double
							.parseDouble("3.356674")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ilupeju Branch").flat(true);

	MarkerOptions marker774 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.46918"), Double
							.parseDouble("3.2409313")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Intnl Trade Fair-Aspamda Branch").flat(true);

	MarkerOptions marker775 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6326615"), Double
							.parseDouble("3.3391142")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Isheri Branch").flat(true);

	MarkerOptions marker776 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.521598"), Double
							.parseDouble("3.322123")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Isolo - Ire Akari Branch").flat(true);

	MarkerOptions marker777 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.515840"), Double
							.parseDouble("3.319866")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Isolo - Oke Afa Road Branch").flat(true);

	MarkerOptions marker778 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5243793"), Double
							.parseDouble("3.377017")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ladipo Branch").flat(true);

	MarkerOptions marker779 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.539393"), Double
							.parseDouble("3.345083")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Matori-Ladipo Street Branch").flat(true);

	MarkerOptions marker780 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459011"), Double
							.parseDouble("3.304256")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Maza Maza Branch").flat(true);

	MarkerOptions marker781 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.627364"), Double
							.parseDouble("3.322609")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Mushin-Idi Oro Branch").flat(true);

	MarkerOptions marker782 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601053"), Double
							.parseDouble("3.338636")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Oba Akran 1  Branch").flat(true);

	MarkerOptions marker783 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.571095"), Double
							.parseDouble("3.396020")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ogudu Branch").flat(true);

	MarkerOptions marker784 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.508738"), Double
							.parseDouble("3.368106")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Ojuelegba Branch").flat(true);

	MarkerOptions marker785 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45989"), Double
							.parseDouble("3.3828913")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Oke-Arin Branch").flat(true);

	MarkerOptions marker786 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.437592"), Double
							.parseDouble("3.357608")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Olodi Apapa Branch").flat(true);

	MarkerOptions marker787 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4598084"), Double
							.parseDouble("3.216476")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Orile Branch").flat(true);

	MarkerOptions marker788 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.498510"), Double
							.parseDouble("3.378995")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Oyingbo Branch").flat(true);

	MarkerOptions marker789 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.450192"), Double
							.parseDouble("3.3613066")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Point Road Apapa Branch").flat(true);

	MarkerOptions marker790 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4478905"), Double
							.parseDouble("3.2545262")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Seme Border Branch").flat(true);

	MarkerOptions marker791 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5243793"), Double
							.parseDouble("3.377017")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("St Patrick Branch").flat(true);

	MarkerOptions marker792 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.491330"), Double
							.parseDouble("3.356570")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Surulere Branch").flat(true);

	MarkerOptions marker793 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.497366"), Double
							.parseDouble("3.355607")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Tbs Branch").flat(true);

	MarkerOptions marker794 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5222786"), Double
							.parseDouble("3.3831812")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("Unilag Branch").flat(true);

	MarkerOptions marker795 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430235"), Double
							.parseDouble("3.409348")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("VI - Adeola Odeku Branch").flat(true);

	MarkerOptions marker796 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.434338"), Double
							.parseDouble("3.431110")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("VI - Bishop Aboyade Cole Branch").flat(true);

	MarkerOptions marker797 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.432266"), Double
							.parseDouble("3.440493")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("VI - Ligali Ayorinde Branch").flat(true);

	MarkerOptions marker798 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.429553"), Double
							.parseDouble("3.4374949")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("VI-Ajose Adeogun Branch").flat(true);

	MarkerOptions marker799 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431605"), Double
							.parseDouble("3.423955")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("VI-Akin Adesola Branch").flat(true);

	MarkerOptions marker800 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4295529"), Double
							.parseDouble("3.4309288")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.ecobank))

			.title("VI-Oyin Jolayemi Branch").flat(true);

	MarkerOptions marker801 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.631205"), Double
							.parseDouble("3.321252")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Abule Egba Branch").flat(true);

	MarkerOptions marker802 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.626694"), Double
							.parseDouble("3.34729")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Acme Road Branch").flat(true);

	MarkerOptions marker803 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.49454"), Double
							.parseDouble("3.357266")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Adeniran Ogunsanya Branch").flat(true);

	MarkerOptions marker804 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.613956"), Double
							.parseDouble("3.3437596")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Adeniyi Jones Branch").flat(true);

	MarkerOptions marker805 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4322078"), Double
							.parseDouble("3.4172476")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Adeola Odeku Branch").flat(true);

	MarkerOptions marker806 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.426339"), Double
							.parseDouble("3.430141")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Adetokunbo Ademola Branch").flat(true);

	MarkerOptions marker807 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430899"), Double
							.parseDouble("3.425151")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Adeyemo Alakija Branch").flat(true);

	MarkerOptions marker808 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.631163"), Double
							.parseDouble("3.32147")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Agege Motor Road Branch").flat(true);

	MarkerOptions marker809 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.618848"), Double
							.parseDouble("3.3451772")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Agidingbi Branch").flat(true);

	MarkerOptions marker810 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.496249"), Double
							.parseDouble("3.340090")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Aguda Branch").flat(true);

	MarkerOptions marker811 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431642"), Double
							.parseDouble("3.424192")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Akin Adesola Branch").flat(true);

	MarkerOptions marker812 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600783"), Double
							.parseDouble("3.309706")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Akowonjo Branch").flat(true);

	MarkerOptions marker813 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458343"), Double
							.parseDouble("3.192084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Alaba International Branch").flat(true);

	MarkerOptions marker814 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.659350"), Double
							.parseDouble("3.292328")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Alagbado Branch").flat(true);

	MarkerOptions marker815 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.497988"), Double
							.parseDouble("3.343929")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Alaka Branch").flat(true);

	MarkerOptions marker816 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6157002"), Double
							.parseDouble("3.3616816")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Alausa Branch").flat(true);

	MarkerOptions marker817 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6023364"), Double
							.parseDouble("3.3496158")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Allen 2 Branch").flat(true);

	MarkerOptions marker818 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5985698"), Double
							.parseDouble("3.3515707")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Allen Branch").flat(true);

	MarkerOptions marker819 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6079978"), Double
							.parseDouble("3.3080864")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Aluminium Village Branch").flat(true);

	MarkerOptions marker820 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.449999"), Double
							.parseDouble("3.2644773")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Amuwo Odofin Branch").flat(true);

	MarkerOptions marker821 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5593403"), Double
							.parseDouble("3.3667964")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Anthony Village Branch").flat(true);

	MarkerOptions marker822 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4395353"), Double
							.parseDouble("3.3667588")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Apapa (Warehouse Road) Branch").flat(true);

	MarkerOptions marker823 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4423343"), Double
							.parseDouble("3.3741953")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Apapa Road Branch").flat(true);

	MarkerOptions marker824 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.46918"), Double
							.parseDouble("3.2409313")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Aspamda Branch").flat(true);

	MarkerOptions marker825 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6172768"), Double
							.parseDouble("3.3597688")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Awolowo Way Branch").flat(true);

	MarkerOptions marker826 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.417948"), Double
							.parseDouble("2.8796518")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Badagry Branch").flat(true);

	MarkerOptions marker827 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4345961"), Double
							.parseDouble("3.4227223")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Bar Beach Branch").flat(true);

	MarkerOptions marker828 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.562177"), Double
							.parseDouble("3.349131")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Bolade Oshodi Branch").flat(true);

	MarkerOptions marker829 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4436168"), Double
							.parseDouble("3.4269045")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Bourdillion Branch").flat(true);

	MarkerOptions marker830 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4562323"), Double
							.parseDouble("3.3806404")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Broad Street Branch").flat(true);

	MarkerOptions marker831 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4509322"), Double
							.parseDouble("3.3935254")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Catholic Mission Branch").flat(true);

	MarkerOptions marker832 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.437592"), Double
							.parseDouble("3.357608")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Charity Oshodi Branch").flat(true);

	MarkerOptions marker833 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.46947"), Double
							.parseDouble("3.3287113")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Coker Branch").flat(true);

	MarkerOptions marker834 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.593152"), Double
							.parseDouble("3.372016")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Computer Village Branch").flat(true);

	MarkerOptions marker835 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.439160"), Double
							.parseDouble("3.375242")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Creek Road Branch").flat(true);

	MarkerOptions marker836 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6093474"), Double
							.parseDouble("3.305496")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Dopemu Branch").flat(true);

	MarkerOptions marker837 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.461949"), Double
							.parseDouble("3.383831")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ebute-Ero Branch").flat(true);

	MarkerOptions marker838 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5350041"), Double
							.parseDouble("3.3038793")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ejigbo Branch").flat(true);

	MarkerOptions marker839 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5820936"), Double
							.parseDouble("3.9740334")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Epe Branch").flat(true);

	MarkerOptions marker840 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.481813"), Double
							.parseDouble("3.358966")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Eric-Moore Branch").flat(true);

	MarkerOptions marker841 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.443729"), Double
							.parseDouble("3.422630")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Falomo Round-about Branch").flat(true);

	MarkerOptions marker842 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.443751"), Double
							.parseDouble("3.422661")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Falomo Branch").flat(true);

	MarkerOptions marker843 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.466445"), Double
							.parseDouble("3.283514")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Festac Branch").flat(true);

	MarkerOptions marker844 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.550587"), Double
							.parseDouble("3.394398")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Gbagada 2 Branch").flat(true);

	MarkerOptions marker845 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.552657"), Double
							.parseDouble("3.391425")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Gbagada Branch").flat(true);
	MarkerOptions marker846 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430981"), Double
							.parseDouble("3.430790")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Head Office Annex 2 Branch").flat(true);

	MarkerOptions marker847 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430327"), Double
							.parseDouble("3.439700")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Head Office Branch").flat(true);

	MarkerOptions marker848 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.495071"), Double
							.parseDouble("3.380799")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Herbert Macaulay Branch").flat(true);

	MarkerOptions marker849 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.492398"), Double
							.parseDouble("3.381951")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Herbert Macaulay 2 Branch").flat(true);

	MarkerOptions marker850 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.587109"), Double
							.parseDouble("3.286114")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Idimu Branch").flat(true);

	MarkerOptions marker851 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.527188"), Double
							.parseDouble("3.354507")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Idi-Oro Branch").flat(true);

	MarkerOptions marker852 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.462684"), Double
							.parseDouble("3.389359")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Idumagbo Branch").flat(true);

	MarkerOptions marker853 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459624"), Double
							.parseDouble("3.386802")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Idumota Branch").flat(true);

	MarkerOptions marker854 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.627886"), Double
							.parseDouble("3.331713")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Iju Branch").flat(true);

	MarkerOptions marker855 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601053"), Double
							.parseDouble("3.338636")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ikeja (Oba Akran) Branch").flat(true);

	MarkerOptions marker856 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.577182"), Double
							.parseDouble("3.355327")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ikeja Gra Branch").flat(true);

	MarkerOptions marker857 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.534135"), Double
							.parseDouble("3.367835")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ikorodu Road Branch").flat(true);

	MarkerOptions marker858 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.622457"), Double
							.parseDouble("3.498771")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ikorodu Branch").flat(true);

	MarkerOptions marker859 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4639538"), Double
							.parseDouble("3.5544102")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ikota Shopping Complex Branch").flat(true);

	MarkerOptions marker860 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.43833"), Double
							.parseDouble("3.5145313")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ikota Branch").flat(true);

	MarkerOptions marker861 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.550560"), Double
							.parseDouble("3.268389")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ikotun Branch").flat(true);

	MarkerOptions marker862 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.441982"), Double
							.parseDouble("3.418491")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ikoyi Branch").flat(true);

	MarkerOptions marker863 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.570721"), Double
							.parseDouble("3.372329")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ilupeju 2 Branch").flat(true);

	MarkerOptions marker864 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.554823"), Double
							.parseDouble("3.354141")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ilupeju Branch").flat(true);

	MarkerOptions marker865 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5818185"), Double
							.parseDouble("3.3189461")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("International Airport (Terminal) Branch")
			.flat(true);

	MarkerOptions marker866 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6489168"), Double
							.parseDouble("3.3029958")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ipaja (Oke-Odo) Branch").flat(true);

	MarkerOptions marker867 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.551528"), Double
							.parseDouble("3.330687")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Isolo Branch").flat(true);

	MarkerOptions marker868 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.447103"), Double
							.parseDouble("3.414820")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Keffi Branch").flat(true);

	MarkerOptions marker869 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.604336"), Double
							.parseDouble("3.390854")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ketu Branch").flat(true);

	MarkerOptions marker870 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.452716"), Double
							.parseDouble("3.432365")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Kingsway Branch").flat(true);

	MarkerOptions marker871 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4349954"), Double
							.parseDouble("3.4146728")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Kofo Abayomi Branch").flat(true);

	MarkerOptions marker872 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600111"), Double
							.parseDouble("3.364972")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Kudirat Abiola Branch").flat(true);

	MarkerOptions marker873 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6146283"), Double
							.parseDouble("3.3435764")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ladipo Oluwole Branch").flat(true);

	MarkerOptions marker874 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.450225"), Double
							.parseDouble("3.394831")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Lagos Central Branch").flat(true);

	MarkerOptions marker875 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.531302"), Double
							.parseDouble("3.334066")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Laspotech Branch").flat(true);

	MarkerOptions marker876 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4625174"), Double
							.parseDouble("3.2005352")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Lasu Branch").flat(true);

	MarkerOptions marker877 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.511148"), Double
							.parseDouble("3.339403")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Lawanson Branch").flat(true);

	MarkerOptions marker878 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431224"), Double
							.parseDouble("3.469051")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Lekki Exp Branch").flat(true);

	MarkerOptions marker879 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.679638"), Double
							.parseDouble("3.279789")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Lekki Branch").flat(true);

	MarkerOptions marker880 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.448735"), Double
							.parseDouble("3.367127")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Liverpool Branch").flat(true);

	MarkerOptions marker881 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.61189"), Double
							.parseDouble("3.3671593")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Magodo Branch").flat(true);

	MarkerOptions marker882 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.464573"), Double
							.parseDouble("3.388349")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Marina 2  Branch").flat(true);

	MarkerOptions marker883 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453503"), Double
							.parseDouble("3.385663")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Marina Branch").flat(true);

	MarkerOptions marker884 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.440543"), Double
							.parseDouble("3.358876")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Marine Road Branch").flat(true);

	MarkerOptions marker885 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.539437"), Double
							.parseDouble("3.3428737")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Matori Branch").flat(true);

	MarkerOptions marker886 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.595759"), Double
							.parseDouble("3.340095")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Medical Road Branch").flat(true);

	MarkerOptions marker887 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455097"), Double
							.parseDouble("3.356763")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Mobil Rd Branch").flat(true);

	MarkerOptions marker888 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.448331"), Double
							.parseDouble("3.405916")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Moloney Branch").flat(true);

	MarkerOptions marker889 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431965"), Double
							.parseDouble("3.431930")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Muri Okonola Branch").flat(true);

	MarkerOptions marker890 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6308684"), Double
							.parseDouble("3.3379769")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ogba Branch").flat(true);

	MarkerOptions marker891 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.628888"), Double
							.parseDouble("3.337987")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ogba 2 Branch").flat(true);

	MarkerOptions marker892 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.581701"), Double
							.parseDouble("3.382500")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ogudu Branch").flat(true);

	MarkerOptions marker893 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.503890"), Double
							.parseDouble("3.351442")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ogunlana Drive Branch").flat(true);

	MarkerOptions marker894 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.64021"), Double
							.parseDouble("3.3639713")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ojodu Branch").flat(true);

	MarkerOptions marker895 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458868"), Double
							.parseDouble("3.383543")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Okearin 2 Branch").flat(true);

	MarkerOptions marker896 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.554166"), Double
							.parseDouble("3.393732")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Oke-Arin Branch").flat(true);

	MarkerOptions marker897 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.515218"), Double
							.parseDouble("3.320300")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Okota Branch").flat(true);

	MarkerOptions marker898 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458962"), Double
							.parseDouble("3.301448")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Old Ojo Road Branch").flat(true);

	MarkerOptions marker899 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.427624"), Double
							.parseDouble("3.429145")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Olosa Branch").flat(true);

	MarkerOptions marker900 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.596484"), Double
							.parseDouble("3.345167")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Olowu Branch").flat(true);
	
	googleMap.addMarker(marker601);
	googleMap.addMarker(marker602);
	googleMap.addMarker(marker603);
	googleMap.addMarker(marker604);
	googleMap.addMarker(marker605);
	googleMap.addMarker(marker606);
	googleMap.addMarker(marker607);
	googleMap.addMarker(marker608);
	googleMap.addMarker(marker609);
	googleMap.addMarker(marker610);
	googleMap.addMarker(marker611);
	googleMap.addMarker(marker612);
	googleMap.addMarker(marker613);
	googleMap.addMarker(marker614);
	googleMap.addMarker(marker615);
	googleMap.addMarker(marker616);
	googleMap.addMarker(marker617);
	googleMap.addMarker(marker618);
	googleMap.addMarker(marker619);
	googleMap.addMarker(marker620);
	googleMap.addMarker(marker621);
	googleMap.addMarker(marker622);
	googleMap.addMarker(marker623);
	googleMap.addMarker(marker624);
	googleMap.addMarker(marker625);
	googleMap.addMarker(marker626);
	googleMap.addMarker(marker627);
	googleMap.addMarker(marker628);
	googleMap.addMarker(marker629);
	googleMap.addMarker(marker630);
	googleMap.addMarker(marker631);
	googleMap.addMarker(marker632);
	googleMap.addMarker(marker633);
	googleMap.addMarker(marker634);
	googleMap.addMarker(marker635);
	googleMap.addMarker(marker636);
	googleMap.addMarker(marker637);
	googleMap.addMarker(marker638);
	googleMap.addMarker(marker639);
	googleMap.addMarker(marker640);
	googleMap.addMarker(marker641);
	googleMap.addMarker(marker642);
	googleMap.addMarker(marker643);
	googleMap.addMarker(marker644);
	googleMap.addMarker(marker645);
	googleMap.addMarker(marker646);
	googleMap.addMarker(marker647);
	googleMap.addMarker(marker648);
	googleMap.addMarker(marker649);
	googleMap.addMarker(marker650);
	googleMap.addMarker(marker651);
	googleMap.addMarker(marker652);
	googleMap.addMarker(marker653);
	googleMap.addMarker(marker654);
	googleMap.addMarker(marker655);
	googleMap.addMarker(marker656);
	googleMap.addMarker(marker657);
	googleMap.addMarker(marker658);
	googleMap.addMarker(marker659);
	googleMap.addMarker(marker660);
	googleMap.addMarker(marker661);
	googleMap.addMarker(marker662);
	googleMap.addMarker(marker663);
	googleMap.addMarker(marker664);
	googleMap.addMarker(marker665);
	googleMap.addMarker(marker666);
	googleMap.addMarker(marker667);
	googleMap.addMarker(marker668);
	googleMap.addMarker(marker669);
	googleMap.addMarker(marker670);
	googleMap.addMarker(marker671);
	googleMap.addMarker(marker672);
	googleMap.addMarker(marker673);
	googleMap.addMarker(marker674);
	googleMap.addMarker(marker675);
	googleMap.addMarker(marker676);
	googleMap.addMarker(marker677);
	googleMap.addMarker(marker740);
	googleMap.addMarker(marker741);
	googleMap.addMarker(marker742);
	googleMap.addMarker(marker743);
	googleMap.addMarker(marker744);
	googleMap.addMarker(marker745);
	googleMap.addMarker(marker746);
	googleMap.addMarker(marker747);
	googleMap.addMarker(marker748);
	googleMap.addMarker(marker749);
	googleMap.addMarker(marker750);
	googleMap.addMarker(marker751);
	googleMap.addMarker(marker752);
	googleMap.addMarker(marker753);
	googleMap.addMarker(marker754);
	googleMap.addMarker(marker755);
	googleMap.addMarker(marker756);
	googleMap.addMarker(marker757);
	googleMap.addMarker(marker758);
	googleMap.addMarker(marker759);
	googleMap.addMarker(marker760);
	googleMap.addMarker(marker761);
	googleMap.addMarker(marker762);
	googleMap.addMarker(marker763);
	googleMap.addMarker(marker764);
	googleMap.addMarker(marker765);
	googleMap.addMarker(marker766);
	googleMap.addMarker(marker767);
	googleMap.addMarker(marker768);
	googleMap.addMarker(marker769);
	googleMap.addMarker(marker770);
	googleMap.addMarker(marker771);
	googleMap.addMarker(marker772);
	googleMap.addMarker(marker773);
	googleMap.addMarker(marker774);
	googleMap.addMarker(marker775);
	googleMap.addMarker(marker776);
	googleMap.addMarker(marker777);
	googleMap.addMarker(marker778);
	googleMap.addMarker(marker779);
	googleMap.addMarker(marker780);
	googleMap.addMarker(marker781);
	googleMap.addMarker(marker782);
	googleMap.addMarker(marker783);
	googleMap.addMarker(marker784);
	googleMap.addMarker(marker785);
	googleMap.addMarker(marker786);
	googleMap.addMarker(marker787);
	googleMap.addMarker(marker788);
	googleMap.addMarker(marker789);
	googleMap.addMarker(marker790);
	googleMap.addMarker(marker791);
	googleMap.addMarker(marker792);
	googleMap.addMarker(marker793);
	googleMap.addMarker(marker794);
	googleMap.addMarker(marker795);
	googleMap.addMarker(marker796);
	googleMap.addMarker(marker797);
	googleMap.addMarker(marker798);
	googleMap.addMarker(marker799);
	googleMap.addMarker(marker800);
	googleMap.addMarker(marker801);
	googleMap.addMarker(marker802);
	googleMap.addMarker(marker803);
	googleMap.addMarker(marker804);
	googleMap.addMarker(marker805);
	googleMap.addMarker(marker806);
	googleMap.addMarker(marker807);
	googleMap.addMarker(marker808);
	googleMap.addMarker(marker809);
	googleMap.addMarker(marker810);
	googleMap.addMarker(marker811);
	googleMap.addMarker(marker812);
	googleMap.addMarker(marker813);
	googleMap.addMarker(marker814);
	googleMap.addMarker(marker815);
	googleMap.addMarker(marker816);
	googleMap.addMarker(marker817);
	googleMap.addMarker(marker818);
	googleMap.addMarker(marker819);
	googleMap.addMarker(marker820);
	googleMap.addMarker(marker821);
	googleMap.addMarker(marker822);
	googleMap.addMarker(marker823);
	googleMap.addMarker(marker824);
	googleMap.addMarker(marker825);
	googleMap.addMarker(marker826);
	googleMap.addMarker(marker827);
	googleMap.addMarker(marker828);
	googleMap.addMarker(marker829);
	googleMap.addMarker(marker830);
	googleMap.addMarker(marker831);
	googleMap.addMarker(marker832);
	googleMap.addMarker(marker833);
	googleMap.addMarker(marker834);
	googleMap.addMarker(marker835);
	googleMap.addMarker(marker836);
	googleMap.addMarker(marker837);
	googleMap.addMarker(marker838);
	googleMap.addMarker(marker839);
	googleMap.addMarker(marker840);
	googleMap.addMarker(marker841);
	googleMap.addMarker(marker842);
	googleMap.addMarker(marker843);
	googleMap.addMarker(marker844);
	googleMap.addMarker(marker845);
	googleMap.addMarker(marker846);
	googleMap.addMarker(marker847);
	googleMap.addMarker(marker848);
	googleMap.addMarker(marker849);
	googleMap.addMarker(marker850);
	googleMap.addMarker(marker851);
	googleMap.addMarker(marker852);
	googleMap.addMarker(marker853);
	googleMap.addMarker(marker854);
	googleMap.addMarker(marker855);
	googleMap.addMarker(marker856);
	googleMap.addMarker(marker857);
	googleMap.addMarker(marker858);
	googleMap.addMarker(marker859);
	googleMap.addMarker(marker860);
	googleMap.addMarker(marker861);
	googleMap.addMarker(marker862);
	googleMap.addMarker(marker863);
	googleMap.addMarker(marker864);
	googleMap.addMarker(marker865);
	googleMap.addMarker(marker866);
	googleMap.addMarker(marker867);
	googleMap.addMarker(marker868);
	googleMap.addMarker(marker869);
	googleMap.addMarker(marker870);
	googleMap.addMarker(marker871);
	googleMap.addMarker(marker872);
	googleMap.addMarker(marker873);
	googleMap.addMarker(marker874);
	googleMap.addMarker(marker875);
	googleMap.addMarker(marker876);
	googleMap.addMarker(marker877);
	googleMap.addMarker(marker878);
	googleMap.addMarker(marker879);
	googleMap.addMarker(marker880);
	googleMap.addMarker(marker881);
	googleMap.addMarker(marker882);
	googleMap.addMarker(marker883);
	googleMap.addMarker(marker884);
	googleMap.addMarker(marker885);
	googleMap.addMarker(marker886);
	googleMap.addMarker(marker887);
	googleMap.addMarker(marker888);
	googleMap.addMarker(marker889);
	googleMap.addMarker(marker890);
	googleMap.addMarker(marker891);
	googleMap.addMarker(marker892);
	googleMap.addMarker(marker893);
	googleMap.addMarker(marker894);
	googleMap.addMarker(marker895);
	googleMap.addMarker(marker896);
	googleMap.addMarker(marker897);
	googleMap.addMarker(marker898);
	googleMap.addMarker(marker899);
	googleMap.addMarker(marker900);
		}
		
		private void bankMarkerFour(){
			MarkerOptions marker901 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.586388"), Double
							.parseDouble("3.363701")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Opebi Branch").flat(true);

	MarkerOptions marker902 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6097874"), Double
							.parseDouble("3.3505178")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Oregun Branch").flat(true);

	MarkerOptions marker903 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.436120"), Double
							.parseDouble("3.416443")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ozumba Mbadiwe 2 Branch").flat(true);

	MarkerOptions marker904 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.437975"), Double
							.parseDouble("3.438117")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Ozumba Mbadiwe Branch").flat(true);

	MarkerOptions marker905 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5622275"), Double
							.parseDouble("3.3469411")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Palm Avenue Branch").flat(true);

	MarkerOptions marker906 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6426449"), Double
							.parseDouble("3.321242")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Pen Cinema Branch").flat(true);

	MarkerOptions marker907 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.427023"), Double
							.parseDouble("3.421211")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Saka Tinubu Branch").flat(true);

	MarkerOptions marker908 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.659350"), Double
							.parseDouble("3.292328")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Sango 2 Branch").flat(true);

	MarkerOptions marker909 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.681769"), Double
							.parseDouble("3.162099")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Sango Ota 3 Branch").flat(true);

	MarkerOptions marker910 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.702393"), Double
							.parseDouble("3.252543")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Sango Ota Branch").flat(true);

	MarkerOptions marker911 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4308995"), Double
							.parseDouble("3.4229997")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Sanusi Fafunwa Branch").flat(true);

	MarkerOptions marker912 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.454908"), Double
							.parseDouble("3.257607")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Satelite Town Branch").flat(true);

	MarkerOptions marker913 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.3803005"), Double
							.parseDouble("2.7070355")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Seme Border Branch").flat(true);

	MarkerOptions marker914 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4293029"), Double
							.parseDouble("3.3431761")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Snake Island Branch").flat(true);

	MarkerOptions marker915 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5403377"), Double
							.parseDouble("3.3906389")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("St Finbarrs Branch").flat(true);

	MarkerOptions marker916 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.510031"), Double
							.parseDouble("3.364180")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Surulere Branch").flat(true);

	MarkerOptions marker917 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.505781"), Double
							.parseDouble("3.365714")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Tejuosho Branch").flat(true);

	MarkerOptions marker918 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4660194"), Double
							.parseDouble("3.2417607")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Trade Fair Complex Branch").flat(true);

	MarkerOptions marker919 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.440880"), Double
							.parseDouble("3.331643")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Trinity 2 Branch").flat(true);

	MarkerOptions marker920 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.441008"), Double
							.parseDouble("3.330899")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Trinity Branch").flat(true);

	MarkerOptions marker921 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455280"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Warehouse Road 2 Branch").flat(true);

	MarkerOptions marker922 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.443915"), Double
							.parseDouble("3.373027")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Wharf Road Branch").flat(true);

	MarkerOptions marker923 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.517745"), Double
							.parseDouble("3.373935")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.zenithbank))

			.title("Yabatech Branch").flat(true);

	MarkerOptions marker924 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4314818"), Double
							.parseDouble("3.4217464")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Head Office").flat(true);

	MarkerOptions marker925 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6235166"), Double
							.parseDouble("3.0594286")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Abule Egba 2 Branch").flat(true);

	MarkerOptions marker926 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5581221"), Double
							.parseDouble("3.2696488")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Abule Egba Branch").flat(true);

	MarkerOptions marker927 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.617304"), Double
							.parseDouble("3.345972")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Adeniyi Jones Branch").flat(true);

	MarkerOptions marker928 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430533"), Double
							.parseDouble("3.41557")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Adeola Odeku Branch").flat(true);

	MarkerOptions marker929 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.43111"), Double
							.parseDouble("3.425168")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Adetokunbo Ademola Branch").flat(true);

	MarkerOptions marker930 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4311268"), Double
							.parseDouble("3.4223687")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Adeyemo Alakija Branch").flat(true);

	MarkerOptions marker931 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5500923"), Double
							.parseDouble("3.3324624")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Airport Road 2 Branch").flat(true);

	MarkerOptions marker932 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.564844"), Double
							.parseDouble("3.322720")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Airport Road Branch").flat(true);

	MarkerOptions marker933 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4692596"), Double
							.parseDouble("3.5722267")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ajah Branch").flat(true);

	MarkerOptions marker934 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430981"), Double
							.parseDouble("3.430790")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ajose Adeogun Branch").flat(true);

	MarkerOptions marker935 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600783"), Double
							.parseDouble("3.309706")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Akowonjo Branch").flat(true);

	MarkerOptions marker936 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("4.774745"), Double
							.parseDouble("7.0105733")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Alaba Branch").flat(true);

	MarkerOptions marker937 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6047313"), Double
							.parseDouble("3.3482193")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Allen Avenue Branch").flat(true);

	MarkerOptions marker938 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4621753"), Double
							.parseDouble("3.1561923")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Amuwo Odofin Branch").flat(true);

	MarkerOptions marker939 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.559085"), Double
							.parseDouble("3.368978")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Anthony Village Branch").flat(true);

	MarkerOptions marker940 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.441131"), Double
							.parseDouble("3.368835")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Apapa Doualla Branch").flat(true);

	MarkerOptions marker941 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.506454"), Double
							.parseDouble("3.375607")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Apapa Branch").flat(true);

	MarkerOptions marker942 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4619194"), Double
							.parseDouble("3.1556988")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Aspamda Branch").flat(true);

	MarkerOptions marker943 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444464"), Double
							.parseDouble("3.424106")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Awolowo 2 Branch").flat(true);

	MarkerOptions marker944 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.619897"), Double
							.parseDouble("3.503553")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ayangbure Branch").flat(true);

	MarkerOptions marker945 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459246"), Double
							.parseDouble("3.432616")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Bayo Kuku Branch").flat(true);
	MarkerOptions marker946 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.488389"), Double
							.parseDouble("3.360585")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Bode Thomas 2 Branch").flat(true);

	MarkerOptions marker947 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.489487"), Double
							.parseDouble("3.357289")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Bode Thomas Branch").flat(true);

	MarkerOptions marker948 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453577"), Double
							.parseDouble("3.389471")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Broad Street Branch").flat(true);

	MarkerOptions marker949 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.438133"), Double
							.parseDouble("3.369925")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Burma Road, Apapa Branch").flat(true);

	MarkerOptions marker950 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.449595"), Double
							.parseDouble("3.397894")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Catholic Mission Branch").flat(true);

	MarkerOptions marker951 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6176732"), Double
							.parseDouble("3.3559504")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("CBD Alausa Branch").flat(true);

	MarkerOptions marker952 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.501646"), Double
							.parseDouble("3.323534")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Cele Ijesha Branch").flat(true);

	MarkerOptions marker953 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601838"), Double
							.parseDouble("3.351486")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Computer Village Branch").flat(true);

	MarkerOptions marker954 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.435797"), Double
							.parseDouble("3.364509")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Creek Road Branch").flat(true);

	MarkerOptions marker955 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600693"), Double
							.parseDouble("3.300757")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Egbeda Idimu Branch").flat(true);

	MarkerOptions marker956 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.587109"), Double
							.parseDouble("3.286114")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Egbeda Branch").flat(true);

	MarkerOptions marker957 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5992536"), Double
							.parseDouble("3.2919083")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Egbeda 2 Branch").flat(true);

	MarkerOptions marker958 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.466445"), Double
							.parseDouble("3.283514")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Festac 2 Branch").flat(true);

	MarkerOptions marker959 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444099"), Double
							.parseDouble("3.294300")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Festac Branch").flat(true);

	MarkerOptions marker960 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.552627"), Double
							.parseDouble("3.391306")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Gbagada Branch").flat(true);

	MarkerOptions marker961 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444403"), Double
							.parseDouble("3.4203321")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("GTExpress Bank Road Branch").flat(true);

	MarkerOptions marker962 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.599328"), Double
							.parseDouble("3.293679")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("GTExpress Boundary Branch").flat(true);

	MarkerOptions marker963 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4507071"), Double
							.parseDouble("3.3911381")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("GTExpress Campos Square Branch").flat(true);

	MarkerOptions marker964 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.470262"), Double
							.parseDouble("3.292430")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("GTExpress Festac Branch").flat(true);

	MarkerOptions marker965 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.649561"), Double
							.parseDouble("3.518328")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("GTExpress Ikorodu Branch").flat(true);

	MarkerOptions marker966 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4856163"), Double
							.parseDouble("3.3855351")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("GTExpress Jebba Branch").flat(true);

	MarkerOptions marker967 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5010749"), Double
							.parseDouble("3.3196996")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("GTExpress Kingsway Branch").flat(true);

	MarkerOptions marker968 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4657408"), Double
							.parseDouble("3.5467568")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("GTExpress Lekki Branch").flat(true);

	MarkerOptions marker969 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5010696"), Double
							.parseDouble("3.3196996")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("GTExpress Mile 2 Branch").flat(true);

	MarkerOptions marker970 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.581601"), Double
							.parseDouble("3.320906")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("GTExpress Old Airport Branch").flat(true);

	MarkerOptions marker971 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455280"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("GTExpress Wharf Road Branch").flat(true);

	MarkerOptions marker972 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601053"), Double
							.parseDouble("3.338636")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Guiness Branch").flat(true);

	MarkerOptions marker973 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442763"), Double
							.parseDouble("3.326734")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ibafon Branch").flat(true);

	MarkerOptions marker974 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5533455"), Double
							.parseDouble("3.3509883")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Idi Oro Branch").flat(true);

	MarkerOptions marker975 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4537212"), Double
							.parseDouble("3.3874837")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Idumota Branch").flat(true);

	MarkerOptions marker976 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.659497"), Double
							.parseDouble("3.323168")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Iju Branch").flat(true);

	MarkerOptions marker977 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.574969"), Double
							.parseDouble("3.361438")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ikeja Cantonment Branch").flat(true);

	MarkerOptions marker978 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.621244"), Double
							.parseDouble("3.501697")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ikorodu Branch").flat(true);

	MarkerOptions marker979 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600568"), Double
							.parseDouble("3.366069")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ikosi Branch").flat(true);

	MarkerOptions marker980 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4639538"), Double
							.parseDouble("3.5544102")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ikota 2 Branch").flat(true);

	MarkerOptions marker981 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.550561"), Double
							.parseDouble("3.268389")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ikotun Branch").flat(true);

	MarkerOptions marker982 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444465"), Double
							.parseDouble("3.425280")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ikoyi 1  Branch").flat(true);

	MarkerOptions marker983 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.546334"), Double
							.parseDouble("3.361303")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ilupeju 2 Branch").flat(true);

	MarkerOptions marker984 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5564335"), Double
							.parseDouble("3.3545371")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ilupeju Branch").flat(true);

	MarkerOptions marker985 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6261938"), Double
							.parseDouble("3.3089728")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ipaja Branch").flat(true);

	MarkerOptions marker986 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.526376"), Double
							.parseDouble("3.330144")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Isolo Branch").flat(true);

	MarkerOptions marker987 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.511113"), Double
							.parseDouble("3.339342")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Itire Branch").flat(true);

	MarkerOptions marker988 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.606289"), Double
							.parseDouble("3.392394")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ketu Branch").flat(true);

	MarkerOptions marker989 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.619413"), Double
							.parseDouble("3.510454")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("LASPOTECH Branch").flat(true);

	MarkerOptions marker990 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4696317"), Double
							.parseDouble("3.1983754")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("LASU Branch").flat(true);

	MarkerOptions marker991 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5120655"), Double
							.parseDouble("3.3454143")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Lawanson Branch").flat(true);

	MarkerOptions marker992 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458985"), Double
							.parseDouble("3.601521")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Lekki Branch").flat(true);

	MarkerOptions marker993 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5243793"), Double
							.parseDouble("3.377017")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("LUTH Branch").flat(true);

	MarkerOptions marker994 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.612587"), Double
							.parseDouble("3.368805")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Magodo Branch").flat(true);

	MarkerOptions marker995 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453505"), Double
							.parseDouble("3.385659")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Marina Branch").flat(true);

	MarkerOptions marker996 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.503910"), Double
							.parseDouble("3.351390")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Masha Branch").flat(true);

	MarkerOptions marker997 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.537044"), Double
							.parseDouble("3.346589")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Matori Branch").flat(true);

	MarkerOptions marker998 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5748099"), Double
							.parseDouble("3.3598781")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Mobolaji Bank Anthony Branch").flat(true);

	MarkerOptions marker999 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.448543"), Double
							.parseDouble("3.406453")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Moloney Branch").flat(true);

	MarkerOptions marker1000 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.536949"), Double
							.parseDouble("3.352278")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Mushin Branch").flat(true);

	MarkerOptions marker1001 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6014372"), Double
							.parseDouble("3.3382755")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Oba Akran II Branch").flat(true);

	MarkerOptions marker1002 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.613955"), Double
							.parseDouble("3.33588")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Oba Akran Branch").flat(true);

	MarkerOptions marker1003 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.635448"), Double
							.parseDouble("3.347134")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ogba 2 Branch").flat(true);

	MarkerOptions marker1004 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6326615"), Double
							.parseDouble("3.3391142")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ogba Branch").flat(true);

	MarkerOptions marker1005 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.575734"), Double
							.parseDouble("3.390600")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ogudu GRA Branch").flat(true);

	MarkerOptions marker1006 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.639815"), Double
							.parseDouble("3.368123")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ojodu Branch").flat(true);

	MarkerOptions marker1007 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.509901"), Double
							.parseDouble("3.364558")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Ojuelegba Branch").flat(true);

	MarkerOptions marker1008 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.584249"), Double
							.parseDouble("3.357104")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Oke Arin Branch").flat(true);

	MarkerOptions marker1009 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.512296"), Double
							.parseDouble("3.321680")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Okota Branch").flat(true);

	MarkerOptions marker1010 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5830719"), Double
							.parseDouble("3.3731849")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Onipanu Branch").flat(true);

	MarkerOptions marker1011 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.589030"), Double
							.parseDouble("3.361555")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Opebi Branch").flat(true);

	MarkerOptions marker1012 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.589682"), Double
							.parseDouble("3.374843")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Oregun Branch").flat(true);

	MarkerOptions marker1013 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.449999"), Double
							.parseDouble("3.2644773")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Orile Coker Branch").flat(true);

	MarkerOptions marker1014 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.429449"), Double
							.parseDouble("3.428862")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Oyin Jolayemi Branch").flat(true);

	MarkerOptions marker1015 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431110"), Double
							.parseDouble("3.425168")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Processing Centre").flat(true);

	MarkerOptions marker1016 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6045741"), Double
							.parseDouble("3.3292061")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Shogunle Branch").flat(true);

	MarkerOptions marker1017 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.445207"), Double
							.parseDouble("3.409177")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("St. Gregory Branch").flat(true);

	MarkerOptions marker1018 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.423565"), Double
							.parseDouble("3.426346")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Tiamiyu Savage Branch").flat(true);

	MarkerOptions marker1019 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.595711"), Double
							.parseDouble("3.351864")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Toyin Street Branch").flat(true);

	MarkerOptions marker1020 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.517619"), Double
							.parseDouble("3.397036")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("UNILAG Branch").flat(true);

	MarkerOptions marker1021 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.623259"), Double
							.parseDouble("3.339286")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Wamco Branch").flat(true);

	MarkerOptions marker1022 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.498054"), Double
							.parseDouble("3.360898")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Western Avenue Branch").flat(true);

	MarkerOptions marker1023 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.495087"), Double
							.parseDouble("3.380793")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Yaba Branch").flat(true);

	MarkerOptions marker1024 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.505226"), Double
							.parseDouble("3.378523")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.gtb))

			.title("Yaba 2 Branch").flat(true);

	MarkerOptions marker1053 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.463167"), Double
							.parseDouble("3.278171")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("1st Avenue Branch").flat(true);

	MarkerOptions marker1054 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.491527"), Double
							.parseDouble("3.356609")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Adeniran Ogunsanya Branch").flat(true);

	MarkerOptions marker1055 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431641"), Double
							.parseDouble("3.426722")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Adeola Hopewell Branch").flat(true);

	MarkerOptions marker1056 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430533"), Double
							.parseDouble("3.415597")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Adeola Odeku Branch").flat(true);

	MarkerOptions marker1057 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.426731"), Double
							.parseDouble("3.429311")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Adetokunbo Ademola Branch").flat(true);

	MarkerOptions marker1058 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.508857"), Double
							.parseDouble("3.311774")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ago Palace Way Branch").flat(true);

	MarkerOptions marker1059 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430981"), Double
							.parseDouble("3.430790")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ajose Adeogun Branch").flat(true);

	MarkerOptions marker1060 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430231"), Double
							.parseDouble("3.423828")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Akin Adesola Branch").flat(true);

	MarkerOptions marker1061 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600783"), Double
							.parseDouble("3.309706")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Akowonjo Branch").flat(true);

	MarkerOptions marker1062 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.588972"), Double
							.parseDouble("3.340286")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Alausa Secretariat Branch").flat(true);

	MarkerOptions marker1063 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.621108"), Double
							.parseDouble("3.360934")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Alausa Branch").flat(true);

	MarkerOptions marker1064 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.447933"), Double
							.parseDouble("3.409792")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Alfred Rewane Road Branch").flat(true);

	MarkerOptions marker1065 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.605025"), Double
							.parseDouble("3.350262")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Allen Avenue Branch").flat(true);

	MarkerOptions marker1066 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.441110"), Double
							.parseDouble("2.919306")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Aspamda Intl Market Branch").flat(true);

	MarkerOptions marker1067 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442141"), Double
							.parseDouble("3.418543")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Awolowo Road Branch").flat(true);

	MarkerOptions marker1068 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.429357"), Double
							.parseDouble("2.892092")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Badagry Branch").flat(true);

	MarkerOptions marker1069 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460506"), Double
							.parseDouble("3.250034")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Bba Branch").flat(true);

	MarkerOptions marker1070 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.449646"), Double
							.parseDouble("3.403150")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("City Hall Branch").flat(true);

	MarkerOptions marker1071 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.429302"), Double
							.parseDouble("3.268421")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Coker Orile Branch").flat(true);

	MarkerOptions marker1072 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.593582"), Double
							.parseDouble("3.338569")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Computer Village Branch").flat(true);

	MarkerOptions marker1073 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.436204"), Double
							.parseDouble("3.364503")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Creek Road Branch").flat(true);

	MarkerOptions marker1074 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.542545"), Double
							.parseDouble("3.296734")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Egbe Branch").flat(true);

	MarkerOptions marker1075 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.593111"), Double
							.parseDouble("3.986328")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Epe Branch").flat(true);

	MarkerOptions marker1076 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.542254"), Double
							.parseDouble("3.348606")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Fatai Atere Branch").flat(true);

	MarkerOptions marker1077 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.450815"), Double
							.parseDouble("3.429570")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Glover Road Branch").flat(true);

	MarkerOptions marker1078 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.461456"), Double
							.parseDouble("3.390013")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Idoluwo Branch").flat(true);

	MarkerOptions marker1079 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.656860"), Double
							.parseDouble("3.323311")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Iju Branch").flat(true);

	MarkerOptions marker1080 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.593611"), Double
							.parseDouble("3.357842")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ikeja Plaza Branch").flat(true);

	MarkerOptions marker1081 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.589954"), Double
							.parseDouble("3.517641")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ikorodu LG Branch").flat(true);

	MarkerOptions marker1082 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.619897"), Double
							.parseDouble("3.503553")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ikorodu Branch").flat(true);

	MarkerOptions marker1083 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.554935"), Double
							.parseDouble("3.269658")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ikotun Branch").flat(true);

	MarkerOptions marker1084 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.530430"), Double
							.parseDouble("3.343957")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Isolo Branch").flat(true);

	MarkerOptions marker1085 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.594850"), Double
							.parseDouble("3.338498")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Kudirat Abiola Way Branch").flat(true);

	MarkerOptions marker1086 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.523224"), Double
							.parseDouble("3.353109")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Luth Idi Araba Branch").flat(true);

	MarkerOptions marker1087 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.612587"), Double
							.parseDouble("3.368805")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Magodo Branch").flat(true);

	MarkerOptions marker1088 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.450581"), Double
							.parseDouble("3.390201")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Marina Branch").flat(true);

	MarkerOptions marker1089 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.532684"), Double
							.parseDouble("3.349595")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Matori Market Branch").flat(true);

	MarkerOptions marker1090 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.509280"), Double
							.parseDouble("3.374761")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Montgomery Branch").flat(true);

	MarkerOptions marker1091 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.581818"), Double
							.parseDouble("3.321135")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Nahco Branch").flat(true);

	MarkerOptions marker1092 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.457694"), Double
							.parseDouble("3.384947")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Nnamdi Azikwe Branch").flat(true);

	MarkerOptions marker1093 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.638978"), Double
							.parseDouble("3.360186")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ogba Branch").flat(true);

	MarkerOptions marker1094 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.574911"), Double
							.parseDouble("3.396020")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ogudu Branch").flat(true);

	MarkerOptions marker1095 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.639977"), Double
							.parseDouble("3.369289")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Oke- Arin Branch").flat(true);

	MarkerOptions marker1096 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.589770"), Double
							.parseDouble("3.361081")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Opebi Branch").flat(true);

	MarkerOptions marker1097 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.472548"), Double
							.parseDouble("3.596261")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Osapa London Branch").flat(true);

	MarkerOptions marker1098 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.545878"), Double
							.parseDouble("3.332688")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Osolo Way Branch").flat(true);

	MarkerOptions marker1099 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.674561"), Double
							.parseDouble("3.271174")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Sango Ota Branch").flat(true);

	MarkerOptions marker1100 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.530670"), Double
							.parseDouble("3.379079")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Shomolu Branch").flat(true);

	MarkerOptions marker1101 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.596321"), Double
							.parseDouble("3.347949")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Toyin Street Branch").flat(true);

	MarkerOptions marker1102 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455280"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Warehouse Road Branch").flat(true);

	MarkerOptions marker1103 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442277"), Double
							.parseDouble("3.376492")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Wharf Road 2 Branch").flat(true);

	MarkerOptions marker1104 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.492975"), Double
							.parseDouble("3.356992")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Adeniran Ogunsanya Branch").flat(true);

	MarkerOptions marker1105 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.434641"), Double
							.parseDouble("3.429385")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Adeola Hopewell Branch").flat(true);

	MarkerOptions marker1106 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4613"), Double
							.parseDouble("3.45867")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Adeola Hopewell 2 Branch").flat(true);

	MarkerOptions marker1107 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430022"), Double
							.parseDouble("3.419289")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Adeola Odeku - The Place Branch").flat(true);

	MarkerOptions marker1108 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430306"), Double
							.parseDouble("3.415599")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Adeola Odeku Branch").flat(true);

	MarkerOptions marker1109 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.43111"), Double
							.parseDouble("3.425168")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Adetokunbo Ademola Branch").flat(true);

	MarkerOptions marker1110 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.434356"), Double
							.parseDouble("3.48013")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Admiralty Branch").flat(true);

	MarkerOptions marker1111 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.524379"), Double
							.parseDouble("3.379206")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Airport Road Branch").flat(true);

	MarkerOptions marker1112 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.47294"), Double
							.parseDouble("3.597668")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ajah Branch").flat(true);

	MarkerOptions marker1113 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431472"), Double
							.parseDouble("3.429642")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ajose Adeogun Branch").flat(true);

	MarkerOptions marker1114 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600693"), Double
							.parseDouble("3.300757")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Akowonjo Branch").flat(true);

	MarkerOptions marker1115 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.652838"), Double
							.parseDouble("3.299147")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Alagbado Branch").flat(true);

	MarkerOptions marker1116 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.605850"), Double
							.parseDouble("3.349831")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Allen Branch").flat(true);

	MarkerOptions marker1117 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601615"), Double
							.parseDouble("3.351937")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Allen 2 Branch").flat(true);

	MarkerOptions marker1118 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.440795"), Double
							.parseDouble("3.378266")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Apapa Creek Road Branch").flat(true);

	MarkerOptions marker1119 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.45528"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Apapa Warehouse Branch").flat(true);

	MarkerOptions marker1120 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.435232"), Double
							.parseDouble("3.366052")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Apapa Branch").flat(true);

	MarkerOptions marker1121 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459993"), Double
							.parseDouble("3.230824")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Aspamda Branch").flat(true);

	MarkerOptions marker1122 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444132"), Double
							.parseDouble("3.423186")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Awolowo Road Branch").flat(true);

	MarkerOptions marker1123 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.44111"), Double
							.parseDouble("2.919306")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Bba Tradefair Branch").flat(true);

	MarkerOptions marker1124 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453434"), Double
							.parseDouble("3.389502")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Broad Street Branch").flat(true);

	MarkerOptions marker1125 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.440372"), Double
							.parseDouble("3.372087")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Commercial Road Branch").flat(true);

	MarkerOptions marker1126 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.595173"), Double
							.parseDouble("3.340223")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Computer Village Mini Branch").flat(true);

	MarkerOptions marker1127 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.517791"), Double
							.parseDouble("3.382686")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Cpc Branch").flat(true);

	MarkerOptions marker1128 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455374"), Double
							.parseDouble("3.384912")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Davies Branch").flat(true);

	MarkerOptions marker1129 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.47596"), Double
							.parseDouble("3.279926")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Festac Branch").flat(true);

	MarkerOptions marker1130 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.584249"), Double
							.parseDouble("3.357104")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Gra Ikeja Branch").flat(true);

	MarkerOptions marker1131 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.599415"), Double
							.parseDouble("3.282268")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Iddo Branch").flat(true);

	MarkerOptions marker1132 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.564985"), Double
							.parseDouble("3.280998")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Idimu Branch").flat(true);

	MarkerOptions marker1133 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.43295"), Double
							.parseDouble("3.42567")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Idowu Taylor Branch").flat(true);

	MarkerOptions marker1134 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458846"), Double
							.parseDouble("3.38971")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Idumagbo Branch").flat(true);

	MarkerOptions marker1135 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.461456"), Double
							.parseDouble("3.390013")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Idumota Branch").flat(true);

	MarkerOptions marker1136 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601053"), Double
							.parseDouble("3.338636")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ikeja Branch").flat(true);

	MarkerOptions marker1137 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.57406"), Double
							.parseDouble("3.368788")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ikorodu Branch").flat(true);

	MarkerOptions marker1138 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442004"), Double
							.parseDouble("3.418622")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ikoyi Branch").flat(true);

	MarkerOptions marker1139 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.550982"), Double
							.parseDouble("3.355711")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ilupeju Branch").flat(true);

	MarkerOptions marker1140 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.450225"), Double
							.parseDouble("3.39483")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Joseph Street Branch").flat(true);

	MarkerOptions marker1141 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.604336"), Double
							.parseDouble("3.390854")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ketu Branch").flat(true);

	MarkerOptions marker1142 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.539335"), Double
							.parseDouble("3.344935")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ladipo Branch").flat(true);

	MarkerOptions marker1143 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.472548"), Double
							.parseDouble("3.596261")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Lekki Chevron Branch").flat(true);

	MarkerOptions marker1144 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458985"), Double
							.parseDouble("3.601521")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Lekki Branch").flat(true);

	MarkerOptions marker1145 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431224"), Double
							.parseDouble("3.469051")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Lekki 2 Branch").flat(true);
	MarkerOptions marker1146 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.589773"), Double
							.parseDouble("3.331411")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Local Airport Branch").flat(true);

	MarkerOptions marker1147 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.446512"), Double
							.parseDouble("3.405086")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Macarthy Branch").flat(true);

	MarkerOptions marker1148 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.448183"), Double
							.parseDouble("3.394522")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Marina Branch").flat(true);

	MarkerOptions marker1149 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.537044"), Double
							.parseDouble("3.346589")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Matori Branch").flat(true);

	MarkerOptions marker1150 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601838"), Double
							.parseDouble("3.351486")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Mikano Office Branch").flat(true);

	MarkerOptions marker1151 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.573279"), Double
							.parseDouble("3.363191")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Mobolaji Bank Anthony Way Branch").flat(true);

	MarkerOptions marker1152 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601838"), Double
							.parseDouble("3.351486")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Motorways Branch").flat(true);

	MarkerOptions marker1153 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.627364"), Double
							.parseDouble("3.322609")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Mushin Branch").flat(true);

	MarkerOptions marker1154 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601409"), Double
							.parseDouble("3.338441")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Oba Akran Branch").flat(true);

	MarkerOptions marker1155 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.628888"), Double
							.parseDouble("3.337987")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ogba Branch").flat(true);

	MarkerOptions marker1156 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.574911"), Double
							.parseDouble("3.391811")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ogudu Branch").flat(true);

	MarkerOptions marker1157 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.465455"), Double
							.parseDouble("3.332868")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Oke Afa Branch").flat(true);

	MarkerOptions marker1158 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.514071"), Double
							.parseDouble("3.321081")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Okota Branch").flat(true);

	MarkerOptions marker1159 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.502206"), Double
							.parseDouble("3.305082")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Okota 2 Branch").flat(true);

	MarkerOptions marker1160 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.536498"), Double
							.parseDouble("3.367796")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Onipanu Branch").flat(true);

	MarkerOptions marker1161 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600111"), Double
							.parseDouble("3.364972")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Oregun Branch").flat(true);

	MarkerOptions marker1162 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.636360"), Double
							.parseDouble("3.321271")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Orile Coker Branch").flat(true);

	MarkerOptions marker1163 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.460553"), Double
							.parseDouble("3.388744")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Oroyinyin Branch").flat(true);

	MarkerOptions marker1164 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.545843"), Double
							.parseDouble("3.333073")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Osolo Way Branch").flat(true);

	MarkerOptions marker1165 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.429449"), Double
							.parseDouble("3.428866")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Oyin Jolayemi Branch").flat(true);

	MarkerOptions marker1166 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.472548"), Double
							.parseDouble("3.596261")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Palms Branch").flat(true);

	MarkerOptions marker1167 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.444837"), Double
							.parseDouble("3.369387")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Randle Road Branch").flat(true);

	MarkerOptions marker1168 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431489"), Double
							.parseDouble("3.425037")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Sanusi Fafunwa Branch").flat(true);

	MarkerOptions marker1169 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.540836"), Double
							.parseDouble("3.368308")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Shomolu Branch").flat(true);

	MarkerOptions marker1170 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.503878"), Double
							.parseDouble("3.351437")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Surulere Branch").flat(true);

	MarkerOptions marker1171 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.595774"), Double
							.parseDouble("3.352128")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Toyin Street Branch").flat(true);

	MarkerOptions marker1172 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442277"), Double
							.parseDouble("3.376492")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Wharf Road Branch").flat(true);

	MarkerOptions marker1173 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.508648"), Double
							.parseDouble("3.368051")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Yaba Branch").flat(true);

	MarkerOptions marker1174 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.631205"), Double
							.parseDouble("3.321252")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Abule Egba Branch").flat(true);

	MarkerOptions marker1175 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.491327"), Double
							.parseDouble("3.356532")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Adeniran Ogunsanya Branch").flat(true);

	MarkerOptions marker1176 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431109"), Double
							.parseDouble("3.424768")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Adetokunbo Ademola Branch").flat(true);

	MarkerOptions marker1177 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.634383"), Double
							.parseDouble("3.319783")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Agege Branch").flat(true);

	MarkerOptions marker1178 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.435559"), Double
							.parseDouble("3.508601")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ajah Branch").flat(true);

	MarkerOptions marker1179 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.454408"), Double
							.parseDouble("3.347992")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ajegunle Branch").flat(true);

	MarkerOptions marker1180 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430981"), Double
							.parseDouble("3.430790")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ajose Adeogun Branch").flat(true);

	MarkerOptions marker1181 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.522806"), Double
							.parseDouble("3.385982")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Akoka Branch").flat(true);

	MarkerOptions marker1182 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601838"), Double
							.parseDouble("3.351486")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Alausa Branch").flat(true);

	MarkerOptions marker1183 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.599856"), Double
							.parseDouble("3.352585")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Allen Avenue Branch").flat(true);

	MarkerOptions marker1184 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.483865"), Double
							.parseDouble("3.170312")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("BBA Branch").flat(true);

	MarkerOptions marker1185 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453559"), Double
							.parseDouble("3.389415")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Broad Street Branch").flat(true);

	MarkerOptions marker1186 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601838"), Double
							.parseDouble("3.351486")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("City Mall, Ikeja Branch").flat(true);

	MarkerOptions marker1187 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.531297"), Double
							.parseDouble("3.338601")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Daleko Branch").flat(true);

	MarkerOptions marker1188 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.597996"), Double
							.parseDouble("3.309641")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Egbeda Branch").flat(true);

	MarkerOptions marker1189 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.543283"), Double
							.parseDouble("3.296270")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ejigbo Branch").flat(true);

	MarkerOptions marker1190 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.429957"), Double
							.parseDouble("3.408936")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Federal Palace Hotel Branch").flat(true);

	MarkerOptions marker1191 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.469330"), Double
							.parseDouble("3.274942")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Festac Town Branch").flat(true);

	MarkerOptions marker1192 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.497988"), Double
							.parseDouble("3.343929")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Gbaja Branch").flat(true);

	MarkerOptions marker1193 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.495085"), Double
							.parseDouble("3.380794")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Herbert Macaulay Road,Yaba Branch").flat(true);

	MarkerOptions marker1194 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.429954"), Double
							.parseDouble("3.412112")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Idejo Branch").flat(true);

	MarkerOptions marker1195 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458846"), Double
							.parseDouble("3.389710")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Idumagbo Branch").flat(true);

	MarkerOptions marker1196 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.475129"), Double
							.parseDouble("3.202317")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Igando Branch").flat(true);

	MarkerOptions marker1197 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.624296"), Double
							.parseDouble("3.494599")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ikorodu Branch").flat(true);

	MarkerOptions marker1198 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.472516"), Double
							.parseDouble("3.568616")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ikota Branch").flat(true);

	MarkerOptions marker1199 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.550560"), Double
							.parseDouble("3.268388")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ikotun Branch").flat(true);

	MarkerOptions marker1200 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442556"), Double
							.parseDouble("3.420328")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ikoyi Branch").flat(true);
	
	googleMap.addMarker(marker901);
	googleMap.addMarker(marker902);
	googleMap.addMarker(marker903);
	googleMap.addMarker(marker904);
	googleMap.addMarker(marker905);
	googleMap.addMarker(marker906);
	googleMap.addMarker(marker907);
	googleMap.addMarker(marker908);
	googleMap.addMarker(marker909);
	googleMap.addMarker(marker910);
	googleMap.addMarker(marker911);
	googleMap.addMarker(marker912);
	googleMap.addMarker(marker913);
	googleMap.addMarker(marker914);
	googleMap.addMarker(marker915);
	googleMap.addMarker(marker916);
	googleMap.addMarker(marker917);
	googleMap.addMarker(marker918);
	googleMap.addMarker(marker919);
	googleMap.addMarker(marker920);
	googleMap.addMarker(marker921);
	googleMap.addMarker(marker922);
	googleMap.addMarker(marker923);
	googleMap.addMarker(marker924);
	googleMap.addMarker(marker925);
	googleMap.addMarker(marker926);
	googleMap.addMarker(marker927);
	googleMap.addMarker(marker928);
	googleMap.addMarker(marker929);
	googleMap.addMarker(marker930);
	googleMap.addMarker(marker931);
	googleMap.addMarker(marker932);
	googleMap.addMarker(marker933);
	googleMap.addMarker(marker934);
	googleMap.addMarker(marker935);
	googleMap.addMarker(marker936);
	googleMap.addMarker(marker937);
	googleMap.addMarker(marker938);
	googleMap.addMarker(marker939);
	googleMap.addMarker(marker940);
	googleMap.addMarker(marker941);
	googleMap.addMarker(marker942);
	googleMap.addMarker(marker943);
	googleMap.addMarker(marker944);
	googleMap.addMarker(marker945);
	googleMap.addMarker(marker946);
	googleMap.addMarker(marker947);
	googleMap.addMarker(marker948);
	googleMap.addMarker(marker949);
	googleMap.addMarker(marker950);
	googleMap.addMarker(marker951);
	googleMap.addMarker(marker952);
	googleMap.addMarker(marker953);
	googleMap.addMarker(marker954);
	googleMap.addMarker(marker955);
	googleMap.addMarker(marker956);
	googleMap.addMarker(marker957);
	googleMap.addMarker(marker958);
	googleMap.addMarker(marker959);
	googleMap.addMarker(marker960);
	googleMap.addMarker(marker961);
	googleMap.addMarker(marker962);
	googleMap.addMarker(marker963);
	googleMap.addMarker(marker964);
	googleMap.addMarker(marker965);
	googleMap.addMarker(marker966);
	googleMap.addMarker(marker967);
	googleMap.addMarker(marker968);
	googleMap.addMarker(marker969);
	googleMap.addMarker(marker970);
	googleMap.addMarker(marker971);
	googleMap.addMarker(marker972);
	googleMap.addMarker(marker973);
	googleMap.addMarker(marker974);
	googleMap.addMarker(marker975);
	googleMap.addMarker(marker976);
	googleMap.addMarker(marker977);
	googleMap.addMarker(marker978);
	googleMap.addMarker(marker979);
	googleMap.addMarker(marker980);
	googleMap.addMarker(marker981);
	googleMap.addMarker(marker982);
	googleMap.addMarker(marker983);
	googleMap.addMarker(marker984);
	googleMap.addMarker(marker985);
	googleMap.addMarker(marker986);
	googleMap.addMarker(marker987);
	googleMap.addMarker(marker988);
	googleMap.addMarker(marker989);
	googleMap.addMarker(marker990);
	googleMap.addMarker(marker991);
	googleMap.addMarker(marker992);
	googleMap.addMarker(marker993);
	googleMap.addMarker(marker994);
	googleMap.addMarker(marker995);
	googleMap.addMarker(marker996);
	googleMap.addMarker(marker997);
	googleMap.addMarker(marker998);
	googleMap.addMarker(marker999);
	googleMap.addMarker(marker1000);
	googleMap.addMarker(marker1001);
	googleMap.addMarker(marker1002);
	googleMap.addMarker(marker1003);
	googleMap.addMarker(marker1004);
	googleMap.addMarker(marker1005);
	googleMap.addMarker(marker1006);
	googleMap.addMarker(marker1007);
	googleMap.addMarker(marker1008);
	googleMap.addMarker(marker1009);
	googleMap.addMarker(marker1010);
	googleMap.addMarker(marker1011);
	googleMap.addMarker(marker1012);
	googleMap.addMarker(marker1013);
	googleMap.addMarker(marker1014);
	googleMap.addMarker(marker1015);
	googleMap.addMarker(marker1016);
	googleMap.addMarker(marker1017);
	googleMap.addMarker(marker1018);
	googleMap.addMarker(marker1019);
	googleMap.addMarker(marker1020);
	googleMap.addMarker(marker1021);
	googleMap.addMarker(marker1022);
	googleMap.addMarker(marker1023);
	googleMap.addMarker(marker1024);

	googleMap.addMarker(marker1053);
	googleMap.addMarker(marker1054);
	googleMap.addMarker(marker1055);
	googleMap.addMarker(marker1056);
	googleMap.addMarker(marker1057);
	googleMap.addMarker(marker1058);
	googleMap.addMarker(marker1059);
	googleMap.addMarker(marker1060);
	googleMap.addMarker(marker1061);
	googleMap.addMarker(marker1062);
	googleMap.addMarker(marker1063);
	googleMap.addMarker(marker1064);
	googleMap.addMarker(marker1065);
	googleMap.addMarker(marker1066);
	googleMap.addMarker(marker1067);
	googleMap.addMarker(marker1068);
	googleMap.addMarker(marker1069);
	googleMap.addMarker(marker1070);
	googleMap.addMarker(marker1071);
	googleMap.addMarker(marker1072);
	googleMap.addMarker(marker1073);
	googleMap.addMarker(marker1074);
	googleMap.addMarker(marker1075);
	googleMap.addMarker(marker1076);
	googleMap.addMarker(marker1077);
	googleMap.addMarker(marker1078);
	googleMap.addMarker(marker1079);
	googleMap.addMarker(marker1080);
	googleMap.addMarker(marker1081);
	googleMap.addMarker(marker1082);
	googleMap.addMarker(marker1083);
	googleMap.addMarker(marker1084);
	googleMap.addMarker(marker1085);
	googleMap.addMarker(marker1086);
	googleMap.addMarker(marker1087);
	googleMap.addMarker(marker1088);
	googleMap.addMarker(marker1089);
	googleMap.addMarker(marker1090);
	googleMap.addMarker(marker1091);
	googleMap.addMarker(marker1092);
	googleMap.addMarker(marker1093);
	googleMap.addMarker(marker1094);
	googleMap.addMarker(marker1095);
	googleMap.addMarker(marker1096);
	googleMap.addMarker(marker1097);
	googleMap.addMarker(marker1098);
	googleMap.addMarker(marker1099);
	googleMap.addMarker(marker1100);
	googleMap.addMarker(marker1101);
	googleMap.addMarker(marker1102);
	googleMap.addMarker(marker1103);
	googleMap.addMarker(marker1104);
	googleMap.addMarker(marker1105);
	googleMap.addMarker(marker1106);
	googleMap.addMarker(marker1107);
	googleMap.addMarker(marker1108);
	googleMap.addMarker(marker1109);
	googleMap.addMarker(marker1110);
	googleMap.addMarker(marker1111);
	googleMap.addMarker(marker1112);
	googleMap.addMarker(marker1113);
	googleMap.addMarker(marker1114);
	googleMap.addMarker(marker1115);
	googleMap.addMarker(marker1116);
	googleMap.addMarker(marker1117);
	googleMap.addMarker(marker1118);
	googleMap.addMarker(marker1119);
	googleMap.addMarker(marker1120);
	googleMap.addMarker(marker1121);
	googleMap.addMarker(marker1122);
	googleMap.addMarker(marker1123);
	googleMap.addMarker(marker1124);
	googleMap.addMarker(marker1125);
	googleMap.addMarker(marker1126);
	googleMap.addMarker(marker1127);
	googleMap.addMarker(marker1128);
	googleMap.addMarker(marker1129);
	googleMap.addMarker(marker1130);
	googleMap.addMarker(marker1131);
	googleMap.addMarker(marker1132);
	googleMap.addMarker(marker1133);
	googleMap.addMarker(marker1134);
	googleMap.addMarker(marker1135);
	googleMap.addMarker(marker1136);
	googleMap.addMarker(marker1137);
	googleMap.addMarker(marker1138);
	googleMap.addMarker(marker1139);
	googleMap.addMarker(marker1140);
	googleMap.addMarker(marker1141);
	googleMap.addMarker(marker1142);
	googleMap.addMarker(marker1143);
	googleMap.addMarker(marker1144);
	googleMap.addMarker(marker1145);
	googleMap.addMarker(marker1146);
	googleMap.addMarker(marker1147);
	googleMap.addMarker(marker1148);
	googleMap.addMarker(marker1149);
	googleMap.addMarker(marker1150);
	googleMap.addMarker(marker1151);
	googleMap.addMarker(marker1152);
	googleMap.addMarker(marker1153);
	googleMap.addMarker(marker1154);
	googleMap.addMarker(marker1155);
	googleMap.addMarker(marker1156);
	googleMap.addMarker(marker1157);
	googleMap.addMarker(marker1158);
	googleMap.addMarker(marker1159);
	googleMap.addMarker(marker1160);
	googleMap.addMarker(marker1161);
	googleMap.addMarker(marker1162);
	googleMap.addMarker(marker1163);
	googleMap.addMarker(marker1164);
	googleMap.addMarker(marker1165);
	googleMap.addMarker(marker1166);
	googleMap.addMarker(marker1167);
	googleMap.addMarker(marker1168);
	googleMap.addMarker(marker1169);
	googleMap.addMarker(marker1170);
	googleMap.addMarker(marker1171);
	googleMap.addMarker(marker1172);
	googleMap.addMarker(marker1173);
	googleMap.addMarker(marker1174);
	googleMap.addMarker(marker1175);
	googleMap.addMarker(marker1176);
	googleMap.addMarker(marker1177);
	googleMap.addMarker(marker1178);
	googleMap.addMarker(marker1179);
	googleMap.addMarker(marker1180);
	googleMap.addMarker(marker1181);
	googleMap.addMarker(marker1182);
	googleMap.addMarker(marker1183);
	googleMap.addMarker(marker1184);
	googleMap.addMarker(marker1185);
	googleMap.addMarker(marker1186);
	googleMap.addMarker(marker1187);
	googleMap.addMarker(marker1188);
	googleMap.addMarker(marker1189);
	googleMap.addMarker(marker1190);
	googleMap.addMarker(marker1191);
	googleMap.addMarker(marker1192);
	googleMap.addMarker(marker1193);
	googleMap.addMarker(marker1194);
	googleMap.addMarker(marker1195);
	googleMap.addMarker(marker1196);
	googleMap.addMarker(marker1197);
	googleMap.addMarker(marker1198);
	googleMap.addMarker(marker1199);
	googleMap.addMarker(marker1200);
	
			
		}
		
		private void bankMarkerFive(){
			MarkerOptions marker1201 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.598869"), Double
							.parseDouble("3.266968")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ipaja Branch").flat(true);

	MarkerOptions marker1202 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430892"), Double
							.parseDouble("3.425503")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Karimu Kotun Branch").flat(true);

	MarkerOptions marker1203 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.598850"), Double
							.parseDouble("3.386851")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.skyebank))

			.title("Ketu Branch").flat(true);

	MarkerOptions marker1204 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.539309"), Double
							.parseDouble("3.344956")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ladipo Branch").flat(true);

	MarkerOptions marker1205 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.511148"), Double
							.parseDouble("3.339403")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Lawanson Road, Surulere Branch").flat(true);

	MarkerOptions marker1206 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.434482"), Double
							.parseDouble("3.481649")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Lekki Admiralty Branch").flat(true);

	MarkerOptions marker1207 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.434858"), Double
							.parseDouble("3.495726")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Lekki Ajah Branch").flat(true);

	MarkerOptions marker1208 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.456454"), Double
							.parseDouble("3.386002")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Martins street Branch").flat(true);

	MarkerOptions marker1209 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.572528"), Double
							.parseDouble("3.363859")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Maryland Branch").flat(true);

	MarkerOptions marker1210 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.564452"), Double
							.parseDouble("3.321122")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("MMA Ikeja Branch").flat(true);

	MarkerOptions marker1211 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.535233"), Double
							.parseDouble("3.348967")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Mushin Branch").flat(true);

	MarkerOptions marker1212 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601053"), Double
							.parseDouble("3.338636")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Oba Akran Branch").flat(true);

	MarkerOptions marker1213 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.628888"), Double
							.parseDouble("3.337987")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ogba Branch").flat(true);

	MarkerOptions marker1214 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.571095"), Double
							.parseDouble("3.396020")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ogudu Branch").flat(true);

	MarkerOptions marker1215 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.461914"), Double
							.parseDouble("3.157888")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ojo Branch").flat(true);

	MarkerOptions marker1216 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.638589"), Double
							.parseDouble("3.361825")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ojodu Branch").flat(true);

	MarkerOptions marker1217 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.509892"), Double
							.parseDouble("3.364494")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ojuelegba Road Yaba Branch").flat(true);

	MarkerOptions marker1218 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.527188"), Double
							.parseDouble("3.354507")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Ojuwoye, Mushin Branch").flat(true);

	MarkerOptions marker1219 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.461949"), Double
							.parseDouble("3.383831")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Oke Arin Branch").flat(true);

	MarkerOptions marker1220 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.636999"), Double
							.parseDouble("3.315727")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Oko-oba Branch").flat(true);

	MarkerOptions marker1221 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.588922"), Double
							.parseDouble("3.361415")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Opebi Branch").flat(true);

	MarkerOptions marker1222 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.492847"), Double
							.parseDouble("3.340751")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Oshodi Branch").flat(true);

	MarkerOptions marker1223 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.539440"), Double
							.parseDouble("3.332523")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Osolo Branch").flat(true);

	MarkerOptions marker1224 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.483301"), Double
							.parseDouble("3.387331")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Oyingbo Branch").flat(true);

	MarkerOptions marker1225 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458985"), Double
							.parseDouble("3.601521")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Palms, Lekki Branch").flat(true);

	MarkerOptions marker1226 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.533483"), Double
							.parseDouble("3.377708")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Shomolu Branch").flat(true);

	MarkerOptions marker1227 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.470485"), Double
							.parseDouble("3.323705")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Tincan Island Branch").flat(true);

	MarkerOptions marker1228 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.595820"), Double
							.parseDouble("3.350968")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Toyin Street Branch").flat(true);

	MarkerOptions marker1229 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.437241"), Double
							.parseDouble("3.427380")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Victoria Island Branch").flat(true);

	MarkerOptions marker1230 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.439216"), Double
							.parseDouble("3.409053")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Walter Carrington Crescent Branch").flat(true);

	MarkerOptions marker1231 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455280"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Warehouse Road Branch").flat(true);

	MarkerOptions marker1232 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455280"), Double
							.parseDouble("3.364084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Warehouse road 2 Branch").flat(true);

	MarkerOptions marker1233 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442277"), Double
							.parseDouble("3.376492")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.fcmb))

			.title("Wharf Road Branch").flat(true);

	MarkerOptions marker1234 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6461895"), Double
							.parseDouble("3.3133315")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Abbatoir Branch").flat(true);

	MarkerOptions marker1235 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6340585"), Double
							.parseDouble("3.2986175")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Abule Egba Branch").flat(true);

	MarkerOptions marker1236 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6187145"), Double
							.parseDouble("3.3435591")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Adeniyi Jones Branch").flat(true);

	MarkerOptions marker1237 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.435443"), Double
							.parseDouble("3.42959")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Adeola Hopewell Branch").flat(true);

	MarkerOptions marker1238 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4299618"), Double
							.parseDouble("3.4071197")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Adeola Odeku Branch").flat(true);

	MarkerOptions marker1239 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431108"), Double
							.parseDouble("3.424658")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Adetokunbo Ademola 2 Branch").flat(true);

	MarkerOptions marker1240 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431109"), Double
							.parseDouble("3.424777")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ademola Adetokunbo Branch").flat(true);

	MarkerOptions marker1241 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.8900053"), Double
							.parseDouble("3.0148113")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Agbara  Estate Branch").flat(true);

	MarkerOptions marker1242 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5818238"), Double
							.parseDouble("3.3189461")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Airport Road Branch").flat(true);

	MarkerOptions marker1243 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.43164"), Double
							.parseDouble("3.424149")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Akin Adesola Branch").flat(true);

	MarkerOptions marker1244 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5915688"), Double
							.parseDouble("3.2910482")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Akowonjo Branch").flat(true);

	MarkerOptions marker1245 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4547538"), Double
							.parseDouble("3.381842")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Alaba Branch").flat(true);

	MarkerOptions marker1246 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4592314"), Double
							.parseDouble("3.1869181")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Alaba 2 Branch").flat(true);

	MarkerOptions marker1247 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4586153"), Double
							.parseDouble("3.1868313")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Alaba 3 Branch").flat(true);

	MarkerOptions marker1248 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4592314"), Double
							.parseDouble("3.1869181")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Alaba 5 Branch").flat(true);

	MarkerOptions marker1249 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4547591"), Double
							.parseDouble("3.381842")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Alaba 6 Branch").flat(true);

	MarkerOptions marker1250 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6115406"), Double
							.parseDouble("3.3428093")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Alausa Branch").flat(true);

	MarkerOptions marker1251 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.432127"), Double
							.parseDouble("3.426987")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Amodu Tijani Branch").flat(true);

	MarkerOptions marker1252 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5001372"), Double
							.parseDouble("3.1086725")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("AOCOED Branch").flat(true);

	MarkerOptions marker1253 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4426453"), Double
							.parseDouble("3.3742747")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Apapa II Branch").flat(true);

	MarkerOptions marker1254 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.438327"), Double
							.parseDouble("3.365099")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Apapa Warehouse Branch").flat(true);

	MarkerOptions marker1255 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.438820"), Double
							.parseDouble("3.367943")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Apapa I Branch").flat(true);

	MarkerOptions marker1256 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.44264"), Double
							.parseDouble("3.376463")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Apapa III Branch").flat(true);

	MarkerOptions marker1257 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4691853"), Double
							.parseDouble("3.2409313")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Aspamda Trade Fair 2 Branch").flat(true);

	MarkerOptions marker1258 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4598084"), Double
							.parseDouble("3.216476")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Aspamda  Trade Fair Branch").flat(true);

	MarkerOptions marker1259 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4861217"), Double
							.parseDouble("3.3462882")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Babs Animashaun Street Branch").flat(true);

	MarkerOptions marker1260 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458662"), Double
							.parseDouble("3.1788538")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Bba-Tradefair Branch").flat(true);

	MarkerOptions marker1261 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6201498"), Double
							.parseDouble("3.2985721")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Berger Branch").flat(true);

	MarkerOptions marker1262 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4343212"), Double
							.parseDouble("3.4287478")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Bishop Aboyade Cole Branch").flat(true);

	MarkerOptions marker1263 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.423540"), Double
							.parseDouble("3.415010")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Bishop Oluwole Branch").flat(true);

	MarkerOptions marker1264 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.59794"), Double
							.parseDouble("3.396442")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Breadfruit Branch").flat(true);

	MarkerOptions marker1265 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453134"), Double
							.parseDouble("3.389955")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Broad Street Branch").flat(true);

	MarkerOptions marker1266 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.438201"), Double
							.parseDouble("3.368106")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Burma Branch").flat(true);

	MarkerOptions marker1267 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4471697"), Double
							.parseDouble("3.4191359")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Chevron Branch").flat(true);

	MarkerOptions marker1268 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455682"), Double
							.parseDouble("3.381729")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Corporate, Marina Branch").flat(true);

	MarkerOptions marker1269 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.531297"), Double
							.parseDouble("3.338601")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Daleko Branch").flat(true);

	MarkerOptions marker1270 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4608258"), Double
							.parseDouble("3.3858788")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Docemo Branch").flat(true);

	MarkerOptions marker1271 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458267"), Double
							.parseDouble("3.415138")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Dolphin Estate Branch").flat(true);

	MarkerOptions marker1272 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6593463"), Double
							.parseDouble("3.290123")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Dopemu 1 Branch").flat(true);

	MarkerOptions marker1273 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6593463"), Double
							.parseDouble("3.290123")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Dopemu 2 Branch").flat(true);

	MarkerOptions marker1274 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4831277"), Double
							.parseDouble("3.3866478")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ebute Metta Branch").flat(true);

	MarkerOptions marker1275 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.495384"), Double
							.parseDouble("3.3785")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ebutemetta 2 Branch").flat(true);

	MarkerOptions marker1276 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442627"), Double
							.parseDouble("3.375792")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ebute-Metta 4 Branch").flat(true);

	MarkerOptions marker1277 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.483472"), Double
							.parseDouble("3.382513")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ebute-Metta(Oyingbo) Branch").flat(true);

	MarkerOptions marker1278 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5505652"), Double
							.parseDouble("3.2662191")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ejigbo 2 Branch").flat(true);

	MarkerOptions marker1279 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5420902"), Double
							.parseDouble("3.3059102")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ejigbo Branch").flat(true);

	MarkerOptions marker1280 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.617472"), Double
							.parseDouble("3.35621")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Elephant Cement House Branch").flat(true);

	MarkerOptions marker1281 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442479"), Double
							.parseDouble("3.420340")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Falomo Branch").flat(true);

	MarkerOptions marker1282 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.471552"), Double
							.parseDouble("3.274113")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Festac II Branch").flat(true);

	MarkerOptions marker1283 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.470262"), Double
							.parseDouble("3.292430")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Festac Branch").flat(true);

	MarkerOptions marker1284 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.505717"), Double
							.parseDouble("3.377993")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Fola Agoro Branch").flat(true);

	MarkerOptions marker1285 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453812"), Double
							.parseDouble("3.384465")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Foundation Branch").flat(true);

	MarkerOptions marker1286 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.558397"), Double
							.parseDouble("3.391162")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Gbagada – Ifako Branch").flat(true);

	MarkerOptions marker1287 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.549108"), Double
							.parseDouble("3.377803")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Gbagada Expressway Branch").flat(true);

	MarkerOptions marker1288 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453812"), Double
							.parseDouble("3.384465")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Global Markets Branch").flat(true);

	MarkerOptions marker1289 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4372584"), Double
							.parseDouble("3.4251726")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("H/O Branch Branch").flat(true);

	MarkerOptions marker1290 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.471879"), Double
							.parseDouble("3.324806")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ibafon Branch").flat(true);

	MarkerOptions marker1291 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.515359"), Double
							.parseDouble("3.369289")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Iddo Branch").flat(true);

	MarkerOptions marker1292 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.433059"), Double
							.parseDouble("3.425095")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Idowu Taylor 2 Branch").flat(true);

	MarkerOptions marker1293 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.433161"), Double
							.parseDouble("3.429141")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Idowu Taylor Branch").flat(true);

	MarkerOptions marker1294 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.458846"), Double
							.parseDouble("3.389710")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Idumagbo Branch").flat(true);

	MarkerOptions marker1295 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4612576"), Double
							.parseDouble("3.3853384")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Idumota 2 Branch").flat(true);

	MarkerOptions marker1296 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453378"), Double
							.parseDouble("3.389908")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Idumota 3 Branch").flat(true);

	MarkerOptions marker1297 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.461626"), Double
							.parseDouble("3.388307")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Idumota 4 Branch").flat(true);

	MarkerOptions marker1298 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4627075"), Double
							.parseDouble("3.3871699")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Idumota 5 Branch").flat(true);

	MarkerOptions marker1299 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4624902"), Double
							.parseDouble("3.3843349")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Idumota Branch").flat(true);

	MarkerOptions marker1300 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.481618"), Double
							.parseDouble("3.363330")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Iganmu Branch").flat(true);

	MarkerOptions marker1301 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.450225"), Double
							.parseDouble("3.394831")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Igbosere Branch").flat(true);

	MarkerOptions marker1302 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.633040"), Double
							.parseDouble("3.341532")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ijaiye Ogba Branch").flat(true);

	MarkerOptions marker1303 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.468982"), Double
							.parseDouble("3.366602")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ijora Branch").flat(true);

	MarkerOptions marker1304 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.656860"), Double
							.parseDouble("3.323311")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Iju Road Branch").flat(true);

	MarkerOptions marker1305 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6127055"), Double
							.parseDouble("3.3521919")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikeja 1 Branch").flat(true);

	MarkerOptions marker1306 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.594849"), Double
							.parseDouble("3.339137")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikeja Computer Village Branch").flat(true);

	MarkerOptions marker1307 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.585291"), Double
							.parseDouble("3.351723")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikeja G.R.A. Branch").flat(true);

	MarkerOptions marker1308 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.602866"), Double
							.parseDouble("3.351214")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikeja 2 Branch").flat(true);

	MarkerOptions marker1309 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.597164"), Double
							.parseDouble("3.340230")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikeja Branch").flat(true);

	MarkerOptions marker1310 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.594850"), Double
							.parseDouble("3.338498")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikeja 3 Branch").flat(true);

	MarkerOptions marker1311 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.598016"), Double
							.parseDouble("3.353736")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikeja 4 Branch").flat(true);

	MarkerOptions marker1312 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.624301"), Double
							.parseDouble("3.4924105")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikorodu Branch").flat(true);

	MarkerOptions marker1313 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.557820"), Double
							.parseDouble("3.367232")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikorodu Road Branch").flat(true);

	MarkerOptions marker1314 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6212329"), Double
							.parseDouble("3.4995002")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikorodu Town Branch").flat(true);

	MarkerOptions marker1315 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.599411"), Double
							.parseDouble("3.373695")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikosi – Ketu Branch").flat(true);

	MarkerOptions marker1316 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4639538"), Double
							.parseDouble("3.5544102")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikota VGC Branch").flat(true);

	MarkerOptions marker1317 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5457751"), Double
							.parseDouble("3.2655734")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikotun 1 Branch").flat(true);

	MarkerOptions marker1318 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.561917"), Double
							.parseDouble("3.273772")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ikotun 2 Branch").flat(true);

	MarkerOptions marker1319 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.550982"), Double
							.parseDouble("3.355711")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ilupeju Branch").flat(true);

	MarkerOptions marker1320 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.610074"), Double
							.parseDouble("3.2869335")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ipaja Nysc Branch").flat(true);

	MarkerOptions marker1321 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4850477"), Double
							.parseDouble("3.361634")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Iponri Shopping Complex Branch").flat(true);

	MarkerOptions marker1322 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.549462"), Double
							.parseDouble("3.331832")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Isolo Branch").flat(true);

	MarkerOptions marker1323 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.511113"), Double
							.parseDouble("3.339342")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Itire Road Branch").flat(true);

	MarkerOptions marker1324 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442208"), Double
							.parseDouble("3.376386")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Kariko Towers Branch").flat(true);

	MarkerOptions marker1325 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.604298"), Double
							.parseDouble("3.390818")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ketu Branch").flat(true);

	MarkerOptions marker1326 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4535761"), Double
							.parseDouble("3.3872332")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Lagos Central Branch").flat(true);

	MarkerOptions marker1327 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453559"), Double
							.parseDouble("3.389415")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Lagos East Branch").flat(true);

	MarkerOptions marker1328 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4696317"), Double
							.parseDouble("3.1983754")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("LASU Branch").flat(true);

	MarkerOptions marker1329 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.511620"), Double
							.parseDouble("3.342598")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Lawanson Branch").flat(true);

	MarkerOptions marker1330 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.610245"), Double
							.parseDouble("3.2190811")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Lekki Oba Elegushi Int’l Mkt Branch").flat(true);

	MarkerOptions marker1331 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.447614"), Double
							.parseDouble("3.481414")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Lekki Phase 1 Branch").flat(true);

	MarkerOptions marker1332 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.44174"), Double
							.parseDouble("3.5286313")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Lekki (2) Protea Branch").flat(true);

	MarkerOptions marker1333 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4579625"), Double
							.parseDouble("3.5529103")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Lekki Branch").flat(true);

	MarkerOptions marker1334 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.516721"), Double
							.parseDouble("3.354041")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("LUTH Branch").flat(true);

	MarkerOptions marker1335 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.454655"), Double
							.parseDouble("3.383966")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Marina West Branch").flat(true);

	MarkerOptions marker1336 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453504"), Double
							.parseDouble("3.385662")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Marina Branch").flat(true);

	MarkerOptions marker1337 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.456024"), Double
							.parseDouble("3.387936")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Martins Branch").flat(true);

	MarkerOptions marker1338 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.574817"), Double
							.parseDouble("3.362084")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Maryland Branch").flat(true);

	MarkerOptions marker1339 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5393294"), Double
							.parseDouble("3.3427317")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Matori II Branch").flat(true);

	MarkerOptions marker1340 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.535233"), Double
							.parseDouble("3.348967")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Matori Branch").flat(true);

	MarkerOptions marker1341 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5818238"), Double
							.parseDouble("3.3189461")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("MMIA Arrival Hal Branch").flat(true);

	MarkerOptions marker1342 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4312292"), Double
							.parseDouble("3.4668622")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Mobil House Branch").flat(true);

	MarkerOptions marker1343 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5310315"), Double
							.parseDouble("3.3404728")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Mushin Branch").flat(true);

	MarkerOptions marker1344 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.572784"), Double
							.parseDouble("3.3221948")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("NAF Base, Ikeja Branch").flat(true);

	MarkerOptions marker1345 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4621589"), Double
							.parseDouble("3.283682")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Navy Town Branch").flat(true);
	MarkerOptions marker1346 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4485732"), Double
							.parseDouble("3.3920771")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Necom Branch").flat(true);

	MarkerOptions marker1347 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6371345"), Double
							.parseDouble("3.3509587")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("New Isheri Road Branch").flat(true);

	MarkerOptions marker1348 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.459599"), Double
							.parseDouble("3.370183")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("NNS Quorra Branch").flat(true);

	MarkerOptions marker1349 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601053"), Double
							.parseDouble("3.338636")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Oba Akran 2 Branch").flat(true);

	MarkerOptions marker1350 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.602002"), Double
							.parseDouble("3.338117")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Oba Akran 3 Branch").flat(true);

	MarkerOptions marker1351 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.601765"), Double
							.parseDouble("3.338247")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Oba Akran Branch").flat(true);

	MarkerOptions marker1352 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4496293"), Double
							.parseDouble("3.4077029")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Obalende Branch").flat(true);

	MarkerOptions marker1353 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.621752"), Double
							.parseDouble("3.336442")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ogba Branch").flat(true);

	MarkerOptions marker1354 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.575752"), Double
							.parseDouble("3.391100")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ogudu Branch").flat(true);

	MarkerOptions marker1355 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5038889"), Double
							.parseDouble("3.3492709")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ogunlana Drive Branch").flat(true);

	MarkerOptions marker1356 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6398349"), Double
							.parseDouble("3.3659236")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ojodu Branch").flat(true);

	MarkerOptions marker1357 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4595453"), Double
							.parseDouble("3.1851613")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ojo-Igbede Road Branch").flat(true);

	MarkerOptions marker1358 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5099134"), Double
							.parseDouble("3.3620459")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ojuelegba 2 Branch").flat(true);

	MarkerOptions marker1359 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.509923"), Double
							.parseDouble("3.363592")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ojuelegba 3 Branch").flat(true);

	MarkerOptions marker1360 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5130288"), Double
							.parseDouble("3.3642063")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Ojuelegba Branch").flat(true);

	MarkerOptions marker1361 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4601078"), Double
							.parseDouble("3.3824471")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Oke Arin 1 Branch").flat(true);

	MarkerOptions marker1362 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6316815"), Double
							.parseDouble("3.3380517")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Oke Arin 2 Branch").flat(true);

	MarkerOptions marker1363 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.455652"), Double
							.parseDouble("3.382715")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Oke-Arin 3 Branch").flat(true);

	MarkerOptions marker1364 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431580"), Double
							.parseDouble("2.887644")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Okokomaiko 2 Branch").flat(true);

	MarkerOptions marker1365 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.483865"), Double
							.parseDouble("3.170312")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Okokomaiko Branch").flat(true);

	MarkerOptions marker1366 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.651377"), Double
							.parseDouble("3.309810")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Oko-Oba Branch").flat(true);

	MarkerOptions marker1367 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.513850"), Double
							.parseDouble("3.321080")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Okota Branch").flat(true);

	MarkerOptions marker1368 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.551655"), Double
							.parseDouble("3.367418")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Onipanu Branch").flat(true);

	MarkerOptions marker1369 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.600111"), Double
							.parseDouble("3.364972")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Oregun 2 Branch").flat(true);

	MarkerOptions marker1370 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.608879"), Double
							.parseDouble("3.361202")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Oregun Branch").flat(true);

	MarkerOptions marker1371 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4423374"), Double
							.parseDouble("3.3761596")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Orile Coker Branch").flat(true);

	MarkerOptions marker1372 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.440923"), Double
							.parseDouble("3.331395")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Oshodi Branch").flat(true);

	MarkerOptions marker1373 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.549462"), Double
							.parseDouble("3.331832")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Osolo Branch").flat(true);

	MarkerOptions marker1374 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.6769583"), Double
							.parseDouble("3.2312403")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Otta Branch").flat(true);

	MarkerOptions marker1375 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.42634"), Double
							.parseDouble("3.4281013")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Oyin Jolayemi Branch").flat(true);

	MarkerOptions marker1376 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.535592"), Double
							.parseDouble("3.348665")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Palm Avenue Branch").flat(true);

	MarkerOptions marker1377 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5425441"), Double
							.parseDouble("3.3656278")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Palmgrove Branch").flat(true);

	MarkerOptions marker1378 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4634976"), Double
							.parseDouble("3.2911616")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Passport Office Branch").flat(true);

	MarkerOptions marker1379 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4487406"), Double
							.parseDouble("3.3649384")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Prestige Apapa Branch").flat(true);

	MarkerOptions marker1380 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4556131"), Double
							.parseDouble("3.3807467")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Retail Financial Services Branch").flat(true);

	MarkerOptions marker1381 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.3803005"), Double
							.parseDouble("2.7070355")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Seme Border Branch").flat(true);

	MarkerOptions marker1382 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4536498"), Double
							.parseDouble("3.4005934")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Simpson Branch").flat(true);

	MarkerOptions marker1383 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4893874"), Double
							.parseDouble("3.3554229")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Surulere Branch").flat(true);

	MarkerOptions marker1384 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4980412"), Double
							.parseDouble("3.35871")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Surulere 2 Branch").flat(true);

	MarkerOptions marker1385 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.452344"), Double
							.parseDouble("3.389301")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Tinubu Branch").flat(true);

	MarkerOptions marker1386 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4581793"), Double
							.parseDouble("3.24797")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Trade Fair Branch").flat(true);

	MarkerOptions marker1387 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4602513"), Double
							.parseDouble("3.3057671")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Tradefair Branch").flat(true);

	MarkerOptions marker1388 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.453812"), Double
							.parseDouble("3.384465")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("UBA Custody Branch").flat(true);

	MarkerOptions marker1389 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.4536504"), Double
							.parseDouble("3.3872484")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("UBA House").flat(true);

	MarkerOptions marker1390 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.5192397"), Double
							.parseDouble("3.3895685")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("UNILAG Branch").flat(true);

	MarkerOptions marker1391 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.431597"), Double
							.parseDouble("3.42458")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Victoria Island Branch").flat(true);

	MarkerOptions marker1392 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.430585"), Double
							.parseDouble("3.4254182")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Victoria Island 2 Branch").flat(true);

	MarkerOptions marker1393 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.438309"), Double
							.parseDouble("3.365059")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Warehouse Road Branch").flat(true);

	MarkerOptions marker1394 = new MarkerOptions()
			.position(
					new LatLng(Double.parseDouble("6.442248"), Double
							.parseDouble("3.3741085")))
			.icon(BitmapDescriptorFactory

			.fromResource(R.drawable.uba))

			.title("Wharf Road Branch").flat(true);
	
	
	googleMap.addMarker(marker1201);
	googleMap.addMarker(marker1202);
	googleMap.addMarker(marker1203);
	googleMap.addMarker(marker1204);
	googleMap.addMarker(marker1205);
	googleMap.addMarker(marker1206);
	googleMap.addMarker(marker1207);
	googleMap.addMarker(marker1208);
	googleMap.addMarker(marker1209);
	googleMap.addMarker(marker1210);
	googleMap.addMarker(marker1211);
	googleMap.addMarker(marker1212);
	googleMap.addMarker(marker1213);
	googleMap.addMarker(marker1214);
	googleMap.addMarker(marker1215);
	googleMap.addMarker(marker1216);
	googleMap.addMarker(marker1217);
	googleMap.addMarker(marker1218);
	googleMap.addMarker(marker1219);
	googleMap.addMarker(marker1220);
	googleMap.addMarker(marker1221);
	googleMap.addMarker(marker1222);
	googleMap.addMarker(marker1223);
	googleMap.addMarker(marker1224);
	googleMap.addMarker(marker1225);
	googleMap.addMarker(marker1226);
	googleMap.addMarker(marker1227);
	googleMap.addMarker(marker1228);
	googleMap.addMarker(marker1229);
	googleMap.addMarker(marker1230);
	googleMap.addMarker(marker1231);
	googleMap.addMarker(marker1232);
	googleMap.addMarker(marker1233);
	googleMap.addMarker(marker1234);
	googleMap.addMarker(marker1235);
	googleMap.addMarker(marker1236);
	googleMap.addMarker(marker1237);
	googleMap.addMarker(marker1238);
	googleMap.addMarker(marker1239);
	googleMap.addMarker(marker1240);
	googleMap.addMarker(marker1241);
	googleMap.addMarker(marker1242);
	googleMap.addMarker(marker1243);
	googleMap.addMarker(marker1244);
	googleMap.addMarker(marker1245);
	googleMap.addMarker(marker1246);
	googleMap.addMarker(marker1247);
	googleMap.addMarker(marker1248);
	googleMap.addMarker(marker1249);
	googleMap.addMarker(marker1250);
	googleMap.addMarker(marker1251);
	googleMap.addMarker(marker1252);
	googleMap.addMarker(marker1253);
	googleMap.addMarker(marker1254);
	googleMap.addMarker(marker1255);
	googleMap.addMarker(marker1256);
	googleMap.addMarker(marker1257);
	googleMap.addMarker(marker1258);
	googleMap.addMarker(marker1259);
	googleMap.addMarker(marker1260);
	googleMap.addMarker(marker1261);
	googleMap.addMarker(marker1262);
	googleMap.addMarker(marker1263);
	googleMap.addMarker(marker1264);
	googleMap.addMarker(marker1265);
	googleMap.addMarker(marker1266);
	googleMap.addMarker(marker1267);
	googleMap.addMarker(marker1268);
	googleMap.addMarker(marker1269);
	googleMap.addMarker(marker1270);
	googleMap.addMarker(marker1271);
	googleMap.addMarker(marker1272);
	googleMap.addMarker(marker1273);
	googleMap.addMarker(marker1274);
	googleMap.addMarker(marker1275);
	googleMap.addMarker(marker1276);
	googleMap.addMarker(marker1277);
	googleMap.addMarker(marker1278);
	googleMap.addMarker(marker1279);
	googleMap.addMarker(marker1280);
	googleMap.addMarker(marker1281);
	googleMap.addMarker(marker1282);
	googleMap.addMarker(marker1283);
	googleMap.addMarker(marker1284);
	googleMap.addMarker(marker1285);
	googleMap.addMarker(marker1286);
	googleMap.addMarker(marker1287);
	googleMap.addMarker(marker1288);
	googleMap.addMarker(marker1289);
	googleMap.addMarker(marker1290);
	googleMap.addMarker(marker1291);
	googleMap.addMarker(marker1292);
	googleMap.addMarker(marker1293);
	googleMap.addMarker(marker1294);
	googleMap.addMarker(marker1295);
	googleMap.addMarker(marker1296);
	googleMap.addMarker(marker1297);
	googleMap.addMarker(marker1298);
	googleMap.addMarker(marker1299);
	googleMap.addMarker(marker1300);
	googleMap.addMarker(marker1301);
	googleMap.addMarker(marker1302);
	googleMap.addMarker(marker1303);
	googleMap.addMarker(marker1304);
	googleMap.addMarker(marker1305);
	googleMap.addMarker(marker1306);
	googleMap.addMarker(marker1307);
	googleMap.addMarker(marker1308);
	googleMap.addMarker(marker1309);
	googleMap.addMarker(marker1310);
	googleMap.addMarker(marker1311);
	googleMap.addMarker(marker1312);
	googleMap.addMarker(marker1313);
	googleMap.addMarker(marker1314);
	googleMap.addMarker(marker1315);
	googleMap.addMarker(marker1316);
	googleMap.addMarker(marker1317);
	googleMap.addMarker(marker1318);
	googleMap.addMarker(marker1319);
	googleMap.addMarker(marker1320);
	googleMap.addMarker(marker1321);
	googleMap.addMarker(marker1322);
	googleMap.addMarker(marker1323);
	googleMap.addMarker(marker1324);
	googleMap.addMarker(marker1325);
	googleMap.addMarker(marker1326);
	googleMap.addMarker(marker1327);
	googleMap.addMarker(marker1328);
	googleMap.addMarker(marker1329);
	googleMap.addMarker(marker1330);
	googleMap.addMarker(marker1331);
	googleMap.addMarker(marker1332);
	googleMap.addMarker(marker1333);
	googleMap.addMarker(marker1334);
	googleMap.addMarker(marker1335);
	googleMap.addMarker(marker1336);
	googleMap.addMarker(marker1337);
	googleMap.addMarker(marker1338);
	googleMap.addMarker(marker1339);
	googleMap.addMarker(marker1340);
	googleMap.addMarker(marker1341);
	googleMap.addMarker(marker1342);
	googleMap.addMarker(marker1343);
	googleMap.addMarker(marker1344);
	googleMap.addMarker(marker1345);
	googleMap.addMarker(marker1346);
	googleMap.addMarker(marker1347);
	googleMap.addMarker(marker1348);
	googleMap.addMarker(marker1349);
	googleMap.addMarker(marker1350);
	googleMap.addMarker(marker1351);
	googleMap.addMarker(marker1352);
	googleMap.addMarker(marker1353);
	googleMap.addMarker(marker1354);
	googleMap.addMarker(marker1355);
	googleMap.addMarker(marker1356);
	googleMap.addMarker(marker1357);
	googleMap.addMarker(marker1358);
	googleMap.addMarker(marker1359);
	googleMap.addMarker(marker1360);
	googleMap.addMarker(marker1361);
	googleMap.addMarker(marker1362);
	googleMap.addMarker(marker1363);
	googleMap.addMarker(marker1364);
	googleMap.addMarker(marker1365);
	googleMap.addMarker(marker1366);
	googleMap.addMarker(marker1367);
	googleMap.addMarker(marker1368);
	googleMap.addMarker(marker1369);
	googleMap.addMarker(marker1370);
	googleMap.addMarker(marker1371);
	googleMap.addMarker(marker1372);
	googleMap.addMarker(marker1373);
	googleMap.addMarker(marker1374);
	googleMap.addMarker(marker1375);
	googleMap.addMarker(marker1376);
	googleMap.addMarker(marker1377);
	googleMap.addMarker(marker1378);
	googleMap.addMarker(marker1379);
	googleMap.addMarker(marker1380);
	googleMap.addMarker(marker1381);
	googleMap.addMarker(marker1382);
	googleMap.addMarker(marker1383);
	googleMap.addMarker(marker1384);
	googleMap.addMarker(marker1385);
	googleMap.addMarker(marker1386);
	googleMap.addMarker(marker1387);
	googleMap.addMarker(marker1388);
	googleMap.addMarker(marker1389);
	googleMap.addMarker(marker1390);
	googleMap.addMarker(marker1391);
	googleMap.addMarker(marker1392);
	googleMap.addMarker(marker1393);
	googleMap.addMarker(marker1394);
		}
	}

}
