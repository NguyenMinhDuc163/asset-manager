package com.ptit.asset.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ptit.asset.configuration.PropertiesFileConfig;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class QRCodeUtil {

    private final PropertiesFileConfig propertiesFileConfig;

    public QRCodeUtil(PropertiesFileConfig propertiesFileConfig){
        this.propertiesFileConfig = propertiesFileConfig;
        try {
            Files.createDirectories(Paths.get(propertiesFileConfig.getStorageDir()).toAbsolutePath().normalize());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Boolean generateFile(String credential){
        String path = propertiesFileConfig.getStorageDir() + credential+".png";

        File directory = new File(path);
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return true;
    }

    public Boolean deleteFile(String credential){

        String path = propertiesFileConfig.getStorageDir() + credential+".png";
        System.out.println("========== Delete file with path: " + path);
        File directory = new File(path);
        if (directory.exists()) {
            System.out.println("========== Deleting file processing ...");
            return directory.delete();
        }
        System.out.println("========== Path not exist ...");
        return true;
    }

    public byte[] generateQRCode(String credential, String infoCompressToQRCode, int width, int height)
            throws WriterException, IOException {

        if (!generateFile(credential)){
            return null;
        }
        String fileName = credential+".png";

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(infoCompressToQRCode, BarcodeFormat.QR_CODE, width, height);

        String finalPath = propertiesFileConfig.getStorageDir() + fileName;
//         this step for save image at resource
        Path path = FileSystems.getDefault().getPath(finalPath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        // this step for save image like byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix,"PNG", bos);
        return bos.toByteArray();

    }




}
