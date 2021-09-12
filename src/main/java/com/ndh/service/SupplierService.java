/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.service;

import com.ndh.hibernate.HibernateUtil;
import com.ndh.pojo.Supplier;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author HIEU
 */
public class SupplierService {
    //Lấy ra SessionFactory từ HibernateUtil (không tạo mới)
    public static final SessionFactory FACTORY = HibernateUtil.getFACTORY();
    
    //Lấy danh sách nhà cung cấp từ Database
    public List<Supplier> getSuppliers(){
        try(Session session = FACTORY.openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder(); //Tạo CriteriaBuilder từ session
            CriteriaQuery<Supplier> query =  builder.createQuery(Supplier.class); //Tạo câu query truy vấn trên class Food
            Root<Supplier> root = query.from(Supplier.class); //Tạo root dựa trên query
            query.select(root); //Câu truy vấn Select
          
            return session.createQuery(query).getResultList(); //Trả về danh sách các dòng lấy được từ query
        }
    } 
    
    //Lấy danh sách nhà cung cấp theo Id từ Database
    public Supplier getSuppliersById(int supplierId){
        try(Session session = FACTORY.openSession()){
            //Trả về đối tượng Supplier theo Id tương ứng
            return session.get(Supplier.class, supplierId);
        }
    }
    
    //Thêm/Cập nhật Supplier vào Database
    public boolean addOrUpdateSupplier(Supplier supplier) {
        try (Session session = FACTORY.openSession()) { //Mở Session
            try {
                session.getTransaction().begin(); //Bắt đầu một nhóm câu lệnh SQL
                session.saveOrUpdate(supplier); //Thêm/cập nhật Supplier
                session.getTransaction().commit(); //commit: Lưu thay đổi
            } catch (Exception ex) {
                session.getTransaction().rollback(); //Nếu gặp lỗi thì phục hồi lại các thay đổi
                return false;
            }
        }
        return true;
    }

    //Xóa Supplier ở Database
    public boolean deleteSupplier(Supplier supplier) {
        try (Session session = FACTORY.openSession()) { //Mở Session
            try {
                session.getTransaction().begin(); //Bắt đầu một nhóm câu lệnh SQL
                session.delete(supplier); //Xóa Supplier
                session.getTransaction().commit(); //commit: Lưu thay đổi
            } catch (Exception ex) {
                session.getTransaction().rollback(); //Nếu gặp lỗi thì phục hồi lại các thay đổi
                return false;
            }
        }
        return true;
    }
    
    
    
    
    
}
