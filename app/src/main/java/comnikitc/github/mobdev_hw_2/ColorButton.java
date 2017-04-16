package comnikitc.github.mobdev_hw_2;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class ColorButton extends Button implements View.OnLongClickListener {
    public float[] originalColor;
    public float[] currentColor;
    public float leftBorder;
    public float rightBorder;
    public long lastClickTime = 0;
    public float upBorderColor = 1;
    public float downBorderColor = 0.0f;
    public Boolean isBeingEdited = false;

    public ColorButton(Context context, float[] originalColor) {
        super(context);
        this.originalColor = originalColor.clone();
        this.currentColor = originalColor.clone();

        leftBorder = originalColor[0] - 12.25f;
        rightBorder = originalColor[0] + 12.25f;

        setOnLongClickListener(this);
    }

    public ColorButton(Context context) {
        super(context);
    }
    public ColorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ColorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String GetStringHSVColor() {
        float hue = currentColor[0];
        float saturation = currentColor[1];
        float value = Math.max(0.0f, currentColor[2]);

        return String.format("(%.2f, %.2f, %.2f)", hue, saturation, value);
    }

    public String GetStringRGBColor() {
        int colorValue = Color.HSVToColor(currentColor);
        int red = Color.red(colorValue);
        int green = Color.green(colorValue);
        int blue = Color.blue(colorValue);

        return String.format("(%s, %s, %s)", red, green, blue);
    }


    @Override
    public boolean onLongClick(View v) {
        isBeingEdited = true;
        Log.d("long", "1212");
        return false;
    }

    public void DiscardColor() {
        this.currentColor = originalColor.clone();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
