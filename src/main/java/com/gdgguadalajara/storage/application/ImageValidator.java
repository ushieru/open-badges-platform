package com.gdgguadalajara.storage.application;

import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;

import com.gdgguadalajara.common.model.DomainException;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImageValidator {

    private static final byte[] PNG_SIGNATURE = new byte[] {
            (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
    };

    public void run(byte[] bytes, String contentType) {
        if ("image/svg+xml".equalsIgnoreCase(contentType))
            validateSvg(bytes);
        else if ("image/png".equalsIgnoreCase(contentType))
            validatePng(bytes);
        else
            throw DomainException.badRequest("Formato de imagen no soportado: " + contentType);
    }

    private void validatePng(byte[] bytes) {
        try {
            for (int i = 0; i < PNG_SIGNATURE.length; i++) {
                if (bytes[i] != PNG_SIGNATURE[i])
                    throw DomainException.badRequest("Archivo PNG corrupto o inválido");
            }

            var img = ImageIO.read(new ByteArrayInputStream(bytes));
            if (img == null)
                throw DomainException.badRequest("No se pudo leer el PNG");

            checkDimensions(img.getWidth(), img.getHeight());
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void validateSvg(byte[] bytes) {
        try {
            var dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            var db = dbf.newDocumentBuilder();
            var doc = db.parse(new ByteArrayInputStream(bytes));
            var svg = doc.getDocumentElement();

            if (!"svg".equals(svg.getNodeName()))
                throw DomainException.badRequest("El archivo no es un SVG válido");

            double width = parseDimension(svg.getAttribute("width"));
            double height = parseDimension(svg.getAttribute("height"));

            if (width == 0 || height == 0) {
                String viewBox = svg.getAttribute("viewBox");
                if (viewBox != null && !viewBox.isEmpty()) {
                    String[] parts = viewBox.split("\\s+");
                    width = Double.parseDouble(parts[2]);
                    height = Double.parseDouble(parts[3]);
                }
            }

            checkDimensions(width, height);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private double parseDimension(String value) {
        if (value == null || value.isEmpty())
            return 0;
        return Double.parseDouble(value.replaceAll("[^\\d.]", ""));
    }

    private void checkDimensions(double width, double height) {
        if (width > 500 || height > 500)
            throw DomainException.badRequest("La imagen excede los 500x500px");
        if (Math.abs(width - height) > 0.001)
            throw DomainException.badRequest("La imagen debe ser cuadrada (1:1)");
    }

    private void handleException(Exception e) {
        if (e instanceof DomainException)
            throw (DomainException) e;
        throw DomainException.badRequest("Imagen inválida o malformada");
    }
}
