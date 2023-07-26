package com.regalaxy.phonesin.back.model.repository;

import com.regalaxy.phonesin.back.model.entity.Back;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class BackRepository {

    private final EntityManager em;

    // back_id인 반납 신청서 read
    public Back findOne(Long back_id) {
        return em.find(Back.class, back_id);
    }
}
