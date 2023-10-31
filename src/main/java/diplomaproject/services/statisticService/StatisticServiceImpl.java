package diplomaproject.services.statisticService;

import diplomaproject.DTO.transaction.TransactionStatisticDTO;
import diplomaproject.models.MyTransaction;
import diplomaproject.repositories.TransactionRepository;
import diplomaproject.utils.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final TransactionRepository transactionRepository;
    private final JwtToken jwtToken;

    @Override
    @Transactional(readOnly = true)
    public TransactionStatisticDTO getCurrentWeekStatistic(String token) {
        String email = jwtToken.getEmail(token);
        List<Date> dates = getDates("Current week");
        var result = transactionRepository.findAllByAccount_EmailAndDateBetween(email, dates.get(0), dates.get(1));

        return toDTO(result);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionStatisticDTO getLastWeekStatistic(String token) {
        String email = jwtToken.getEmail(token);
        List<Date> dates = getDates("Last week");
        var result = transactionRepository.findAllByAccount_EmailAndDateBetween(email, dates.get(0), dates.get(1));

        return toDTO(result);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionStatisticDTO getCurrentMonthStatistic(String token) {
        String email = jwtToken.getEmail(token);
        List<Date> dates = getDates("Current month");
        var result = transactionRepository.findAllByAccount_EmailAndDateBetween(email, dates.get(0), dates.get(1));
//        var result = transactionRepository.findAllByAccountAndDateBetween(accountRepository.findAccountByEmail(email), dates.get(0), dates.get(1));
        return toDTO(result);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionStatisticDTO getLastMonthStatistic(String token) {
        String email = jwtToken.getEmail(token);
        List<Date> dates = getDates("Last month");
        var result = transactionRepository.findAllByAccount_EmailAndDateBetween(email, dates.get(0), dates.get(1));
        System.out.println(result);
        return toDTO(result);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionStatisticDTO> getYearStatistic(String token) {
        List<TransactionStatisticDTO> yearly = new ArrayList<>();
        String email = jwtToken.getEmail(token);
        List<Date> dates = getDates("Current year");
        for (int i = 0; i <= dates.size() - 1; i = i + 2) {
            var result = transactionRepository.findAllByAccount_EmailAndDateBetween(email, dates.get(i), dates.get(i + 1));
            yearly.add(toDTO(result));
        }
        return yearly;
    }

    private TransactionStatisticDTO toDTO(List<MyTransaction> transactions) {
        Map<String, Double> incomes = new HashMap<>();
        Map<String, Double> expenses = new HashMap<>();
        TransactionStatisticDTO statistic = new TransactionStatisticDTO();
        transactions.forEach(r -> {
            if (r.getType().name().equals("INCOME")) {
                if (incomes.containsKey(r.getCategory())) {
                    incomes.put(r.getCategory(), incomes.get(r.getCategory()) + r.getSum());
                } else {
                    incomes.put(r.getCategory(), r.getSum());
                }
            } else if (r.getType().name().equals("EXPENSE")) {
                if (expenses.containsKey(r.getCategory())) {
                    expenses.put(r.getCategory(), expenses.get(r.getCategory()) + r.getSum());
                } else {
                    expenses.put(r.getCategory(), r.getSum());
                }
            }
        });
        statistic.setExpenses(expenses);
        statistic.setIncomes(incomes);
        return statistic;
    }

    private List<Date> getDates(String period) {
        Calendar calendar = Calendar.getInstance();
        List<Date> dates = new ArrayList<>();
        Date now = new Date();
        switch (period) {
            case "Current week" -> {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                Date firstDayOfWeek = calendar.getTime();
                dates.add(firstDayOfWeek);
                dates.add(now);
            }
            case "Last week" -> {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                calendar.add(Calendar.DAY_OF_WEEK, -7);
                dates.add(calendar.getTime());
                calendar.add(Calendar.DAY_OF_WEEK, 6);
                dates.add(calendar.getTime());
            }
            case "Current month" -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                dates.add(calendar.getTime());
                dates.add(now);
            }
            case "Last month" -> {
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                dates.add(calendar.getTime());
                calendar.add(Calendar.MONTH, 1);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                dates.add(calendar.getTime());
            }
            case "Current year" -> {
                for (int i = 5; i >= 0; i--) {
                    calendar.add(Calendar.MONTH, -i);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    dates.add(calendar.getTime());
                    if (i == 0) {
                        dates.add(now);
                        break;
                    }
                    calendar.add(Calendar.MONTH, 1);
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    dates.add(calendar.getTime());
                    calendar.add(Calendar.MONTH, i);
                }
            }
        }

        return dates;
    }
}
