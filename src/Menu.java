import javax.swing.*;
import java.awt.*;

public class Menu  extends JFrame{
    JButton lecturer;
    JButton cours;
    JButton classRoom;
    JButton report1;
    JButton report2;
    JButton report3;
    JButton schuedlerbt;
    JTable table ;

    public Menu() throws HeadlessException {
        setSize(1200,600);
        setTitle("Scheduler");
        lecturer = new JButton("LECTURER");
        cours = new JButton("COURSE");
        classRoom = new JButton("CLASSROOM");
        report1 = new JButton("reprot1");
        report2 = new JButton("report2");
        report3 = new JButton("report3");
        schuedlerbt = new JButton("ADD/EDIT/DELETE");

        table = new JTable(2,4);

        lecturer.setBounds(15,30,300,70);
        cours.setBounds(15,100,300,70);
        classRoom.setBounds(15,170,300,70);
        report1.setBounds(15,240,300,70);
        report2.setBounds(15,310,300,70);
        report3.setBounds(15,380,300,70);
        schuedlerbt.setBounds(15,460,300,70);



        add(lecturer);add(cours);add(classRoom);add(report1);add(report2);add(report3);add(schuedlerbt);
        setLayout(new BorderLayout());
        setResizable(false);
        setLayout(new FlowLayout());
        setLayout(null);
        setVisible(true);
    }
}