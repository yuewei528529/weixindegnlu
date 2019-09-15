package com.tmy.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tmy.model.Dengluuser;

@Transactional
public interface DengluuserRepository extends JpaRepository<Dengluuser, String> {
    
	Dengluuser findBycode(String code);

	int findByoAuthNichegn(String oAuthNichegn);

	void deleteByoAuthNichegn(String getoAuthNichegn);

	boolean existsByoAuthNichegn(String getoAuthNichegn);

	@Modifying
	@Query("update dengluuser d set d.code = ?1 where d.oAuthNichegn = ?2")
	void setcode(String code, String oAuthNichegn);

}
