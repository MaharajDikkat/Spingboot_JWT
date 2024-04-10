package com.jwt.example.domain.common;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;



@MappedSuperclass
public class IdentityEntity<T> {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    T id;
    @CreatedDate
    LocalDateTime dateCreated;
    @LastModifiedDate
    LocalDateTime lastUpdate;

}
