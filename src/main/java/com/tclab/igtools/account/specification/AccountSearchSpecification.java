package com.tclab.igtools.account.specification;

import com.tclab.igtools.account.dto.AccountDto;
import com.tclab.igtools.account.entity.Account;
import com.tclab.igtools.commons.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class AccountSearchSpecification implements Specification<Account> {

    private final AccountDto searchCriteria;

    public AccountSearchSpecification(AccountDto searchCriteriaDto) {
        this.searchCriteria = searchCriteriaDto;
    }

    @Override
    public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        // igBusinessAccountId
        if (Objects.nonNull(searchCriteria.getIgBusinessAccountId())){
            predicates.add(builder.equal(root.get("igBusinessAccountId"), searchCriteria.getIgBusinessAccountId()));
        }

        // name
        if (Utils.isNotEmpty(searchCriteria.getUsername())){
            predicates.add(builder.equal(root.get("name"), searchCriteria.getUsername()));
        }

        // type
        if (Utils.isNotEmpty(searchCriteria.getType())){
            predicates.add(builder.equal(root.get("type"), searchCriteria.getType()));
        }

        // parentIgBusinessAccountId
        if (Objects.nonNull(searchCriteria.getParentIgBusinessAccountId())){
            predicates.add(builder.equal(root.get("parentIgBusinessAccountId"), searchCriteria.getParentIgBusinessAccountId()));
        }

        // followers
        if (Objects.nonNull(searchCriteria.getFollowers())){
            predicates.add(builder.equal(root.get("followers"), searchCriteria.getFollowers()));
        }

        // creationDate
        if (Objects.nonNull(searchCriteria.getCreationDate())){
            predicates.add(builder.equal(root.get("creationDate"), searchCriteria.getCreationDate()));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}