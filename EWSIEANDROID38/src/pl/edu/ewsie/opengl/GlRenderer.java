package pl.edu.ewsie.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

/**
 * @author Piotr Wąsiewicz (piotrwasiewiczewsie@gmail.com) na podstawie blogów
 *  http://obviam.net/index.php/3d-programming-with-android-projections-perspective/
 *  http://www3.ntu.edu.sg/home/ehchua/programming/android/Android_3D.html
 *  http://www.songho.ca/opengl/gl_vertexarray.html
 */

public class GlRenderer implements Renderer {

	private Obiekt 		kwadrat[];    				// "wskaźnik" na tablicę "wskaźników" na kwadraty
	private Context 	    kontekst;
	   //Do kontrolowania pozycji obiektów
	   float angleX = 0;  
	   float angleY = 0;  
	   float speedX = 0;  
	   float speedY = 0;   
	   float z = -6.0f;    
	   
	/** Konstruktor kontekstu GL */
	public GlRenderer(Context context) {
		this.kontekst = context;
		
		// inicjalizowanie obiektów typu kwadrat
		this.kwadrat = new Obiekt[4];				// tablica "wskaźników" na kwadraty czyli referencje
		kwadrat[0] = new Obiekt(this.kontekst, R.raw.szescian);
		kwadrat[1] = new Obiekt(this.kontekst, R.raw.stozek);
		kwadrat[2] = new Obiekt(this.kontekst, R.raw.szescian);
		kwadrat[3] = new Obiekt(this.kontekst, R.raw.szescian);

	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// wyczyść ekran i bufor
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// resetuj tablicę modelu
		gl.glLoadIdentity();

		// rysuj pierwszy kwadrat
		gl.glTranslatef(-0.5f, 0.0f, -5.0f);	// przesuń się 5 jednostek w ekran 
																// to to samo co cofnięcie się kamerą o 5 j.
		gl.glScalef(0.2f, 0.2f, 0.2f);			// zmniejsz każdy wymiar o 50%
																// bo kwadrat będzie zbyt duży
	    gl.glRotatef(0f, 1.0f, 0.0f, 0.0f); 	// obróć wokół osi x
	    gl.glRotatef(-10f, 0.0f, 1.0f, 0.0f);	// obróć wokół osi y
		kwadrat[0].draw(gl,0);						// rysuj kwadrat
		gl.glScalef(2f, 2f, 2f);
		
		// rysuj następne kwadraty
		gl.glTranslatef(3f, 0.0f, 0.0f);				// przesuń się 5 jednostek w ekran oraz 3 w bok
	      gl.glTranslatef(0.0f, 0.0f, z);          		// przesuń kamerę
	      gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f); 	// obróć o kąt angleX wokół osi x
	      gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f); 	// obróć o kąt angleY wokół osi y
		kwadrat[1].draw(gl,1);

		gl.glTranslatef(0f, 3f, -5.0f);			// przesuń się 5 jednostek w ekran oraz 3 do góry
		kwadrat[2].draw(gl,0);	
	
		gl.glTranslatef(0f, 3f, -10.0f);		// przesuń się 5 jednostek w ekran oraz 3 do góry
		kwadrat[3].draw(gl,1);	
		
	    // Zmien kąt rotacji x, y
	    angleX += speedX;  
	    angleY += speedY; 
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//zapobiegnij dzieleniu przez zero
			height = 1; 							//przyrównaj do 1
		}

		gl.glViewport(0, 0, width, height); 					//resetowanie aktualnego okna
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//wybranie w maszynie stanu opengl macierzy projekcji
		gl.glLoadIdentity(); 											//resetowanie macierzy projekcji

		//Obliczanie perspektywy
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//wybranie w maszynie stanu opengl macierzy modelu
		gl.glLoadIdentity(); 										//resetowanie macierzy modelu
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TEKSTURA: Przypisanie tekstury do kwadratów
		kwadrat[0].loadGLTexture(gl, this.kontekst);
		kwadrat[1].loadGLTexture(gl, this.kontekst);
		kwadrat[2].loadGLTexture(gl, this.kontekst);
		kwadrat[3].loadGLTexture(gl, this.kontekst);
		gl.glEnable(GL10.GL_TEXTURE_2D);			//włącz mapowanie tekstury
		gl.glShadeModel(GL10.GL_SMOOTH); 		//włącz płynne cieniowanie
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 		//czare tło
		gl.glClearDepthf(1.0f); 								//inicjacja bufora głębokości
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//testowanie tego bufora
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//wybór typu bufora
		
		//najlepiej wyglądające przeliczenia perspektywy
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 

	}

}
