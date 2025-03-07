package com.ptit.asset.spacetest;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;


public class QRCodeGenerator {

    static byte [] data;

    public static void generateQRCode(String text, int width, int height, String filePath) throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        // this solution for save image at resource
//        Path path = FileSystems.getDefault().getPath(filePath);
//        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix,"PNG", bos);

        data = bos.toByteArray();

    }

//    public static void readQRCode(byte [] data){
//        BinaryBitmap binaryBitmap
//                = new BinaryBitmap(new HybridBinarizer(
//                new ));
//    }

    public static void main (String [] arg) throws WriterException, IOException {
        generateQRCode("Duy Khanh Ne", 100,100, "Not filePath");
        System.out.println("Data byte array : " + data);

    }

}
