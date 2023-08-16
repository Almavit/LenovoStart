package by.matveev.lenovostart.lib;


import android.os.AsyncTask;
import android.os.Environment;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

//import static android.content.Context.MODE_PRIVATE;

public class FTPModel {

    public FTPClient mFTPClient = null;
  //  public FTPClient ftp = null;


    public boolean connect(String host, String username, String password, int port) {

        try {
            return new asyncConnexion(host, username, password, port).execute().get();
        } catch (Exception e) {
            return false;
        }

    }

    public class asyncConnexion extends AsyncTask<Void, Void, Boolean> {
        private String host;
        private String username;
        private String password;
        private int port;

        asyncConnexion(String host, String username, String password, int port) {
            this.host = host;
            this.username = username;
            this.password = password;
            this.port = port;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                mFTPClient = new FTPClient();
                //connect to the host
                mFTPClient.connect(host, port);
             //   Integer iCode = mFTPClient.getReplyCode();//230	Пользователь идентифицирован, продолжайте||| 530	Вход не выполнен! Требуется авторизация (not logged in)|||
                //220	Служба готова для нового пользователя.
                boolean status = mFTPClient.login(username, password);
           //     iCode = mFTPClient.getReplyCode();//230	Пользователь идентифицирован, продолжайте||| 530	Вход не выполнен! Требуется авторизация (not logged in)
             //  Boolean iconnect = mFTPClient.isConnected();
             //   Integer iFtpUser = mFTPClient.user(username);//331 - FTP пользователь есть;

               // Integer iStat = mFTPClient.pass(password);//230	Пользователь идентифицирован, продолжайте||| 530	Вход не выполнен! Требуется авторизация (not logged in)
                // String sConnect = mFTPClient.getStatus().toString();
                if (!status) {
                    return status;
                }
                mFTPClient.setFileType(FTP.ASCII_FILE_TYPE);

                FileInputStream fInput = new FileInputStream(Environment.getExternalStorageDirectory() + "/Documents/Dat1.txt");
                String fs = "Dat1.txt";
                mFTPClient.storeFile(fs, fInput);
                mFTPClient.logout();


                mFTPClient.enterLocalPassiveMode();
                return status;
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
////////////////////////////////////////////////////////

    public Boolean downloadAndSaveFile(String host, int port, String username, String password, String filename, File localFile) {
        try {
            return new asyncConnexionFTP(host, port, username, password, filename, localFile).execute().get();
        } catch (Exception e) {
            return false;
        }
    }

    public class asyncConnexionFTP extends AsyncTask<Void, Void, Boolean> {
        private String host;
        private int port;
        private String username;
        private String password;
        private String filename;
        private File localFile;

        asyncConnexionFTP(String host, int port, String username, String password, String filename, File localFile) {
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
            this.filename = filename;
            this.localFile = localFile;

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean success = false;
            try {
                mFTPClient = new FTPClient();
                //connect to the host
                mFTPClient.connect(host, port);
                boolean status = mFTPClient.login(username, password);
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);//  /ASCII_FILE_TYPE
                mFTPClient.enterLocalPassiveMode();
                OutputStream outputStream = null;
                try {
                    outputStream = new BufferedOutputStream(new FileOutputStream(localFile));
                    success = mFTPClient.retrieveFile(filename, outputStream);
                    if (!success) {
                        String sssd = mFTPClient.getReplyString();
                        throw new Exception(mFTPClient.getReplyString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (outputStream != null) {

                        outputStream.close();
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (mFTPClient != null) {
                    try {
                        mFTPClient.logout();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mFTPClient.disconnect();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return success;
        }
    }


}
