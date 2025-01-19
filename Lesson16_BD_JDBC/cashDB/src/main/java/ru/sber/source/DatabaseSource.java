package ru.sber.source;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Класс, который использует JDBC для работы с БД - Postgresql
 */
public class DatabaseSource implements Source {

    private static final String URL = "jdbc:postgresql://localhost:5433/";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static final Connection conn;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public DatabaseSource() {
        try (Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS public.cache (key TEXT PRIMARY KEY, value bytea)";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(String key, List<Integer> data) {
        try (PreparedStatement pstmt =
                     conn.prepareStatement("INSERT INTO public.cache (key, value) VALUES (?, ?) ON CONFLICT (key) DO " +
                             "UPDATE SET value = excluded.value")) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
            byte[] bytes = baos.toByteArray();

            pstmt.setString(1, key);
            pstmt.setBytes(2, bytes);
            pstmt.executeUpdate();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Integer> load(String key) {
        try (PreparedStatement pstmt =
                     conn.prepareStatement("SELECT value FROM public.cache WHERE key = ?")) {
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                byte[] bytes = rs.getBytes("value");
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return (List<Integer>) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
