package exe201.Refashion.service;

import exe201.Refashion.dto.request.ReportRequest;
import exe201.Refashion.dto.response.ReportResponse;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Reports;
import exe201.Refashion.entity.Users;
import exe201.Refashion.repository.ProductRepository;
import exe201.Refashion.repository.ReportsRepository;
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
public class ReportService {

    ReportsRepository reportsRepository;
    UserRepository userRepository;
    ProductRepository productRepository;

    @Transactional
    public ReportResponse createReport(String reporterId, ReportRequest request) {
        Users reporter = userRepository.findById(reporterId).orElseThrow(() -> new RuntimeException("Reporter not found"));
        Users reportedUser = request.getReportedUserId() != null ?
                userRepository.findById(request.getReportedUserId()).orElseThrow(() -> new RuntimeException("Reported user not found")) : null;
        Products reportedProduct = request.getReportedProductId() != null ?
                productRepository.findById(request.getReportedProductId())
                        .orElseThrow(() -> new RuntimeException("Reported product not found")) : null;

        // Kiểm tra trùng lặp báo cáo
        if (reportedUser != null && reportsRepository.existsByReporterIdAndReportedUserId(reporterId, reportedUser.getId())) {
            throw new RuntimeException("You have already reported this user");
        }
        if (reportedProduct != null && reportsRepository.existsByReporterIdAndReportedProductId(reporterId, reportedProduct.getId())) {
            throw new RuntimeException("You have already reported this product");
        }

        Reports report = Reports.builder()
                .id(UUID.randomUUID().toString())
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reportedProduct(reportedProduct)
                .reason(request.getReason())
                .description(request.getDescription())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        Reports savedReport = reportsRepository.save(report);
        return mapToReportResponse(savedReport);
    }

    private ReportResponse mapToReportResponse(Reports report) {
        return ReportResponse.builder()
                .id(report.getId())
                .reporterId(report.getReporter().getId())
                .reportedUserId(report.getReportedUser() != null ? report.getReportedUser().getId() : null)
                .reportedProductId(report.getReportedProduct() != null ? report.getReportedProduct().getId() : null)
                .reason(report.getReason())
                .description(report.getDescription())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .build();
    }
}