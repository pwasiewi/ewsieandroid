package pl.edu.ewsie.android;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class Lista extends Activity{
	ListView oknolisty;
	Cursor czytnik;
	SimpleCursorAdapter oknobazy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista);
		oknolisty = (ListView) findViewById(R.id.lista);
		czytnik = Centrum.notki.query();
		
		String[] from = {Centrum.Notki.N_ID, Centrum.Notki.N_TEXT};
		int[] to = {R.id.wnumer, R.id.wtekst};
		
		oknobazy = new SimpleCursorAdapter(this, R.layout.wiersz, czytnik, from, to, 0);
		oknolisty.setAdapter(oknobazy);
		
		
		/*String nota;
		while(czytnik.moveToNext()){
			nota = czytnik.getString(czytnik.getColumnIndex(Centrum.Notki.N_TEXT));
			oknolisty.append(String.format("\n< %s >", nota));
		}*/
	}
	
	

}
