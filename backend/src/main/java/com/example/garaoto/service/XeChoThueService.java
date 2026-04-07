package com.example.garaoto.service;

import com.example.garaoto.dto.request.XeChoThueRequest;
import com.example.garaoto.dto.response.XeChoThueResponse;

import java.util.List;

public interface XeChoThueService {
    XeChoThueResponse create(XeChoThueRequest request);
    XeChoThueResponse update(Integer id, XeChoThueRequest request);
    List<XeChoThueResponse> getAll();
    XeChoThueResponse getById(Integer id);
    void delete(Integer id);
}