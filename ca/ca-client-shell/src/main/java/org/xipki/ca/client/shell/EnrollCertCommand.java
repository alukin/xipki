/*
 *
 * This file is part of the XiPKI project.
 * Copyright (c) 2014 Lijun Liao
 * Author: Lijun Liao
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * THE AUTHOR LIJUN LIAO. LIJUN LIAO DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the XiPKI software without
 * disclosing the source code of your own applications.
 *
 * For more information, please contact Lijun Liao at this
 * address: lijun.liao@gmail.com
 */

package org.xipki.ca.client.shell;

import java.io.File;
import java.security.cert.X509Certificate;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.xipki.ca.common.CertificateOrError;
import org.xipki.ca.common.EnrollCertResult;
import org.xipki.security.common.IoCertUtil;

/**
 * @author Lijun Liao
 */

@Command(scope = "caclient", name = "enroll", description="Enroll certificate")
public class EnrollCertCommand extends ClientCommand
{

    @Option(name = "-p10",
            required = true, description = "Required. PKCS#10 request file")
    protected String p10File;

    @Option(name = "-profile",
            required = true, description = "Required. Certificate profile")
    protected String profile;

    @Option(name = "-out",
            required = false, description = "Where to save the certificate")
    protected String outputFile;

    @Option(name = "-user",
            required = false, description = "Username")
    protected String user;

    @Override
    protected Object doExecute()
    throws Exception
    {
        CertificationRequest p10Req = CertificationRequest.getInstance(
                IoCertUtil.read(p10File));
        EnrollCertResult result = raWorker.requestCert(p10Req, profile, null, user);

        X509Certificate cert = null;
        if(result != null)
        {
            String id = result.getAllIds().iterator().next();
            CertificateOrError certOrError = result.getCertificateOrError(id);
            cert = (X509Certificate) certOrError.getCertificate();
        }

        if(cert == null)
        {
            err("No certificate received from the server");
            return null;
        }

        if(outputFile == null)
        {
            outputFile = p10File.substring(0, p10File.length() - ".p10".length()) + ".der";
        }

        File certFile = new File(outputFile);
        saveVerbose("Certificate saved to file", certFile, cert.getEncoded());

        return null;
    }

}
