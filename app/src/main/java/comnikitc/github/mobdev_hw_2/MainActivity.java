package comnikitc.github.mobdev_hw_2;

import android.content.Context;
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
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{

    Button favoriteButton;
    ArrayList<float[]> favoriteColors = new ArrayList<>();
    final int maxFavoriteColors = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreateColorPicker();
        CreateAddFavoriteButton();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        ColorImageView chooseColorView = (ColorImageView) findViewById(R.id.chooseColorImage);
        savedInstanceState.putInt("sizeFavoritesColors", favoriteColors.size());

        for (int i = 0; i < favoriteColors.size(); i++) {
            savedInstanceState.putFloatArray("favorite" + (i + 1), favoriteColors.get(i));
        }

        savedInstanceState.putFloatArray("chooseColor", chooseColorView.getHsvColor());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int count = savedInstanceState.getInt("sizeFavoritesColors");
        for (int i = count; i >= 1; i--) {
            favoriteColors.add(savedInstanceState.getFloatArray("favorite" + i));
        }

        CreateFavoriteColors();
        DisplayCurrentStatus(savedInstanceState.getFloatArray("chooseColor"));
    }


    protected void CreateAddFavoriteButton() {
        favoriteButton = (Button) findViewById(R.id.favoritebutton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorImageView chooseColor = (ColorImageView) findViewById(R.id.chooseColorImage);
                if (favoriteColors.size() == maxFavoriteColors) {
                    favoriteColors.remove(0);
                }
                favoriteColors.add(chooseColor.getHsvColor());

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Color add favorite", Toast.LENGTH_SHORT);
                toast.show();

                CreateFavoriteColors();
            }
        });
    }

    protected void CreateFavoriteColors() {
        LinearLayout favoriteColorsLayout = (LinearLayout) findViewById(R.id.favoriteColors);
        favoriteColorsLayout.removeAllViews();
        for (int i = 0; i < favoriteColors.size(); i++) {
            FavoriteButton button = new FavoriteButton(this, favoriteColors.get(i));
            button.setBackgroundColor(Color.HSVToColor(favoriteColors.get(i)));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FavoriteButton favoriteButton = (FavoriteButton) view;

                    DisplayCurrentStatus(favoriteButton.hsvColor);
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.width = 80;
            params.height = 80;
            params.leftMargin = 20;
            params.rightMargin = 20;
            button.setLayoutParams(params);
            favoriteColorsLayout.addView(button);
        }
    }

    protected void DisplayCurrentStatus(float[] hsvColor) {

        ColorImageView chooseColorView = (ColorImageView) findViewById(R.id.chooseColorImage);
        chooseColorView.setHsvColor(hsvColor);

        TextView rgbText = (TextView) findViewById(R.id.chooseValueRGB);
        TextView hsvText = (TextView) findViewById(R.id.chooseValueHSV);

        chooseColorView.setBackgroundColor(Color.HSVToColor(hsvColor));

        String newRGBColor = ColorButton.GetStringRGBColor(hsvColor);
        String newHSVColor = ColorButton.GetStringHSVColor(hsvColor);

        rgbText.setText(newRGBColor);
        hsvText.setText(newHSVColor);
    }


    protected void DisplayOnPalitreStatus(ColorButton currentColorButton) {

        ImageView chooseColorView = (ImageView) findViewById(R.id.currentColorImage);

        TextView rgbText = (TextView) findViewById(R.id.currentValueRGB);
        TextView hsvText = (TextView) findViewById(R.id.currentValueHSV);


        chooseColorView.setBackgroundColor(
                Color.HSVToColor(currentColorButton.getCurrentColor()));

        String newRGBColor = ColorButton.GetStringRGBColor(currentColorButton.getCurrentColor());
        String newHSVColor = ColorButton.GetStringHSVColor(currentColorButton.getCurrentColor());

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
                        if (scroll.getIsCanMove()) {
                            break;
                        }
                        HandleActionMove(currentColorButton, x, y, event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!scroll.getIsCanMove()) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Editing is finished", Toast.LENGTH_SHORT);
                            toast.show();
                            scroll.setIsCanMove(true);
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
            if (currentColorButton.getCurrentColor()[0] == currentColorButton.getRightBorder()) {
                CallVibrator();
            }
            if (currentColorButton.getCurrentColor()[0] < currentColorButton.getRightBorder()) {
                currentColorButton.getCurrentColor()[0] += 0.25;
                currentColorButton.setBackgroundColor(
                        Color.HSVToColor(currentColorButton.getCurrentColor()));
                DisplayOnPalitreStatus(currentColorButton);
            }
            return;
        }

        if (oldX > x && Math.abs(oldY - y) < 1) {
            if (currentColorButton.getCurrentColor()[0] == currentColorButton.getLeftBorder()) {
                CallVibrator();
            }
            if (currentColorButton.getCurrentColor()[0] > currentColorButton.getLeftBorder()) {
                currentColorButton.getCurrentColor()[0] -= 0.25;
                currentColorButton.setBackgroundColor(
                        Color.HSVToColor(currentColorButton.getCurrentColor()));
                DisplayOnPalitreStatus(currentColorButton);
            }
            return;
        }

        if (oldY < y && Math.abs(oldX - x) < 1) {
            if (currentColorButton.getCurrentColor()[2] <= currentColorButton.downBorderColor) {
                CallVibrator();
            }
            if (currentColorButton.getCurrentColor()[2] > currentColorButton.downBorderColor) {
                currentColorButton.getCurrentColor()[2] -= 0.05;
                currentColorButton.setBackgroundColor(
                        Color.HSVToColor(currentColorButton.getCurrentColor()));
                DisplayOnPalitreStatus(currentColorButton);
            }
            return;
        }
        if (oldY > y && Math.abs(oldX - x) < 1) {
            if (currentColorButton.getCurrentColor()[2] == currentColorButton.upBorderColor) {
                CallVibrator();
            }
            if (currentColorButton.getCurrentColor()[2] < currentColorButton.upBorderColor) {
                currentColorButton.getCurrentColor()[2] += 0.05;
                currentColorButton.setBackgroundColor(
                        Color.HSVToColor(currentColorButton.getCurrentColor()));
                DisplayOnPalitreStatus(currentColorButton);
            }
        }
    }

    protected void HandleActionDown(ColorButton currentColorButton) {

        if (IsDoubleClick(currentColorButton.lastClickTime)) {
            currentColorButton.setBackgroundColor(
                    Color.HSVToColor(currentColorButton.getOriginalColor()));
            currentColorButton.DiscardColor();
            DisplayOnPalitreStatus(currentColorButton);
            currentColorButton.lastClickTime = SystemClock.elapsedRealtime();
            return;
        }

        currentColorButton.lastClickTime = SystemClock.elapsedRealtime();
        DisplayCurrentStatus(currentColorButton.getCurrentColor());
    }

    protected void DisableColorPickerScroll() {
        ColorPickerScroll scroll = (ColorPickerScroll) findViewById(R.id.colorPickerScroll);
        scroll.setIsCanMove(false);
    }

    protected void CallVibrator() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    protected void CreateColorPicker() {
        float[] pixelHSV = new float[3];
        pixelHSV[0] = 12.25f;
        pixelHSV[1] = 1;
        pixelHSV[2] = 1;
        int countButtons = 16;
        final LinearLayout colorPickerLayout = (LinearLayout) findViewById(R.id.colorPickerLayout);

        for (int i = 0; i < countButtons; i++) {
            ColorButton button = new ColorButton(this, pixelHSV.clone());
            button.setOnLongClickListener(GetOnLongClickListener());
            button.setOnTouchListener(GetOnTouchListener());
            colorPickerLayout.addView(button);
            pixelHSV[0] += 22.5;
        }
        GradientColorPicker.SetGradientBackGround(colorPickerLayout);
    }

}