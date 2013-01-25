package pl.edu.ewsie.android;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class Lista extends Activity{
	ListView oknolisty;
	Cursor czytnik;
	SimpleCursorAdapter oknobazy;
	static final ViewBinder Poprawiacz = new ViewBinder() {
		
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if(view.getId() == R.id.wnumer){
				int nowyId = cursor.getInt(columnIndex);
				CharSequence nowyIdTekst;
				
				if(nowyId < 10)
					nowyIdTekst = String.format("00%s", Integer.toString(nowyId));
				else if(nowyId < 100)
					nowyIdTekst = String.format("0%s", Integer.toString(nowyId));
				else 
					nowyIdTekst = String.format("%s", Integer.toString(nowyId));
				
				((TextView)view).setText(nowyIdTekst);
				return true;
			}
			else
				return false;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista);
		oknolisty = (ListView) findViewById(R.id.lista);
		czytnik = Centrum.notki.query();
		
		String[] from = {Centrum.Notki.N_ID, Centrum.Notki.N_TEXT};
		int[] to = {R.id.wnumer, R.id.wtekst};
		
		oknobazy = new SimpleCursorAdapter(this, R.layout.wiersz, czytnik, from, to, 0);
		oknobazy.setViewBinder(Poprawiacz);
		oknolisty.setAdapter(oknobazy);
		
		
		/*String nota;
		while(czytnik.moveToNext()){
			nota = czytnik.getString(czytnik.getColumnIndex(Centrum.Notki.N_TEXT));
			oknolisty.append(String.format("\n< %s >", nota));
		}*/
	}
	
	

}
