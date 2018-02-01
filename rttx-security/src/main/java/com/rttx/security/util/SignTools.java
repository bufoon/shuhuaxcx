package com.rttx.security.util;

import com.rttx.security.support.SignException;
import javafx.util.Pair;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @Author: bufoon
 * @Email: song.anling@zyxr.com
 * @Datetime: Created In 2018/1/29 10:39
 * @Desc: as follows.
 */
public class SignTools {

    public static final String KEY_ALGORITHM = "RSA";
    public static final int KEY_SIZE = 1024;
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String ENCODE_ALGORITHM = "SHA-256";

    /**
     * 生成公钥和私钥
     * key 公钥， value 私钥
     * @return Pair<公钥[], 私钥[]>
     * @throws NoSuchAlgorithmException
     */
    public static Pair<byte[], byte[]> generateKeyBytes() throws NoSuchAlgorithmException {
        Pair<byte[], byte[]> keyBytePair = null;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        keyBytePair = new Pair<>(publicKey.getEncoded(), privateKey.getEncoded());
        return keyBytePair;
    }

    /**
     * 公钥对象
     *
     * @param keyBytes
     * @return
     */
    public static PublicKey genPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
        return publicKey;
    }

    /**
     * 私钥对象
     *
     * @param keyBytes
     * @return
     */
    public static PrivateKey genPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                keyBytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = factory
                .generatePrivate(pkcs8EncodedKeySpec);
        return privateKey;
    }

    /**
     * HmacMD5签名
     * @param data
     * @param secret
     * @return
     * @throws IOException
     */
    public static byte[] hmacMD5Sign(String data, String secret) throws SignException {
        byte[] bytes;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(Charset.defaultCharset()), "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(Charset.defaultCharset()));
        } catch (GeneralSecurityException gse) {
            throw new SignException("hmacMD5Sign error.", gse);
        }
        return bytes;
    }


    /**
     * 签名
     *
     * @param privateKey
     *            私钥
     * @param signText
     *            明文
     * @return
     */
    public static byte[] sha256WithRsaSign(byte[] privateKey, String signText) throws SignException {
        byte[] signed;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ENCODE_ALGORITHM);
            messageDigest.update(signText.getBytes());
            byte[] outputDigest_sign = messageDigest.digest();
            Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            Sign.initSign(genPrivateKey(privateKey));
            Sign.update(outputDigest_sign);
            signed = Sign.sign();
        } catch (Exception e) {
            throw new SignException("sha256WithRsaSign error.", e);
        }
        return signed;
    }

    /**
     * 验签
     *
     * @param publicKey
     *            公钥
     * @param signText
     *            明文
     * @param signed
     *            签名
     */
    public static boolean sha256WithRsaVerifySign(String publicKey, String signText, String signed) throws SignException {

        MessageDigest messageDigest;
        boolean SignedSuccess=false;
        try {
            messageDigest = MessageDigest.getInstance(ENCODE_ALGORITHM);
            messageDigest.update(signText.getBytes());
            byte[] outputDigest_verify = messageDigest.digest();
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(genPublicKey(Base64.getDecoder().decode(publicKey)));
            verifySign.update(outputDigest_verify);
            SignedSuccess = verifySign.verify(Base64.getDecoder().decode(signed));

        } catch (Exception e) {
            throw new SignException("sha256WithRsaVerifySign error.", e);
        }
        return SignedSuccess;
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {
        Pair<byte[], byte[]> pair = SignTools.generateKeyBytes();
        System.out.println(Hex.encodeHexString(pair.getKey()));
        System.out.println(Hex.encodeHexString(pair.getValue()));
        System.out.println(Base64.getEncoder().encodeToString(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(pair.getKey()))));
        System.out.println(Base64.getEncoder().encodeToString(pair.getKey()) + ", \n privateKey: \n" + Base64.getEncoder().encodeToString(pair.getValue()));
    }
}
