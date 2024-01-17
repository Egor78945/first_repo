package com.example.socialweb.services;

import com.example.socialweb.models.entities.Message;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.requestModels.MessageBody;
import com.example.socialweb.repositories.MessageRepository;
import com.example.socialweb.repositories.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<Message> getAllBySenderIdAndRecipientId(Long senderId, Long recipientId) {
        return messageRepository.findAllBySenderIdAndRecipientId(senderId, recipientId);
    }

    public void sendMessageTo(MessageBody body, User sender, Long recipientId) {
        if (!body.getId().equals(sender.getId())) {
            if (isValidMessage(body)) {
                User recipient = userRepository.findUserById(recipientId);
                Message message = new Message();
                message.setSender(sender);
                message.setRecipient(recipient);
                message.setMessage(body.getMessage());
                recipient.getMessages().add(sender);
                log.info("message: attempt to send the message.");
                messageRepository.save(message);
                userRepository.save(recipient);
                log.info("message: message has been sent.");
            }
        } else {
            log.info("message: user cant send messages to himself.");
        }
    }

    private boolean isValidMessage(MessageBody message) {
        log.info("message: checking the message...");
        if (!message.getMessage().isEmpty() && message.getMessage().length() < 250) {
            log.info("message: message is valid.");
            return true;
        } else {
            log.info("message: message is too short or empty.");
            return false;
        }
    }

    private List<Message> getAllByRecipientId(Long id) {
        return messageRepository.findAllByRecipientId(id);
    }
}
