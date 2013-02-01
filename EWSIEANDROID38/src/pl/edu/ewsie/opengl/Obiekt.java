/**
 * 
 */
package pl.edu.ewsie.opengl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.util.Log;

/**
 * @author Piotr Wąsiewicz (piotrwasiewiczewsie@gmail.com) na podstawie blogów
 *  http://obviam.net/index.php/3d-programming-with-android-projections-perspective/
 *  http://www3.ntu.edu.sg/home/ehchua/programming/android/Android_3D.html
 *  http://www.songho.ca/opengl/gl_vertexarray.html
 */
public class Obiekt {

	private class punkt {
		float v[] = {0.0f, 0.0f, 0.0f};
		public void ustaw(float x, float y, float z){
			v[0] = x; v[1] = y; v[2] = z;
		}
		public punkt(float x, float y, float z){
			v[0] = x; v[1] = y;	v[2] = z;
		}
		public punkt(String linia){
			temp = linia.split(" ");
			v[0] = Float.valueOf(temp[1].trim()).floatValue();
			v[1] = Float.valueOf(temp[2].trim()).floatValue();
			v[2] = Float.valueOf(temp[3].trim()).floatValue();
		}
	}
	private class sciana {
		int nrpunktu[] = new int[3];
		int normalna[] = new int[3];
		int material;
		public sciana(String linia){
			temp = linia.split(" ");
			temp2 = temp[1].split("//"); 
			nrpunktu[0] = Integer.parseInt(temp2[0].trim());
			normalna[0] = Integer.parseInt(temp2[1].trim());
			temp2 = temp[2].split("//");
			nrpunktu[1] = Integer.parseInt(temp2[0].trim());
			normalna[1] = Integer.parseInt(temp2[1].trim());
			temp2 = temp[3].split("//");
			nrpunktu[2] = Integer.parseInt(temp2[0].trim());
			normalna[2] = Integer.parseInt(temp2[1].trim());
			//material = faceIndex;			
		}
	}
	private int ilepunktow = 0;
	private int ilenormalnych = 0;
	private int ilescian = 0;
	String[] temp, temp2;
	String linia;
	
	private float czerwony, zielony, niebieski;    	// składowe koloru obiektu
	private FloatBuffer bufornapunkty;			    // bufor na punkty
	private ArrayList<punkt> punkty = new ArrayList<punkt>();
	private FloatBuffer bufornanormalne;			// bufor na normalne
	private ArrayList<punkt> normalne = new ArrayList<punkt>();
	private ArrayList<sciana> sciany = new ArrayList<sciana>();
	sciana powierzchnia;
	sciana macierzscian[];
	private ShortBuffer bufornaindeksy;				// bufor na indeksy
	
    // TEKSTURA: definicja punktów 	
    private FloatBuffer bufornateksture;	// bufor na punkty tekstury
	private float tekstura[] = {    		
			// mapowanie punktów tekstury, ZWRÓĆCIE UWAGĘ NA inną kolejność punktów: mapowanie uv
			0.0f, 1.0f,		// na górze lewy (V2)
			0.0f, 0.0f,	// na dole lewy	 (V1)
			1.0f, 1.0f,		// na górze prawy(V4)
			1.0f, 0.0f		// na dole prawy (V3)
	};
	
	// wskaźnik na teksturę
	private int[] tekstury = new int[2];

	// KOLORY
	private FloatBuffer bufornakolory;	// bufor na kolory

	
	 
