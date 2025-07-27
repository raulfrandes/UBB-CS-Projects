package jss.graph.graphtag;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class GraphTag extends SimpleTagSupport {
    private String valuesOx;
    private String valuesOy;
    private String labelOx;
    private String labelOy;
    private int minOx;
    private int maxOx;
    private int minOy;
    private int maxOy;
    private String color;

    public void setValuesOx(String valuesOx) {
        this.valuesOx = valuesOx;
    }

    public void setValuesOy(String valuesOy) {
        this.valuesOy = valuesOy;
    }

    public void setLabelOx(String labelOx) {
        this.labelOx = labelOx;
    }

    public void setLabelOy(String labelOy) {
        this.labelOy = labelOy;
    }

    public void setMinOx(int minOx) {
        this.minOx = minOx;
    }

    public void setMaxOx(int maxOx) {
        this.maxOx = maxOx;
    }

    public void setMinOy(int minOy) {
        this.minOy = minOy;
    }

    public void setMaxOy(int maxOy) {
        this.maxOy = maxOy;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void doTag() throws JspException, IOException {
        if (valuesOx == null || valuesOy == null) {
            throw new JspException("valuesOx and valuesOy must not be null");
        }

        int[] ox = parseArray(valuesOx);
        int[] oy = parseArray(valuesOy);

        if (ox == null || oy == null) {
            throw new JspException("Parsed valuesOx and valuesOy must not be null");
        }

        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 400, 300);

        g2d.setColor(Color.BLACK);
        g2d.drawLine(50, 250, 350, 250);
        g2d.drawLine(50, 50, 50, 250);

        int tickCount = 10;
        for (int i = 0; i <= tickCount; i++) {
            int x = 50 + i * 300 / tickCount;
            int value = minOx + i * (maxOx - minOx) / tickCount;
            g2d.drawLine(x, 245, x, 255);
            g2d.drawString(String.valueOf(value), x - 5, 265);
        }

        for (int i = 0; i <= tickCount; i++) {
            int y = 250 - i * 200 / tickCount;
            int value = minOy + i * (maxOy - minOy) / tickCount;
            g2d.drawLine(45, y, 55, y);
            g2d.drawString(String.valueOf(value), 30, y + 5);
        }

        g2d.drawString(labelOx, 200, 290);
        g2d.drawString(labelOy, 10, 20);

        g2d.setColor(Color.decode(color));
        for (int i = 0; i < ox.length - 1; i++) {
            int x1 = 50 + (ox[i] - minOx) * 300 / (maxOx - minOx);
            int y1 = 250 - (oy[i] - minOy) * 200 / (maxOy - minOy);
            int x2 = 50 + (ox[i + 1] - minOx) * 300 / (maxOx - minOx);
            int y2 = 250 - (oy[i + 1] - minOy) * 200 / (maxOy - minOy);
            g2d.drawLine(x1, y1, x2, y2);
        }

        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());

        JspWriter out = getJspContext().getOut();
        out.write("<img src='data:image/png;base64," + base64Image + "'/>");
    }

    private int[] parseArray(String array) {
        if (array == null || array.isEmpty()) {
            return null;
        }
        String[] parts = array.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }
}
