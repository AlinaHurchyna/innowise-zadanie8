package com.innowise.marketplace.data;

import com.innowise.marketplace.model.Advertisement;
import com.innowise.marketplace.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    @Query("SELECT a FROM Advertisement a WHERE "
            + "(:keyword IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND "
            + "(:categoryId IS NULL OR a.category.id = :categoryId) AND "
            + "(:city IS NULL OR a.city = :city)")
    List<Advertisement> search(@Param("keyword") String keyword, @Param("categoryId") Long categoryId,
                                @Param("city") String city, Sort sort);

    List<Advertisement> findBySellerOrderByCreatedAtDesc(User seller);
}
