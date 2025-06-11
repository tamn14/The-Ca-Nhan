package com.example.The_Ca_Nhan.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Educations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eduId ;
    private String schoolName ;
    private String degree ;
    private Date startDate ;
    private Date endDate ;
    private String description ;
    @ManyToOne(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.PERSIST ,
                    CascadeType.MERGE ,
                    CascadeType.REFRESH
            }

    )
    @JoinColumn(name = "userId")
    private Users users ;
}
