package jss.calendar.calendartag;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarTag extends SimpleTagSupport {
    private int an;
    private int luna;
    private Integer zi;
    private String culoare;
    private String cssClass;

    public void setAn(int an) {
        this.an = an;
    }

    public void setLuna(int luna) {
        this.luna = luna;
    }

    public void setZi(Integer zi) {
        this.zi = zi;
    }

    public void setCuloare(String culoare) {
        this.culoare = culoare;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        Calendar calendar = new GregorianCalendar(an, luna - 1, 1);

        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        out.write("<table class='" + (cssClass != null ? cssClass : "") + "'>");
        out.write("<tr>");
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : weekDays) {
            out.write("<th>" + day + "</th>");
        }
        out.write("</tr>");

        int dayOfWeek = firstDayOfMonth;
        out.write("<tr>");
        for (int i = firstDayOfWeek; i < dayOfWeek; i++) {
            out.write("<td></td>");
        }

        for (int day = 1; day <= daysInMonth; day++) {
            if (dayOfWeek > Calendar.SATURDAY) {
                dayOfWeek = Calendar.SUNDAY;
                out.write("<tr></tr>");
            }

            String style = "";
            if (zi != null && zi == day && culoare != null) {
                style = " style='background-color:" + culoare +";'";
            }
            out.write("<td" + style + ">" + day + "</td>");
            dayOfWeek++;
        }

        while (dayOfWeek <= Calendar.SATURDAY) {
            out.write("<td></td>");
            dayOfWeek++;
        }
        out.write("</tr>");
        out.write("</table>");
    }
}
