/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.converter;

import com.ndh.service.SupplierService;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author HIEU
 */
@FacesConverter("SupplierConverter")
public class SupplierConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        //Chuyển đổi các trường được nhập từ textBox giao diện sang đối tượng Supplier
        //trong ManagedBean
        return new SupplierService().getSuppliersById(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object value) {
        //Chuyển đổi đối tượng Supplier trong ManagedBean sang các trường dữ liệu 
        //(thuộc tính) để đổ ra giao diện
        return value.toString();
    }
    
}
