package pl.marek.knx.utils;

public class DataRepresentation {
    
    public static String byteArrayToHexString(byte[] b) {
        /*
         * Convert byte array to String representing
         * byte data in hexadecimal format
         * 
         */
        if (b == null){
            return null;
        }
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
            if (i < b.length-1){
                sb.append(" ");
            }
        }
        return sb.toString().toUpperCase();
    }
    public static String byteArrayToBinaryString(byte[] b) {
        /*
         * Convert byte array to String representing
         * byte data in binary format with
         * leading zeros
         */
        if (b == null){
            return null;
        }
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            String leadingZeros = "";
            String binary = Integer.toBinaryString(v);
            if (binary.length()<8){
                int addLeadingZeros = 8 - binary.length();
                StringBuilder leadZeros = new StringBuilder(addLeadingZeros);
                for (int j = 0; j < addLeadingZeros; j++){
                    leadZeros.append("0");
                }
                leadingZeros = leadZeros.toString();
            }
            
            sb.append(leadingZeros);
            sb.append(binary);
            if (i < b.length-1){
                sb.append(" ");
            }
        }
        return sb.toString().toUpperCase();
    }
    
    public static String byteArrayToDecimalString(byte[] b) {
        /*
         * Convert byte array to String representing
         * byte data in decimal format
         * 
         */
        if (b == null){
            return null;
        }
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            sb.append(Integer.toString(v));
            if (i < b.length-1){
                sb.append(" ");
            }
        }
        return sb.toString().toUpperCase();
    }
    
    public static byte[] hexStringToByteArray(String value){
    	/*
    	 *  Convert String representation of hexadimal value
    	 *  to byte array 
    	 * 
    	 * */
    	String hexValue = value.replace(" ", "").trim();
    	int length = hexValue.length();
    	byte[] bytes = new byte[length/2];
    	for (int i = 0; i < length; i += 2) {
            bytes[i/2] = (byte) ((Character.digit(hexValue.charAt(i), 16) << 4)
                                 + Character.digit(hexValue.charAt(i+1), 16));
        }
        return bytes;
    }
}
