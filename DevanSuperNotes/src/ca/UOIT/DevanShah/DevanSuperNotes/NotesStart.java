package ca.UOIT.DevanShah.DevanSuperNotes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This class is used to perform the action for the main activity.
 * @author Devan Shah 100428864
 *
 */
@SuppressLint({ "UseSparseArrays", "UseValueOf", "NewApi" })
public class NotesStart extends ActionBarActivity {

	// Variable Deceleration
	public static String action;
	public static int listPosition;
	
	// Date Structure Deceleration
	public Vector<Object> notes;
	
	// Object Deceleration
	public NotesDescription myNotesDescription;
	public NotesList myNotesList;
	public File notesRawDataFile ;
	
	// Variable Tags
    private final String actionTag = "actionTag";
    private final String listPositionTag = "listPositionTag" ;
    
	/**
	 * This function is used to create the activity. This also handles activity changes
	 * like rotation and destructions of activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// Initialize the notes Vector
		notes = new Vector<Object>() ;

		// Restore the notes from the internal application storage file if available.
		restoreNotesFromInternalStorage();
		
        // Construct the action to be done when there is no save instance state available
        if ( savedInstanceState == null ) 
        {
        	// Create the new notes list fragment 
    		myNotesList = new NotesList();
    		
    		// Start the fragment transaction to place the fragment into the activity
    		FragmentTransaction transaction = getFragmentManager().beginTransaction();
    		// add the notes list fragment to the activity by replacing the FramLayout
    		transaction.add(R.id.notes_list, myNotesList, "NotesList");
    		transaction.commit(); // Commit the changes
    		setContentView(R.layout.activity_notes_start); // Set the view for the activity
        }
        else {
        	
        	// Restore the action and listPosition when onCreate function is called with a valid saveInstanceState bundle
        	action = savedInstanceState.getString( actionTag ) ;
        	listPosition = savedInstanceState.getInt( listPositionTag ) ;
        	
        	// Start the fragment transaction to place the fragment into the activity
        	FragmentTransaction transaction = getFragmentManager().beginTransaction();
        	
        	// Check which fragment was placed into the save instance state bundle so that the correct one can be restored.
        	// Restore the notes list fragment 
        	if ( (savedInstanceState.getSerializable("NotesList")) != null ) 
        	{
        		// Get the serialized data of the fragment
        		myNotesList = (NotesList) savedInstanceState.getSerializable("NotesList");
        		transaction.replace(R.id.notes_list, myNotesList, "NotesList");
        	}
        	// Restore the description fragment
        	else {
        		// Create the description fragment
    			myNotesDescription = new NotesDescription();
    			// Replace the notes list in the activity
    			transaction.replace(R.id.notes_list, myNotesDescription, "Description");
        	}
    		transaction.commit(); // Commit the changes
    		setContentView(R.layout.activity_notes_start); 
        }
	}

	/**
	 * Function is used to create the options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes_start, menu);
		return true;
	}

	/**
	 * This function is used to handle when buttons in the action bar are selected.
	 * Supports Add note.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{

		/**
		 * Switch through the possible options on the action bar and perform the actions accordingly. 
		 */
		switch (item.getItemId()) 
		{
			// Handle when the add is clicked
			case R.id.add:
				
				// Set the action and listposition
				action = "new" ;
				listPosition = -1;
				
				// Create the new notes description with empty title and content
				myNotesDescription = new NotesDescription();
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.notes_list, myNotesDescription, "Description");
				transaction.addToBackStack("Description");
				transaction.commit();

