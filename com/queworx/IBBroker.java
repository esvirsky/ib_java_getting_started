package com.queworx;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.Order;

public class IBBroker {
    private EClientSocket __clientSocket;
    private IBDatastore __ibDatastore;

    // You should push these to some config
    private static final int __clientId = 1;
    private static final String __ibAccount = "UA55555"; // This is your IB account

    public IBBroker(EClientSocket clientSocket, IBDatastore ibDatastore) {
        __clientSocket = clientSocket;
        __ibDatastore = ibDatastore;
    }

    public void connect() {
        // ip_address, port, and client ID. Client ID is used to identify the app that connects to TWS, you can
        // have multiple apps connect to one TWS instance
        __clientSocket.eConnect("127.0.0.1",7497, __clientId);
    }

    public void subscribeQuoteData(int tickerId, String symbol, String exchange) {
        Contract contract = __createContract(symbol, exchange);

        // We are asking for additional shortable (236) and fundamental ratio (258) information.
        // The false says that we don't want a snapshot, we want a streaming feed of data.
        // https://interactivebrokers.github.io/tws-api/classIBApi_1_1EClient.html#a7a19258a3a2087c07c1c57b93f659b63
        __clientSocket.reqMktData(tickerId, contract, "236,258", false, null);
    }

    private void createOrder(String symbol, String exchange, int quantity, double price, boolean buy)
    {
        Contract contract = __createContract(symbol, exchange);

        int orderId = __ibDatastore.nextValidId;

        // https://interactivebrokers.github.io/tws-api/classIBApi_1_1Order.html
        Order order = new Order();
        order.clientId(__clientId);
        order.transmit(true);
        order.orderType("LMT");
        order.orderId(orderId);
        order.action(buy ? "BUY" : "SELL");
        order.totalQuantity(quantity);
        order.lmtPrice(price);
        order.account(__ibAccount);
        order.hidden(false);
        order.minQty(100);

        __clientSocket.placeOrder(orderId, contract, order);

        // We can either request the next valid orderId or just increment it
        __ibDatastore.nextValidId++;
    }

    public void disconnect() {
        __clientSocket.eDisconnect();
    }

    protected Contract __createContract(String symbol, String exchange) {
        // https://interactivebrokers.github.io/tws-api/classIBApi_1_1Contract.html
        return new Contract(0, symbol, "STK", null, 0.0d, null,
                null, exchange, "USD", null, null, null,
                "SMART", false, null, null);
    }
}
