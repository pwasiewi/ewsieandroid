package pl.edu.ewsie.android;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

 
public class Centrum extends Activity implements OnClickListener{
	Button zapisz;
	Button kolko;
    ObslugaAkcji obsluga;
    Notki notki;
    SQLiteDatabase baza;
    EditText edycja;
    Cursor czytnik;
    public static Integer liczba = 0; 
   
    
    public enum Komunikaty{ 
    	KOLKO ("pl.edu.ewsie.android.KOLKO"), 
    	ZAPISZ("pl.edu.ewsie.android.ZAPISZ"),; 
    	private String id;
    	private final static Map<String, Komunikaty> mapa = new HashMap<String, Komunikaty>();
    	static{ for (Komunikaty km: Komunikaty.values()) mapa.put(km.tekst(), km);}
    	private 					     	Komunikaty(String id) 	{ this.id = id; }
    	public String 					tekst() 						{ return id; }
    	public static Komunikaty get(String id) 				{ return mapa.get(id); }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.centrum);
        zapisz = (Button) findViewById(R.id.zapisz);
        kolko = (Button) findViewById(R.id.kolko);
        zapisz.setOnClickListener(this);
        kolko.setOnClickListener(this);
        obsluga = new ObslugaAkcji();
        notki = new Notki(this);
        baza = notki.getWritableDatabase();
        edycja = (EditText) findViewById(R.id.edycja);
    }
    
    public class ObslugaAkcji extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String kmtekst = intent.getAction();
			Toast.makeText(Centrum.this, "Wywołano obsługę komunikatu: "+kmtekst, Toast.LENGTH_SHORT).show();
			Komunikaty komunikat = Komunikaty.get(kmtekst);
			switch(komunikat){
			case KOLKO:
				Toast.makeText(Centrum.this, "Obsługa komunikatu: "+komunikat.tekst(), Toast.LENGTH_SHORT).show();
		       	startActivity(new Intent(Centrum.this, Sweep.class));
		        break;
			case ZAPISZ:
				Toast.makeText(Centrum.this, "Obsługa komunikatu: "+komunikat.tekst(), Toast.LENGTH_SHORT).show();
				String tekst = (String) edycja.getText().toString();
				ContentValues nota = new ContentValues();
				liczba = liczba +1;
				nota.put(Notki.N_ID, liczba.toString());
				nota.put(Notki.N_TEXT, tekst);
				baza.insertWithOnConflict(Notki.TABLE, null, nota, SQLiteDatabase.CONFLICT_REPLACE);
				Log.d("!!!!!!INSERT!!!!!!!!!","Wstawiłem tekst: " + tekst);
				break;
			}
		}
    }
    
    @Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(obsluga);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(obsluga, new IntentFilter(Komunikaty.KOLKO.tekst()));
		registerReceiver(obsluga, new IntentFilter(Komunikaty.ZAPISZ.tekst()));
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.centrum, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.mzapisz:
        	Toast.makeText(this, "Naciśnięto element menu mzapisz!", Toast.LENGTH_SHORT).show();
			sendBroadcast(new Intent(Komunikaty.ZAPISZ.tekst()));
    		break;
    	case R.id.mkolko:
        	Toast.makeText(this, "Naciśnięto element menu mkolko!", Toast.LENGTH_SHORT).show();
        	sendBroadcast(new Intent(Komunikaty.KOLKO.tekst()));
        	startActivity(new Intent(this, Sweep.class));
    		break;
    	}
    	return true;
    }
    
	@Override
	public void onClick(View v) {
		final Integer przycisk=v.getId();
		Log.d("ONCLICK!!!!", "debug w onclick");
		switch (przycisk) {
		case R.id.zapisz:
			Toast.makeText(this, "Naciśnięto przycisk zapisz!", Toast.LENGTH_SHORT).show();	
			sendBroadcast(new Intent(Komunikaty.ZAPISZ.tekst()));
			break;
		case R.id.kolko:
			Toast.makeText(this, "Naciśnięto przycisk kolko!", Toast.LENGTH_SHORT).show();
			sendBroadcast(new Intent(Komunikaty.KOLKO.tekst()));
			break;
		}
	}

	public class Notki extends SQLiteOpenHelper{
		private static final String DB_NAME = "notki.db";
		private static final int DB_VERSION=1;
		private static final String TABLE = "zapiski";
		private static final String N_ID = BaseColumns._ID;
		private static final String N_TEXT = "ntext";
		
		
		Context kontekts;
		
		public Notki(Context context){
		    super(context, DB_NAME, null, DB_VERSION);
		    //this.kontekst = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = String.format("create table %s (%s int, %s TEXT)", TABLE, N_ID, N_TEXT);
			db.execSQL(sql);
			Log.d("notki","onCreate sql: "+sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	

}
