package com.queworx;

import com.ib.client.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;

public class IBReceiver implements EWrapper {

    private IBDatastore __ibDatastore;

    public IBReceiver(IBDatastore ibDatastore) {
        __ibDatastore = ibDatastore;
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
        if (field != 1 && field != 2 && field != 4)
            return;

        Tick tick = __ibDatastore.getLatestTick(tickerId);

        if (field == 1)
            tick.bid = price;
        else if (field == 2)
            tick.ask = price;
        else if (field == 4)
            tick.last = price;

        tick.modified_at = System.currentTimeMillis();
    }

    @Override
    public void tickSize(int tickerId, int field, int size) {
        if (field != 0 && field != 3 && field != 8)
            return;

        Tick tick = __ibDatastore.getLatestTick(tickerId);

        if (field == 0)
            tick.bidSize = size * 100;
        else if (field == 3)
            tick.askSize = size * 100;
        else if (field == 8)
            tick.lastTradeVolume = size;

        tick.modified_at = System.currentTimeMillis();
    }

    @Override
    public void tickOptionComputation(int i, int i1, double v, double v1, double v2, double v3, double v4, double v5, double v6, double v7) {

    }

    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {

        // https://interactivebrokers.github.io/tws-api/tick_types.html, 46 is shortable
        if(tickType == 46)
        {
            Tick tick = __ibDatastore.getLatestTick(tickerId);
            //3.0 at least 1000 shares shortable
            if(value > 2.5)
                tick.shortable = true;
            // 2.0 IB is trying to locate shares
            else if(value > 1.5)
                tick.shortable = false;
            // >0, not shortable
            else
                tick.shortable = false;
        }
    }

    @Override
    public void tickString(int i, int i1, String s) {

    }

    @Override
    public void tickEFP(int i, int i1, double v, String s, double v1, int i2, String s1, double v2, double v3) {

    }

    @Override
    public void orderStatus(int orderId, String status, double filled, double remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
        /**
         * Here we can check on how our order did. If it partially filled, we might want to resubmit at a different price.
         * We might want to update our budget, so that we don't trade any more positions. Etc. All of this is a bit
         * beyond the scope of this tutorial.
         */
    }

    @Override
    public void openOrder(int i, Contract contract, Order order, OrderState orderState) {

    }

    @Override
    public void openOrderEnd() {

    }

    @Override
    public void updateAccountValue(String s, String s1, String s2, String s3) {

    }

    @Override
    public void updatePortfolio(Contract contract, double v, double v1, double v2, double v3, double v4, double v5, String s) {

    }

    @Override
    public void updateAccountTime(String s) {

    }

    @Override
    public void accountDownloadEnd(String s) {

    }

    @Override
    public void nextValidId(int id) {
        __ibDatastore.nextValidId = id;
    }

    @Override
    public void contractDetails(int i, ContractDetails contractDetails) {

    }

    @Override
    public void bondContractDetails(int i, ContractDetails contractDetails) {

    }

    @Override
    public void contractDetailsEnd(int i) {

    }

    @Override
    public void execDetails(int i, Contract contract, Execution execution) {

    }

    @Override
    public void execDetailsEnd(int i) {

    }

    @Override
    public void updateMktDepth(int i, int i1, int i2, int i3, double v, int i4) {

    }

    @Override
    public void updateMktDepthL2(int i, int i1, String s, int i2, int i3, double v, int i4) {

    }

    @Override
    public void updateNewsBulletin(int i, int i1, String s, String s1) {

    }

    @Override
    public void managedAccounts(String s) {

    }

    @Override
    public void receiveFA(int i, String s) {

    }

    @Override
    public void historicalData(int i, String s, double v, double v1, double v2, double v3, int i1, int i2, double v4, boolean b) {

    }

    @Override
    public void scannerParameters(String s) {

    }

