import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Set;

public class GUI extends JFrame
{
    private Model model;
    private JMenuBar menuBar;
    private JMenu file;
    private JMenuItem open;
    private JMenuItem saveAs;
    private JMenuItem exit;
    private JMenu patientsSpecific;
    private JMenu operations;
    private JMenuItem youngestPerson;
    private JMenuItem oldestPerson;
    private JMenuItem averageAge;
    private JMenuItem genderBreakdown;
    private JMenu specialFilters;
    private JCheckBoxMenuItem changedName;
    private JCheckBoxMenuItem alive;
    private JCheckBoxMenuItem dead;
    private JMenu graphs;
    private JMenu help;
    private JPanel mainPanel;
    private JPanel tablePanel;
    private JPanel sidePanel;
    private JScrollPane tableScroll;
    private JTable table;
    private final int tableWidth = 800;
    private final int tableHeight = 650;
    private JPanel searchPanel;
    private JPanel searchPanelA;
    private JPanel searchPanelB;
    private JComboBox searchColumn;
    private JTextField searchField;
    private JButton searchButton;
    private JButton resetButton;
    private JPanel countResultsPanel;
    private JLabel countResults;
    private JPanel allFiltersPanel;
    private JPanel filtersPanel;
    private JPanel showHideLabelPanel;
    private JScrollPane filtersScroll;
    private JPanel showHideColumnsPanel;
    private JPanel advancedFiltersPanel;
    private JScrollPane advancedFiltersScroll;
    private JList columns;
    private ArrayList<JList> advancedFiltersList;

    public GUI()
    {
        openFile();
    }

    private void openFile()
    {
        JFileChooser fc = new JFileChooser(".");
        if (selectOpenFile(fc) == JFileChooser.APPROVE_OPTION)
        {
            tryToOpenFile(fc);
        }
        else
        {
            displayErrorMessage("File Load Error","Error, no file selected!");
            System.exit(-1);
        }
    }

    private void tryToOpenFile(JFileChooser fc)
    {
        LoadingBar lb = new LoadingBar();
        lb.showLoadingBar();
        String fileName = fc.getSelectedFile().getName();
        String filePath = fc.getSelectedFile().getAbsolutePath();
        openSelectedFile(fileName,filePath);
        lb.deleteLoadingBar();
    }

    private void openSelectedFile(String fileName, String filePath)
    {
        try
        {
            createModel(fileName, filePath);
            setTitle(fileName);
            createGUI();
            displayGUI();
        }
        catch (Exception exp)
        {
            fileLoadError(fileName);
            System.exit(0);
        }
    }

    private void createModel(String fileName, String filePath)
    {
        try
        {
            newModel(fileName,filePath);
        }
        catch (Exception e)
        {
            displayErrorMessage("File Load Error", "Unable To Load "+fileName);
            System.exit(0);
        }
    }

    private void newModel(String filename, String filePath) throws Exception
    {
        model = new Model(filename, filePath);
    }

    private void displayErrorMessage(String title, String errorMessage)
    {
        JOptionPane.showMessageDialog(this, errorMessage, title, JOptionPane.ERROR_MESSAGE);
    }

    private void createGUI()
    {
        createMenu();
        createTable();
        createSearchPanel();
        createCountResultsPanel();
        createFiltersPanel();
        combineAllPanels();
    }

    private void createMenu()
    {
        menuBar = new JMenuBar();
        createFile();
        createPatientsSpecific();
        createHelp();
        createGraphs();
        combineAllPartsOfMenu();
    }

    private void createFile()
    {
        file = new JMenu("File");
        createFileMenuItems();
        combineAllFileMenuItems();
    }

    private void createFileMenuItems()
    {
        open = new JMenuItem("Open");
        saveAs = new JMenuItem("Save As");
        exit = new JMenuItem("Exit");
        open.addActionListener((ActionEvent e) -> openClicked());
        saveAs.addActionListener((ActionEvent e) -> saveAsClicked());
        exit.addActionListener((ActionEvent e) -> exitClicked());
    }

