package com.example.lab_8.domain;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message {
    private Message replyTo;
    public ReplyMessage(User from, List<User> to, String message, LocalDateTime date, Message replyTo) {
        super(from, to, message, date);
        this.replyTo = replyTo;
    }

    public Message getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Message replyTo) {
        this.replyTo = replyTo;
    }

    @Override
    public String toString() {
        return "replyTo: " + replyTo.getMessage() + " | " + getMessage();
    }
}
