package com.porty.swing.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * Test instrument which populates all string properties of a bean with randomly
 * generated strings.
 * 
 * @author iportyankin
 */
public class TestDataGenerator {

    public static void populateBean(Object bean) throws Exception {
        BeanInfo info = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] pds = info.getPropertyDescriptors();
        for (PropertyDescriptor next: pds) {
            if ( next.getPropertyType().equals(String.class) && next.getWriteMethod() != null ) {
                next.getWriteMethod().invoke(bean, genString(10));
            }
        }
    }


    public static String genString(int size) {
        StringBuilder str = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int symbol = 'A' + (int) (Math.random() * 26);
            str.append((char) symbol);
        }
        return str.toString();
    }

}
