package exe201.Refashion.service;

import exe201.Refashion.dto.request.ProductAdminRequest;
import exe201.Refashion.dto.response.ProductAdminResponse;
import exe201.Refashion.entity.Products;
import exe201.Refashion.entity.Reports;
import exe201.Refashion.repository.ProductRepository;
import exe201.Refashion.repository.ReportsRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductAdminService {

    ProductRepository productRepository;
    ReportsRepository reportsRepository;

    @Transactional
    public ProductAdminResponse reviewProduct(String productId, ProductAdminRequest request) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Reports> pendingReports = reportsRepository.findPendingReportsByProductId(productId);
        if (pendingReports.isEmpty()) {
            throw new RuntimeException("No pending reports for this product");
        }

        if (request.getAction().equals("APPROVE")) {
            product.setIsActive(true);
            for (Reports report : pendingReports) {
                report.setStatus("RESOLVED");
            }
        } else if (request.getAction().equals("REJECT")) {
            product.setIsActive(false);
            for (Reports report : pendingReports) {
                report.setStatus("VIOLATION_CONFIRMED");
            }
        } else if (request.getAction().equals("DELETE")) {
            productRepository.delete(product);
            for (Reports report : pendingReports) {
                report.setStatus("VIOLATION_CONFIRMED");
            }
            reportsRepository.saveAll(pendingReports);
            return ProductAdminResponse.builder()
                    .productId(productId)
                    .status("DELETED")
                    .message("Product deleted successfully")
                    .build();
        } else {
            throw new RuntimeException("Invalid action");
        }

        productRepository.save(product);
        reportsRepository.saveAll(pendingReports);

        return ProductAdminResponse.builder()
                .productId(productId)
                .status(product.getIsActive() ? "APPROVED" : "REJECTED")
                .message("Product reviewed successfully")
                .build();
    }


}