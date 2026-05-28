package rulebook;

import java.io.BufferedWriter;
import java.io.IOException;

public abstract class RulebookChapter {

    private final String name;

    public RulebookChapter(String name) {
        this.name = name;
    }

    public abstract void generate(BufferedWriter writer) throws IOException;

    public String getName() {
        return name;
    }
}
