/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ndh.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author HIEU
 */
@Entity
@Table(name = "food")
public class Food implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Id tăng tự động
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private String image;
    
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "supplier_prod",
                joinColumns = {
                    @JoinColumn(name = "food_id")
                },
                inverseJoinColumns = {
                    @JoinColumn(name = "supplier_id")
                }
    )
    private Set<Supplier> supplier; //Set: Danh sách không có phần tử trùng lặp
    
    

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
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
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
    
}
