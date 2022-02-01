package pl.goreit.zk.infrastructure.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import pl.goreit.zk.domain.model.Order;

import java.util.List;

@Component
public interface OrderRepo extends MongoRepository<Order, String> {

    List<Order> findByUserId(String userId);

}
