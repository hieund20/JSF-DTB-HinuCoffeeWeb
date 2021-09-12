/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.bean;

import com.ndh.pojo.Supplier;
import com.ndh.service.SupplierService;
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
@Named(value = "supplierBean")
@SessionScoped
public class SupplierBean implements Serializable {
    private static final SupplierService supplierService = new SupplierService();
    
    private int id;
    private String name;
    private String country;
    
    /**
     * Creates a new instance of SupplierBean
     */
    //Dành cho chức năng cập nhật Menu
    public SupplierBean() {
        if(!FacesContext.getCurrentInstance().isPostback()){ //!Trang đã từng được load
            String supplierId = FacesContext.getCurrentInstance().
                                            getExternalContext().
                                            getRequestParameterMap().
                                            get("supplier_id");
            //supplier_id lấy từ <f:param name="supplier_id".....> ở suppliers-management
            if(supplierId != null && !supplierId.isEmpty()){
                //Chỉ lấy Supplier với Id tương ứng (dòng được bấm Cập nhật)
                Supplier s = supplierService.getSuppliersById(Integer.parseInt(supplierId));
                //Lấy dữ liệu về Supplier đã được thêm trước đó để hiển thị lên các
                //inputText trong suppliers-addition
                this.id = s.getId();
                this.name = s.getName();
                this.country = s.getCountry();
            }
        }
    }
    
    //Lấy danh sách Supplier thông qua SupplierService
    public List<Supplier> getSuppliers(){
        return supplierService.getSuppliers();
    }
    
    //Thêm/Cập nhật Supplier thông qua SupplierService
    public String addOrUpadateSupplier() throws Exception {
        Supplier s;
        if (this.id > 0) { //Thực hiện cập nhật
            s = supplierService.getSuppliersById(this.id); //Lấy ra Supplier đã được tạo trong Database
            //Đã ở trạng thái persistent (đối tượng)
        } else { //Thực hiện thêm
            s = new Supplier(); //Tạo mới hoàn toàn Supplier
            //Chưa là persistent, đang ỏ trạng thái trasient (Tạm thời)
        }

        //Set các thuộc tính của đối tượng Supplier tương ứng với các column trong bảng Food
        s.setName(this.name);
        s.setCountry(this.country);

        try{
            if (supplierService.addOrUpdateSupplier(s) == true) {
                //Nếu thêm/cập nhật Food thành công thì chuyển đến suppliers-management xem kết quả
                return "suppliers-management?faces-redirect=true"; 
            }
        }catch(Exception ex){
            throw new Exception(ex);
        }
        //Nếu thêm/cập nhật Food thất bại thì ở lại suppliers-addition
        return "suppliers-addition";
    }
    
    //Xóa Supplier thông qua SupplierService
    public String deleteSupplier(Supplier s) throws Exception{
        if(supplierService.deleteSupplier(s))
            return "Xóa thành công";
        
        throw new Exception("Xóa không thành công");
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

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
    
}
