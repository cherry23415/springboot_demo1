package com.ying.repository;

import com.ying.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lyz
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
