package com.example.socialweb.controllers.adminControllers;

import com.example.socialweb.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final UserService userService;
    private final NewsService newsService;
    private final ReportService reportService;
    private final AdminService adminService;
    private final CommunityService communityService;

}
