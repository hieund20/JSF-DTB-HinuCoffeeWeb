/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.bean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author HIEU
 */
@ManagedBean
@Named(value = "cartBean")
@SessionScoped
public class CartBean implements Serializable {
    //Carts (Giỏ hàng) sẽ ở dạng List<Map<key, value>> vì giỏ hàng gồm nhiều mặt hàng (cart)
    //và mỗi mặt hàng có một định danh riêng

    /**
     * Creates a new instance of CartBean
     */
    public CartBean() {
    }

    //Vòng đời của CDI Bean khi có @PostConstruct, thứ tự khởi tạo Bean:
    //1. CDI container gọi hàm khởi tạo (contructor) của Bean (hàm khởi tạo mặc định
    //hoặc hàm khởi tạo được dánh dấu @inject)
    //2. CDI container khởi tạo giá trị cho các field được inject của bean
    //3. Nếu có method được đánh dấu @PostContruct sẽ được gọi
    @PostConstruct
    public void init() { //Phương thức khởi tại cart
        if (FacesContext.getCurrentInstance().
                getExternalContext().
                getSessionMap().
                get("cart") == null) { //cart chưa tồn tại trong session
            FacesContext.getCurrentInstance().
                    getExternalContext().
                    getSessionMap().
                    put("cart", new HashMap<>()); //put(key, value): Kiểu băm
            //Nếu cart chưa tồn tại thì thêm cart vào session 
        }
    }

    //Lấy ra giỏ hàng (lấy ra danh sách cart trong carts)
    public List<Map<String, Object>> getCarts() {
        Map<Integer, Object> cart = (Map<Integer, Object>) FacesContext.
                getCurrentInstance().
                getExternalContext().
                getSessionMap().get("cart"); //Lấy ra cart đã có trong session

        //Trả về carts (carts gồm các cart)
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object obj: cart.values()) {
            Map<String, Object> c = (Map<String, Object>) obj; //cart
            result.add(c); //Thêm cart vào carts
        }
        return result;
    }

    //Thêm mặt hàng vào giỏ hàng (Thêm food vào cart)
    public String addItemToCart(int foodId, String foodName, BigDecimal price) {
        Map<Integer, Object> cart = (Map<Integer, Object>) FacesContext.
                getCurrentInstance().
                getExternalContext().
                getSessionMap().get("cart"); //Lấy ra cart đã có trong session

        //Khi thêm mặt hàng mới mà trước đó chưa từng có trong cart
        if (cart.get(foodId) == null) { //Nếu cart tương ứng với mặt hàng được bấm chưa có
            Map<String, Object> c = new HashMap<>();
            //put<key, value> kiểu băm
            c.put("foodId", foodId);
            c.put("foodName", foodName);
            c.put("price", price);
            c.put("count", 1); //Số lượng mặt hàng sẽ là 1

            cart.put(foodId, c); //Thêm mặt hàng vào cart
        } else { //Khi thêm mặt hàng đã từng được thêm vào cart trước đó
            Map<String, Object> c = (Map<String, Object>) cart.get(foodId);
            //Vì trước đó, mặt hàng đã từng được thêm vào cart nên khi thêm lần kế tiếp
            //số lượng mặt hàng đó sẽ được tăng lên 1 đơn vị
            c.put("count", Integer.parseInt(c.get("count").toString()) + 1);
        }
        return "";
    }
}
