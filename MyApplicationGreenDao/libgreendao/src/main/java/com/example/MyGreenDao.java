package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyGreenDao {
    public static void main(String args[]){
        Schema schema=new Schema(1,"com.example.nbhung.myapplicationgreendao.db");
        addTables(schema);
        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void addTables(final Schema schema) {
        addGroceryEntity(schema);
    }
    // This is use to describe the columns of your table
    private static Entity addGroceryEntity(final Schema schema) {
        Entity grocery = schema.addEntity("Student");
        grocery.addIdProperty().primaryKey().autoincrement();
        grocery.addStringProperty("name").notNull();
        grocery.addIntProperty("age").notNull();
        grocery.addStringProperty("status");
        return grocery;
    }
}
