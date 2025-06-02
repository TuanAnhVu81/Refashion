package exe201.Refashion.repository;

import exe201.Refashion.entity.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, String> {
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reports r WHERE r.reporter.id = :reporterId " +
            "AND r.reportedUser.id = :reportedUserId")
    boolean existsByReporterIdAndReportedUserId(@Param("reporterId") String reporterId,
                                                @Param("reportedUserId") String reportedUserId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reports r WHERE r.reporter.id = :reporterId " +
            "AND r.reportedProduct.id = :reportedProductId")
    boolean existsByReporterIdAndReportedProductId(@Param("reporterId") String reporterId,
                                                   @Param("reportedProductId") String reportedProductId);
}