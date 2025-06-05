package exe201.Refashion.service;

import exe201.Refashion.dto.request.AnalyticsRequest;
import exe201.Refashion.dto.response.AnalyticsResponse;
import exe201.Refashion.repository.OrderRepository;
import exe201.Refashion.repository.RatingsRepository;
import exe201.Refashion.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnalyticsService {

    UserRepository userRepository;
    OrderRepository orderRepository;
    RatingsRepository ratingsRepository;

    public AnalyticsResponse getUserAnalytics(AnalyticsRequest request) {
        long totalUsers = userRepository.count();
        BigDecimal totalRevenue = orderRepository.findAll().stream()
                .filter(order -> order.getCreatedAt().isAfter(request.getStartDate())
                        && order.getCreatedAt().isBefore(request.getEndDate())
                        && order.getStatus().equals("COMPLETED"))
                .map(order -> order.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double averageRating = ratingsRepository.findAll().stream()
                .filter(rating -> rating.getCreatedAt().isAfter(request.getStartDate())
                        && rating.getCreatedAt().isBefore(request.getEndDate()))
                .mapToInt(rating -> rating.getRating())
                .average()
                .orElse(0.0);

        return AnalyticsResponse.builder()
                .totalUsers(totalUsers)
                .totalRevenue(totalRevenue)
                .averageRating(averageRating)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }
}