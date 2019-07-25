package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import ca.jrvs.apps.trading.service.FundTransferService;
import ca.jrvs.apps.trading.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/trader")
public class TraderContoller {

    private FundTransferService fundTransferService;
    private RegisterService registerService;
    private TraderDao traderDao;
    private AccountDao accountDao;
    private SecurityOrderDao securityOrderDao;
    private PositionDao positionDao;

    @Autowired

    public TraderContoller(FundTransferService fundTransferService, RegisterService registerService,
                           TraderDao traderDao, AccountDao accountDao, SecurityOrderDao securityOrderDao,
                           PositionDao positionDao) {
        this.fundTransferService = fundTransferService;
        this.registerService = registerService;
        this.traderDao = traderDao;
        this.accountDao = accountDao;
        this.securityOrderDao = securityOrderDao;
        this.positionDao = positionDao;
    }

    @PostMapping(path = "/traderId/{traderId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTrader(@PathVariable Integer traderId) {
        try {
            registerService.deleteTraderById(traderId);
        } catch (Exception e) {
            throw ResponseExceptionUtil.getResponseStatusException(e);
        }
    }

    @PostMapping(path = "/")
    @ResponseStatus(HttpStatus.OK)
    public TraderAccountView createTraderAndAccount(@RequestBody Trader trader) {
        try {
            return registerService.createTraderAndAccount(trader);
        } catch (Exception e) {
            throw ResponseExceptionUtil.getResponseStatusException(e);
        }
    }

    @PostMapping(path = "/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    public TraderAccountView createTraderAndAccount(@PathVariable String firstname,
                                                    @PathVariable String lastname,
                                                    @PathVariable String dob,
                                                    @PathVariable String country,
                                                    @PathVariable String email) {
        try {
            return registerService.createTraderAndAccount(
                    traderDao.createTrader(firstname, lastname, dob, country, email));
        } catch (Exception e) {
            throw ResponseExceptionUtil.getResponseStatusException(e);
        }
    }

    @PutMapping(path = "/deposit/traderId/{traderId}/amount/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public Account depositFund(@PathVariable Integer traderId, @PathVariable Double amount) {
        try {
            return fundTransferService.deposit(traderId, amount);
        } catch (Exception e) {
            throw ResponseExceptionUtil.getResponseStatusException(e);
        }
    }

    @PutMapping(path = "/withdraw/traderId/{traderId}/amount/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public Account withdrawFund(@PathVariable Integer traderId, @PathVariable Double amount) {
        try {
            return fundTransferService.withdraw(traderId, amount);
        } catch (Exception e) {
            throw ResponseExceptionUtil.getResponseStatusException(e);
        }
    }

}
