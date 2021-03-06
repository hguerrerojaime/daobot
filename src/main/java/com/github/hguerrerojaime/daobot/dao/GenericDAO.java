package com.github.hguerrerojaime.daobot.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;

import com.github.hguerrerojaime.daobot.core.AbstractCB;
import com.github.hguerrerojaime.daobot.core.DynamicQueryBuilder;
import com.github.hguerrerojaime.daobot.core.ResultSet;
import com.github.hguerrerojaime.daobot.eo.EntityObject;

 public interface GenericDAO {

    static final int MAX_RECORDS = 500;

    /* Read only methods */

    /**
     * Fetches a record with the given id
     * 
     * @param id
     * @return The Instance
     */
    <T extends EntityObject<K>, K extends Serializable> T get(
            Class<T> entityClass, K id);

    /**
     * Same as get(id) != null; Checks if a record exists with the given id
     * 
     * @param id
     * @return True if instance is not null, False otherwise
     */
    <T extends EntityObject<K>, K extends Serializable> boolean exists(
            Class<T> entityClass, K id);

    /**
     * Same as findBy(new CB()); Fetches the first record it
     * finds, not very useful
     * 
     * @return The Instance
     */
    <T extends EntityObject<K>, K extends Serializable> T find(
            Class<T> entityClass);

    /**
     * Finds the first record matching the given criteria
     * 
     * @param criteriaBuilder
     *            - The criteria to be met (Filters, Orders)
     * @return The Instance
     */
    <T extends EntityObject<K>, K extends Serializable> T find(
            Class<T> entityClass, AbstractCB criteriaBuilder);
    
    /**
     * Same as findAll(0,0); Fetches all the records
     * 
     * @return JPAResulset containing the fetched records and the total record
     *         count
     */
    <T extends EntityObject<K>, K extends Serializable> ResultSet<T> findAll(
            Class<T> entityClass);

    /**
     * Same as findAll(new CB(),max,offset); Fetches all the
     * records paginating the results
     * 
     * @param max
     *            - The maximum number of records to be fetched
     * @param offset
     *            - The first record position to be fetched
     * @return JPAResulset containing the fetched records and the total record
     *         count
     */
     <T extends EntityObject<K>, K extends Serializable> ResultSet<T> findAll(
            Class<T> entityClass, int max, int offset);
    
    /**
     * Same as findAllBy(criteriaBuilder,0,0); Fetches ALL the records matching
     * the given criteria
     * 
     * @param criteriaBuilder
     *            - The criteria to be met (Filters, Orders)
     * @return JPAResulset containing the fetched records and the total record
     *         count
     */
     <T extends EntityObject<K>, K extends Serializable> ResultSet<T> findAll(
            Class<T> entityClass, AbstractCB criteriaBuilder);

    /**
     * Fetches all the records matching the given criteria paginating the
     * results
     * 
     * @param criteriaBuilder
     *            - The criteria to be met (Filters, Orders)
     * @param max
     *            - The maximum number of records to be fetched
     * @param offset
     *            - The first record position to be fetched
     * @return JPAResulset containing the fetched records and the total record
     *         count
     */
     <T extends EntityObject<K>, K extends Serializable> ResultSet<T> findAll(
            Class<T> entityClass, AbstractCB criteriaBuilder, int max, int offset);
    
    /**
     * Same as countBy(new JPAcriteria()); Fetches the count of all the
     * records
     * 
     * @return the record count
     */
     <T extends EntityObject<K>, K extends Serializable> Long count(
            Class<T> entityClass);

    /**
     * Fetches the count of all the records matching the given criteria
     * 
     * @param criteria
     *            - The criteria to be met
     * @return
     */
     <T extends EntityObject<K>, K extends Serializable> Long count(
            Class<T> entityClass, AbstractCB criteria);


    /* Write methods */

    /**
     * Same as save(instance,false); Calls method save without flushing the
     * session
     * 
     * @param instance
     * @return
     */
     <T extends EntityObject<K>, K extends Serializable> T save(
            T instance);
     

    /**
     * Saves the instance and optionally flushes (calls method flush()) the
     * session If the id of the instance is null then it will persist (insert)
     * the instance, Else it will merge (update) the instance
     * 
     * @param instance
     *            - The instance to be saved into the database
     * @param flush
     *            - If true will call the flush method
     * @return
     */
     <T extends EntityObject<K>, K extends Serializable> T save(
            T instance, boolean flush);

    /**
     * Flushes the session
     */
     void flush();

    /**
     * Deletes de instance from the database
     * 
     * @param instance
     *            - The instance to be deleted
     * @return id of the instance
     */
     <T extends EntityObject<K>, K extends Serializable> K delete(
            T instance);
     
     
     <T extends EntityObject<K>, K extends Serializable> DynamicQueryBuilder<T,K> from(Class<T> entityClass);

    /* Extras */

    /**
     * @return the EntityManager
     */
     EntityManager getEntityManager();

}
