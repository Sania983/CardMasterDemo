package com.CardMaster.dao.iam;
import com.CardMaster.model.iam.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}