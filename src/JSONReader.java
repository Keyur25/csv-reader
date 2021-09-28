import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class JSONReader
{
    private final String fileName;
    private DataFrame frame;
    private final String[] brackets = {"[","]","{","}","},"};
    String line;

    public JSONReader(String fileName) throws Exception
    {
        this.fileName = fileName;
        this.frame = new DataFrame();
        initializeFrame();
    }

    public DataFrame getDataFrame()
    {
        return frame;
    }

    private void initializeFrame() throws Exception
    {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        while ((line = file.readLine())!=null)
        {
            if (!Arrays.asList(brackets).contains(line))
            {
                ArrayList<Integer> indices = indicesOfSpeechMarks(line);
                checkIndicesSize(indices);
                addToFrame(indices);
            }
        }
    }

    private ArrayList<Integer> indicesOfSpeechMarks(String line)
    {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < line.length(); i++)
        {
            if (line.charAt(i) == '"')
            {
                indices.add(i);
            }
        }
        return indices;
    }

    private void checkIndicesSize(ArrayList<Integer> indices) throws Exception
    {
        if (indices.size()!=4)
        {
            throw new Exception();
        }
    }

    private void addToFrame(ArrayList<Integer> indices)
    {
        String column = line.substring(indices.get(0)+1, indices.get(1));
        addColumnToFrame(column);
        String data = line.substring(indices.get(2)+1, indices.get(3));
        addDataToFrame(data,column);
    }

    private void addColumnToFrame(String column)
    {
        if (!isColumnInDataFrame(column))
        {
            Column title = new Column(column);
            frame.addColumn(title);
        }
    }

    private boolean isColumnInDataFrame(String column)
    {
        ArrayList<String> columnsNames = frame.getColumnNames();
        for (String col:columnsNames)
        {
            if (col.equals(column))
            {
                return true;
            }
        }
        return false;
    }

    private void addDataToFrame(String data, String column)
    {
        Column col = frame.getColumn(frame.getColumnNames().indexOf(column));
        col.addRowValue(data);
    }
}