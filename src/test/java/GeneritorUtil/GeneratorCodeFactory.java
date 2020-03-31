package GeneritorUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sun.security.krb5.internal.crypto.Des;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GeneratorCodeFactory {
    private String baseName;
    private ConnectionPool pool;
    private ArrayList<DesTable> desTables = new ArrayList<>(32);
    private ArrayList<String> tableNames = new ArrayList<>(32);
    private String dirPath;
    public GeneratorCodeFactory(ConnectionPool pool, String baseName) {
        this.baseName = baseName;
        this.pool = pool;
        init();
    }
    private void init() {
        try {
            getTableNameByCon();
            for (int i = 0; i < tableNames.size(); i++) {
                getDesTable(tableNames.get(i));
            }
            getProjectPath();
            generatorCode();
            generatorDao();
            generatorService();
            generatorController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTableNameByCon() {
        Connection con = pool.getConnection();
        try {
            DatabaseMetaData meta = con.getMetaData();
            ResultSet rs = meta.getTables(baseName, null, null,
                    new String[]{"TABLE"});
            while (rs.next()) {
                System.out.println("表名：" + rs.getString(3));
                tableNames.add(rs.getString(3));
            }
            con.close();
        } catch (Exception e) {
            try {
                con.close();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getDesTable(String tableName) throws SQLException {
        ResultSet rs = pool.getResultSet(tableName);
        if (rs.next()) {
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            DesTable table = new DesTable();
            table.setName(metaData.getTableName(1));
            for (int i = 1; i < count + 1; i++) {
                DesColumn column = new DesColumn(metaData.getColumnName(i),
                        metaData.getColumnTypeName(i), metaData.isAutoIncrement(i));
                table.addColomn(column);
            }
            desTables.add(table);
        }
    }

    private void getProjectPath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
        System.out.println(path);
        File directory = new File("");//设定为当前文件夹
        String proPath = directory.getAbsolutePath();
        String[] arr = path.split("/");
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("target")) {
                index = i - 1;
                break;
            }
        }
        System.out.println(arr[index]);
      if(proPath.contains(arr[index])){
          dirPath = proPath  + "/src/main/java/";
      }else{
          dirPath = proPath + "/" + arr[index] + "/src/main/java/";
      }
        System.out.println(dirPath);
    }

    private void generatorCode() {
        generatorDir();
        generatorDomain();
    }

    private void generatorDir() {
        String[] pathArr = new String[]{
                "com", "com/domain", "com/controller", "com/dao", "com/service"
        };
        for (int i = 0; i < pathArr.length; i++) {
            File file = new File(dirPath + pathArr[i]);
            if (file.exists()) {
                System.out.println("代码已经生成，若重新生成，请手删除");
            } else {
                try {
                    file.mkdir();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void generatorDomain() {
        for (DesTable desTable : desTables) {
            String fileName = desTable.getName();
            fileName = camelCase(fileName);
            fileName = fileName.substring(0, 1).toUpperCase() + fileName.substring(1);
            File file = new File(dirPath + "com/domain/" + fileName + ".java");
            try {
                file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                PrintWriter out = new PrintWriter(bw);
                out.println("package com.domain;");
                out.println("import java.util.Date;");
                out.println("import lombok.Setter;");
                out.println("import lombok.Getter;");
                out.println("@Setter");
                out.println("@Getter");
                out.println("public class " + fileName + "{");
                List<DesColumn> colomns = desTable.getColumns();
                for (DesColumn column : colomns) {
                    String type = column.getType();
                    String name = camelCase(column.getCname());
                    if (type.equals("VARCHAR") || type.equals("TEXT")) {
                        out.println(" private String " + name + ";");
                    } else if (type.equals("INT")) {
                        out.println(" private Integer " + name + ";");
                    } else if (type.equals("FLOAT")) {
                        out.println(" private Float " + name + ";");
                    } else if (type.equals("DATE")) {
                        out.println("private Date " + name + ";");
                    } else if (type.equals("DATETIME")) {
                        out.println("private Date " + name + ";");
                    } else if (type.equals("DECIMAL")) {
                        out.println("private Float " + name + ";");
                    }
                }
                out.println("}");
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String camelCase(String name) {
        int index = name.indexOf("_");
        if (name.indexOf("_") > -1) {
            String temp = name.substring(0, index);
            String temp1 = name.substring(index + 1, index + 2).toUpperCase();
            return camelCase(temp + temp1 + name.substring(index + 2));
        }
        return name;
    }

    private String upperFirst(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private void generatorDao() throws IOException {
        for (DesTable table : desTables) {
            String tableName = table.getName();
            String objectName = camelCase(tableName);
            objectName = upperFirst(objectName);
            File file = new File(dirPath + "com/dao/" + objectName + "Dao.java");
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            PrintWriter out = new PrintWriter(bw);
            out.println("package com.dao;");
            out.println("import java.util.List;");
            out.println("import com.domain." + objectName + ";");
            out.println("import org.apache.ibatis.annotations.*;");
            out.println("@Mapper");
            out.println("public interface " + objectName + "Dao {");
            createMethodCreate(table, out);
            createMethodSelect(table, out);
            createMethodUpdate(table, out);
            createDeleteMethod(table, out);
            out.println("}");
            out.flush();
            out.close();
        }
    }

    private void createMethodSelect(DesTable table, PrintWriter out) {
        String tableName = table.getName();
        String objectName = camelCase(tableName);
        objectName = upperFirst(objectName);
        StringBuilder s = new StringBuilder("\"select ");
        List<DesColumn> colomns = table.getColumns();
        String idColunm = "";
        for (DesColumn column : colomns) {
            s.append(column.getCname());
            s.append(",");
            if (column.getIsAuto()) {
                idColunm = column.getCname();
            }
        }
        s.delete(s.length() - 1, s.length());
        s.append(" from " + table.getName());
        out.println("  @Select(" + s.toString() + "\")");
        //selectList 方法
        out.println("List<" + objectName + "> " + "selectList();");
        //selectOne 方法
        s.append(" where " + idColunm + "=#{" + camelCase(idColunm) + "}");
        out.println("@Select(" + s.toString() + "\")");
        out.println(objectName + " selectOne(int id);");
    }

    private void createMethodCreate(DesTable table, PrintWriter out) {
        String tableName = table.getName();
        String objectName = camelCase(tableName);
        objectName = upperFirst(objectName);
        StringBuilder s = new StringBuilder("\"insert into ");
        StringBuilder valueS = new StringBuilder("values(");
        s.append(tableName + "(");
        List<DesColumn> colomns = table.getColumns();
        String idColunm = "";
        for (DesColumn column : colomns) {
            if (column.getIsAuto()) {
                idColunm = column.getCname();
            } else {
                String name = column.getCname();
                s.append(name + ",");
                valueS.append("#{" + camelCase(name) + "},");
            }
        }
        s.delete(s.length() - 1, s.length());
        s.append(" )");
        valueS.delete(valueS.length() - 1, valueS.length());
        valueS.append(")");
        out.println("@Insert(" + s.toString() + valueS.toString() + "\")");
        out.println("@Options(useGeneratedKeys=true, keyProperty = \"" + camelCase(idColunm) + "\",keyColumn=\"" + idColunm + "\")");
        out.println(" int create(" + objectName + " " + camelCase(tableName) + ");");
    }

    private void createMethodUpdate(DesTable table, PrintWriter out) {
        String tableName = table.getName();
        String objectName = camelCase(tableName);
        objectName = upperFirst(objectName);
        StringBuilder s = new StringBuilder("\"update ");
        s.append(tableName + " set ");
        List<DesColumn> colomns = table.getColumns();
        String idColunm = "";
        for (DesColumn column : colomns) {
            if (column.getIsAuto()) {
                idColunm = column.getCname();
            } else {
                String name = column.getCname();
                s.append(name + "=#{" + camelCase(name) + "},");
            }
        }
        s.delete(s.length() - 1, s.length());
        s.append(" where " + idColunm + "=#{" + camelCase(idColunm) + "}");
        out.println("@Update(" + s.toString().toString() + "\")");
        out.println(" int update(" + objectName + " " + camelCase(tableName) + ");");
    }

    private void createDeleteMethod(DesTable table, PrintWriter out) {
        String tableName = table.getName();
        String objectName = camelCase(tableName);
        objectName = upperFirst(objectName);
        StringBuilder s = new StringBuilder("\"delete from  ");
        s.append(tableName + " where ");
        List<DesColumn> colomns = table.getColumns();
        String idColunm = "";
        for (DesColumn column : colomns) {
            if (column.getIsAuto()) {
                idColunm = column.getCname();
                break;
            }
        }
        s.append(idColunm + "=#{" + camelCase(idColunm) + "}\"");
        out.println("@Delete(" + s.toString() + ")");
        out.println("int delete(int " + camelCase(idColunm) + ");");
    }

    //service层生成
    private void generatorService() throws IOException {
        for (DesTable table : desTables) {
            String tableName = table.getName();
            String objectName = camelCase(tableName);
            objectName = upperFirst(objectName);
            File file = new File(dirPath + "com/service/" + objectName + "Service.java");
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            PrintWriter out = new PrintWriter(bw);
            out.println("package com.service;");
            out.println("import java.util.List;");
            out.println("import com.domain." + objectName + ";");
            out.println("import com.dao." + objectName + "Dao;");
            out.println("import org.springframework.beans.factory.annotation.Autowired;");
            out.println("import org.springframework.stereotype.Service;");
            out.println("@Service");
            out.println("public class " + objectName + "Service {");
            //
            //    BlogImageDao blogImageDao;
            out.println("@Autowired");
            out.println("private "+ objectName + "Dao " +camelCase(tableName) +"Dao;");
            createServiceMethodCreate(table, out);
            createServiceMethodSelect(table, out);
            createServiceMethodUpdate(table,out);
            createServiceMethodDelete(table,out);
            out.println("}");
            out.flush();
            out.close();
        }
    }

    private void createServiceMethodCreate(DesTable table, PrintWriter out) {
        String tableName = table.getName();
        String instanceName =camelCase(tableName);
        String objectName = upperFirst(instanceName);
        String daoName =instanceName+"Dao";
        out.println("public int create(" + objectName + " " + camelCase(tableName) + "){");
        out.println("int result = "+daoName+".create("+instanceName+");");
        out.println("return result;");
        out.println("}");
    }

    private void createServiceMethodSelect(DesTable table, PrintWriter out) {
        String tableName = table.getName();
        String instanceName =camelCase(tableName);
        String objectName = upperFirst(instanceName);
        String daoName =instanceName+"Dao";
        String columnName = getIdColumn(table);
        out.println("public List<"+objectName+"> selectList(){");
        out.println("List<"+objectName+"> result = "+daoName+".selectList();");
        out.println("return result;");
        out.println("}");
        out.println("public "+objectName+" selectOne(int "+columnName+"){");
        out.println( objectName+ " "+instanceName+"= "+daoName+".selectOne("+columnName+");");
        out.println("return "+instanceName+";" );
        out.println("}");
    }
    private void createServiceMethodUpdate(DesTable table, PrintWriter out) {
        String tableName = table.getName();
        String instanceName =camelCase(tableName);
        String objectName = upperFirst(instanceName);
        String daoName =instanceName+"Dao";
        out.println("public int update("+objectName+" "+ instanceName+"){");
        out.println("int result = "+daoName+".update("+instanceName+");");
        out.println("return result;");
        out.println("}");
    }
    private void createServiceMethodDelete(DesTable table, PrintWriter out) {
        String tableName = table.getName();
        String instanceName =camelCase(tableName);
        String objectName = upperFirst(instanceName);
        String daoName =instanceName+"Dao";
        String idColunm = getIdColumn(table);
        out.println("public int delete(int "+idColunm+"){");
        out.println("int result = "+daoName+".delete("+idColunm+");");
        out.println("return result;");
        out.println("}");
    }
    private String getIdColumn(DesTable table) {
        List<DesColumn> colomns = table.getColumns();
        String idColunm = "";
        for (DesColumn column : colomns) {
            if (column.getIsAuto()) {
                idColunm = column.getCname();
                break;
            }
        }
        return camelCase(idColunm);
    }
    private void generatorController() throws IOException {
        for (DesTable table : desTables) {
            String tableName = table.getName();
            String objectName = camelCase(tableName);
            objectName = upperFirst(objectName);
            File file = new File(dirPath + "com/controller/" + objectName + "Controller.java");
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            PrintWriter out = new PrintWriter(bw);
            out.println("package com.controller;");
            out.println("import java.util.List;");
            out.println("import com.domain." + objectName + ";");
            out.println("import com.service." + objectName + "Service;");
            out.println("import org.springframework.web.bind.annotation.RequestBody;");
            out.println("import org.springframework.web.bind.annotation.RequestMapping;");
            out.println("import org.springframework.web.bind.annotation.RestController;");
            out.println("import org.springframework.beans.factory.annotation.Autowired;");
            out.println("@RestController");
            out.println("public class " + objectName + "Controller {");
            //
            //    BlogImageDao blogImageDao;
            out.println("@Autowired");
            out.println("private "+ objectName + "Service " +camelCase(tableName) +"Service;");
            createControllerMethodCreate(table, out);
            createControllerMethodSelect(table, out);
            createControllerMethodUpdate(table,out);
            createControllerMethodDelete(table,out);
            out.println("}");
            out.flush();
            out.close();
        }
    }
    private void createControllerMethodCreate(DesTable table, PrintWriter out) {
        String tableName = table.getName();
        String instanceName =camelCase(tableName);
        String objectName = upperFirst(instanceName);
        String servieceName =instanceName+"Service";
        out.println("@RequestMapping(\"/add"+objectName+"\")");
        out.println("public int create("+"@RequestBody " + objectName + " " + camelCase(tableName) + "){");
        out.println("int result = "+servieceName+".create("+instanceName+");");
        out.println("return result;");
        out.println("}");
    }
    private void createControllerMethodSelect(DesTable table, PrintWriter out) {
        String tableName = table.getName();
        String instanceName =camelCase(tableName);
        String objectName = upperFirst(instanceName);
        String serviceName =instanceName+"Service";
        String columnName = getIdColumn(table);
        out.println("@RequestMapping(\"/get"+objectName+"List\")");
        out.println("public List<"+objectName+"> selectList(){");
        out.println("List<"+objectName+"> result = "+serviceName+".selectList();");
        out.println("return result;");
        out.println("}");
        out.println("@RequestMapping(\"/get"+objectName+"\")");
        out.println("public "+objectName+" selectOne(int "+columnName+"){");
        out.println( objectName+ " "+instanceName+"= "+serviceName+".selectOne("+columnName+");");
        out.println("return "+instanceName+";" );
        out.println("}");
    }
    private void createControllerMethodUpdate(DesTable table, PrintWriter out) {
        String tableName = table.getName();
        String instanceName =camelCase(tableName);
        String objectName = upperFirst(instanceName);
        String serviceName =instanceName+"Service";
        out.println("@RequestMapping(\"/edit"+objectName+"\")");
        out.println("public int update("+"@RequestBody "+objectName+" "+ instanceName+"){");
        out.println("int result = "+serviceName+".update("+instanceName+");");
        out.println("return result;");
        out.println("}");
    }
    private void createControllerMethodDelete(DesTable table, PrintWriter out) {
        String tableName = table.getName();
        String instanceName =camelCase(tableName);
        String objectName = upperFirst(instanceName);
        String serviceName =instanceName+"Service";
        String idColunm = getIdColumn(table);
        out.println("@RequestMapping(\"/delete"+objectName+"\")");
        out.println("public int delete(int "+idColunm+"){");
        out.println("int result = "+serviceName+".delete("+idColunm+");");
        out.println("return result;");
        out.println("}");
    }

}