	public Obiekt(Context kontekst, int IdRes) {

		InputStream strumien = kontekst.getResources().openRawResource(IdRes);
		InputStreamReader czytnikstrumienia = new InputStreamReader(strumien);
		BufferedReader czytnikbufora = new BufferedReader(czytnikstrumienia);
		try
		{
			while((linia = czytnikbufora.readLine()) != null) {
				
				if(linia != null && (linia.length() > 1)) {
					temp = linia.split(" ");
					switch(linia.charAt(0)) {
					case 'v':
						// załaduj wierzchołki (punkty), normalne do powierzchni 
						// oraz powierzchnie trójkątów (ang. face) wyznaczone indeksami wierzchołków i normalnych 
						switch(linia.charAt(1)) {
							case ' ':
								punkty.add(new punkt(linia)); 
								break;
							case 'n':
								normalne.add(new punkt(linia)); 
								break;
							case 't':
								// ignore textures
								break;
						}
						break;
					case 'f':
						// załaduj ściany trójkąty 
						// (plik .obj mieć powierzchnie - trójkątne ograniczenie OpenGLES) 
						sciany.add(new sciana(linia));
						break;
					
					}
				}
			}
	}
	catch (IOException e) 
	{
		Log.e("Obiekt","Pliku wavefront obj nie ma!");
	}	
		
/*		punkty.add(new punkt("v 1.000000 -1.000000 -1.000000")); 
		punkty.add(new punkt("v 1.000000 -1.000000 1.000000")); 
		punkty.add(new punkt("v -1.000000 -1.000000 1.000000")); 
		punkty.add(new punkt("v -1.000000 -1.000000 -1.000000")); 
		punkty.add(new punkt("v 1.000000 1.000000 -1.000000")); 
		punkty.add(new punkt("v 1.000000 1.000000 1.000001")); 
		punkty.add(new punkt("v -1.000000 1.000000 1.000000")); 
		punkty.add(new punkt("v -1.000000 1.000000 -1.000000")); 

		
	    normalne.add(new punkt("vn 0.000000 -1.000000 0.000000")); 
		normalne.add(new punkt("vn 0.000000 1.000000 0.000000")); 
		normalne.add(new punkt("vn 1.000000 0.000000 0.000000")); 
		normalne.add(new punkt("vn -0.000000 -0.000000 1.000000")); 
		normalne.add(new punkt("vn -1.000000 -0.000000 -0.000000")); 
		normalne.add(new punkt("vn 0.000000 0.000000 -1.000000")); 

		
		sciany.add(new sciana("f 1//1 2//1 3//1"));ilescian++;
		sciany.add(new sciana("f 1//1 3//1 4//1"));ilescian++;
		sciany.add(new sciana("f 5//2 8//2 7//2"));ilescian++;
		sciany.add(new sciana("f 5//2 7//2 6//2"));ilescian++;
		sciany.add(new sciana("f 1//3 5//3 6//3"));ilescian++;
		sciany.add(new sciana("f 1//3 6//3 2//3"));ilescian++;
		sciany.add(new sciana("f 2//4 6//4 7//4"));ilescian++;
		sciany.add(new sciana("f 2//4 7//4 3//4"));ilescian++;
		sciany.add(new sciana("f 3//5 7//5 8//5"));ilescian++;
		sciany.add(new sciana("f 3//5 8//5 4//5"));ilescian++;
		sciany.add(new sciana("f 5//6 1//6 4//6"));ilescian++;
		sciany.add(new sciana("f 5//6 4//6 8//6"));ilescian++;*/
		
		ilepunktow = punkty.size();
        ilenormalnych = normalne.size();
		ilescian = sciany.size();
		
		macierzscian = new sciana[sciany.size()];
		macierzscian = (sciana[])sciany.toArray(macierzscian);
		
		int lookup[][] = new int[ilepunktow][ilenormalnych];
		int npunkty = 0;
		
		// Łączymy wierzchołki i normalne dla każdego wierzchołka
		
		for(int i=0; i<ilepunktow; i++) {
			for(int j=0; j<ilenormalnych; j++) {
				lookup[i][j] = -1;
			}
		}
		for(int i=0; i<ilescian; i++) {
			for(int j=0; j<3; j++) {
				int vertex = macierzscian[i].nrpunktu[j] - 1;
				int normal = macierzscian[i].normalna[j] - 1;
				if(lookup[vertex][normal] == -1) {
					lookup[vertex][normal] = npunkty;
					npunkty++;
				}
			}
		}
		// Po policzeniu punktów możemy wykonać tablice do glDrawElements

		float macierzmiennych[] = new float[npunkty * 3];
		float normalzmiennych[] = new float[npunkty * 3];
		punkt macierzpunktow[] = new punkt[punkty.size()]; 
		macierzpunktow = (punkt[])punkty.toArray(macierzpunktow);
		punkt macierznormal[] = new punkt[normalne.size()]; 
		macierznormal = (punkt[])normalne.toArray(macierznormal);
		
		for(int i=0;i<ilepunktow;i++) {
			for(int j=0;j<ilenormalnych; j++) {
				int vertex = lookup[i][j];
				if(vertex != -1) {
					for(int k=0; k<3; k++) {
						macierzmiennych[3 * vertex + k] = macierzpunktow[i].v[k];
						normalzmiennych[3 * vertex + k] = macierznormal[j].v[k];
					}
				}
			}
		}
		
		int indekscian;
		short indeksy[] = new short[ilescian*3]; 
		indekscian = 0;
		for(int i=0;i<ilescian;i++) {
			for(int j=0;j<3;j++) {
				int vertex = macierzscian[i].nrpunktu[j] - 1;
				int normal = macierzscian[i].normalna[j] - 1;
				indeksy[3 * indekscian + j] = (short)lookup[vertex][normal];
			}
			indekscian += 1;
		}

		// Zbuduj losową tablicę kolorów o takiej samej długości
		float[] kolory = new float[npunkty];
		for(int i=0; i<npunkty; i++) {
			kolory[i] = (float)Math.random(); 
		}

		
		// float ma 4 bajty to cztery bajty (ang. byte) allokujemy
		ByteBuffer bufornabajty = ByteBuffer.allocateDirect(npunkty * 3 * 4);
		bufornabajty.order(ByteOrder.nativeOrder());
		// alokacja pamięci z buforu na pukty
		bufornapunkty = bufornabajty.asFloatBuffer();
		// wrzuć wierzcholki do buforunapunkty
		bufornapunkty.put(macierzmiennych);
		// ustaw kursor na początku buforu
		bufornapunkty.position(0);
	
		bufornabajty = ByteBuffer.allocateDirect(npunkty * 3 * 4);
		bufornabajty.order(ByteOrder.nativeOrder());
		bufornanormalne = bufornabajty.asFloatBuffer();
		bufornanormalne.put(normalzmiennych);
		bufornanormalne.position(0);
		
		bufornabajty = ByteBuffer.allocateDirect(ilescian*3*2);
		bufornabajty.order(ByteOrder.nativeOrder());
		bufornaindeksy = bufornabajty.asShortBuffer();
		bufornaindeksy.put(indeksy);
		bufornaindeksy.position(0);
				
		
/*	// TEKSTURA: bufor
		bufornabajty = ByteBuffer.allocateDirect(tekstura.length * 4);
		bufornabajty.order(ByteOrder.nativeOrder());
		bufornateksture = bufornabajty.asFloatBuffer();
		bufornateksture.put(tekstura);
		bufornateksture.position(0);*/
		
		// KOLORY: bufor
		bufornabajty = ByteBuffer.allocateDirect(kolory.length * 4);
		bufornabajty.order(ByteOrder.nativeOrder());
		bufornakolory = bufornabajty.asFloatBuffer();
		bufornakolory.put(kolory);
		bufornakolory.position(0);
	}

