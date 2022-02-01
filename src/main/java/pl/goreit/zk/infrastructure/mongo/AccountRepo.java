package pl.goreit.zk.infrastructure.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.goreit.zk.domain.model.Account;

@Repository
public interface AccountRepo extends MongoRepository<Account, String > {
    Account findByUserId(String userId);
}
