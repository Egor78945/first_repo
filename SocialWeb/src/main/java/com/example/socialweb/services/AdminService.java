package com.example.socialweb.services;

import com.example.socialweb.errors.AttributeError;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final UserService userService;
}
