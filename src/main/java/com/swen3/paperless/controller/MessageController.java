package com.swen3.paperless.controller;

import com.swen3.paperless.model.Message;
import com.swen3.paperless.repository.MessageRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")

public class MessageController {

        private final MessageRepository repo;

        public MessageController(MessageRepository repo) {
            this.repo = repo;
        }
        @GetMapping
        public List<Message> getAll () {
            return repo.findAll();
        }

        @PostMapping
        public Message create (@RequestBody Message msg){
            return repo.save(msg); // speichert in H2, gibt mit id zurück
        }
}
