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

public class Experiences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ExpId ;
    private String name ;
    private int possiton  ;
    private String description ;
    private Date startDate ;
    private Date endDate ;
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
