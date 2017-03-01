import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import java.awt.*;

/**
 * Created by n7701-00-134 on 28.12.2016.
 */
public class QuizCardBuilder {
    private JTextArea question;
    private JTextArea answer;
    private JFrame frame;
    private ArrayList<QuizCard> cardList;

    public static void main(String[] args) {
        QuizCardBuilder builder = new QuizCardBuilder();
        builder.go();
    }

    public void go() {
        frame = new JFrame("Quiz Card Builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        Font bigFont = new Font("tahoma", Font.BOLD, 24);

        question = new JTextArea(6,20);
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setFont(bigFont);
        JScrollPane queScroll = new JScrollPane(question);
        queScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        queScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        answer = new JTextArea(6,20);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setFont(bigFont);
        JScrollPane ansScroll = new JScrollPane(answer);
        ansScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        ansScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JButton nextButton = new JButton("Next Card");
        nextButton.addActionListener(new NextCardListener());
        cardList = new ArrayList<QuizCard>();
        JLabel qLabel = new JLabel("Question");
        JLabel aLabel = new JLabel("Answer");

        panel.add(qLabel);
        panel.add(queScroll);
        panel.add(aLabel);
        panel.add(ansScroll);
        panel.add(nextButton);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItemMenu = new JMenuItem("Save");
        saveItemMenu.addActionListener(new SaveListener());
        fileMenu.add(saveItemMenu);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setSize(480,550);
        frame.setVisible(true);
    }

    public class NextCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isEmpty())
                addToCardList();
        }
    }

    public class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isEmpty()) {
                addToCardList();
                JFileChooser fileSave = new JFileChooser();
                fileSave.showSaveDialog(frame);
                saveFile(fileSave.getSelectedFile());
            }
        }
    }

    private boolean isEmpty() {
        if ((question.getText().isEmpty()) && (answer.getText().isEmpty())) {
            return true;
        } else
            return false;
    }

    private void addToCardList() {
        QuizCard card = new QuizCard(question.getText(), answer.getText());
        cardList.add(card);
        clearCard();
    }

    private void saveFile(File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for(QuizCard card:cardList) {
                if(!(card.getQuestion().isEmpty()) && (!card.getAnswer().isEmpty())) {
                    writer.write(card.getQuestion() + "/");
                    writer.write(card.getAnswer() + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    private void clearCard() {
        question.setText("");
        answer.setText("");
        question.requestFocus();
    }

    public class QuizCard {
        private String question = null;
        private String answer = null;

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