    private void openClicked()
    {
        JFileChooser fc = new JFileChooser(".");
        if (selectOpenFile(fc) == JFileChooser.APPROVE_OPTION)
        {
            String fileName = fc.getSelectedFile().getName();
            String filePath = fc.getSelectedFile().getAbsolutePath();
            LoadingBar lb = new LoadingBar();
            try
            {
                lb.showLoadingBar();
                tryToOpenSelectedFile(fileName,filePath);
            }
            catch (Exception exp) { fileLoadError(fileName); }
            finally { lb.deleteLoadingBar(); }
        }
    }

    private void tryToOpenSelectedFile(String fileName, String filePath) throws Exception
    {
        newModel(fileName,filePath);
        newFrame(fileName);
    }

    private int selectOpenFile(JFileChooser fc)
    {
        fc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter("CSV", "csv");
        FileNameExtensionFilter filter2 = new FileNameExtensionFilter("JSON", "json");
        fc.addChoosableFileFilter(filter1);
        fc.addChoosableFileFilter(filter2);
        return fc.showOpenDialog(this);
    }

    private void newFrame(String fileName)
    {
        setTitle(fileName);
        updateMenuBar();
        updateTablePanel();
        updateCountResults();
        updateSearchPanelA();
        updateShowHideColumnsPanel();
        updateAdvancedFilters();
        updateAllFiltersPanel();
        mainPanel.revalidate();
        mainPanel.validate();
        mainPanel.repaint();
    }

    private void updateMenuBar()
    {
        menuBar.removeAll();
        createFile();
        createGraphs();
        createPatientsSpecific();
        createHelp();
        combineAllPartsOfMenu();
        refreshMenuBar();
    }

    private void refreshMenuBar()
    {
        menuBar.revalidate();
        menuBar.validate();
        menuBar.repaint();
    }

    private void updateTablePanel()
    {
        tablePanel.remove(tableScroll);
        updateTable();
    }

    private void updateSearchPanelA()
    {
        searchPanelA.removeAll();
        createSearchColumn();
        searchField.setText("");
        formatSearchPanelA();
        searchPanelA.add(searchColumn);
        searchPanelA.add(searchField);
        refreshSearchPanelA();
    }

    private void refreshSearchPanelA()
    {
        searchColumn.revalidate();
        searchColumn.validate();
        searchColumn.repaint();
        searchPanelA.revalidate();
        searchPanelA.validate();
        searchPanelA.repaint();
    }

    private void updateShowHideColumnsPanel()
    {
        showHideColumnsPanel.removeAll();
        changeShowHideColumnsPanel();
        formatShowHideColumnPanel();
        refreshShowHideColumnsPanel();
    }

    private void changeShowHideColumnsPanel()
    {
        if (model.getTableHeadings().length==0)
        {
            showHideColumnsPanel.setLayout(new BorderLayout());
            showHideColumnsPanel.add(new JLabel("    No Columns Found!", SwingConstants.CENTER));
        }
        else
        {
            createColumn();
            addColumnToShowHideColumnPanel();
            showHideColumnsPanel.setLayout(new BoxLayout(showHideColumnsPanel,BoxLayout.Y_AXIS));
        }
    }

    private void refreshShowHideColumnsPanel()
    {
        showHideColumnsPanel.revalidate();
        showHideColumnsPanel.validate();
        showHideColumnsPanel.repaint();
    }

    private void updateAdvancedFilters()
    {
        advancedFiltersPanel.setVisible(false);
        advancedFiltersPanel.removeAll();
        advancedFiltersPanel.setBackground(Color.white);
        advancedFiltersList = new ArrayList<>();
        formatAdvancedFiltersPanel();
        changeAdvancedFilters();
        refreshAdvancedFilters();
    }

    private void changeAdvancedFilters()
    {
        Hashtable<String, ArrayList<String>> filterOptions = model.getFilterOptions();
        Set<String> keys = filterOptions.keySet();
        int keysLength = keys.size();
        if (keysLength==0)
        {
            createEmptyAdvancedFiltersPanel();
        }
        else
        {
            createAdvancedFilters(filterOptions, keys);
        }
    }

