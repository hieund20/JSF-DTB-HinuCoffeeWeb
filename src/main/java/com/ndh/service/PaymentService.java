/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.service;

import com.ndh.hibernate.HibernateUtil;
import com.ndh.pojo.Payment;
import com.ndh.pojo.PaymentDetail;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author HIEU
 */
public class PaymentService {
    //Lấy ra SessionFactory từ Hibernate (Không tạo mới)
   private static final SessionFactory FACTORY = HibernateUtil.getFACTORY();
   
   public boolean addPayment(Payment payment, List<PaymentDetail> details){
       try (Session session = FACTORY.openSession()) { //Mở session
           try {
               session.getTransaction().begin(); //Bắt đầu một khối lệnh SQL
               session.save(payment); //Thêm payment (không cập nhật)

               for (PaymentDetail detail: details) 
                   session.save(detail); //Thêm detail vào details list (không cập nhật)
            
               session.getTransaction().commit(); //commit: Lưu những thay đổi
               return true;
           }catch(Exception ex){
               session.getTransaction().rollback(); //Nếu gặp lỗi thì phục hồi lại lúc trước thay đổi
           }
       }  
       return false;
   }
   
   
}
