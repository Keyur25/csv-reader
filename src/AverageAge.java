import javax.swing.*;
import java.awt.*;

public class AverageAge extends JFrame
{
    private JPanel mainPanel;
    private JLabel title;
    private double averageAge;
    private int numberOfResults;

    public AverageAge(double averageAge, int numberOfResults)
    {
        super("Average Age");
        this.averageAge = averageAge;
        this.numberOfResults = numberOfResults;
        createAverageAge();
        displayAverageAge();
    }

    private void createAverageAge()
    {
        mainPanel = new JPanel();
        formatMainPanel();
        if (averageAge<0)
        {
            title = new JLabel("COULDN'T COMPUTE AVERAGE AGE!");
            mainPanel.add(title);
            return;
        }
        addAverageAgeToPanel();
    }

    private void formatMainPanel()
    {
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.white);
    }

    private void addAverageAgeToPanel()
    {
        title = new JLabel("THE AVERAGE AGE OF THE "+numberOfResults+" RECORDS IN THE TABLE IS: ",
                SwingConstants.CENTER);
        mainPanel.add(title);
        mainPanel.add(new JLabel(" "));
        String space = "                                            ";
        JLabel average = new JLabel(space+Math.round(averageAge / 365) +" YEARS");
        mainPanel.add(average);
    }

    private void displayAverageAge()
    {
        add(mainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(getPreferredSize());
        setVisible(true);
    }
}