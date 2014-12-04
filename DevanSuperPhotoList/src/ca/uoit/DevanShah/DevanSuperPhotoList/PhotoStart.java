package ca.uoit.DevanShah.DevanSuperPhotoList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * This class is the main class of the project and is used to create the main 
 * activity and fill all the data. This class also handles the Google Play Services
 * connections. These connection were based of Google example that was provided and
 * told to use.
 * @author Devan Shah 100428864
 *
 */
@SuppressLint("SimpleDateFormat")
public class PhotoStart extends Activity implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener
{
	// Photo capture constants 
    static final int REQUEST_PHOTO_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    
	// Date Structure Deceleration
	public Vector<Object> photos;
	
	// Object Deceleration
	public PhotosView myPhotesView;
	public PhotosList myPhotosList;
	
	// Create the File handle for the file that is checked if an restore is needed
	public File photosRawDataFile;
	
	// Global variable declaration
	public static String myCurrentPhotoPath;
	public static String myCurrentPhotoGeoLocation;
	public static String myCurrentPhotoFileName;
	public static String myCurrentPhotoDirectory;
	public static Bitmap photoCaptureBitmap;
	public static int listPosition;
	public static double LATITUDE;
	public static double LONGITUDE;
	
	/****************************************** GOOGLE PLAY SERVICES VARIABLES START ***********************************/
	// This block falls under Copyright (C) 2013 The Android Open Source Project
	
	// A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;
    