				return true;
			default:
				return super.onOptionsItemSelected(item) ;
		}
	}
	
	/**
	 * This function is used to save the instance of the activity.
	 * @param outState
	 */
    @Override
    protected void onSaveInstanceState( Bundle outState ) 
    {	
    	// Check which fragment is currently visible in the activity and store that one to the bundle
    	if ( myNotesList.isVisible() )
    	{
    		outState.putSerializable("NotesList", myNotesList);
    	}
    	else if ( myNotesDescription.isVisible() ){
    		outState.putSerializable("NotesDescription", myNotesDescription);
    	}
    	
    	// Samve the global variable used to construct description fragments
    	outState.putString( actionTag,  action ) ;
    	outState.putFloat( listPositionTag, listPosition );
    	
    	super.onSaveInstanceState( outState ) ;
    }
    
	/**
	 * This function is used to handle the action to be performed when item in the list view is clicked.
	 * @param position - position value from the list view
	 */
	public void onNoteClicked(int position) 
	{
		// Set the action and listposition
		action = "old" ;
		listPosition = position;
		
		// Create the new notes description with empty title and content
		myNotesDescription = new NotesDescription();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.notes_list, myNotesDescription, "Description");
		transaction.addToBackStack("Description");
		transaction.commit();
	}

	/**
	 * This 
	 * @param newNote - The new note that needs to be saved
	 * @param action - the action that was used to construct the fragment for description 
	 */
	public void onSave ( Notes newNote, String action, int position ) 
	{
		// When the fragment was a new note make sure that the new note is added to the Vector
		if ( action == "new" )
		{
		   notes.addElement(newNote);
		}
		// When the fragment is edit then remove the old note and add the new one to the Vector
		else {
		   notes.remove(position);
		   notes.addElement(newNote);
		}
		
		// Create the new notes list with
		myNotesList = new NotesList();
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.notes_list, myNotesList, "NotesList");
		transaction.commit();
		
		// Save the notes vector to the intern storage.
		saveNotesInInternalStorage() ;
	}

	/**
	 * This function is used to cancel the operation that the user has performed on the, 
	 * editable note or discard the note if it is new.
	 */
	public void onCancel() 
	{
		// Create the new notes list with
		myNotesList = new NotesList();
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.notes_list, myNotesList, "NotesList");
		transaction.commit();
	}

	/**
	 * This function is used to delete the note that is selected by the user. 
	 * @param position
	 */
	public void onDelete(int position) 
	{
		// Only remote elements if a position is positive,
		// to avoid null expections
		if ( position >= 0 ) 
		{
			notes.remove(position);
		}
		
		// Create the new notes list with
		myNotesList = new NotesList();
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.notes_list, myNotesList, "NotesList");
		transaction.commit();	
		
		// Save the notes vector to the intern storage.
		saveNotesInInternalStorage() ;
	}
	
	/**
	 * Used to store the Vector of notes to a file located on the internal storage 
	 * of the device. Saves using serialization to make the data secure. 
	 */
	public void saveNotesInInternalStorage() 
	{
        // variable deceleration 
        ObjectOutputStream notesRawDataOut ;
        
        try {
            // Construct the stream to write the vector of notes saved already.
			notesRawDataOut = new ObjectOutputStream(new FileOutputStream(
					notesRawDataFile.getAbsoluteFile()));
			notesRawDataOut.writeObject ( notes ) ; // Write the object
	        notesRawDataOut.flush() ; // flush the stream to make sure everything is written.
	        notesRawDataOut.close() ; // Close the stream
		} catch ( IOException e ) { e.printStackTrace() ; }
	}
	
	/**
	 * This function is used to restore the notes saved by the user from the internal
	 * application file.
	 */
	@SuppressWarnings("unchecked")
	public void restoreNotesFromInternalStorage() 
	{
		// Create the File handle for the file that is checked if an restore is needed
        notesRawDataFile = new File ( getFilesDir(), "NotesRawData.ser" ) ;

        // Perform the restore only if the file exists.
        if ( notesRawDataFile.exists() ) 
        { 
	        // Variable deceleration 
	        ObjectInputStream notesRawDataRestore;
	        
	        try
	        {
	            // Open the stream to retrieve the saved notes from the file.
				notesRawDataRestore = new ObjectInputStream(
						new FileInputStream(notesRawDataFile.getAbsoluteFile()));
	            
	            // Read the data in the file and store it in the notes vector.
	            notes = ( Vector<Object> ) notesRawDataRestore.readObject() ;
	
	            notesRawDataRestore.close() ; // Close the stream.
	        }
	        catch ( IOException e ) { e.printStackTrace() ; }
	        catch ( ClassNotFoundException e ) { e.printStackTrace() ; }
        }
	}
}