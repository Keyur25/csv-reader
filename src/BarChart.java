import javax.swing.*;
import java.awt.*;
import java.util.*;

public class BarChart extends JPanel
{
    private final int startOfX = 150;
    private final int startOfY = 150;
    private final int lengthOfX = 750;
    private final int lengthOfY = 500;
    private final int yCoordinateOfXAxis = 600;
    private final int maxWidthOfBar = 100;
    private int maxValue;
    private String title;
    private String xLabel;
    private String yLabel;
    private Hashtable<String, Integer> values;

    public BarChart(Hashtable<String, Integer> values, String xLabel, String yLabel, String title)
    {
        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.values = values;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawAxes(g2);
        drawBars(g2);
        labelGraph(g2);
        formatBarChart();
    }

    private void drawAxes(Graphics2D g2)
    {
        g2.setColor(Color.black);
        g2.drawLine(startOfX-5,yCoordinateOfXAxis,startOfX+lengthOfX,yCoordinateOfXAxis); // x-axis
        g2.drawLine(startOfY,100,startOfY,605); // y-axis
    }

    private void drawBars(Graphics2D g2)
    {
        this.maxValue = Collections.max(values.values());
        int widthOfBar = Math.min(Math.round(lengthOfX/values.size()), maxWidthOfBar);
        double unitHeight = (double) lengthOfY/ (double) maxValue;
        int start = startOfX;
        for (Map.Entry<String, Integer> stringIntegerEntry : values.entrySet())
        {
            generateBar(g2, widthOfBar, unitHeight, start, stringIntegerEntry);
            start += widthOfBar;
        }
    }

    private void generateBar(Graphics2D g2, int widthOfBar, double unitHeight, int start, Map.Entry stringIntegerEntry)
    {
        int currentValue = ((int) stringIntegerEntry.getValue());
        double height = unitHeight * currentValue;
        Rectangle bar = new Rectangle(start,  (int) (yCoordinateOfXAxis - height), widthOfBar, (int)height);
        g2.setColor(randomColour());
        g2.fill(bar);
        g2.setColor(Color.black);
        g2.drawLine(start + widthOfBar,yCoordinateOfXAxis, start + widthOfBar,yCoordinateOfXAxis+5);
        String label = generateLabel(stringIntegerEntry);
        g2.drawString(label, start + 5, (yCoordinateOfXAxis) + 15);
    }

    private String generateLabel(Map.Entry mapElement)
    {
        String label = (String) mapElement.getKey();
        if (label.equals(""))
        {
            label = "Unspecified";
        }
        if (label.length()>11)
        {
            label = label.substring(0,11)+"...";
        }
        return label;
    }

    private void labelGraph(Graphics2D g2)
    {
        g2.drawString(xLabel, startOfX+(lengthOfX/2), (yCoordinateOfXAxis) + 50); // label x-axis
        labelYAxis(g2);
    }

    private void labelYAxis(Graphics2D g2)
    {
        g2.drawString(yLabel, startOfY-140, 605-(lengthOfY/2));
        for (int i = 1; i < 5; i++)
        {
            g2.drawString(String.valueOf(i*(maxValue/4.0)), 90, 605-((lengthOfY/4)*(i)));
        }
    }

    private void formatBarChart()
    {
        setBackground(Color.white);
    }

    //www.stackoverflow.com/questions/4246351/creating-random-colour-in-java
    private Color randomColour()
    {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b, (float) 0.6);
    }

    public void displayGraph()
    {
        JFrame barChartWindow = new JFrame();
        barChartWindow.setPreferredSize(new Dimension(1000,750));
        barChartWindow.setTitle(title);
        barChartWindow.add(new BarChart(values, xLabel, yLabel, title));
        barChartWindow.pack();
        barChartWindow.setLocationRelativeTo(null);
        barChartWindow.setVisible(true);
    }
}