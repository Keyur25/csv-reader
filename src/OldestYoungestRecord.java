import javax.swing.*;
import java.awt.*;

public class OldestYoungestRecord extends JFrame
{
    private JPanel mainPanel;
    private JLabel title;
    private String[] tableHeadings;
    private String[] record;
    private int age;
    private int numberOfResults;
    private boolean oldest;
    private String labelText;

    public OldestYoungestRecord(String[] tableHeadings, String[] record, int age, int numberOfResults, boolean oldest)
    {
        this.tableHeadings = tableHeadings;
        this.record = record;
        this.age = age;
        this.numberOfResults = numberOfResults;
        this.oldest = oldest;
        initializeLabelText();
        createOldestYoungest();
        displayOldestYoungest();
    }

    private void initializeLabelText()
    {
        if (oldest)
        {
            labelText = "Oldest";
        }
        else
        {
            labelText = "Youngest";
        }
    }

    private void createOldestYoungest()
    {
        mainPanel = new JPanel();
        formatMainPanel();
        if (age<0)
        {
            title = new JLabel("COULDN'T FIND "+labelText.toUpperCase()+" PERSON!");
            mainPanel.add(title);
            return;
        }
        addOldestYoungestToPanel();
    }

    private void formatMainPanel()
    {
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.white);
        setTitle(labelText+" Person");
    }

    private void addOldestYoungestToPanel()
    {
        title = new JLabel("THE "+labelText.toUpperCase()+" PERSON OF THE "+numberOfResults+ " RECORDS IN THE " +
                "TABLE IS: ", SwingConstants.CENTER);
        mainPanel.add(title);
        mainPanel.add(new JLabel(" "));
        for (int i = 0; i < tableHeadings.length; i++)
        {
            if (!record[i].equals(""))
            {
                addHeadingToFrame(i);
            }
        }
    }

    private void addHeadingToFrame(int i)
    {
        JLabel details = new JLabel(tableHeadings[i]+": "+record[i]);
        mainPanel.add(details);
        if (i==1)
        {
            JLabel ageLabel = new JLabel("AGE: "+Math.round(Math.floor(age/365))+" years old");
            Font f = ageLabel.getFont();
            ageLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            mainPanel.add(ageLabel);
        }
    }

    private void displayOldestYoungest()
    {
        add(mainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(getPreferredSize());
        setVisible(true);
    }
}
