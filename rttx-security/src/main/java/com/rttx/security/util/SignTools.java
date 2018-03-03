package com.rttx.security.util;

import com.rttx.security.support.SignException;
import javafx.util.Pair;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
            bytes = mac.doFinal(data.getBytes("UTF-8"));
        } catch (GeneralSecurityException gse) {
            throw new SignException("hmacMD5Sign error.", gse);
        } catch (UnsupportedEncodingException e) {
            throw new SignException("hmacMD5Sign error.", e);
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
            messageDigest.update(signText.getBytes("UTF-8"));
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
            messageDigest.update(signText.getBytes("UTF-8"));
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


    public static void main(String[] args) throws NoSuchAlgorithmException, SignException {
        Pair<byte[], byte[]> pair = SignTools.generateKeyBytes();
//        System.out.println(Hex.encodeHexString(pair.getKey()));
//        System.out.println(Hex.encodeHexString(pair.getValue()));
//        System.out.println(Base64.getEncoder().encodeToString(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(pair.getKey()))));
        System.out.println(Base64.getEncoder().encodeToString(pair.getKey()) + ", \n privateKey: \n" + Base64.getEncoder().encodeToString(pair.getValue()));
        String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCeFLYmro/ahNBAH9opN3ap5Ut0aSfWp5BTx8fQNpqN+mP+OnzMUeW/NHL2DAXXvSdvMa20vs0j+tib5F/i/Humrguj8B9DoFFTLhH271mSPxvWjNUBno+Co95lxXoybUczNfhM7v8ylYzo40xMy0QP1k979nUgtkaRbxPTPz3lfQIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ4Utiauj9qE0EAf2ik3dqnlS3RpJ9ankFPHx9A2mo36Y/46fMxR5b80cvYMBde9J28xrbS+zSP62JvkX+L8e6auC6PwH0OgUVMuEfbvWZI/G9aM1QGej4Kj3mXFejJtRzM1+Ezu/zKVjOjjTEzLRA/WT3v2dSC2RpFvE9M/PeV9AgMBAAECgYEAiyTD6GyCpt98xtTCnMD7LDRuJDtjG6hGe9g/4uANTqd1WJN2wgncZl7uSfDvO6R9j3x038GKo/7qpt1wIUYIN54EqljX6orritTbtw4HqDNr0j0B4AgYxJdq6yGr1oIXZOm24XgFrltOc6yuQ1De7mywHIsWQkRiQU6ag1KZR8ECQQDVDqjaY7B69ZFKFTODcFPQ31+DwFgvzYpELqUgDJTMZ0KsYmVOEBq6GJNduoI3qlqK3GtJi+WZ612uAJ+2O1IVAkEAvfFg8+gP7L/N/sNCLPZBKNZli/pk6/Z3b8MyVOUtu8uFa9yqPaDTfiHkjnaR2zCx3I4M7LJbD2nLiqH1FxRnyQJAK0KlwGo7fEv+azdw1UBqlF+2qi7U+SfX6k46Uw6a7AIPhKcaAMa+VokE6yQiqqDZb5af5jgUr7Mdu2X/B+Z1nQJATA2XNXIo1jHiiGykVZ5wbqqR1F95A2YXYp+0yntNOZF/JbYgUDbSGAH4Ivbk5e/0d4xFLo0nmt9QiXzZxtj7yQJAQ2XliBpYJk7Cl1gw1nqIVMGa2XiAUGOHK1uET3osxFJLfQi7AApLgtM5bFWMSJ3lt1xjjprQZTzH840wi7VcyQ==";
        String signed = "kPaqhW/BcmcAEpHlquExLqfxFRkk14dIkA4KI6em3EdoH4UFcakPOlLUVYnvUsDymnlTcc7Yro1ZnBYApJYZ87IbEUwgk8vlCXltKR8r/Y07KRMFxgO3yNlN7HCsI1ndC61U69RNv4imi2WgeYtg//Cw0lIzVy4E4f/5h5x/W8w=";
        String str="code000000data{\"bizNo\":\"968822000511709184\",\"limitAmount\":20000.0,\"loanPeriod\":\"6,9,12\"}desc成功msgSuccesstimestamp2018-03-02 10:19:31traceIdF1F06BA4-9E36-4C5A-82E0-52DBAF8189C3";
        System.out.println(Base64.getEncoder().encodeToString(sha256WithRsaSign(Base64.getDecoder().decode(privateKey), str)));
        System.out.println("************");
        System.out.println(sha256WithRsaVerifySign(publickey, str, signed));

    }
}
