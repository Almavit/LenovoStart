package by.matveev.lenovostart.lib;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

public class FTPModel {

    public FTPClient mFTPClient = null;
    private Context context; // Добавляем контекст для работы с хранилищем

    // Конструктор с контекстом
    public FTPModel(Context context) {
        this.context = context.getApplicationContext();
    }

    public boolean connect(String host, String username, String password, int port) {
        try {
            return new asyncConnexion(host, username, password, port).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public class asyncConnexion extends AsyncTask<Void, Void, Boolean> {
        private String host;
        private String username;
        private String password;
        private int port;
        private String errorMessage = "";

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

                // Настройка таймаутов для стабильности
                mFTPClient.setConnectTimeout(30000);
                mFTPClient.setDataTimeout(30000);
                mFTPClient.setControlKeepAliveTimeout(300);

                // Подключение
                mFTPClient.connect(host, port);

                // Проверка ответа сервера
                int reply = mFTPClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    errorMessage = "Connection failed with code: " + reply;
                    mFTPClient.disconnect();
                    return false;
                }

                // Авторизация
                boolean status = mFTPClient.login(username, password);
                if (!status) {
                    errorMessage = "Login failed";
                    return false;
                }

                // Настройки для FTP
                mFTPClient.enterLocalPassiveMode();
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.setBufferSize(1024 * 1024); // 1MB буфер

                // Загрузка файла с использованием нового API для Android 10+
                boolean uploadSuccess = uploadFileForAndroid10("Dat1.txt");

                mFTPClient.logout();
                return uploadSuccess;

            } catch (SocketException e) {
                errorMessage = "Socket error: " + e.getMessage();
                e.printStackTrace();
            } catch (IOException e) {
                errorMessage = "IO error: " + e.getMessage();
                e.printStackTrace();
            }
            return false;
        }

        // Метод для загрузки файла с поддержкой Android 10+
        private boolean uploadFileForAndroid10(String filename) {
            try {
                InputStream inputStream = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Для Android 10+ используем MediaStore или запрос разрешений
                     inputStream = getFileInputStreamForAndroid10("Dat1.txt");
                } else {
                    // Для старых версий
                    String filePath = Environment.getExternalStorageDirectory() + "/Documents/Dat1.txt";
                    File file = new File(filePath);
                    if (file.exists()) {
                        inputStream = new FileInputStream(file);
                    }
                }

                if (inputStream != null) {
                    boolean result = mFTPClient.storeFile(filename, inputStream);
                    inputStream.close();
                    return result;
                }

                return false;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        // Получение InputStream для Android 10+
        private InputStream getFileInputStreamForAndroid10(String filename) throws IOException {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Ищем файл через MediaStore
                String[] projection = {MediaStore.MediaColumns.DATA};
                String selection = MediaStore.MediaColumns.DISPLAY_NAME + " = ?";
                String[] selectionArgs = {filename};

                Uri collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);

                try (android.database.Cursor cursor = context.getContentResolver().query(
                        collection, projection, selection, selectionArgs, null)) {

                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                        String filePath = cursor.getString(columnIndex);
                        return new FileInputStream(filePath);
                    }
                }
            }

            // Fallback для старых версий
            String filePath = Environment.getExternalStorageDirectory() + "/Documents/" + filename;
            return new FileInputStream(filePath);
        }
    }
        private OutputStream getOutputStreamForAndroid10(String filename) throws IOException {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = context.getContentResolver();
                Uri contentUri = MediaStore.Files.getContentUri("external");

                // 1. Ищем, существует ли уже файл с таким именем
                String selection = MediaStore.MediaColumns.DISPLAY_NAME + "=?";
                String[] selectionArgs = new String[]{filename};
                Cursor cursor = resolver.query(contentUri, null, selection, selectionArgs, null);

                Uri uri = null;
                if (cursor != null && cursor.moveToFirst()) {
                    // Файл найден — берем его Uri для перезаписи
                    int idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
                    long id = cursor.getLong(idColumn);
                    uri = ContentUris.withAppendedId(contentUri, id);
                    cursor.close();
                } else {
                    // Файл не найден — создаем новую запись
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename );//filename
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream");
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);
                    uri = resolver.insert(contentUri, values);
                }

                if (uri != null) {
                    // Режим "wt" (write-truncate) очищает файл перед записью
                    return resolver.openOutputStream(uri, "wt");
                }
            } else {
                // Для Android 8 и 9
                File documentsDir = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                }
                if (!documentsDir.exists()) {
                    documentsDir.mkdirs();
                }
                File localFile = new File(documentsDir, filename );//"wifi.csv"
                // FileOutputStream по умолчанию перезаписывает файл
                return new FileOutputStream(localFile);
            }
            throw new IOException("Не удалось создать поток вывода");
        }
    //}

    // Скачивание файла с поддержкой Android 10+
    public Boolean downloadAndSaveFile(String host, int port, String username, String password,
                                       String filename, String localFilename) {
        try {
            return new asyncConnexionFTP(host, port, username, password, filename, localFilename).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public class asyncConnexionFTP extends AsyncTask<Void, Void, Boolean> {
        private String host;
        private int port;
        private String username;
        private String password;
        private String filename;
        private String localFilename;
        private String errorMessage = "";

        asyncConnexionFTP(String host, int port, String username, String password,
                          String filename, String localFilename) {
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
            this.filename = filename;
            this.localFilename = localFilename;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean success = false;
            OutputStream outputStream = null;

            try {
                mFTPClient = new FTPClient();

                // Настройка таймаутов
                mFTPClient.setConnectTimeout(30000);
                mFTPClient.setDataTimeout(30000);

                // Подключение
                mFTPClient.connect(host, port);

                int reply = mFTPClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    errorMessage = "Connection failed with code: " + reply;
                    return false;
                }

                // Авторизация
                boolean status = mFTPClient.login(username, password);
                if (!status) {
                    errorMessage = "Login failed";
                    return false;
                }

                // Настройки
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();
                mFTPClient.setBufferSize(1024 * 1024);

                // Сохранение файла для Android 10+
                outputStream = getOutputStreamForAndroid10(filename);

                if (outputStream != null) {
                    success = mFTPClient.retrieveFile(filename, outputStream);
                    if (!success) {
                        errorMessage = "Retrieve failed: " + mFTPClient.getReplyString();
                    }
                } else {
                    errorMessage = "Cannot create output stream";
                }

            } catch (SocketException e) {
                errorMessage = "Socket error: " + e.getMessage();
                e.printStackTrace();
            } catch (IOException e) {
                errorMessage = "IO error: " + e.getMessage();
                e.printStackTrace();
            } finally {
                // Закрытие потоков
                if (outputStream != null) {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // Отключение от FTP
                if (mFTPClient != null && mFTPClient.isConnected()) {
                    try {
                        mFTPClient.logout();
                        mFTPClient.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return success;
        }

        // Получение OutputStream для Android 10+
//        private OutputStream getOutputStreamForAndroid10(String filename) throws IOException {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                // Используем MediaStore для Android 10+
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
//                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream");
//                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);
//
//                Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
//
//                if (uri != null) {
//                    return context.getContentResolver().openOutputStream(uri);
//                }
//            }
//
//            // Fallback для старых версий
//            File documentsDir = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//            }
//            if (!documentsDir.exists()) {
//                documentsDir.mkdirs();
//            }
//            File localFile = new File(documentsDir, filename);
//            return new FileOutputStream(localFile);
//        }
    }
}