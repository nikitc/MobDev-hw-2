package comnikitc.github.mobdev_hw_2;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class ColorButton extends Button implements View.OnLongClickListener {
    private float[] originalColor;
    public float[] getOriginalColor() {
        return originalColor;
    }

    private float[] currentColor;
    public float[] getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(float[] value) {
        currentColor = value;
    }

    private float leftBorder;
    public float getLeftBorder() {
        return leftBorder;
    }

    private float rightBorder;
    public float getRightBorder() {
        return rightBorder;
    }

    public long lastClickTime = 0;
    public final float upBorderColor = 1;
    public final float downBorderColor = 0.0f;

    private Boolean isBeingEdited = false;
    public Boolean getIsBeingEdited() {
        return isBeingEdited;
    }

    public void setIsBeingEdited(Boolean value) {
        isBeingEdited = value;
    }


    public ColorButton(Context context, float[] originalColor) {
        super(context);
        this.originalColor = originalColor.clone();
        this.currentColor = originalColor.clone();

        leftBorder = originalColor[0] - 12.25f;
        rightBorder = originalColor[0] + 12.25f;

        this.setBackgroundColor(Color.HSVToColor(originalColor));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.width = 150;
        params.height = 150;
        params.leftMargin = 45;
        params.rightMargin = 45;
        this.setLayoutParams(params);
        setOnLongClickListener(this);
    }

    public ColorButton(Context context, int color) {
        super(context);
        Color.colorToHSV(color, this.originalColor);
        Color.colorToHSV(color, this.currentColor);
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

    public static String GetStringHSVColor(float[] color) {
        float hue = color[0];
        float saturation = color[1];
        float value = Math.max(0.0f, color[2]);

        return String.format("HSV: (%.2f, %.2f, %.2f)", hue, saturation, value);
    }

    public static String GetStringRGBColor(float[] color) {
        int colorValue = Color.HSVToColor(color);
        int red = Color.red(colorValue);
        int green = Color.green(colorValue);
        int blue = Color.blue(colorValue);

        return String.format("RGB: (%s, %s, %s)", red, green, blue);
    }


    @Override
    public boolean onLongClick(View v) {
        isBeingEdited = true;
        return false;
    }

    public void DiscardColor() {
        this.currentColor = originalColor.clone();
    }
}
