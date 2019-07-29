package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.domain.*;
import ca.jrvs.apps.trading.model.dto.MarketOrderDto;
import ca.jrvs.apps.trading.util.ParametersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Math.abs;

@Service
@Transactional
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private AccountDao accountDao;
    private SecurityOrderDao securityOrderDao;
    private QuoteDao quoteDao;
    private PositionDao positionDao;

    @Autowired
    public OrderService(AccountDao accountDao, SecurityOrderDao securityOrderDao,
                        QuoteDao quoteDao, PositionDao positionDao) {
        this.accountDao = accountDao;
        this.securityOrderDao = securityOrderDao;
        this.quoteDao = quoteDao;
        this.positionDao = positionDao;
    }

    /**
     * Execute a market order
     * <p>
     * - validate the order (e.g. size, and ticker)
     * - Create a securityOrder (for security_order table)
     * - Handle buy or sell order
     * - buy order : check account balance
     * - sell order: check position for the ticker/symbol
     * - (please don't forget to update securityOrder.status)
     * - Save and return securityOrder
     * <p>
     * NOTE: you will need to some helper methods (protected or private)
     *
     * @param orderDto market order
     * @return SecurityOrder from security_order table
     * @throws org.springframework.dao.DataAccessException if unable to get data from DAO
     * @throws IllegalArgumentException                    for invalid input
     */
    public SecurityOrder executeMarketOrder(MarketOrderDto orderDto) {
        List<String> fieldsAsNull = ParametersUtil.checkIfNullsInObject(orderDto);
        if (orderDto == null || fieldsAsNull.size() != 0 || orderDto.getSize() == 0) {
            throw new IllegalArgumentException("Cannot pass null orderDto");
        }
        if (!quoteDao.existsById(orderDto.getTicker())) {
            throw new IllegalArgumentException("Enter a valid ticker");
        }

        SecurityOrder securityOrder = new SecurityOrder();
        Account account = accountDao.findById(orderDto.getAccountId());
        Quote quote = quoteDao.findById(orderDto.getTicker());

        securityOrder.setAccountId(account.getId());
        securityOrder.setTicker(orderDto.getTicker());
        securityOrder.setSize(orderDto.getSize());
        securityOrder.setPrice(quote.getAskPrice()); //should the price be total amount payed during order but edwards makes no sense
        securityOrder.setStatus(OrderStatus.PENDING);

        if (securityOrder.getSize() > 0) {
            securityOrder.setStatus(buyStock(account, quote, orderDto));
        } else {
            securityOrder.setStatus(sellStock(account, quote, orderDto));
        }
        accountDao.updateAmountById(account.getId(), account.getAmount());
        return securityOrderDao.save(securityOrder);
    }

    private OrderStatus buyStock(Account account, Quote quote, MarketOrderDto orderDto) {
        double cost = abs(orderDto.getSize()) * quote.getAskPrice();
        if (account.getAmount() >= cost) {
            account.setAmount(account.getAmount() - cost);
            return OrderStatus.FILLED;
        } else {
            return OrderStatus.CANCELLED;
        }
    }

    private OrderStatus sellStock(Account account, Quote quote, MarketOrderDto orderDto) {
        double price = abs(orderDto.getSize()) * quote.getAskPrice();
        Position position;
        try {
            position = positionDao.findByTickerAndAccount(orderDto.getTicker(), orderDto.getAccountId());
        } catch (Exception e) {
            throw new ResourceNotFoundException("No such position for ticker");
        }

        if (position.getPosition() >= abs(orderDto.getSize())) {
            account.setAmount(account.getAmount() + price);
            return OrderStatus.FILLED;
        } else {
            return OrderStatus.CANCELLED;
        }
    }
}
