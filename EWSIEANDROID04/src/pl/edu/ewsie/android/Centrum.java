package pl.edu.ewsie.android;
 
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;


public class Centrum extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.centrum);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.centrum, menu);
        return true;
    }
}
