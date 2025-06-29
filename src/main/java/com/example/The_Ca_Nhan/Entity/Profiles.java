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
    @ElementCollection
    @CollectionTable(name = "profile_hobbies", joinColumns = @JoinColumn(name = "profile_id"))
    private List<String>  hobby ;
    @ElementCollection
    @CollectionTable(name = "profile_website", joinColumns = @JoinColumn(name = "profile_id"))
    private List<String> website ;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users users ;

}
