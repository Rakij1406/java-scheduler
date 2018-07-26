package com.ram.batchproto.processor;

import org.springframework.batch.item.ItemProcessor;

import com.ram.batchproto.model.PaymentError;
import com.ram.batchproto.model.PaymentStatus;

public class MyItemProcessor implements ItemProcessor<PaymentError, PaymentStatus> {
    @Override
	public PaymentStatus process(final PaymentError item) throws Exception {
    	PaymentStatus paymentStatus=new PaymentStatus();
    	paymentStatus.setPaymentErrorId(item.getId());
    	paymentStatus.setErrorMsg(item.getErrorMsg()+"_Processed");
    	paymentStatus.setErrorCode(item.getErrorCode()+"_Processed");
    	if(item.getId()%2==0){
    		 paymentStatus.setRetryable(false);	
    	}
        paymentStatus.setProcessed(true);
        System.out.println("Processed :"+item.getId());
		return paymentStatus;
	}
}
