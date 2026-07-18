package exceptions;

public class CannotCaptureYourOwnPiece extends Exception{
    public CannotCaptureYourOwnPiece(String message){
        super(message);
    }
}
