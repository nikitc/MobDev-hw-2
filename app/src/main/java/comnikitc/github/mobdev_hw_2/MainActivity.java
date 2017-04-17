package comnikitc.github.mobdev_hw_2;

import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button favoriteButton;
    ArrayDeque<Integer> favoriteColors = new ArrayDeque<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreateColorPicker();
        CreateAddFavoriteButton();

        Button favorites = (Button) findViewById(R.id.favoriteColorsButton);
        favorites.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            int color = intent.getExtras().getInt("chooseColor");
            ArrayList<Integer> colors = intent.getExtras().getIntegerArrayList("favoriteColors");
            if (colors == null) {
                return;
            }
            favoriteColors = new ArrayDeque<Integer>(colors);
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            ColorButton buttonDisplay = new ColorButton(this, hsv);
            DisplayCurrentStatus(buttonDisplay);
        }
    }

    protected void CreateAddFavoriteButton() {
        favoriteButton = (Button) findViewById(R.id.favoritebutton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView chooseColor = (ImageView) findViewById(R.id.chooseColorImage);
                ColorDrawable drawable = (ColorDrawable) chooseColor.getBackground();
                if (favoriteColors.size() == 3) {
                    favoriteColors.removeFirst();
                }
                favoriteColors.add(drawable.getColor());

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Color add favorite", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    protected ColorButton GetNewButton(float[] pixelHSV) {
        ColorButton button = new ColorButton(this, pixelHSV.clone());
        button.setBackgroundColor(Color.HSVToColor(pixelHSV));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.width = 120;
        params.height = 120;
        params.leftMargin = 45;
        params.rightMargin = 45;
        button.setLayoutParams(params);

        return button;
    }

    protected void SetGradientBackGround(final LinearLayout colorPickerLayout) {
        ShapeDrawable.ShaderFactory factory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient lg = new LinearGradient(0, 0,
                        colorPickerLayout.getWidth(), colorPickerLayout.getHeight(),
                        GradientColorPicker.GetValueColors(),
                        GradientColorPicker.GetPositionColors(),
                        Shader.TileMode.REPEAT);
                return lg;
            }
        };

        PaintDrawable paint = new PaintDrawable();
        paint.setShape(new RectShape());
        paint.setShaderFactory(factory);
        colorPickerLayout.setBackground(paint);
    }

    protected void DisplayCurrentStatus(ColorButton currentColorButton) {

        ImageView chooseColorView = (ImageView) findViewById(R.id.chooseColorImage);

        TextView rgbText = (TextView) findViewById(R.id.chooseValueRGB);
        TextView hsvText = (TextView) findViewById(R.id.chooseValueHSV);

        chooseColorView.setBackgroundColor(
                Color.HSVToColor(currentColorButton.currentColor));

        String newRGBColor = currentColorButton.GetStringRGBColor();
        String newHSVColor = currentColorButton.GetStringHSVColor();

        rgbText.setText(newRGBColor);
        hsvText.setText(newHSVColor);
    }


    protected void DisplayOnPalitreStatus(ColorButton currentColorButton) {

        ImageView chooseColorView = (ImageView) findViewById(R.id.currentColorImage);

        TextView rgbText = (TextView) findViewById(R.id.currentValueRGB);
        TextView hsvText = (TextView) findViewById(R.id.currentValueHSV);


        chooseColorView.setBackgroundColor(
                Color.HSVToColor(currentColorButton.currentColor));

        String newRGBColor = currentColorButton.GetStringRGBColor();
        String newHSVColor = currentColorButton.GetStringHSVColor();

        rgbText.setText(newRGBColor);
        hsvText.setText(newHSVColor);
    }

    protected boolean IsDoubleClick(long lastClickTime) {
        final long doublePressInterval = 500;
        return SystemClock.elapsedRealtime() - lastClickTime < doublePressInterval;
    }

    protected View.OnLongClickListener GetOnLongClickListener() {
        View.OnLongClickListener onClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Start editing", Toast.LENGTH_SHORT);
                toast.show();
                DisableColorPickerScroll();
                return true;
            }
        };

        return onClickListener;
    }


    protected View.OnTouchListener GetOnTouchListener() {
        View.OnTouchListener onClickListener = new View.OnTouchListener() {
            float x = 0;
            float y = 0;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                ColorPickerScroll scroll = (ColorPickerScroll) findViewById(R.id.colorPickerScroll);
                ColorButton currentColorButton = (ColorButton) view;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        HandleActionDown(currentColorButton);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!scroll.isEdited) {
                            break;
                        }
                        HandleActionMove(currentColorButton, x, y, event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (scroll.isEdited) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Editing is finished", Toast.LENGTH_SHORT);
                            toast.show();
                            scroll.isEdited = false;
                        }
                        break;
                }
                return false;
            }
        };
        return onClickListener;
    }

    protected void HandleActionMove(ColorButton currentColorButton, float oldX, float oldY,
                                    float x, float y) {

        if (oldX < x && Math.abs(oldY - y) < 1) {
            if (currentColorButton.currentColor[0] == currentColorButton.rightBorder) {
                CallVibrator();
            }
            if (currentColorButton.currentColor[0] < currentColorButton.rightBorder) {
                currentColorButton.currentColor[0] += 0.25;
                currentColorButton.setBackgroundColor(
                        Color.HSVToColor(currentColorButton.currentColor));
                DisplayOnPalitreStatus(currentColorButton);
            }
            return;
        }

        if (oldX > x && Math.abs(oldY - y) < 1) {
            if (currentColorButton.currentColor[0] == currentColorButton.leftBorder) {
                CallVibrator();
            }
            if (currentColorButton.currentColor[0] > currentColorButton.leftBorder) {
                currentColorButton.currentColor[0] -= 0.25;
                currentColorButton.setBackgroundColor(
                        Color.HSVToColor(currentColorButton.currentColor));
                DisplayOnPalitreStatus(currentColorButton);
            }
            return;
        }

        if (oldY < y && Math.abs(oldX - x) < 1) {
            if (currentColorButton.currentColor[2] <= currentColorButton.downBorderColor) {
                CallVibrator();
            }
            if (currentColorButton.currentColor[2] > currentColorButton.downBorderColor) {
                currentColorButton.currentColor[2] -= 0.05;
                currentColorButton.setBackgroundColor(
                        Color.HSVToColor(currentColorButton.currentColor));
                DisplayOnPalitreStatus(currentColorButton);
            }
            return;
        }
        if (oldY > y && Math.abs(oldX - x) < 1) {
            if (currentColorButton.currentColor[2] == currentColorButton.upBorderColor) {
                CallVibrator();
            }
            if (currentColorButton.currentColor[2] < currentColorButton.upBorderColor) {
                currentColorButton.currentColor[2] += 0.05;
                currentColorButton.setBackgroundColor(
                        Color.HSVToColor(currentColorButton.currentColor));
                DisplayOnPalitreStatus(currentColorButton);
            }
        }
    }

    protected void HandleActionDown(ColorButton currentColorButton) {

        if (IsDoubleClick(currentColorButton.lastClickTime)) {
            currentColorButton.setBackgroundColor(
                    Color.HSVToColor(currentColorButton.originalColor));
            currentColorButton.DiscardColor();
            DisplayOnPalitreStatus(currentColorButton);
            currentColorButton.lastClickTime = SystemClock.elapsedRealtime();
            return;
        }

        currentColorButton.lastClickTime = SystemClock.elapsedRealtime();
        DisplayCurrentStatus(currentColorButton);
    }

    protected void DisableColorPickerScroll() {
        ColorPickerScroll scroll = (ColorPickerScroll) findViewById(R.id.colorPickerScroll);
        scroll.isEdited = true;
    }

    protected void CallVibrator() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    protected void CreateColorPicker() {
        float[] pixelHSV = new float[3];
        pixelHSV[0] = 0;
        pixelHSV[1] = 1;
        pixelHSV[2] = 1;
        int countButtons = 16;
        final LinearLayout colorPickerLayout = (LinearLayout) findViewById(R.id.colorPickerLayout);

        for (int i = 0; i < countButtons; i++) {
            pixelHSV[0] += 22.5;
            ColorButton button = GetNewButton(pixelHSV);
            button.setOnLongClickListener(GetOnLongClickListener());
            button.setOnTouchListener(GetOnTouchListener());
            colorPickerLayout.addView(button);
        }
        SetGradientBackGround(colorPickerLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favoriteColorsButton:
                Intent intent = new Intent(this, ColorsActivity.class);
                ArrayList<Integer> colors = new ArrayList<Integer>();
                ArrayDeque<Integer> newFavorites = favoriteColors.clone();
                while (newFavorites.size() != 0) {
                    colors.add(newFavorites.pop());
                }
                intent.putExtra("colors", colors);
                startActivity(intent);
        }
    }
}