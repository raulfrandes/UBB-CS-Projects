package jss.lab.weblab;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@WebServlet("/captcha")
public class CaptchaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int width = 150;
        int height = 50;
        char data[][] = {
                {'z', 'a', 'f', '1', '2', '3'},
                {'q', 'r', 't', '4', '5', '6'},
                {'w', 'x', '7', '8', '9', '0'}
        };
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, width, height);

        Random random = new Random();
        String captcha = "";
        for (int i = 0; i < 6; i++) {
            int idx1 = random.nextInt(data.length);
            int idx2 = random.nextInt(data[idx1].length);
            captcha += data[idx1][idx2];
        }

        HttpSession session = req.getSession();
        session.setAttribute("captcha", captcha);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString(captcha, 10, 40);

        resp.setContentType("image/jpeg");
        ImageIO.write(bufferedImage, "jpeg", resp.getOutputStream());
        g.dispose();
    }
}
