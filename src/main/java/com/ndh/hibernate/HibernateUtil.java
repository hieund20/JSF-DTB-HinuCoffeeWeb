/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.hibernate;

import com.ndh.pojo.Food;
import com.ndh.pojo.Menu;
import com.ndh.pojo.Payment;
import com.ndh.pojo.PaymentDetail;
import com.ndh.pojo.Supplier;
import com.ndh.pojo.User;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author HIEU
 */
public class HibernateUtil {
    private final static SessionFactory FACTORY;

    static{
        //Configuration: Cấu hình
        Configuration conf = new Configuration();
        Properties props = new Properties();
        
        //put(key, value)
        //Lấy Class MySQL5Dialect trong dependencies org.hibernate.dialect
        props.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");  
        //Lấy Class Driver trong dependencies com.mysql.cj.jdbc
        props.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver"); 
        //Lấy ra URL của database (port 3306)
        props.put(Environment.URL, "jdbc:mysql://localhost:3306/ecoffeeshopjsf");
        props.put(Environment.USER, "root");
        props.put(Environment.PASS, "Duchieu200301");
        conf.setProperties(props);
        
        //Gắn Annotaion cho các lớp Pojo
        conf.addAnnotatedClass(Food.class);
        conf.addAnnotatedClass(Menu.class);
        conf.addAnnotatedClass(Supplier.class);
        conf.addAnnotatedClass(User.class);
        conf.addAnnotatedClass(Payment.class);
        conf.addAnnotatedClass(PaymentDetail.class);
        
        ServiceRegistry registry = new StandardServiceRegistryBuilder().
                                    applySettings(conf.getProperties()).
                                    build();
        
        FACTORY = conf.buildSessionFactory(registry);
    }
    
    /**
     * @return the FACTORY
     */
    public static SessionFactory getFACTORY() {
        return FACTORY;
    }

}
