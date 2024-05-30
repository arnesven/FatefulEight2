package sound;

public enum Volume {
    LOWEST(-1.5f), LOW(-1.0f), MEDIUM(-0.5f), HIGH(0.0f), HIGHEST(0.5f);
    public float amplitude;

    Volume(float f) {
        amplitude = f;
    }
}
