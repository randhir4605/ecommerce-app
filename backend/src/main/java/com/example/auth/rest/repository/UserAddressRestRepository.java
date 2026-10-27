package com.example.auth.rest.repository;

import com.example.auth.entity.UserAddress;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "userAddresses", path = "user-addresses")
public interface UserAddressRestRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUser_Id(Long userId);
}
