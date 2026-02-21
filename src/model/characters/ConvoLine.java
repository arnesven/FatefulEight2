package model.characters;

import model.characters.appearance.FacialExpression;

public class ConvoLine {
    private final GameCharacter person;
    private final String line;
    private final FacialExpression expression;

    public ConvoLine(GameCharacter person, String line, FacialExpression expression) {
        this.person = person;
        this.line = line;
        this.expression = expression;
    }

    public ConvoLine(GameCharacter person, String line) {
        this(person, line, FacialExpression.none);
    }

    public GameCharacter getPerson() {
        return person;
    }

    public String getLine() {
        return line;
    }

    public FacialExpression getExpression() {
        return expression;
    }
}
