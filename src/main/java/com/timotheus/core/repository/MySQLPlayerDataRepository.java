package com.timotheus.core.repository;

import com.timotheus.core.model.PlayerData;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;

public class MySQLPlayerDataRepository implements PlayerDataRepository {

    private final DataSource dataSource;

    public MySQLPlayerDataRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<PlayerData> load(UUID uuid) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT coins, joins FROM player_data WHERE uuid = ?"
             )) {

            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PlayerData data = new PlayerData(uuid);
                data.addCoins(rs.getInt("coins"));
                for (int i = 0; i < rs.getInt("joins"); i++) {
                    data.incrementJoins();
                }
                return Optional.of(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void save(PlayerData data) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     """
                     INSERT INTO player_data (uuid, coins, joins)
                     VALUES (?, ?, ?)
                     ON DUPLICATE KEY UPDATE
                       coins = VALUES(coins),
                       joins = VALUES(joins)
                     """
             )) {

            ps.setString(1, data.getUuid().toString());
            ps.setInt(2, data.getCoins());
            ps.setInt(3, data.getJoins());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}