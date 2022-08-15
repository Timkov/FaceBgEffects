package opu.timkov.effects.manager;

public enum EffectType {
    FACE(1),
    BG(2);

    private final int value;

    private EffectType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
