package com.pb.repository;

import com.pb.model.SubscriptionEntity;
import com.stripe.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity,Long> {

    SubscriptionEntity findByUserEntityId(Long userId);

    SubscriptionEntity findByCustomerId(String id);
}
