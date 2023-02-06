package model.combat;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public abstract class Condition implements Serializable {
    private String name;
    private String shortName;
    public Condition(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    protected String getShortName() {
        return shortName;
    }

    public static boolean disablesCombatTurn(Set<Condition> conditions) {
        for (Condition c : conditions) {
            if (c.noCombatTurn()) {
                return true;
            }
        }
        return false;
    }

    public static String getCharacterStatus(Set<Condition> conditions) {
        StringBuilder bldr = new StringBuilder();
        for (Condition c : conditions) {
            bldr.append(c.getShortName() + ", ");
        }
        bldr.replace(bldr.length()-2, bldr.length(), "");
        return bldr.toString();
    }

    protected abstract boolean noCombatTurn();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return Objects.equals(shortName, condition.shortName);
    }
}
