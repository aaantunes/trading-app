package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import ca.jrvs.apps.trading.util.ParametersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterService {

    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

    private TraderDao traderDao;
    private AccountDao accountDao;
    private PositionDao positionDao;
    private SecurityOrderDao securityOrderDao;

    @Autowired
    public RegisterService(TraderDao traderDao, AccountDao accountDao,
                           PositionDao positionDao, SecurityOrderDao securityOrderDao) {
        this.traderDao = traderDao;
        this.accountDao = accountDao;
        this.positionDao = positionDao;
        this.securityOrderDao = securityOrderDao;
    }

    /**
     * Create a new trader and initialize a new account with 0 amount.
     * - validate user input (all fields must be non empty)
     * - create a trader
     * - create an account
     * - create, setup, and return a new traderAccountView
     *
     * @param trader trader info
     * @return traderAccountView
     * @throws ca.jrvs.apps.trading.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException        if unable to retrieve data
     * @throws IllegalArgumentException                           for invalid input
     */
    public TraderAccountView createTraderAndAccount(Trader trader) {
        List<String> fieldsAsNull = ParametersUtil.checkIfNullsInObject(trader);
        if (fieldsAsNull.size() != 1) {
            logger.error("Empty Trader fields: " + fieldsAsNull);
            throw new IllegalArgumentException("Cannot pass trader object with null values");
        }

        Trader newTrader = traderDao.save(trader);
        Account account = new Account();
        account.setAmount(0.0);
        account.setTraderId(newTrader.getId());
        account.setId(newTrader.getId());
        accountDao.save(account);

        TraderAccountView traderAccountView = new TraderAccountView();
        traderAccountView.setTrader(newTrader);
        traderAccountView.setAccount(account);
        return traderAccountView;
    }

    /**
     * A trader can be deleted iff no open position and no cash balance.
     * - validate traderID
     * - get trader account by traderId and check account balance
     * - get positions by accountId and check positions
     * - delete all securityOrders, account, trader (in this order)
     *
     * @param traderId
     * @throws ca.jrvs.apps.trading.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException        if unable to retrieve data
     * @throws IllegalArgumentException                           for invalid input
     */
    public void deleteTraderById(Integer traderId) {
        if (traderId == null || !traderDao.existsById(traderId)) {
            throw new IllegalArgumentException("Cannot pass null traderId");
        }

        Account account = accountDao.findByTraderId(traderId);
        if (account.getAmount() != 0) {
            throw new IllegalArgumentException("Cannot delete Trader due to non-zero amount");
        }
        List<Position> positions = positionDao.findByAccountId(account.getId());
        positions.forEach(position -> {
            if (position.getPosition() != 0) {
                throw new IllegalArgumentException("Cannot delete Trader due to non-zero position");
            }
        });

        securityOrderDao.deleteById(account.getId());
        accountDao.deleteById(account.getId());
        traderDao.deleteById(traderId);

        if (accountDao.existsById(account.getId())) {
            throw new ResourceNotFoundException("Account not deleted");
        }
    }
}
