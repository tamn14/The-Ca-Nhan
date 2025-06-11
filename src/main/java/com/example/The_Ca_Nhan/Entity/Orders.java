package com.example.The_Ca_Nhan.Entity;

import jakarta.persistence.*;
import jdk.dynalink.linker.LinkerServices;
import lombok.*;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId ;
    private String orderType ;
    private int totalAmount ;
    private String status ;
    private String address ;
    private Date ordersDate ;
    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "orders"

    )
    private List<Payment> payments = new ArrayList<>();
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
    @ManyToOne(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.PERSIST ,
                    CascadeType.MERGE ,
                    CascadeType.REFRESH
            }

    )
    @JoinColumn(name = "cardId")
    private Card card ;
}
