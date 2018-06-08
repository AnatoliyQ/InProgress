import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

    public Wallet() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random); //256
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


//    public static KeyPair getKeyPairFromKeyStore() throws Exception { //Reading KeyPar from KeyStore
//        File file = new File(PRIVATE_KEY_LOCATION);
//        FileInputStream ins = new FileInputStream(file);
//        KeyStore keyStore = KeyStore.getInstance("JCEKS");
//        keyStore.load(ins, "s3cr3t".toCharArray());   //Keystore password
//        KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection("s3cr3t".toCharArray()); //Key password
//        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("mykey", keyPassword);
//        java.security.cert.Certificate cert = keyStore.getCertificate("mykey");
//        PublicKey publicKey = cert.getPublicKey();
//        PrivateKey privateKey = privateKeyEntry.getPrivateKey();
//
//        return new KeyPair(publicKey, privateKey);
//    }
}
