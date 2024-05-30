package sound;

public class ClientSound {
    private final float lowVolume;
    private byte[] bytes;

    public ClientSound(byte[] bytes, float volume) {
        this.bytes = bytes;
        this.lowVolume = volume;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public float getVolume() {
        return lowVolume;
    }
}
