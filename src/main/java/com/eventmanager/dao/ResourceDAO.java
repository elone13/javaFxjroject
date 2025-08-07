package com.eventmanager.dao;

import com.eventmanager.model.Resource;
import com.eventmanager.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO {
    
    public List<Resource> getAllResources() {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT * FROM resources ORDER BY name";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resources.add(new Resource(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getObject("capacity", Integer.class),
                    rs.getString("description"),
                    rs.getBigDecimal("price_per_hour"),
                    rs.getString("status"),
                    rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }

    public List<Resource> getAvailableResources() {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT * FROM resources WHERE status = 'DISPONIBLE' ORDER BY name";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resources.add(new Resource(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getObject("capacity", Integer.class),
                    rs.getString("description"),
                    rs.getBigDecimal("price_per_hour"),
                    rs.getString("status"),
                    rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }

    public boolean addResource(Resource resource) {
        String sql = "INSERT INTO resources (name, type, capacity, description, price_per_hour, status, location) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resource.getName());
            stmt.setString(2, resource.getType());
            if (resource.getCapacity() != null) {
                stmt.setInt(3, resource.getCapacity());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, resource.getDescription());
            stmt.setBigDecimal(5, resource.getPricePerHour());
            stmt.setString(6, resource.getStatus());
            stmt.setString(7, resource.getLocation());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateResource(Resource resource) {
        String sql = "UPDATE resources SET name=?, type=?, capacity=?, description=?, price_per_hour=?, status=?, location=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resource.getName());
            stmt.setString(2, resource.getType());
            if (resource.getCapacity() != null) {
                stmt.setInt(3, resource.getCapacity());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, resource.getDescription());
            stmt.setBigDecimal(5, resource.getPricePerHour());
            stmt.setString(6, resource.getStatus());
            stmt.setString(7, resource.getLocation());
            stmt.setInt(8, resource.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteResource(int id) {
        String sql = "DELETE FROM resources WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Resource> getResourcesByType(String type) {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT * FROM resources WHERE type = ? AND status = 'DISPONIBLE' ORDER BY name";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resources.add(new Resource(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getObject("capacity", Integer.class),
                    rs.getString("description"),
                    rs.getBigDecimal("price_per_hour"),
                    rs.getString("status"),
                    rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }
}
