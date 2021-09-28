import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class Model
{
    private DataFrame frame;
    private ArrayList<String> tableHeadings;
    private ArrayList<ArrayList<String>> data;
    private String selectedSearchColumn = "All";
    private String query = "";
    private int[] selectedHeadings;
    private Hashtable<String, ArrayList<String>> filterOptions;
    private ArrayList<int[]> filterOptionsSelected;
    private final int maxFilter = 15;
    private int numberOfResults;
    private int total;
    private final String resultsText = " records found";
    private final String deathDate = "DEATHDATE";
    private boolean isPatientsType;
    private boolean filterChangedName;
    private boolean filterAlive;
    private boolean filterDead;

    public Model(String fileName, String filePath) throws Exception
    {
        readFile(fileName, filePath);
        initialize();
    }

    public String[][] getData()
    {
        ArrayList<String[]> filteredData = filteredData();
        ArrayList<String[]> finalData = showHideHeadings(filteredData);
        updateNumberOfResults(finalData.size());
        return finalData.toArray(new String[0][]);
    }

    public String[] getTableHeadings()
    {
        try
        {
            return computeTableHeadings();
        }
        catch (Exception e)
        {
            return new String[0];
        }
    }

    public Hashtable<String, Integer> getGraphData(String heading)
    {
        Hashtable<String, Integer> graphData = new Hashtable<>();
        ArrayList<String[]> filteredData = filteredData();
        for (String[] record:filteredData)
        {
            String key = record[tableHeadings.indexOf(heading)];
            updateGraphData(graphData, key);
        }
        return graphData;
    }

    public int getTotal()
    {
        return total;
    }

    public int getNumberOfResults()
    {
        return numberOfResults;
    }

    public String getResultsText()
    {
        return resultsText;
    }

    public Hashtable<String, ArrayList<String>> getFilterOptions()
    {
        return filterOptions;
    }

    public void newSearchColumnSelected(String columnName)
    {
        selectedSearchColumn = columnName;
    }

    public void searchQuery(String text)
    {
        query = text;
    }

    public void newHeadingsSelected(int[] headings)
    {
        selectedHeadings = headings;
    }

    public void resetFilterOptionsSelected()
    {
        filterOptionsSelected.clear();
    }

    public void addFilterOptionsSelected(int[] filterOptions)
    {
        filterOptionsSelected.add(filterOptions);
    }

    public boolean getPatientsFile()
    {
        return isPatientsType;
    }

    public void setFilterChangedName(boolean filterChangedName)
    {
        this.filterChangedName = filterChangedName;
    }

    public void setFilterAlive(boolean filterAlive)
    {
        this.filterAlive = filterAlive;
    }

    public void setFilterDead(boolean filterDead)
    {
        this.filterDead = filterDead;
    }

    public void saveAsJSON(String fileName, String filePath) throws IOException
    {
        new JSONWriter(filePath,JSONFrame());
    }

    public void saveAsCSV(String fileName, String filePath) throws IOException
    {
        new CSVWriter(filePath, getTableHeadings(), getData());
    }

    public String getFilterOptionsAtIndex(int index)
    {
        Set<String> keys = filterOptions.keySet();
        return valueAtSetIndex(index, keys);
    }

    public String[] searchColumnOptions()
    {
        ArrayList<String> columnNames = frame.getColumnNames();
        columnNames.add(0,"All");
        return columnNames.toArray(new String[0]);
    }

    public long calculateAge(String DOB)
    {
        int yyyy = Integer.parseInt(DOB.substring(0,4));
        int mm = Integer.parseInt(DOB.substring(5,7));
        int dd = Integer.parseInt(DOB.substring(8,10));
        LocalDateTime birthDate = LocalDateTime.of(yyyy,mm,dd,0,0);
        LocalDateTime currentDate = LocalDateTime.now();
        return Duration.between(birthDate, currentDate).toDays();
    }

    public double[] getGenderBreakdown()
    {
        try
        {
            double male = 0.0;
            double total = 0.0;
            ArrayList<String[]> filteredData = filteredData();
            for (String[] record:filteredData)
            {
                male += numberOfMales(record[tableHeadings.indexOf("GENDER")]);
                total += 1.0;
            }
            return maleFemalePercentages(male, total);
        }
        catch (Exception e)
        {
            return new double[0];
        }
    }

    public double getAverageAge()
    {
        try
        {
            long totalAge = 0;
            long totalRecords = 0;
            ArrayList<String[]> filteredData = filteredData();
            for (String[] record:filteredData)
            {
                if (isRecordAlive(record))
                {
                    totalAge += calculateAge(record[tableHeadings.indexOf("BIRTHDATE")]);
                    totalRecords +=1;
                }
            }
            return calculateAverage(totalAge, totalRecords);
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public String[] getOldestPerson()
    {
        try
        {
            String highestAgeID = findYoungestOrOldestPerson(filteredData(), true, 0);
            return findRecordWithID(highestAgeID, filteredData());
        }
            catch (Exception e)
        {
            return new String[0];
        }
    }

    public String[] getYoungestPerson()
    {
        try
        {
            String lowestAgeID = findYoungestOrOldestPerson(filteredData(), false, 999999999);
            return findRecordWithID(lowestAgeID, filteredData());
        }
        catch (Exception e)
        {
            return new String[0];
        }
    }

    private void readFile(String fileName, String filePath) throws Exception
    { 
        if (isFiletypeCorrect(fileName))
        {
            loadDataFromFileIntoFrame(fileName,filePath);
            return;
        }
        throw new Exception();
    }

    private void loadDataFromFileIntoFrame(String fileName, String filePath) throws Exception
    {
        String extension = fileTypeOfFile(fileName);
        if (extension.equals("csv"))
        {
            this.frame = new DataLoader(filePath).getDataFrame();
        }
        else
        {
            this.frame = new JSONReader(filePath).getDataFrame();
        }
        isFilePatientsType(fileName);
    }

    private boolean isFiletypeCorrect(String fileName)
    { 
        String extension = fileTypeOfFile(fileName);
        return extension.equals("csv") | extension.equals("json");
    }
    
    private String fileTypeOfFile(String fileName)
    {
        int indexOfPeriod = fileName.lastIndexOf(".");
        if (indexOfPeriod>0)
        {
            return fileName.substring(indexOfPeriod+1).toLowerCase();
        }
        return "";
    }

    private void updateGraphData(Hashtable<String, Integer> graphData, String key)
    {
        if (graphData.containsKey(key))
        {
            graphData.put(key, graphData.get(key)+1);
        }
        else
        {
            graphData.put(key,1);
        }
    }

    private void updateNumberOfResults(int size)
    {
        numberOfResults = size;
    }

    private ArrayList<String[]> filteredData()
    {
        ArrayList<String[]> searchedData = searchedData();
        ArrayList<String[]> filteredData = filterData(searchedData);
        return patientsFilter(filteredData);
    }

    private ArrayList<String[]> patientsFilter(ArrayList<String[]> data)
    {
        if (isPatientsType)
        {
            try
            {
                return applyPatientsFilter(data);
            }
            catch (Exception e)
            {
                return data;
            }
        }
        return data;
    }

    private ArrayList<String[]> applyPatientsFilter(ArrayList<String[]> data)
    {
        ArrayList<String[]> filteredData = new ArrayList<>();
        for (String[] record : data)
        {
            if (patientsFilterRecord(record))
            {
                filteredData.add(record);
            }
        }
        return filteredData;
    }

    private boolean patientsFilterRecord(String[] record)
    {
        if (filterChangedName & patientsCheckEmptyField(record, "MAIDEN"))
        {
            return false;
        }
        if (filterAlive & !patientsCheckEmptyField(record, deathDate))
        {
            return false;
        }
        return !(filterDead & patientsCheckEmptyField(record, deathDate));
    }

    private boolean patientsCheckEmptyField(String[] record, String heading)
    {
        int index = tableHeadings.indexOf(heading);
        return record[index].equals("");
    }

    private ArrayList<String[]> filterData(ArrayList<String[]> data)
    {
        ArrayList<String[]> filteredData = new ArrayList<>();
        for (String[] record : data)
        {
            if (filterRecord(record))
            {
                filteredData.add(record);
            }
        }
        return filteredData;
    }

    private String valueAtSetIndex(int index, Set<String> keys)
    {
        int count = 0;
        for (String str : keys)
        {
            if (count== index)
            {
                return str;
            }
            count += 1;
        }
        return null;
    }

    private DataFrame JSONFrame()
    {
        DataFrame JSONFrame = new DataFrame();
        addHeadingToFrame(getSelectedHeadings(), JSONFrame);
        addDataToFrame(getData(), JSONFrame);
        return JSONFrame;
    }

    private ArrayList<String> getSelectedHeadings()
    {
        ArrayList<String> sh = new ArrayList<>();
        for (int selectedHeading : selectedHeadings)
        {
            sh.add(tableHeadings.get(selectedHeading));
        }
        return sh;
    }

    private void addDataToFrame(String[][] data, DataFrame JSONFrame)
    {
        for (String[] record:data)
        {
            for (int i = 0; i < record.length; i++)
            {
                JSONFrame.addValue(tableHeadings.get(selectedHeadings[i]), record[i]);
            }
        }
    }

    private void addHeadingToFrame(ArrayList<String> row, DataFrame frame)
    {
        for (String heading:row)
        {
            Column title = new Column(heading);
            frame.addColumn(title);
        }
    }

    private void initialize()
    {
        initializeTableHeadings();
        initializeTableData();
        initializeNumberOfResults();
        initializeFilters();
    }

    private void initializeTableHeadings()
    {
        tableHeadings = frame.getColumnNames();
        int[] allHeadings = new int[tableHeadings.size()];
        for (int i = 0; i < tableHeadings.size(); i++)
        {
            allHeadings[i] = i;
        }
        selectedHeadings = allHeadings;
    }

    private void initializeNumberOfResults()
    {
        numberOfResults = data.size();
        total = data.size();
    }

    private void addToFilterOptions(int i, ArrayList<String> check)
    {
        if (check.size()>1 & check.size()<maxFilter) // change number later?
        {
            filterOptions.put(tableHeadings.get(i), check);
        }
    }

    private ArrayList<String[]> allData()
    {
        ArrayList<String[]> allData = new ArrayList<>();
        for (ArrayList<String> record : data)
        {
            allData.add(record.toArray(new String[0]));
        }
        return allData;
    }

    private ArrayList<String[]> lookForQuery()
    {
        ArrayList<String[]> searchedData = new ArrayList<>();
        for (ArrayList<String> record : data)
        {
            if (isQueryInRecord(record))
            {
                searchedData.add(record.toArray(new String[0]));
            }
        }
        return searchedData;
    }

    private boolean isQueryInRecord(ArrayList<String> record)
    {
        for (int j = 0; j < record.size(); j++)
        {
            if(selectedSearchColumn.equals("All") | selectedSearchColumn.equals(tableHeadings.get(j)))
            {
                String s = record.get(j).toLowerCase();
                if (s.contains(query.toLowerCase()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private void isFilePatientsType(String fileName)
    {
        isPatientsType = (fileName.equals("patients100.csv")|fileName.equals("patients1000.csv")|
                fileName.equals("patients10000.csv")|fileName.equals("patients100000.csv"));
    }

    private ArrayList<String[]> showHideHeadings(ArrayList<String[]> searchedData)
    {
        ArrayList<String[]> data = new ArrayList<>();
        for (String[] oldRecord:searchedData)
        {
            String[] newRecord = removeHeadingsFromData(oldRecord);
            data.add(newRecord);
        }
        return data;
    }

    private ArrayList<String[]> searchedData()
    {
        if (query.length()>0)
        {
            return lookForQuery();
        }
        else
        {
            return allData();
        }
    }

    private double numberOfMales(String gender)
    {
        if (gender.equals("M"))
        {
            return 1.0;
        }
        return 0.0;
    }

    private double[] maleFemalePercentages(double male, double total)
    {
        if (total<=0)
        {
            return new double[0];
        }
        double scale = Math.pow(10, 2);
        double malePercentage = Math.round(((male/total)*100)*scale)/scale;
        double femalePercentage = 100-malePercentage;
        return new double[] {malePercentage, femalePercentage};
    }

    private boolean filterRecord(String[] record)
    {
        for (int i = 0; i < filterOptionsSelected.size(); i++)
        {
            String s = record[tableHeadings.indexOf(getFilterOptionsAtIndex(i))];
            int[] possibleOptions = filterOptionsSelected.get(i);
            if (checkPossibleOptions(i, s, possibleOptions)) return false;
        }
        return true;
    }

    private boolean checkPossibleOptions(int i, String s, int[] possibleOptions)
    {
        if (possibleOptions.length>0)
        {
            return !isRecordInData(filterOptions.get(getFilterOptionsAtIndex(i)), s, possibleOptions);
        }
        return false;
    }

    private boolean isRecordInData(ArrayList<String> values, String s, int[] possibleOptions)
    {
        for (int index: possibleOptions)
        {
            if (s.toLowerCase().equals(values.get(index).toLowerCase()))
            {
                return true;
            }
        }
        return false;
    }

    private void initializeTableData()
    {
        data = new ArrayList<>();
        if (frame.getRowCount()>0)
        {
            for (int i = 0; i < frame.getRowCount(); i++)
            {
                data.add(recordFromFrame(i));
            }
        }
    }

    private ArrayList<String> recordFromFrame(int i)
    {
        ArrayList<String> record = new ArrayList<>();
        for (String tableHeading : tableHeadings)
        {
            record.add(frame.getValue(tableHeading, i));
        }
        return record;
    }

    private String[] findRecordWithID(String lowestAgeID, ArrayList<String[]> filteredData)
    {
        for (String[] record:filteredData)
        {
            if (record[0].equals(lowestAgeID))
            {
                return record;
            }
        }
        return new String[0];
    }

    private void initializeFilters()
    {
        filterOptions = new Hashtable<>();
        for (int i = 0; i < tableHeadings.size(); i++)
        {
            addToFilterOptions(i, generateFilter(i));
        }
        initializeFilterOptionSelected();
    }

    private ArrayList<String> generateFilter(int i)
    {
        ArrayList<String> filter = new ArrayList<>();
        Column currentColumn = frame.getColumn(i);
        for (int j = 0; j < currentColumn.getSize(); j++)
        {
            if (!filter.contains(currentColumn.getRowValue(j)))
            {
                filter.add(currentColumn.getRowValue(j));
                if (checkSize(filter)) break;
            }
        }
        return filter;
    }

    private boolean checkSize(ArrayList<String> filter) {
        return filter.size() > maxFilter;
    }

    private void initializeFilterOptionSelected()
    {
        filterOptionsSelected = new ArrayList<>();
        for (int i = 0; i < filterOptions.size(); i++)
        {
            int[] temp = new int[filterOptions.get(getFilterOptionsAtIndex(i)).size()];
            for (int j = 0; j < temp.length; j++)
            {
                temp[j] = j;
            }
            filterOptionsSelected.add(temp);
        }
    }

    private String[] removeHeadingsFromData(String[] oldRecord)
    {
        String[] newRecord = new String[selectedHeadings.length];
        for (int i = 0; i < newRecord.length; i++)
        {
            newRecord[i] = oldRecord[selectedHeadings[i]];
        }
        return newRecord;
    }

    private double calculateAverage(long a, long b)
    {
        if (b==0)
        {
            return -1;
        }
        return a/b;
    }

    private boolean isRecordAlive(String[] record)
    {
        return patientsCheckEmptyField(record, deathDate) &
                !patientsCheckEmptyField(record, "BIRTHDATE");
    }

    private String[] computeTableHeadings()
    {
        String[] headings = new String[selectedHeadings.length];
        for (int i = 0; i < headings.length; i++)
        {
            headings[i] = tableHeadings.get(selectedHeadings[i]);
        }
        return headings;
    }

    private String findYoungestOrOldestPerson(ArrayList<String[]> filteredData, boolean oldest, int initialAge)
    {
        long boundaryAge = initialAge;
        String iD = null;
        for (String[] record:filteredData)
        {
            if (isRecordAlive(record))
            {
                long age = calculateAge(record[tableHeadings.indexOf("BIRTHDATE")]);
                if ((age<boundaryAge & !oldest) | (age>boundaryAge & oldest))
                {
                    boundaryAge = age;
                    iD = record[0];
                }
            }
        }
        return iD;
    }
}