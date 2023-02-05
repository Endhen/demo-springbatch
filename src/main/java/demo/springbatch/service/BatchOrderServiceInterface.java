package demo.springbatch.service;

import demo.springbatch.enums.OrderStatus;

public interface BatchOrderServiceInterface {
    public void updateStatus(long id, OrderStatus status);    
}