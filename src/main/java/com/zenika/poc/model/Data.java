package com.zenika.poc.model;

import java.util.List;


public record Data(List<DataPoint> produced, List<DataPoint> announced, double tolerance) {
}