    /*
     * Note if updates have been turned on. Starts out as "false"; is set to "true" in the
     * method handleRequestSuccess of LocationUpdateReceiver.
     */
    boolean mUpdatesRequested = false;
    
    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;

    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;
    /****************************************** GOOGLE PLAY SERVICES VARIABLES END ***********************************/
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_start);
        
        /**************************************************** Initialize GOOGLE PLAY SERVICES variables Start *****************************/
        // This block falls under Copyright (C) 2013 The Android Open Source Project
        
        // Create a new global location parameters object
        mLocationRequest = LocationRequest.create();

        // Set update interval
        mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        // Note that location updates are off until the user turns them on
        mUpdatesRequested = false;

        // Open Shared Preferences
        mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        // Get an editor
        mEditor = mPrefs.edit();

        // Create the location client
        mLocationClient = new LocationClient(this, this, this);
        
        /***************************************** Initialize GOOGLE PLAY SERVICES variables End **************************************/
        
        photosRawDataFile = new File( getFilesDir(), "PhotosRawData.ser");
        
        // Initialize the photo Vector
     	photos = new Vector<Object>();
     	
		// Restore the photos from the internal application storage file if available.
     	restorePhotosHistoryFromInternalStorage();
		
		// Create the new photo list fragment
		myPhotosList = new PhotosList();

		// Start the fragment transaction to place the fragment into the activity
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		
		// add the photo list fragment to the activity by replacing the FramLayout
		transaction.replace(R.id.photos_list, myPhotosList, "PhotosList");
		transaction.commit(); // Commit the changes
		setContentView(R.layout.activity_photo_start); // Set the view for the activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_start, menu);
        return true;
    }

	/**
	 * This function is used to handle when buttons in the action bar are
	 * selected. Supports capture photo.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		/**
		 * Switch through the possible options on the action bar and perform the
		 * actions accordingly.
		 */
		switch ( item.getItemId() ) 
		{
			// Handle when the camera icon in the action bar is clicked
			case R.id.camera:
				
				// Dispatch the take picture intent, to take a picture and save it in a file.
				dispatchTakePictureIntent();
				
				return true;
				
			default:
				
				return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * When the activity is stopped make sure to disconnect from the 
	 * Google Play Services.
	 */
    @Override
    public void onStop() {

        // If the client is connected
        if ( mLocationClient.isConnected() ) 
        {
        	 mLocationClient.removeLocationUpdates(this);
        }

        // Kill the Location client
        mLocationClient.disconnect();

        super.onStop();
    }
    
    /**
     * When the activity is paused make sure to request an update.
     */
    @Override
    public void onPause() {

        // Save the current setting for updates
        mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, mUpdatesRequested);
        mEditor.commit();

        super.onPause();
    }
    
    /**
     * When the activity starts make sure to start the location client
     */
    @Override
    public void onStart() 
    {
        super.onStart();

        // Connect to the client
        mLocationClient.connect();
    }
    
    /**
     * When the activity is resumed make sure to get and updates that are needed.
     */
    @Override
    public void onResume() 
    {
        super.onResume();

        // Get updates if needed
        if ( mPrefs.contains(LocationUtils.KEY_UPDATES_REQUESTED) ) 
        {
            mUpdatesRequested = mPrefs.getBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
        } 
        else {
            mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
            mEditor.commit();
        }
    }
    
    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() 
    {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) 
        {
            // In debug mode, log the status
            Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));

            return true;
        } 
        else 
        {
            return false;
        }
    }
    
    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    /**
     * This function is called by the location service. When 
     * there is a connection.
     * @param bundle
     */
    @Override
    public void onConnected ( Bundle bundle ) 
    {
    	// Update the location on conectio always
        if (true) 
        {
        	mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }
    }

    @Override
    public void onDisconnected() {
        
    }

    /**
     * Handles errors. Current not handling any.
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) 
    {

    }
    
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}
    
	/**
	 * 
	 */
    private void dispatchTakePictureIntent()
    {
    	// Create a new intent to switch to the camera application
        Intent capturePhotoIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        
        if ( capturePhotoIntent.resolveActivity( getPackageManager() ) != null) 
        {   
            // Variable that stores the File where the photo will be saved.
            File constructedPhotoFile = null;

            // Handle creating the file and storing it into variable 
            try 
            {
                constructedPhotoFile = PhotoSaverCreator.createPhotoFile();
                
                // Set the path to the current photo for Photo Info creation
                myCurrentPhotoPath = constructedPhotoFile.getAbsolutePath();
                
                myCurrentPhotoGeoLocation = getMyLocationAddress();
              
            }
            // Error occurred while creating the File
            catch ( IOException e ) { System.out.println("Message:" + e.getMessage()); e.printStackTrace(); }
            
            // Only put the data into the intent if the photo file was constructed successfully
            if ( constructedPhotoFile != null ) 
            {
            	// Put the URI URI of the image into the photo intent 
                capturePhotoIntent.putExtra ( MediaStore.EXTRA_OUTPUT, Uri.fromFile(constructedPhotoFile) );
                startActivityForResult ( capturePhotoIntent, REQUEST_TAKE_PHOTO );
            }
        }
    }
    
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) 
    {	
    	if ( resultCode == Activity.RESULT_OK )
    	{
            PhotoInfo photoInformation = new PhotoInfo( myCurrentPhotoGeoLocation, myCurrentPhotoPath, myCurrentPhotoFileName );
            photos.addElement(photoInformation);
    	}
    	
        // Save the photo Vector to a file, for restore later.
        savePhotoHistoryInInternalStorage() ;
        
		// Create the new photo list fragment
		myPhotosList = new PhotosList();
		
		// Start the fragment transaction to place the fragment into the
		// activity
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		
		// add the notes list fragment to the activity by replacing the
		// FramLayout
		transaction.replace(R.id.photos_list, myPhotosList, "PhotosList");
		transaction.commit(); // Commit the changes	
    }
    
	/**
	 * This function is used to handle the action to be performed when item in
	 * the list view is clicked.
	 * 
	 * @param position
	 *            - position value from the list view
	 */
	public void onPhotoClicked ( int position ) 
	{
		listPosition = position ;
		
		// Create the new notes description with empty title and content
		myPhotesView = new PhotosView();
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		
		transaction.replace(R.id.photos_list, myPhotesView, "PhotoView");
		transaction.commit(); // Commit the changes
	}
	
	/**
	 * This function is used to restore the notes saved by the user from the
	 * internal application file.
	 */
	@SuppressWarnings("unchecked")
	public void restorePhotosHistoryFromInternalStorage() 
	{
		// Perform the restore only if the file exists.
		if ( photosRawDataFile.exists() ) 
		{
			// Variable deceleration
			ObjectInputStream photosRawDataRestore;

			try {
				// Open the stream to retrieve the saved notes from the file.
				photosRawDataRestore = new ObjectInputStream(
						new FileInputStream(photosRawDataFile.getAbsoluteFile()));

				// Read the data in the file and store it in the notes vector.
				photos = (Vector<Object>) photosRawDataRestore.readObject();

				photosRawDataRestore.close(); // Close the stream.
			} 
			catch (IOException e) { e.printStackTrace(); } 
			catch (ClassNotFoundException e) { e.printStackTrace(); }
		}
	}
	
	/**
	 * Used to store the Vector of notes to a file located on the internal
	 * storage of the device. Saves using serialization to make the data secure.
	 */
	public void savePhotoHistoryInInternalStorage() 
	{
		// variable deceleration
		ObjectOutputStream photoRawDataOut;

		try {
			
			// Construct the stream to write the vector of notes saved already.
			photoRawDataOut = new ObjectOutputStream(new FileOutputStream(
					photosRawDataFile.getAbsoluteFile()));
			photoRawDataOut.writeObject(photos); // Write the object
			photoRawDataOut.flush(); // flush the stream to make sure everything is written.
			photoRawDataOut.close(); // Close the stream
		}
		catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * Delete the photo in the selected position.
	 * @param position
	 */
	public void onDelete(int position) 
	{
		// Only remote elements if a position is positive,
		// to avoid null expections
		if (position >= 0) {
			photos.remove(position);
		}
		
		// Create the new photos list with
		myPhotosList = new PhotosList();

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.photos_list, myPhotosList, "PhotosList");
		transaction.commit(); // Commit the changes

		// Save the photos vector to the internal storage.
		savePhotoHistoryInInternalStorage();
	}

	/**
	 * This function is used to cancel the operation of viewing the photo
	 */
	public void onCancel() 
	{
		// Create the new photo list with
		myPhotosList = new PhotosList();

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.photos_list, myPhotosList, "PhotosList");
		transaction.commit(); // Commit the changes
	}
	
	/**
	 * Get the current location using Google Play Services or Location Manager
	 * depending on which one is available.
	 * @return location - the location in either address or longitude and latitude
	 */
	public String getMyLocationAddress() 
	{
		// Variable deceleration
		String location = "Location Not Found";
		
        // If Google Play Services is available and connected to 
        if ( servicesConnected() ) {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();

            if ( currentLocation != null ) 
            {
                // Get the current location information
                LONGITUDE = currentLocation.getLongitude();
                LATITUDE = currentLocation.getLatitude();
            }
            else {
            	// Use location Manager if Google Play Services was not able to provide data.
                PhotoLocationManager photoLocationManager = new PhotoLocationManager(this);
                photoLocationManager.locationManager();
            }
        }
        else {
        	// Use location Manager if Google Play Services was not able to provide data.
            PhotoLocationManager photoLocationManager = new PhotoLocationManager(this);
            photoLocationManager.locationManager();
        }
        
		try {

			// Use the geocoder to get the address 
			if ( Geocoder.isPresent() == true ) 
			{
				// Initialize the geocoder if it is available 
				Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
				
				// Using the latitude and longitude try to get the full address
				List<Address> addresses = geocoder.getFromLocation(LATITUDE,LONGITUDE, 1);

				// Only try to access the addresses list of data is present
				if ( addresses != null && addresses.size() > 0 ) 
				{
					// Get the Address object
					Address foundAddress = addresses.get(0) ;

					location = foundAddress.getLocality(); // Get the City
					location += ", " + foundAddress.getAdminArea(); // Get the Major Area (Ontario)
					location += ", " + foundAddress.getCountryCode(); // Get the country code
				}
				else {
					location = "Location Not Found"; // Return when no location found
				}
			}
			else {
				location = Double.toString(LATITUDE) + ", " + Double.toString(LONGITUDE);
			}
		}
		catch (IOException e) {
			// Try to use Location Manager if an IO Exception is hit
			PhotoLocationManager photoLocationManager = new PhotoLocationManager(this);
            photoLocationManager.locationManager();
            location = Double.toString(LATITUDE) + ", " + Double.toString(LONGITUDE);
		}
		
		return location;
	}
}
