package ca.UOIT.DevanShah.DevanSuperNotes;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * This class is used to construct the notes object with the information about the note.
 * The note object contains, Title, content of note and date created.
 * @author Devan Shah 100428864
 *
 */
public class Notes implements Serializable, Comparable<Notes>
{

    /**
     * Default serialization constant for this object.
     */
    private static final long serialVersionUID = 1L ;
    
    // Variable Deceleration 
	public String noteTitle;
	public String noteDescription;
	public Date noteCreationDate;
	
	// Date formatter 
	static DateFormat TimeFormatter = DateFormat.getTimeInstance ();
	static DateFormat DateFormatter = DateFormat.getDateInstance();
	
	/**
	 * This constructor defines the note title and note description for a note that is created.
	 * @param noteTitle
	 * @param noteDescription
	 */
	public Notes ( String noteTitle, String noteDescription) 
	{
		this.noteTitle = noteTitle;
		this.noteDescription = noteDescription;
		this.noteCreationDate = new Date() ;
	}
	
	/**
	 * Comvert the note into a string if needed. (not used)
	 */
	@Override
	public String toString() {
		return noteTitle + " " + getNoteCreationDate() + ">";
	}
	
	/**
	 * Return the note title of the note.
	 * @return noteTitle - Title of the note
	 */
	public String getNoteTitle() 
	{
		return noteTitle;
	}
	
	/**
	 * Return the note description of the note.
	 * @return noteDescription - Content of the note
	 */
	public String getNoteDescription() 
	{
		return noteDescription;
	}
	
	/**
	 * This function is used to get the creation date of the note. 
	 * This function provides the date based on the date and formats it accordingly. 
	 * Then the day is the same as current the time is returned only, otherwise 
	 * only the data is returned. 
	 * @return
	 */
	public String getNoteCreationDate() {
		
		// Get the current date
		Date currentDate = new Date();
		
		// If the date is still current only return the time
		if ( DateFormatter.format(currentDate).equals(DateFormatter.format(noteCreationDate)))
		{
			// Format the time and return it 
			return TimeFormatter.format(noteCreationDate) + " >";
		}
		// When the date is not current return only the date
		else {
			// format the date and return it
			return DateFormatter.format(noteCreationDate) + " >";
		}
	}

	/**
	 * This function is used to compare 2 note objects on the data of each of the objects.
	 */
	@Override
	public int compareTo(Notes another) {
		
		// If the date of this note is after the provided note then return -1
		if ( this.noteCreationDate.after(another.noteCreationDate))
		{
			return -1;
		}
		// When the date of this note is before the provided note then return 1
		else if ( this.noteCreationDate.before(another.noteCreationDate))
		{
			return 1;	
		}
		// Otherwise return 0 (note is the same)
		else {
			return 0;	
		}
	}
}
