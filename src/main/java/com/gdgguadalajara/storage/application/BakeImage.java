package com.gdgguadalajara.storage.application;

import com.gdgguadalajara.assertion.model.Assertion;
import com.gdgguadalajara.common.model.DomainException;

import jakarta.enterprise.context.ApplicationScoped;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.awt.image.BufferedImage;
import java.io.*;

@ApplicationScoped
public class BakeImage {

    public byte[] bake(Assertion assertion) throws Exception {
        if ("image/svg+xml".equalsIgnoreCase(assertion.badgeClass.image.contentType))
            return bakeSvg(assertion.badgeClass.image.data, assertion);
        if ("image/png".equalsIgnoreCase(assertion.badgeClass.image.contentType))
            return bakePng(assertion.badgeClass.image.data, assertion);
        throw DomainException.badRequest("Formato de imagen no soportado: " + assertion.badgeClass.image.contentType);
    }

    private byte[] bakeSvg(byte[] originalImage, Assertion assertion) throws Exception {
        var dbf = DocumentBuilderFactory.newInstance();
        var db = dbf.newDocumentBuilder();
        var doc = db.parse(new ByteArrayInputStream(originalImage));
        var svg = doc.getDocumentElement();

        var script = doc.createElement("script");
        script.setAttribute("type", "application/ld+json");
        script.setAttribute("id", "openbadges");
        script.setTextContent(assertion.jsonPayload);
        svg.insertBefore(script, svg.getFirstChild());

        var tf = TransformerFactory.newInstance();
        var transformer = tf.newTransformer();
        var os = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(os));
        return os.toByteArray();
    }

    private byte[] bakePng(byte[] originalImage, Assertion assertion) throws Exception {
        InputStream is = new ByteArrayInputStream(originalImage);
        ImageReader reader = ImageIO.getImageReadersByFormatName("png").next();
        reader.setInput(ImageIO.createImageInputStream(is));

        BufferedImage image = reader.read(0);
        IIOMetadata metadata = reader.getImageMetadata(0);
        String formatName = "javax_imageio_png_1.0";

        IIOMetadataNode textNode = new IIOMetadataNode("tEXt");

        textNode.appendChild(createTextEntry("openbadges", assertion.jsonPayload));
        textNode.appendChild(createTextEntry("Description", "Badge: " + assertion.badgeClass.name));
        textNode.appendChild(createTextEntry("Comment", "Insignia oficial de " + assertion.badgeClass.issuer.name));

        IIOMetadataNode root = new IIOMetadataNode(formatName);
        root.appendChild(textNode);
        metadata.mergeTree(formatName, root);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(os)) {
            writer.setOutput(ios);
            IIOImage iioImage = new IIOImage(image, null, metadata);
            writer.write(null, iioImage, null);
        } finally {
            writer.dispose();
            reader.dispose();
        }

        return os.toByteArray();
    }

    private IIOMetadataNode createTextEntry(String keyword, String value) {
        IIOMetadataNode entry = new IIOMetadataNode("tEXtEntry");
        entry.setAttribute("keyword", keyword);
        entry.setAttribute("value", value);
        return entry;
    }
}
