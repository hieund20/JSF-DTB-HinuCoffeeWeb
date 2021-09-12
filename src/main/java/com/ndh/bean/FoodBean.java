/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.bean;

import com.ndh.pojo.Food;
import com.ndh.pojo.Menu;
import com.ndh.pojo.Supplier;
import com.ndh.service.FoodService;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

/**
 *
 * @author HIEU
 */
@ManagedBean
@Named(value = "foodBean")
@RequestScoped
public class FoodBean {
    private static FoodService foodService = new FoodService();
    
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private Menu menu;
    private Set<Supplier> supplier;
    private Part imgFile;
    
    /**
     * Creates a new instance of FoodBean
     */
    
    //Dành cho chức năng cập nhật Food
    public FoodBean() {    
        //isPostBack() cho biết có phải trang được load lần đầu không
        //Nếu trước đó, admin chưa bấm thêm sản phẩm ở foods-addition thì trả về false
        //Ngược lại, nếu đã bấm thêm sản phẩm thì trả về true
        if(!FacesContext.getCurrentInstance().isPostback()){ // !Trang đã từng được load
            String foodId = FacesContext.getCurrentInstance().
                                        getExternalContext().
                                        getRequestParameterMap().
                                        get("food_id");
            //food_id lấy từ <f:param name="food_id".....> ở foods-management
            if(foodId != null && !foodId.isEmpty()){
                //Chỉ lấy Food với Id tương ứng (dòng được bấm Cập nhật)
                Food f = foodService.getFoodById(Integer.parseInt(foodId));
                //Lấy dữ liệu về Food đã được thêm trước đó để hiển thị lên các
                //inputText trong foods-addition
                this.id = f.getId();
                this.name = f.getName();
                this.description = f.getDescription();
                this.price = f.getPrice();
                this.menu = f.getMenu();
                this.supplier = f.getSupplier();
            }
        }
    }
    
    //Lấy danh sách Food thông qua FoodService
    public List<Food> getFoods(){
        return foodService.getFoods();
    }
    
    //Phương thức xử lý việc upload file ảnh từ local
    public void uploadImageFile() throws IOException{
        //Đường dẫn path được lấy từ gói uploadPath
        String path = FacesContext.getCurrentInstance().
                getExternalContext().getInitParameter("com.ndh.uploadPath") //Đường dẫn được cấu hình trong web.xml 
                + this.imgFile.getSubmittedFileName();
        
        try(InputStream inStream = this.imgFile.getInputStream(); //Lấy hình từ local
            OutputStream outStream = new FileOutputStream(path)){ //Lưu hình vào đường dẫn path
            //Thực hiện đọc byte từ file hình và xuất file hình hoàn chỉnh
            byte[] b = new byte[1024];
            int byteRead;
            while((byteRead = inStream.read(b)) != -1)
                outStream.write(b, 0, byteRead);
        }
    }
    
    //Thêm/cập nhật Food thông qua FoodService
    public String addOrUpdateFood() {
        Food f;
        if (this.id > 0) { //Thực hiện cập nhật
            f = foodService.getFoodById(this.id); //Lấy ra Food đã được tạo trong Database
            //Đã ở trạng thái Persistent (Đối tượng) 
        } else { //Thực hiện thêm
            f = new Food(); //Tạo mới hoàn toàn Food 
            //Chưa là Persistent, đang ở trạng thái Transient (Tạm thời)
        }

        //Set các thuộc tính của đối tượng Food tương ứng với các column trong bảng Food 
        f.setName(this.name);
        f.setDescription(this.description);
        f.setPrice(this.price);
        f.setMenu(this.menu);
        f.setSupplier(this.supplier);

        try {
            if (this.imgFile != null) {
                //Đường dẫn file ảnh tồn tại (có chọn ảnh)
                this.uploadImageFile(); //Upload File ảnh
                //Lưu file ảnh được upload lên vào Folder upload trong resources/images
                f.setImage("upload/" + this.imgFile.getSubmittedFileName());
            }
            //Nếu thêm/cập nhật Food thành công thì chuyển đến foods-management.xhtml xem kết quả
            if (foodService.addOrUpdateFood(f) == true) {
                return "foods-management?faces-redirect=true";
            }
        } catch (IOException ex) {
            Logger.getLogger(FoodBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Nếu thêm/cập nhật Food thất bại thì ở lại foods-addition       
        return "foods-addition";
    }
    
    //Xóa Food thông qua FoodService
    public String deleteFood(Food f) throws Exception{
        if(foodService.deleteFood(f))
            return "Xóa thành công !!";
        throw new Exception("Xóa không thành công");
    }
    

    //================LOG===================
    //Hình đã upload được (đã validator được) nhưng chưa lưu được vào folder upload
    
    
    

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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the menu
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    /**
     * @return the supplier
     */
    public Set<Supplier> getSupplier() {
        return supplier;
    }

    /**
     * @param supplier the supplier to set
     */
    public void setSupplier(Set<Supplier> supplier) {
        this.supplier = supplier;
    }

    /**
     * @return the imgFile
     */
    public Part getImgFile() {
        return imgFile;
    }

    /**
     * @param imgFile the imgFile to set
     */
    public void setImgFile(Part imgFile) {
        this.imgFile = imgFile;
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
    
}