    private void refreshAdvancedFilters()
    {
        advancedFiltersPanel.revalidate();
        advancedFiltersPanel.validate();
        advancedFiltersPanel.repaint();
        advancedFiltersScroll = new JScrollPane(advancedFiltersPanel);
        advancedFiltersScroll.setBackground(Color.white);
        advancedFiltersScroll.revalidate();
        advancedFiltersScroll.validate();
        advancedFiltersScroll.repaint();
        advancedFiltersPanel.setVisible(true);
    }

    private void updateAllFiltersPanel()
    {
        filtersPanel.removeAll();
        filtersPanel.add(showHideLabelPanel, BorderLayout.CENTER);
        filtersPanel.add(showHideColumnsPanel, BorderLayout.CENTER);
        filtersPanel.add(advancedFiltersScroll, BorderLayout.CENTER);
        refreshFiltersPanel();
        allFiltersPanel.removeAll();
        filtersScroll = new JScrollPane(filtersPanel);
        formatFiltersScroll();
        refreshAllFiltersPanel();
    }

    private void refreshFiltersPanel()
    {
        filtersPanel.revalidate();
        filtersPanel.validate();
        filtersPanel.repaint();
    }

    private void refreshAllFiltersPanel()
    {
        allFiltersPanel.add(filtersScroll);
        allFiltersPanel.revalidate();
        allFiltersPanel.validate();
        allFiltersPanel.repaint();
    }

    private void fileLoadError(String fileName)
    {
        displayErrorMessage("File Load Error", "Unable To Load "+ fileName);
    }

    private void saveAsClicked()
    {
        JFileChooser fc = new JFileChooser(".");
        if (selectSaveFile(fc) == JFileChooser.APPROVE_OPTION)
        {
            String fileName = fc.getSelectedFile().getName();
            String filePath = fc.getSelectedFile().getAbsolutePath();
            try { loadFile(fileName, filePath); }
            catch (Exception e)
            {
                fileSaveError(fileName);
            }
        }
    }

    private int selectSaveFile(JFileChooser fc)
    {
        fc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV or JSON", "csv", "json");
        fc.addChoosableFileFilter(filter);
        return fc.showSaveDialog(this);
    }

    private void loadFile(String fileName, String filePath) throws Exception
    {
        if (Objects.requireNonNull(getFiletype(fileName)).toLowerCase().equals("json"))
        {
            model.saveAsJSON(fileName, filePath);
        }
        else if (Objects.requireNonNull(getFiletype(fileName)).toLowerCase().equals("csv"))
        {
            model.saveAsCSV(fileName, filePath);
        }
        else
        {
            throw new Exception();
        }
    }

    private String getFiletype(String fileName)
    {
        int index = fileName.lastIndexOf(".");
        if (index>0)
        {
            return fileName.substring(index+1);
        }
        return null;
    }

    private void fileSaveError(String fileName)
    {
        String errorMessage = "Unable To Save " + fileName + "! Check If Extension Is .json Or .csv";
        String title = "File Save Error";
        JOptionPane.showMessageDialog(this, errorMessage, title, JOptionPane.ERROR_MESSAGE);
    }

    private void exitClicked()
    {
        System.exit(-1);
    }

    private void combineAllFileMenuItems()
    {
        file.add(open);
        file.add(saveAs);
        file.add(exit);
    }

    private void createPatientsSpecific()
    {
        patientsSpecific = new JMenu("Patients Specific");
        if(model.getPatientsFile())
        {
            createPatientsOperations();
            createPatientsFilters();
            combineAllPartsOfPatientsSpecific();
        }
        else
        {
            patientsSpecific.add(new JMenuItem("No Operations Available"));
        }
    }

    private void createPatientsOperations()
    {
        operations = new JMenu("Operations ");
        createYoungestPerson();
        createOldestPerson();
        createAverageAge();
        createGenderBreakdown();
        combineAllOperations();
    }

    private void createYoungestPerson()
    {
        youngestPerson = new JMenuItem("Youngest Person");
        youngestPerson.addActionListener((ActionEvent e) -> youngestPersonClicked());
    }

