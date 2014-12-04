package ca.uoit.DevanShah.DevanSuperPhotoList;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This is the custom adapter that is uses to display the information in a list view.
 * @author Devan Shah 100428864
 *
 */
public class CustomImageHandlerAdapter extends ArrayAdapter<PhotoInfo> 
{
	// Declare the global objects
	ArrayList<PhotoInfo> photos;
	Context context;
	
	/**
	 * Default constructor of the CustomImageHandlerAdapter class
	 * @param context - the activity
	 * @param resource - the resource (layout, configuration)
	 * @param photos - The list of photo information to be displayed
	 */
	public CustomImageHandlerAdapter(Context context, int resource, ArrayList<PhotoInfo> photos ) 
	{
		super ( context, resource, photos );
		this.photos = photos;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// Define the view
		View itemView = convertView;
		
		// Online set up the view if the view is not empty 
		if (itemView == null) 
		{
			// Inflate the view
			itemView = ((Activity) context).getLayoutInflater().inflate(R.layout.listviewrow, parent, false);
		}
		
		// Add data to the list view if the photos list is not empty
		if ( ! photos.isEmpty() )
		{
			// Get the current photo that will be represented on the list view
			PhotoInfo currentPhoto = photos.get(position);
			
			// Fill the image view with the photo
			ImageView imageView = (ImageView) itemView.findViewById(R.id.photoThumbnail);
			imageView.setImageBitmap((Bitmap) PhotoSaverCreator.resizePhoto((String) currentPhoto.getPhotoDirectory(), imageView.getWidth(), imageView.getHeight()));
			
			// Fill the text view with the file name of the photo
			TextView makeText = (TextView) itemView.findViewById(R.id.PHOTO_FILE_NAME);
			makeText.setText((CharSequence) currentPhoto.getPhotoFileName());

			// Fill the text view with the geo location of the photo
			TextView yearText = (TextView) itemView.findViewById(R.id.GEO_LOCATION_CELL);
			yearText.setText((CharSequence) currentPhoto.getPhotoGeoLocation());
			
			// Fill the text view with the creation date of the photo
			TextView condionText = (TextView) itemView.findViewById(R.id.DATE_CELL);
			condionText.setText((CharSequence) currentPhoto.getPhotoCreationDate());	
		}

		return itemView;
	}			
}
