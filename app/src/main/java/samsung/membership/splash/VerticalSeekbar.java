package samsung.membership.splash;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class VerticalSeekbar extends AppCompatSeekBar
{
    public VerticalSeekbar(Context c)
    {
        super(c);
    }

    public VerticalSeekbar(Context c, AttributeSet attrs)
    {
        super(c, attrs);
    }

    public VerticalSeekbar(Context c, AttributeSet attrs, int defStyle)
    {
        super(c, attrs, defStyle);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c)
    {
        c.rotate(-90);
        c.translate(-getHeight(), 0);

        super.onDraw(c);
    }

    private OnSeekBarChangeListener mChangeListener;

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onChangeListener)
    {
        this.mChangeListener = onChangeListener;
    }

    private int mLastProgress = 0;

    public boolean onTouchEvent(MotionEvent event)
    {
        if(!isEnabled())
        {
            return false;
        }
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(mChangeListener != null)
                    mChangeListener.onStartTrackingTouch(this);

                setPressed(true);
                setSelected(true);
                break;

            case MotionEvent.ACTION_MOVE:
                super.onTouchEvent(event);
                int nProgress = getMax() - (int) (getMax() * event.getY() / getHeight());
                if(nProgress < 0)
                {
                    nProgress = 0;
                }
                if(nProgress > getMax())
                {
                    nProgress = getMax();
                }
                setProgress(nProgress); // Draw progress
                if(nProgress != mLastProgress)
                {
                    mLastProgress = nProgress;
                    if(mChangeListener != null)
                        mChangeListener.onProgressChanged(this, nProgress, true);
                }
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                setPressed(true);
                setSelected(true);

                break;
            case MotionEvent.ACTION_UP:
                if(mChangeListener != null)
                    mChangeListener.onStopTrackingTouch(this);
                setPressed(false);
                setSelected(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                super.onTouchEvent(event);
                setPressed(false);
                setSelected(false);
                break;
        }
        return true;

    }

    public synchronized void setProgressAndThumb(int progress)
    {
        setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
        if(progress != mLastProgress)
        {
            mLastProgress = progress;
            if(mChangeListener != null)
                mChangeListener.onProgressChanged(this, progress, true);
        }
    }

    public synchronized void setMaximum(int maximum)
    {
        setMax(maximum);
    }

    public synchronized int getMaximum()
    {
        return getMax();
    }
}