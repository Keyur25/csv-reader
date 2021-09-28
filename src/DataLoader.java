import java.io.BufferedReader;
import java.io.FileReader;

public class DataLoader
{
    private final String filePath;
    private DataFrame frame;
    private boolean firstLineCheck; // used to make sure that the first row is initialized as the column headings
    private String line;

    public DataLoader(String filePath) throws Exception
    {
        this.filePath = filePath;
        this.frame = new DataFrame();
        this.firstLineCheck = true;
        addDataFromFileToFrame();
    }

    public DataFrame getDataFrame()
    {
        return frame;
    }

    private void addDataFromFileToFrame() throws Exception
    {
        BufferedReader file = new BufferedReader(new FileReader(filePath));
        while ((line = file.readLine())!=null)
        {
            String[] row = line.split(",", -1);
            addRowToFrame(row);
        }
    }

    private boolean checkFormat()
    {
        if (line.contains(","))
        {
            String[] row = line.split(",", -1);
            addRowToFrame(row);
        }
        else
        {
            return true;
        }
        return false;
    }

    private void addRowToFrame(String[] row)
    {
        if (firstLineCheck)
        {
            addHeading(row);
            firstLineCheck = false;
            return;
        }
        addRecord(row);
    }

    private void addHeading(String[] row)
    {
        for (String heading:row)
        {
            Column title = new Column(heading);
            frame.addColumn(title);
        }
    }

    private void addRecord(String[] row)
    {
        for (int i = 0; i < row.length; i++)
        {
            Column col = frame.getColumn(i);
            col.addRowValue(row[i]);
        }
    }
}