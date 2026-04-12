package model;

public class SilentSteppingMatrix<T> extends SteppingMatrix<T> {
    public SilentSteppingMatrix(int columns, int rows) {
        super(columns, rows);
        setSoundEnabled(false);
    }
}
