/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.edu.ewsie.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class Sweep extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SampleView(this));
    }

    private static class SampleView extends View {
        private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private float mRotate;
        private Matrix mMatrix = new Matrix();
        private Shader mShader;
        private boolean mDoTiming;
        float x = 160;
        float y = 200;
        float r = 100;
        float dr = 10;

        public SampleView(Context context) {
            super(context);
            setFocusable(true);
            setFocusableInTouchMode(true);

            mShader = new SweepGradient(x, y, new int[] { Color.GREEN,
                                                  Color.RED,
                                                  Color.BLUE,
                                                  Color.GREEN }, null);
            mPaint.setShader(mShader);
        }

        @Override protected void onDraw(Canvas canvas) {
            Paint paint = mPaint;

            canvas.drawColor(Color.WHITE);
 
            mMatrix.setRotate(mRotate, x, y);
            mShader.setLocalMatrix(mMatrix);
            mRotate += 3;
            if (mRotate >= 360) {
                mRotate = 0;
            }
            invalidate();

            if (mDoTiming) {
                long now = System.currentTimeMillis();
                for (int i = 0; i < 20; i++) {
                    canvas.drawCircle(x, y, r, paint);
                }
                now = System.currentTimeMillis() - now;
                android.util.Log.d("skia", "sweep ms = " + (now/20.));
            }
            else {
                canvas.drawCircle(x, y, r, paint);
            }
        }

        @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_Y:
                    mPaint.setDither(!mPaint.isDither());
                    invalidate();
                    return true;
                case KeyEvent.KEYCODE_T:
                    mDoTiming = !mDoTiming;
                    invalidate();
                    return true;
                case KeyEvent.KEYCODE_A:
                    r+=dr;
                    invalidate();
                    return true;
                case KeyEvent.KEYCODE_S:
                	r-=dr;
                    invalidate();
                    return true;
                case KeyEvent.KEYCODE_D:
                	y+=dr;
                	invalidate();
                    return true;
                case KeyEvent.KEYCODE_F:
                	y+=dr;
                	invalidate();
                    return true;
            }
            return super.onKeyDown(keyCode, event);
        }
    }
}

