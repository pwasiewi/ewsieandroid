package pl.edu.ewsie.android;

import android.app.Activity;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.centrum);
        zapisz = (Button) findViewById(R.id.zapisz);
        kolko = (Button) findViewById(R.id.kolko);
        zapisz.setOnClickListener(this);
        kolko.setOnClickListener(this);
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
    		break;
    	case R.id.mkolko:
        	Toast.makeText(this, "Naciśnięto element menu mkolko!", Toast.LENGTH_SHORT).show();
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
			break;
		case R.id.kolko:
			Toast.makeText(this, "Naciśnięto przycisk kolko!", Toast.LENGTH_SHORT).show();	
			break;
		}
	}



}
