package com.example.Cafe.Management.System.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.sql.Update;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name = "User.findByEmailId", query = "select u from User u where u.email=:email")
@NamedQuery(name = "User.getAllUser",query = "select new com.example.Cafe.Management.System.wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status) from User u where u.role='user'")
@NamedQuery(name = "User.getAllAdmin",query = "select u.email from User u where u.role='admin'")
@NamedQuery(name = "User.updateStatus",query = "update User u set u.status=:status where u.id=:id")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
//@DynamicInsert
//@DynamicUpdate
@Table(name="users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNmae")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;

//    public User(String name, String contactNumber, String email, String password, String status, String role) {
//        this.name = name;
//        this.contactNumber = contactNumber;
//        this.email = email;
//        this.password = password;
//        this.status = status;
//        this.role = role;
//    }
}
