package demo.springbatch.service;

public interface MessageSenderServiceInterface {
    public void sendMessage(String routingkey, long orderId);
}
