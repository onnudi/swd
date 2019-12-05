package com.example.swd1.views;

import com.example.swd1.models.entities.Table;

import java.util.List;

public interface TableViewListener {

    void displayTableList(List<Table> list);

    void displayError();
}