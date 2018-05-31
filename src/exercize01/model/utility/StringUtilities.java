package exercize01.model.utility;

public final class StringUtilities {

    public static boolean isStringNumeric(String str) {
        try {
            Integer.parseInt(str);
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
