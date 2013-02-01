package pl.edu.ewsie.opengl;

/**
 * @author Piotr Wąsiewicz (piotrwasiewiczewsie@gmail.com) na podstawie blogów
 *  http://obviam.net/index.php/3d-programming-with-android-projections-perspective/
 *  http://www3.ntu.edu.sg/home/ehchua/programming/android/Android_3D.html
 *  http://www.songho.ca/opengl/gl_vertexarray.html
 */

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Run extends Activity {
	
	// OpenGL okno 
	private MyGLSurfaceView glSurfaceView;
	
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
        glSurfaceView = new MyGLSurfaceView(this);
        
        // renderer przypisany do okna opengl 
        // z tego kontekstu - TERAZ w konstruktorze MyGLSurfaceView
        // glSurfaceView.setRenderer(new GlRenderer(this));
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