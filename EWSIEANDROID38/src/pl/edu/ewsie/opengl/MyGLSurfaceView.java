package pl.edu.ewsie.opengl;

/**
 * @author Piotr Wąsiewicz (piotrwasiewiczewsie@gmail.com) na podstawie blogów
 *  http://obviam.net/index.php/3d-programming-with-android-projections-perspective/
 *  http://www3.ntu.edu.sg/home/ehchua/programming/android/Android_3D.html
 *  http://www.songho.ca/opengl/gl_vertexarray.html
 */

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;
/*
 * Klasa potomna  GLSurfaceView 
 * dodająca obsługę zdarzeń klawiatury i dotyku (myszy w emulatorze) onKeyUp(), onTouchEvent()
 */
public class MyGLSurfaceView extends GLSurfaceView {
   GlRenderer renderer;    // Własny Renderer zaimplementowany w projekcie
   
   // Dla zdarzenia dotyku
   private final float TOUCH_SCALE_FACTOR = 180.0f / 320.0f;
   private float previousX;
   private float previousY;

   // Konstruktor ustaw renderer
   public MyGLSurfaceView(Context context) {
      super(context);
      renderer = new GlRenderer(context);
      this.setRenderer(renderer);
      // Ustaw fokus inaczej nie będzie reagować klawiatura i dotyk (mysza w emulatorze)
      this.requestFocus();  
      this.setFocusableInTouchMode(true);
   }
   
   // Obsługa klawiatury
   @Override
   public boolean onKeyUp(int keyCode, KeyEvent evt) {
      switch(keyCode) {
         case KeyEvent.KEYCODE_DPAD_LEFT:   	// zmniejsz prędkość kątową Y
            renderer.speedY -= 0.1f;
            break;
         case KeyEvent.KEYCODE_DPAD_RIGHT:  // zwiększ prędkość kątową Y
            renderer.speedY += 0.1f;
            break;
         case KeyEvent.KEYCODE_DPAD_UP:     	// zmniejsz prędkość kątową X  
            renderer.speedX -= 0.1f;
            break;
         case KeyEvent.KEYCODE_DPAD_DOWN:   // zwiększ prędkość kątową X 
            renderer.speedX += 0.1f;
            break;
         case KeyEvent.KEYCODE_A:           			 // zmniejsz z (oddalenie kamery  - głębokość)
            renderer.z -= 0.2f;
            break;
         case KeyEvent.KEYCODE_Z:           			// zwiększ z
            renderer.z += 0.2f;
            break;
      }
      return true;  // koniec obsługi
   }

   // Obsługa dotyku
   @Override
   public boolean onTouchEvent(final MotionEvent evt) {
      float currentX = evt.getX();
      float currentY = evt.getY();
      float deltaX, deltaY;
      switch (evt.getAction()) {
         case MotionEvent.ACTION_MOVE:
            // Zmodyfikuj kąty zależnie od ruchu
            deltaX = currentX - previousX;
            deltaY = currentY - previousY;
            renderer.angleX += deltaY * TOUCH_SCALE_FACTOR;
            renderer.angleY += deltaX * TOUCH_SCALE_FACTOR;
      }
      // Zapisz aktualne x, y
      previousX = currentX;
      previousY = currentY;
      return true;  //  koniec obsługi
   }
}