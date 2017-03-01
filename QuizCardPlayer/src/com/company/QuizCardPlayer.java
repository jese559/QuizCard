package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class QuizCardPlayer {
    private JTextArea question;
    private JTextArea answer;
    private JButton showAns;
    private JScrollPane ansScroll;
    private JFrame frame;
    private QuizCard currentCard;
    private ArrayList<QuizCard> cardArrayList;
    private int currentCardIndex;
    private boolean isShowAnswer;

    public static void main(String[] args) {
	// write your code here
        QuizCardPlayer main = new QuizCardPlayer();
        main.Go();
    }

    public void Go() {
        frame = new JFrame("Quiz Card Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Font font = new Font("sanserif", Font.BOLD, 24);
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

        showAns = new JButton("Show answer");
        showAns.addActionListener(new showAnswerListener());
        backPanel.add(showAns);

        answer = new JTextArea(6,20);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setFont(font);
        answer.setEditable(false);
        ansScroll = new JScrollPane(answer);
        ansScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ansScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        ansScroll.setVisible(false);
        backPanel.add(ansScroll);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItemMenu = new JMenuItem("Load file");
        saveItemMenu.addActionListener(new loadFileListener());
        fileMenu.add(saveItemMenu);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
        frame.setSize(480,310);
        frame.getContentPane().add(BorderLayout.CENTER, backPanel);
    }

    public class loadFileListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(frame);
            loadFile(fileOpen.getSelectedFile());
        }
    }

    public class showAnswerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (isShowAnswer) {
                ansScroll.setVisible(true);
                frame.setSize(480,520);
                answer.setText(currentCard.getAnswer());
                showAns.setText("Next card");
                isShowAnswer = false;
            } else if(currentCardIndex < cardArrayList.size()) {
                showCard();
                notVisible();
            } else {
                question.setText("No more cards!");
                notVisible();
                showAns.setEnabled(false);
            }
        }
    }

    public void notVisible() {
        ansScroll.setVisible(false);
        frame.setSize(480,310);
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
        showCard();
    }

    public void makeCard(String line) {
        String[] params = line.split("/");
        QuizCard card = new QuizCard(params[0], params[1]);
        cardArrayList.add(card);
        System.out.println("Made a card");
    }

    public void showCard() {
        currentCard = cardArrayList.get(currentCardIndex);
        currentCardIndex++;
        question.setText(currentCard.getQuestion());
        showAns.setText("Show answer");
        isShowAnswer = true;
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
