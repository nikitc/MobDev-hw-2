package comnikitc.github.mobdev_hw_2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;


public class ColorPickerScroll extends HorizontalScrollView {
    public Boolean isEdited = false;
    public ColorPickerScroll(Context context) {
        super(context);
    }

    public ColorPickerScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ColorPickerScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEdited) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
