package pl.edu.ewsie.android;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class Centrum extends Activity implements OnClickListener{
	Button zapisz;
	Button kolko;
    ObslugaAkcji obsluga;
    public enum Komunikaty{ 
    	KOLKO ("pl.edu.ewsie.android.KOLKO"), 
    	ZAPISZ("pl.edu.ewsie.android.ZAPISZ"),; 
    	private String id;
    	private final static Map<String, Komunikaty> mapa=new HashMap<String, Komunikaty>();
    	static{ for (Komunikaty km: Komunikaty.values()) mapa.put(km.tekst(), km);}
    	private 					Komunikaty(String id) { this.id = id; }
    	public String 				tekst() { return id; }
    	public static Komunikaty 	get(String id) { return mapa.get(id); }
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



}
