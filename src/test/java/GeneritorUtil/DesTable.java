package GeneritorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DesTable {
    private String name;
    private ArrayList<DesColumn> columns = new ArrayList<>(16);
    public void addColomn(DesColumn column){
        this.columns.add(column);
    }
}
