package ru.nikita.abeserver.services;

import ru.nikita.abeserver.domain.File;
import ru.nikita.abeserver.domain.User;

public interface MailService {
    void SendFileForUser(User user, File file);

    void sendTextMail(String to, String subject, String text);

    void sendHtmlMail(String to, String subject, String html);
}
