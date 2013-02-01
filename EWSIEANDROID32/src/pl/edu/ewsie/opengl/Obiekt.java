/**
 * 
 */
package pl.edu.ewsie.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * @author Piotr Wąsiewicz (piotrwasiewiczewsie@gmail.com) na podstawie blogów
 *  http://obviam.net/index.php/3d-programming-with-android-projections-perspective/
 *  http://www3.ntu.edu.sg/home/ehchua/programming/android/Android_3D.html
 *  http://www.songho.ca/opengl/gl_vertexarray.html
 */
public class Obiekt {
	private float red, green, blue;    	// składowe koloru obiektu
	private FloatBuffer bufornapunkty;			// bufor na punkty
	private float wierzcholki[] = {
			-1.0f, -1.0f,  0.0f,		// V1 - na dole lewy
			-1.0f,  1.0f,  0.0f,		// V2 - na górze lewy
			 1.0f, -1.0f,  0.0f,		// V3 - na dole prawy
			 1.0f,  1.0f,  0.0f			// V4 - na górze prawy
	};

    // TEKSTURA: definicja punktów 	
    private FloatBuffer bufornateksture;	// bufor na punkty tekstury
	private float tekstura[] = {    		
			// mapowanie punktów tekstury, ZWRÓĆCIE UWAGĘ NA inną kolejność punktów: mapowanie uv
			0.0f, 1.0f,		// na górze lewy (V2)
			0.0f, 0.0f,		// na dole lewy	 (V1)
			1.0f, 1.0f,		// na górze prawy(V4)
			1.0f, 0.0f		// na dole prawy (V3)
	};
	
	// wskaźnik na teksturę
	private int[] textures = new int[1];

	public Obiekt(float red, float green, float blue) {
		// ustaw przy inicjacji kolor 
		this.red = red;
		this.green = green;
		this.blue = blue;
		
		// float ma 4 bajty to cztery bajty (ang. byte) allokujemy
		ByteBuffer bufornabajty = ByteBuffer.allocateDirect(wierzcholki.length * 4);
		bufornabajty.order(ByteOrder.nativeOrder());
		// alokacja pamięci z buforu na pukty
		bufornapunkty = bufornabajty.asFloatBuffer();
		// wrzuć wierzcholki do buforunapunkty
		bufornapunkty.put(wierzcholki);
		// ustaw kursor na początku buforu
		bufornapunkty.position(0);
		
		// TEKSTURA: bufor
		bufornabajty = ByteBuffer.allocateDirect(tekstura.length * 4);
		bufornabajty.order(ByteOrder.nativeOrder());
		bufornateksture = bufornabajty.asFloatBuffer();
		bufornateksture.put(tekstura);
		bufornateksture.position(0);

	}

	/**
	 * Załaduj teksturę do Obiektu
	 * @param gl
	 * @param context
	 */
	public void loadGLTexture(GL10 gl, Context context) {
		// TEKSTURA: załadowanie obrazka najlepiej kwadratu o bokach równych 2 do potęgi
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.android);

		// generowanie wskaźnika na teksturę czyli numeru 
		gl.glGenTextures(1, textures, 0);
		// i przypisanie go do naszej tablicy
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		// utwórz teksturę np. w taki sposób
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		// inne parametry struktury np. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		// Android GLUtils procedura przekształca bitmapę w dwuwymiarową strukturę   
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		// zwolnij cenną pamięć
		bitmap.recycle();
	}

	
	/** Rysuj obiekt w gl kontekście */
	public void draw(GL10 gl) {
		// ustaw kolor obiektu
		gl.glColor4f(red, green, blue, 0.5f);//red, green, blue
		
		// TEKSTURA: przypisz poprzednio zdefiniowaną teksturę
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		// wskaż wykonywane bufory
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// TEKSTURA: 
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		// ustaw rotację
		gl.glFrontFace(GL10.GL_CW);
		
		// wskaż na bufor na punkty: pierwszy argument oznacza ilość zmiennych np. x,y,z to 3 zmienne
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bufornapunkty);
		// TEKSTURA: 
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, bufornateksture);
		
		// rysuj połaczenia między wierzchołkami na zasadzie trójkątów
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, wierzcholki.length / 3);

		// usuń nawiązania do buforów
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		// TEKSTURA: 
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
}
