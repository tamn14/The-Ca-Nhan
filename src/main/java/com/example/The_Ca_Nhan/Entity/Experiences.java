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

public class Experiences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ExpId ;
    private String name ;
    private int position  ;
    private String description ;
    private LocalDate startDate ;
    private LocalDate endDate ;
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
