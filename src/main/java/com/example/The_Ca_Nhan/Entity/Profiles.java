package com.example.The_Ca_Nhan.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Profiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profileId ;
    private String summary  ;
    private List<String>  hobby ;
    private List<String> website ;
    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Users users ;

}
