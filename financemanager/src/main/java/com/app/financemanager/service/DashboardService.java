package com.app.financemanager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.financemanager.dao.ExpenseDao;
import com.app.financemanager.dao.IncomeDao;
import com.app.financemanager.dao.ProfileDao;
import com.app.financemanager.dto.RecentTransactionDto;
import com.app.financemanager.model.Expense;
import com.app.financemanager.model.Income;
import com.app.financemanager.model.Profile;
import com.app.financemanager.responseStructure.ResponseStructure;

@Service
public class DashboardService {

    @Autowired
    private ExpenseDao expenseDao;

    @Autowired
    private IncomeDao incomeDao;

    @Autowired
    private ProfileDao profileDao;

    private Profile getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return profileDao.findByEmail(email);
    }

    public Map<String, Object> getDashboardData() {
        Profile profile = getCurrentProfile();
        Long profileId = profile.getId();

        BigDecimal totalIncome = incomeDao.findTotalIncome(profileId);
        BigDecimal totalExpense = expenseDao.findTotalExpense(profileId);
        BigDecimal totalBalance = (totalIncome == null ? BigDecimal.ZERO : totalIncome)
                .subtract(totalExpense == null ? BigDecimal.ZERO : totalExpense);

        List<Income> latestIncomes = incomeDao.findRecentIncomes(profileId);
        List<Expense> latestExpenses = expenseDao.findRecentExpenses(profileId);

        List<RecentTransactionDto> merged = new ArrayList<>();

        if (latestIncomes != null) {
            merged.addAll(latestIncomes.stream().map(i -> new RecentTransactionDto(i.getId(), i.getName(), i.getIcon(), i.getProfile().getId(), i.getAmount(), i.getDate(), i.getCreatedAt(), i.getUpdatedAt(), "income"))
                    .collect(Collectors.toList()));
        }

        if (latestExpenses != null) {
            merged.addAll(latestExpenses.stream().map(e -> new RecentTransactionDto(e.getId(), e.getName(), e.getIcon(), e.getProfile().getId(), e.getAmount(), e.getDate(), e.getCreatedAt(), e.getUpdatedAt(), "expense"))
                    .collect(Collectors.toList()));
        }

        List<RecentTransactionDto> recentTransactions = merged.stream()
                .sorted(Comparator.comparing(RecentTransactionDto::getDate, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(RecentTransactionDto::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("totalBalance", totalBalance);
        map.put("totalIncome", totalIncome == null ? BigDecimal.ZERO : totalIncome);
        map.put("totalExpense", totalExpense == null ? BigDecimal.ZERO : totalExpense);
        map.put("recentIncomes", latestIncomes == null ? new ArrayList<>() : latestIncomes);
        map.put("recentExpenses", latestExpenses == null ? new ArrayList<>() : latestExpenses);
        map.put("recentTransactions", recentTransactions);

        return map;
    }

    public ResponseEntity<ResponseStructure<Map<String, Object>>> getDashboard() {
        ResponseStructure<Map<String, Object>> structure = new ResponseStructure<>();
        Map<String, Object> data = getDashboardData();
        structure.setStatusCode(HttpStatus.OK.value());
        structure.setMessage("Dashboard Data");
        structure.setData(data);
        return new ResponseEntity<>(structure, HttpStatus.OK);
    }

}
