package com.queworx;

import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReaderSignal;

public class Main {

    public static void main(String[] args) {
        // Signal processing with TWS, we will not be using it
        EReaderSignal readerSignal = new EJavaSignal();

        IBDatastore ibDatastore = new IBDatastore();

        IBReceiver ibReceiver = new IBReceiver(ibDatastore);
        EClientSocket clientSocket = new EClientSocket(ibReceiver, readerSignal);
        IBBroker ibBroker = new IBBroker(clientSocket, ibDatastore);

        try
        {
            ibBroker.connect();

            // Wait for nextValidId
            for (int i=0; i<10; i++) {
                if (ibDatastore.nextValidId != null)
                    break;

                Thread.sleep(1000);
            }

            if (ibDatastore.nextValidId == null)
                throw new Exception("Didn't get a valid id from IB");

            // From here you can add the logic of your application
        }
        catch(Exception ex)
        {
            System.err.println(ex);
        }
        finally
        {
            ibBroker.disconnect();
            System.exit(0);
        }
    }
}
