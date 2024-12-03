package software_design.model;

import java.sql.*;
import java.util.List;

import software_design.repository.MenuRepository;

public class Menu {
    private MenuRepository menuRepository;

    public Menu() {
        menuRepository = new MenuRepository();
    }

    public List<MenuItem> getMenuItems(String role) throws SQLException 
    {
        return menuRepository.getMenuItems(role);
    }

    public void createItem(MenuItem item) throws SQLException 
    {
        menuRepository.createItem(item);
    }

    public List<String> getDistinctCategories(String role) throws SQLException 
    {
        return menuRepository.getDistinctCategories(role);
    }
}
