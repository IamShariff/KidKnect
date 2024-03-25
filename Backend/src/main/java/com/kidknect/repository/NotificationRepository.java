package com.kidknect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kidknect.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer>{

}
