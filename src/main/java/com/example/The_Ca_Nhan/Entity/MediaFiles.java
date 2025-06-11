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
public class MediaFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mediaId ;
    private String entityType ;
    private String fileType ;
    private String fileName;
    private int entityId ;
    private String link ;
    private Date updateDate ;
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
