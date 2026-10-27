package com.example.auth.rest.repository;

import com.example.auth.entity.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "accountStatuses", path = "account-statuses")
public interface AccountStatusRestRepository extends JpaRepository<AccountStatus, String> {
}
