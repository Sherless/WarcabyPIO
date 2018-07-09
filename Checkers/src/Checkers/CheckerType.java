package Checkers;

/**
 *
 * @author Mateusz
 */
public enum CheckerType {
    BLACK,
    RED,
    WHITE;

    @Override
    public String toString() {
        if (this == null) {
            return "null";
        } else {
            switch (this) {
                case BLACK:
                    return "Black";
                case RED:
                    return "Red";
                default:
                    return "White";
            }
        }
    }
}
