package com.swen3.paperless.repository;

import com.swen3.paperless.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

//kein class sondern interface
public interface MessageRepository extends JpaRepository<Message, UUID> {
}
