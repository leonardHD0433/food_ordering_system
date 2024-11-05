package software_design.database.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ItemIdGenerator implements IdentifierGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ItemIdGenerator.class);

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String prefix = "I";
        String query = "SELECT MAX(ItemId) as maxId FROM menu_table";
        try (Connection connection = session.getJdbcConnectionAccess().obtainConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            if (rs.next()) {
                String maxId = rs.getString("maxId");
                if (maxId != null) {
                    int id = Integer.parseInt(maxId.substring(1)) + 1;
                    return prefix + String.format("%03d", id);
                }
            }
        } catch (Exception e) {
            logger.error("Error generating item id", e);
        }
        return prefix + "001";
    }
}
