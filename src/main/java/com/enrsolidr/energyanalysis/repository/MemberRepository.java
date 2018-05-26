package com.enrsolidr.energyanalysis.repository;

import com.enrsolidr.energyanalysis.entity.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends MongoRepository<Member, Long> {
    @Query(value = "{ 'user.email' : ?0}", fields = "{ 'user.email' : 0 }")
    List<Member> findByUserEmail(String email);

    List<Member> findByUsername(String name);

    List<Member> findByAuthoritiesIn(Collection<String> role);

    List<Member> findByLinkyActivated(boolean activated);
}
