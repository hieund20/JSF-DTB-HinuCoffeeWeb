/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

/**
 *
 * @author HIEU
 */

//Dùng để gọi validator cho thuộc tính validator(giao diện)
@FacesValidator("UploadValidator") 
public class UploadValidator implements Validator{
    //Lớp kiểm tra định dạng khi tải file ảnh
    
    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        Part p = (Part) value;
        
        //Kiểm tra định dạng ảnh
        if(!p.getContentType().equals("image/png") && //Định dạng khác png/jpg
           !p.getContentType().equals("image/jpeg")){
            FacesMessage facesMess = new FacesMessage("Định dạng của hình khác png/jpg");
            throw new ValidatorException(facesMess);
        }
        //Kiểm tra kích thước ảnh
        if(p.getSize() > 3145728){ //Kích thước lớn hơn 3MB (3MB = 3145728 bytes)
            FacesMessage faceMess = new FacesMessage("Kích thước vượt quá 3MB");
            throw new ValidatorException(faceMess);
        }
    }
    
}
