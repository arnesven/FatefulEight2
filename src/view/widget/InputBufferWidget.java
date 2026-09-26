package view.widget;

import java.awt.event.KeyEvent;

public class InputBufferWidget {
    private final int inputMaxLength;
    private final String startString;
    private final StringBuffer buffer;
    private int caretPos = 0;

    public InputBufferWidget(int inputLength) {
        this.inputMaxLength = inputLength;
        startString = makeStartString();
        buffer = new StringBuffer(startString);
    }

    private String makeStartString() {
        StringBuilder bldr = new StringBuilder();
        for (int i = inputMaxLength; i > 0; --i) {
            bldr.append('þ');
        }
        return bldr.toString();
    }

    public String getText() {
        String text = buffer.toString();
        if (text.contains("þ")) {
            text = text.substring(0, text.indexOf("þ"));
        }
        return text;
    }

    public boolean hasChanged() {
        return !buffer.toString().equals(startString);
    }

    public boolean enterKeyStroke(KeyEvent keyEvent) {
        if (' ' == keyEvent.getKeyChar() || '-' == keyEvent.getKeyChar() ||
                ('a' <= keyEvent.getKeyChar() && keyEvent.getKeyChar() <= 'z') ||
                ('A' <= keyEvent.getKeyChar() && keyEvent.getKeyChar() <= 'Z')) {
            if (caretPos < inputMaxLength) {
                buffer.setCharAt(caretPos++, keyEvent.getKeyChar());
                return true;
            }
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            caretPos = Math.max(0, caretPos - 1);
            buffer.setCharAt(caretPos, 'þ');
            return true;
        }
        return false;
    }

    public String getRawText() {
        return buffer.toString();
    }

    public void setText(String s) {
        buffer.delete(0, buffer.length());
        buffer.insert(0, makeStartString());
        buffer.replace(0, s.length(), s);
        caretPos = s.length();
    }
}
