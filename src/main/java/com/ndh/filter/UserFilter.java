/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.filter;

import com.ndh.pojo.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HIEU
 */
//Phân quyền User-Admin
//Các trang được phân quyền: foods-management, menus-management, supplier-management
@WebFilter(urlPatterns= {"/faces/foods-management.xhtml", "/faces/menus-management.xhtml", "/faces/suppliers-management.xhtml"})
public class UserFilter implements Filter{
    private HttpServletRequest httpRequest;

    //Phương thức khởi tạo các thông số cho Filter
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    //Phương thức thực thi, được thi hành khi có request hay response dùng Filter
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        httpRequest = (HttpServletRequest) request;
        
        HttpSession httpSession = httpRequest.getSession(); //Lấy ra Http Session
        User user = (User) httpSession.getAttribute("user"); //Lấy thông tin user
        
        //Đã đăng nhập (tồn tại user trong session)
        if(user != null){ 
            //Không phải ADMIN, bị đẩy về index 
            if(!user.getuRole().equals("ADMIN")){ 
                String home = "/faces/index.xhtml";
                //RequestDispatcher: Giúp trang web chuyển request từ servlet này sang
                //một servlet khác bằng cách gọi một servlet khác từ một servlet khác
                RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(home);
                //Ở đây, đúng ở các trang được phân quyền thực hiển chuyển request đến index page
                //đẩy user không phải admin về index
                dispatcher.forward(request, response);
            }
            //Là ADMIN, cho phép vào các trang được phân quyền
            else{ 
                //Filter chains là đối tượng cho Filter có nhiều công cụ hơn Servlet
                chain.doFilter(request, response);
            }
        }
        //Chưa đăng nhập (chưa tồn tại user trong session)
        else{
            String loginPage = "/faces/login.xhtml";
            //Chuyển request từ các trang được phân quyền đến login, đẩy user về login page
            //để đăng nhập
            RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(loginPage);
            dispatcher.forward(request, response);
        }
    }

    //Phương thức hủy Filter
    @Override
    public void destroy() {
    }
    
}
