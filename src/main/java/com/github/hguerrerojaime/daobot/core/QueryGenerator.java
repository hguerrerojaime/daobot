package com.github.hguerrerojaime.daobot.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.github.hguerrerojaime.daobot.eo.EntityObject;

/**
 * @author Humberto Guerrero Jaime
 *
 * @param <T> the Type of the Entity Object
 * @param <K> the Primary Key for the Entity Object
 */
public class QueryGenerator<T extends EntityObject<K>, K extends Serializable> {

	//
    // instance variables
    //

	private EntityManager entityManager;

	private Class<T> entityClass;
	
	//
    // constructor
    //

	/**
	 * @param entityManager
	 * @param entityClass
	 */
	public QueryGenerator(EntityManager entityManager, Class<T> entityClass) {
		// TODO Auto-generated constructor stub
		this.entityClass = entityClass;
		this.entityManager = entityManager;
	}

	/**
	 * Same as build(true);
	 * @return
	 */
	public DResultSet<T> build() {
		return build(true);
	}

	/**
	 * Same as build(new JPACriteriaBuilder(), autoCount);
	 * @param autoCount
	 * @return
	 */
	public DResultSet<T> build(boolean autoCount) {
		return build(new CB(), autoCount);
	}

	/**
	 * Same as build(new JPACriteriaBuilder(), max, offset, true);
	 * @param max
	 * @param offset
	 * @return
	 */
	public DResultSet<T> build(int max, int offset) {
		return build(new CB(), max, offset, true);
	}

	/**
	 * 
	 * @param max
	 * @param offset
	 * @param autoCount
	 * @return
	 */
	public DResultSet<T> build(int max, int offset, boolean autoCount) {
		return build(new CB(), max, offset, autoCount);
	}

	/**
	 * @param jpaCriteriaBuilder
	 * @param max
	 * @param offset
	 * @return
	 */
	public DResultSet<T> build(CB jpaCriteriaBuilder,
			int max, int offset) {
		return build(jpaCriteriaBuilder, max, offset, true);
	}

	/**
	 * @param jpaCriteriaBuilder
	 * @return
	 */
	public DResultSet<T> build(CB jpaCriteriaBuilder) {
		return build(jpaCriteriaBuilder, true);
	}

	/**
	 * @param jpaCriteriaBuilder
	 * @param autoCount
	 * @return
	 */
	public DResultSet<T> build(CB jpaCriteriaBuilder,
			boolean autoCount) {
		return build(jpaCriteriaBuilder, 0, 0, autoCount);
	}

	/**
	 * @param jpaCriteriaBuilder
	 * @param max
	 * @param offset
	 * @param autoCount
	 * @return
	 */
	public DResultSet<T> build(CB jpaCriteriaBuilder,
			int max, int offset, boolean autoCount) {

		CriteriaQuery<T> criteriaResultQuery = buildCriteriaResultQuery(jpaCriteriaBuilder);

		Long totalCount = 0L;

		if (autoCount) {
			totalCount = getCount(jpaCriteriaBuilder);
		}

		Query resultQuery = entityManager.createQuery(criteriaResultQuery);

		if (max > 0) {
			resultQuery.setMaxResults(max);
		}

		if (offset >= 0) {
			resultQuery.setFirstResult(offset);
		}

		DResultSet<T> queryResult = new DResultSet<T>(resultQuery,
				totalCount);

		return queryResult;
	}
	
	/**
     * @param jpaCriteriaBuilder
     * @param max
     * @param offset
     * @param autoCount
     * @return
     */
    public DResultSet<T> build(JsQLCriteriaQuery jpaCriteriaBuilder,
            int max, int offset, boolean autoCount) {

        CriteriaQuery<T> criteriaResultQuery = buildCriteriaResultQuery(jpaCriteriaBuilder);

        Long totalCount = 0L;

        if (autoCount) {
            totalCount = getCount(jpaCriteriaBuilder);
        }

        Query resultQuery = entityManager.createQuery(criteriaResultQuery);

        if (max > 0) {
            resultQuery.setMaxResults(max);
        }

        if (offset >= 0) {
            resultQuery.setFirstResult(offset);
        }

        DResultSet<T> queryResult = new DResultSet<T>(resultQuery,
                totalCount);

        return queryResult;
    }

	

	/**
	 * @return
	 */
	public Long getCount() {
		return getCount(new FB());
	}

