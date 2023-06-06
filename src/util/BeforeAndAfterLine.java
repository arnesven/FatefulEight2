package util;

public class BeforeAndAfterLine<T> {
    private final String label;
    private T before;
    private T after;

    public BeforeAndAfterLine(String label, T before, T after) {
        this.label = label;
        this.before = before;
        this.after = after;
    }

    public boolean isEmpty() {
        return label.equals("");
    }

    public String getLabel() {
        return label;
    }

    public T getBefore() {
        return before;
    }

    public T getAfter() {
        return after;
    }
}
