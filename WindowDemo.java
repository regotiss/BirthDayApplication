//By Sujata Regoti
//Date:24 April 2015
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import static java.awt.GraphicsDevice.WindowTranslucency.*;
import java.text.*; 
public class WindowDemo extends JFrame implements ActionListener
{
	JTextField tf1,tf2,tf3;
	JLabel l1,l;
	int cnt;
	JButton add,update,delete,search,show;
	JComboBox cb,cb1; 
	Statement st;
	Connection conn;
	String month[];
	DateFormatSymbols s;
    public WindowDemo() 
	{
		 super("BirthDay Record");
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			String database ="jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=database/BDay.mdb;";
			Connection conn = DriverManager.getConnection(database, "", "");
			st = conn.createStatement();
		}
		catch (Exception e)
		{
		}
		cnt=1;

		

        //setLayout(new GridBagLayout());
       
		l=new JLabel(new ImageIcon("images/cake1.jpg"));
		
		l1=new JLabel("Name: ");
		l1.setForeground(Color.yellow);
		l1.setFont(new Font("Arial",Font.BOLD,30));
		l1.setBounds(20,50,100,30);
		l.setLayout(null);
		add(l);
		l.add(l1);

		JLabel anim=new JLabel(new ImageIcon("images/anim1.gif"));
		anim.setBounds(450,0,75,75);
		l.add(anim);

		anim=new JLabel(new ImageIcon("images/anim1.gif"));
		anim.setBounds(480,50,75,75);
		l.add(anim);

		anim=new JLabel(new ImageIcon("images/anim1.gif"));

		anim.setBounds(550,0,75,75);
		l.add(anim);

		l1=new JLabel("DOB:");
		l1.setForeground(Color.yellow);
		l1.setFont(new Font("Arial",Font.BOLD,30));
		l1.setBounds(20,150,100,30);
		l.add(l1);
		
		tf1=new JTextField();
		tf1.setBounds(120,50,200,30);
		l.add(tf1);

		tf2=new JTextField();
		tf2.setBounds(120,150,50,30);
		l.add(tf2);

		s=new DateFormatSymbols();
		month=s.getShortMonths();

		cb=new JComboBox(month);
		cb.setBounds(170,150,60,30);
		l.add(cb);

		String choice[]={"By Name","By Date"};
		cb1=new JComboBox(choice);
		cb1.setBounds(10,300,100,30);
		l.add(cb1);


		tf3=new JTextField();
		tf3.setBounds(230,150,80,30);
		l.add(tf3);

		add=new JButton("ADD");
		add.setBounds(10,250,80,30);
		add.addActionListener(this);
		l.add(add);

		update=new JButton("UPDATE");
		update.setBounds(120,250,80,30);
		update.addActionListener(this);
		l.add(update);

		delete=new JButton("DELETE");
		delete.setBounds(230,250,80,30);
		delete.addActionListener(this);
		l.add(delete);

		search=new JButton("SEARCH");
		search.setBounds(130,300,100,30);
		search.addActionListener(this);
		l.add(search);

		show=new JButton("SHOW");
		show.setBounds(250,300,80,30);
		show.addActionListener(this);
		l.add(show);

		setSize(600,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	public void actionPerformed(ActionEvent e)
	{
		Object o=e.getSource();
		if(o==add)
		{
			try
			{
				int j=cb.getSelectedIndex();
				j++;
				String str=tf3.getText()+"-"+j+"-"+tf2.getText();
			
				System.out.println(j+" ");
				String s = "insert into BirthRecord values('"+tf1.getText()+"','"+str+"')";
				st.execute(s);
				JOptionPane.showMessageDialog(this,"Registered Successfully..");
				tf1.setText("");tf2.setText("");tf3.setText("");
			}
			catch (Exception e1)
			{
				JOptionPane.showMessageDialog(this,"Username Already Exist.."+e1);
			}
		}
		else if(o==update)
		{
			try
			{
					
					boolean flg=isPresent();
					if(flg)
					{
						JOptionPane.showMessageDialog(this,"record not found..");
					}
					else
					{
						int j=cb.getSelectedIndex();
						j++;
						String dat=tf3.getText()+"-"+j+"-"+tf2.getText();

						String str = "update BirthRecord set dob='"+dat+"' where Bname='"+tf1.getText()+"'";
						st.execute(str);
						JOptionPane.showMessageDialog(this,"Updated Successfully..");
					}
				}
				catch (SQLException e1)
				{
					System.out.println(e1);	
				}
		}
		else if(o==delete)
		{
				try
				{
					boolean flg=isPresent();
					if(flg)
					{
						JOptionPane.showMessageDialog(this,"record not found..");
					}
					else
					{
						String remove = "Delete from BirthRecord where Bname='"+tf1.getText()+"'";
						st.execute(remove);
						JOptionPane.showMessageDialog(this,"Removed Successfully..");
					}
				}
				catch (Exception e1)
				{
				}
		}
		else if(o==show)
		{
			try
			{
				String str = "SELECT * FROM BirthRecord";
				display(str);
			}
			catch (Exception e1)
			{
			}
		}
		else if(o==search)
		{
			try
			{
				//boolean flg=isPresent();
				/*if(flg)
				{
					JOptionPane.showMessageDialog(this,"record not found..");
				}*/
				
				{
					String str="";
					if(cb1.getSelectedIndex()==0)
					{
						str="select * from BirthRecord where Bname='"+tf1.getText()+"'";
					}
					else
					{
						int j=cb.getSelectedIndex();
						j++;
						if((tf3.getText()).length()>0)
						{
							String dat=tf3.getText()+"-"+j+"-"+tf2.getText();//change
							str="select * from BirthRecord where dob='"+dat+"'";
						}
						else
						{
							int i=Integer.parseInt(tf2.getText());//change
							str="select * from BirthRecord where month(dob)="+j+" and day(dob)="+i;
						}
							
					}
					display(str);
				}
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}

	}
	void display(String str)
	{
		try
		{
				JTable j=new JTable();

				
				st.execute(str);
				ResultSet rs = st.getResultSet();
				
				Vector<Vector> vv=new Vector<Vector>();
				Vector v=new Vector();
				Vector sv=new Vector();
					
				while(rs.next())
				{
					sv.add(""+rs.getString(1));
					sv.add(""+rs.getString(2).substring(0,10));
					vv.add(new Vector(sv));
					sv.clear();
				}
				v.add("Name");
				v.add("Date Of Birth");
				
				j = new JTable(vv,v);
				JScrollPane scrollpane = new JScrollPane(j);
				scrollpane.setBounds(50,250,300,100);
				JFrame f=new JFrame("All Records");
				f.add(scrollpane);
				f.pack();
			
				f.setLocationRelativeTo(null);
				f.setVisible(true);			
		}
		catch (Exception e)
		{
		}
	}
	boolean isPresent() throws SQLException
	{
		String str = "SELECT * FROM BirthRecord";
		st.execute(str);
		ResultSet rs = st.getResultSet();
		boolean flg=true;
		while(rs.next())
		{
			if(tf1.getText().equals(rs.getString(1)))
			{
					flg=false;
			}
		}
		return (flg);
			
	}

    public static void main(String[] args) {
     
        JFrame.setDefaultLookAndFeelDecorated(true);
 
        // Create the GUI on the event-dispatching thread
        //SwingUtilities.invokeLater(new Runnable() {
           // @Override
          //  public void run() {
				WindowDemo tw = new WindowDemo();
 
                // Set the window to 55% opaque (45% translucent).
                //tw.setOpacity(0.1f);
                tw.setVisible(true);
            //}
        //});
    }
}