package ca.uoit.DevanShah.DevanSuperPhotoList;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * This class is used to store the general information of an image
 * @author Devan Shah 100428864
 *
 */
public class PhotoInfo implements Serializable, Comparable<PhotoInfo>
{

    /**
     * Default serialization constant for this object.
     */
    private static final long serialVersionUID = 1L ;
    
    // Variable Deceleration
	public String photoGeoLocation;
	public String photoPathOnDevice;
	public String photoFileName;
	public Date photoCreationDate;
	
	// Date formatter
	static DateFormat TimeFormatter = DateFormat.getTimeInstance ();
	static DateFormat DateFormatter = DateFormat.getDateInstance();
	
	/**
	 * Default constructor that is used to initialize the values.
	 * @param photoThumbnail
	 * @param photoGeoLocation
	 * @param photosDirectory
	 * @param photoFileName
	 */
	public PhotoInfo ( String photoGeoLocation, String photoPathOnDevice, String photoFileName) 
	{
		this.photoGeoLocation  = photoGeoLocation;
		this.photoPathOnDevice = photoPathOnDevice;
		this.photoFileName     = photoFileName;
		this.photoCreationDate = new Date() ;
	}
	
	/**
	 * Return the photo's geo location
	 * @return photoGeoLocation
	 */
	public String getPhotoGeoLocation() 
	{
		return photoGeoLocation;
	}
	
	/**
	 * Return the photo's path on the device
	 * @return photoPathOnDevice
	 */
	public String getPhotoDirectory() 
	{
		return photoPathOnDevice;
	}
	
	/**
	 * Return the photo's file name
	 * @return photoFileName
	 */
	public String getPhotoFileName() 
	{
		return photoFileName;
	}
	
	/**
	 * This function is used to get the creation date of the photo. 
	 * This function provides the date based on the date and formats it accordingly. 
	 * Then the day is the same as current the time is returned only, otherwise 
	 * only the data is returned. 
	 * @return
	 */
	public String getPhotoCreationDate() 
	{
		// Get the current date
		Date currentDate = new Date();
		
		// If the date is still current only return the time
		if ( DateFormatter.format(currentDate).equals(DateFormatter.format(photoCreationDate)))
		{
			// Format the time and return it 
			return TimeFormatter.format(photoCreationDate) + " >";
		}
		// When the date is not current return only the date
		else {
			// format the date and return it
			return DateFormatter.format(photoCreationDate) + " >";
		}
	}

	/**
	 * This function is used to compare 2 photo objects on the data of each of the objects.
	 */
	@Override
	public int compareTo ( PhotoInfo another ) 
	{	
		// If the date of this photo is after the provided photo then return -1
		if ( this.photoCreationDate.after(another.photoCreationDate))
		{
			return -1;
		}
		// When the date of this photo is before the provided photo then return 1
		else if ( this.photoCreationDate.before(another.photoCreationDate))
		{
			return 1;
		}
		// Otherwise return 0 (photo is the same)
		else {
			return 0;	
		}
	}
}