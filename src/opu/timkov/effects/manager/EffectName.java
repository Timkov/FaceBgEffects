package opu.timkov.effects.manager;

import java.util.ArrayList;


public enum EffectName {
    BRIGHTNESS(0),
    DRAW(1),
    BLUR(2),
    OIL(3),
    SEPIA(4),
    BLACK_AND_WHITE(5),
    SUMMER(6),
    GREEN(7),
    SHARPNESS(8),
    EDGES(9),
    PIXELIZATION(10),
    CARTOON(11),
    NATURE(12),
    CITY(13),
    ONPU(14);

    private final int value;

    private static final ArrayList<EffectName> availableForFace;
    private static final ArrayList<EffectName> availableForBg;

    static {
        availableForFace = new ArrayList<>();
        availableForFace.add(BRIGHTNESS);
        availableForFace.add(SEPIA);
        availableForFace.add(BLACK_AND_WHITE);
        availableForFace.add(SUMMER);
        availableForFace.add(GREEN);
        availableForFace.add(SHARPNESS);
        availableForFace.add(DRAW);
        availableForFace.add(OIL);
        availableForFace.add(CARTOON);

        availableForBg = new ArrayList<>();
        availableForBg.add(ONPU);
        availableForBg.add(CITY);
        availableForBg.add(NATURE);
        availableForBg.add(SEPIA);
        availableForBg.add(BLACK_AND_WHITE);
        availableForBg.add(SUMMER);
        availableForBg.add(GREEN);
        availableForBg.add(EDGES);
        availableForBg.add(DRAW);
        availableForBg.add(OIL);
        availableForBg.add(PIXELIZATION);
        availableForBg.add(BLUR);
        availableForBg.add(CARTOON);
    }

    private EffectName(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean availableForType(EffectType type) {
        switch (type) {
            case FACE:
                return EffectName.availableForFace.contains(this);
            case BG:
                return EffectName.availableForBg.contains(this);
            default:
                return false;
        }
    }
}
