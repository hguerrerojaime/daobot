package com.github.hguerrerojaime.daobot.core.builders.predicate

import com.github.hguerrerojaime.daobot.core.ConditionFilter
import com.github.hguerrerojaime.daobot.core.ConditionFilterType
import com.github.hguerrerojaime.daobot.integration.eo.BookEO
import spock.lang.Specification

import static com.github.hguerrerojaime.daobot.helpers.ExpressionBuilderHelper.path;

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path;

class EqPredicateBuilderSpec extends Specification {

    Path mockPath
    CriteriaBuilder criteriaBuilder;


    def setup() {

        mockPath = Mock()
        criteriaBuilder = Mock()

    }


    def "test Build with all expression arguments"() {

        given: "An Equal PredicateBuilder instance"

            PredicateBuilder predicateBuilder = new EqPredicateBuilder()

        and: "A condition filter with 3 expression arguments"

            ConditionFilter conditionFilter =
                    new ConditionFilter(
                            ConditionFilterType.EQ,
                            path("releaseDate"),
                            path("releaseDate")
                    )

        when: "Executing build predicate"

            predicateBuilder.build(conditionFilter,criteriaBuilder,mockPath,BookEO)

        then: "criteriaBuilder.equal has executed once"

            1 * criteriaBuilder.equal(_,_)

    }

    def "test Build with path expression argument and parameter values"() {

        given: "An Equal PredicateBuilder instance"

            PredicateBuilder predicateBuilder = new EqPredicateBuilder()

        and: "A condition filter with 3 expression arguments"

            ConditionFilter conditionFilter =
                    new ConditionFilter(
                            ConditionFilterType.EQ,
                            path("releaseDate"),
                            new Date()
                    )

        when: "Executing build predicate"

            predicateBuilder.build(conditionFilter,criteriaBuilder,mockPath,BookEO)

        then: "criteriaBuilder.equal has executed once"

            1 * criteriaBuilder.equal(_,_)

    }

}
