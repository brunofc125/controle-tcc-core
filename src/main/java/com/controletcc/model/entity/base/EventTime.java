package com.controletcc.model.entity.base;

import com.controletcc.util.LocalDateTimeUtil;
import com.controletcc.util.LocalDateUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@MappedSuperclass
public class EventTime extends BaseEntity {

    @Column(name = "data_inicial", nullable = false)
    protected LocalDateTime dataInicial;

    @Column(name = "data_final", nullable = false)
    protected LocalDateTime dataFinal;

    public boolean isDataInicialEmpty() {
        return dataInicial == null;
    }

    public boolean isDataFinalEmpty() {
        return dataFinal == null;
    }

    public boolean isEmpty() {
        return isDataInicialEmpty() || isDataFinalEmpty();
    }

    public boolean isDataInicialEqualOrAfterDataFinal() {
        return !isEmpty() && LocalDateTimeUtil.compare(dataInicial, dataFinal) >= 0;
    }

    public boolean isDataInicialAndDataFinalDifferentDays() {
        return !isEmpty() && LocalDateUtil.compare(dataInicial.toLocalDate(), dataFinal.toLocalDate()) != 0;
    }

    public boolean isDataInicialHourInvalid() {
        return !isDataInicialEmpty() && LocalDateTimeUtil.date1HourInvalid(dataInicial);
    }

    public boolean isDataFinalHourInvalid() {
        return !isDataFinalEmpty() && LocalDateTimeUtil.date1HourInvalid(dataFinal);
    }

    public boolean isEventOccurring(LocalDateTime occurringDate) {
        return occurringDate != null && occurringDate.compareTo(dataInicial) >= 0 && occurringDate.compareTo(dataFinal) < 0;
    }

    public boolean invalidInterval(LocalDate limitDateStart, LocalDate limitDateEnd, Integer limitHourStart, Integer limitHourEnd) {
        return !isEmpty() && !LocalDateTimeUtil.validInterval(dataInicial, dataFinal, limitDateStart, limitDateEnd, limitHourStart, limitHourEnd);
    }

    public <T extends EventTime> boolean invalidInterpolation(List<T> events) {
        return events.stream().anyMatch(r -> !r.equals(this) && LocalDateTimeUtil.intersectDate(this.getDataInicial(), this.getDataFinal(), r.getDataInicial(), r.getDataFinal()));
    }

}
