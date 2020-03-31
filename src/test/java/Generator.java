import GeneritorUtil.ConnectionPool;

import GeneritorUtil.GeneratorCodeFactory;
public class Generator {
    public static void main(String[] args) throws Exception {
        String url ="jdbc:mysql://localhost:3306/emp?serverTimezone=UTC&useUnicode=true&useSSL=false&characterEncoding=utf8";
        String userName = "root";
        String password = "***";
        String baseName = "emp";
       ConnectionPool pool = new ConnectionPool(url,userName,password);
       GeneratorCodeFactory factory = new GeneratorCodeFactory(pool,baseName);

    }

}
