//package com.example.meetup_study.common.replication;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import org.springframework.transaction.support.TransactionSynchronizationManager;
//
//@RequiredArgsConstructor
//public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {
//
//    private final DataSourceKey dataSourceKey;
//
//    @Override
//    protected Object determineCurrentLookupKey() {
//        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
//        if (isReadOnly) {
//            return dataSourceKey.getDefaultSlaveKey();
//        } else {
//            return dataSourceKey.getMasterKey();
//        }
//    }
//}