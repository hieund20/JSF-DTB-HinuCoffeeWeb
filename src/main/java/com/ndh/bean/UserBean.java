/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.bean;
import com.ndh.pojo.User;
import com.ndh.service.UserService;
import java.net.http.HttpRequest;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
import javax.persistence.Transient;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HIEU
 */
@ManagedBean
@Named(value = "userBean")
@RequestScoped
public class UserBean{
    private static final UserService userService = new UserService();
    
    private String name;
    private String email;
    private String username;
    private String password;
    
    @Transient
    private String confirmPassword;
    
    //Đăng ký
    public String register() {
        if (!this.password.isEmpty() && this.password.equals(this.confirmPassword)) {
            User u = new User();

            u.setName(this.name);
            u.setEmail(this.email);
            u.setUsername(this.username);
            u.setPassword(this.password);

            if (userService.addUser(u) == true) {
                return "login?faces-redirect=true";
            }
        }
        return "register";
    }
    
    //Đăng nhập
    public String login() {
        try {
            User user = userService.login(this.username, this.password);
            if (user != null) { //user có tồn tại
                FacesContext.getCurrentInstance().
                        getExternalContext().
                        getSessionMap().
                        put("user", user); //Thêm user vào danh sách đăng nhập
                //Đăng nhập thành công thì chuyển về index
                return "index?faces-redirect=true";
            }
        } catch (NoResultException ex) {
            FacesMessage facesMess = new FacesMessage("Tài khoản không tồn tại");
            FacesContext faceContext = FacesContext.getCurrentInstance();
            faceContext.addMessage("handleMessage", facesMess);
        }
        //Đăng nhập thất bại thì ở lại login
        return "login";
    }
    
    //Kiểm tra đã đăng nhập chưa, chặn user cố ý đăng nhập dù đã đăng nhập rồi
    public String checkLogin(){
        //Phương thức được gắn ở đầu trang Login qua thẻ <metadata> -> <viewAction>
        //Khi user nhấn Đăng nhập ở index, trang được điều hướng đến login, gặp
        //thẻ trên tiến hàng checkLogin(). Nếu đã đăng nhập rồi (có tồn tại user trong session)
        //thì điều hướng về lại index và ngược lại.
        if(FacesContext.getCurrentInstance().
                getExternalContext().
                getSessionMap().
                get("user") != null) //Lấy ra user đã tồn tại
            return "index?faces-redirect=true";
        
        return null;
    }
    
    //Đăng xuất
    public String logOut(){
        FacesContext.getCurrentInstance().
                    getExternalContext().
                    getSessionMap().
                    remove("user"); //Xóa user khỏi danh sách đăng nhập
        return "login?faces-redirect=true";
    }
   
    //Kiểm tra có phải Admin để hiển thị thanh quản lý
//    public String filterUser(){
//        HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        HttpSession httpSession = httpRequest.getSession();
//        User user = (User) httpSession.getAttribute("user"); //Lấy thông tin user
//        
//        if(user != null){
//            if(user.getuRole().equals("ADMIN"))
//                return "ADMIN";
//        }
//        return "USER";
//    }
    
    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }

    /**
     * @return the userService
     */
    public static UserService getUserService() {
        return userService;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * @param confirmPassword the confirmPassword to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
}