	/**
	 * Załaduj teksturę do Obiektu
	 * @param gl
	 * @param context
	 */
	public void loadGLTexture(GL10 gl, Context context) {
		/*
	// TEKSTURA: załadowanie obrazka najlepiej kwadratu o bokach równych 2 do potęgi
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.android);

		Bitmap bitmap3d = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.android3d);

		
		// generowanie wskaźnika na teksturę czyli numeru 
		gl.glGenTextures(2, tekstury, 0);
		
		// i przypisanie go do naszej tablicy
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tekstury[0]);
		// utwórz teksturę np. w taki sposób
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		// inne parametry struktury np. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
    	// Android GLUtils procedura przekształca bitmapę w dwuwymiarową strukturę   
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		// i przypisanie go do naszej tablicy
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tekstury[1]);
		// utwórz teksturę np. w taki sposób
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		// inne parametry struktury np. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		// Android GLUtils procedura przekształca bitmapę w dwuwymiarową strukturę   
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap3d, 0);
		// zwolnij cenną pamięć
		bitmap3d.recycle();
		// zwolnij cenną pamięć
		bitmap.recycle();
		*/
	}

	
	/** Rysuj obiekt w gl kontekście */
	public void draw(GL10 gl, int ntekstura) {
		// jak zakomentowane KOLORY: ustaw kolor obiektu
		//gl.glColor4f(czerwony, zielony, niebieski, 0.5f); //czerwony, zielony, niebieski
		// TEKSTURA: przypisz poprzednio zdefiniowaną teksturę
//		gl.glBindTexture(GL10.GL_TEXTURE_2D, tekstury[ntekstura]);
		// wskaż wykonywane bufory
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//NORMALNE
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		// TEKSTURA
// 	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        // KOLORY
	    gl.glEnableClientState(GL10.GL_COLOR_ARRAY);            
		// ustaw rotację wewnątrz trójkątów CW lub CCW (zgodnie lub przeciwnie do kierunku wskazówek zegara)
		//gl.glFrontFace(GL10.GL_CW);      //raczej dominuje CCW
	     gl.glFrontFace(GL10.GL_CCW);    	// ściana trójkąt w odwrotnej do ruchu wskazówek zegara 
	     gl.glEnable(GL10.GL_CULL_FACE);// Przesłonięte ściany
	     gl.glCullFace(GL10.GL_BACK);    		// Nie wyświetlaj tylniej ściany
		// wskaż na bufor na punkty: pierwszy argument oznacza ilość zmiennych np. x,y,z to 3 zmienne
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bufornapunkty);
		//NORMALNE
		gl.glNormalPointer( GL10.GL_FLOAT, 0, bufornanormalne);
		// TEKSTURA: 
//		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, bufornateksture);
		// KOLORY
	    gl.glColorPointer(4, GL10.GL_FLOAT, 0, bufornakolory);
		// rysuj połaczenia między wierzchołkami na zasadzie trójkątów
		//gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, ilepunktow);
		gl.glDrawElements(GL10.GL_TRIANGLES, bufornaindeksy.capacity(),
				   GL10.GL_UNSIGNED_SHORT, bufornaindeksy);
    	// usuń nawiązania do buforów
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		//NORMALNE
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		// TEKSTURA
//  	gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// KOLORY
	    gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
}
