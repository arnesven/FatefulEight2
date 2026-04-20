package sound;

public enum Volume {
    VOL_1(-5.0f),
    VOL_2(-3.5f),
    VOL_3(-1.0f),
    VOL_4(-1.5f),
    VOL_5(-0.5f),
    VOL_6(0.0f),
    VOL_7(0.5f),
    VOL_8(2.0f),
    VOL_9(3.5f),
    VOL_10(5.0f);

    public float amplitude;

    Volume(float f) {
        amplitude = f;
    }
}
