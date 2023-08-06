package model.horses;

public abstract class FullBloodHorse extends Horse {

    public FullBloodHorse(String name, int cost) {
        super("Full Blood", name, cost);
    }

    @Override
    public String getInfo() {
        return "Halflings and dwarves must ride together with non-halfing, non-dwarf rider.";
    }
}
