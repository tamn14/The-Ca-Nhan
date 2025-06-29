package com.example.The_Ca_Nhan.Entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id  ;
    private String userName ;
    private String keycloakId ;
    private String lastName ;
    private String firstName ;
    private String email ;
    private String address ;
    private LocalDate createAt ;
    private boolean enable;
    private String url ;
    private boolean isPayment ;
    private LocalDate deleteAt ;


   

    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "users"

    )
    private List<Orders> orders ;
    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "users"

    )
    private List<Experiences> experiences;
    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "users"

    )
    private List<Projects> projects ;
    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "users"

    )
    private List<Educations> educations ;
    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "users"

    )
    private List<Skills> skills ;
    @OneToOne(mappedBy = "users")
    private Profiles profiles ;
    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "users"

    )
    private List<MediaFiles> mediaFiles ;



}
