package net.thirdy.blackmarket.swing;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class JButtonTableExample {

    public JButtonTableExample() {
        JFrame frame = new JFrame("JButtonTable Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel dm = new DefaultTableModel();
        dm.setDataVector(new Object[][]{{"button 1", "foo"},
                    {"button 2", "bar"}}, new Object[]{"Button", "String"});

        JTable table = new JTable(dm);
        table.getColumn("Button").setCellRenderer(new ButtonRenderer());
        table.getColumn("Button").setCellEditor(new ButtonEditor(new JCheckBox()));


        JScrollPane scroll = new JScrollPane(table);

        table.setPreferredScrollableViewportSize(table.getPreferredSize());//thanks mKorbel +1 http://stackoverflow.com/questions/10551995/how-to-set-jscrollpane-layout-to-be-the-same-as-jtable

        table.getColumnModel().getColumn(0).setPreferredWidth(100);//so buttons will fit and not be shown butto..

        frame.add(scroll);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JButtonTableExample();
            }
        });

    }
}