	/**
	 * @param jpaCriteriaBuilder
	 * @return
	 */
	public Long getCount(FB jpaFilterBuilder) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Long> criteriaCountQuery = criteriaBuilder
				.createQuery(Long.class);

		Root<T> eoRoot = criteriaCountQuery.from(entityClass);

		criteriaCountQuery.select(criteriaBuilder.count(eoRoot));

		Predicate filters = buildAndEncapsulateFilters(
				jpaFilterBuilder.build(), criteriaBuilder, eoRoot);

		criteriaCountQuery.where(filters);

		Long totalCount = entityManager.createQuery(criteriaCountQuery)
				.getSingleResult();

		return totalCount;
	}
	
	/**
     * @param jpaCriteriaBuilder
     * @return
     */
    public Long getCount(JsQLFilterQuery jpaFilterBuilder) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> criteriaCountQuery = criteriaBuilder
                .createQuery(Long.class);

        Root<T> eoRoot = criteriaCountQuery.from(entityClass);

        criteriaCountQuery.select(criteriaBuilder.count(eoRoot));

        Predicate filters = buildAndEncapsulateFilters(
                jpaFilterBuilder.build(), criteriaBuilder, eoRoot);

        criteriaCountQuery.where(filters);

        Long totalCount = entityManager.createQuery(criteriaCountQuery)
                .getSingleResult();

        return totalCount;
    }

	
	/**
	 * @param jpaCriteriaBuilder
	 * @return
	 */
	private CriteriaQuery<T> buildCriteriaResultQuery(
			CB jpaCriteriaBuilder) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> criteriaResultQuery = criteriaBuilder
				.createQuery(entityClass);

		Root<T> entityObjectRoot = criteriaResultQuery.from(entityClass);

		criteriaResultQuery.select(entityObjectRoot);

		Predicate filters = buildAndEncapsulateFilters(
				jpaCriteriaBuilder.build(), criteriaBuilder, entityObjectRoot);

		criteriaResultQuery.where(filters);

		List<QuerySort> sortList = jpaCriteriaBuilder.getSortList();

		if (!sortList.isEmpty()) {

			List<Order> orderList = buildOrder(sortList, entityObjectRoot,
					criteriaBuilder);

			criteriaResultQuery.orderBy(orderList);

		}

		return criteriaResultQuery;
	}
	
	/**
     * @param jpaCriteriaBuilder
     * @return
     */
    private CriteriaQuery<T> buildCriteriaResultQuery(
            JsQLCriteriaQuery jpaCriteriaBuilder) {
        
        FilterGroup filterGroup = jpaCriteriaBuilder.build();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> criteriaResultQuery = criteriaBuilder
                .createQuery(entityClass);

        Root<T> entityObjectRoot = criteriaResultQuery.from(entityClass);

        criteriaResultQuery.select(entityObjectRoot);

        Predicate filters = buildAndEncapsulateFilters(
                filterGroup, criteriaBuilder, entityObjectRoot);

        criteriaResultQuery.where(filters);

        List<QuerySort> sortList = jpaCriteriaBuilder.getSortList();

        if (!sortList.isEmpty()) {

            List<Order> orderList = buildOrder(sortList, entityObjectRoot,
                    criteriaBuilder);

            criteriaResultQuery.orderBy(orderList);

        }

        return criteriaResultQuery;
    }
	
	/**
	 * @param fg
	 * @param criteriaBuilder
	 * @param eoRoot
	 * @return
	 */
	private Predicate buildAndEncapsulateFilters(FilterGroup fg,
			CriteriaBuilder criteriaBuilder, Root<T> eoRoot) {
		
		QueryFilterGenerator<T, K> queryFilterBuilder =
				new QueryFilterGenerator<T,K>(
						eoRoot,entityClass, criteriaBuilder
				);
		
		return queryFilterBuilder.buildAndEncapsulateFilters(fg);
	
	}
		
	private List<Order> buildOrder(List<QuerySort> sortList, Root<T> eoRoot,
			CriteriaBuilder criteriaBuilder) {

		List<Order> orderList = new ArrayList<Order>();

		for (QuerySort sort : sortList) {

			if (sort.getOrder().equals(DOrder.ASC)) {

				orderList.add(criteriaBuilder.asc(eoRoot.get(sort.getSort())));

			} else if (sort.getOrder().equals(DOrder.DESC)) {

				orderList.add(criteriaBuilder.desc(eoRoot.get(sort.getSort())));

			}

		}

		return orderList;

	}

}