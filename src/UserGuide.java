import javax.swing.*;
import java.awt.*;

public class UserGuide extends JFrame
{
    private final String headingHTMLStart = "<html><body>";
    private final String headingHTMLEnd = "</body></html>";
    private final String heading1 = headingHTMLStart+"Overview"+headingHTMLEnd;
    private final String heading2 = headingHTMLStart+"Filters"+headingHTMLEnd;
    private final String heading3 = headingHTMLStart+"Patients Specific"+headingHTMLEnd;

    private final String textHTMLStart = "<html><body style='width:250px;text-align:justify;font-weight:normal'>";
    private final String textHTMLEnd = "</body></html>";
    private final String text1 = textHTMLStart+ "This is a application which lets you view, manipulate and export " +
            "CSV and JSON Files. From the file option in the menu you can load in any simple CSV and JSON file " +
            "you can then use the search and filter options on the left hand side to manipulate the data. Once you " +
            "are happy you can then save the filtered data as a CSV or JSON file." + textHTMLEnd;
    private final String text2 = textHTMLStart+ "The dropdown right above the search field represents the column " +
            "you want to search in. If it is set to all then the search query will be looked for in all the data. " +
            "In the filters panel below you can choose which columns you would like to see and in the advanced " +
            "filters you can look for records that satisfy specific parameters. To select an option just click " +
            "on it, and to deselect hold CTRL and then click on the option. To select multiple option that are in " +
            "a row click on the first one then hold SHIFT and click on the last one. To select multiple option " +
            "which are not together hold CTRL and click on all the option you want." + textHTMLEnd;
    private final String text3 = textHTMLStart+ "The patients specific menu option gives you additional features " +
            "when looking at patients files. The operations open a new window display the results while the " +
            "filters tab allow for a more advanced filters which can be used like the normal filters." + textHTMLEnd;

    private JScrollPane mainScroll;
    private JPanel textArea;
    private JLabel headingLabel1;
    private JLabel headingLabel2;
    private JLabel headingLabel3;
    private JLabel textLabel1;
    private JLabel textLabel2;
    private JLabel textLabel3;

    public UserGuide()
    {
        super("User Guide");
        createUserGuide();
        displayUserGuide();
    }

    private void createUserGuide()
    {
        initializeTextArea();
        initializeLabels();
        addLabelsToTextArea();
    }

    private void initializeTextArea()
    {
        textArea = new JPanel();
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));
    }

    private void initializeLabels()
    {
        headingLabel1  = new JLabel(heading1);
        headingLabel2  = new JLabel(heading2);
        headingLabel3  = new JLabel(heading3);
        textLabel1 = new JLabel(text1);
        textLabel2 = new JLabel(text2);
        textLabel3 = new JLabel(text3);
    }

    private void addLabelsToTextArea()
    {
        textArea.add(headingLabel1);
        textArea.add(textLabel1);
        textArea.add(new JLabel(" "));
        textArea.add(headingLabel2);
        textArea.add(textLabel2);
        textArea.add(new JLabel(" "));
        textArea.add(headingLabel3);
        textArea.add(textLabel3);
    }

    private void displayUserGuide()
    {
        mainScroll = new JScrollPane(textArea);
        add(mainScroll, BorderLayout.CENTER);
        setPreferredSize(new Dimension(375,250));
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(getPreferredSize());
        setVisible(true);
    }
}
