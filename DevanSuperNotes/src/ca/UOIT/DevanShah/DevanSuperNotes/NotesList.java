package ca.UOIT.DevanShah.DevanSuperNotes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * This class is used to perform the actions for the fragment notes list.
 * @author Devan Shah 100428864
 *
 */
@SuppressLint("NewApi")
public class NotesList extends Fragment implements Serializable {

    /**
     * Default serialization constant for this object.
     */
    private static final long serialVersionUID = 1L ;
	
    // Date Structure Deceleration
	ArrayList<Map<String, String>> notesExtracted;
	Vector<Object> notes;
	
	// Stores the main activity
	Activity myNotesStartActivity;

	/**
	 * Default constructor
	 */
	public NotesList() {

	}

	/**
	 * This function is used to create the fragment view with some important data for the 
	 * fragment.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// Grab the inflate the view and store it 
		View view = inflater.inflate(R.layout.notes_list, container, false);
		
		// Get the main activity
		myNotesStartActivity = getActivity();
		
		// get the note vector from the main activity
		notes = ((NotesStart) myNotesStartActivity).notes;
		
		// Initialize the ArrayList<Map<String, String>>()
		notesExtracted = new ArrayList<Map<String, String>>();
		
		// Build the ArrayList<Map<String, String>>() using the notes object
		buildNotesArray();
		
		// Create the SimpleAdapter to construct the list view with the notes information
		SimpleAdapter notesAdapter = new SimpleAdapter ( myNotesStartActivity,                         // The main activity
				                                         notesExtracted,                               // The ArrayList<Map<String, String>>() of the notes
				                                         R.layout.listviewrow,                         // The layout of the list view of rows
				                                         new String[] { "Title", "Date" },             // The header that is extracted from the HaspMap
				                                         new int[] { R.id.TITLE_CELL, R.id.DATE_CELL } // The textview id where the data needs to be placed
		                                               );
		
		// Grab the list view so that it can be placed in
		ListView listView = (ListView) view.findViewById(R.id.listView1);
		listView.setAdapter(notesAdapter); // Set the adapter in the list view
		listView.setOnItemClickListener(noteSelected); // Enable the on click listerner when emtries in the list view are clicked
		return view;
	}

	/**
	 * This function is used to create the ArrayList<Map<String, String>>() using Vector<Object> of notes
	 */
	private void buildNotesArray() {
        
		// Declare the Map to be user for the notes.
		Map<String, String> noteMap;
		
		// Sort the entries that are in the notes vector, by date. This is to display the latest note first.
        Collections.sort ( notes, new Comparator<Object>() 
        {
        	  // Define a compare function that calls the compareTo available in the Notes class. (for each note)
        	  public int compare ( Object a, Object b ) 
        	  {
        		// Return the values of compare conditions, 1, -1, 0  
        	    return ( ((Notes) a).compareTo( (Notes) b));
        	  }
        });
        
		// Loop through the note vector and build a HashMap and add it to the ArrayList
        for ( int i = 0; i < notes.size(); i++ )
        {
        	// Create a new HashMap
        	noteMap = new HashMap<String, String>();
        	
        	// Grab the notes entry
            Notes noteEntry = ( ( Notes ) notes.elementAt (i) ) ;
            
            // Put the Title and Date from the notes object into the HashMap
            noteMap.put ( "Title", noteEntry.getNoteTitle() );
            noteMap.put ( "Date", noteEntry.getNoteCreationDate() );
            
            // Put the HashMap into the ArrayList
            notesExtracted.add(noteMap);
        }
	}

	/**
	 * This function is used to perform the action when a row is clicked in the list view.
	 */
	private OnItemClickListener noteSelected = new OnItemClickListener() 
	{
		// On item click for the list view
		public void onItemClick ( AdapterView<?> parent, View v, int position, long id ) 
		{	
			// Perform the action for on note clicked, call the function from the main activity.
			((NotesStart) myNotesStartActivity).onNoteClicked(position);
		}
	};
}
