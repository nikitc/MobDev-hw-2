package comnikitc.github.mobdev_hw_2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Integer> favoriteColors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);
        Intent intent = getIntent();
        favoriteColors = intent.getIntegerArrayListExtra("colors");
        SetColorFavoriteButtons();

    }

    protected void SetColorFavoriteButtons() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.favoritesLayout);

        for (int i = 0; i < favoriteColors.size(); i++) {
            Button button = new Button(this);
            button.setBackgroundColor(favoriteColors.get(i));
            button.setOnClickListener(this);
            button.setId(i + 2000);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.width = 120;
            params.height = 120;
            params.bottomMargin = 45;
            button.setLayoutParams(params);

            layout.addView(button);
        }
    }

    protected void GoToMainActivity(int id) {
        Intent intent = new Intent(this, MainActivity.class);
        Button currentButton = (Button) findViewById(id);
        ColorDrawable drawable = (ColorDrawable) currentButton.getBackground();

        intent.putExtra("chooseColor", drawable.getColor());
        intent.putExtra("favoriteColors", favoriteColors);

        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        GoToMainActivity(v.getId());
    }
}
