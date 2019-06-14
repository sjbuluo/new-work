package com.sun.health.newwork.interview.java.network.ssl;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;

/**
 * Created by 华硕 on 2019-06-14.
 */
public class SecureOrderTaker {

    public final static int PORT = 9362;
    public final static String ALGORITHM = "SSL";

    public static void main(String[] args) {
        try {
            SSLContext context = SSLContext.getInstance(ALGORITHM);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            KeyStore ks = KeyStore.getInstance("JKS");
            char[] password = System.console().readPassword();
            ks.load(new FileInputStream("jnp4e.keys") , password);
            kmf.init(ks, password);
            context.init(kmf.getKeyManagers(), null, null);
            Arrays.fill(password, '0');
            SSLServerSocketFactory factory = context.getServerSocketFactory();
            SSLServerSocket server = (SSLServerSocket) factory.createServerSocket(PORT);
            String[] supportedCipherSuites = server.getSupportedCipherSuites();
            String[] anonChipherSuitesSuppored = new String[supportedCipherSuites.length];
            int numAnonCipherSupported = 0;
            for (int i = 0; i < supportedCipherSuites.length; i++) {
                if (supportedCipherSuites[i].indexOf("_anon_") > 0) {
                    anonChipherSuitesSuppored[numAnonCipherSupported++] = supportedCipherSuites[i];
                }
            }
            String[] oldEnabled = server.getEnabledCipherSuites();
            String[] newEnabled = new String[oldEnabled.length + numAnonCipherSupported];
            System.arraycopy(oldEnabled, 0, newEnabled, 0, oldEnabled.length);;
            System.arraycopy(anonChipherSuitesSuppored, 0, newEnabled, oldEnabled.length, numAnonCipherSupported);
            server.setEnabledCipherSuites(newEnabled);
            while (true) {
                try (Socket theConnection = server.accept(); InputStream in = theConnection.getInputStream()) {
                    int c;
                    while ((c = in.read()) != -1) {
                        System.out.write(c);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

    }
}
