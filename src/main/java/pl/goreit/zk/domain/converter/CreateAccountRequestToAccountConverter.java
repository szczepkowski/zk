package pl.goreit.zk.domain.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.goreit.api.generated.account.CreateAccountRequest;
import pl.goreit.zk.domain.model.Account;
import pl.goreit.zk.domain.model.Person;

@Component
public class CreateAccountRequestToAccountConverter implements Converter<CreateAccountRequest, Account> {

    private final ConversionService conversionService;
    @Value("${pricing.default.balance}")
    private String defaultBalance;

    @Value("${pricing.default.coins}")
    private String defaultCoins;

    @Lazy
    public CreateAccountRequestToAccountConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Account convert(CreateAccountRequest request) {
        return new Account(request.getUserId(),
                conversionService.convert(request.getPerson(), Person.class));
    }
}
