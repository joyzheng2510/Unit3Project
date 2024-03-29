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
  private static JLabel formatError;
  private static JLabel taskLabel;
  private static JLabel noTask;
  private static JTextField taskEnter;
  private static JLabel list;
  private static JButton enterData;
  private static JButton deleteButton;
  static String listItems = "";
  public static void main(String[] args) {    
    JFrame frame = new JFrame("To-do List");
    frame.setSize(500, 600);
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

    instructions = new JLabel("<html>To make a to-do entry, enter the date and task below. <br><font color='red'>**MAKE SURE SINGLE DIGITS ARE PRECEDED BY A ZERO. </font><br>To delete a task, enter in the date and task of the entry you want to delete.</html>");
    instructions.setBounds(10, 5, 500, 90);
    instructions.setVerticalAlignment(JLabel.TOP);
    panel.add(instructions);
    
    dateLabel = new JLabel("Date to be completed: MM/DD/YYYY");
    dateLabel.setBounds(10,70,300,25);
    panel.add(dateLabel);

    slash = new JLabel("       /         /");
    slash.setBounds(10,100,300,25);
    panel.add(slash);
    monthField = new JTextField(10);
    monthField.setBounds(10,100,25,25);
    panel.add(monthField);
    dayField = new JTextField(10);
    dayField.setBounds(50,100,25,25);
    panel.add(dayField);
    yearField = new JTextField(10);
    yearField.setBounds(90,100,40,25);
    panel.add(yearField);

    formatError = new JLabel("");
    formatError.setBounds(140,100,350,25);
    panel.add(formatError);

    taskLabel = new JLabel("Task to be completed");
    taskLabel.setBounds(10,130,200,25);
    panel.add(taskLabel);

    taskEnter = new JTextField(20);
    taskEnter.setBounds(10,160,200,25);
    panel.add(taskEnter);

    noTask = new JLabel("");
    noTask.setBounds(220,160,200,25);
    panel.add(noTask);

    enterData = new JButton("Enter");
    enterData.setBounds(10, 190, 80, 25);
    enterData.addActionListener(
      new ActionListener(){
        public void actionPerformed(ActionEvent e){
          String data = monthField.getText()+","+dayField.getText()+","+yearField.getText()+","+taskEnter.getText().trim();

          if(monthField.getText().length()==2 && dayField.getText().length()==2 && yearField.getText().length()==4 && taskEnter.getText().length()>0){
            writeToFile(data);
            append();
            formatError.setText("");
            noTask.setText("");
          } else if(!(monthField.getText().length()==2 || dayField.getText().length()==2 || yearField.getText().length()==4) && taskEnter.getText().length()>0) {
            formatError.setText("Please enter the date in the correct format.");
            noTask.setText("");
          } else if(!(monthField.getText().length()==2 || dayField.getText().length()==2 || yearField.getText().length()==4 && taskEnter.getText().length()>0)) {
            formatError.setText("Please enter the date in the correct format.");
            noTask.setText("Please enter a task.");
          } else if(!(taskEnter.getText().length()>0)){
            formatError.setText("");
            noTask.setText("Please enter a task.");
          } 
        }
      }
    );
    panel.add(enterData);
    
    deleteButton = new JButton("Delete");
    deleteButton.setBounds(100, 190, 110, 25);
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
          if(monthField.getText().length()==2 && dayField.getText().length()==2 && yearField.getText().length()==4 && taskEnter.getText().length()>0){
            append();
            formatError.setText("");
            noTask.setText("");
          } else if(!(monthField.getText().length()==2 || dayField.getText().length()==2 || yearField.getText().length()==4) && taskEnter.getText().length()>0) {
            formatError.setText("Please enter the date in the correct format.");
            noTask.setText("");
          } else if(!(monthField.getText().length()==2 || dayField.getText().length()==2 || yearField.getText().length()==4 && taskEnter.getText().length()>0)) {
            formatError.setText("Please enter the date in the correct format.");
            noTask.setText("Please enter a task.");
          } else if(!(taskEnter.getText().length()>0)){
            formatError.setText("");
            noTask.setText("Please enter a task.");
          } 
        }
      }
    );
    panel.add(deleteButton);
    
    list = new JLabel("");
    list.setBounds(10,220,300,200);
    list.setVerticalAlignment(JLabel.TOP);
    panel.add(list);
  }

  @Override
  public void actionPerformed(ActionEvent e){
    
  }
  
  public static void printOut() {
    try {
      File file = new File("data.txt");
      Scanner scan = new Scanner(file);
      if (!(file.length() == 0)) {
        ArrayList<String> tempList = new ArrayList<>();
        while (scan.hasNextLine()) {
          String data = scan.nextLine();
          ArrayList<String> dataArray = new ArrayList<>(Arrays.asList(data.split(",")));
          String month = dataArray.get(0);
          String day = dataArray.get(1);
          String year = dataArray.get(2);
          String task = dataArray.get(3);
          tempList.add(year + month + day + ": " + task.trim());
        }
        Collections.sort(tempList);
        for (String item : tempList) {
          String formattedDate = item.substring(4, 6) + "/" + item.substring(6, 8) + "/" + item.substring(0, 4);
          listItems += formattedDate + ": " + item.substring(10) + "<br>";
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
      if (!(file.length() == 0)) {
        ArrayList<String> tempList = new ArrayList<>();
        while (scan.hasNextLine()) {
          String data = scan.nextLine();
          ArrayList<String> dataArray = new ArrayList<>(Arrays.asList(data.split(",")));
          String month = dataArray.get(0);
          String day = dataArray.get(1);
          String year = dataArray.get(2);
          String task = dataArray.get(3);
          tempList.add(year + month + day + ": " + task.trim());
        }
        Collections.sort(tempList);
        for (String item : tempList) {
          String formattedDate = item.substring(4, 6) + "/" + item.substring(6, 8) + "/" + item.substring(0, 4);
          listItems += formattedDate + ": " + item.substring(10) + "<br>";
        }
        list.setText("<html>" + listItems + "</html>");
      }
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
