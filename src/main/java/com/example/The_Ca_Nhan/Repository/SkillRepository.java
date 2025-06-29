package com.example.The_Ca_Nhan.Repository;

import com.example.The_Ca_Nhan.Entity.Card;
import com.example.The_Ca_Nhan.Entity.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skills, Integer> {
}
