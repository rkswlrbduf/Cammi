package samsung.membership.splash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by KyuYeol on 2017-08-13.
 */

public class UnderlineAnim extends View {

    myanim anim;

    private class myanim extends Animation {

        public myanim() {
            this.setDuration(1500);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            time = getWidth() * interpolatedTime;
            postInvalidate();
        }
    }

    public UnderlineAnim(Context context) {
        super(context);
    }

    public UnderlineAnim(Context context, AttributeSet attrs) {
        super(context, attrs);
        anim = new myanim();
        this.startAnimation(anim);
    }

    public void AnimStart() {
        this.startAnimation(anim);
    }

    Paint paint = new Paint();
    float time = 0;

    @Override
    public void onDraw(Canvas canvas) {

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#24282b"));
        paint.setStrokeWidth(20);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
        paint.setStrokeWidth(20);
        paint.setColor(Color.RED);
        canvas.drawLine(0, getHeight() / 2, time, getHeight() / 2, paint);
        //canvas.drawLine(getWidth() / 2, getHeight() / 2, , getHeight() / 2, paint);
        super.onDraw(canvas);
    }
}