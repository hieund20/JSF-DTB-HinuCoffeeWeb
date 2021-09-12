/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.bean;

import com.ndh.pojo.Menu;
import com.ndh.service.MenuService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author HIEU
 */
@ManagedBean
@Named(value = "menuBean")
@SessionScoped
public class MenuBean implements Serializable {
    private static final MenuService menuService = new MenuService();
    
    private int id;
    private String name;
    
    /**
     * Creates a new instance of MenuBean
     */
    //Dành cho chức năng cập nhật Menu
    public MenuBean() {
        //isPostBack() cho biết có phải trang được load lần đầu không
        //Nếu trước đó, admin chưa bấm thêm Menu ở menus-addition thì trả về false
        //Ngược lại, nếu đã bấm thêm sản phẩm thì trả về true
        if(!FacesContext.getCurrentInstance().isPostback()){ //!Trang đã từng dược load
            String menuId = FacesContext.getCurrentInstance().
                                        getExternalContext().
                                        getRequestParameterMap().
                                        get("menu_id");
            //menu_id lấy từ <f:param name="menu_id".....> ở menus-management
            if(menuId != null && !menuId.isEmpty()){
                //Chỉ lấy Menu với Id tương ứng (dòng được bấm Cập nhật)
                Menu menu = menuService.getMenusById(Integer.parseInt(menuId));
                //Lấy dữ liệu về Menu đã được thêm trước đó để hiển thị lên các
                //inputText trong menus-addition
                this.id = menu.getId();
                this.name = menu.getName();
            }
        }
    }
    
    //Thêm/cập nhật Menu thông qua MenuService
    public String addOrUpdateMenus(){
        Menu menu;
        if(this.id > 0){ //Thực hiện cập nhật
            menu = menuService.getMenusById(this.id); //Lấy ra Menu đã được tạo trong Database
            //Đã ở trạng thái persistent (đối tượng)
        }
        else{ //Thực hiện thêm
            menu = new Menu(); //Tạo mới hoàn toàn Menu 
            //Chưa là persistent, đang ỏ trạng thái trasient (Tạm thời)
        }
        
        //Set các thuộc tính của Menu tương ứng với các Column trong Database
        menu.setName(this.name);
        
        //Nếu thêm/cập nhật Menu thành công thì chuyển đến trang menus-management xem kết quả
        if(menuService.addOrUpdateMenus(menu) == true)
            return "menus-management?faces-redirect=true";
        
        //Nếu thêm/cập nhật Menu thất bại thì ở lại trang menus-addition
        return "menus-addtion?faces-redirect=true";
    }
    
    //Xóa Menu thông qua MenuService
    public String deleteMenus(Menu menu) throws Exception{
        if(menuService.deleteMenus(menu))
            return "Xóa thành công";
        throw new Exception("Xóa không thành công");
    }
    
    //Lấy ra kết quả truy vấn thông qua MenuService
    public List<Menu> getMenus(){
        return menuService.getMenus();
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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
    
}
