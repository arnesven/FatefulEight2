package sound;

public class ClientSound {
    private byte[] bytes;
    private float volume;

    public ClientSound(byte[] bytes, float v) {
        this.bytes = bytes;
        volume = v;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
