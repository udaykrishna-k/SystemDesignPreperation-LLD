package exceptions;

public class NoPieceFoundAtSourceCell extends Exception {
    public NoPieceFoundAtSourceCell(String message) {
        super(message);
    }
}
