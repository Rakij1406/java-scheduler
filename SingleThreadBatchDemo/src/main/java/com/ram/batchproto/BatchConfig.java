package com.ram.batchproto;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.ram.batchproto.model.PaymentError;
import com.ram.batchproto.model.PaymentStatus;
import com.ram.batchproto.processor.MyItemProcessor;




@Configuration
@EnableBatchProcessing
@ComponentScan
@EnableAutoConfiguration
// file that contains the properties
@PropertySource(value = {"file:/home/qison/Projects/SpringBatch/src/main/resources/application.properties"}, ignoreResourceNotFound = false)
public class BatchConfig{
    
    private static SimpleDateFormat dateFormatGmt = new SimpleDateFormat();
 	public static final String TIMEZONE = "GMT";
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    
	/*Load the properties
	*/
	@Value("${database.driver}")
	private String databaseDriver;
	@Value("${database.url}")
	private String databaseUrl;
	@Value("${database.username}")
	private String databaseUsername;
	@Value("${database.password}")
	private String databasePassword;
	  /**
     * As data source we use an external database
     *
     * @return
     */

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(databaseDriver);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUsername);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }


    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setPackagesToScan(new String[] {"com.ram.batchproto.model"});
        lef.setDataSource(dataSource());
        lef.setJpaVendorAdapter(jpaVendorAdapter());
        lef.setJpaProperties(new Properties());
        return lef;
    }


    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.MYSQL);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(false);
        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
        return jpaVendorAdapter;
    }
	
	
	
    /**
     * JPAPagingItemReader
     *
     * @return
     */
    @Bean
    public ItemReader<PaymentError> reader() throws Exception {
            String jpqlQuery = "select pe from PaymentError pe  where pe.id<5000";
            System.out.println("**********reader****************");
    		JpaPagingItemReader<PaymentError> reader = new JpaPagingItemReader<PaymentError>();
    		reader.setQueryString(jpqlQuery);
    		reader.setEntityManagerFactory(entityManagerFactory().getObject());
    		reader.setPageSize(100);
    		reader.afterPropertiesSet();
    		reader.setSaveState(true);
    		return reader;
    }

    /**
     * The ItemProcessor is called after a new record is read and it allows the developer
     * to transform the data read
     * In our example it simply read and write the data
     *
     * @return
     */
    @Bean
    public ItemProcessor<PaymentError, PaymentStatus> processor() {
    	System.out.println("***********processor*****************");
        return new MyItemProcessor();
    }

    /**
     * Nothing special here a simple JpaItemWriter
     * @return
     */
    @Bean
    public ItemWriter<PaymentStatus> writer() {
    	System.out.println("**********writer****************");
        JpaItemWriter<PaymentStatus>  writer = new JpaItemWriter<PaymentStatus>();
        writer.setEntityManagerFactory(entityManagerFactory().getObject());
        return writer;
    }

    /**
     * This method declare the steps that the batch has to follow
     *
     * @param jobs
     * @param s1
     * @return
     * @throws Exception 
     */
    @Bean
    public Job importJob( JobBuilderFactory jobs,StepBuilderFactory stepBuilderFactory) throws Exception {

        return jobs.get("importJob")
                .incrementer(new RunIdIncrementer()) // because a spring config bug, this incrementer is not really useful
                .flow(step1(stepBuilderFactory,reader(),writer(), processor()))
                .end()
                .build();
    }


    /**
     * Step
     * We declare that every 1000 lines processed the data has to be committed
     *
     * @param stepBuilderFactory
     * @param reader
     * @param writer
     * @param processor
     * @return
     */

    @Bean
    @Autowired
    public Step step1(StepBuilderFactory stepBuilderFactory,ItemReader<PaymentError> reader,
                      ItemWriter<PaymentStatus> writer, ItemProcessor<PaymentError,PaymentStatus> processor) {
        return stepBuilderFactory.get("step1")
                .<PaymentError,PaymentStatus>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    
   
  

   
    @Bean
    @Autowired
	public JobExecutionListener listener() {
		return new BatchJobCompletionListener();
	}
  
    
    
   public void  insertRecords(){
    	int entityCount = 50;
    	int batchSize = 25;
    	 
    	EntityManager entityManager = null;
    	EntityTransaction transaction = null;
    	 
    	try {
    	    entityManager = ((EntityManagerFactory) entityManagerFactory())
    	        .createEntityManager();
    	 
    	    transaction = entityManager.getTransaction();
    	    transaction.begin();
    	 
    	    for ( int i = 0; i < entityCount; ++i ) {
    	        if ( i > 0 && i % batchSize == 0 ) {
    	            entityManager.flush();
    	            entityManager.clear();
    	 
    	            transaction.commit();
    	            transaction.begin();
    	        }
    	        PaymentError paymentError=new PaymentError();
    	        paymentError.setRequestId("req_"+i);
    	        paymentError.setUpdatedDate(getGMTCurrentTime());
    	        paymentError.setCreatedDate(getGMTCurrentTime());
    	        paymentError.setVersion(i);
    	        paymentError.setPayload("{{sample}}");
    	        paymentError.setErrorCode("errorCode_"+i);
    	        paymentError.setErrorMsg("errorMsg_"+i);
    	        paymentError.setProcessed(true);
    	        paymentError.setRetryable(true);
    	        paymentError.setServiceName("TestAPI_"+i);
    	        
    	        entityManager.persist( paymentError );
    	    }
    	 
    	    transaction.commit();
    	} catch (RuntimeException e) {
    	    if ( transaction != null && 
    	         transaction.isActive()) {
    	        transaction.rollback();
    	    }
    	    throw e;
    	} finally {
    	    if (entityManager != null) {
    	        entityManager.close();
    	    }
    	}
    }
   public static Timestamp getGMTCurrentTime() {

		dateFormatGmt.applyPattern(DATE_FORMAT);
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
		return Timestamp.valueOf(dateFormatGmt.format(System.currentTimeMillis()));

	}

}
