import java.util.ArrayList;

public class DataFrame
{
    private ArrayList<Column> frame;

    public DataFrame()
    {
        this.frame = new ArrayList<>();
    }

    public ArrayList<String> getColumnNames()
    {
        ArrayList<String> columnNames = new ArrayList<>();
        for (Column col : frame)
        {
            columnNames.add(col.getName());
        }
        return columnNames;
    }

    public Column getColumn(int index)
    {
        try
        {
            return frame.get(index);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public int getRowCount() //all columns have the same length so just return the length of the first column
    {
        try
        {
            return frame.get(0).getSize();
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public String getValue(String columnName, int row)
    {
        for (Column col : frame)
        {
            if (col.getName().equals(columnName))
            {
                return col.getRowValue(row);
            }
        }
        return null;
    }

    public void putValue(String columnName, int row, String value)
    {
        for (Column col : frame)
        {
            if (col.getName().equals(columnName))
            {
                col.setRowValue(row,value);
            }
        }
    }

    public void addValue(String columnName, String value)
    {
        for (Column col : frame)
        {
            if (col.getName().equals(columnName))
            {
                col.addRowValue(value);
            }
        }
    }

    public void addColumn(Column col)
    {
        frame.add(col);
    }
}