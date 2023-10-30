package diplomaproject.controllers;

import diplomaproject.DTO.transaction.TransactionStatisticDTO;
import diplomaproject.services.statisticService.StatisticService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistic")
@CrossOrigin
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/currentWeek")
    public TransactionStatisticDTO getCurrentWeekStatistic(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return statisticService.getCurrentWeekStatistic(token);
    }
    @GetMapping("/lastWeek")
    public TransactionStatisticDTO getLastWeekStatistic(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return statisticService.getLastWeekStatistic(token);
    }
    @GetMapping("/currentMonth")
    public TransactionStatisticDTO getCurrentMonthStatistic(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return statisticService.getCurrentMonthStatistic(token);
    }
    @GetMapping("/lastMonth")
    public TransactionStatisticDTO getLastMonthStatistic(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return statisticService.getLastMonthStatistic(token);
    }
    @GetMapping("/currentYear")
    public List<TransactionStatisticDTO> getCurrentYearStatistic(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return statisticService.getYearStatistic(token);
    }
}