    @Override
    public void scannerData(int i, int i1, ContractDetails contractDetails, String s, String s1, String s2, String s3) {

    }

    @Override
    public void scannerDataEnd(int i) {

    }

    @Override
    public void realtimeBar(int i, long l, double v, double v1, double v2, double v3, long l1, double v4, int i1) {

    }

    @Override
    public void currentTime(long l) {

    }

    @Override
    public void fundamentalData(int i, String s) {

    }

    @Override
    public void deltaNeutralValidation(int i, DeltaNeutralContract deltaNeutralContract) {

    }

    @Override
    public void tickSnapshotEnd(int i) {

    }

    @Override
    public void marketDataType(int i, int i1) {

    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {

    }

    @Override
    public void position(String s, Contract contract, double v, double v1) {

    }

    @Override
    public void positionEnd() {

    }

    @Override
    public void accountSummary(int i, String s, String s1, String s2, String s3) {

    }

    @Override
    public void accountSummaryEnd(int i) {

    }

    @Override
    public void verifyMessageAPI(String s) {

    }

    @Override
    public void verifyCompleted(boolean b, String s) {

    }

    @Override
    public void verifyAndAuthMessageAPI(String s, String s1) {

    }

    @Override
    public void verifyAndAuthCompleted(boolean b, String s) {

    }

    @Override
    public void displayGroupList(int i, String s) {

    }

    @Override
    public void displayGroupUpdated(int i, String s) {

    }

    @Override
    public void error(Exception e) {
        System.err.println("IBBroker error " + e.getMessage());
    }

    @Override
    public void error(String str) {
        System.err.println("IBBroker error " + str);
    }

    @Override
    public void error(int id, int errorCode, String errorMsg)
    {
        System.err.println("IB Error: " + id + " " + errorCode + " " + errorMsg);

        // Some data farm is connected/disconnected, I just need to ignore it
        if (Arrays.asList(2103, 2104, 2105, 2106, 2108).contains(errorCode))
            return;

        if (errorCode == 200) {
            System.err.println("IBBroker error: " + id + "," + errorCode + "," + errorMsg);
            return;
        }

        // Order cancelled, can't modify a filled order, order held while securities are located, cancel attempted
        // when order is not in a cancellable state, Unable to modify this order as its still being processed
        if (Arrays.asList(201, 202, 104, 404, 161, 2102).contains(errorCode))
            return;

        if (errorMsg == "" || errorMsg == null)
            return;

        // Happens when IB kills the order for whatever reason, and then I try to modify it
        if (errorCode == 103)
            return;

        // Duplicate ticker id
        if (errorCode == 322 && errorMsg.contains("Duplicate ticker id"))
            return;

        // Requested market data is not subscribed, not sure why I get this - maybe 2
        // seconds isn't enough to subscribe and unsubscribe
        if (errorCode == 354 || errorCode == 300)
            return;

        // ib disconnected, ib connection restored
        if (errorCode == 1100 || errorCode == 1102)
            return;

        if (errorCode == 0 && errorMsg.contains("Warning: Approaching max rate of 50 messages per second")) {
            System.out.println("IB approaching max rate of messages per second");
            return;
        }

        // Final fail
        System.exit(0);
    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void connectAck() {

    }

    @Override
    public void positionMulti(int i, String s, String s1, Contract contract, double v, double v1) {

    }

    @Override
    public void positionMultiEnd(int i) {

    }

    @Override
    public void accountUpdateMulti(int i, String s, String s1, String s2, String s3, String s4) {

    }

    @Override
    public void accountUpdateMultiEnd(int i) {

    }

    @Override
    public void securityDefinitionOptionalParameter(int i, String s, int i1, String s1, String s2, Set<String> set, Set<Double> set1) {

    }

    @Override
    public void securityDefinitionOptionalParameterEnd(int i) {

    }

    @Override
    public void softDollarTiers(int i, SoftDollarTier[] softDollarTiers) {

    }
}
