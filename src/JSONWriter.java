import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JSONWriter
{
    private String filePath;
    private String[] tableHeadings;
    private ArrayList<ArrayList<String>> data;
    private ArrayList<StringBuilder> JSONData;

    public JSONWriter(String filePath, DataFrame frame) throws IOException
    {
        this.filePath = filePath;
        this.tableHeadings = frame.getColumnNames().toArray(new String[0]);
        dataFromFrame(frame);
        this.JSONData = new ArrayList<>();
        dataToJSONData();
        writeJSONToFile();
    }

    private void dataFromFrame(DataFrame frame)
    {
        data = new ArrayList<>();
        if (frame.getRowCount()>0)
        {
            for (int i = 0; i < frame.getRowCount(); i++)
            {
                data.add(recordFromFrame(frame,i));
            }
        }
    }

    private ArrayList<String> recordFromFrame(DataFrame frame, int i)
    {
        ArrayList<String> record = new ArrayList<>();
        for (String tableHeading : tableHeadings)
        {
            record.add(frame.getValue(tableHeading, i));
        }
        return record;
    }

    private void dataToJSONData()
    {
        JSONData.add(new StringBuilder("["));
        for (int i = 0; i < data.size(); i++)
        {
            String[] record = data.get(i).toArray(new String[0]);
            JSONData.add(new StringBuilder("{"));
            for (int j = 0; j < tableHeadings.length; j++)
            {
                addRecord(record[j], j);
            }
            addEndBrace(i);
        }
        JSONData.add(new StringBuilder("]"));
    }

    private void addRecord(String str, int j)
    {
        StringBuilder JSONFormattedRecord = generateJSONFormattedString(str, j);
        if (j<tableHeadings.length-1)
        {
            JSONFormattedRecord.append(",");
        }
        JSONData.add(JSONFormattedRecord);
    }

    private StringBuilder generateJSONFormattedString(String str, int j)
    {
        StringBuilder JSONFormattedRecord = new StringBuilder();
        JSONFormattedRecord.append("\"");
        JSONFormattedRecord.append(tableHeadings[j]);
        JSONFormattedRecord.append("\": \"");
        JSONFormattedRecord.append(str);
        JSONFormattedRecord.append("\"");
        return JSONFormattedRecord;
    }

    private void addEndBrace(int i)
    {
        StringBuilder endBrace = new StringBuilder("}");
        if (i <data.size()-1)
        {
            endBrace.append(",");
        }
        JSONData.add(endBrace);
    }

    private void writeJSONToFile() throws IOException
    {
        FileWriter myWriter = new FileWriter(filePath);
        for (StringBuilder line:JSONData)
        {
            myWriter.write(line+"\r\n");
        }
        myWriter.close();
    }
}