package com.porty.swing.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jdesktop.swingx.JXTable;

/**
 * Summary table panel which takes main and summary table and make them look
 * and cooperate as one. Main and summary tables should have exactly the same column
 * set (including types of the columns). It is up to creator of summary table to provide
 * summary data set. By default summary table is not scrollable vertically and takes exact space
 * needed to show its number of rows.
 * <br/>
 * You can override this class and add additional manipulation to tables and scrolling panes if needed
 * as they are accessible for subclasses.
 *
 * @author iportyankin
 * @version 1.0
 */
public class SummaryTablePanel extends JPanel {
    protected final JTable mainTable;
    protected final JTable summaryTable;
    protected JScrollPane mainScrollPane, summaryScrollPane;

    /**
     * Creates a summary panel which lays and connects main and summary tables.
     * @param mainTable Main table with the data
     * @param summaryTable Summary table with the summary data. All the headers and additional behaivour will be stripped.
     */
    public SummaryTablePanel(JTable mainTable, JTable summaryTable) {
        setLayout(new BorderLayout());
        this.mainTable = mainTable;
        this.summaryTable = summaryTable;
        initComponents();
    }

    private void initComponents() {
        // step 1 - add tables into their areas
        mainScrollPane = new JScrollPane(mainTable);
        add(mainScrollPane);
        summaryScrollPane = new JScrollPane(summaryTable);
        add(summaryScrollPane, "South");
        // step 2 - share the column model
        summaryTable.setTableHeader(null);
        // summaryTable.setRowSorter(null);
        summaryTable.setColumnModel(mainTable.getColumnModel());
        // step 3 - remove unnecessary UI
        if ( summaryTable instanceof BaseTable ) {
            ((BaseTable)summaryTable).setFilterHeaderEnabled(false);
        }
        if (summaryTable instanceof JXTable) {
            ((JXTable)summaryTable).setColumnControlVisible(false);
        }
        // step 4 - use only necessary size
        summaryTable.setPreferredScrollableViewportSize(
                new Dimension(summaryTable.getPreferredSize().width,
                summaryTable.getRowHeight() * (summaryTable.getRowCount() == 0 ? 1 : summaryTable.getRowCount()) + 1));
        // step 5 - adjust and connect scroll bars from scroll panes
        summaryScrollPane.setColumnHeader(null);
        summaryScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        summaryScrollPane.getHorizontalScrollBar().setModel(mainScrollPane.getHorizontalScrollBar().getModel());

    }

    // find out when tables are added to make sure all sizes are processed
    // main table may have something available
    @Override
    public void addNotify() {
        super.addNotify();
        // find if tables has installed anything
        if ( mainScrollPane.getRowHeader() != null && mainScrollPane.getRowHeader().getView() != null ) {
            // need to install something the same height to keep summary synced with main
            final JComponent mainRowView = (JComponent) mainScrollPane.getRowHeader().getView();
            summaryScrollPane.setRowHeaderView(new JPanel() {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(mainRowView.getPreferredSize().width, summaryTable.getRowHeight());
                }

            });
        }
    }

}
