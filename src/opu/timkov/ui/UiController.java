package opu.timkov.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import opu.timkov.R;
import opu.timkov.core.CameraSwitcher;
import opu.timkov.effects.manager.EffectName;
import opu.timkov.effects.manager.EffectType;
import opu.timkov.effects.manager.EffectsChanger;

public class UiController {

    private EffectsChanger effectsChanger;

    public UiController(EffectsChanger effChanger, RecordButtonListener recordButtonListener,
                        CameraSwitcher cameraSwitcher, ActivityProxy proxy) {
        effectsChanger = effChanger;
        initRecordButton((ImageButton) proxy.findViewById(R.id.snapbutton), recordButtonListener);
        initCameraSwitchButton((ImageButton) proxy.findViewById(R.id.switchcambutton), cameraSwitcher);
        initMuteButton((ImageButton) proxy.findViewById(R.id.mutebutton));

        int marginLeft = 5, marginTop = 0, marginRight = 5, marginBottom = 0;

        ScrollView bgEffectButtonsScrollView = new ScrollView(proxy.getContext());
        bgEffectButtonsScrollView.setLayoutParams(new LinearLayout.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.MATCH_PARENT, (float) 1));
        bgEffectButtonsScrollView.setFillViewport(true);

        LinearLayout bgEffectButtonsLayout = new LinearLayout(proxy.getContext());
        bgEffectButtonsLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams bgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        bgParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        bgEffectButtonsLayout.setLayoutParams(bgParams);
        bgEffectButtonsLayout.setOrientation(LinearLayout.VERTICAL);
        bgEffectButtonsLayout.setGravity(Gravity.RIGHT);
        bgEffectButtonsScrollView.addView(bgEffectButtonsLayout);

        for (EffectName effectName: EffectName.values()) {
            if (!effectName.availableForType(EffectType.BG)) { continue; }
            Button button = createButton(effectName.name(),
                    EffectType.BG.getValue() * 10 + effectName.getValue(), proxy.getContext());
            button.setOnClickListener(createButtonListener(effectName, EffectType.BG));
            setEnabledViewAlpha(button, false);
            bgEffectButtonsLayout.addView(button);
        }


        ScrollView faceEffectButtonsScrollView = new ScrollView(proxy.getContext());
        faceEffectButtonsScrollView.setLayoutParams(new LinearLayout.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.MATCH_PARENT, (float) 1));
        faceEffectButtonsScrollView.setFillViewport(true);

        final LinearLayout faceEffectButtonsLayout = new LinearLayout(proxy.getContext());
        faceEffectButtonsLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams faceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        faceParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        faceEffectButtonsLayout.setLayoutParams(faceParams);
        faceEffectButtonsLayout.setOrientation(LinearLayout.VERTICAL);

        faceEffectButtonsScrollView.addView(faceEffectButtonsLayout);

        for (EffectName effectName: EffectName.values()) {
            if (!effectName.availableForType(EffectType.FACE)) { continue; }
            Button button = createButton(effectName.name(),
                    EffectType.FACE.getValue() * 10 + effectName.getValue(), proxy.getContext());
            button.setOnClickListener(createButtonListener(effectName, EffectType.FACE));
            setEnabledViewAlpha(button, false);
            faceEffectButtonsLayout.addView(button);
        }

        initEffectGroupsButtons((ImageButton) proxy.findViewById(R.id.facebutton), (ImageButton) proxy.findViewById(R.id.bgbutton),
                faceEffectButtonsScrollView, bgEffectButtonsScrollView);

        LinearLayout contentLayout = new LinearLayout(proxy.getContext());
        LinearLayout.LayoutParams contentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, (float) 1);
        contentLayoutParams.setMargins(marginLeft, marginTop + 200, marginRight, marginBottom);
        contentLayout.setLayoutParams(contentLayoutParams);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);

        contentLayout.addView(faceEffectButtonsScrollView);
        contentLayout.addView(bgEffectButtonsScrollView);

        ((LinearLayout)proxy.findViewById(R.id.ui_layout)).addView(contentLayout);
    }

    private Button createButton(String name, int id, Context context) {
        Button button = new Button(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 0);
        button.setLayoutParams(params);
        button.setId(id);
        String displayName = name.charAt(0) + name.substring(1).toLowerCase().replace("_", " ");
        button.setText(displayName);
        button.setTextColor(Color.argb(255, 255, 255, 255));
        button.setBackgroundColor(Color.alpha(0));

        return button;
    }

    private View.OnClickListener createButtonListener(final EffectName name, final EffectType type) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isEnabled = effectsChanger.changeEffectEnabled(name, type);
                setEnabledViewAlpha(view, isEnabled);
            }
        };
    }

    private void setEnabledViewAlpha(View view, boolean isEnabled) {
        view.setAlpha(isEnabled ? 0.95f : 0.45f);
    }

    private void initRecordButton(final ImageButton recordButton, final RecordButtonListener recordButtonListener) {
        recordButton.setOnClickListener(new View.OnClickListener() {
            private boolean isRecording = false;
            @Override
            public void onClick(View view) {
                recordButtonListener.onRecordButtonPress();
                isRecording = !isRecording;
                if (isRecording) {
                    recordButton.setImageResource(R.drawable.stop_record);
                } else {
                    recordButton.setImageResource(R.drawable.record);
                }
            }
        });
    }

    private void initMuteButton(final ImageButton muteButton) {
        muteButton.setOnClickListener(new View.OnClickListener() {
            private boolean isMuted = false;
            @Override
            public void onClick(View view) {
                isMuted = !isMuted;
                if (isMuted) {
                    muteButton.setBackgroundResource(R.drawable.mute);
                } else {
                    muteButton.setBackgroundResource(R.drawable.unmute);
                }
            }
        });
    }

    private void initCameraSwitchButton(final ImageButton cameraButton, final CameraSwitcher cameraSwitcher) {
        cameraButton.setOnClickListener(new View.OnClickListener() {
            private boolean isRotated = false;
            @Override
            public void onClick(View view) {
                isRotated = !isRotated;
                if (isRotated) {
                    cameraButton.setBackgroundResource(R.drawable.switch_camera_rt);
                } else {
                    cameraButton.setBackgroundResource(R.drawable.switch_camera);
                }
                cameraSwitcher.switchCamera();
            }
        });
    }

    private void initEffectGroupsButtons(final ImageButton faceButton, final ImageButton bgButton,
                                         final View faceEffects, final View bgEffects) {
        faceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExpanded = faceEffects.getVisibility() == View.VISIBLE;
                faceEffects.setVisibility(isExpanded ? View.INVISIBLE : View.VISIBLE);
                view.setAlpha(isExpanded ? 0.45f : 0.9f);
            }
        });
        bgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExpanded = bgEffects.getVisibility() == View.VISIBLE;
                bgEffects.setVisibility(isExpanded ? View.INVISIBLE : View.VISIBLE);
                view.setAlpha(isExpanded ? 0.45f : 0.9f);
            }
        });
    }

}
