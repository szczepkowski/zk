package pl.goreit.zk.domain.service;

import pl.goreit.api.generated.account.CreateAccountRequest;
import pl.goreit.zk.domain.DomainException;
import pl.goreit.zk.domain.model.Account;
import pl.goreit.zk.domain.model.Car;
import pl.goreit.zk.domain.model.OrderLine;

import java.util.List;

public interface AccountService {

    Account findByUserId(String userId);

    Account add(CreateAccountRequest request);

    Account save(Account account);
}
