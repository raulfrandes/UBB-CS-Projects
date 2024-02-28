package com.example.lab_8.service;

import com.example.lab_8.domain.Message;
import com.example.lab_8.domain.ReplyMessage;
import com.example.lab_8.domain.User;
import com.example.lab_8.repository.Repository;
import com.example.lab_8.utils.events.ChangeEventType;
import com.example.lab_8.utils.events.MessageChangeEvent;
import com.example.lab_8.utils.observer.Observable;
import com.example.lab_8.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MessageService implements Observable<MessageChangeEvent> {
    private Repository<Long, Message> messageRepository;
    private Repository<Long, User> userRepository;
    private List<Observer<MessageChangeEvent>> observers = new ArrayList<>();

    public MessageService(Repository<Long, Message> messageRepository, Repository<Long, User> userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public Optional<Message> addMessage(Long id_source, List<Long> id_destination, String messageText, LocalDateTime date) {
        User sender = userRepository.findOne(id_source)
                .orElseThrow(() -> new ServiceException("User does not exist!"));
        List<User> receivers = id_destination
                .stream()
                .map(id -> userRepository.findOne(id)
                        .orElseThrow(() -> new ServiceException("User does not exist!")))
                .toList();
        Message message = new Message(sender, receivers, messageText, date);
        Optional<Message> savedMessage = messageRepository.save(message);
        if (savedMessage.isEmpty()) {
            notifyObservers(new MessageChangeEvent(ChangeEventType.ADD, null));
        }
        return savedMessage;
    }

    public Optional<Message> addMessage(Long id_source, Long id_destination, String messageText, LocalDateTime date) {
        User sender = userRepository.findOne(id_source)
                .orElseThrow(() -> new ServiceException("User does not exist!"));
        User receiver = userRepository.findOne(id_destination)
                .orElseThrow(() -> new ServiceException("User does not exist!"));
        List<User> receivers = new ArrayList<>();
        receivers.add(receiver);
        Message message = new Message(sender, receivers, messageText, date);
        Optional<Message> savedMessage = messageRepository.save(message);
        if (savedMessage.isEmpty()){
            notifyObservers(new MessageChangeEvent(ChangeEventType.ADD, null));
            return Optional.of(message);
        }
        return savedMessage;
    }

    public Optional<Message> addReplyMessage(Long id_source, Long id_destination, String messageText, LocalDateTime date, Message reply) {
        User sender = userRepository.findOne(id_source)
                .orElseThrow(() -> new ServiceException("User does not exist!"));
        User receiver = userRepository.findOne(id_destination)
                .orElseThrow(() -> new ServiceException("User does not exist!"));
        List<User> receivers = new ArrayList<>();
        receivers.add(receiver);
        ReplyMessage message = new ReplyMessage(sender, receivers, messageText, date, reply);
        Optional<Message> savedMessage = messageRepository.save(message);
        if (savedMessage.isEmpty()){
            notifyObservers(new MessageChangeEvent(ChangeEventType.ADD, null));
            return Optional.of(message);
        }
        return savedMessage;
    }

    public List<User> getUserChats(User user) {
        Iterable<Message> messages = messageRepository.findAll();
        Set<User> chats = StreamSupport.stream(messages.spliterator(), false)
                .filter(message -> message.getFrom().equals(user) || message.getTo().contains(user))
                .flatMap(message -> Stream.concat(Stream.of(message.getFrom()), message.getTo().stream()))
                .collect(Collectors.toSet());
        chats.remove(user);
        return chats.stream().toList();
    }

    public List<Message> getUsersConvo(User user1, User user2) {
        Iterable<Message> messages = messageRepository.findAll();
        return StreamSupport.stream(messages.spliterator(), false)
                .filter(message -> (message.getFrom().equals(user1) && message.getTo().contains(user2)) ||
                                    message.getFrom().equals(user2) && message.getTo().contains(user1))
                .sorted(Comparator.comparing(Message::getDate))
                .toList();
    }

    @Override
    public void addObserver(Observer<MessageChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageChangeEvent t) {
        observers.forEach(x -> x.update(t));
    }
}
