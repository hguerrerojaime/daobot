package com.github.hguerrerojaime.daobot.core.builders.predicate

import com.github.hguerrerojaime.daobot.core.ConditionFilter
import com.github.hguerrerojaime.daobot.core.ConditionFilterType
import com.github.hguerrerojaime.daobot.integration.eo.BookEO

import static com.github.hguerrerojaime.daobot.helpers.ExpressionBuilderHelper.path;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;

import spock.lang.Specification;

class GePredicateBuilderSpec extends Specification {

	Path mockPath
	CriteriaBuilder criteriaBuilder;


	def setup() {

		mockPath = Mock()
		criteriaBuilder = Mock()

	}


	def "test Build with all expression arguments"() {

		given: "An Equal PredicateBuilder instance"

			PredicateBuilder predicateBuilder = new GePredicateBuilder()

		and: "A condition filter with 3 expression arguments"

			ConditionFilter conditionFilter =
					new ConditionFilter(
							ConditionFilterType.GE,
							path("releaseDate"),
							path("releaseDate")
					)

		when: "Executing build predicate"

			predicateBuilder.build(conditionFilter,criteriaBuilder,mockPath,BookEO)

		then: "criteriaBuilder.equal has executed once"

			1 * criteriaBuilder.greaterThanOrEqualTo(_,_)

	}

	def "test Build with path expression argument and parameter values"() {

		given: "An Equal PredicateBuilder instance"

			PredicateBuilder predicateBuilder = new GePredicateBuilder()

		and: "A condition filter with 3 expression arguments"

			ConditionFilter conditionFilter =
					new ConditionFilter(
							ConditionFilterType.GE,
							path("releaseDate"),
							new Date()
					)

		when: "Executing build predicate"

			predicateBuilder.build(conditionFilter,criteriaBuilder,mockPath,BookEO)

		then: "criteriaBuilder.equal has executed once"

			1 * criteriaBuilder.greaterThanOrEqualTo(_,_)

	}

}
