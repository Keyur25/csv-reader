import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSVWriter
{
    private String filePath;
    private String[] tableHeadings;
    private String[][] data;
    private ArrayList<StringBuilder> CSVData;

    public CSVWriter(String filePath, String[] tableHeadings, String[][] data) throws IOException
    {
        this.filePath = filePath;
        this.tableHeadings = tableHeadings;
        this.data = data;
        this.CSVData = new ArrayList<>();
        dataToCSVData();
        writeCSVToFile();
    }

    private void dataToCSVData()
    {
        makeHeadings();
        makeRecords();
    }

    private void makeHeadings()
    {
        StringBuilder allHeadings = new StringBuilder();
        for (int i = 0; i < tableHeadings.length; i++)
        {
            allHeadings.append(tableHeadings[i]);
            if (i< tableHeadings.length-1)
            {
                allHeadings.append(",");
            }
        }
        CSVData.add(allHeadings);
    }

    private void makeRecords()
    {
        for (String[] record : data)
        {
            StringBuilder CSVRecord = new StringBuilder();
            for (int j = 0; j < record.length; j++)
            {
                CSVRecord.append(record[j]);
                if (j < record.length - 1) CSVRecord.append(",");
            }
            CSVData.add(CSVRecord);
        }
    }

    private void writeCSVToFile() throws IOException
    {
        FileWriter myWriter = new FileWriter(filePath);
        for (StringBuilder line:CSVData)
        {
            myWriter.write(line+"\r\n");
        }
        myWriter.close();
    }
}
