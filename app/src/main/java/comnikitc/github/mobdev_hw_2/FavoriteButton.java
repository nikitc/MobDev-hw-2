package comnikitc.github.mobdev_hw_2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;


public class FavoriteButton extends Button{

    public float[] hsvColor;
    public float[] getHsvColor() {
        return hsvColor;
    }

    public FavoriteButton(Context context, float[] hsv) {
        super(context);
        this.hsvColor = hsv;
    }

    public FavoriteButton(Context context) {
        super(context);
    }
    public FavoriteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
