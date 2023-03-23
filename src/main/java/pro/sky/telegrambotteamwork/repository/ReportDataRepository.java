package pro.sky.telegrambotteamwork.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambotteamwork.model.ReportData;

import java.util.Set;

/**
 * Класс-репозиторий для работы с методами составленного отчета
 */
@Repository
public interface ReportDataRepository extends JpaRepository<ReportData, Long> {

    Set<ReportData> findListByChatId(Long id);

    ReportData findByChatId(Long id);

    @Query(value = "SELECT * FROM reports_data WHERE id = :id", nativeQuery = true)
    ReportData findReportDataById(@Param("id") Long id);
//    Optional<ReportData> findById(Long id);

}
