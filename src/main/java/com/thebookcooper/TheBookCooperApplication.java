package com.thebookcooper;

import com.thebookcooper.dao.*;
import com.thebookcooper.model.*;
import com.thebookcooper.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

//@ComponentScan(basePackages = "com.thebookcooper")
@SpringBootApplication
public class TheBookCooperApplication {
    public static void main(String[] args) {
        SpringApplication.run(TheBookCooperApplication.class, args);
    }
}
