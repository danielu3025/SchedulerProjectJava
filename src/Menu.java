import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class Menu  extends JFrame{

    private JTable table = new JTable();

    private DefaultTableModel model = new DefaultTableModel();

    private Object[] columns = {"Name", "Unit", "Code", "Country", "Rate", "Change"};


    private TableRowSorter<TableModel> rowSorter;

    Object[] row = new Object[6];
    private String[][] data;

    public Menu() throws HeadlessException {
        model.setColumnIdentifiers(columns);
        table.setModel(model);

        table.setRowHeight(30);


        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(10, 10, 682, 200);

        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon("./img/tableImg.png")));
        setLayout(new FlowLayout());
        setResizable(false);
        setLayout(null);
        setSize(700,430);
        setTitle("Table");
        setLocationRelativeTo(null);

        add(pane);

        data = new String[1][6];
            data[0][0] = "test";
            data[0][1] = "test";
            data[0][2] = "test";
            data[0][3] = "test";
            data[0][4] = "test";
            data[0][5] = "test";


        model = new DefaultTableModel(data,columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };

        table.setModel(model);

        setVisible(true);

    }
}