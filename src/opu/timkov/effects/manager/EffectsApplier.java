package opu.timkov.effects.manager;

import org.opencv.core.Mat;

public interface EffectsApplier {
    Mat applyEffects(Mat src, EffectType type);
}
