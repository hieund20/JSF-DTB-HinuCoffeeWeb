/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.service;

import com.ndh.hibernate.HibernateUtil;
import com.ndh.pojo.Food;
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
public class FoodService {
    private static final SessionFactory FACTORY = HibernateUtil.getFACTORY();
    
    //Lấy danh sách Food từ Database
    public List<Food> getFoods(){
        try(Session session = FACTORY.openSession()){ //Mở Session
            CriteriaBuilder builder = session.getCriteriaBuilder(); //Tạo CriteriaBuilder từ session
            CriteriaQuery<Food> query = builder.createQuery(Food.class); //Tạo câu query truy vấn trên class Food
            Root<Food> root = query.from(Food.class); //Tạo root dựa trên query
            query.select(root); //Câu truy vấn Select
                        
            return session.createQuery(query).getResultList(); //Trả về danh sách các dòng lấy được từ query
        }
    }
    
    //Lấy ra Food theo Id từ Database
    public Food getFoodById(int foodId){
        try(Session session = FACTORY.openSession()){ //Mở Session
             //Trả về đối tượng Food theo Id tương ứng
            return session.get(Food.class, foodId); 
        }
    } 
    
    //Thêm/cập nhật Food vào Database
    public boolean addOrUpdateFood(Food f){
        try (Session session = FACTORY.openSession()) { //Mở Session
            try {
                session.getTransaction().begin(); //Bắt đầu một nhóm câu lệnh SQL
                session.saveOrUpdate(f); //Thêm/cập nhật Food được truyền vào
                session.getTransaction().commit(); //commit: Lưu thay đổi
            } catch (Exception ex) {
                session.getTransaction().rollback(); //Nếu gặp lỗi thì phục hồi lại các thay đổi 
                return false;
            }
        }        
        return true;
    }
    
    //Xóa Food được chọn 
    public boolean deleteFood(Food f){
        try(Session session = FACTORY.openSession()){ //Mở Sesion
            try {
                session.getTransaction().begin(); //Bắt đầu một nhóm câu lệnh SQL
                session.delete(f); //Xóa Food 
                session.getTransaction().commit(); //commit: Lưu thay đổi
            } catch(Exception ex){
                session.getTransaction().rollback(); //Nếu phát sinh lỗi thì phục hồi lại các thay đổi
                return false;
            }
        }
        return true;
    }
    
    
    
    
    
    
}
