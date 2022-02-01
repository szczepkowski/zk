package pl.goreit.zk.domain.model;

import com.google.common.collect.Lists;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.goreit.zk.domain.DomainException;
import pl.goreit.zk.domain.ExceptionCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document
public class Product {

    @Id
    private String id;
    private String workshopId;

    @Indexed(unique=true)
    private String title;
    private String text;
    private BigDecimal price;
    private List<Comment> comments;
    private Status status;

    private LocalDateTime creationDate;

    public Product(String id, String workshopId, String title, String text, BigDecimal price) {
        this.id = id;
        this.workshopId = workshopId;
        this.title = title;
        this.text = text;
        this.price = price;
        this.status = Status.ACTIVE;
        this.creationDate = LocalDateTime.now();
        this.comments = Lists.newArrayList();
    }

    public boolean addComment(Comment comment) throws DomainException {
        if (Status.ACTIVE != getStatus()) {
            throw new DomainException(ExceptionCode.NON_AVAILABLE_PRODUCT);
        }
        return comments.add(comment);
    }

    public String getId() {
        return id;
    }

    public String getWorkshopId() {
        return workshopId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public List<Comment> getComments() {
        return Lists.newArrayList(comments);
    }


    public enum Status {
        ACTIVE, DISABLED
    }
}
