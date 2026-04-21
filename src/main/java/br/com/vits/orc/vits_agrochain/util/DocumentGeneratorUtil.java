package br.com.vits.orc.vits_agrochain.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Component
public class DocumentGeneratorUtil {

    public String generateSHA256Hash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }

    public String generateQRCodeBase64(String data, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
            
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            
            return Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error generating QR Code", e);
        }
    }

    public String generatePDFBase64(String lotName, List<String> eventSummaries, String qrCodeBase64, String hash) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            
            document.open();
            document.add(new Paragraph("Relatório de Finalização de Cultivo"));
            document.add(new Paragraph("Lote: " + lotName));
            document.add(new Paragraph("Hash SHA-256 (Garantia de Autenticidade): " + hash));
            document.add(new Paragraph("\nHistórico de Eventos:"));
            
            for (String event : eventSummaries) {
                document.add(new Paragraph("- " + event));
            }
            
            document.add(new Paragraph("\nPara verificar a autenticidade, leia o QR Code gerado."));
            document.close();
            
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    public String generateTXTBase64(String lotName, List<String> eventSummaries, String hash) {
        StringBuilder txt = new StringBuilder();
        txt.append("Relatório de Finalização de Cultivo\n");
        txt.append("===================================\n");
        txt.append("Lote: ").append(lotName).append("\n");
        txt.append("Hash SHA-256 (Garantia de Autenticidade): ").append(hash).append("\n\n");
        txt.append("Histórico de Eventos:\n");
        for (String event : eventSummaries) {
            txt.append("- ").append(event).append("\n");
        }
        return Base64.getEncoder().encodeToString(txt.toString().getBytes());
    }
}
