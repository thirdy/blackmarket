package net.thirdy.blackmarket.swing;

import com.porty.swing.util.TestDataGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Test Java bean object which is shown by reflection table model.
 *
 * @author iportyankin
 */
public class TableBean {
    private String name, surname;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    static List<TableBean> generateList(int size) {
        List<TableBean> list = new ArrayList<TableBean>();
        char[] chars = new char[20];
        for (int i = 0; i < size; i++) {
            TableBean tableBean = new TableBean();
            try {
                TestDataGenerator.populateBean(tableBean);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            tableBean.setDate(new Date());
            list.add(tableBean);
        }
        return list;
    }

}
