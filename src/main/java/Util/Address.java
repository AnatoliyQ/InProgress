package Util;


import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class Address {
    public static String getAddress (PublicKey publicKey){
        byte[] keyBytes = publicKey.getEncoded();
        return Base58.encodeAddress(keyBytes);
    }

    public static PublicKey getKeyFromAddress (String address) throws AddressFormatException {
        PublicKey publicKey = null;
        byte[] keyArr = Base58.decodeAddress(address);
        try {
            publicKey =  StringUtil.publicKeyFromByte(keyArr);
        } catch (InvalidKeySpecException | NoSuchProviderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return publicKey;

    }
}
