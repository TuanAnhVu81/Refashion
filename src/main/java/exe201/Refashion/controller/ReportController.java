package exe201.Refashion.controller;

import exe201.Refashion.dto.request.ReportRequest;
import exe201.Refashion.dto.response.ApiResponse;
import exe201.Refashion.dto.response.ReportResponse;
import exe201.Refashion.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ReportController {

    ReportService reportService;

    @PostMapping
    public ApiResponse<ReportResponse> createReport(@RequestParam String reporterId, @RequestBody ReportRequest request) {
        ReportResponse report = reportService.createReport(reporterId, request);
        return ApiResponse.<ReportResponse>builder()
                .result(report)
                .build();
    }
}