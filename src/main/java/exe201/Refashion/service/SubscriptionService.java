package exe201.Refashion.service;

import exe201.Refashion.dto.request.SubscriptionRequest;
import exe201.Refashion.dto.response.SubscriptionResponse;
import exe201.Refashion.entity.Subscriptions;
import exe201.Refashion.entity.Users;
import exe201.Refashion.repository.SubscriptionsRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionService {

    SubscriptionsRepository subscriptionsRepository;
    UserRepository userRepository;

    public SubscriptionResponse subscribe(String userId, SubscriptionRequest request) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Subscriptions subscription = Subscriptions.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .packageType(request.getPackageType())
                .messageLimit(request.getMessageLimit())
                .startDate(LocalDateTime.now())
                .endDate(request.getEndDate())
                .status("ACTIVE")
                .build();

        Subscriptions savedSubscription = subscriptionsRepository.save(subscription);
        return mapToSubscriptionResponse(savedSubscription);
    }

    @Transactional
    public void cancelSubscription(String subscriptionId, String userId) {
        Subscriptions subscription = subscriptionsRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        if (!subscription.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to cancel this subscription");
        }

        if (!subscription.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("Only active subscriptions can be canceled");
        }

        subscription.setStatus("CANCELED");
        subscriptionsRepository.save(subscription);
    }

    private SubscriptionResponse mapToSubscriptionResponse(Subscriptions subscription) {
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .userId(subscription.getUser().getId())
                .packageType(subscription.getPackageType())
                .messageLimit(subscription.getMessageLimit())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .status(subscription.getStatus())
                .build();
    }
}