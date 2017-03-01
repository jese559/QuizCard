package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuizCardPlayer_score {
    private JTextArea question;
    private JTextArea answer;
    private JButton showAns;
    private JScrollPane ansScroll;
    private JFrame frame;
    private QuizCard currentCard;
    private ArrayList<QuizCard> cardArrayList;
    private int currentCardIndex;
    private int rightCount, allCount;
    private Date date;
    private SimpleDateFormat currentDate;

    public static void main(String[] args) {
	// write your code here
        QuizCardPlayer_score main = new QuizCardPlayer_score();
        main.Go();
    }

    public void Go() {
        date = new Date();
        currentDate = new SimpleDateFormat("ddMMyyyy_hhmmss");

        frame = new JFrame("Quiz Card Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Font font = new Font("tahoma", Font.BOLD, 24);
        JPanel backPanel = new JPanel();
        frame.getContentPane().add(BorderLayout.CENTER, backPanel);

        JLabel quesLabel = new JLabel("Question");
        backPanel.add(quesLabel);

        question = new JTextArea(6,20);
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setFont(font);
        question.setEditable(false);
        JScrollPane quesScroll = new JScrollPane(question);
        quesScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        quesScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        backPanel.add(quesScroll);

        showAns = new JButton("Next question");
        showAns.addActionListener(new answerListener());
        backPanel.add(showAns);

        /*JLabel ansLabel = new JLabel("Answer");
        backPanel.add(ansLabel);*/

        answer = new JTextArea(6,20);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setFont(font);
        answer.setEditable(true);
        ansScroll = new JScrollPane(answer);
        ansScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ansScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        ansScroll.setVisible(true);
        backPanel.add(ansScroll);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItemMenu = new JMenuItem("Load file");
        saveItemMenu.addActionListener(new loadFileListener());
        fileMenu.add(saveItemMenu);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
        frame.setSize(500,480);
        frame.getContentPane().add(BorderLayout.CENTER, backPanel);
    }

    public class loadFileListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(frame);
            loadFile(fileOpen.getSelectedFile());
        }
    }

    public class answerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String result = answer.getText();
            if (result.equals(currentCard.getAnswer())) {
                saveToLog("Right!");
                //System.out.println("Right!");
                answer.setText("");
                rightCount++;
            } else {
                saveToLog("Fail!");
                //System.out.println("Fail!");
                answer.setText("");
            }
            if(currentCardIndex < allCount)
                showCard();
            else {
                question.setText("You answered " + rightCount + " questions out of " + allCount + ". You have " + ((float)rightCount/allCount)*100 + "% right answers.");
                /*System.out.println("You answered " + rightCount + " questions out of " + allCount);
                System.out.println(". You have " + ((float)rightCount/allCount)*100 + "% right answers");*/
                saveToLog(question.getText());
                showAns.setEnabled(false);
            }
        }
    }

    public void loadFile(File file) {
        cardArrayList = new ArrayList<QuizCard>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = in.readLine()) != null)
                makeCard(line);
            in.close();
        } catch(Exception e) {}
        allCount = cardArrayList.size();
        showCard();
    }

    public void saveToLog(String text) {
        Date date2 = new Date();
        SimpleDateFormat currentDate2 = new SimpleDateFormat("dd.MM.yyyy_hh:mm:ss");

        try {
            File file = new File(currentDate.format(date) + ".txt");
            file.createNewFile();

            FileWriter out = new FileWriter(file.getAbsoluteFile(), true);
            try {
                out.append(text + " Timestamp[" + currentDate2.format(date2) + "]" + '\n');
            } finally {
                out.close();
            }
        } catch(IOException e) {}
    }

    public void makeCard(String line) {
        String[] params = line.split("/");
        QuizCard card = new QuizCard(params[0], params[1]);
        cardArrayList.add(card);
        saveToLog("Made a card.");
    }

    public void showCard() {
        currentCard = cardArrayList.get(currentCardIndex);
        currentCardIndex++;
        question.setText(currentCard.getQuestion());
    }

    public class QuizCard {
        private String question;
        private String answer;

        QuizCard(String q, String a) {
            this.question = q;
            this.answer = a;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
