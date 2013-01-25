package pl.edu.ewsie.opengl;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Run extends Activity {
	
	// OpenGL okno 
	private GLSurfaceView glSurfaceView;
	
    // Przy tworzeniu Aktywności (ang. activity) 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // usuwanie głównego tytułu
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // pełny ekran
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // tworzenie okna opengl
        glSurfaceView = new GLSurfaceView(this);
        
        // renderer przypisany do okna opengl 
        // z tego kontekstu
        glSurfaceView.setRenderer(new GlRenderer(this));
        setContentView(glSurfaceView);
    }

	// Ponownie uruchamiaj glSurface
	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
	}


	// Pauzuj glSurface
	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();
	}

}