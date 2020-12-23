package com.ysq.theTourGuide.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Administrator implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String account;

    private String password;

    private Integer state;

    @Column(name = "type_id")
    private Integer typeId;

    public static final String ID = "id";

    public static final String ACCOUNT = "account";

    public static final String PASSWORD = "password";

    public static final String STATE = "state";

    public static final String TYPE_ID = "typeId";

    public Administrator(String account){
        this.account = account;
        this.state = 1;
    }

    public Administrator(String account,String psw,Integer typeId){
        this.account = account;
        this.password = psw;
        this.typeId = typeId;
        this.state = 1;
    }

    public Administrator(Integer typeId){
        this.typeId = typeId;
        this.state = 1;
    }
}