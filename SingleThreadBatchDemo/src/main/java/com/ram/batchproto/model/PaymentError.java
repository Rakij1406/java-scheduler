package com.ram.batchproto.model;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.TableGenerator;

@Entity
@Table(name="payment_error")
public class PaymentError {
	@GeneratedValue(generator = "tableGen", strategy = GenerationType.TABLE)
	@GenericGenerator(name = "tableGen", strategy = "org.hibernate.id.enhanced.TableGenerator", parameters = {
	            @Parameter(name = TableGenerator.CONFIG_PREFER_SEGMENT_PER_ENTITY, value = "true"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
	    })
	@Id
	@Column
	private long id;
	@Column(name="request_id")
    private String 	requestId;
    
    @Column(name="created_date")
    private Timestamp createdDate;
    
    
    @Column(name="updated_date")
    private Timestamp updatedDate;
    
    @Column
	private long version;
    
    @Column
	private String payload;
    @Column(name="error_msg")
	private String errorMsg;
    
    @Column(name="error_code")
	private String errorCode;
    
    @Column(name="is_retryable")
	private boolean retryable;
    
    @Column(name="is_processed")
	private boolean  processed;
    
    @Column(name="schedular_picup_ts")
    private Timestamp schedularPicupTs;
    
    
    @Column(name="service_name")
	private String serviceName;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getRequestId() {
		return requestId;
	}


	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}


	public Timestamp getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Timestamp timestamp) {
		this.createdDate = timestamp;
	}


	public Timestamp getUpdatedDate() {
		return updatedDate;
	}


	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}


	public long getVersion() {
		return version;
	}


	public void setVersion(long version) {
		this.version = version;
	}


	public String getPayload() {
		return payload;
	}


	public void setPayload(String payload) {
		this.payload = payload;
	}


	public String getErrorMsg() {
		return errorMsg;
	}


	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public String getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


	public boolean isRetryable() {
		return retryable;
	}


	public void setRetryable(boolean retryable) {
		this.retryable = retryable;
	}


	public boolean isProcessed() {
		return processed;
	}


	public void setProcessed(boolean processed) {
		this.processed = processed;
	}


	public Timestamp getSchedularPicupTs() {
		return schedularPicupTs;
	}


	public void setSchedularPicupTs(Timestamp schedularPicupTs) {
		this.schedularPicupTs = schedularPicupTs;
	}


	public String getServiceName() {
		return serviceName;
	}


	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
    
    
}
