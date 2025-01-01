package at.carcar.carcarbackend.Report;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Optional<Report> findReportById(long id) {
        return reportRepository.findById(id);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
}
