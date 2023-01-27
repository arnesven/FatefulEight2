package sound;

public class ClientSound {
    private final boolean lowVolume;
    private byte[] bytes;

    public ClientSound(byte[] bytes, boolean lowVolume) {
        this.bytes = bytes;
        this.lowVolume = lowVolume;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public float getVolume() {
        return lowVolume?BackgroundMusic.VOLUME:SoundEffects.VOLUME;
    }
}
