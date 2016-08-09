package a3danimation.android.com.a3danimation;


import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_HOVER_EXIT;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_OUTSIDE;
import static android.view.MotionEvent.ACTION_UP;


public class ThreeDImageView extends ImageView implements
        Animation.AnimationListener , SensorEventListener{

    private static final Interpolator fDefaultInterpolator = new DecelerateInterpolator();

    private Drawable mDrawable;

    private Drawable mFlippedDrawable;

    private FlipAnimator mAnimation;

    public ThreeDImageView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ThreeDImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ThreeDImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        mDrawable = getDrawable();
        mAnimation = new FlipAnimator();
        mAnimation.setAnimationListener(this);
        mAnimation.setInterpolator(fDefaultInterpolator);
        mAnimation.setDuration(1000);

        lastTimeStamp = System.currentTimeMillis();
        SensorManager manager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> list1 = manager.getSensorList(Sensor.TYPE_ALL);

        Log.d("anurag"," list :"+ list1.size());
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ALL);
        manager.registerListener(this, sensor, Integer.MAX_VALUE);

        setImageDrawable(mDrawable);

    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }


    public void startAnimation() {
        startAnimation(mAnimation);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    float dX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case ACTION_DOWN:
            case ACTION_UP:
            case ACTION_OUTSIDE:
                if(dX == 0) {
                    dX = event.getRawX();
                    break;
                }
            case ACTION_MOVE:
                float diff = event.getRawX() - dX;

                if (diff > 20) {

                    Log.d("anurag"," right move by :"+ diff);
                    mAnimation.isLeftMove = false;
                    mAnimation.dX = Math.abs(diff);
                    startAnimation();
                    //right move
                } else if (diff < -20) {
                    Log.d("anurag"," left move by :"+ diff);
                    mAnimation.isLeftMove = true;
                    mAnimation.dX = Math.abs(diff);
                    startAnimation();
                    //left move
                }
                break;

        }
        return true;
    }

    long lastTimeStamp;

    float last_x = 0.0f;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        long timestamp = System.currentTimeMillis();

        if(timestamp - lastTimeStamp > 10) {

            if(sensorEvent.values[0] - last_x > 0.05f){
                mAnimation.isLeftMove = false;
                mAnimation.dX = Math.abs(sensorEvent.values[0] * 20);
                startAnimation();
                //right move
            }else if(sensorEvent.values[0] - last_x < 0.05f){
                //left move
                mAnimation.isLeftMove = true;
                mAnimation.dX = Math.abs(sensorEvent.values[0] * 20);
                startAnimation();
            }

            Log.d("anurag", sensorEvent.values[0] + " " + sensorEvent.values[1] + " " + sensorEvent.values[2]);
            lastTimeStamp = timestamp;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d("anurag", sensor+ " "+i);


    }

    public class FlipAnimator extends Animation {

        private Camera camera;

        private float centerX;

        private float centerY;

        private boolean isLeftMove;

        private float dX;

        private int width;

        public FlipAnimator() {
            setFillAfter(false);
        }


        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            camera = new Camera();
            this.centerX = width / 2;
            this.centerY = height / 2;
            this.width = width;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final double radians = Math.PI * dX/width;
            float degrees = (float) (180.0 * radians / Math.PI);

           /* if (interpolatedTime >= 0.5f) {
                degrees -= 180.0f;
            }*/

            final Matrix matrix = t.getMatrix();

            camera.save();
            camera.translate(0.0f, 0.0f, (float) (150.0 * Math.sin(radians)));
            camera.rotateX(0);
            camera.rotateZ(0);
            if (!isLeftMove)
                camera.rotateY(degrees);
            else
                camera.rotateY(-degrees);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}


