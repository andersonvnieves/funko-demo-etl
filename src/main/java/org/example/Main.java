package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        var dados = new ArrayList<String>();
        try (Connection conn = DriverManager.getConnection(System.getenv("DATABASE_URL"), System.getenv("DATABASE_USERNAME"), System.getenv("DATABASE_PASSWORD"))) {
            ResultSet rs = conn.createStatement().executeQuery("select f.id,f.name,f.image_url,c.label as category,s.label as serie,f.created_at,f.last_modified_at from public.funko f inner join public.category c on f.category_id = c.id inner join public.serie s on s.id = f.serie_id where f.created_at >= (current_date - 1) OR f.last_modified_at >= (current_date - 1)");
            while (rs.next()) {
                dados.add(String.format("\"%s\"", rs.getString("id")) +
                        "," +
                        String.format("\"%s\"", rs.getString("name")) +
                        "," +
                        String.format("\"%s\"", rs.getString("image_url")) +
                        "," +
                        String.format("\"%s\"", rs.getString("category")) +
                        "," +
                        String.format("\"%s\"", rs.getString("serie")) +
                        "," +
                        String.format("\"%s\"", rs.getString("created_at")) +
                        "," +
                        String.format("\"%s\"", rs.getString("last_modified_at")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        var fileName = "C:\\temp\\export.csv";
        File csvOutputFile = new File(fileName);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dados.forEach(pw::println);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


}