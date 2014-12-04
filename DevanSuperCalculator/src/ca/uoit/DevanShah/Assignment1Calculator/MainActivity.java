package ca.uoit.DevanShah.Assignment1Calculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("NewApi") public class MainActivity extends ActionBarActivity implements View.OnClickListener {

	// Define buttons that are used in the calculator
	Button backSpace, clear_entry, clear, plus_minus, squareRoot, percentage, oneOver, squared, log;
	Button decimal, add, minus, multiply, divide, equal;
	Button zero, one, two, three, four, five, six, seven, eight, nine;

	// Define the result display
	EditText resultDisplay;

	// Define the label
	TextView progressLabel;

	// Declare numbers
	float number1;
	float number2;
	String progress = "" ;
	String progressRestore = "" ;
    String operation = "" ;
    String number1Holder = "" ;
    String number2Holder = "" ;
    private final String number1Tag = "number1Tag";
    private final String number2Tag = "number2Tag" ;
    private final String progressTag = "progressTag" ;
    private final String operationTag = "operationTag" ;
    private final String number1HolderTag = "number1HolderTag" ;
    private final String number2HolderTag = "number2HolderTag" ;
    
	@Override
	protected void onCreate ( Bundle savedInstanceState ) 
	{
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.activity_main );

		// Get the number buttons
		one   = ( Button ) findViewById ( R.id.one );
		two   = ( Button ) findViewById ( R.id.two );
		three = ( Button ) findViewById ( R.id.three );
		four  = ( Button ) findViewById ( R.id.four );
		five  = ( Button ) findViewById ( R.id.five );
		six   = ( Button ) findViewById ( R.id.six );
		seven = ( Button ) findViewById ( R.id.seven );
		eight = ( Button ) findViewById ( R.id.eight );
		nine  = ( Button ) findViewById ( R.id.nine );
		
		// Get the simple operation buttons
		decimal  = ( Button ) findViewById ( R.id.decimal );
		add      = ( Button ) findViewById ( R.id.add );
		minus    = ( Button ) findViewById ( R.id.minus );
		multiply = ( Button ) findViewById ( R.id.multiply );
		divide   = ( Button ) findViewById ( R.id.divide );
		equal    = ( Button ) findViewById ( R.id.equal );
		
		// Get the special operation buttons
		backSpace   = ( Button ) findViewById ( R.id.backspace );
		clear_entry = ( Button ) findViewById ( R.id.clear_entry );
		clear       = ( Button ) findViewById ( R.id.clear );
		plus_minus  = ( Button ) findViewById ( R.id.plus_minus );
		squareRoot  = ( Button ) findViewById ( R.id.squareRoot );
		percentage  = ( Button ) findViewById ( R.id.perctange );
		oneOver     = ( Button ) findViewById ( R.id.one_over );
		squared     = ( Button ) findViewById ( R.id.squared );
		log         = ( Button ) findViewById ( R.id.log );
		
		//Get the label and the edit box
		resultDisplay = ( EditText ) findViewById ( R.id.resultDisplay );
		progressLabel = ( TextView ) findViewById ( R.id.progressLabel );

		try {
			
			// Define the click event for number buttons
			one.setOnClickListener(this) ;
			two.setOnClickListener(this) ;
			three.setOnClickListener(this) ;
			four.setOnClickListener(this) ;
			five.setOnClickListener(this) ;
			six.setOnClickListener(this) ;
			seven.setOnClickListener(this) ;
			eight.setOnClickListener(this) ;
			nine.setOnClickListener(this) ;
			
			// Define the click event for operation buttons
			decimal.setOnClickListener(this) ;
			add.setOnClickListener(this) ;
			minus.setOnClickListener(this) ;
			multiply.setOnClickListener(this) ;
			divide.setOnClickListener(this) ;
			equal.setOnClickListener(this) ;
			
			// Define the click event for special operation buttons
			backSpace.setOnClickListener(this) ;
			clear_entry.setOnClickListener(this) ;
			clear.setOnClickListener(this) ;
			plus_minus.setOnClickListener(this) ;
			squareRoot.setOnClickListener(this) ;
			percentage.setOnClickListener(this) ;
			oneOver.setOnClickListener(this) ;
			squared.setOnClickListener(this) ;
			log.setOnClickListener(this) ;
		}
		catch ( Exception e ) {} 
		
        // Construct the action to be done when there is a saved instance.
        if ( savedInstanceState != null ) 
        {
        
        	progressRestore = savedInstanceState.getString( progressTag ) ;
        	number1 = savedInstanceState.getFloat( number1Tag ) ;
        	number2 = savedInstanceState.getFloat( number2Tag ) ;
        	operation = savedInstanceState.getString( operationTag ) ;
        	number1Holder = savedInstanceState.getString( number1HolderTag ) ;
        	number2Holder = savedInstanceState.getString( number2HolderTag ) ;

        	// Make sure that when name is null or empty that the text view is reset to default.
        	if ( !progressRestore.isEmpty() )
        	{
        		progressLabel.setText( progressRestore ) ;
        	}
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    @Override
    protected void onSaveInstanceState( Bundle outState ) 
    {
    	// Grab the text in the Edit text box before instance is destroyed
    	TextView nameText = ( TextView ) findViewById( R.id.progressLabel ) ;
    	progressRestore = nameText.getText().toString() ;
    	
    	// Store this in the global variable nameTag to pull down again
    	// for onCreate.
    	outState.putString( progressTag,  progressRestore ) ;
    	outState.putFloat(number1Tag, number1);
    	outState.putFloat(number2Tag, number2);
    	outState.putString( operationTag,  operation ) ;
    	outState.putString( number1HolderTag,  number1Holder ) ;
    	outState.putString( number2HolderTag,  number2Holder ) ;

    	super.onSaveInstanceState( outState ) ;
    }
    
	@SuppressLint("NewApi") @Override
	public void onClick ( View arg0 ) 
	{
		Editable currentString = resultDisplay.getText() ;
		String currentProgressString ;
		
		switch ( arg0.getId() ) 
		{
			case R.id.one:
				
				if ( number1Holder == null || number1Holder.isEmpty() || operation.isEmpty() ) { number1Holder = number1Holder + one.getText().toString() ; }
				else { number2Holder = number2Holder + one.getText().toString() ; }
				
				if ( number1 != 0 && 
						  ( resultDisplay.getText().length() > 0 ) && 
						  ( number1 == Float.parseFloat ( resultDisplay.getText().toString() ) ) 
						) 
				{ 
					resultDisplay.setText ( "" ) ; 
					currentString.replace ( 0, currentString.length(), "" ) ;
				}
				
				// Append one string to the label
				currentProgressString = progressLabel.getText().toString() ;
				progressLabel.setText ( currentProgressString + one.getText().toString() ) ;
				
				currentString = currentString.append ( one.getText() ) ;
				resultDisplay.setText ( currentString ) ;
				
			break;
			case R.id.two:
				
				if ( number1Holder == null || number1Holder.isEmpty() || operation.isEmpty() ) { number1Holder = number1Holder + two.getText().toString() ; }
				else { number2Holder = number2Holder + two.getText().toString() ; }
				
				if ( number1 != 0 && 
						  ( resultDisplay.getText().length() > 0 ) && 
						  ( number1 == Float.parseFloat ( resultDisplay.getText().toString() ) ) 
						) 
				{ 
					resultDisplay.setText ( "" ) ; 
					currentString.replace ( 0, currentString.length(), "" ) ;
				}
				
				// Append one string to the label
				currentProgressString = progressLabel.getText().toString() ;
				progressLabel.setText ( currentProgressString + two.getText().toString() ) ;
				
				currentString = currentString.append ( two.getText() ) ;
				resultDisplay.setText ( currentString ) ;
				
			break;
			case R.id.three:
				
				if ( number1Holder == null || number1Holder.isEmpty() || operation.isEmpty() ) { number1Holder = number1Holder + three.getText().toString() ; }
				else { number2Holder = number2Holder + three.getText().toString() ; }
				
				if ( number1 != 0 && 
						  ( resultDisplay.getText().length() > 0 ) && 
						  ( number1 == Float.parseFloat ( resultDisplay.getText().toString() ) ) 
						) 
				{ 
					resultDisplay.setText ( "" ) ; 
					currentString.replace ( 0, currentString.length(), "" ) ;
				}
				
				// Append one string to the label
				currentProgressString = progressLabel.getText().toString() ;
				progressLabel.setText ( currentProgressString + three.getText().toString() ) ;
				
				currentString = currentString.append ( three.getText() ) ;
				resultDisplay.setText ( currentString ) ;
				
			break;
			case R.id.four:
				
				if ( number1Holder == null || number1Holder.isEmpty() || operation.isEmpty() ) { number1Holder = number1Holder + four.getText().toString() ; }
				else { number2Holder = number2Holder + four.getText().toString() ; }
				
				if ( number1 != 0 && 
						  ( resultDisplay.getText().length() > 0 ) && 
						  ( number1 == Float.parseFloat ( resultDisplay.getText().toString() ) ) 
						) 
				{ 
					resultDisplay.setText ( "" ) ; 
					currentString.replace ( 0, currentString.length(), "" ) ;
				}
				
				// Append one string to the label
				currentProgressString = progressLabel.getText().toString() ;
				progressLabel.setText ( currentProgressString + four.getText().toString() ) ;
				
				currentString = currentString.append ( four.getText() ) ;
				resultDisplay.setText ( currentString ) ;
				
			break;
			case R.id.five:
				
				if ( number1Holder == null || number1Holder.isEmpty() || operation.isEmpty() ) { number1Holder = number1Holder + five.getText().toString() ; }
				else { number2Holder = number2Holder + five.getText().toString() ; }
				
				if ( number1 != 0 && 
						  ( resultDisplay.getText().length() > 0 ) && 
						  ( number1 == Float.parseFloat ( resultDisplay.getText().toString() ) ) 
						) 
				{ 
					resultDisplay.setText ( "" ) ; 
					currentString.replace ( 0, currentString.length(), "" ) ;
				}
				
				// Append one string to the label
				currentProgressString = progressLabel.getText().toString() ;
				progressLabel.setText ( currentProgressString + five.getText().toString() ) ;
				
				currentString = currentString.append ( five.getText() ) ;
				resultDisplay.setText ( currentString ) ;
				
			break;
			case R.id.six:
				
				if ( number1Holder == null || number1Holder.isEmpty() || operation.isEmpty() ) { number1Holder = number1Holder + six.getText().toString() ; }
				else { number2Holder = number2Holder + six.getText().toString() ; }
				
				if ( number1 != 0 && 
						  ( resultDisplay.getText().length() > 0 ) && 
						  ( number1 == Float.parseFloat ( resultDisplay.getText().toString() ) ) 
						) 
				{ 
					resultDisplay.setText ( "" ) ; 
					currentString.replace ( 0, currentString.length(), "" ) ;
				}
				
				// Append one string to the label
				currentProgressString = progressLabel.getText().toString() ;
				progressLabel.setText ( currentProgressString + six.getText().toString() ) ;
				
				currentString = currentString.append ( six.getText() ) ;
				resultDisplay.setText ( currentString ) ;
				
			break;
			case R.id.seven:
				
				if ( number1Holder == null || number1Holder.isEmpty() || operation.isEmpty() ) { number1Holder = number1Holder + seven.getText().toString() ; }
				else { number2Holder = number2Holder + seven.getText().toString() ; }
				
				if ( number1 != 0 && 
						  ( resultDisplay.getText().length() > 0 ) && 
						  ( number1 == Float.parseFloat ( resultDisplay.getText().toString() ) ) 
						) 
				{ 
					resultDisplay.setText ( "" ) ; 
					currentString.replace ( 0, currentString.length(), "" ) ;
				}
				
				// Append one string to the label
				currentProgressString = progressLabel.getText().toString() ;
				progressLabel.setText ( currentProgressString + seven.getText().toString() ) ;
				
				currentString = currentString.append ( seven.getText() ) ;
				resultDisplay.setText ( currentString ) ;
				
			break;
			case R.id.eight:
				
				if ( number1Holder == null || number1Holder.isEmpty() || operation.isEmpty() ) { number1Holder = number1Holder + eight.getText().toString() ; }
				else { number2Holder = number2Holder + eight.getText().toString() ; }
				
				if ( number1 != 0 && 
						  ( resultDisplay.getText().length() > 0 ) && 
						  ( number1 == Float.parseFloat ( resultDisplay.getText().toString() ) ) 
						) 
				{ 
					resultDisplay.setText ( "" ) ; 
					currentString.replace ( 0, currentString.length(), "" ) ;
				}
				
				// Append one string to the label
				currentProgressString = progressLabel.getText().toString() ;
				progressLabel.setText ( currentProgressString + eight.getText().toString() ) ;
				
				currentString = currentString.append ( eight.getText() ) ;
				resultDisplay.setText ( currentString ) ;
				
			break;
			case R.id.nine:
				
				if ( number1Holder == null || number1Holder.isEmpty() || operation.isEmpty() ) { number1Holder = number1Holder + nine.getText().toString() ; }
				else { number2Holder = number2Holder + nine.getText().toString() ; }
				
				if ( number1 != 0 && 
						  ( resultDisplay.getText().length() > 0 ) && 
						  ( number1 == Float.parseFloat ( resultDisplay.getText().toString() ) ) 
						) 
				{ 
					resultDisplay.setText ( "" ) ; 
					currentString.replace ( 0, currentString.length(), "" ) ;
				}
				
				// Append one string to the label
				currentProgressString = progressLabel.getText().toString() ;
				progressLabel.setText ( currentProgressString + nine.getText().toString() ) ;
				
				currentString = currentString.append ( nine.getText() ) ;
				resultDisplay.setText ( currentString ) ;
				
			break;
			case R.id.clear:
				number1 = 0 ;
				number2 = 0 ;
				number1Holder = "";
				number2Holder = "";
				resultDisplay.setText ( "" ) ;
				progressLabel.setText ( "" ) ;
			break;
			case R.id.clear_entry:
				number2 = 0 ;
				number2Holder = "";
				resultDisplay.setText ( "" ) ;
				if ( !progressLabel.getText().toString().isEmpty() ){
					progressLabel.setText( progressLabel.getText().toString().substring ( 0, progressLabel.getText().toString().length()-1 ) ) ;
				}
				
			break;
			case R.id.add:
				
				resultDisplay.setText ( "" ) ;
				
				if ( ( ( number1Holder != null && !number1Holder.isEmpty() ) && 
				       ( number2Holder != null && !number2Holder.isEmpty() ) )
				   ) 
				{
				   number1 = Float.parseFloat(number1Holder) + Float.parseFloat(number2Holder) ;
				   number2 = 0 ;
				   number2Holder = "" ;
				   number1Holder = "" ;
				   resultDisplay.setText( Float.toString ( number1 ) ) ;
				}
				else if ( ( number1Holder != null && !number1Holder.isEmpty() ) && number1 != 0  ) 
				{
					   number1 = number1 + ( Float.parseFloat(number1Holder) );
					   number2 = 0 ;
					   number2Holder = "" ;
					   number1Holder = "" ;
					   resultDisplay.setText( Float.toString ( number1 ) ) ;
				}
				
				operation = "+" ;
				
				currentProgressString = progressLabel.getText().toString() ;
				progressLabel.setText ( currentProgressString  + " + " ) ; 
			break;
			case R.id.minus:
				
				resultDisplay.setText ( "" ) ;
				
				if ( ( ( number1Holder != null && !number1Holder.isEmpty() ) && 
				       ( number2Holder != null && !number2Holder.isEmpty() ) )
				   ) 
				{
				   number1 = Float.parseFloat(number1Holder) - Float.parseFloat(number2Holder) ;
				   number2 = 0 ;
				   number2Holder = "" ;
				   number1Holder = "" ;
				   resultDisplay.setText( Float.toString ( number1 ) ) ;
				}
				else if ( ( number1Holder != null && !number1Holder.isEmpty() ) && number1 != 0 ) 
				{
					   number1 = number1 - ( Float.parseFloat(number1Holder) );
					   number2 = 0 ;
					   number2Holder = "" ;
					   number1Holder = "" ;
					   resultDisplay.setText( Float.toString ( number1 ) ) ;
				}
				
				currentProgressString = progressLabel.getText().toString() ;
				progressLabel.setText ( currentProgressString  + " - " ) ; 
			break;
			case R.id.divide:
				
				resultDisplay.setText ( "" ) ;
				
				operation = "/" ;
				
				if ( ( ( number1Holder != null && !number1Holder.isEmpty() ) && 
				       ( number2Holder != null && !number2Holder.isEmpty() ) )
				   ) 
				{
				   number1 = Float.parseFloat(number1Holder) / Float.parseFloat(number2Holder) ;
				   number2 = 0 ;
				   number2Holder = "" ;
				   number1Holder = "" ;
				   resultDisplay.setText( Float.toString ( number1 ) ) ;
				}
				else if ( ( number1Holder != null && !number1Holder.isEmpty() ) && number1 != 0 ) 
				{
					   number1 = number1 / ( Float.parseFloat(number1Holder) );
					   number2 = 0 ;
					   number2Holder = "" ;
					   number1Holder = "" ;
					   resultDisplay.setText( Float.toString ( number1 ) ) ;
				}
				
				currentProgressString = progressLabel.getText().toString() ;
				progressLabel.setText ( currentProgressString  + " / " ) ; 
			break;
			case R.id.multiply:
				
				resultDisplay.setText ( "" ) ;
				
				operation = "-" ;
				
				if ( ( ( number1Holder != null && !number1Holder.isEmpty() ) && 
				       ( number2Holder != null && !number2Holder.isEmpty() ) )
				   ) 
				{
				   number1 = Float.parseFloat(number1Holder) * Float.parseFloat(number2Holder) ;
				   number2 = 0 ;
				   number2Holder = "" ;
				   number1Holder = "" ;
				   resultDisplay.setText( Float.toString ( number1 ) ) ;
				}
				else if ( ( number1Holder != null && !number1Holder.isEmpty() ) && number1 != 0 ) 
				{
					   number1 = number1 * ( Float.parseFloat(number1Holder) );
					   number2 = 0 ;
					   number2Holder = "" ;
					   number1Holder = "" ;
					   resultDisplay.setText( Float.toString ( number1 ) ) ;
				}
				
				currentProgressString = progressLabel.getText().toString() ;
				progressLabel.setText ( currentProgressString  + " * " ) ; 
			break;
			case R.id.equal:
				
				if ( ( ( number1Holder != null && !number1Holder.isEmpty() ) && 
					       ( number2Holder != null && !number2Holder.isEmpty() ) )
					   ) 
					{
					   operation(Float.parseFloat(number1Holder),Float.parseFloat(number2Holder));
					}
					else if ( ( number1Holder != null && !number1Holder.isEmpty() ) && number1 != 0 ) 
					{
					   operation(number1,Float.parseFloat(number1Holder));
					}
				
			break;
			case R.id.plus_minus:
			   number1 = number1 * -1 ;
			   number2 = 0 ;
			   number2Holder = "" ;
			   number1Holder = "" ;
			   resultDisplay.setText( Float.toString ( number1 ) ) ;
			break;
			case R.id.squareRoot:
			   number1 = (float) ( Math.sqrt(number1) ) ;
			   number2 = 0 ;
			   number2Holder = "" ;
			   number1Holder = "" ;
			   resultDisplay.setText( Float.toString ( number1 ) ) ;
			break;
			case R.id.perctange:
				number1 = number1/100 ;
				number2 = 0 ;
			    number2Holder = "" ;
			    number1Holder = "" ;
			    resultDisplay.setText( Float.toString ( number1 ) ) ;
			break;
			case R.id.one_over:
				number1 = 1/number1 ;
				number2 = 0 ;
			    number2Holder = "" ;
			    number1Holder = "" ;
			    resultDisplay.setText( Float.toString ( number1 ) ) ;
			break;
			case R.id.squared:
				number1 = number1*number1 ;
				number2 = 0 ;
			    number2Holder = "" ;
			    number1Holder = "" ;
			    resultDisplay.setText( Float.toString ( number1 ) ) ;
			break;
			case R.id.log:
				number1 = (float) Math.log10(number1) ;
				number2 = 0 ;
			    number2Holder = "" ;
			    number1Holder = "" ;
			    resultDisplay.setText( Float.toString ( number1 ) ) ;
			break;
			case R.id.backspace:
				if ( !resultDisplay.getText().toString().isEmpty() ) {
					resultDisplay.setText( resultDisplay.getText().toString().substring ( 0, resultDisplay.getText().toString().length()-1 ) ) ;
				}
			
				if ( !progressLabel.getText().toString().isEmpty() ) {
					progressLabel.setText( progressLabel.getText().toString().substring ( 0, progressLabel.getText().toString().length()-1 ) ) ;	
				}
				number2Holder = "";
		}
	}
	
	public void operation ( float tempNumber1, float tempNumber2 ) 
	{
		if ( operation.equals("+") ) 
		{
			number1 = tempNumber1 + tempNumber2;
			resultDisplay.setText( Float.toString ( number1 ) ) ;
			number2 = 0 ;
			operation = "" ;
			progressLabel.setText ( "" ) ;
		}
		else if ( operation.equals("-") )
		{
			number1 = tempNumber1 - tempNumber2;
			resultDisplay.setText( Float.toString ( number1 ) ) ;
			number2 = 0 ;
			operation = "" ;
			progressLabel.setText ( "" ) ;
		}
		else if ( operation.equals("*") )
		{
			number1 = tempNumber1 * tempNumber2;
			resultDisplay.setText( Float.toString ( number1 ) ) ;
			number2 = 0 ;
			operation = "" ;
			progressLabel.setText ( "" ) ;
		}
		else if ( operation.equals("/") )
		{
			number1 = tempNumber1 / tempNumber2;
			resultDisplay.setText( Float.toString ( number1 ) ) ;
			number2 = 0 ;
			operation = "" ;
			progressLabel.setText ( "" ) ;
		}
	}
}