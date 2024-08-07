package sound;

public enum Volume {
    LOW_BGM(-5.0f),
    MEDIUM_BGM(2.0f),
    HIGH_BGM(3.5f),

    LOWEST(-1.5f),
    LOW(-1.0f),
    MEDIUM(-0.5f), 
    HIGH(0.0f),
    HIGHEST(0.5f);
    public float amplitude;

    Volume(float f) {
        amplitude = f;
    }
}
