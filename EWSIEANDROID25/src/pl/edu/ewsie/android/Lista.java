package pl.edu.ewsie.android;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class Lista extends Activity{
	TextView oknolisty;
	Cursor czytnik;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista);
		oknolisty = (TextView) findViewById(R.id.lista);
		czytnik = Centrum.notki.query();
		
		String nota;
		while(czytnik.moveToNext()){
			nota = czytnik.getString(czytnik.getColumnIndex(Centrum.Notki.N_TEXT));
			oknolisty.append(String.format("\n< %s >", nota));
		}
	}
	
	

}
