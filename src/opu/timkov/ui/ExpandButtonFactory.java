package opu.timkov.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import opu.timkov.R;

public class ExpandButtonFactory {

    public static Button createButton(Context context, final View childView, String name, int id, boolean isRightSide) {
        Button button = new Button(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(5, 200, 5, 0);
        button.setLayoutParams(params);
        button.setId(id);
//        button.setText(name.charAt(0) + name.substring(1).toLowerCase());
//        button.setTextColor(Color.argb(255, 255, 255, 255));
        button.setBackgroundResource(isRightSide ? R.drawable.bg : R.drawable.face);
        button.setGravity(isRightSide ? Gravity.RIGHT : Gravity.LEFT);

        return button;
    }
}
