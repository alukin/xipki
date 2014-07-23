/*
 * Copyright (c) 2014 Lijun Liao
 *
 * TO-BE-DEFINE
 *
 */

package org.xipki.security.api;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Set;

import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.operator.ContentVerifierProvider;

/**
 * @author Lijun Liao
 */

public interface SecurityFactory
{
    String getPkcs11Provider();
    String getPkcs11Module();

    public Set<Integer> getPkcs11ExcludeSlotIndexes();

    public Set<Integer> getPkcs11IncludeSlotIndexes();

    ConcurrentContentSigner createSigner(String type, String conf,
            X509Certificate cert, PasswordResolver passwordResolver)
    throws SignerException, PasswordResolverException;

    ConcurrentContentSigner createSigner(String type, String conf,
            X509Certificate[] certs,
            PasswordResolver passwordResolver)
    throws SignerException, PasswordResolverException;

    ContentVerifierProvider getContentVerifierProvider(PublicKey publicKey)
    throws InvalidKeyException;

    ContentVerifierProvider getContentVerifierProvider(X509Certificate cert)
    throws InvalidKeyException;

    ContentVerifierProvider getContentVerifierProvider(X509CertificateHolder cert)
    throws InvalidKeyException;

    PublicKey generatePublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo)
    throws InvalidKeyException;

    byte[] generateSelfSignedRSAKeyStore(
            BigInteger serial, String subject, String keystoreType, char[] password, String keyLabel,
            int keysize, BigInteger publicExponent)
    throws SignerException;

    boolean verifyPOPO(CertificationRequest p10Req);
}
