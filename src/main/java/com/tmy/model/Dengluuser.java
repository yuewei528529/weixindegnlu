package com.tmy.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Dengluuser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    private String code;
    private String oAuthNichegn;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getcode() {
        return code;
    }
    public void setcode(String code) {
        this.code = code;
    }
    public String getoAuthNichegn() {
        return oAuthNichegn;
    }
    public void setoAuthNichegn(String oAuthNichegn) {
        this.oAuthNichegn = oAuthNichegn;
    }

}