    private void youngestPersonClicked()
    {
        String[] tableHeadings = model.getTableHeadings();
        String[] record = model.getYoungestPerson();
        int age;
        try
        {
            age = (int) model.calculateAge(record[1]);
        }
        catch (Exception e)
        {
            age = -1;
        }
        int numberOfResults = model.getNumberOfResults();
        new OldestYoungestRecord(tableHeadings,record,age,numberOfResults,false);
    }

    private void createOldestPerson()
    {
        oldestPerson = new JMenuItem("Oldest Person");
        oldestPerson.addActionListener((ActionEvent e) -> oldestPersonClicked());
    }

    private void oldestPersonClicked()
    {
        String[] tableHeadings = model.getTableHeadings();
        String[] record = model.getOldestPerson();
        int age;
        try
        {
            age = (int) model.calculateAge(record[1]);
        }
        catch (Exception e)
        {
            age = -1;
        }
        int numberOfResults = model.getNumberOfResults();
        new OldestYoungestRecord(tableHeadings,record,age,numberOfResults,true);
    }

    private void createAverageAge()
    {
        averageAge = new JMenuItem("Average Age");
        averageAge.addActionListener((ActionEvent e) -> averageAgeClicked());
    }

    private void averageAgeClicked()
    {
        new AverageAge(model.getAverageAge(), model.getNumberOfResults());
    }

    private void createGenderBreakdown()
    {
        genderBreakdown = new JMenuItem("Gender Breakdown");
        genderBreakdown.addActionListener((ActionEvent e) -> genderBreakdownClicked());
    }

    private void genderBreakdownClicked()
    {
        new GenderBreakdown(model.getGenderBreakdown(), model.getNumberOfResults());
    }

    private void combineAllOperations()
    {
        operations.add(youngestPerson);
        operations.add(oldestPerson);
        operations.add(averageAge);
        operations.add(genderBreakdown);
    }

    private void createPatientsFilters()
    {
        specialFilters = new JMenu("Filters");
        changedName = new JCheckBoxMenuItem("Changed Name");
        specialFilters.add(changedName);
        alive = new JCheckBoxMenuItem("Alive");
        specialFilters.add(alive);
        dead = new JCheckBoxMenuItem("Dead");
        specialFilters.add(dead);
    }

    private void combineAllPartsOfPatientsSpecific()
    {
        patientsSpecific.add(operations);
        patientsSpecific.add(specialFilters);
    }

    private void createHelp()
    {
        help = new JMenu("Help");
        JMenu versionInfo = new JMenu("Version Information  ");
        JMenuItem info = new JMenuItem("V0.1 (pre-release)");
        versionInfo.add(info);
        JMenuItem userGuide = new JMenuItem("User Guide");
        userGuide.addActionListener((ActionEvent e) -> userGuideClicked());
        help.add(versionInfo);
        help.add(userGuide);
    }

    private void userGuideClicked()
    {
        new UserGuide();
    }

    private void createGraphs()
    {
        graphs = new JMenu("Graphs");
        Hashtable<String, ArrayList<String>> filterOptions = model.getFilterOptions();
        Set<String> keys = filterOptions.keySet();
        if (keys.isEmpty())
        {
            JMenuItem graphOption = new JMenuItem("No Graphs Available");
            graphs.add(graphOption);
        }
        else
        {
            for (String str : keys)
            {
                JMenuItem graphOption = new JMenuItem(str+" Graph");
                graphOption.addActionListener((ActionEvent e) -> graphOptionClicked(str));
                graphs.add(graphOption);
            }
        }
    }

    private void graphOptionClicked(String heading)
    {
        Hashtable<String, Integer> values = model.getGraphData(heading);
        new BarChart(values, heading, "Frequency", heading+" Data Bar Chart ("+model.getNumberOfResults()
                +" records)").displayGraph();
    }

    private void combineAllPartsOfMenu()
    {
        menuBar.add(file);
        menuBar.add(graphs);
        menuBar.add(patientsSpecific);
        menuBar.add(help);
    }

