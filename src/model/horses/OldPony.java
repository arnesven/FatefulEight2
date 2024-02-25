package model.horses;

public class OldPony extends Pony {

    @Override
    public String getName() {
        return "Old Pony";
    }

    @Override
    public int getCost() {
        return 25;
    }

    @Override
    public Horse copy() {
        return new OldPony();
    }
}
