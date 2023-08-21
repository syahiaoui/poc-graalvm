package com.zenika.poc.service;

import com.zenika.poc.model.DataPoint;

import java.util.List;

public interface ScriptService {
    String execute(String content, List<DataPoint> produced, List<DataPoint> announced, double tolerance);
}
