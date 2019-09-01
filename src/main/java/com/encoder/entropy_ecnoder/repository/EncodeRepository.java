package com.encoder.entropy_ecnoder.repository;

import com.encoder.entropy_ecnoder.model.Encoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EncodeRepository extends PagingAndSortingRepository<Encoder,Long> {

    @Query(value = "select * from encode where user_id=?1",nativeQuery = true)
    Page<Encoder> getAllEncodingsForUser(Long userId, Pageable pageable);
}
