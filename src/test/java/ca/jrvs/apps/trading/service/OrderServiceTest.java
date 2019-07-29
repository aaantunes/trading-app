package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.*;
import ca.jrvs.apps.trading.model.dto.MarketOrderDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    //capture parameters when calling securityOrderDao.save()
    @Captor
    ArgumentCaptor<SecurityOrder> captorSecurityOrder;

    //mock all dependencies
    @Mock
    private AccountDao accountDao;
    @Mock
    private SecurityOrderDao securityOrderDao;
    @Mock
    private QuoteDao quoteDao;
    @Mock
    private PositionDao positionDao;

    //inject mocked dependencies to the testing class via constructor
    @InjectMocks
    private OrderService orderService;

    //setup test data
    private MarketOrderDto orderDto;

    @Before
    public void setUp() throws Exception {
        orderDto = new MarketOrderDto();
        orderDto.setAccountId(1);
        orderDto.setSize(1);
        orderDto.setTicker("AAPL");
    }

    @Test
    public void executeMarketOrderBuyHappy() {
        when(quoteDao.existsById(orderDto.getTicker())).thenReturn(true);

        Quote quote = new Quote();
        quote.setAskSize(10);
        quote.setAskPrice(100.00);
        when(quoteDao.findById(orderDto.getTicker())).thenReturn(quote);

        Account account = new Account();
        account.setAmount(100.00);
        account.setId(orderDto.getAccountId());
        when(accountDao.findById(orderDto.getAccountId())).thenReturn(account);

        orderService.executeMarketOrder(orderDto);
        verify(securityOrderDao).save(captorSecurityOrder.capture());
        SecurityOrder captorOrder = captorSecurityOrder.getValue();
        assertEquals(OrderStatus.FILLED, captorOrder.getStatus());
    }

    @Test
    public void executeMarketOrderBuySad() {
        when(quoteDao.existsById(orderDto.getTicker())).thenReturn(true);

        Quote quote = new Quote();
        quote.setAskSize(10);
        quote.setAskPrice(100.00);
        when(quoteDao.findById(orderDto.getTicker())).thenReturn(quote);

        Account account = new Account();
        account.setAmount(10.00);
        account.setId(orderDto.getAccountId());
        when(accountDao.findById(orderDto.getAccountId())).thenReturn(account);

        orderService.executeMarketOrder(orderDto);
        verify(securityOrderDao).save(captorSecurityOrder.capture());
        SecurityOrder captorOrder = captorSecurityOrder.getValue();
        assertEquals(OrderStatus.CANCELLED, captorOrder.getStatus());
    }


    @Test
    public void executeMarketOrderSellHappy() {
        orderDto.setSize(-1);
        when(quoteDao.existsById(orderDto.getTicker())).thenReturn(true);

        Quote quote = new Quote();
        quote.setAskSize(10);
        quote.setAskPrice(100.00);
        when(quoteDao.findById(orderDto.getTicker())).thenReturn(quote);

        Account account = new Account();
        account.setAmount(100.00);
        account.setId(orderDto.getAccountId());
        account.setTraderId(orderDto.getAccountId());
        when(accountDao.findById(orderDto.getAccountId())).thenReturn(account);

        Position position = new Position();
        position.setAccountId(account.getId());
        position.setTicker(quote.getTicker());
        position.setPosition(1);
        when(positionDao.findByTickerAndAccount(orderDto.getTicker(), orderDto.getAccountId())).thenReturn(position);

        orderService.executeMarketOrder(orderDto);
        verify(securityOrderDao).save(captorSecurityOrder.capture());
        SecurityOrder captorOrder = captorSecurityOrder.getValue();
        assertEquals(OrderStatus.FILLED, captorOrder.getStatus());
    }

    @Test
    public void executeMarketOrderSellSad() {
        orderDto.setSize(-10);
        when(quoteDao.existsById(orderDto.getTicker())).thenReturn(true);

        Quote quote = new Quote();
        quote.setAskSize(10);
        quote.setAskPrice(100.00);
        when(quoteDao.findById(orderDto.getTicker())).thenReturn(quote);

        Account account = new Account();
        account.setAmount(100.00);
        account.setId(orderDto.getAccountId());
        account.setTraderId(orderDto.getAccountId());
        when(accountDao.findById(orderDto.getAccountId())).thenReturn(account);

        Position position = new Position();
        position.setAccountId(account.getId());
        position.setTicker(quote.getTicker());
        position.setPosition(5);
        when(positionDao.findByTickerAndAccount(orderDto.getTicker(), orderDto.getAccountId())).thenReturn(position);

        orderService.executeMarketOrder(orderDto);
        verify(securityOrderDao).save(captorSecurityOrder.capture());
        SecurityOrder captorOrder = captorSecurityOrder.getValue();
        assertEquals(OrderStatus.CANCELLED, captorOrder.getStatus());
    }

}