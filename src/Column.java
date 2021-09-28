import java.util.ArrayList;

public class Column
{
    private String name;
    private ArrayList<String> rows;

    public Column(String name)
    {
        this.name = name;
        this.rows = new ArrayList<>();
    }

    public String getName()
    {
        return name;
    }

    public int getSize()
    {
        return rows.size();
    }

    public String getRowValue(int index)
    {
        try
        {
            return rows.get(index);
        }
        catch (Exception e)
        {
            return null;
        }

    }

    public void setRowValue(int index, String value)
    {
        try
        {
            rows.set(index, value);
        }
        catch (Exception e)
        {
            return;
        }
    }

    public void addRowValue(String value)
    {
        rows.add(value);
    }
}