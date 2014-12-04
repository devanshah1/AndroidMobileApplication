package ca.uoit.DevanShah.DevanSuperPhotoList;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * This class is used to save and create the photo. Also used to 
 * resize the photo.
 * @author Devan Shah 100428864
 *
 */
@SuppressLint("SimpleDateFormat") 
public class PhotoSaverCreator 
{
	/**
	 * This function is used to create the photo file under a specific location in the 
	 * Picture directory.
	 * @return photoFile - return the photoFile File object
	 * @throws IOException
	 */
    public static File createPhotoFile() throws IOException 
    {
    	// General storage for photos created in DevanSuperPhotoList application
    	String APP_PHOTO_DIR = "/DevanSuperPhotoList/";
    	
        // Create an image file name
    	String timeStamp = new SimpleDateFormat("MMddyyyyHHmmss").format(new Date());
    	PhotoStart.myCurrentPhotoFileName = "IMG" + timeStamp + ".jpg" ;
        
        // Create the File object for the photo storage directory for this application
        File appPhotoStorageDir = new File ( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + APP_PHOTO_DIR );
        
        // Store the global directory of photo
        PhotoStart.myCurrentPhotoDirectory = appPhotoStorageDir.getAbsolutePath();
        
        /**
         * Before making the file for the photo make sure that the photo application directory exists.
         * If it does not exist then create the directory.
         */
        if ( appPhotoStorageDir != null ) 
        {
        	if ( ! appPhotoStorageDir.mkdirs() ) 
        	{
        		if ( ! appPhotoStorageDir.exists() )
        		{
        			Log.d("DevanSuperPhotoList", "Failed to create directory: " + appPhotoStorageDir.getAbsolutePath());
        		}
        	}
        }
        
        // After directory is created create the photo file now
        File photoFile = new File ( appPhotoStorageDir.getAbsolutePath() + "/" + PhotoStart.myCurrentPhotoFileName );
        
        return photoFile;
    }
    
    /**
     * This function is used to resize the images and return the resized image.
     * @return bitmap of the image that was resized
     */
    public static Bitmap resizePhoto( String photoPath, int width, int height ) 
    {
    	// Variables deceleration
		int scaleFactor = 1;
		int photoW;
		int photoH;
		
		// Set the size the image needs to be
		int targetW = width;
		int targetH = height;

		// Get the current size of the image
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoPath, bmOptions);
		photoW = bmOptions.outWidth;
		photoH = bmOptions.outHeight;
		
		// Determine a new scale factor based on the target and photo
		// height and width.
		if ( (targetW > 0) || (targetH > 0) ) 
		{
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}
		
		// Set bitmap options to scale the image decode target
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;

		// Decode the file and return the bitmap
		return BitmapFactory.decodeFile(photoPath, bmOptions);
	}
}