    private void createTable()
    {
        table = new JTable(model.getData(), model.getTableHeadings());
        formatTable();
        tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(tableWidth,tableHeight));
        tablePanel = new JPanel();
        tablePanel.add(tableScroll);
        tablePanel.setBackground(Color.white);
        tablePanel.setPreferredSize(new Dimension(tableWidth+5,tableHeight+5));
    }

    private void formatTable()
    {
        setColumnWidthToFitData();
        table.setAutoCreateRowSorter(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setDefaultEditor(Object.class, null);
    }

    //www.stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths
    private void setColumnWidthToFitData()
    {
        for (int column = 0; column < table.getColumnCount(); column++)
        {
            int width = 90;
            for (int row = 0; row < table.getRowCount(); row++)
            {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width+5, width);
                width = Math.max(width, table.getColumnModel().getColumn(column).getPreferredWidth()+15);
            }
            width = Math.min(width, 250);
            table.getColumnModel().getColumn(column).setPreferredWidth(width);
        }
    }

    private void createSearchPanel()
    {
        createSearchPanelA();
        createSearchPanelB();
        combineSearchPanels();
    }

    private void createSearchPanelA()
    {
        searchPanelA = new JPanel();
        formatSearchPanelA();
        createSearchColumn();
        createSearchField();
        searchPanelA.add(searchColumn);
        searchPanelA.add(searchField);
    }

    private void formatSearchPanelA()
    {
        searchPanelA.setLayout(new BoxLayout(searchPanelA, BoxLayout.Y_AXIS));
        searchPanelA.add(Box.createRigidArea(new Dimension(5, 5)));
        searchPanelA.setBackground(Color.white);
    }

    private void createSearchColumn()
    {
        searchColumn = new JComboBox(model.searchColumnOptions());
        searchColumn.setSelectedIndex(0);
    }

    private void createSearchField()
    {
        searchField = new JTextField("",15);
        formatSearchField();
    }

    private void formatSearchField()
    {
        TitledBorder title = BorderFactory.createTitledBorder("SEARCH");
        title.setTitleJustification(TitledBorder.CENTER);
        searchField.setBorder(title);
    }

    private void createSearchPanelB()
    {
        searchPanelB = new JPanel();
        formatSearchPanelB();
        createSearchButton();
        createResetButton();
        searchPanelB.add(searchButton);
        searchPanelB.add(resetButton);
    }

    private void formatSearchPanelB()
    {
        searchPanelB.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
        searchPanelB.setBackground(Color.white);
    }

    private void createSearchButton()
    {
        searchButton = new JButton("UPDATE");
        searchButton.addActionListener((ActionEvent e) -> searchButtonClicked());
    }

    private void searchButtonClicked()
    {
        if (columnsSelected()) return;
        String search = searchField.getText();
        model.searchQuery(search);
        searchOptionSelected();
        patientsFiltersSelected();
        advancedFiltersSelected();
        updateView();
    }

    private void searchOptionSelected()
    {
        String selectedColumn = (String) searchColumn.getItemAt(searchColumn.getSelectedIndex());
        model.newSearchColumnSelected(selectedColumn);
    }

    private void patientsFiltersSelected()
    {
        if (model.getPatientsFile())
        {
            model.setFilterChangedName(changedName.getState());
            model.setFilterDead(dead.getState());
            model.setFilterAlive(alive.getState());
        }
    }

    private boolean columnsSelected()
    {
        if (!headingsSelected())
        {
            displayErrorMessage("Search Error", "No Columns Selected!");
            return true;
        }
        return false;
    }

    private boolean headingsSelected()
    {
        try
        {
            int[] indices = columns.getSelectedIndices();
            if (indices.length>0)
            {
                model.newHeadingsSelected(indices);
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private void advancedFiltersSelected()
    {
        model.resetFilterOptionsSelected();
        for (JList list:advancedFiltersList)
        {
            int[] indices = list.getSelectedIndices();
            model.addFilterOptionsSelected(indices);
        }
    }

    private void updateView()
    {
        updateTablePanel();
        updateCountResults();
    }

    private void updateTable()
    {
        table = new JTable(model.getData(), model.getTableHeadings());
        formatTable();
        tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(tableWidth,tableHeight));
        tablePanel.add(tableScroll);
        tablePanel.revalidate();
        tablePanel.validate();
        tablePanel.repaint();
    }

    private void updateCountResults()
    {
        countResults.setText(model.getNumberOfResults()+model.getResultsText());
    }

    private void createResetButton()
    {
        resetButton = new JButton("RESET");
        resetButton.addActionListener((ActionEvent e) -> resetButtonClicked());
    }

    private void resetButtonClicked()
    {
        resetFilters();
        resetSearch();
        resetPatientsFilters();
        if (model.getTotal()>0)
        {
            searchButtonClicked();
        }
    }

    private void resetFilters()
    {
        headingsSelected();
        setColumnSelectionInterval();
        for (JList a : advancedFiltersList)
        {
            a.clearSelection();
            int[] emptyArray = new int[0];
            model.addFilterOptionsSelected(emptyArray);
        }
    }

    private void resetSearch()
    {
        searchColumn.setSelectedIndex(0);
        searchField.setText("");
        model.searchQuery("");
    }

    private void resetPatientsFilters()
    {
        if (model.getPatientsFile())
        {
            changedName.setState(false);
            alive.setState(false);
            dead.setState(false);
        }
    }

    private void combineSearchPanels()
    {
        searchPanel = new JPanel(new BorderLayout());
        formatSearchPanel();
        searchPanel.add(searchPanelA, BorderLayout.NORTH);
        searchPanel.add(searchPanelB, BorderLayout.SOUTH);
    }

    private void formatSearchPanel()
    {
        searchPanel.setBackground(Color.white);
        searchPanel.setPreferredSize(new Dimension(350,110));
        searchPanel.setMaximumSize(new Dimension(350,145));
    }

    private void createCountResultsPanel()
    {
        countResultsPanel = new JPanel(new BorderLayout());
        formatCountResultsPanel();
        countResults = new JLabel(model.getNumberOfResults()+model.getResultsText(),SwingConstants.CENTER);
        countResultsPanel.add(countResults, BorderLayout.CENTER);
    }

    private void formatCountResultsPanel()
    {
        countResultsPanel.setBackground(Color.white);
        countResultsPanel.setPreferredSize(new Dimension(350,50));
        countResultsPanel.setMaximumSize(new Dimension(350,50));
    }

    private void createFiltersPanel()
    {
        createShowHideLabelPanel();
        createShowHideColumnsPanel();
        createAdvancedFiltersPanel();
        combineFilterPanels();
    }

    private void createShowHideLabelPanel()
    {
        showHideLabelPanel = new JPanel(new BorderLayout());
        showHideLabelPanel.setPreferredSize(new Dimension(100,25));
        showHideLabelPanel.setMaximumSize(new Dimension(350,25));
        JLabel showHide = new JLabel("    Show/Hide Columns", SwingConstants.CENTER);
        showHideLabelPanel.add(showHide, BorderLayout.CENTER);
    }

    private void createShowHideColumnsPanel()
    {
        showHideColumnsPanel = new JPanel(new BorderLayout());
        if (model.getTableHeadings().length==0)
        {
            showHideColumnsPanel.add(new JLabel("    No Columns Found!", SwingConstants.CENTER));
        }
        else
        {
            createColumn();
            addColumnToShowHideColumnPanel();
            showHideColumnsPanel.setLayout(new BoxLayout(showHideColumnsPanel,BoxLayout.Y_AXIS));
        }
        formatShowHideColumnPanel();
    }

    private void createColumn()
    {
        columns = new JList(model.getTableHeadings());
        setColumnSelectionInterval();
        setUpColumn();
    }

    private void setUpColumn()
    {
        columns.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        columns.setLayoutOrientation(JList.VERTICAL);
        columns.setVisibleRowCount(-1);
    }

    private void setColumnSelectionInterval()
    {
        try
        {
            int end = columns.getModel().getSize() - 1;
            if (end >= 0)
            {
                columns.setSelectionInterval(0, end);
            }
        }
        catch (Exception e)
        {
            return;
        }
    }

    private void addColumnToShowHideColumnPanel()
    {
        JScrollPane showHideColumnsScroll = new JScrollPane(columns);
        showHideColumnsScroll.setBackground(Color.white);
        showHideColumnsPanel.add(showHideColumnsScroll);
    }

    private void formatShowHideColumnPanel()
    {
        showHideColumnsPanel.setBackground(Color.white);
        showHideColumnsPanel.setPreferredSize(new Dimension(300,150));
        showHideColumnsPanel.setMaximumSize(new Dimension(500,150));
    }

    private void createAdvancedFiltersPanel()
    {
        advancedFiltersPanel = new JPanel();
        formatAdvancedFiltersPanel();
        advancedFiltersList = new ArrayList<>();
        if (model.getFilterOptions().keySet().size()==0)
        {
            createEmptyAdvancedFiltersPanel();
        }
        else
        {
            createAdvancedFilters(model.getFilterOptions(), model.getFilterOptions().keySet());
        }
        advancedFiltersScroll = new JScrollPane(advancedFiltersPanel);
        advancedFiltersScroll.setBackground(Color.white);
    }

    private void formatAdvancedFiltersPanel()
    {
        advancedFiltersPanel.setBackground(Color.white);
    }

    private void createEmptyAdvancedFiltersPanel()
    {
        advancedFiltersPanel.setLayout(new BorderLayout());
        advancedFiltersPanel.add(new JLabel("     No Advanced Filters Found!", SwingConstants.CENTER),
                BorderLayout.CENTER);
    }

    private void createAdvancedFilters(Hashtable<String, ArrayList<String>> filterOptions, Set<String> keys)
    {
        advancedFiltersPanel.setLayout(new BoxLayout(advancedFiltersPanel,BoxLayout.Y_AXIS));
        for (String str : keys)
        {
            JLabel tempLabel = new JLabel(str+":");
            JList tempList = createFilter(filterOptions, str);
            JPanel test = createTempFilterPanel(tempLabel, tempList);
            advancedFiltersPanel.add(test, BorderLayout.WEST);
            advancedFiltersList.add(tempList);
        }
    }

    private JList createFilter(Hashtable<String, ArrayList<String>> filterOptions, String str)
    {
        JList tempList = new JList(filterOptions.get(str).toArray(new String[0]));
        int end = tempList.getModel().getSize() - 1;
        tempList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tempList.setLayoutOrientation(JList.VERTICAL);
        tempList.setVisibleRowCount(Math.min(5, end));
        return tempList;
    }

    private JPanel createTempFilterPanel(JLabel tempLabel, JList tempList)
    {
        JPanel test = new JPanel();
        test.add(tempLabel, BorderLayout.WEST);
        test.add(tempList, BorderLayout.WEST);
        return test;
    }

    private void combineFilterPanels()
    {
        filtersPanel = new JPanel();
        formatFiltersPanel();
        filtersPanel.add(showHideLabelPanel, BorderLayout.CENTER);
        filtersPanel.add(showHideColumnsPanel, BorderLayout.CENTER);
        filtersPanel.add(advancedFiltersScroll, BorderLayout.CENTER);
        filtersScroll = new JScrollPane(filtersPanel);
        formatFiltersScroll();
        allFiltersPanel = new JPanel();
        allFiltersPanel.setBackground(Color.white);
        allFiltersPanel.add(filtersScroll);
    }

    private void formatFiltersPanel()
    {
        filtersPanel.setBackground(Color.white);
        filtersPanel.setLayout(new BoxLayout(filtersPanel, BoxLayout.PAGE_AXIS));
    }

    private void formatFiltersScroll()
    {
        filtersScroll.setPreferredSize(new Dimension(350,490));
        filtersScroll.setBackground(Color.white);
    }

    private void combineAllPanels()
    {
        setJMenuBar(menuBar);
        createSidePanel();
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tablePanel, BorderLayout.WEST);
        mainPanel.add(sidePanel, BorderLayout.EAST);
        mainPanel.setBackground(Color.white);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void createSidePanel()
    {
        sidePanel = new JPanel();
        sidePanel.setBackground(Color.white);
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
        sidePanel.add(searchPanel);
        sidePanel.add(countResultsPanel);
        sidePanel.add(allFiltersPanel);
    }

    private void displayGUI()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(getPreferredSize());
        setVisible(true);
    }
}