package com.lumospath.dao.impl;

import com.lumospath.dao.MoodEntryDAO;
import com.lumospath.model.MoodEntry;
import com.lumospath.model.MoodType;
import com.lumospath.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of MoodEntryDAO for database operations
 */
public class MoodEntryDAOImpl implements MoodEntryDAO {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public MoodEntry save(MoodEntry moodEntry) throws SQLException {
        String sql = "INSERT INTO mood_entries (user_id, mood_type, mood_scale, description, trigger_cause, tags, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, moodEntry.getUserId());
            pstmt.setString(2, moodEntry.getMoodType().toString());
            pstmt.setInt(3, moodEntry.getMoodScale());
            pstmt.setString(4, moodEntry.getDescription());
            pstmt.setString(5, moodEntry.getTrigger());
            pstmt.setString(6, moodEntry.getTags());
            pstmt.setString(7, moodEntry.getCreatedAt().format(DATE_FORMATTER));
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating mood entry failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    moodEntry.setEntryId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating mood entry failed, no ID obtained.");
                }
            }
            
            return moodEntry;
        }
    }

    @Override
    public MoodEntry update(MoodEntry moodEntry) throws SQLException {
        String sql = "UPDATE mood_entries SET mood_type = ?, mood_scale = ?, description = ?, trigger_cause = ?, tags = ? WHERE entry_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, moodEntry.getMoodType().toString());
            pstmt.setInt(2, moodEntry.getMoodScale());
            pstmt.setString(3, moodEntry.getDescription());
            pstmt.setString(4, moodEntry.getTrigger());
            pstmt.setString(5, moodEntry.getTags());
            pstmt.setInt(6, moodEntry.getEntryId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating mood entry failed, no rows affected.");
            }
            
            return moodEntry;
        }
    }

    @Override
    public Optional<MoodEntry> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM mood_entries WHERE entry_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMoodEntry(rs));
                }
            }
        }
        
        return Optional.empty();
    }

    @Override
    public List<MoodEntry> findAll() throws SQLException {
        String sql = "SELECT * FROM mood_entries ORDER BY created_at DESC";
        List<MoodEntry> moodEntries = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                moodEntries.add(mapResultSetToMoodEntry(rs));
            }
        }
        
        return moodEntries;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM mood_entries WHERE entry_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(MoodEntry moodEntry) throws SQLException {
        return deleteById(moodEntry.getEntryId());
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM mood_entries WHERE entry_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM mood_entries";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            return rs.next() ? rs.getLong(1) : 0;
        }
    }

    @Override
    public List<MoodEntry> saveAll(List<MoodEntry> entities) throws SQLException {
        List<MoodEntry> savedEntities = new ArrayList<>();
        for (MoodEntry entry : entities) {
            savedEntities.add(save(entry));
        }
        return savedEntities;
    }

    @Override
    public List<MoodEntry> findByUserId(Integer userId) throws SQLException {
        String sql = "SELECT * FROM mood_entries WHERE user_id = ? ORDER BY created_at DESC";
        List<MoodEntry> moodEntries = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    moodEntries.add(mapResultSetToMoodEntry(rs));
                }
            }
        }
        
        return moodEntries;
    }

    @Override
    public List<MoodEntry> findByUserIdAndDateRange(Integer userId, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        String sql = "SELECT * FROM mood_entries WHERE user_id = ? AND created_at BETWEEN ? AND ? ORDER BY created_at DESC";
        List<MoodEntry> moodEntries = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, startDate.format(DATE_FORMATTER));
            pstmt.setString(3, endDate.format(DATE_FORMATTER));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    moodEntries.add(mapResultSetToMoodEntry(rs));
                }
            }
        }
        
        return moodEntries;
    }

    @Override
    public List<MoodEntry> findByUserIdAndMoodType(Integer userId, MoodType moodType) throws SQLException {
        String sql = "SELECT * FROM mood_entries WHERE user_id = ? AND mood_type = ? ORDER BY created_at DESC";
        List<MoodEntry> moodEntries = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, moodType.toString());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    moodEntries.add(mapResultSetToMoodEntry(rs));
                }
            }
        }
        
        return moodEntries;
    }

    @Override
    public List<MoodEntry> findRecentByUserId(Integer userId, int limit) throws SQLException {
        String sql = "SELECT * FROM mood_entries WHERE user_id = ? ORDER BY created_at DESC LIMIT ?";
        List<MoodEntry> moodEntries = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    moodEntries.add(mapResultSetToMoodEntry(rs));
                }
            }
        }
        
        return moodEntries;
    }

    @Override
    public double getAverageMoodScale(Integer userId, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        String sql = "SELECT AVG(mood_scale) as avg_mood FROM mood_entries WHERE user_id = ? AND created_at BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, startDate.format(DATE_FORMATTER));
            pstmt.setString(3, endDate.format(DATE_FORMATTER));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getDouble("avg_mood") : 0.0;
            }
        }
    }

    @Override
    public List<Object[]> getMoodStatistics(Integer userId, int days) throws SQLException {
        String sql = "SELECT mood_type, COUNT(*) as count FROM mood_entries WHERE user_id = ? AND created_at >= ? GROUP BY mood_type ORDER BY count DESC";
        List<Object[]> statistics = new ArrayList<>();
        
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, startDate.format(DATE_FORMATTER));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    statistics.add(new Object[]{rs.getString("mood_type"), rs.getInt("count")});
                }
            }
        }
        
        return statistics;
    }

    @Override
    public List<MoodEntry> findByTriggerContaining(Integer userId, String triggerText) throws SQLException {
        String sql = "SELECT * FROM mood_entries WHERE user_id = ? AND trigger_cause LIKE ? ORDER BY created_at DESC";
        List<MoodEntry> moodEntries = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, "%" + triggerText + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    moodEntries.add(mapResultSetToMoodEntry(rs));
                }
            }
        }
        
        return moodEntries;
    }

    @Override
    public List<Object[]> getMoodTrendData(Integer userId, int days) throws SQLException {
        String sql = "SELECT DATE(created_at) as date, AVG(mood_scale) as avg_mood FROM mood_entries WHERE user_id = ? AND created_at >= ? GROUP BY DATE(created_at) ORDER BY date";
        List<Object[]> trendData = new ArrayList<>();
        
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, startDate.format(DATE_FORMATTER));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    trendData.add(new Object[]{rs.getString("date"), rs.getDouble("avg_mood")});
                }
            }
        }
        
        return trendData;
    }

    @Override
    public long countByUserId(Integer userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM mood_entries WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        }
    }

    @Override
    public int deleteOldEntries(LocalDateTime olderThan) throws SQLException {
        String sql = "DELETE FROM mood_entries WHERE created_at < ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, olderThan.format(DATE_FORMATTER));
            return pstmt.executeUpdate();
        }
    }

    /**
     * Delete all mood entries for a specific user
     */
    public int deleteAllByUserId(Integer userId) throws SQLException {
        String sql = "DELETE FROM mood_entries WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate();
        }
    }

    /**
     * Map ResultSet to MoodEntry object
     */
    private MoodEntry mapResultSetToMoodEntry(ResultSet rs) throws SQLException {
        MoodEntry entry = new MoodEntry();
        entry.setEntryId(rs.getInt("entry_id"));
        entry.setUserId(rs.getInt("user_id"));
        entry.setMoodType(MoodType.valueOf(rs.getString("mood_type")));
        entry.setMoodScale(rs.getInt("mood_scale"));
        entry.setDescription(rs.getString("description"));
        entry.setTrigger(rs.getString("trigger_cause"));
        entry.setTags(rs.getString("tags"));
        
        String createdAtStr = rs.getString("created_at");
        if (createdAtStr != null) {
            entry.setCreatedAt(LocalDateTime.parse(createdAtStr, DATE_FORMATTER));
        }
        
        return entry;
    }
}