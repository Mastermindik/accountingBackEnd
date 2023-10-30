package diplomaproject.services.statisticService;

import diplomaproject.DTO.transaction.TransactionStatisticDTO;

import java.util.List;
import java.util.Map;

public interface StatisticService {
    TransactionStatisticDTO getCurrentWeekStatistic(String token);
    TransactionStatisticDTO getLastWeekStatistic(String token);
    TransactionStatisticDTO getCurrentMonthStatistic(String token);
    TransactionStatisticDTO getLastMonthStatistic(String token);
    List<TransactionStatisticDTO> getYearStatistic(String token);
}
