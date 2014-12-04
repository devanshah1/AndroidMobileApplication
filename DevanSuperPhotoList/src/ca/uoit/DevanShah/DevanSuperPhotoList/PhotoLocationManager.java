package ca.uoit.DevanShah.DevanSuperPhotoList;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * This class is used to setup the location manager and get the location information.
 * @author Devan Shah 100428864
 *
 */
public class PhotoLocationManager 
{
	// Declare the main activity that is passed in
	public Activity mainActivity;
	
	/**
	 * The default constructor
	 * @param mainActivity - the activity that is calling this
	 */
	public PhotoLocationManager( Activity mainActivity )
	{
		this.mainActivity = mainActivity;
	}
	
	/**
	 * This function is used to set up the location manager and enable the listener 
	 * to get the latest location.
	 */
	public void locationManager() 
	{
		// define the location manager
		final LocationManager manager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
		
		final LocationListener listener = new LocationListener() 
		{
			@Override
			public void onLocationChanged(Location location) 
			{
				// get the latitude and longitude when the location changes.
				PhotoStart.LATITUDE = location.getLatitude();
				PhotoStart.LONGITUDE = location.getLongitude();
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
			}
		};
		
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
	}
}
