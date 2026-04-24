package org.voting.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class BlindSignatureService {
    private final BigInteger n;
    private final BigInteger e;
    private final BigInteger d;
    private final SecureRandom random = new SecureRandom();

    public BlindSignatureService() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        
        RSAPublicKey pub = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey priv = (RSAPrivateKey) kp.getPrivate();
        
        this.n = pub.getModulus();
        this.e = pub.getPublicExponent();
        this.d = priv.getPrivateExponent();
    }

    public BlindedMessage blind(BigInteger message) {
        BigInteger r;
        do {
            r = new BigInteger(n.bitLength(), random);
        } while (r.compareTo(n) >= 0 || !r.gcd(n).equals(BigInteger.ONE));

        BigInteger blinded = message.multiply(r.modPow(e, n)).mod(n);
        return new BlindedMessage(blinded, r);
    }

    public BigInteger sign(BigInteger blindedMessage) {
        return blindedMessage.modPow(d, n);
    }

    public BigInteger unblind(BigInteger blindedSignature, BigInteger r) {
        BigInteger rInv = r.modInverse(n);
        return blindedSignature.multiply(rInv).mod(n);
    }

    public boolean verify(BigInteger message, BigInteger signature) {
        return signature.modPow(e, n).equals(message);
    }

    public static class BlindedMessage {
        public final BigInteger blinded;
        public final BigInteger r;

        public BlindedMessage(BigInteger blinded, BigInteger r) {
            this.blinded = blinded;
            this.r = r;
        }
    }
}
