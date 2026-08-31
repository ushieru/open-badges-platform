package com.gdgguadalajara.assertion.model.dto;

import java.time.LocalDate;

import com.gdgguadalajara.assertion.model.AssertionStatus;

import jakarta.ws.rs.QueryParam;

public class AssertionFilterParams {
    @QueryParam("status")
    public AssertionStatus status;

    @QueryParam("search")
    public String search;

    @QueryParam("from")
    public LocalDate from;

    @QueryParam("to")
    public LocalDate to;
}