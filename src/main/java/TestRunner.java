import Util.Address;
import Util.AddressFormatException;
import Util.StringUtil;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class TestRunner {
    public static void main(String[] args) throws AddressFormatException {
        Wallet test = new Wallet();

        PublicKey qq = test.publicKey;
        String address = Address.getAddress(qq);
        System.out.println(address);
        PublicKey fromaddress = Address.getKeyFromAddress(address);
        System.out.println(qq.equals(fromaddress));


    }
}
