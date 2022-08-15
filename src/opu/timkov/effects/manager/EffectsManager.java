package opu.timkov.effects.manager;

import android.content.Context;

import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.HashMap;

import opu.timkov.effects.filters.BlackAndWhite;
import opu.timkov.effects.filters.Cartoon;
import opu.timkov.effects.filters.Green;
import opu.timkov.effects.filters.Blur;
import opu.timkov.effects.filters.Brightness;
import opu.timkov.effects.Effect;
import opu.timkov.effects.filters.CityBgEffect;
import opu.timkov.effects.filters.Draw;
import opu.timkov.effects.filters.Edges;
import opu.timkov.effects.filters.Grey;
import opu.timkov.effects.filters.NatureBgEffect;
import opu.timkov.effects.filters.Oil;
import opu.timkov.effects.filters.OnpuBgEffect;
import opu.timkov.effects.filters.Pixelization;
import opu.timkov.effects.filters.Sepia;
import opu.timkov.effects.filters.Sharpness;



public class EffectsManager implements EffectsChanger, EffectsApplier {

    private final int MAX_EFFECTS = 2;

    private HashMap<EffectName, Effect> effects;

    private ArrayList<EffectName> faceEffectsNamesList;

    private ArrayList<EffectName> bgEffectsNamesList;

    public EffectsManager(Size imageSize, Context context) {
        faceEffectsNamesList = new ArrayList<>();
        bgEffectsNamesList = new ArrayList<>();
        effects = new HashMap<>();

        effects.put(EffectName.BRIGHTNESS, new Brightness());
        effects.put(EffectName.DRAW, new Draw());
        effects.put(EffectName.OIL, new Oil());
        effects.put(EffectName.BLUR, new Blur());
        effects.put(EffectName.SEPIA, new Sepia());
        effects.put(EffectName.BLACK_AND_WHITE, new BlackAndWhite());
        effects.put(EffectName.SUMMER, new Grey());
        effects.put(EffectName.GREEN, new Green());
        effects.put(EffectName.SHARPNESS, new Sharpness());
        effects.put(EffectName.EDGES, new Edges());
        effects.put(EffectName.PIXELIZATION, new Pixelization());
        effects.put(EffectName.CARTOON, new Cartoon());
        effects.put(EffectName.CITY, new CityBgEffect());
        effects.put(EffectName.NATURE, new NatureBgEffect());
        effects.put(EffectName.ONPU, new OnpuBgEffect());

        for (Effect effect: effects.values()) {
            effect.init(imageSize, context);
        }
    }

    public Mat applyEffects(Mat src, EffectType type) {
        ArrayList<EffectName> namesList = getNamesList(type);
        Effect curEffect;
        for (EffectName name: namesList) {
            curEffect = effects.get(name);
            src = curEffect.process(src);
        }
        return src;
    }


    public boolean changeEffectEnabled(EffectName name, EffectType type) {
        ArrayList<EffectName> list = getNamesList(type);
        if (list.contains(name)) {
            list.remove(name);
            return false;
        } else if (list.size() > MAX_EFFECTS) {
            return false;
        } else {
            list.add(name);
            return true;
        }
    }

    private ArrayList<EffectName> getNamesList(EffectType type) {
        ArrayList<EffectName> list = null;
        switch (type) {
            case FACE:
                list = faceEffectsNamesList;
                break;
            case BG:
                list = bgEffectsNamesList;
                break;
        }
        return list;
    }

}
