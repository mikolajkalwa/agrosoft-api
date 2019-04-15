package com.agrosoft.restAPI.model;

import com.agrosoft.restAPI.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=true)
@Entity
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    private String first_name;
    private String last_name;
    @Column(name = "email")
    private String username;
    @JsonIgnore
    private String password;
//    private Date created_at;
//    private Date updated_at;
    private Date last_login;
    @ManyToOne
    @JoinColumn(name = "farm_id")
    @JsonManagedReference
    private Farm farm;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_has_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
