/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.bean;

import com.ndh.pojo.Food;
import com.ndh.pojo.Payment;
import com.ndh.pojo.PaymentDetail;
import com.ndh.pojo.User;
import com.ndh.service.FoodService;
import com.ndh.service.PaymentService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author HIEU
 */
@ManagedBean
@Named(value = "paymentBean")
@RequestScoped
public class PaymentBean {
    private static final FoodService foodService = new FoodService();
    private static final PaymentService paymentService = new PaymentService();
    
    /**
     * Creates a new instance of PaymentBean
     */
    public PaymentBean() {
    }
    
    //Chức năng thanh toán sản phẩm trong giỏ hàng (carts)
    //Khi thực hiện nhấn thanh toán, toàn bộ thông tin của user và mặt hàng được mua bởi
    //user sẽ được lưu vào Database
    //Chú Ý: Phải tồn tại user trong session (user đã được đăng nhập) thì mới thanh toán được
    public String payment(){
        //Lấy ra cart trong session
        Map<Integer, Object> cart = (Map<Integer, Object>) FacesContext.
                                                getCurrentInstance().
                                                getExternalContext().
                                                getSessionMap().get("cart");
        if(cart != null){ //Tồn tại cart trong session
            Payment p = new Payment(); //Tạo mới tiến trình thanh toán
            //Set các thuộc tính của Payment
            p.setCreatedDate(new Date()); //Lấy ra ngày hiện tại
            p.setUser((User) FacesContext.getCurrentInstance().
                                    getExternalContext().
                                    getSessionMap().get("user")); //Lấy ra user đã nhấn thanh toán
            
            List<PaymentDetail> details = new ArrayList<>(); //Danh sách chi tiết hóa đơn
            for(Object obj: cart.values()){
                Map<String, Object> c = (Map<String, Object>) obj; //vẫn là cart
                //food gốc
                Food food = foodService.getFoodById(Integer.parseInt(c.get("foodId").toString()));
                
                PaymentDetail detail = new PaymentDetail(); //Chi tiết hóa đơn
                //Set các thuộc tính của PaymentDetail
                detail.setFood(food);
                detail.setPayment(p);
                detail.setPrice(food.getPrice());
                detail.setCount(Integer.parseInt(c.get("count").toString()));
                details.add(detail);//Thêm detail vào details
            }
            
            //Thanh toán thành công
            if (paymentService.addPayment(p, details) == true) {
                //cart được xóa khi thanh toán thành công
                FacesContext.getCurrentInstance().
                        getExternalContext().
                        getSessionMap().remove("cart");
                //Trở về index
                return "index?faces-redirect=true";
            }
        }
        //Thanh toán không thành công
        return "cart-payment";
    }   
}
