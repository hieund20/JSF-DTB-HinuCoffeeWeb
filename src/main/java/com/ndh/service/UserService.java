/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.service;

import com.ndh.hibernate.HibernateUtil;
import com.ndh.pojo.User;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author HIEU
 */
public class UserService {
    //Lấy ra FACTORY từ HibernateUtil (Không tạo mới)
    private static final SessionFactory FACTORY = HibernateUtil.getFACTORY();
    
    //Thêm User (Đăng ký)
    public boolean addUser(User u){
        try(Session session = FACTORY.openSession()){ //Mở Session
            try{
                session.getTransaction().begin();
                u.setPassword(DigestUtils.md5Hex(u.getPassword()));
                session.save(u);
                session.getTransaction().commit();
                return true;
            }catch(Exception ex){
                session.getTransaction().rollback();
            }   
        }
        return false;
    }
    
    //Đăng nhập
    public User login(String username, String password){
        password = DigestUtils.md5Hex(password); //Băm password khi đăng nhập
        //Khi kiểm tra, sẽ so sánh username và mã băm 
        try(Session session = FACTORY.openSession()){
            //builder xem như user trong phiên đăng nhập hiện tại
            CriteriaBuilder builder = session.getCriteriaBuilder(); 
            CriteriaQuery<User> query = builder.createQuery(User.class);
            
            //Câu truy vấn Select Fron Where
            //root xem như user đã được đăng ký vá có trong database
            Root<User> root = query.from(User.class);
            query.select(root);
            //Select user
            //From user table
            //Where input username = username and input password = password
            query.where(builder.and(builder.equal(root.get("username").as(String.class), username), 
                        builder.equal(root.get("password").as(String.class), password)));
            
            //Trả về chỉ một dòng kết quả
            return session.createQuery(query).getSingleResult();
        }
    
    }


}
