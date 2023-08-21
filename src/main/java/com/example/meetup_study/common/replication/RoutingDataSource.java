package com.example.meetup_study.common.replication;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;


public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if (isReadOnly) {
//            System.out.println("slave(replica) db server 요청");
            return "replica";
        }else{
//        System.out.println("master(source) db server  요청");
            return "source";
        }
    }

}
