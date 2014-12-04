package ca.uoit.DevanShah.DevanSuperPhotoList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * This class is used to create the photo list fragment.
 * @author Devan Shah 100428864
 *
 */
public class PhotosList extends Fragment implements Serializable 
{
    /**
     * Default serialization constant for this object.
     */
    private static final long serialVersionUID = 1L ;
    
    // Date Structure Deceleration
	List<PhotoInfo> photosExtracted;
	Vector<Object> photos;
	
	// Stores the main activity
	public static Activity myPhotosStartActivity;
	
	/**
	 * Default constructor
	 */
	public PhotosList() {

	}
	
	/**
	 * This function is used to create the fragment view with some important data for the 
	 * fragment.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// Grab the inflate the view and store it 
		View view = inflater.inflate(R.layout.photos_list, container, false);
		
		// Get the main activity
		myPhotosStartActivity = getActivity();
		
		// get the photos vector from the main activity
		photos = ((PhotoStart) myPhotosStartActivity).photos;
		
		// Initialize the ArrayList<PhotoInfo>()
		photosExtracted = new ArrayList<PhotoInfo>();
		
		// Build the List<PhotoInfo> using the photo object
		buildPhotosArray();
		CustomImageHandlerAdapter photosAdapter = new CustomImageHandlerAdapter ( myPhotosStartActivity, 
																				  R.layout.listviewrow, 
																				  (ArrayList<PhotoInfo>) photosExtracted 
																				) ;
		// Grab the list view so that it can be placed in
		ListView listView = (ListView) view.findViewById(R.id.listView1);
		listView.setAdapter(photosAdapter); // Set the adapter in the list view
		listView.setOnItemClickListener(photoSelected); // Enable the on click listener when entries in the list view are clicked
		
		return view;
	}
	
	/**
	 * This function is used to perform the action when a row is clicked in the list view.
	 */
	private OnItemClickListener photoSelected = new OnItemClickListener() 
	{
		// On item click for the list view
		public void onItemClick ( AdapterView<?> parent, View v, int position, long id ) 
		{	
			// Perform the action for on photo clicked, call the function from the main activity.
			((PhotoStart) myPhotosStartActivity).onPhotoClicked(position);
		}
	};
	
	/**
	 * This function is used to create the ArrayList<PhotoInfo>() using Vector<Object> of photos
	 */
	private void buildPhotosArray() {
		
		// Sort the entries that are in the photos vector, by date. This is to display the latest photo first.
        Collections.sort ( photos, new Comparator<Object>() 
        {
        	  // Define a compare function that calls the compareTo available in the PhotoInfo class. (for each photo)
        	  public int compare ( Object a, Object b ) 
        	  {
        		// Return the values of compare conditions, 1, -1, 0  
        	    return ( ((PhotoInfo) a).compareTo( (PhotoInfo) b));
        	  }
        });
        
		// Loop through the photos vector and build a ArrayList
        for ( int i = 0; i < photos.size(); i++ )
        {	
        	// Grab the photoInfo entry
        	PhotoInfo photoEntry = ( ( PhotoInfo ) photos.elementAt (i) ) ;
            
            // Put the PhotoInfo entry into the ArrayList
        	photosExtracted.add(photoEntry);
        }
	}
}
