/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.service;

import com.ndh.hibernate.HibernateUtil;
import com.ndh.pojo.Menu;
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
public class MenuService {
    //Lấy ra FACTORY đã được tạo trong HibernateUtil (Không tạo mới)
    private static final SessionFactory FACTORY = HibernateUtil.getFACTORY();
    
    //Lấy danh sách Menu từ Database
    public List<Menu> getMenus(){
        try(Session session = FACTORY.openSession()){ //Mở Session 
            CriteriaBuilder builder = session.getCriteriaBuilder(); //Tạo CriteriaBuilder từ session
            CriteriaQuery<Menu> query = builder.createQuery(Menu.class); //Tạo câu query truy vấn trên class Menu
            Root<Menu> root = query.from(Menu.class); //Tạo root dựa trên query
            query.select(root); //Câu truy vấn Select 
        
            return session.createQuery(query).getResultList(); //Trả về danh sách các dòng lấy được từ query
        }
    }

     //Lấy Menu theo Id từ Database
    public Menu getMenusById(int menuId){
        try(Session session = FACTORY.openSession()){ //Mở Session
            //Trả về đối tượng Menu theo Id tương ứng
            return session.get(Menu.class, menuId);
        }
    }
    
    //Thêm/cập nhật Menu vào Database
    public boolean addOrUpdateMenus(Menu menu){
        try (Session session = FACTORY.openSession()) { //Mỏ Session
            try {
                session.getTransaction().begin(); //Bắt đầu một nhóm câu lệnh SQL
                session.saveOrUpdate(menu); //Thêm/cập nhật Food được truyền vào
                session.getTransaction().commit(); //commit: Lưu thay đổi
            } catch (Exception ex) {
                session.getTransaction().rollback(); //Nếu gặp lỗi thì phục hồi lại các thay đổi
                return false;
            }
        }
        return true;
    }
    
    //Xóa Menu được chọn trong Database
    public boolean deleteMenus(Menu menu){
        try(Session session = FACTORY.openSession()){ //Mở Session
            try{
                session.getTransaction().begin(); //Bắt đầu một nhóm câu lệnh SQL
                session.delete(menu); //Xóa Menu
                session.getTransaction().commit(); //commit: Lưu thay đổi
            } catch (Exception ex){
                session.getTransaction().rollback(); //Nếu gặp lỗi thì phục hồi lại các thay đổi
                return false;
            }
        }
        return true;
    }
   
}
