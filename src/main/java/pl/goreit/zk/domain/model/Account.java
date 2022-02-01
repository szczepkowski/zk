package pl.goreit.zk.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
public class Account {

    @Id
    private String userId;
    private Person person;

    private LocalDateTime createdAt;

    public Account(String userId, Person person) {
        this.userId = userId;
        this.person = person;
        this.createdAt = LocalDateTime.now();
    }


    public String getUserId() {
        return userId;
    }

    public Person getPerson() {
        return person;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
