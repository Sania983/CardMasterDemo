
package com.CardMaster.dao.cau;

import com.CardMaster.model.cau.UserCau;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserCau, Long> {}