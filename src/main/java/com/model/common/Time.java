package com.model.common;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class Time {

    private LocalDateTime reg_datetime;
    private LocalDateTime updated_datetime;

    public Time() {

    }

    public Time(LocalDateTime reg_datetime, LocalDateTime update_datetime) {
        this.reg_datetime = reg_datetime;
        this.updated_datetime = update_datetime;
    }
}
