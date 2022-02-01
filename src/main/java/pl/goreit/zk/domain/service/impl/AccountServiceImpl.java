package pl.goreit.zk.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.goreit.api.generated.ProductView;
import pl.goreit.api.generated.account.CreateAccountRequest;
import pl.goreit.zk.domain.DomainException;
import pl.goreit.zk.domain.ExceptionCode;
import pl.goreit.zk.domain.model.Account;
import pl.goreit.zk.domain.model.Car;
import pl.goreit.zk.domain.model.OrderLine;
import pl.goreit.zk.domain.service.AccountService;
import pl.goreit.zk.infrastructure.mongo.AccountRepo;
import pl.goreit.zk.infrastructure.mongo.ProductRepo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ConversionService conversionService;


    @Override
    public Account findByUserId(String userId) {
        return accountRepo.findByUserId(userId);
    }

    @Override
    public Account add(CreateAccountRequest request) {
        return accountRepo.save(Objects.requireNonNull(conversionService.convert(request, Account.class)));
    }

    @Override
    public Account save(Account account) {
        return this.accountRepo.save(account);
    }
}
