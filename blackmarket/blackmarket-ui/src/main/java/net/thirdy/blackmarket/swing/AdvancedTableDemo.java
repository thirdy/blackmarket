package net.thirdy.blackmarket.swing;


import com.porty.swing.table.BaseTable;
import com.porty.swing.table.model.BeanPropertyTableModel;
import com.porty.swing.util.WindowUtils;

import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * Demoes usage of the advanced version of table, BaseTable.
 *
 * @author iportyankin
 */
public class AdvancedTableDemo extends JFrame {

    public AdvancedTableDemo() {
        super("Advanced Table Demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 600);
        WindowUtils.centerWindow(this);
//        setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );
        final BaseTable table = new BaseTable();
        table.setFilterHeaderEnabled(false);
        add(new JScrollPane(table));
        BeanPropertyTableModel<TableBean> model = new BeanPropertyTableModel<TableBean>(TableBean.class);
        model.setOrderedProperties(Arrays.asList("name","surname","date"));
        model.setData(TableBean.generateList(100));
        table.setModel(model);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new AdvancedTableDemo();
            }
        });
    }
}
