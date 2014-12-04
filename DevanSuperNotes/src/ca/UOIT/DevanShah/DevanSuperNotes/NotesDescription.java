package ca.UOIT.DevanShah.DevanSuperNotes;

import java.io.Serializable;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * This class is used to perform the actions for the fragment note description
 * @author Devan Shah 100428864
 *
 */
@SuppressLint("NewApi")
public class NotesDescription extends Fragment implements Serializable {

    /**
     * Default serialization constant for this object.
     */
    private static final long serialVersionUID = 1L ;
    
    // Variable Deceleration 
	String action = NotesStart.action;
	int position = NotesStart.listPosition;
	
	// Date structure Deceleration
	Vector<Object> notes;
	
	// Declare the main activity
	Activity myNotesStartActivity;
	
	// Declare the edit text objects
	EditText noteTitle;
	EditText noteDescription;
	
	/**
	 * Default constructor
	 */
	public NotesDescription() {

	}
	
	/**
	 * This function is used to create the fragment view with some important data for the 
	 * fragment.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// Grab the inflate the view and store it 
		View view = inflater.inflate(R.layout.notes_description, container, false);
		
		// Get the main activity
		myNotesStartActivity = getActivity();
		
		// get the note vector from the main activity
		notes = ((NotesStart) myNotesStartActivity).notes;
		
		// Get the myNotesDescription object from the main activity
		((NotesStart) myNotesStartActivity).myNotesDescription = this;
		
		// Setup the edit text boxes for note title and note description
		noteTitle = (EditText) view.findViewById(R.id.editText1);
		noteDescription = (EditText) view.findViewById(R.id.editText2);
		
		// Setup the button and listener for the cancel button. 
		Button cancel = (Button) view.findViewById(R.id.button1);
		cancel.setOnClickListener(myCancelListener);
		
		// Setup the button and listener for the save button. 
		Button save = (Button) view.findViewById(R.id.button2);
		save.setOnClickListener(mySaveListener);
		
		// Setup the button and listener for the delete button. 
		Button delete = (Button) view.findViewById(R.id.button3);
		delete.setOnClickListener(myDeleteListener);
		
		// When the description fragment creation is requested as an old note 
		// display the title and content of the note.
		if ( action == "old") 
		{
		   displayDetails(position);
		}
		
        // Construct the action to be done when there is a saved instance.
        if ( savedInstanceState != null ) 
        {
        	// Cancel the action when instance is saved.
        	((NotesStart) myNotesStartActivity).onCancel();
        }
		
		return view;
	}
	
	/**
	 * Display the information of the row in the list view that was selected.
	 * @param position
	 */
	public void displayDetails ( int position ) 
	{
		// get the note in the position that the info needs to be displayed.
		Notes note = (Notes) notes.get(position);
		
		// Set the information for the edit test ( title and description)
		noteTitle.setText(note.getNoteTitle());
		noteDescription.setText(note.getNoteDescription()) ;
	}
	
	/**
	 * This is the creation of an action to be performed on the click of the save button.
	 */
	private OnClickListener mySaveListener = new OnClickListener() 
	{
		// Action when the lick is performed
		public void onClick ( View view ) 
		{
			// Construct the note object that will be saved.
			Notes newNote = new Notes(noteTitle.getText().toString(), noteDescription.getText().toString());
			
			// If the note object title is empty then pop up a dialog notifying the user of this
			if ( newNote.getNoteTitle().isEmpty() )  
			{
				// Create the dialog to notify the user
				new AlertDialog.Builder(myNotesStartActivity)
			    			   .setTitle("Empty Note Title") // Set the title
			    			   .setMessage("Please enter a Note Title before Saving. Thanks") // Set the message
			    			   .setPositiveButton ( android.R.string.ok, // Set the type of button
			    					   			    new DialogInterface.OnClickListener() // Create the new on click listener for click of ok
			    			   						{
			    				   						// handle the on click of ok
			    				   						public void onClick(DialogInterface dialog, int which) 
			    				   						{ 
			    				   							// Close the dialog button and let the user go back to adding title
			    				   							dialog.cancel();
			    				   						}
			    			   						}
			    					   			   )
			    					   			   .setIcon(android.R.drawable.ic_dialog_alert) // Set the layout of the box, using default.
			    					   			   .show(); // Show the dialog box
			}
			else 
			{
				// When the note title is good, call the same function, to same the note.
				((NotesStart) myNotesStartActivity).onSave(newNote,action,position);	
			}
		}
	};
	
	/**
	 * This is the creation of an action to be performed on the click of the cancel button.
	 */
	private OnClickListener myCancelListener = new OnClickListener() 
	{
		// Action when the click is performed
		public void onClick(View view) 
		{
			// Call the cancel function from the main activity.
			((NotesStart) myNotesStartActivity).onCancel();
		}
	};
	
	/**
	 * This is the creation of an action to be performed on the click of the delete button.
	 */
	private OnClickListener myDeleteListener = new OnClickListener() 
	{
		// Action when the click is performed
		public void onClick(View view) 
		{
			// Call the delete function from the main activity. To remove the note that was selected.
			((NotesStart) myNotesStartActivity).onDelete(position);
		}
	};

}
