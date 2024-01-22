import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class Main implements ActionListener {
  private static JLabel instructions;
  private static JLabel dateLabel; 
  private static JLabel slash;
  private static JTextField monthField;
  private static JTextField dayField;
  private static JTextField yearField;
  private static JLabel taskLabel;
  private static JTextField taskEnter;
  private static JLabel list;
  private static JButton enterData;
  private static JButton deleteButton;
  static String listItems = "";
  public static void main(String[] args) {    
    JFrame frame = new JFrame("To-do List");
    frame.setSize(500, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel panel = new JPanel();    
    frame.add(panel);
    placeComponents(panel);
    frame.setVisible(true);

    File file = new File("data.txt");
    if (!(file.exists())) {
      try {
        file.createNewFile();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    printOut();
  }

  private static void placeComponents(JPanel panel) {
    panel.setLayout(null);

    instructions = new JLabel("<html>To make a to-do entry, enter the date and task below. To delete a task, enter in the date and task of the entry you want to delete.</html>");
    instructions.setBounds(10, 10, 500, 90);
    instructions.setVerticalAlignment(JLabel.TOP);
    panel.add(instructions);
    
    dateLabel = new JLabel("Date to be completed: MM/DD/YYYY");
    dateLabel.setBounds(10,50,300,25);
    panel.add(dateLabel);

    slash = new JLabel("       /         /");
    slash.setBounds(10,80,300,25);
    panel.add(slash);
    monthField = new JTextField(10);
    monthField.setBounds(10,80,25,25);
    panel.add(monthField);
    dayField = new JTextField(10);
    dayField.setBounds(50,80,25,25);
    panel.add(dayField);
    yearField = new JTextField(10);
    yearField.setBounds(90,80,40,25);
    panel.add(yearField);

    taskLabel = new JLabel("Task to be completed");
    taskLabel.setBounds(10,110,200,25);
    panel.add(taskLabel);

    taskEnter = new JTextField(20);
    taskEnter.setBounds(10,140,200,25);
    panel.add(taskEnter);

    enterData = new JButton("Enter");
    enterData.setBounds(10, 170, 80, 25);
    enterData.addActionListener(
      new ActionListener(){
        public void actionPerformed(ActionEvent e){
          String data = monthField.getText()+","+dayField.getText()+","+yearField.getText()+","+taskEnter.getText().trim();
          writeToFile(data);

          append();
        }
      }
    );
    panel.add(enterData);
    
    deleteButton = new JButton("Delete");
    deleteButton.setBounds(100, 170, 110, 25);
    deleteButton.addActionListener(
      new ActionListener(){
        public void actionPerformed(ActionEvent e){
          File inputFile = new File("data.txt");
          File tempFile = new File("temp.txt");

          try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String lineToRemove = monthField.getText() + "," + dayField.getText() + "," + yearField.getText() + "," + taskEnter.getText().trim();
            String currentLine;
            while ((currentLine = reader.readLine()) != null){
              if (currentLine.equals(lineToRemove)){
                continue;
              }
              writer.write(currentLine + "\n");
            }
            writer.close();
            reader.close();
            tempFile.renameTo(inputFile);
          } catch (IOException e1) {
            e1.printStackTrace();
          }
          append();
        }
      }
    );
    panel.add(deleteButton);
    
    list = new JLabel("");
    list.setBounds(10,200,300,200);
    list.setVerticalAlignment(JLabel.TOP);
    panel.add(list);
  }

  @Override
  public void actionPerformed(ActionEvent e){
    
  }
  
  public static void printOut(){
    try {
      File file = new File("data.txt");
      Scanner scan = new Scanner(file);
      if (!(file.length()==0)) {
        ArrayList<String> tempList = new ArrayList<>();
        while (scan.hasNextLine()){
          String data = scan.nextLine();
          ArrayList<String> dataArray = new ArrayList<>(Arrays.asList(data.split(",")));
          String month = dataArray.get(0);
          String day = dataArray.get(1);
          String year = dataArray.get(2);
          String task = dataArray.get(3);
          tempList.add(month+"/"+day+"/"+year+": "+task.trim());
        }
        Collections.sort(tempList);
        for (String item : tempList) {
          listItems += item + "<br>";
        }
        list.setText("<html>" + listItems + "</html>");
      }
      scan.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public static void append(){
    try {
      File file = new File("data.txt");
      Scanner scan = new Scanner(file);
      listItems = "";
      ArrayList<String> tempList = new ArrayList<>();
      while (scan.hasNextLine()){
        String data = scan.nextLine();
        ArrayList<String> dataArray = new ArrayList<>(Arrays.asList(data.split(",")));
        String month = dataArray.get(0);
        String day = dataArray.get(1);
        String year = dataArray.get(2);
        String task = dataArray.get(3);
        tempList.add(month+"/"+day+"/"+year+": "+task.trim());
      }
      Collections.sort(tempList);
      for (String item : tempList) {
        listItems += item + "<br>";
      }
      list.setText("<html>" + listItems + "</html>");
      scan.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public static void writeToFile(String data){
    try{
      FileWriter writer = new FileWriter("data.txt", true);
      writer.write(data + "\n");
      writer.close();
    }
    catch(IOException e){
      e.printStackTrace();
    } 
  }
}
