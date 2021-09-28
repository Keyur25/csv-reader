import javax.swing.*;
import java.awt.*;

public class GenderBreakdown extends JFrame
{
    private JPanel mainPanel;
    private JLabel title;
    private double[] breakdown;
    private int numberOfResults;

    public GenderBreakdown(double[] breakdown, int numberOfResults)
    {
        super("Gender Breakdown");
        this.breakdown = breakdown;
        this.numberOfResults = numberOfResults;
        createGenderBreakdown();
        displayGenderBreakdown();
    }

    private void createGenderBreakdown()
    {
        mainPanel = new JPanel();
        formatMainPanel();
        if (breakdown.length!=2)
        {
            title = new JLabel("COULDN'T COMPUTE GENDER BREAKDOWN!");
            mainPanel.add(title);
            return;
        }
        addBreakdownToPanel();
    }

    private void formatMainPanel()
    {
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.white);
    }

    private void addBreakdownToPanel()
    {
        title = new JLabel("THE GENDER BREAKDOWN OF THE "+numberOfResults+" RECORDS IN THE TABLE IS: ", SwingConstants.CENTER);
        mainPanel.add(title);
        mainPanel.add(new JLabel(" "));
        JLabel male = new JLabel("MALE: "+breakdown[0]+"%");
        JLabel female = new JLabel("FEMALE: "+breakdown[1]+"%");
        mainPanel.add(male);
        mainPanel.add(female);
    }

    private void displayGenderBreakdown()
    {
        add(mainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(getPreferredSize());
        setVisible(true);
    }
}

